package com.app.maththpt.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.widget.TextView;

import com.app.maththpt.R;
import com.app.maththpt.adapter.ChiTietDiemAdapter;
import com.app.maththpt.model.Category;
import com.app.maththpt.model.ChiTietDiem;
import com.app.maththpt.model.Question;
import com.app.maththpt.widget.DisableScrollRecyclerView;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;

public class ChamDiemActivity extends BaseActivity implements OnChartValueSelectedListener {
    private static final int[] VORDIPLOM_COLORS = {
            rgb("#2ecc71"), rgb("#f1c40f"), Color.rgb(255, 247, 140), Color.rgb(255, 208, 140),
            Color.rgb(140, 234, 255),};
    private TextView tvPoint;
    private List<Question> list;
    private PieChart mChart;
    private Typeface mTfLight;
    private Typeface mTfRegular;
    private float point;
    private List<Category> listCategory;
    private DisableScrollRecyclerView rvChiTiet;
    private ChiTietDiemAdapter adapter;
    private List<ChiTietDiem> chiTietDiems;
    int soCauDung = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cham_diem);
        getData();
        initUI();
        bindData();
    }

    private void getData() {
        Intent intent = getIntent();
        list = intent.getParcelableArrayListExtra("listAnswer");
        listCategory = intent.getParcelableArrayListExtra("listCate");
    }

    private void initChart() {
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);
        mChart.setDragDecelerationFrictionCoef(0.95f);

        mChart.setCenterTextTypeface(mTfLight);
        mChart.setCenterText(generateCenterSpannableText());

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        mChart.setOnChartValueSelectedListener(this);

        setData(soCauDung, list.size());

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

//        mSeekBarX.setOnSeekBarChangeListener(this);
//        mSeekBarY.setOnSeekBarChangeListener(this);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        mChart.setEntryLabelColor(Color.WHITE);
        mChart.setEntryLabelTypeface(mTfRegular);
        mChart.setEntryLabelTextSize(12f);
    }

    private void bindData() {
        getPoint();
        mTfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");
        mTfRegular = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        chiTietDiems = new ArrayList<>();
        for (int i = 0; i < listCategory.size(); i++) {
            int demSum = 0;
            int demTrue = 0;
            for (int j = 0; j < list.size(); j++) {
                if (listCategory.get(i).id == list.get(j).cateID) {
                    demSum++;
                    if (list.get(j).isCorrect) {
                        demTrue++;
                    }
                }

            }
            chiTietDiems.add(new ChiTietDiem(listCategory.get(i).name, demSum, demTrue));
        }
        adapter.addAll(chiTietDiems);
        initChart();
    }


    private void getPoint() {

        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).isCorrect) {
                    soCauDung++;
                }
            }
        }
        point = soCauDung * 10 / list.size();
        tvPoint.setText(getString(R.string.yourPoint) + ": " + String.format("%.2f", point));
    }

    private void initUI() {
        setBackButtonToolbar();
        setTitleToolbar(getString(R.string.yourPoint));
        tvPoint = (TextView) findViewById(R.id.tvPoint);
        mChart = (PieChart) findViewById(R.id.chart1);
        rvChiTiet = (DisableScrollRecyclerView) findViewById(R.id.rvChiTiet);
        rvChiTiet.setDivider();
        adapter = new ChiTietDiemAdapter(this, new ArrayList<ChiTietDiem>());
        rvChiTiet.setAdapter(adapter);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getY() + ", index: " + h.getX()
                        + ", DataSet index: " + h.getDataSetIndex());
    }


    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

    private void setData(int count, float range) {

        float mult = range;

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
//        for (int i = 0; i < count; i++) {
//            entries.add(new PieEntry((float) ((Math.random() * mult) + mult / 5), mParties[i % mParties.length]));
//        }
        entries.add(new PieEntry((float) (soCauDung), "Đúng"));
        entries.add(new PieEntry((float) (list.size() - soCauDung), "Sai"));
        PieDataSet dataSet = new PieDataSet(entries, "Đáp án");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(mTfLight);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }

    private SpannableString generateCenterSpannableText() {
        float PhanTram = (float) soCauDung * 100 / list.size();
        String ss = String.format("%.1f", PhanTram) + "%";
        SpannableString s = new SpannableString(ss);
//        SpannableString s = new SpannableString("" + point);
        s.setSpan(new RelativeSizeSpan(1.7f), 0, s.length(), 0);
//        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
//        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
//        s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
//        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
//        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
        return s;
    }
}
