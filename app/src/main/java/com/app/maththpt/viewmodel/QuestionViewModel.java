package com.app.maththpt.viewmodel;

import android.app.Activity;
import android.view.View;

/**
 * Created by FRAMGIA\nguyen.duc.manh on 17/02/2017.
 */

public class QuestionViewModel extends BaseViewModel {

    public QuestionViewModel(Activity activity, String title) {
        super(activity, title);
        visiableError = View.GONE;
    }


}
