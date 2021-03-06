package com.app.maththpt.activity;

import android.content.SharedPreferences;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ImageView;

import com.app.maththpt.R;
import com.app.maththpt.adapter.StatisticalPointAdapter;
import com.app.maththpt.config.Configuaration;
import com.app.maththpt.databinding.ActivityUserProfileBinding;
import com.app.maththpt.model.Point;
import com.app.maththpt.model.StatisticalPoint;
import com.app.maththpt.realm.HistoryModule;
import com.app.maththpt.realm.StatisticalModule;
import com.app.maththpt.utils.FacebookUtils;
import com.app.maththpt.viewmodel.UserProfileViewModel;
import com.app.maththpt.widget.MyMarkerView;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.Sort;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;

public class UserProfileActivity extends BaseActivity {
    private ActivityUserProfileBinding userProfileBinding;
    private UserProfileViewModel userProfileViewModel;
    private Typeface mTfLight;
    private List<Point> listPoint;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userProfileBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_profile);
        SharedPreferences sharedPreferences
                = getSharedPreferences(Configuaration.Pref, MODE_PRIVATE);
        String userName = sharedPreferences.getString(Configuaration.KEY_NAME, "");
        String email = sharedPreferences.getString(Configuaration.KEY_EMAIL, "");
        String avatar = "";
        if (!FacebookUtils.getAvatarFBFromId().isEmpty()) {
            avatar = FacebookUtils.getAvatarFBFromId();
        }
        userID = sharedPreferences.getString(Configuaration.KEY_ID, "");
        setSupportActionBar(userProfileBinding.toolbar);
        setBackButtonToolbar();
        userProfileViewModel = new UserProfileViewModel(this, getString(R.string.user_profile), userName, email,
                avatar,userID);
        userProfileBinding.setUserProfileViewModel(userProfileViewModel);

        bindData();
    }

    @BindingAdapter("android:srcUrl")
    public static void setImageUrl(ImageView view, String url) {
        if (url != null && !url.trim().isEmpty()) {
            Picasso.with(view.getContext()).load(url).placeholder(R.mipmap.ic_avatar).into(view);
        } else {
            Picasso.with(view.getContext()).load(R.mipmap.ic_avatar).into(view);
        }

    }

    private void bindData() {
        Realm.init(this);
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

    List<StatisticalPoint> statisticalPoints;

    private void getStatisticalPoint() {
        RealmConfiguration settingConfig = new RealmConfiguration.Builder()
                .name("statisticalPoint.realm")
                .modules(Realm.getDefaultModule(), new StatisticalModule())
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(MyApplication.with(this).REALM_VERSION)
                .build();

        Realm realStatistical = Realm.getInstance(settingConfig);
        statisticalPoints = realStatistical
                .where(StatisticalPoint.class)
                .equalTo("userID", userID)
                .findAll();
        if (statisticalPoints != null && statisticalPoints.size() > 0) {
            userProfileBinding.rvStatisticalPoint.setDivider();
            StatisticalPointAdapter pointAdapter
                    = new StatisticalPointAdapter(this, new ArrayList<>());
            userProfileBinding.rvStatisticalPoint.setAdapter(pointAdapter);
            pointAdapter.addAll(statisticalPoints);
        }
        initChartStatistical();
    }

    private void initChartStatistical() {
        userProfileBinding.chartStatistical.setDrawBarShadow(false);

        userProfileBinding.chartStatistical.setDrawValueAboveBar(true);

        userProfileBinding.chartStatistical.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        userProfileBinding.chartStatistical.setMaxVisibleValueCount(100);

        // scaling can now only be done on x- and y-axis separately
        userProfileBinding.chartStatistical.setPinchZoom(false);
        userProfileBinding.chartStatistical.setDoubleTapToZoomEnabled(false);
        // draw shadows for each bar that show the maximum value
        // userProfileBinding.chartStatistical.setDrawBarShadow(true);

        userProfileBinding.chartStatistical.setDrawGridBackground(false);

        XAxis xl = userProfileBinding.chartStatistical.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setTypeface(mTfLight);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(false);
        xl.setGranularity(10f);
        xl.setValueFormatter((value, axis) -> {
            String name = statisticalPoints.get(
                    (int) (value / 10)).getCateName();
            if (name.length() > 10) {
                name = name.substring(0, 10) + "...";
            }
            return name;
        });
        YAxis yl = userProfileBinding.chartStatistical.getAxisLeft();
        yl.setTypeface(mTfLight);
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(true);
        yl.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//        yl.setInverted(true);

        YAxis yr = userProfileBinding.chartStatistical.getAxisRight();
        yr.setTypeface(mTfLight);
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(false);
        yr.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        yr.setInverted(true);

        setDataStatistical();
        userProfileBinding.chartStatistical.setFitBars(true);
        userProfileBinding.chartStatistical.animateY(2500);

        Legend l = userProfileBinding.chartStatistical.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setXEntrySpace(4f);
        userProfileBinding.chartStatistical.setNoDataText(getString(R.string.no_data));
    }

    private static final int[] MY_COLORS = {
            rgb("#ff5252"), rgb("#aa00ff"), rgb("#304ffe"), rgb("#0091ea"),
            rgb("#00c853"), rgb("#ffd600"), rgb("#ff6e40"), rgb("#455a64")};

    private void setDataStatistical() {

        float barWidth = 9f;
        float spaceForBar = 10f;
        ArrayList<BarEntry> yVals1 = new ArrayList<>();

        for (int i = statisticalPoints.size() - 1; i >= 0; i--) {
            float val = statisticalPoints.get(i).getRatio();
//            if (i == statisticalPoints.size() - 1) {
//                yVals1.add(new BarEntry(i * spaceForBar, 100,
//                        statisticalPoints.get(i).getCateName()));
//            } else {
//                yVals1.add(new BarEntry(i * spaceForBar, val,
//                        statisticalPoints.get(i).getCateName()));
//            }
            yVals1.add(new BarEntry(i * spaceForBar, val,
                    statisticalPoints.get(i).getCateName()));
        }

        BarDataSet set1;

        if (userProfileBinding.chartStatistical.getData() != null &&
                userProfileBinding.chartStatistical.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) userProfileBinding.chartStatistical.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            userProfileBinding.chartStatistical.getData().notifyDataChanged();
            userProfileBinding.chartStatistical.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, getString(R.string.tiLeLamBaiDung));
            set1.setColors(MY_COLORS);
//            set1.setDrawIcons(false);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setValueTypeface(mTfLight);
            data.setBarWidth(barWidth);
            userProfileBinding.chartStatistical.setData(data);
        }
    }

    private void getListPoint() {
        // TODO: 01/03/2017 getTop10

        RealmConfiguration settingConfig = new RealmConfiguration.Builder()
                .name("history.realm")
                .modules(Realm.getDefaultModule(), new HistoryModule())
                .schemaVersion(MyApplication.with(this).REALM_VERSION)
//                .migration(new HistoryMigration())
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm realm = Realm.getInstance(settingConfig);
        listPoint = realm
                .where(Point.class)
                .equalTo("userID", userID)
                .findAllSorted("time", Sort.DESCENDING);
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
                val = listPoint.get(i).point;
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
