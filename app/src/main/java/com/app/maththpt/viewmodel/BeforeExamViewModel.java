package com.app.maththpt.viewmodel;

import android.app.Activity;
import android.databinding.Bindable;

import com.app.maththpt.R;

import java.util.concurrent.TimeUnit;

/**
 * Created by manhi on 21/2/2017.
 */

public class BeforeExamViewModel extends BaseViewModel {

    private int number;
    private long time;
    private static final String FORMAT = "%02d phút";

    public BeforeExamViewModel(Activity activity, int number, long time) {
        super(activity);
        this.number = number;
        this.time = time;
    }

    @Bindable
    public String getNumber() {
        return number + " " + activity.getString(R.string.question);
    }

    @Bindable
    public String getTime() {
        return "" + String.format(FORMAT,
                TimeUnit.MILLISECONDS.toHours(time) * 60 +
                        TimeUnit.MILLISECONDS.toMinutes(time) - TimeUnit.HOURS.toMinutes(
                        TimeUnit.MILLISECONDS.toHours(time))
        );
    }

    public void setNumber(int number) {
        this.number = number;
        notifyChange();
    }

    public void setTime(long time) {
        this.time = time;
        notifyChange();
    }
}
