package com.jdry.zhcm.mvp.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import com.jdry.zhcm.global.JDRYDYApplication;
import com.jdry.zhcm.mvp.view.IView;
import com.jdry.zhcm.mvp.view.custom.RichText;
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

public abstract class SjmBaseActivity extends AppCompatActivity implements IView {
    public abstract int getResouceId();

    protected abstract void onCreateByMe(Bundle savedInstanceState);

    private Unbinder unbinder;

    private SjmProgressBar jdryProgressBar;

    private RefreshLayout mRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResouceId());
        unbinder = ButterKnife.bind(this);
        onCreateByMe(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        closeActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        //leakCanary检测内存泄漏的方法
        RefWatcher refWatcher = JDRYDYApplication.getRefWatcher(this);
        refWatcher.watch(this);
        ToastUtils.hideToast();
        AppManager.getAppManager().finishActivity(this);
    }

    /**
     * 简单的返回前一个页面
     *
     * @param richText
     */
    public void setTopBar(final Activity activity, TextView textView, String title, RichText richText) {
        textView.setText(title);
        richText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeActivity(activity);
            }
        });
    }

    /**
     * 设置状态栏透明
     */
    public void setStatusBarTransparent() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

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

    public void openNewActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public void openNewActivityByIntent(Class<?> cls, Intent intent) {
        if (null == intent) {
            return;
        }
        intent.setClass(this, cls);
        startActivity(intent);
    }

    public void openNewActivity(Class<?> cls) {
        openNewActivity(cls, null);
    }

    public void closeActivity() {
        AppManager.getAppManager().finishActivity();
    }

    public void closeActivity(Activity activity) {
        AppManager.getAppManager().finishActivity(activity);
    }

    @Override
    public void showProgress() {
        if (jdryProgressBar == null) {
            jdryProgressBar = SjmProgressBar.show(this);
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
        ToastUtils.toast(this, desc);
    }

    public int getScreenWidth() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public int getScreenHeight() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    public void refresh(int pageNum) {
        stopRefreshLayout();
    }

    public void loadMore(int pageNum) {
        stopRefreshLoadMore();
    }

    public void initSmartRefreshLayout(SmartRefreshLayout smartRefreshLayout, boolean isLoadMore, final int page) {
        smartRefreshLayout.setRefreshHeader(new ClassicsHeader(this));
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                setRefreshLayout(refreshlayout);
                refresh(page);
            }
        });
        if (isLoadMore) {
            smartRefreshLayout.setRefreshFooter(new ClassicsFooter(this));
            smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
                @Override
                public void onLoadmore(RefreshLayout refreshlayout) {
                    setRefreshLayout(refreshlayout);
                    loadMore(page);
                }
            });
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
