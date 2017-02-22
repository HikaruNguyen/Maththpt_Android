package com.app.maththpt.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.maththpt.R;
import com.app.maththpt.config.Configuaration;
import com.app.maththpt.config.MathThptService;
import com.app.maththpt.databinding.ActivityQuestionBinding;
import com.app.maththpt.eventbus.ShareQuestionEvent;
import com.app.maththpt.eventbus.XemDapAnEvent;
import com.app.maththpt.fragment.QuestionWVFragment;
import com.app.maththpt.model.Answer;
import com.app.maththpt.model.Category;
import com.app.maththpt.model.Question;
import com.app.maththpt.modelresult.DetailTestsResult;
import com.app.maththpt.utils.CLog;
import com.app.maththpt.viewmodel.QuestionViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class QuestionActivity extends BaseActivity {
    private static final String TAG = QuestionActivity.class.getSimpleName();
    private static final int CODE_CHAM_DIEM = 12;
    private static final String FORMAT = "%02d:%02d:%02d";
    public List<Question> list;
    private int type;
    private String title;
    private int cateID;
    private int positionCurrent = 0;
    private List<Category> listCategory;
    private int soCau;
    private long time;
    private CountDownTimer countDownTimer;
    private ImageView[] dots;
    private List<Fragment> fragmentList;
    private ActivityQuestionBinding activityQuestionBinding;
    private QuestionViewModel questionViewModel;
    private String testID;
    private Subscription mSubscription;
    private MathThptService apiService;
    private DetailTestsResult mDetailTestsResult;
    public static ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityQuestionBinding = DataBindingUtil.setContentView(this, R.layout.activity_question);
        getData();
        setSupportActionBar(activityQuestionBinding.toolbar);
        setBackButtonToolbar();
        activityQuestionBinding.setQuestionViewModel(questionViewModel);
        bindData();
        event();
    }

    private void event() {
        activityQuestionBinding.viewpager.setOnPageChangeListener(
                new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(
                            int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        positionCurrent = position;
                        questionViewModel.setPostion(positionCurrent);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });

        activityQuestionBinding.imgNext.setOnClickListener(v ->
                activityQuestionBinding.viewpager.setCurrentItem(positionCurrent + 1));

        activityQuestionBinding.imgPre.setOnClickListener(v ->
                activityQuestionBinding.viewpager.setCurrentItem(positionCurrent - 1));

        activityQuestionBinding.fab.setOnClickListener(v -> {
            if (list != null && list.size() > 0)
                EventBus.getDefault().post(
                        new XemDapAnEvent(positionCurrent + 1, XemDapAnEvent.TYPE_CHECK));
        });
    }

    private void getData() {
        Intent intent = getIntent();
        type = intent.getIntExtra("type", Configuaration.TYPE_ONTAP);
        if (type == Configuaration.TYPE_ONTAP) {
            title = intent.getStringExtra("title");
            cateID = intent.getIntExtra("cateID", 0);
        } else if (type == Configuaration.TYPE_KIEMTRA) {
            title = getString(R.string.exam);
            cateID = 0;
            listCategory = intent.getParcelableArrayListExtra("listCate");
            CLog.d(TAG, "listCategory SIZE:" + listCategory.size());
            soCau = intent.getIntExtra("soCau", 5);
            time = intent.getLongExtra("time", (long) (60 * 1000 * 0.5));
        } else if (type == Configuaration.TYPE_BODE) {
            title = intent.getStringExtra("title");
            testID = intent.getStringExtra("testID");
//            Toast.makeText(this, testID, Toast.LENGTH_SHORT).show();
        }
        questionViewModel = new QuestionViewModel(this, title, type);
    }

    private void countDown() {
        countDownTimer = new CountDownTimer(time, 1000) { // adjust the milli seconds here

            @SuppressLint("DefaultLocale")
            public void onTick(long millisUntilFinished) {
                questionViewModel.setTitle(
                        "" + String.format(
                                FORMAT,
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                                        - TimeUnit.HOURS.toMinutes(
                                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)
                                        - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                questionViewModel.setTitle(getString(R.string.hetGio));
                nopBai();
            }
        };
        countDownTimer.start();

    }

    ViewPagerAdapter pagerAdapter;
    private int page = 1;

    private void bindData() {
        list = new ArrayList<>();
        if (type == Configuaration.TYPE_BODE) {
            apiService = MyApplication.with(this).getMaththptSerivce();
            if (mSubscription != null && !mSubscription.isUnsubscribed())
                mSubscription.unsubscribe();
            mSubscription = apiService.getContentbyTestID(2, testID, page)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<DetailTestsResult>() {
                        @Override
                        public void onCompleted() {
                            convertResultToListQuestion(mDetailTestsResult);
                        }

                        @Override
                        public void onError(Throwable e) {
                            CLog.d(TAG, "getListTest Error");
                            questionViewModel.setVisiableError(true);
                            questionViewModel.setMessageError(getString(R.string.error_connect));
                        }

                        @Override
                        public void onNext(DetailTestsResult detailTestsResult) {
                            if (detailTestsResult != null
                                    && detailTestsResult.success
                                    && detailTestsResult.status == 200) {
                                mDetailTestsResult = detailTestsResult;
                            }
                        }
                    });
        } else if (type == Configuaration.TYPE_ONTAP) {
//                list = QuestionDBHelper.getListQuestionByCateID(QuestionActivity.this, cateID);
            apiService = MyApplication.with(this).getMaththptSerivce();
            if (mSubscription != null && !mSubscription.isUnsubscribed())
                mSubscription.unsubscribe();
            mSubscription = apiService.getContentbyCateID(1, cateID, page)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<DetailTestsResult>() {
                        @Override
                        public void onCompleted() {
                            convertResultToListQuestion(mDetailTestsResult);
                        }

                        @Override
                        public void onError(Throwable e) {
                            CLog.d(TAG, "getListTest Error");
                            questionViewModel.setVisiableError(true);
                            questionViewModel.setMessageError(getString(R.string.error_connect));
                        }

                        @Override
                        public void onNext(DetailTestsResult detailTestsResult) {
                            if (detailTestsResult != null
                                    && detailTestsResult.success
                                    && detailTestsResult.status == 200) {
                                mDetailTestsResult = detailTestsResult;
                            }
                        }
                    });

        } else if (type == Configuaration.TYPE_KIEMTRA) {
//            list = QuestionDBHelper.getListQuestionByListCateID(QuestionActivity.this, listCategory, soCau);
//            if (list.size() > 0) {
//                Collections.shuffle(list);
//            }
            String cateIDs = "";
            if (listCategory.size() > 0) {
                cateIDs = listCategory.get(0).id + "";
            }
            if (listCategory.size() > 1) {
                for (int i = 1; i < listCategory.size(); i++) {
                    cateIDs += "-" + listCategory.get(i).id;
                }
            }
            apiService = MyApplication.with(this).getMaththptSerivce();
            if (mSubscription != null && !mSubscription.isUnsubscribed())
                mSubscription.unsubscribe();
            mSubscription = apiService.getExamByCateID(cateIDs, soCau)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<DetailTestsResult>() {
                        @Override
                        public void onCompleted() {
                            convertResultToListQuestion(mDetailTestsResult);
                        }

                        @Override
                        public void onError(Throwable e) {
                            CLog.d(TAG, "getListTest Error");
                            questionViewModel.setVisiableError(true);
                            questionViewModel.setMessageError(getString(R.string.error_connect));
                        }

                        @Override
                        public void onNext(DetailTestsResult detailTestsResult) {
                            if (detailTestsResult != null
                                    && detailTestsResult.success
                                    && detailTestsResult.status == 200) {
                                mDetailTestsResult = detailTestsResult;
                            }
                        }
                    });
        }


    }

    private void convertResultToListQuestion(DetailTestsResult mDetailTestsResult) {
        if (mDetailTestsResult.data != null && mDetailTestsResult.data.size() > 0) {
            questionViewModel.setVisiableError(false);
            for (int i = 0; i < mDetailTestsResult.data.size(); i++) {
                List<Answer> answerList = new ArrayList<>();
                if (Integer.parseInt(mDetailTestsResult.data.get(i).answerTrue) == 1) {
                    answerList.add(new Answer(mDetailTestsResult.data.get(i).answerA, true));
                    answerList.add(new Answer(mDetailTestsResult.data.get(i).answerB, false));
                    answerList.add(new Answer(mDetailTestsResult.data.get(i).answerC, false));
                    answerList.add(new Answer(mDetailTestsResult.data.get(i).answerD, false));
                } else if (Integer.parseInt(mDetailTestsResult.data.get(i).answerTrue) == 2) {
                    answerList.add(new Answer(mDetailTestsResult.data.get(i).answerA, false));
                    answerList.add(new Answer(mDetailTestsResult.data.get(i).answerB, true));
                    answerList.add(new Answer(mDetailTestsResult.data.get(i).answerC, false));
                    answerList.add(new Answer(mDetailTestsResult.data.get(i).answerD, false));
                } else if (Integer.parseInt(mDetailTestsResult.data.get(i).answerTrue) == 3) {
                    answerList.add(new Answer(mDetailTestsResult.data.get(i).answerA, false));
                    answerList.add(new Answer(mDetailTestsResult.data.get(i).answerB, false));
                    answerList.add(new Answer(mDetailTestsResult.data.get(i).answerC, true));
                    answerList.add(new Answer(mDetailTestsResult.data.get(i).answerD, false));
                } else if (Integer.parseInt(mDetailTestsResult.data.get(i).answerTrue) == 4) {
                    answerList.add(new Answer(mDetailTestsResult.data.get(i).answerA, false));
                    answerList.add(new Answer(mDetailTestsResult.data.get(i).answerB, false));
                    answerList.add(new Answer(mDetailTestsResult.data.get(i).answerC, true));
                    answerList.add(new Answer(mDetailTestsResult.data.get(i).answerD, false));
                }
                Question question = new Question(
                        Integer.parseInt(mDetailTestsResult.data.get(i).id),
                        mDetailTestsResult.data.get(i).question,
                        mDetailTestsResult.data.get(i).image,
                        answerList,
                        Integer.parseInt(mDetailTestsResult.data.get(i).cateID));
                list.add(question);
            }
            genQuestion();
        } else {
            questionViewModel.setVisiableError(true);
            questionViewModel.setMessageError(getString(R.string.no_data));
        }

    }

    private void genQuestion() {
        if (list != null && list.size() > 0) {
            if (type == Configuaration.TYPE_KIEMTRA) {
                Collections.shuffle(list);

            }
            questionViewModel.setVisiableError(false);
            questionViewModel.setSize(list.size());
            questionViewModel.setPostion(positionCurrent);
            fragmentList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                fragmentList.add(QuestionWVFragment.newInstance(list.get(i), i + 1, false));
            }
            pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList, false);
            activityQuestionBinding.viewpager.setAdapter(pagerAdapter);
//            activityQuestionBinding.viewpager.setOffscreenPageLimit(list.size());
            if (type == Configuaration.TYPE_KIEMTRA) {
                countDown();
            }
        } else {
            questionViewModel.setVisiableError(true);
            questionViewModel.setMessageError(getString(R.string.no_data));
        }
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragmentList = new ArrayList<>();
        private boolean isXemKQ;

        ViewPagerAdapter(FragmentManager manager, List<Fragment> fragmentList, boolean isXemKQ) {
            super(manager);
            this.isXemKQ = isXemKQ;
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
//            return fragment;
        }

        @Override
        public int getCount() {
            return list.size();
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (type == Configuaration.TYPE_ONTAP || type == Configuaration.TYPE_BODE) {
            getMenuInflater().inflate(R.menu.menu_quiz, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_kiemtra, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        } else if (id == R.id.action_xemDA) {
            if (list != null && list.size() > 0)
                EventBus.getDefault().post(new XemDapAnEvent(positionCurrent + 1, XemDapAnEvent.TYPE_DETAIL));
        } else if (id == R.id.action_Share) {
            if (list != null && list.size() > 0) {
                EventBus.getDefault().post(new ShareQuestionEvent(positionCurrent + 1));
            }
        } else if (id == R.id.action_nopBai) {
            if (list != null && list.size() > 0) {
                if (countDownTimer != null)
                    countDownTimer.cancel();
                nopBai();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void nopBai() {
        for (int i = 0; i < list.size(); i++) {
            QuestionWVFragment fragment = (QuestionWVFragment) pagerAdapter.getItem(i);
            if (fragment.fragmentQuestionBinding == null
                    || fragment.fragmentQuestionBinding.webView == null
                    || fragment.fragmentQuestionBinding.webView.answer == 0) {
                list.get(i).isCorrect = false;
            } else {
                if (list.get(i).answerList.get(0).isCorrect) {
                    list.get(i).isCorrect = fragment.fragmentQuestionBinding.webView.answer == 1;
                } else if (list.get(i).answerList.get(1).isCorrect) {
                    list.get(i).isCorrect = fragment.fragmentQuestionBinding.webView.answer == 2;
                } else if (list.get(i).answerList.get(2).isCorrect) {
                    list.get(i).isCorrect = fragment.fragmentQuestionBinding.webView.answer == 3;
                } else if (list.get(i).answerList.get(3).isCorrect) {
                    list.get(i).isCorrect = fragment.fragmentQuestionBinding.webView.answer == 4;
                }

            }

        }
        Intent intent = new Intent(this, MarkPointActivity.class);
        intent.putParcelableArrayListExtra(
                "listAnswer", (ArrayList<? extends Parcelable>) list);
        intent.putParcelableArrayListExtra(
                "listCate", (ArrayList<? extends Parcelable>) listCategory);
        startActivityForResult(intent, CODE_CHAM_DIEM);
    }

    @Override
    protected void onDestroy() {
        if (countDownTimer != null)
            countDownTimer.cancel();
        if (QuestionActivity.progressDialog != null
                && QuestionActivity.progressDialog.isShowing()) {
            QuestionActivity.progressDialog.dismiss();
            QuestionActivity.progressDialog = null;
        }
        if (mSubscription != null && !mSubscription.isUnsubscribed()) mSubscription.unsubscribe();
        mSubscription = null;
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_CHAM_DIEM) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK, data);
                finish();
            } else {
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (type == Configuaration.TYPE_KIEMTRA) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getString(R.string.confirmQuitExam));
            builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.setNegativeButton(getString(R.string.late), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        } else {
            finish();
        }
    }
}
