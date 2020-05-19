package com.example.questapp.views.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.questapp.R;
import com.example.questapp.views.model.Questions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuizzDetail extends Fragment {
    private static final String TAG = "QuizzDetail";
    private FirebaseFirestore firebaseFirestore;
    private String quizzId;

    private List<Questions> questionsList = new ArrayList<>();
    private List<Questions> answerList = new ArrayList<>();
    //
    private TextView quiz_title;
    public QuizzDetail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quizz_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        quiz_title = view.findViewById(R.id.quiz_title);
        if (getArguments() != null) {
            quizzId = QuizzDetailArgs.fromBundle(getArguments()).getQuizzId();
        }
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("quizz").document(quizzId).collection("questions").get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            questionsList = Objects.requireNonNull(task.getResult()).toObjects(Questions.class);
                            Log.d(TAG, "onComplete: " + questionsList.get(0));
                        } else {
                            Log.d(TAG, "onComplete: " + task.getException());
                            quiz_title.setText(task.getException() + "");
                        }
                    }
                }
        );
    }
}
