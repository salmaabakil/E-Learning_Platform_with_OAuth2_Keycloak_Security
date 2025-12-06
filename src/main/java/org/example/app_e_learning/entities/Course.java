package org.example.app_e_learning.entities;

public class Course {
    private Long id;
    private String title;

    public Course(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
}

