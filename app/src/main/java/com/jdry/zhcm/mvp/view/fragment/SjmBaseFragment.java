package com.jdry.zhcm.mvp.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jdry.zhcm.global.JDRYDYApplication;
import com.jdry.zhcm.mvp.view.IView;
import com.jdry.zhcm.mvp.view.custom.SjmProgressBar;
import com.jdry.zhcm.utils.AppManager;
import com.jdry.zhcm.utils.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.leakcanary.RefWatcher;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by JDRY_SJM on 2017/4/17.
 */

public abstract class SjmBaseFragment extends Fragment implements IView {

    private Unbinder unbinder;
    protected Context mContext;
    protected View mRootView;

    private AppManager appManager = AppManager.getAppManager();

    private SjmProgressBar jdryProgressBar = null;

    public abstract int getResourceId();

    protected abstract void onCreateViewByMe(Bundle savedInstanceState);

    private RefreshLayout mRefreshLayout;

    @Override
    public <T> void httpFailureRender(T t, int order) {

    }

    @Override
    public <T> void httpSuccessRender(T t, int order) {

    }

    @Override
    public void uploadSuccess(String filePath) {

    }

    @Override
    public void uploadFailure(String msg) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(getResourceId(), container, false);
        unbinder = ButterKnife.bind(this, mRootView);
        this.mContext = getContext();
        onCreateViewByMe(savedInstanceState);
        return mRootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        //leakCanary检测内存泄漏的方法
        RefWatcher refWatcher = JDRYDYApplication.getRefWatcher(getContext());
        refWatcher.watch(this);
    }

    public void openNewActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(mContext, cls);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public void openNewActivityByIntent(Class<?> cls, Intent intent) {
        if (null == intent) {
            return;
        }
        intent.setClass(mContext, cls);
        startActivity(intent);
    }

    public void openNewActivity(Class<?> cls) {
        openNewActivity(cls, null);
    }

    public void closeActivity() {
        appManager.finishActivity();
    }

    @Override
    public void showProgress() {
        if (jdryProgressBar == null) {
            jdryProgressBar = SjmProgressBar.show(mContext);
        } else {
            jdryProgressBar.show();
        }
    }

    @Override
    public void hideProgress() {
        if (null != jdryProgressBar && jdryProgressBar.isShowing()) {
            jdryProgressBar.dismiss();
        }
    }

    public void toast(String desc) {
        ToastUtils.toast(getContext(), desc);
    }

    public void refresh() {
        stopRefreshLayout();
    }

    public void loadMore() {
        stopRefreshLoadMore();
    }

    public void initSmartRefreshLayout(SmartRefreshLayout smartRefreshLayout, boolean isLoadMore) {
        smartRefreshLayout.setRefreshHeader(new ClassicsHeader(getContext()));
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                setRefreshLayout(refreshlayout);
                refresh();
            }
        });
        if (isLoadMore) {
            smartRefreshLayout.setRefreshFooter(new ClassicsFooter(getContext()));
            smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
                @Override
                public void onLoadmore(RefreshLayout refreshlayout) {
                    setRefreshLayout(refreshlayout);
                    loadMore();
                }
            });
        }
    }

    public void stopLayout(boolean isLoadMore) {
        if (isLoadMore) {
            stopRefreshLoadMore();
        } else {
            stopRefreshLayout();
        }
    }

    public RefreshLayout getRefreshLayout() {
        return mRefreshLayout;
    }

    public void setRefreshLayout(RefreshLayout refreshLayout) {
        if (this.mRefreshLayout == null) {
            this.mRefreshLayout = refreshLayout;
        }
    }

    public void stopRefreshLayout() {
        if (mRefreshLayout == null) {
            return;
        }
        mRefreshLayout.finishRefresh();
    }

    public void stopRefreshLoadMore() {
        if (mRefreshLayout == null) {
            return;
        }
        mRefreshLayout.finishLoadmore();
    }
}
