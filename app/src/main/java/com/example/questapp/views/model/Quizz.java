package com.example.questapp.views.model;

import com.google.firebase.firestore.DocumentId;

public class Quizz {
    @DocumentId
    private String quizz_id;
    private String desc, name, lvl, image, visibility;
    private long question;


    public Quizz() {
    }

    public Quizz(String quizz_id, String desc, String name, String lvl, String image, String visibility, long question) {
        this.quizz_id = quizz_id;
        this.desc = desc;
        this.name = name;
        this.lvl = lvl;
        this.image = image;
        this.visibility = visibility;
        this.question = question;
    }

    public String getQuizz_id() {
        return quizz_id;
    }

    public void setQuizz_id(String quizz_id) {
        this.quizz_id = quizz_id;
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

    public long getQuestion() {
        return question;
    }

    public void setQuestion(long question) {
        this.question = question;
    }
}
