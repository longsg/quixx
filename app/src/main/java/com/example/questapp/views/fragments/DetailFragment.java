package com.example.questapp.views.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.questapp.R;
import com.example.questapp.views.QuizzViewModel;
import com.example.questapp.views.model.Quizz;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "DetailFragment";
    private NavController navController;
    private QuizzViewModel quizzViewModel;
    private int position;
    private Long totalQuestions = 0L;
    private String quizzId;
    //
    private ImageView details_image;
    private TextView
            details_description,
            details_difficulty_text,
            details_questions_text,
            details_score_text;
    private Button details_start_btn;
    private String quizzName;


    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        assert getArguments() != null;
        position = DetailFragmentArgs.fromBundle(getArguments()).getPosition();
        Log.d("APP", "onViewCreated: " + position);
        details_image = view.findViewById(R.id.details_image);
        details_description = view.findViewById(R.id.details_desc);
        details_difficulty_text = view.findViewById(R.id.details_difficulty_text);
        details_questions_text = view.findViewById(R.id.details_questions_text);
        details_score_text = view.findViewById(R.id.details_score_text);
        details_start_btn = view.findViewById(R.id.details_start_btn);
        details_start_btn.setOnClickListener(this);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        quizzViewModel = new ViewModelProvider(getActivity()).get(QuizzViewModel.class);
        quizzViewModel.getQuizzMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<Quizz>>() {
            @Override
            public void onChanged(List<Quizz> quizzList) {
                Glide.with(getContext())
                        .load(quizzList.get(position).getImage())
                        .centerCrop()
                        .into(details_image);
                details_description.setText(quizzList.get(position).getDesc());

                details_difficulty_text.setText(quizzList.get(position).getLvl());
                quizzId = quizzList.get(position).getQuizzId();
                totalQuestions = quizzList.get(position).getQuestions();
                details_questions_text.setText(totalQuestions + "");
                quizzName = quizzList.get(position).getName();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.details_start_btn:
                DetailFragmentDirections.ActionDetailFragmentToQuizzFragment actionDetailFragmentToQuizzFragment = DetailFragmentDirections.actionDetailFragmentToQuizzFragment();
                actionDetailFragmentToQuizzFragment.setQuizzId(quizzId);
                actionDetailFragmentToQuizzFragment.setTotalQuestions(totalQuestions);
                navController.navigate(actionDetailFragmentToQuizzFragment);
                break;
        }
    }
}
