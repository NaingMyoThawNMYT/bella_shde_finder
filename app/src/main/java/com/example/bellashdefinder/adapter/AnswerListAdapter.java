package com.example.bellashdefinder.adapter;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bellashdefinder.R;
import com.example.bellashdefinder.model.Answer;

import java.util.List;

public class AnswerListAdapter extends RecyclerView.Adapter<AnswerListAdapter.ViewHolder> {
    private List<Answer> dataSet;

    public AnswerListAdapter(List<Answer> dataSet) {
        this.dataSet = dataSet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.answer_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Answer answer = dataSet.get(holder.getAdapterPosition());

        if (answer.isSelected()) {
            holder.tvAnswer.setTextColor(Color.parseColor("#E91E63"));
        } else {
            holder.tvAnswer.setTextColor(Color.parseColor("#000000"));
        }

        if (!TextUtils.isEmpty(answer.getColor())) {
            holder.cv.setCardBackgroundColor(Color.parseColor(answer.getColor()));
        }

        holder.tvAnswer.setText(answer.getAnswer());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Answer a : dataSet) {
                    a.setSelected(false);
                }

                answer.setSelected(true);

                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet == null ? 0 : dataSet.size();
    }

    public Answer getSelectedAnswer() {
        for (Answer a : dataSet) {
            if (a.isSelected()) {
                return a;
            }
        }

        return null;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cv;
        private AppCompatTextView tvAnswer;

        ViewHolder(@NonNull View v) {
            super(v);

            cv = v.findViewById(R.id.cv);
            tvAnswer = v.findViewById(R.id.tv_answer);
        }
    }
}
