package com.example.questapp.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.questapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResultFragment extends Fragment {

    private NavController navController;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private String currentUserId;

    private String quizId;


    private ProgressBar results_progress;
    private TextView
            results_percent, results_correct_text,
            results_wrong_text,
            results_missed_text;
    private Button results_home_btn;

    public ResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUI(view);
        //firebase, navicontronller
        navController = Navigation.findNavController(view);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            currentUserId = firebaseAuth.getCurrentUser().getUid();
        } else {
            //do somethingszzzzz
        }

        if (getArguments() != null) {
            quizId = ResultFragmentArgs.fromBundle(getArguments()).getQuizId();
        }

        firebaseFirestore.collection("quizz")
                .document(quizId)
                .collection("results")
                .document(currentUserId)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot result = task.getResult();

                    Long correct = result.getLong("correct");
                    Long wrong = result.getLong("wrong");
                    Long missed = result.getLong("unAnswered");

                    results_correct_text.setText(correct.toString());
                    results_wrong_text.setText(wrong.toString());
                    results_missed_text.setText(missed.toString());

                    //Calculate Progress
                    Long total = correct + wrong + missed;
                    Long percent = (correct * 100) / total;

                    results_percent.setText(percent + "%");
                    results_progress.setProgress(percent.intValue());
                }
            }
        });
        results_home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_resultFragment_to_ListFragment);
            }
        });

    }

    private void initUI(View view) {
        results_progress = view.findViewById(R.id.results_progress);
        results_percent = view.findViewById(R.id.results_percent);
        results_correct_text = view.findViewById(R.id.results_correct_text);
        results_missed_text = view.findViewById(R.id.results_missed_text);
        results_wrong_text = view.findViewById(R.id.results_wrong_text);
        results_home_btn = view.findViewById(R.id.results_home_btn);
    }

    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
        super.onAttachFragment(childFragment);


    }


}
