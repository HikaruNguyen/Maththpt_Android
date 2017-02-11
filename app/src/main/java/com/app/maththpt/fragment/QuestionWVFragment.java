package com.app.maththpt.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.app.maththpt.R;
import com.app.maththpt.eventbus.ShareQuestionEvent;
import com.app.maththpt.eventbus.XemDapAnEvent;
import com.app.maththpt.model.Question;
import com.app.maththpt.utils.MathUtils;
import com.app.maththpt.utils.Utils;
import com.app.maththpt.widget.CustomeWebView;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionWVFragment extends Fragment {
    private static final String QUESTION = "QUESTION";
    private static final String POSITION = "POSITION";
    private static final String ISXEMKQ = "ISXEMKQ";
    public CustomeWebView webView;
    private Button btnCheck;
    public static ProgressDialog progressDialog;
    private boolean isXemKQ = false;
    private Question question;
    private int position;
    ShareDialog shareDialog;

    public QuestionWVFragment() {
        // Required empty public constructor
    }

    public static QuestionWVFragment newInstance(Question question, int position, boolean isXemKQ) {
        QuestionWVFragment fragment = new QuestionWVFragment();
        Bundle args = new Bundle();
        args.putSerializable(QUESTION, question);
        args.putInt(POSITION, position);
        args.putBoolean(ISXEMKQ, isXemKQ);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            question = (Question) getArguments().getSerializable(QUESTION);
            position = getArguments().getInt(POSITION);
            isXemKQ = getArguments().getBoolean(ISXEMKQ);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initUI(view);
        bindData();
        event();
        return view;
    }

    private void event() {
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String loadingUrl = "javascript:checkAnswer1()";
                webView.loadUrl(loadingUrl);
            }
        });
    }

    private void bindData() {
//        webView.loadUrl("file:///android_asset/MathView/demo.html");
        shareDialog = new ShareDialog(getActivity());
        if (question != null) {
//            Collections.shuffle(question.answerList);
            MathUtils mathUtils = new MathUtils();
            mathUtils.question = getString(R.string.questionNo) + " " + position + ": " + Utils.replaceMath(question.question.trim());
            mathUtils.answer1 = Utils.replaceMath(question.answerList.get(0).answer.trim());
            mathUtils.answer2 = Utils.replaceMath(question.answerList.get(1).answer.trim());
            mathUtils.answer3 = Utils.replaceMath(question.answerList.get(2).answer.trim());
            mathUtils.answer4 = Utils.replaceMath(question.answerList.get(3).answer.trim());
            if (question.image != null && !question.image.trim().isEmpty()) {
                mathUtils.image = question.image;
            }
            webView.loadDataWithBaseURL("file:///android_asset/", mathUtils.htmlContain(), "text/html", "UTF-8", null);
        }

    }

    private void initUI(View view) {
        webView = (CustomeWebView) view.findViewById(R.id.webView);
        webView.setPosition(position - 1);
        btnCheck = (Button) view.findViewById(R.id.btnCheck);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDetach() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDetach();
    }

    Bitmap bitmap;

    @Subscribe
    public void onEvent(ShareQuestionEvent event) {
        if (position == event.position) {
            bitmap = Utils.getScreenShot(webView);
            if (ShareDialog.canShow(ShareLinkContent.class)) {
                SharePhoto photo = new SharePhoto.Builder()
                        .setBitmap(bitmap)
                        .build();
                SharePhotoContent content = new SharePhotoContent.Builder()
                        .addPhoto(photo)
                        .build();

                shareDialog.show(content);
            }
        }
    }

    @Subscribe
    public void onEvent(XemDapAnEvent event) {
        if (position == event.position) {
            if (question.answerList.get(0).isCorrect) {
                webView.loadUrl("javascript:setColor(checkAnswer(),1);");
            } else if (question.answerList.get(1).isCorrect) {
                webView.loadUrl("javascript:setColor(checkAnswer(),2);");
            } else if (question.answerList.get(2).isCorrect) {
                webView.loadUrl("javascript:setColor(checkAnswer(),3);");
            } else if (question.answerList.get(3).isCorrect) {
                webView.loadUrl("javascript:setColor(checkAnswer(),4);");
            }
        }
    }

}
