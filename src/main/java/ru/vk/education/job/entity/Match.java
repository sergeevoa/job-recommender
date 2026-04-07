package ru.vk.education.job.entity;

public class Match {
    private final User user;

    private final Job job;

    private final double matchRate;

    public Match(User user, Job job) {
        this.user = user;
        this.job = job;
        this.matchRate = computeRate();
    }

    private double computeRate() {
        double matchRate = job.countMatchingTags(user.getSkills());

        if (user.getExp() < job.getExp())
            matchRate /= 2;

        return matchRate;
    }

    public double getMatchRate() {
        return matchRate;
    }

    public void printJobInfo() {
        job.print();
    }

    public boolean isMatch() {
        return matchRate > 0;
    }
}
