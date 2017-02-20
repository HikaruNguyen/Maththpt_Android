package com.app.maththpt.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.maththpt.BR;
import com.app.maththpt.R;
import com.app.maththpt.model.Point;
import com.app.maththpt.utils.CLog;

import java.util.Date;
import java.util.List;


/**
 * Created by manhi on 2/7/2016.
 */

public class HistoryAdapter extends BaseRecyclerAdapter<Point, HistoryAdapter.ViewHolder> {
    public HistoryAdapter(Context context, List<Point> list) {
        super(context, list);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
//        holder.bindData(list.get(position));
        ViewDataBinding viewDataBinding = holder.getViewDataBinding();
        viewDataBinding.setVariable(BR.point, list.get(position));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_history, parent, false);

        return new ViewHolder(binding);
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private ViewDataBinding mViewDataBinding;

        public ViewHolder(ViewDataBinding viewDataBinding) {
            super(viewDataBinding.getRoot());

            mViewDataBinding = viewDataBinding;
            mViewDataBinding.executePendingBindings();
            itemView.setOnClickListener(view -> {
//                    Intent intent = new Intent(mContext, QuestionActivity.class);
//                    intent.putExtra("title", list.get(getAdapterPosition()).name);
//                    intent.putExtra("type", Configuaration.TYPE_ONTAP);
//                    intent.putExtra("cateID", list.get(getAdapterPosition()).id);
//                    mContext.startActivity(intent);
            });
        }

        public ViewDataBinding getViewDataBinding() {
            return mViewDataBinding;
        }
    }

    @BindingAdapter("displayPoint")
    public static void displayPoint(TextView textView, String point) {
        textView.setText(textView.getContext().getString(R.string.yourPoint) + " : " + point);
    }

    @SuppressLint("SimpleDateFormat")
    @BindingAdapter("displayDate")
    public static void displayDate(TextView textView, String time) {
        CLog.d("", "DATE :" + time);
        long lTime = 0;
        String sDateTime = "";
        try {
            lTime = Long.parseLong(time);
            sDateTime = DateFormat.format("dd/MM/yyyy hh:mm", new Date(lTime)).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        textView.setText(textView.getContext().getString(R.string.datetime) + " : " + DateFormat.format("MM/dd/yyyy", new Date(Long.parseLong(time) * 1000)).toString());
        textView.setText(textView.getContext().getString(R.string.datetime) + " : " + sDateTime);
    }
}
