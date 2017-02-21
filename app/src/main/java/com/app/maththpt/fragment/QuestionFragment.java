package com.app.maththpt.fragment;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.app.maththpt.R;
import com.app.maththpt.activity.QuestionActivity;
import com.app.maththpt.eventbus.ShareQuestionEvent;
import com.app.maththpt.eventbus.XemDapAnEvent;
import com.app.maththpt.model.Question;
import com.app.maththpt.utils.Utils;
import com.app.maththpt.widget.MathView;
import com.app.maththpt.widget.ResizableImageViewByWidth;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionFragment extends Fragment {
    private static final String QUESTION = "QUESTION";
    private static final String POSITION = "POSITION";
    private static final String ISXEMKQ = "ISXEMKQ";
    private MathView tv_question, tv_answerA, tv_answerB, tv_answerC, tv_answerD;
    private RadioButton rbA, rbB, rbC, rbD;
    private ResizableImageViewByWidth image;
    private LinearLayout lnA, lnB, lnC, lnD;
    private boolean isXemKQ = false;
    private Question question;
    private int position;
    ShareDialog shareDialog;
    private LinearLayout lnQuestion;
    private boolean isCheckedKQ = false;

    public QuestionFragment() {
        // Required empty public constructor
    }

    public static QuestionFragment newInstance(Question question, int position, boolean isXemKQ) {
        QuestionFragment fragment = new QuestionFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_on_tap, container, false);
        initUI(view);
        bindData();
        event();
        return view;
    }

    private void event() {
        rbA.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                rbB.setChecked(false);
                rbC.setChecked(false);
                rbD.setChecked(false);
                if (question.answerList.get(0).isCorrect) {
                    ((QuestionActivity) getActivity()).list.get(position - 1).isCorrect = true;
                    Toast.makeText(getActivity(), "True", Toast.LENGTH_SHORT).show();
                } else {
                    ((QuestionActivity) getActivity()).list.get(position - 1).isCorrect = false;
                }
            }

        });
        rbB.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                rbA.setChecked(false);
                rbC.setChecked(false);
                rbD.setChecked(false);
                if (question.answerList.get(1).isCorrect) {
                    ((QuestionActivity) getActivity()).list.get(position - 1).isCorrect = true;
                    Toast.makeText(getActivity(), "True", Toast.LENGTH_SHORT).show();
                } else {
                    ((QuestionActivity) getActivity()).list.get(position - 1).isCorrect = false;
                }
            }
        });
        rbC.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                rbB.setChecked(false);
                rbA.setChecked(false);
                rbD.setChecked(false);
                if (question.answerList.get(2).isCorrect) {
                    ((QuestionActivity) getActivity()).list.get(position - 1).isCorrect = true;
                    Toast.makeText(getActivity(), "True", Toast.LENGTH_SHORT).show();
                } else {
                    ((QuestionActivity) getActivity()).list.get(position - 1).isCorrect = false;
                }
            }

        });
        rbD.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                rbB.setChecked(false);
                rbC.setChecked(false);
                rbA.setChecked(false);
                if (question.answerList.get(3).isCorrect) {
                    ((QuestionActivity) getActivity()).list.get(position - 1).isCorrect = true;
                    Toast.makeText(getActivity(), "True", Toast.LENGTH_SHORT).show();
                } else {
                    ((QuestionActivity) getActivity()).list.get(position - 1).isCorrect = false;
                }
            }

        });
    }

    private void bindData() {

        shareDialog = new ShareDialog(getActivity());
        if (question != null) {
//            Collections.shuffle(question.answerList);
            tv_question.setText(getString(R.string.questionNo) + " " + position + ": " + Utils.replaceMath(question.question));
            tv_answerA.setText(Utils.replaceMath(question.answerList.get(0).answer));
            tv_answerB.setText(Utils.replaceMath(question.answerList.get(1).answer));
            tv_answerC.setText(Utils.replaceMath(question.answerList.get(2).answer));
            tv_answerD.setText(Utils.replaceMath(question.answerList.get(3).answer));
            if (question.image != null && !question.image.trim().isEmpty()) {
                image.setVisibility(View.VISIBLE);
                image.setImageBitmap(Utils.decodeBase64(question.image));
            } else {
                image.setVisibility(View.GONE);
            }

        }

    }

    private void initUI(View view) {
        tv_question = (MathView) view.findViewById(R.id.tv_question);
        tv_answerA = (MathView) view.findViewById(R.id.tv_answerA);
        tv_answerB = (MathView) view.findViewById(R.id.tv_answerB);
        tv_answerC = (MathView) view.findViewById(R.id.tv_answerC);
        tv_answerD = (MathView) view.findViewById(R.id.tv_answerD);

        rbA = (RadioButton) view.findViewById(R.id.rbA);
        rbB = (RadioButton) view.findViewById(R.id.rbB);
        rbC = (RadioButton) view.findViewById(R.id.rbC);
        rbD = (RadioButton) view.findViewById(R.id.rbD);

        image = (ResizableImageViewByWidth) view.findViewById(R.id.image);

        lnA = (LinearLayout) view.findViewById(R.id.lnA);
        lnB = (LinearLayout) view.findViewById(R.id.lnB);
        lnC = (LinearLayout) view.findViewById(R.id.lnC);
        lnD = (LinearLayout) view.findViewById(R.id.lnD);
        lnQuestion = (LinearLayout) view.findViewById(R.id.lnQuestion);
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

    @Subscribe
    public void onEvent(XemDapAnEvent event) {
        if (event.type == XemDapAnEvent.TYPE_CHECK) {
            if (!isCheckedKQ) {
                if (position == event.position) {
                    if (question.answerList.get(0).isCorrect) {
                        lnA.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.background_true));
                    } else if (question.answerList.get(1).isCorrect) {
                        lnB.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.background_true));
                    } else if (question.answerList.get(2).isCorrect) {
                        lnC.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.background_true));
                    } else if (question.answerList.get(3).isCorrect) {
                        lnD.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.background_true));
                    }
                    if (rbA.isChecked() && !question.answerList.get(0).isCorrect) {
                        lnA.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.background_false));
                    } else if (rbB.isChecked() && !question.answerList.get(1).isCorrect) {
                        lnB.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.background_false));
                    } else if (rbC.isChecked() && !question.answerList.get(2).isCorrect) {
                        lnC.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.background_false));
                    } else if (rbD.isChecked() && !question.answerList.get(3).isCorrect) {
                        lnD.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.background_false));
                    }
                }
            } else {
                if (position == event.position) {
                    lnA.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
                    lnB.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
                    lnC.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
                    lnD.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
                }

            }
            isCheckedKQ = !isCheckedKQ;
        } else if (event.type == XemDapAnEvent.TYPE_DETAIL) {
            Toast.makeText(getActivity(), getString(R.string.coming_soon), Toast.LENGTH_SHORT).show();
        }

    }

    Bitmap bitmap;

    @Subscribe
    public void onEvent(ShareQuestionEvent event) {
        if (position == event.position) {
            bitmap = Utils.getScreenShot(lnQuestion);
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


}
