package ru.vk.education.job.entity;

import java.util.Set;

public class Job {
    private String jobName;

    private String companyName;

    private Set<String> tags;

    private int exp;

    public Job(String jobName, String companyName, Set<String> tags, int exp) {
        this.jobName = jobName;
        this.companyName = companyName;
        this.tags = tags;
        this.exp = exp;
    }

    public void print() {
        System.out.println(jobName + " at " + companyName);
    }

    public long countMatchingTags(Set<String> skills) {
        return  tags.stream()
                .filter(skills::contains)
                .count();
    }

    public String getJobName() {
        return jobName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public int getExp() {
        return exp;
    }
}
