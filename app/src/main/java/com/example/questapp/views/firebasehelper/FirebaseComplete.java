package com.example.questapp.views.firebasehelper;

import com.example.questapp.views.model.Quizz;

import java.util.List;

public interface FirebaseComplete {
    void onFirebaseComplete(List<Quizz> quizzList);

    void onFailed(Exception e);
}
