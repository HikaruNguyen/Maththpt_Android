package com.app.maththpt.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.app.maththpt.BR;
import com.app.maththpt.R;
import com.app.maththpt.activity.QuestionActivity;
import com.app.maththpt.config.Configuaration;
import com.app.maththpt.model.Tests;
import com.app.maththpt.widget.BadgeView;

import java.util.List;


/**
 * Created by manhi on 2/7/2016.
 */

public class TestsAdapter extends BaseRecyclerAdapter<Tests, TestsAdapter.ViewHolder> {

    public TestsAdapter(Context context, List<Tests> list) {
        super(context, list);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ViewDataBinding viewDataBinding = holder.getViewDataBinding();
        viewDataBinding.setVariable(BR.test, list.get(position));

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_tests, parent, false);
        return new ViewHolder(binding);
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private BadgeView badgeView;
        private ViewDataBinding mViewDataBinding;

        ViewHolder(ViewDataBinding viewDataBinding) {
            super(viewDataBinding.getRoot());

            mViewDataBinding = viewDataBinding;
            mViewDataBinding.executePendingBindings();
            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(mContext, QuestionActivity.class);
                intent.putExtra("title", list.get(getAdapterPosition()).displayname);
                intent.putExtra("type", Configuaration.TYPE_BODE);
                intent.putExtra("testID", list.get(getAdapterPosition()).id + "");
                mContext.startActivity(intent);
            });
        }

        ViewDataBinding getViewDataBinding() {
            return mViewDataBinding;

        }
    }
}
