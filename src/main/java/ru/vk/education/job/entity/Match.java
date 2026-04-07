package ru.vk.education.job.entity;

public class Match {
    private final User user;

    private final Job job;

    private final long matchingTagsNumber;

    private Double matchRate = null;

    public Match(User user, Job job) {
        this.user = user;
        this.job = job;
        this.matchingTagsNumber = job.countMatchingTags(user.getSkills());
    }

    private void computeRate() {
        double matchRate = matchingTagsNumber;

        if (user.getExp() < job.getExp())
            matchRate /= 2;

        this.matchRate = matchRate;
    }

    public double getMatchRate() {
        if(matchRate == null) {
            computeRate();
        }
        return matchRate;
    }

    public void printJobInfo() {
        job.print();
    }

    public long getMatchingTagsNumber() { return matchingTagsNumber; }
}
