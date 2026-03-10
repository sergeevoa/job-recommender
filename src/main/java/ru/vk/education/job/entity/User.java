package ru.vk.education.job.entity;

import java.util.Set;

public class User {
    private String name;

    private Set<String> skills;

    private int exp;

    public User(String name, Set<String> skills, int exp) {
        this.name = name;
        this.skills = skills;
        this.exp = exp;
    }

    public void print() {
        System.out.println(name + " " +
                String.join(",", skills) + " " + exp);
    }

    public Set<String> getSkills() {
        return skills;
    }

    public int getExp() {
        return exp;
    }
}
