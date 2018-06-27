package com.jdry.zhcm.mvp.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jdry.zhcm.R;
import com.jdry.zhcm.beans.AlarmBean;
import com.jdry.zhcm.beans.ApiParameter;
import com.jdry.zhcm.beans.CommonBean;
import com.jdry.zhcm.http.IService;
import com.jdry.zhcm.http.RetrofitUtil;
import com.jdry.zhcm.mvp.presenter.PresenterManager;
import com.jdry.zhcm.mvp.view.activity.AlarmDetailActivity;
import com.jdry.zhcm.mvp.view.adpater.CommonAdapter;
import com.jdry.zhcm.mvp.view.adpater.ViewHolder;
import com.jdry.zhcm.utils.JdryTime;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by JDRY_SJM on 2017/4/20.
 */

public class AlarmFragment extends SjmBaseFragment {

    @BindView(R.id.lv_alarm)
    ListView lvAlarm;
    @BindView(R.id.no_data)
    View noDataView;
    @BindView(R.id.smart_layout)
    SmartRefreshLayout smartLayout;

    private CommonAdapter<AlarmBean> adapter;

    private List<AlarmBean> list = new ArrayList<>();
    private ApiParameter apiParameter = new ApiParameter();
    private int limit = 10;
    private int mPage = 0;
    private int offset = 0;
    private boolean isLoadMore = false;

    @Override
    public int getResourceId() {
        return R.layout.fragment_alarm;
    }

    @Override
    protected void onCreateViewByMe(Bundle savedInstanceState) {
        initSmartRefreshLayout(smartLayout, true);
        initData();
        httpRequest();
    }

    @Override
    public void refresh() {
        mPage = 0;
        isLoadMore = false;
        httpRequest();
    }

    @Override
    public void loadMore() {
        mPage++;
        isLoadMore = true;
        httpRequest();
    }

    private void initData() {
        adapter = new CommonAdapter<AlarmBean>(getContext(), list, R.layout.alarm_item) {
            @Override
            public void convert(ViewHolder viewHolder, AlarmBean item) {
                if (item == null) {
                    return;
                }
                viewHolder.setText(R.id.tv_unit, item.getDeviceName());
                viewHolder.setText(R.id.tv_time, JdryTime.getDayHourMinSec(item.getAlertTime()));
            }
        };
        lvAlarm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.putExtra("bean", list.get(i));
                openNewActivityByIntent(AlarmDetailActivity.class, intent);
            }
        });
        lvAlarm.setAdapter(adapter);
    }

    private void httpRequest() {
        showProgress();
        offset = limit * mPage;
        apiParameter.setLimit(limit);
        apiParameter.setOffset(offset);
        PresenterManager.getInstance()
                .setmIView(this)
                .setCall(RetrofitUtil.getInstance()
                        .createReq(IService.class)
                        .getAlerts(apiParameter))
                .request();
    }

    @Override
    public <T> void httpSuccessRender(T t, int order) {
        hideProgress();
        stopLayout(isLoadMore);
        CommonBean commonBean = (CommonBean) t;
        JSONObject jsonObject = JSON.parseObject(commonBean.getData().toString());
        String rowsValue = jsonObject.getString("rows");
        List<AlarmBean> alarmBeans = JSON.parseArray(rowsValue, AlarmBean.class);

        if (!isLoadMore) {
            if (alarmBeans == null || alarmBeans.size() == 0) {
                lvAlarm.setVisibility(View.GONE);
                noDataView.setVisibility(View.VISIBLE);
                return;
            }
            lvAlarm.setVisibility(View.VISIBLE);
            noDataView.setVisibility(View.GONE);
            list = alarmBeans;
            adapter.setItems(list);
            return;
        }

        if (alarmBeans == null || alarmBeans.size() == 0) {
            toast("已经没有更多数据");
            return;
        }

        list.addAll(alarmBeans);
        adapter.setItems(list);
    }

    @Override
    public <T> void httpFailureRender(T t, int order) {
        toast((String) t);
        hideProgress();
        stopLayout(isLoadMore);
    }

}
