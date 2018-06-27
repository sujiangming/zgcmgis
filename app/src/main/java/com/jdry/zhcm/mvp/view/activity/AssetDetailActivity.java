package com.jdry.zhcm.mvp.view.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.mapapi.utils.OpenClientUtil;
import com.baoyz.actionsheet.ActionSheet;
import com.jdry.zhcm.R;
import com.jdry.zhcm.beans.AssetBean;
import com.jdry.zhcm.beans.BaiduPositionBean;
import com.jdry.zhcm.global.JDRYDYApplication;
import com.jdry.zhcm.http.IService;
import com.jdry.zhcm.http.RetrofitUtil;
import com.jdry.zhcm.jpush.ExampleUtil;
import com.jdry.zhcm.mvp.view.custom.RichText;
import com.jdry.zhcm.service.LocationService;
import com.jdry.zhcm.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.jdry.zhcm.utils.ApkUtils.isAvilible;

/**
 * Created by JDRY-SJM on 2017/12/4.
 */

public class AssetDetailActivity extends SjmBaseActivity {
    @BindView(R.id.rtv_top_bar_back)
    RichText rtvTopBarBack;
    @BindView(R.id.tv_top_bar_title)
    TextView tvTopBarTitle;
    @BindView(R.id.tv_qj_xh_desc)
    TextView tvQjXhDesc;
    @BindView(R.id.tv_id)
    TextView tvId;
    @BindView(R.id.tv_dev_name)
    TextView tvDevName;
    @BindView(R.id.tv_x)
    TextView tvX;
    @BindView(R.id.tv_y)
    TextView tvY;
    @BindView(R.id.tv_alarm_count)
    TextView tvAlarmCount;
    @BindView(R.id.jz_detail_container)
    ScrollView jzDetailContainer;
    @BindView(R.id.iv_line)
    ImageView ivLine;
    @BindView(R.id.jz_btn)
    Button jzBtn;
    @BindView(R.id.ll_btn)
    LinearLayout llBtn;

    @OnClick(R.id.jz_btn)
    public void goDaoHang() {
        if (isLocateSuccess) {
            startNavi(start, end);
        } else {
            waitingDialog = new ProgressDialog(AssetDetailActivity.this);
            isFirstLocationSuccess = true;
            showWaitingDialog();
        }
    }

    private AssetBean.ChildrensBean assetBean;
    private LocationService locationService;
    private LatLng start;
    private LatLng end;
    private boolean isLocateSuccess = false;
    private ProgressDialog waitingDialog;
    private boolean isFirstLocationSuccess = false;
    private List<BaiduPositionBean.ResultBean> dataBeanList = new ArrayList<>();

    @Override
    public int getResouceId() {
        return R.layout.activity_asset_detail;
    }

    @Override
    protected void onCreateByMe(Bundle savedInstanceState) {
        setTopBar(this, tvTopBarTitle, "资产信息", rtvTopBarBack);
        initData();
    }

    @Override
    public void onStop() {
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        getLocation();
    }

    private void initData() {
        assetBean = (AssetBean.ChildrensBean) getIntent().getSerializableExtra("bean");
        String deviceName = getIntent().getStringExtra("deviceName");
        double deviceY = getIntent().getDoubleExtra("deviceY", 0);
        double deviceX = getIntent().getDoubleExtra("deviceX", 0);
        if (assetBean == null) {
            return;
        }

        tvId.setText(assetBean.getDeviceCardNO());
        tvDevName.setText(deviceName + "-" + Utils.getPhaseDesc(assetBean.getPhaseNo()));
        tvX.setText(String.valueOf(deviceX));
        tvY.setText(String.valueOf(deviceY));
        tvAlarmCount.setText(assetBean.getAlarmCount());

        String coords = deviceX + "," + deviceY;
        String url = Utils.getBaiDuServiceUrl(coords);
        transferPosition(url);

        Log.d("getDeviceY():", "{y:" + deviceY + "   x:" + deviceX + "}");

        //end = new LatLng(assetBean.getDeviceY(), assetBean.getDeviceX());
    }

    private void getLocation() {
        locationService = ((JDRYDYApplication) getApplication()).locationService;
        locationService.registerListener(mListener); //注册监听
        locationService.setLocationOption(locationService.getOption());
        locationService.start();// 定位SDK
    }

    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDAbstractLocationListener mListener = new BDAbstractLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                //打印当前位置信息
                ExampleUtil.getLocationInfo(location);
                //判断是否已经获得到当前的坐标
                if (Double.isNaN(location.getLatitude()) && Double.isNaN(location.getLongitude())) {
                    isLocateSuccess = false;
                    return;
                }
                isLocateSuccess = true;
                start = new LatLng(location.getLatitude(), location.getLongitude());
                if (isFirstLocationSuccess) {
                    isFirstLocationSuccess = false;
                    waitingDialog.dismiss();
                    showNavDialog();
                }
            }
        }
    };

    /**
     * 等待Dialog具有屏蔽其他控件的交互能力
     *
     * @setCancelable 为使屏幕不可点击，设置为不可取消(false)
     * 下载等事件完成后，主动调用函数关闭该Dialog
     */
    private void showWaitingDialog() {
        waitingDialog.setTitle("正在获取您当前的位置");
        waitingDialog.setMessage("请您稍等片刻...");
        waitingDialog.setIndeterminate(true);
        waitingDialog.setCancelable(true);
        waitingDialog.show();
    }

    /**
     * 显示提示可以导航对话框
     */
    public void showNavDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("已成功获取您当前位置，您是否需要导航？");
        builder.setTitle("温馨提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startNavi(start, end);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 启动百度地图导航(Native)
     */
    public void startNavi(LatLng start, LatLng end) {
        // 构建 导航参数
        NaviParaOption para = new NaviParaOption()
                .startPoint(start).endPoint(end);//.startName("天安门").endName("百度大厦");
        try {
            if (!isAvilible(this, "com.baidu.BaiduMap")) {
                showChooseItem(para);
                return;
            }
            BaiduMapNavigation.openBaiduMapNavi(para, this);
        } catch (BaiduMapAppNotSupportNaviException e) {
            e.printStackTrace();
            showChooseItem(para);
        }
    }

    private void showChooseItem(final NaviParaOption para) {
        new ActionSheet.Builder(this, getSupportFragmentManager())
                .setCancelButtonTitle("取消")
                .setOtherButtonTitles("去安装百度地图", "启动浏览器导航")
                .setCancelableOnTouchOutside(true)
                .setListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

                    }

                    @Override
                    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                        switch (index) {
                            case 0:
                                OpenClientUtil.getLatestBaiduMapApp(AssetDetailActivity.this);
                                break;
                            case 1:
                                startWebNavi(para);
                                break;
                            default:
                                break;
                        }
                    }
                }).show();
    }

    /**
     * 启动百度地图导航(Web)
     */
    public void startWebNavi(NaviParaOption para) {
        // 构建 导航参数
        BaiduMapNavigation.openWebBaiduMapNavi(para, this);
    }

    private void transferPosition(String url) {
        showProgress();
        RetrofitUtil.getInstance().createReq(IService.class)
                .transferPosition(url)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        hideProgress();
                        if (!response.isSuccessful()) {
                            return;
                        }
                        if (null == response.body()) {
                            return;
                        }
                        try {
                            String json = response.body().string();
                            BaiduPositionBean baiduPositionBean = JSON.parseObject(json, BaiduPositionBean.class);
                            if (null == baiduPositionBean.getResult() || 0 == baiduPositionBean.getResult().size()) {
                                return;
                            }
                            dataBeanList = baiduPositionBean.getResult();
                            end = new LatLng(dataBeanList.get(0).getY(), dataBeanList.get(0).getX());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        hideProgress();
                        Log.d("Throwable:", t.toString());
                    }
                });
    }
}
