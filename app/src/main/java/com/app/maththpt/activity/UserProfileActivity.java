package com.app.maththpt.activity;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import com.app.maththpt.R;
import com.app.maththpt.adapter.StatisticalPointAdapter;
import com.app.maththpt.config.Configuaration;
import com.app.maththpt.database.HistoryDBHelper;
import com.app.maththpt.database.StatisticalPointDBHelper;
import com.app.maththpt.databinding.ActivityUserProfileBinding;
import com.app.maththpt.model.Point;
import com.app.maththpt.model.StatisticalPoint;
import com.app.maththpt.viewmodel.UserProfileViewModel;
import com.app.maththpt.widget.MyMarkerView;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

public class UserProfileActivity extends BaseActivity {
    private ActivityUserProfileBinding userProfileBinding;
    private UserProfileViewModel userProfileViewModel;
    private Typeface mTfLight;
    private List<Point> listPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userProfileBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_profile);
        SharedPreferences sharedPreferences
                = getSharedPreferences(Configuaration.Pref, MODE_PRIVATE);
        String userName = sharedPreferences.getString(Configuaration.KEY_NAME, "");
        String email = sharedPreferences.getString(Configuaration.KEY_EMAIL, "");
        setSupportActionBar(userProfileBinding.toolbar);
        setBackButtonToolbar();
        userProfileViewModel = new UserProfileViewModel(this, getString(R.string.user_profile), userName, email);
        userProfileBinding.setUserProfileViewModel(userProfileViewModel);

        bindData();
    }

    private void bindData() {
        mTfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");
        listPoint = new ArrayList<>();
        getListPoint();
        if (listPoint != null && listPoint.size() > 0) {
            initChart();
        } else {
            userProfileBinding.chartPoint.setNoDataText(getString(R.string.no_data));
            userProfileViewModel.setVisiableError(true);
        }
        getStatisticalPoint();
    }

    private void getStatisticalPoint() {
        StatisticalPointDBHelper.StatisticalPointDatabase statisticalPointDatabase
                = new StatisticalPointDBHelper.StatisticalPointDatabase(this);
        statisticalPointDatabase.open();
        List<StatisticalPoint> statisticalPoints = statisticalPointDatabase.getAll();
        if (statisticalPoints != null && statisticalPoints.size() > 0) {
            userProfileBinding.rvStatisticalPoint.setDivider();
            StatisticalPointAdapter pointAdapter
                    = new StatisticalPointAdapter(this, new ArrayList<>());
            userProfileBinding.rvStatisticalPoint.setAdapter(pointAdapter);
            pointAdapter.addAll(statisticalPoints);
        }
        statisticalPointDatabase.close();

    }

    private void getListPoint() {
        HistoryDBHelper.HistoryDatabase historyDatabase = new HistoryDBHelper.HistoryDatabase(this);
        historyDatabase.open();
        listPoint = historyDatabase.getTop10();
        historyDatabase.close();
    }

    private void initChart() {
//        userProfileBinding.chartPoint.setOnChartValueSelectedListener(this);
        userProfileBinding.chartPoint.setDrawGridBackground(false);

        // no description text
        userProfileBinding.chartPoint.getDescription().setEnabled(false);

        // enable touch gestures
        userProfileBinding.chartPoint.setTouchEnabled(true);

        // enable scaling and dragging
        userProfileBinding.chartPoint.setDragEnabled(true);
        userProfileBinding.chartPoint.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        userProfileBinding.chartPoint.setPinchZoom(true);

        // set an alternative background color
        // userProfileBinding.chartPoint.setBackgroundColor(Color.GRAY);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
        mv.setChartView(userProfileBinding.chartPoint); // For bounds control
        userProfileBinding.chartPoint.setMarker(mv); // Set the marker to the chart

        XAxis xl = userProfileBinding.chartPoint.getXAxis();
        xl.setAvoidFirstLastClipping(true);
        xl.setAxisMinimum(0f);

        YAxis leftAxis = userProfileBinding.chartPoint.getAxisLeft();
        leftAxis.setInverted(false);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = userProfileBinding.chartPoint.getAxisRight();
        rightAxis.setEnabled(false);
        // add data
        setData();

        // // restrain the maximum scale-out factor
        // userProfileBinding.chartPoint.setScaleMinima(3f, 3f);
        //
        // // center the view to a specific position inside the chart
        // userProfileBinding.chartPoint.centerViewPort(10, 50);

        // get the legend (only possible after setting data)
        Legend l = userProfileBinding.chartPoint.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);

        // dont forget to refresh the drawing
        userProfileBinding.chartPoint.setNoDataText(getString(R.string.no_data));
        userProfileBinding.chartPoint.invalidate();
    }

    private void setData() {

        ArrayList<Entry> yVals = new ArrayList<>();
        int j = 0;
        for (int i = listPoint.size() - 1; i >= 0; i--) {
            float val;
            try {
                val = Float.parseFloat(listPoint.get(i).point);
            } catch (Exception e) {
                e.printStackTrace();
                val = 0;
            }
            yVals.add(new Entry(j, val));
            j++;
        }

        LineDataSet set1 = new LineDataSet(yVals, getString(R.string.yourPoint));

        set1.setLineWidth(1.5f);
        set1.setCircleColor(Color.RED);
        set1.setColor(Color.GREEN);
        set1.setCircleRadius(4f);

        // create a data object with the datasets
        LineData data = new LineData(set1);

        // set data
        userProfileBinding.chartPoint.setData(data);
    }
}
