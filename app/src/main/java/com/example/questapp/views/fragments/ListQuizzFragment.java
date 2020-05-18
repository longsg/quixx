package com.example.questapp.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.questapp.R;
import com.example.questapp.views.QuizzViewModel;
import com.example.questapp.views.adapter.QuizzAdapter;
import com.example.questapp.views.model.Quizz;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListQuizzFragment extends Fragment {
    private static final String TAG = "ListQuizzFragment";
    private RecyclerView recyclerView;
    private TextView tv_quizz;
    private QuizzViewModel quizzViewModel;
    private QuizzAdapter quizzAdapter;
    private ProgressBar quizzProgressBar;
    private Animation feadIn, feadOut;

    private NavController controller;
    public ListQuizzFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_quizz, container, false);
    }

    // find  ID, or some thing here
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_container);
        tv_quizz = view.findViewById(R.id.tv_list_page);
        quizzProgressBar = view.findViewById(R.id.list_progress);

        feadIn = AnimationUtils.loadAnimation(getContext(), R.anim.feadin);
        feadOut = AnimationUtils.loadAnimation(getContext(), R.anim.feadout);

        quizzAdapter = new QuizzAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(quizzAdapter);


        controller = Navigation.findNavController(view);

        quizzOnClick(quizzAdapter);
    }

    private void quizzOnClick(QuizzAdapter adapter) {
        adapter.setQuizzOnClick(new QuizzAdapter.IQuizzOnClick() {
            @Override
            public void onQuizzClick(int position) {
                ListQuizzFragmentDirections.ActionListFragmentToDetailFragment action = ListQuizzFragmentDirections.actionListFragmentToDetailFragment();
                action.setPosition(position);
                controller.navigate(action);
            }
        });

    }

    // running after onCreate
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        quizzViewModel = new ViewModelProvider(getActivity()).get(QuizzViewModel.class);
        quizzViewModel.getQuizzMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<Quizz>>() {
            // when QuizzViewModel change, notifi to here
            @Override
            public void onChanged(List<Quizz> quizzList) {
                //load Progress
                recyclerView.startAnimation(feadIn);

                quizzProgressBar.startAnimation(feadOut);


                quizzAdapter.setViewModelList(quizzList);
                quizzAdapter.notifyDataSetChanged();
            }
        });

    }
}
