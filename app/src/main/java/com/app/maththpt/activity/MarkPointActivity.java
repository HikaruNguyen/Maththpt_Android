package com.app.maththpt.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.app.maththpt.R;
import com.app.maththpt.adapter.DetailPointAdapter;
import com.app.maththpt.config.Configuaration;
import com.app.maththpt.databinding.ActivityMarkPointBinding;
import com.app.maththpt.model.Category;
import com.app.maththpt.model.DetailPoint;
import com.app.maththpt.model.Point;
import com.app.maththpt.model.Question;
import com.app.maththpt.model.StatisticalPoint;
import com.app.maththpt.realm.HistoryModule;
import com.app.maththpt.realm.StatisticalModule;
import com.app.maththpt.utils.FacebookUtils;
import com.app.maththpt.utils.Utils;
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
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

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
    private DetailPointAdapter adapter;
    int soCauDung = 0;
    private ActivityMarkPointBinding chamDiemBinding;
    private ChamDiemViewModel chamDiemViewModel;
    private String userID;
    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chamDiemBinding = DataBindingUtil.setContentView(this, R.layout.activity_mark_point);
        chamDiemViewModel = new ChamDiemViewModel(this, getString(R.string.yourPoint));
        chamDiemBinding.setChamDiemViewModel(chamDiemViewModel);
        initBanner();
        initInterstitial();
        getData();
        initUI();
        bindData();
        event();
    }

    private void initBanner() {
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdOpened() {
                // Save app state before going to the ad overlay.
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }
        });
        if (Utils.isNetworkConnected(MarkPointActivity.this)) {
            mAdView.setVisibility(View.VISIBLE);
        } else {
            mAdView.setVisibility(View.GONE);
        }
    }

    private void initInterstitial() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });

        requestNewInterstitial();
    }

    private void event() {
        chamDiemBinding.btnHistory.setOnClickListener(v -> {
            String token = getSharedPreferences(Configuaration.Pref, MODE_PRIVATE)
                    .getString(Configuaration.KEY_TOKEN, "");
            if (!FacebookUtils.getFacebookID().isEmpty() || !token.isEmpty()) {
                Intent intent = new Intent();
                intent.putExtra("menu_item", R.id.nav_history);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(this, getString(R.string.needLogin), Toast.LENGTH_SHORT).show();
            }

        });
        chamDiemBinding.btnReview.setOnClickListener(v -> {
            loadInterstitialAd();
            Intent intent = new Intent();
            intent.putExtra("isReview", true);
            setResult(RESULT_OK, intent);
            finish();
        });
    }

    private void loadInterstitialAd() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {

        }

    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("SEE_YOUR_LOGCAT_TO_GET_YOUR_DEVICE_ID")
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    private void getData() {
        Intent intent = getIntent();
        list = intent.getParcelableArrayListExtra("listAnswer");
        listCategory = intent.getParcelableArrayListExtra("listCate");
        SharedPreferences sharedPreferences = getSharedPreferences(Configuaration.Pref, MODE_PRIVATE);
        userID = sharedPreferences.getString(Configuaration.KEY_ID, "");
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
        Realm.init(this);
        getPoint();
        mTfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");
        mTfRegular = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        getStatistical();
        initChart();
    }

    private void getStatistical() {
        List<DetailPoint> chiTietDiems = new ArrayList<>();
//        StatisticalPointDBHelper.StatisticalPointDatabase statisticalPointDatabase
//                = new StatisticalPointDBHelper.StatisticalPointDatabase(this);
//        statisticalPointDatabase.open();
        RealmConfiguration settingConfig = new RealmConfiguration.Builder()
                .name("statisticalPoint.realm")
                .modules(Realm.getDefaultModule(), new StatisticalModule())
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(MyApplication.with(this).REALM_VERSION)
                .build();

        Realm realStatistical = Realm.getInstance(settingConfig);

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
            DetailPoint chiTietDiem = new DetailPoint(listCategory.get(i).name, demSum, demTrue);
            chiTietDiems.add(chiTietDiem);
            if (!userID.isEmpty()) {
                boolean isExistCateId = realStatistical
                        .where(StatisticalPoint.class)
                        .equalTo("cateID", listCategory.get(i).id)
                        .count() > 0;
                if (isExistCateId) {
                    realStatistical.beginTransaction();
                    StatisticalPoint statisticalPoint =
                            realStatistical.where(StatisticalPoint.class)
                                    .equalTo("cateID", listCategory.get(i).id)
                                    .findFirst();
                    statisticalPoint.setTotalQuestion(statisticalPoint.getTotalQuestion() + demSum);
                    statisticalPoint.setTotalQuestionTrue(
                            statisticalPoint.getTotalQuestionTrue() + demTrue);
//                statisticalPointDatabase.updateStatisticalPointByCateID(statisticalPoint);
                    realStatistical.insertOrUpdate(statisticalPoint);
                    realStatistical.commitTransaction();
                } else {
                    StatisticalPoint statisticalPoint =
                            new StatisticalPoint(
                                    demTrue,
                                    demSum,
                                    listCategory.get(i).id,
                                    listCategory.get(i).name,
                                    userID);
                    realStatistical.beginTransaction();
                    realStatistical.insertOrUpdate(statisticalPoint);
                    realStatistical.commitTransaction();

                }
            }

        }
        adapter.addAll(chiTietDiems);
//        statisticalPointDatabase.close();
    }

    private void getPoint() {
        RealmConfiguration settingConfig = new RealmConfiguration.Builder()
                .name("history.realm")
                .modules(Realm.getDefaultModule(), new HistoryModule())
                .schemaVersion(MyApplication.with(this).REALM_VERSION)
//                .migration(new HistoryMigration())
                .build();
        Realm realmHistory = Realm.getInstance(settingConfig);
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).isCorrect) {
                    soCauDung++;
                }
            }
            chamDiemViewModel.setYourPoint((float) (soCauDung * 10.0 / list.size()));
            long dtMili = System.currentTimeMillis();
            if (!userID.isEmpty()) {
                Point point = new Point((float) (soCauDung * 10.0 / list.size()), dtMili + "", userID);
                realmHistory.beginTransaction();
                realmHistory.insert(point);
                realmHistory.commitTransaction();
            }

        }

    }

    private void initUI() {
        setBackButtonToolbar();
        mChart = chamDiemBinding.chart1;
        chamDiemBinding.rvChiTiet.setDivider();
        adapter = new DetailPointAdapter(this, new ArrayList<>());
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
