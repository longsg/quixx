package com.example.questapp.views.model;

import com.google.firebase.firestore.DocumentId;

public class Quizz {
    @DocumentId
    private String quizzId;
    private String desc, name, lvl, image, visibility;
    private long questions;


    public Quizz() {
    }

    public Quizz(String quizzId, String desc, String name, String lvl, String image, String visibility, long questions) {
        this.quizzId = quizzId;
        this.desc = desc;
        this.name = name;
        this.lvl = lvl;
        this.image = image;
        this.visibility = visibility;
        this.questions = questions;
    }

    public String getQuizzId() {
        return quizzId;
    }

    public void setQuizzId(String quizzId) {
        this.quizzId = quizzId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLvl() {
        return lvl;
    }

    public void setLvl(String lvl) {
        this.lvl = lvl;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public long getQuestions() {
        return questions;
    }

    public void setQuestions(long questions) {
        this.questions = questions;
    }
}
