package com.example.questapp.views.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.questapp.R;
import com.example.questapp.views.model.Quizz;

import java.util.List;

public class QuizzAdapter extends RecyclerView.Adapter<QuizzAdapter.QuizzHolder> {
    private List<Quizz> viewModelList;
    private Context context;
    private static final String TAG = "QuizzAdapter";
    private int height = Resources.getSystem().getDisplayMetrics().heightPixels;
    private int width = Resources.getSystem().getDisplayMetrics().widthPixels;
    private IQuizzOnClick quizzOnClick;

    public void setQuizzOnClick(IQuizzOnClick quizzOnClick) {
        this.quizzOnClick = quizzOnClick;
    }
    //    public QuizzAdapter(Context context) {
//        this.context = context;
//    }

    public void setViewModelList(List<Quizz> viewModelList) {
        this.viewModelList = viewModelList;
    }

    @NonNull
    @Override
    public QuizzHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_item, parent, false);
        return new QuizzHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizzHolder holder, int position) {
        Quizz currentQuizz = viewModelList.get(position);
        holder.onBindQuizz(currentQuizz);
    }

    @Override
    public int getItemCount() {
        if (viewModelList
                == null)
            return 0;
        else return viewModelList.size();
    }

    public class QuizzHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Quizz currentQuizz;
        ImageView list_image;
        TextView list_title, list_desc, list_difficulty, list_btn;

        public QuizzHolder(@NonNull View itemView) {
            super(itemView);
            list_btn = itemView.findViewById(R.id.list_btn);
            list_image = itemView.findViewById(R.id.list_image);
            list_desc = itemView.findViewById(R.id.list_desc);
            list_title = itemView.findViewById(R.id.list_title);
            list_difficulty = itemView.findViewById(R.id.list_difficulty);
            itemView.setOnClickListener(this);
            list_btn.setOnClickListener(this);
        }

        public void onBindQuizz(Quizz quizz) {
            currentQuizz = quizz;
            list_title.setText(currentQuizz.getName());

            if (currentQuizz.getDesc().length() >= 150) {
                String result = currentQuizz.getDesc().trim().substring(0, 150);
                list_desc.setText(result);
            }


            list_difficulty.setText(currentQuizz.getLvl());

            Glide.with(itemView.getContext())
                    .load(currentQuizz.getImage())
                    .centerInside()
                    .into(list_image);

            Log.d(TAG, "onBindQuizz: " + list_image.getHeight() + "");

        }

        @Override
        public void onClick(View v) {
            quizzOnClick.onQuizzClick(getAdapterPosition());
        }
    }


    public interface IQuizzOnClick {

        void onQuizzClick(int position);
    }

}
