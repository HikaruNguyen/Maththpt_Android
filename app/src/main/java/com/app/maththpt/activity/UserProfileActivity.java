package com.app.maththpt.activity;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Float.parseFloat;

public class UserProfileActivity extends BaseActivity {
    private ActivityUserProfileBinding userProfileBinding;
    private UserProfileViewModel userProfileViewModel;
    private Typeface mTfLight;
    private List<Point> listPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userProfileBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_profile);
        SharedPreferences sharedPreferences = getSharedPreferences(Configuaration.Pref, MODE_PRIVATE);
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
            StatisticalPointAdapter pointAdapter = new StatisticalPointAdapter(this, new ArrayList<>());
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
        userProfileBinding.chartPoint.setViewPortOffsets(0, 0, 0, 0);
        userProfileBinding.chartPoint.setBackgroundColor(Color.WHITE);

        // no description text
        userProfileBinding.chartPoint.getDescription().setEnabled(false);

        // enable touch gestures
        userProfileBinding.chartPoint.setTouchEnabled(true);

        // enable scaling and dragging
        userProfileBinding.chartPoint.setDragEnabled(true);
        userProfileBinding.chartPoint.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        userProfileBinding.chartPoint.setPinchZoom(false);

        userProfileBinding.chartPoint.setDrawGridBackground(false);
        userProfileBinding.chartPoint.setMaxHighlightDistance(300);

        XAxis x = userProfileBinding.chartPoint.getXAxis();
        x.setEnabled(false);

        YAxis y = userProfileBinding.chartPoint.getAxisLeft();
        y.setTypeface(mTfLight);
        y.setLabelCount(6, false);
        y.setTextColor(Color.RED);
        y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        y.setDrawGridLines(false);
        y.setAxisLineColor(Color.rgb(104, 241, 175));

        userProfileBinding.chartPoint.getAxisRight().setEnabled(false);

        // add data
        setData();

        userProfileBinding.chartPoint.getLegend().setEnabled(false);

        userProfileBinding.chartPoint.animateXY(2000, 2000);


        for (IDataSet set : userProfileBinding.chartPoint.getData().getDataSets())
            set.setDrawValues(!set.isDrawValuesEnabled());

        List<ILineDataSet> sets = userProfileBinding.chartPoint.getData()
                .getDataSets();

        for (ILineDataSet iSet : sets) {

            LineDataSet set = (LineDataSet) iSet;

            if (set.isDrawFilledEnabled())
                set.setDrawFilled(false);
            else
                set.setDrawFilled(true);

            set.setMode(set.getMode() == LineDataSet.Mode.CUBIC_BEZIER
                    ? LineDataSet.Mode.LINEAR
                    : LineDataSet.Mode.CUBIC_BEZIER);
        }
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

        LineDataSet set1;

        if (userProfileBinding.chartPoint.getData() != null &&
                userProfileBinding.chartPoint.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) userProfileBinding.chartPoint.getData().getDataSetByIndex(0);
            set1.setValues(yVals);
            userProfileBinding.chartPoint.getData().notifyDataChanged();
            userProfileBinding.chartPoint.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(yVals, "DataSet 1");

            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setCubicIntensity(0.2f);
            //set1.setDrawFilled(true);
            set1.setDrawCircles(true);
            set1.setLineWidth(1.8f);
            set1.setCircleRadius(4f);
            set1.setCircleColor(Color.RED);
            set1.setHighLightColor(Color.rgb(244, 117, 117));
            set1.setColor(Color.WHITE);
            set1.setFillColor(Color.parseColor("#2196F3"));
            set1.setFillAlpha(100);
            set1.setDrawHorizontalHighlightIndicator(false);
            set1.setFillFormatter((dataSet, dataProvider) -> -10);

            // create a data object with the datasets
            LineData data = new LineData(set1);
            data.setValueTypeface(mTfLight);
            data.setValueTextSize(9f);
            data.setDrawValues(false);

            // set data
            userProfileBinding.chartPoint.setData(data);
        }
    }
}
