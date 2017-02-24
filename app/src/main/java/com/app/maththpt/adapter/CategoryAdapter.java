package com.app.maththpt.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.maththpt.BR;
import com.app.maththpt.R;
import com.app.maththpt.activity.QuestionActivity;
import com.app.maththpt.config.Configuaration;
import com.app.maththpt.model.Category;

import java.util.List;


/**
 * Created by manhi on 2/7/2016.
 */

public class CategoryAdapter extends BaseRecyclerAdapter<Category, CategoryAdapter.ViewHolder> {
    public CategoryAdapter(Context context, List<Category> list) {
        super(context, list);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ViewDataBinding viewDataBinding = holder.getViewDataBinding();
        viewDataBinding.setVariable(BR.category, list.get(position));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_category, parent, false);

        return new ViewHolder(binding);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ViewDataBinding mViewDataBinding;

        ViewHolder(ViewDataBinding viewDataBinding) {
            super(viewDataBinding.getRoot());

            mViewDataBinding = viewDataBinding;
            mViewDataBinding.executePendingBindings();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, QuestionActivity.class);
                    intent.putExtra("title", list.get(getAdapterPosition()).name);
                    intent.putExtra("type", Configuaration.TYPE_ONTAP);
                    intent.putExtra("cateID", list.get(getAdapterPosition()).id);
                    mContext.startActivity(intent);
                }
            });
        }

        ViewDataBinding getViewDataBinding() {
            return mViewDataBinding;
        }
    }

    @BindingAdapter("imageResource")
    public static void setImageResource(ImageView imageView, int resource) {
        imageView.setImageResource(resource);
    }
}
