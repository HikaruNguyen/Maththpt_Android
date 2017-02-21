package com.app.maththpt.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;

import com.app.maththpt.R;
import com.app.maththpt.adapter.ChiTietDiemAdapter;
import com.app.maththpt.database.HistoryDBHelper;
import com.app.maththpt.database.StatisticalPointDBHelper;
import com.app.maththpt.databinding.ActivityMarkPointBinding;
import com.app.maththpt.model.Category;
import com.app.maththpt.model.ChiTietDiem;
import com.app.maththpt.model.Question;
import com.app.maththpt.model.StatisticalPoint;
import com.app.maththpt.viewmodel.ChamDiemViewModel;
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

public class MarkPointActivity extends BaseActivity implements OnChartValueSelectedListener {
    private static final int[] VORDIPLOM_COLORS = {
            rgb("#2ecc71"), rgb("#f1c40f"), Color.rgb(255, 247, 140), Color.rgb(255, 208, 140),
            Color.rgb(140, 234, 255),};
    private List<Question> list;
    private PieChart mChart;
    private Typeface mTfLight;
    private Typeface mTfRegular;
    private List<Category> listCategory;
    private ChiTietDiemAdapter adapter;
    int soCauDung = 0;
    private ActivityMarkPointBinding chamDiemBinding;
    private ChamDiemViewModel chamDiemViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chamDiemBinding = DataBindingUtil.setContentView(this, R.layout.activity_mark_point);
        chamDiemViewModel = new ChamDiemViewModel(this, getString(R.string.yourPoint));
        chamDiemBinding.setChamDiemViewModel(chamDiemViewModel);
        getData();
        initUI();
        bindData();
        event();
    }

    private void event() {
        chamDiemBinding.btnHistory.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("menu_item", R.id.nav_history);
            setResult(RESULT_OK, intent);
            finish();
        });
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
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        mChart.setOnChartValueSelectedListener(this);

        setData();

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

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
        List<ChiTietDiem> chiTietDiems = new ArrayList<>();
        StatisticalPointDBHelper.StatisticalPointDatabase statisticalPointDatabase
                = new StatisticalPointDBHelper.StatisticalPointDatabase(this);
        statisticalPointDatabase.open();

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
            if (statisticalPointDatabase.isExistCateID(listCategory.get(i).id)) {
                StatisticalPoint statisticalPoint = statisticalPointDatabase.getStatisticalPointByCateID(listCategory.get(i).id);
                statisticalPoint.setTotalQuestion(statisticalPoint.getTotalQuestion() + demSum);
                statisticalPoint.setTotalQuestionTrue(statisticalPoint.getTotalQuestionTrue() + demTrue);
                statisticalPointDatabase.updateStatisticalPointByCateID(statisticalPoint);
            } else {
                StatisticalPoint statisticalPoint = new StatisticalPoint(demTrue, demSum, listCategory.get(i).id);
                statisticalPointDatabase.addStaticticalPoint(statisticalPoint);
            }
        }
        adapter.addAll(chiTietDiems);
        statisticalPointDatabase.close();
        initChart();
    }


    private void getPoint() {

        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).isCorrect) {
                    soCauDung++;
                }
            }
            chamDiemViewModel.setYourPoint(soCauDung * 10 / list.size());
            HistoryDBHelper.HistoryDatabase historyDatabase =
                    new HistoryDBHelper.HistoryDatabase(this);
            historyDatabase.open();
            historyDatabase.addPointToHistory(String.valueOf(soCauDung * 10 / list.size()));
            historyDatabase.close();
        }

    }

    private void initUI() {
        setBackButtonToolbar();
        mChart = chamDiemBinding.chart1;
        chamDiemBinding.rvChiTiet.setDivider();
        adapter = new ChiTietDiemAdapter(this, new ArrayList<>());
        chamDiemBinding.rvChiTiet.setAdapter(adapter);
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

    private void setData() {

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

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
        s.setSpan(new RelativeSizeSpan(1.7f), 0, s.length(), 0);
        return s;
    }
}
