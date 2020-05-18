package com.example.questapp.views;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.questapp.views.firebasehelper.FirebaseComplete;
import com.example.questapp.views.firebasehelper.FirebaseRepository;
import com.example.questapp.views.model.Quizz;

import java.util.List;

public class QuizzViewModel extends ViewModel implements FirebaseComplete {
    private static final String TAG = "QuizzViewModel";


    private FirebaseRepository firebaseRepository = new FirebaseRepository(this);
    private MutableLiveData<List<Quizz>> quizzMutableLiveData = new MutableLiveData<>();

    public QuizzViewModel() {
        firebaseRepository.getData();
    }

    //mutableLive extend LiveData
    // setValue must call Main thread
    // postValue must call Background Thread
    public LiveData<List<Quizz>> getQuizzMutableLiveData() {
        return quizzMutableLiveData;
    }
//
//    public void setQuizzMutableLiveData(MutableLiveData<List<Quizz>> quizzMutableLiveData) {
//        this.quizzMutableLiveData = quizzMutableLiveData;
//    }

    @Override
    public void onFirebaseComplete(List<Quizz> quizzList) {
        quizzMutableLiveData.setValue(quizzList);
    }

    @Override
    public void onFailed(Exception e) {
        Log.d(TAG, "onFailed: " + e.getMessage());
    }
}
