package com.example.questapp.views.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.questapp.R;
import com.example.questapp.views.model.Questions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuizzDetail extends Fragment implements View.OnClickListener {
    private static final String TAG = "QuizzDetail";
    private NavController controller;
    private FirebaseFirestore firebaseFirestore;
    private String quizzId;

    private String currentUser;
    private List<Questions> questionsList = new ArrayList<>();
    private List<Questions> answerList = new ArrayList<>();
    //UI
    private ImageView quizz_close_btn;
    private TextView quiz_question_number, quiz_question, quiz_question_feedback, quiz_question_time;
    private Button
            quiz_option_one,
            quiz_option_two,
            quiz_option_three,
            question_next_btn;
    private ProgressBar progressBar;
    private Long totalQuestions = 0L;
    private CountDownTimer coundownTimer;
    //variable
    private boolean isTrueAnser = false;
    private int currentQuestion = 0;
    private int correctAnswer = 0;
    private int wrongAnswer = 0;
    private int notAnswer = 0;
    private FirebaseAuth firebaseAuth;
    private String quizzName;


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
        controller = Navigation.findNavController(view);

        //textview
        quizz_close_btn = view.findViewById(R.id.quizz_close_btn);
        quiz_question_number = view.findViewById(R.id.quiz_question_number);
        quiz_question_feedback = view.findViewById(R.id.quiz_question_feedback);
        quiz_question_time = view.findViewById(R.id.quiz_question_time);
        //button
        quiz_option_one = view.findViewById(R.id.quiz_option_one);
        quiz_option_two = view.findViewById(R.id.quiz_option_two);
        quiz_option_three = view.findViewById(R.id.quiz_option_three);
        question_next_btn = view.findViewById(R.id.question_next_btn);

        progressBar = view.findViewById(R.id.quiz_question_progress);
        quiz_question = view.findViewById(R.id.quiz_question);
        if (getArguments() != null) {
            quizzId = QuizzDetailArgs.fromBundle(getArguments()).getQuizzId();
            totalQuestions = QuizzDetailArgs.fromBundle(getArguments()).getTotalQuestions();
            quizzName = QuizzDetailArgs.fromBundle(getArguments()).getQuizzName();
        }
        Log.d(TAG, "totalQuestions: " + totalQuestions);
        Log.d(TAG, "Quizz ID: " + quizzId);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            currentUser = firebaseAuth.getCurrentUser().getUid();
        } else {

        }

        //query firebase
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.
                collection("quizz").
                document(quizzId).collection("questions").
                get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            questionsList = Objects.requireNonNull(task.getResult()).toObjects(Questions.class);
                            Log.d(TAG, "Question List size: " + questionsList.size());
                            pickAllQuestions();
                            displayData();
                        } else {
                            Log.d(TAG, "onComplete: " + task.getException());
                            quiz_question.setText(task.getException() + "");
                        }
                    }
                }
        );

        quiz_option_one.setOnClickListener(this);
        quiz_option_two.setOnClickListener(this);
        quiz_option_three.setOnClickListener(this);
        question_next_btn.setOnClickListener(this);
    }


    private void displayData() {


        //enableOption
        enableOptions();
        //loadQuestion

        loadQuestion(1);
    }

    private void loadQuestion(int questionPosition) {

        quiz_question.setText(answerList.get(questionPosition - 1).getQuestion());
        quiz_option_one.setText(answerList.get(questionPosition - 1).getAnswer_a());
        quiz_option_two.setText(answerList.get(questionPosition - 1).getAnswer_b());
        quiz_option_three.setText(answerList.get(questionPosition - 1).getAnswer_c());

        currentQuestion = questionPosition;
        isTrueAnser = true;
        startTimeer(questionPosition);
    }

    private void startTimeer(int questionPosition) {
        final int timer = answerList.get(questionPosition - 1).getTimer();

        progressBar.setVisibility(View.VISIBLE);
        coundownTimer = new CountDownTimer(timer * 1000, 10) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {

                quiz_question_time.setText(millisUntilFinished / 1000 + "");
                long percent = millisUntilFinished / (timer * 10);
                progressBar.setProgress((int) percent);
            }

            @Override
            public void onFinish() {
                quiz_question_feedback.setText("Timeup, not answer");
                quiz_question_feedback.setTextColor(getResources().getColor(R.color.notAnswer));
                notAnswer++;

                isTrueAnser = false;
                showNextButton();
            }
        };
        coundownTimer.start();
    }

    private void enableOptions() {
        quiz_option_one.setVisibility(View.VISIBLE);
        quiz_option_two.setVisibility(View.VISIBLE);
        quiz_option_three.setVisibility(View.VISIBLE);

        quiz_option_one.setEnabled(true);
        quiz_option_two.setEnabled(true);
        quiz_option_three.setEnabled(true);
//hide
        quiz_question_feedback.setVisibility(View.INVISIBLE);
        question_next_btn.setVisibility(View.INVISIBLE);
        question_next_btn.setEnabled(false);
    }

    private void loadUI(View view) {
        //textview
        quizz_close_btn = view.findViewById(R.id.quizz_close_btn);
        quiz_question_number = view.findViewById(R.id.quiz_question_number);
        quiz_question_feedback = view.findViewById(R.id.quiz_question_feedback);
        quiz_question_time = view.findViewById(R.id.quiz_question_time);
        //button
        quiz_option_one = view.findViewById(R.id.quiz_option_one);
        quiz_option_two = view.findViewById(R.id.quiz_option_two);
        quiz_option_three = view.findViewById(R.id.quiz_option_three);
        question_next_btn = view.findViewById(R.id.question_next_btn);
    }

    private void pickAllQuestions() {
        for (int i = 0; i < totalQuestions; i++) {
            int random = randomQuestion(0, questionsList.size());
            answerList.add(questionsList.get(random));
            questionsList.remove(random);
            Log.d(TAG, "pickAllQuestions: " + i + answerList.get(i).getQuestion());
        }
    }

    private int randomQuestion(int min, int max) {
        return ((int) (Math.random() * (max - min))) + min;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.quiz_option_one:
                verifyAnswer(quiz_option_one);
                break;
            case R.id.quiz_option_two:
                verifyAnswer(quiz_option_two);
                break;
            case R.id.quiz_option_three:
                verifyAnswer(quiz_option_three);
                break;
            case R.id.question_next_btn:
                if (currentQuestion == totalQuestions) {
                    sendResult();
                } else {
                    currentQuestion++;
                    loadQuestion(currentQuestion);
                    restartQuestion();
                }
                break;
        }
    }

    private void sendResult() {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("correct", correctAnswer);
        resultMap.put("wrong", wrongAnswer);
        resultMap.put("unAnswered", notAnswer);


        firebaseFirestore.collection("quizz")
                .document(quizzId)
                .collection("results")
                .document(currentUser)
                .set(resultMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            QuizzDetailDirections.ActionQuizzFragmentToResultFragment action = QuizzDetailDirections.actionQuizzFragmentToResultFragment();
                            action.setQuizId(quizzId);
                            controller.navigate(action);


                        } else {
                            Log.d(TAG, "onComplete: " + task.getException());
                        }
                    }
                });
    }

    private void restartQuestion() {
        quiz_option_one.setBackground(getResources().getDrawable(R.drawable.custom_button, null));
        quiz_option_two.setBackground(getResources().getDrawable(R.drawable.custom_button, null));
        quiz_option_three.setBackground(getResources().getDrawable(R.drawable.custom_button, null));

        quiz_option_one.setTextColor(getResources().getColor(R.color.colorLightText, null));
        quiz_option_two.setTextColor(getResources().getColor(R.color.colorLightText, null));
        quiz_option_three.setTextColor(getResources().getColor(R.color.colorLightText, null));

        quiz_question_feedback.setVisibility(View.INVISIBLE);
        question_next_btn.setVisibility(View.INVISIBLE);
        question_next_btn.setEnabled(false);
    }

    private void verifyAnswer(Button button) {

        button.setTextColor(getResources().getColor(R.color.colorDark, null));
        if (isTrueAnser) {
            if (button.getText().equals(answerList.get(currentQuestion - 1).getAnswer())) {
                correctAnswer++;
                button.setBackground(getResources().getDrawable(R.drawable.correct_answer_button, null));
                quiz_question_feedback.setTextColor(getResources().getColor(R.color.correct, null));
                quiz_question_feedback.setText("Correct Answer \n");
                Log.d(TAG, "verifyAnswer: " + "correct");
            } else {
                wrongAnswer++;
                quiz_question_feedback.setTextColor(getResources().getColor(R.color.wrong, null));
                quiz_question_feedback.setText("Wrong Answer \n Correct Answer\n" + answerList.get(currentQuestion - 1).getAnswer());
                button.setBackground(getResources().getDrawable(R.drawable.wrong_answer_button, null));
                Log.d(TAG, "verifyAnswer: Wrong");
            }


        }

        isTrueAnser = false;
        coundownTimer.cancel();

        showNextButton();
    }

    private void showNextButton() {
        if (currentQuestion == totalQuestions) {
            question_next_btn.setTextColor(getResources().getColor(R.color.correct));
            question_next_btn.setText("Submit result");
        }

        quiz_question_feedback.setVisibility(View.VISIBLE);
        question_next_btn.setVisibility(View.VISIBLE);
        question_next_btn.setEnabled(true);
    }
}
