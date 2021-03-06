package com.app.maththpt.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.app.maththpt.R;
import com.app.maththpt.model.Category;

import java.util.List;


/**
 * Created by manhi on 2/7/2016.
 */

public class CategoryCheckAdapter extends
        BaseRecyclerAdapter<Category, CategoryCheckAdapter.ViewHolder> {
    public CategoryCheckAdapter(Context context, List<Category> list) {
        super(context, list);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.bindData(list.get(position));
        holder.checkbox.setOnCheckedChangeListener(
                (compoundButton, b) -> list.get(position).isChecked = b);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_checkbox_category, parent, false);
        return new ViewHolder(view);
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private CheckBox checkbox;

        ViewHolder(View view) {
            super(view);
            checkbox = (CheckBox) view.findViewById(R.id.checkbox);
            itemView.setOnClickListener(v -> {
                if (checkbox.isChecked()) {
                    checkbox.setChecked(false);
                } else {
                    checkbox.setChecked(true);
                }
            });
        }

        public void bindData(Category category) {
            checkbox.setText(category.name);
            if (category.isChecked) {
                checkbox.setChecked(true);
            } else {
                checkbox.setChecked(false);
            }
        }

    }

}
