package com.example.reactiveexample;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Line {
    @Id
    private int id;
    private String text;

    public Line() { }

    public Line(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Line{" +
                "id=" + id +
                ", text='" + text + '\'' +
                '}';
    }
}
