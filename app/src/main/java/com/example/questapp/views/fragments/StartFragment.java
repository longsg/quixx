package com.example.questapp.views.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Process;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.questapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class StartFragment extends Fragment {
    private static final String TAG = "StartFragment";
    private ProgressBar progressBar;
    private TextView tvTitleQuizz, tv_feedBack;
    private FirebaseAuth firebaseAuth;
    private NavController navController;

    public StartFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start, container, false);
    }

    //called after onCreateView running
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progress_circular_bar);
        tv_feedBack = view.findViewById(R.id.tv_feedBack);

        firebaseAuth = FirebaseAuth.getInstance();
        navController = Navigation.findNavController(view);
        tv_feedBack.setText("Checking...");
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onStart() {
        super.onStart();


        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        //1 : no user, create a new user
        //2 : do something
        if (currentUser == null) {
            firebaseAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        tv_feedBack.setText("Creating");

                        navController.navigate(R.id.action_startFragment_to_listFragment);
                    }
                }
            });
        } else {
            tv_feedBack.setText("Logged");
            //select action to move
            navController.navigate(R.id.action_startFragment_to_listFragment);
        }

    }
}
