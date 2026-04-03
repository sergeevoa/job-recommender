package ru.vk.education.job;

import ru.vk.education.job.entity.Job;
import ru.vk.education.job.entity.Match;
import ru.vk.education.job.entity.User;
import ru.vk.education.job.service.FileService;

import java.io.IOException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {
    private static final Pattern ADD_USER_COMMAND_PATTERN = Pattern.compile(
            "^user\\s(\\w+)(\\s--\\w+=\\S+)+$"
    );

    private static final Pattern SKILLS_PATTERN = Pattern.compile(
            "--skills=(\\S+(?:,\\S+)*)"
    );

    private static final Pattern EXP_PATTERN = Pattern.compile(
      "--exp=(\\d+)"
    );

    private static final String USER_LIST_COMMAND = "user-list";

    private static final Pattern ADD_JOB_COMMAND_PATTERN = Pattern.compile(
            "^job\\s(\\w+)(\\s--\\w+=\\S+)+$"
    );

    private static final Pattern TAGS_PATTERN = Pattern.compile(
            "--tags=(\\S+(?:,\\S+)*)"
    );

    private static final Pattern COMPANY_PATTERN = Pattern.compile(
            "--company=(\\w+)"
    );

    private static final String JOB_LIST_COMMAND = "job-list";

    private static final Pattern SUGGEST_COMMAND_PATTERN = Pattern.compile(
            "^suggest\\s(\\w+)$"
    );

    private static final String HISTORY_COMMAND = "history";

    private static final String EXIT_COMMAND = "exit";

    private static final Map<String, User> users = new LinkedHashMap<>();

    private static final Map<String, Job> jobs = new LinkedHashMap<>();

    private static final FileService fileService = new FileService();

    public static void main(String[] args) throws IOException {
        fileService.getCommandLog().stream()
                .filter(command -> ADD_USER_COMMAND_PATTERN.matcher(command).matches()
                || ADD_JOB_COMMAND_PATTERN.matcher(command).matches())
                .forEach(command -> {
                    try {
                        execute(command, true);
                    } catch (IOException e) {
                        throw new IllegalArgumentException(e);
                    }
                });

        Scanner sc = new Scanner(System.in);

        while(true) {
            execute(sc.nextLine(), false);
        }
    }

    private static Set<String> findMatchSet(Pattern pattern, String command) {
        Matcher skillsMatcher = pattern.matcher(command);

        if(skillsMatcher.find()) {
            return Arrays.stream(skillsMatcher.group(1).split(","))
                    .collect(Collectors.toCollection(TreeSet::new));
        } else {
            throw new IllegalArgumentException();
        }
    }

    private static int findMatchExp(String command) {
        Matcher expMatcher = EXP_PATTERN.matcher(command);

        if(expMatcher.find()) {
            return Integer.parseInt(expMatcher.group(1));
        } else {
            throw new IllegalArgumentException();
        }
    }

    private static void execute(String command, boolean fromLog) throws IOException {
        switch (command) {
            case USER_LIST_COMMAND -> {
                users.values().forEach(User::print);
                if(!fromLog)
                    fileService.logCommand(command);
            }

            case JOB_LIST_COMMAND -> {
                jobs.values().forEach(Job::print);
                if(!fromLog)
                    fileService.logCommand(command);
            }

            case HISTORY_COMMAND -> {
                fileService.getCommandLog().forEach(System.out::println);
                if(!fromLog)
                    fileService.logCommand(command);
            }

            case EXIT_COMMAND -> System.exit(0);


            default -> {
                Matcher userMatcher = ADD_USER_COMMAND_PATTERN.matcher(command);

                if(userMatcher.matches()) {
                    String name = userMatcher.group(1);

                    Set<String> skills = findMatchSet(SKILLS_PATTERN, command);

                    int exp = findMatchExp(command);

                    users.putIfAbsent(name, new User(name, skills, exp));

                    if(!fromLog)
                        fileService.logCommand(command);

                } else {
                    Matcher jobMatcher = ADD_JOB_COMMAND_PATTERN.matcher(command);

                    if(jobMatcher.matches()) {
                        String name = jobMatcher.group(1);

                        Matcher companyMatcher = COMPANY_PATTERN.matcher(command);

                        String company;

                        if(companyMatcher.find()) {
                            company = companyMatcher.group(1);
                        } else {
                            throw new IllegalArgumentException();
                        }

                        Set<String> tags = findMatchSet(TAGS_PATTERN, command);

                        int exp = findMatchExp(command);

                        jobs.putIfAbsent(name, new Job(name, company, tags, exp));

                        if(!fromLog)
                            fileService.logCommand(command);

                    } else {
                        Matcher suggestMatcher = SUGGEST_COMMAND_PATTERN.matcher(command);

                        if(suggestMatcher.matches()) {
                            String username = suggestMatcher.group(1);

                            User user = users.get(username);
                            if (user == null) break;

                            List<Match> matches = new ArrayList<>();

                            for(Job job : jobs.values()) {
                                matches.add(new Match(user, job));
                            }

                            matches.sort(Comparator.comparing(Match::getMatchRate).reversed());

                            matches.stream()
                                    .limit(2)
                                    .forEach(Match::printJobInfo);

                            if(!fromLog)
                                fileService.logCommand(command);

                        } else {
                            throw new IllegalArgumentException();
                        }
                    }

                }
            }
        }
    }
}