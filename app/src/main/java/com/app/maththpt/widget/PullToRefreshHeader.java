package com.app.maththpt.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.app.maththpt.R;
import com.app.maththpt.utils.CLog;

import com.pnikosis.materialishprogress.ProgressWheel;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

/**
 * Created by FRAMGIA\hoang.ngoc.dai on 26/10/2016.
 */

public class PullToRefreshHeader extends RelativeLayout implements PtrUIHandler {

    private ProgressWheel progressWheel;
    private LayoutParams layoutParams;

    private int currentPosY;

    public int getCurrentPosY() {
        return currentPosY;
    }

    public PullToRefreshHeader(Context context) {
        super(context);
        initViews();
    }

    public PullToRefreshHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public PullToRefreshHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    protected void initViews() {
        View header = LayoutInflater.from(getContext()).inflate(R.layout.pull_to_refresh_header,
                this);
        progressWheel = (ProgressWheel) header.findViewById(R.id.progress_wheel);
        layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(layoutParams);
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
        CLog.d("PullToRefreshHeader", "onUIReset");
        progressWheel.setVisibility(VISIBLE);
        layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(layoutParams);
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        CLog.d("PullToRefreshHeader", "onUIRefreshPrepare");
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        CLog.d("PullToRefreshHeader", "onUIRefreshBegin");
        progressWheel.spin();
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        CLog.d("PullToRefreshHeader", "onUIRefreshComplete");
        progressWheel.setVisibility(GONE);
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch,
                                   byte status, PtrIndicator ptrIndicator) {
        currentPosY = ptrIndicator.getCurrentPosY();
        float percent = Math.min(1f, ptrIndicator.getCurrentPercent());
        if (status == PtrFrameLayout.PTR_STATUS_PREPARE) {
            progressWheel.setProgress(percent);

        }
        if (status == PtrFrameLayout.PTR_STATUS_PREPARE
                || status == PtrFrameLayout.PTR_STATUS_LOADING) {
            if (ptrIndicator.getCurrentPercent() > 1f) {
                layoutParams.bottomMargin =
                        ptrIndicator.getCurrentPosY() - ptrIndicator.getHeaderHeight();
                layoutParams.topMargin =
                        ptrIndicator.getHeaderHeight() - ptrIndicator.getCurrentPosY();
                setLayoutParams(layoutParams);
            }
        }
        invalidate();
    }
}
