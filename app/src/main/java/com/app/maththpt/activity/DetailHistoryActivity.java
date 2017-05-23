package com.app.maththpt.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;

import com.app.maththpt.R;
import com.app.maththpt.adapter.DetailPointAdapter;
import com.app.maththpt.databinding.ActivityDetailHistoryBinding;
import com.app.maththpt.model.DetailPoint;
import com.app.maththpt.model.Point;
import com.app.maththpt.viewmodel.DetailPointViewModel;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.List;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;

public class DetailHistoryActivity extends BaseActivity {
    private ActivityDetailHistoryBinding detailHistoryBinding;
    private DetailPointViewModel detailPointViewModel;
    private DetailPointAdapter detailPointAdapter;
    private Point point;
    private Typeface mTfLight;
    private List<DetailPoint> detailPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailHistoryBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_detail_history);
        detailPointViewModel = new DetailPointViewModel();
        detailHistoryBinding.setDetailPointViewModel(detailPointViewModel);
        getData();
        initUI();
        bindData();
    }

    private void bindData() {
        if (point != null) {
            detailPoints = point.getListdetailPoints(point.detailPoint);
            detailPointAdapter.addAll(detailPoints);
            if (detailPointAdapter.getItemCount() > 0) {
                initChart();
            }
        }
    }

    private void getData() {
        Intent intent = getIntent();
        point = new Point();
        point.detailPoint = intent.getStringExtra("detailHistory");
    }

    private void initUI() {
        detailPointViewModel.setTitle(getString(R.string.detailPoint));
        setBackButtonToolbar();
        detailHistoryBinding.rvDetailPoint.setDivider();
        detailPointAdapter = new DetailPointAdapter(this, new ArrayList<>());
        detailHistoryBinding.rvDetailPoint.setAdapter(detailPointAdapter);
    }

    private void initChart() {
        detailHistoryBinding.chartStatistical.setDrawBarShadow(false);

        detailHistoryBinding.chartStatistical.setDrawValueAboveBar(true);

        detailHistoryBinding.chartStatistical.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        detailHistoryBinding.chartStatistical.setMaxVisibleValueCount(100);

        // scaling can now only be done on x- and y-axis separately
        detailHistoryBinding.chartStatistical.setPinchZoom(false);
        detailHistoryBinding.chartStatistical.setDoubleTapToZoomEnabled(false);
        // draw shadows for each bar that show the maximum value
        // detailHistoryBinding.chartStatistical.setDrawBarShadow(true);

        detailHistoryBinding.chartStatistical.setDrawGridBackground(false);

        XAxis xl = detailHistoryBinding.chartStatistical.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setTypeface(mTfLight);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(false);
        xl.setGranularity(10f);
        xl.setValueFormatter((value, axis) -> {
            String name = detailPoints.get(
                    (int) (value / 10)).name;
            if (name.length() > 10) {
                name = name.substring(0, 10) + "...";
            }
            return name;
        });
        YAxis yl = detailHistoryBinding.chartStatistical.getAxisLeft();
        yl.setTypeface(mTfLight);
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(true);
        yl.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//        yl.setInverted(true);

        YAxis yr = detailHistoryBinding.chartStatistical.getAxisRight();
        yr.setTypeface(mTfLight);
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(false);
        yr.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        yr.setInverted(true);

        setDataDetailPoint();
        detailHistoryBinding.chartStatistical.setFitBars(true);
        detailHistoryBinding.chartStatistical.animateY(2500);

        Legend l = detailHistoryBinding.chartStatistical.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setXEntrySpace(4f);
        detailHistoryBinding.chartStatistical.setNoDataText(getString(R.string.no_data));
    }

    private static final int[] MY_COLORS = {
            rgb("#ff5252"), rgb("#aa00ff"), rgb("#304ffe"), rgb("#0091ea"),
            rgb("#00c853"), rgb("#ffd600"), rgb("#ff6e40"), rgb("#455a64")};

    private void setDataDetailPoint() {
        float barWidth = 9f;
        float spaceForBar = 10f;
        ArrayList<BarEntry> yVals1 = new ArrayList<>();

        for (int i = detailPoints.size() - 1; i >= 0; i--) {
            float val = detailPoints.get(i).getRatio();
//            if (i == statisticalPoints.size() - 1) {
//                yVals1.add(new BarEntry(i * spaceForBar, 100,
//                        statisticalPoints.get(i).getCateName()));
//            } else {
//                yVals1.add(new BarEntry(i * spaceForBar, val,
//                        statisticalPoints.get(i).getCateName()));
//            }
            yVals1.add(new BarEntry(i * spaceForBar, val,
                    detailPoints.get(i).name));
        }

        BarDataSet set1;

        if (detailHistoryBinding.chartStatistical.getData() != null &&
                detailHistoryBinding.chartStatistical.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) detailHistoryBinding.chartStatistical.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            detailHistoryBinding.chartStatistical.getData().notifyDataChanged();
            detailHistoryBinding.chartStatistical.notifyDataSetChanged();
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
            detailHistoryBinding.chartStatistical.setData(data);
        }
    }

}
