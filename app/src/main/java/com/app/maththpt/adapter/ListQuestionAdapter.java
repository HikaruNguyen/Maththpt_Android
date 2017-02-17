package com.app.maththpt.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.app.maththpt.R;
import com.app.maththpt.model.Question;
import com.app.maththpt.utils.Utils;
import com.app.maththpt.widget.MathView;
import com.app.maththpt.widget.ResizableImageViewByWidth;

import java.util.List;



/**
 * Created by manhi on 2/7/2016.
 */

public class ListQuestionAdapter extends BaseRecyclerAdapter<Question, ListQuestionAdapter.ViewHolder> {
    public ListQuestionAdapter(Context context, List<Question> list) {
        super(context, list);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.bindData(list.get(position));
        Question question = list.get(position);
        if (question != null) {
            holder.tv_question.setText(mContext.getString(R.string.questionNo) + " " + (position + 1) + ": " + question.question);
            holder.tv_answerA.setText(question.answerList.get(0).answer);
            holder.tv_answerB.setText(question.answerList.get(1).answer);
            holder.tv_answerC.setText(question.answerList.get(2).answer);
            holder.tv_answerD.setText(question.answerList.get(3).answer);
            if (question.image != null && !question.image.trim().isEmpty()) {
                holder.image.setVisibility(View.VISIBLE);
                holder.image.setImageBitmap(Utils.decodeBase64(question.image));
            } else {
                holder.image.setVisibility(View.GONE);
            }

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = layoutInflater.inflate(R.layout.item_question, parent, false);
        return new ViewHolder(view);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private MathView tv_question, tv_answerA, tv_answerB, tv_answerC, tv_answerD;
        private RadioButton rbA, rbB, rbC, rbD;
        private ResizableImageViewByWidth image;

        public ViewHolder(View view) {
            super(view);
            tv_question = (MathView) view.findViewById(R.id.tv_question);
            tv_answerA = (MathView) view.findViewById(R.id.tv_answerA);
            tv_answerB = (MathView) view.findViewById(R.id.tv_answerB);
            tv_answerC = (MathView) view.findViewById(R.id.tv_answerC);
            tv_answerD = (MathView) view.findViewById(R.id.tv_answerD);

            rbA = (RadioButton) view.findViewById(R.id.rbA);
            rbB = (RadioButton) view.findViewById(R.id.rbB);
            rbC = (RadioButton) view.findViewById(R.id.rbC);
            rbD = (RadioButton) view.findViewById(R.id.rbD);

            image = (ResizableImageViewByWidth) view.findViewById(R.id.image);
            itemView.setOnClickListener(v -> {

            });

            rbA.setOnCheckedChangeListener((compoundButton, b) -> {
                if (b) {
                    rbB.setChecked(false);
                    rbC.setChecked(false);
                    rbD.setChecked(false);
                }

            });
            rbB.setOnCheckedChangeListener((compoundButton, b) -> {
                if (b) {
                    rbA.setChecked(false);
                    rbC.setChecked(false);
                    rbD.setChecked(false);
                }
            });
            rbC.setOnCheckedChangeListener((compoundButton, b) -> {
                if (b) {
                    rbB.setChecked(false);
                    rbA.setChecked(false);
                    rbD.setChecked(false);
                }

            });
            rbD.setOnCheckedChangeListener((compoundButton, b) -> {
                if (b) {
                    rbB.setChecked(false);
                    rbC.setChecked(false);
                    rbA.setChecked(false);
                }

            });
        }

        public void bindData(Question question) {

        }

    }

}
