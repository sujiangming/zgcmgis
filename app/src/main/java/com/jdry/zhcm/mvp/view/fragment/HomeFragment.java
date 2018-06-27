package com.jdry.zhcm.mvp.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.jdry.zhcm.R;
import com.jdry.zhcm.beans.ApiParameter;
import com.jdry.zhcm.beans.AssetBean;
import com.jdry.zhcm.beans.CommonBean;
import com.jdry.zhcm.http.IService;
import com.jdry.zhcm.http.RetrofitUtil;
import com.jdry.zhcm.mvp.presenter.PresenterManager;
import com.jdry.zhcm.mvp.view.activity.AssetDetailActivity;
import com.jdry.zhcm.mvp.view.adpater.HomeAssertAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by JDRY_SJM on 2017/4/20.
 */

public class HomeFragment extends SjmBaseFragment {

    @BindView(R.id.lv_home)
    ExpandableListView lvHome;
    @BindView(R.id.smart_layout)
    SmartRefreshLayout smartLayout;
    @BindView(R.id.no_data)
    View noDataView;

    private ApiParameter apiParameter = new ApiParameter();

    private List<AssetBean> list = new ArrayList<>();

    private HomeAssertAdapter adapter;

    @Override
    public int getResourceId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void onCreateViewByMe(Bundle savedInstanceState) {
        initSmartRefreshLayout(smartLayout, false);
        initAdapter();
        httpRequest();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            httpRequest();
        }
    }

    private void initAdapter() {
        adapter = new HomeAssertAdapter(getContext(), list);
        lvHome.setAdapter(adapter);
        //设置分组项的点击监听事件
        lvHome.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                // 请务必返回 false，否则分组不会展开
                ImageView view1 = view.findViewById(R.id.kpi_back_img);
                String tag = view1.getTag().toString();
                if ("0".equals(tag)) {
                    view1.setTag("1");
                    view1.setImageResource(R.drawable.list_home_item_parent_jiantou_up);
                } else {
                    view1.setTag("0");
                    view1.setImageResource(R.drawable.list_home_item_parent_jiantou_down);
                }
                return false;
            }
        });
        lvHome.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                goDetailWin(groupPosition, childPosition);
                return false;
            }
        });
    }

    private void goDetailWin(int groupPosition, int childPosition) {
        Intent intent = new Intent();
        intent.putExtra("deviceName", list.get(groupPosition).getDeviceName());
        intent.putExtra("deviceX", list.get(groupPosition).getDeviceX());
        intent.putExtra("deviceY", list.get(groupPosition).getDeviceY());
        intent.putExtra("bean", list.get(groupPosition).getChildrens().get(childPosition));
        openNewActivityByIntent(AssetDetailActivity.class, intent);
    }

    private void httpRequest() {
        apiParameter.setType(1);
        PresenterManager.getInstance()
                .setmIView(this)
                .setCall(RetrofitUtil.getInstance()
                        .createReq(IService.class)
                        .getDeviceInfos(apiParameter))
                .request();
    }

    @Override
    public <T> void httpSuccessRender(T t, int order) {
        hideProgress();
        stopRefreshLayout();
        CommonBean commonBean = (CommonBean) t;
        List<AssetBean> alarmBeans = JSON.parseArray(commonBean.getData().toString(), AssetBean.class);
        if (alarmBeans == null || alarmBeans.size() == 0) {
            lvHome.setVisibility(View.GONE);
            noDataView.setVisibility(View.VISIBLE);
            return;
        }
        lvHome.setVisibility(View.VISIBLE);
        noDataView.setVisibility(View.GONE);
        list = alarmBeans;
        adapter.setItemData(list);
    }

    @Override
    public <T> void httpFailureRender(T t, int order) {
        toast((String) t);
        hideProgress();
        stopRefreshLayout();
    }

    @Override
    public void refresh() {
        httpRequest();
    }

    public List<AssetBean> getAssets() {
        return list;
    }
}
