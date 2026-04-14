package ru.vk.education.job.service;

import ru.vk.education.job.entity.Job;
import ru.vk.education.job.entity.Match;
import ru.vk.education.job.entity.User;

import java.util.Comparator;
import java.util.Map;

public class BestJobSuggester implements Runnable {
    private final Map<String, User> users;
    private final Map<String, Job> jobs;

    public BestJobSuggester(Map<String, User> users, Map<String, Job> jobs) {
        this.users = users;
        this.jobs = jobs;
    }

    @Override
    public void run() {
        users.values().forEach(this::printBestJobForUser);
    }

    private void printBestJobForUser(User user) {
        jobs.values().stream()
                .map(job -> new Match(user, job))
                .max(Comparator.comparing(Match::getMatchRate))
                .filter(match -> match.getMatchRate() > 0)
                .ifPresent(match -> System.out.printf(
                        "%s, лучшее предложение — %s в %s%n",
                        user.getName(),
                        match.getJob().getJobName(),
                        match.getJob().getCompanyName()
                ));
    }
}
