package com.app.maththpt.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.app.maththpt.R;
import com.app.maththpt.model.Category;

/**
 * Created by FRAMGIA\nguyen.duc.manh on 24/05/2017.
 */

public class ItemCategoryViewModel extends BaseObservable {
    @Bindable
    public Category category;
    public Context context;

    public ItemCategoryViewModel(Context context, Category category) {
        this.context = context;
        this.category = category;
    }

    @Bindable
    public String getToalQuestion() {
        return context.getString(R.string.soCauDaLam) + category.countViewQuestion + "/" + category.countTotalQuestion;
    }
}
