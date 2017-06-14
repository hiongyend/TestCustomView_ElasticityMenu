package com.kincai.testcustomview_elasticitymenu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

/**
 * Author KINCAI
 * .
 * description TODO
 * .
 * Time 2017-06-11 20:20
 */

public class ElasticityMenu {

    private final ViewGroup mParentView;
    private View mRootView;
    private final ElasticityView mElasticityView;
    private final RelativeLayout mMenuRootView;
    private  RelativeLayout mContentView;
    private boolean isShow;
    private boolean mCanceledOnTouchOutside = true;
    public void setCanceledOnTouchOutside(boolean mCanceledOnTouchOutside) {
        this.mCanceledOnTouchOutside = mCanceledOnTouchOutside;
    }

    public ElasticityMenu(View view, int layoutId) {
        mParentView = findRootView(view);
        mRootView = View.inflate(view.getContext(), layoutId, null);
        mElasticityView = (ElasticityView) mRootView.findViewById(R.id.sv);
        mContentView = (RelativeLayout) mRootView.findViewById(R.id.content_view);
        mMenuRootView = (RelativeLayout) mRootView.findViewById(R.id.menu_root_view);
        mMenuRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShow && mCanceledOnTouchOutside) {
                    dismiss();
                }
            }
        });
    }

    /**
     * 找到content view
     *
     * @param view 需要显示的layout布局
     * @return 返回content view
     */
    private ViewGroup findRootView(View view) {
        View contentView = ((Activity) view.getContext()).findViewById(android.R.id.content);
        if (contentView != null) {
            return (ViewGroup) contentView;
        }

        while (view != null) {
            if (view instanceof FrameLayout) {
                if (view.getId() == android.R.id.content) {
                    return (ViewGroup) view;
                }
            }

            ViewParent parent = view.getParent();
            view = parent instanceof View ? (View) parent : null;
        }
        return null;
    }

    public static ElasticityMenu makeMenu(View view, int layoutId) {
        return new ElasticityMenu(view, layoutId);
    }

    public ElasticityMenu show() {
        if (mParentView == null) {
            return null;
        }

        if (mRootView.getParent() != null) {
            mParentView.removeView(mRootView);
        }

        mParentView.addView(mRootView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mElasticityView.show();
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mContentView, "translationY", 0, mRootView.getHeight());
        objectAnimator.setDuration(600);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mContentView.setVisibility(View.VISIBLE);
            }
        });
        objectAnimator.start();
        isShow = true;
        return this;
    }

    public void dismiss() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mRootView, "translationY", 0, mRootView.getHeight());
        objectAnimator.setDuration(600);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (mParentView != null) {
                    mParentView.removeView(mRootView);
                    mRootView = null;
                }
            }
        });

        objectAnimator.start();
        isShow = false;
    }
}
