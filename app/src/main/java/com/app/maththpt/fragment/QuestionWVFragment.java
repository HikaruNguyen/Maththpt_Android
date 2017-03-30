package com.app.maththpt.fragment;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.maththpt.R;
import com.app.maththpt.databinding.FragmentQuestionBinding;
import com.app.maththpt.eventbus.ShareQuestionEvent;
import com.app.maththpt.eventbus.CheckAnswerQuestionEvent;
import com.app.maththpt.model.Question;
import com.app.maththpt.utils.MathUtils;
import com.app.maththpt.utils.Utils;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
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
    private Question question;
    private int position;
    ShareDialog shareDialog;
    public FragmentQuestionBinding fragmentQuestionBinding;
    private boolean isCheckedKQ = false;
    CallbackManager callbackManager;

    public QuestionWVFragment() {
        // Required empty public constructor
    }

    public static QuestionWVFragment newInstance(Question question, int position) {
        QuestionWVFragment fragment = new QuestionWVFragment();
        Bundle args = new Bundle();
        args.putSerializable(QUESTION, question);
        args.putInt(POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            question = (Question) getArguments().getSerializable(QUESTION);
            position = getArguments().getInt(POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentQuestionBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_question, container, false);
        View view = fragmentQuestionBinding.getRoot();
        bindData();
        return view;
    }


    private void bindData() {
        callbackManager = CallbackManager.Factory.create();
        fragmentQuestionBinding.webView.setProgress_wheel(fragmentQuestionBinding.progressWheel);
        fragmentQuestionBinding.webView.setPosition(position - 1);
        shareDialog = new ShareDialog(getActivity());
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Toast.makeText(getActivity(), getString(R.string.shareSucess), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
//                Toast.makeText(getActivity(), "cancel", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                error.printStackTrace();
//                Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
            }
        });
        if (question != null) {
//            Collections.shuffle(question.answerList);
            MathUtils mathUtils = new MathUtils();
            mathUtils.question = "<b>" + getString(R.string.questionNo) + " " + position + "</b>: "
                    + Utils.replaceMath(question.question.trim());
            mathUtils.answer1 = Utils.replaceMath(question.answerList.get(0).answer.trim());
            mathUtils.answer2 = Utils.replaceMath(question.answerList.get(1).answer.trim());
            mathUtils.answer3 = Utils.replaceMath(question.answerList.get(2).answer.trim());
            mathUtils.answer4 = Utils.replaceMath(question.answerList.get(3).answer.trim());
            if (question.image != null
                    && !question.image.trim().isEmpty()
                    && question.image.startsWith("data")) {
                mathUtils.image = question.image;
            } else {
                mathUtils.image = "";
            }
            fragmentQuestionBinding.webView.loadDataWithBaseURL(
                    "file:///android_asset/",
                    mathUtils.htmlContain(),
                    "text/html", "UTF-8", null);
        }

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
            bitmap = Utils.getScreenShot(fragmentQuestionBinding.webView);
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
    public void onEvent(CheckAnswerQuestionEvent event) {
        if (event.type == CheckAnswerQuestionEvent.TYPE_CHECK) {
            if (position == event.position || event.isCheckAll) {
                if (!isCheckedKQ || event.isCheckAll) {
                    if (question.answerList.get(0).isCorrect) {
                        fragmentQuestionBinding.webView.loadUrl(
                                "javascript:setColor(checkAnswer(),1);");
                    } else if (question.answerList.get(1).isCorrect) {
                        fragmentQuestionBinding.webView.loadUrl(
                                "javascript:setColor(checkAnswer(),2);");
                    } else if (question.answerList.get(2).isCorrect) {
                        fragmentQuestionBinding.webView.loadUrl(
                                "javascript:setColor(checkAnswer(),3);");
                    } else if (question.answerList.get(3).isCorrect) {
                        fragmentQuestionBinding.webView.loadUrl(
                                "javascript:setColor(checkAnswer(),4);");
                    }
                } else {
                    fragmentQuestionBinding.webView.loadUrl("javascript:resetColor();");
                }
                isCheckedKQ = event.isCheckAll || !isCheckedKQ;

            }
        } else if (event.type == CheckAnswerQuestionEvent.TYPE_DETAIL) {
            Toast.makeText(
                    getActivity(),
                    getString(R.string.no_data),
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
