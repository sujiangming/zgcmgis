package com.jdry.zhcm.mvp.view.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.jdry.zhcm.R;
import com.jdry.zhcm.beans.ApiParameter;
import com.jdry.zhcm.beans.AssetBean;
import com.jdry.zhcm.beans.AssetStatusBean;
import com.jdry.zhcm.beans.BaiduPositionBean;
import com.jdry.zhcm.beans.CommonBean;
import com.jdry.zhcm.global.JDRYDYConstants;
import com.jdry.zhcm.http.IService;
import com.jdry.zhcm.http.RetrofitUtil;
import com.jdry.zhcm.mvp.presenter.PresenterManager;
import com.jdry.zhcm.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by JDRY-SJM on 2017/9/6.
 */

public class MapFragment extends SjmBaseFragment {

    @BindView(R.id.mTexturemap)
    TextureMapView mTexturemap;
    @BindView(R.id.tv_map_title)
    TextView tvMapTitle;

    private BaiduMap mBaiduMap;
    private List<Marker> mMarkerList = new ArrayList<>();
    private InfoWindow mInfoWindow;

    // 初始化全局 bitmap 信息，不用时及时 recycle
    BitmapDescriptor normalDb = BitmapDescriptorFactory
            .fromResource(R.drawable.normal_status);
    BitmapDescriptor alarmDd = BitmapDescriptorFactory
            .fromResource(R.drawable.alarm_status);
    BitmapDescriptor offDd = BitmapDescriptorFactory
            .fromResource(R.drawable.off_status);

    private List<AssetStatusBean> assetStatusBeanList = new ArrayList<>();
    private List<BaiduPositionBean.ResultBean> transResultBeans = new ArrayList<>();

    private HomeFragment homeFragment;
    private List<AssetBean> assetBeanList;
    private static final String OPEN_STATUS = "断开";
    private static final String CLOSE_STATUS = "闭合";
    private static final String EXCEPTION_STATUS = "未安装";
    private boolean isGetStatus = false;
    private boolean isTransform = false;

    private ApiParameter apiParameter = new ApiParameter();
    private Map<String, AssetStatusBean> assetStatusBeanMap = new HashMap<>();

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //handler.postDelayed(runnable, 1800000);//每三分钟调用一次180000
            //getDevStas();//调用实时数据接口
            getDeviceInfos();
        }
    };

    @Override
    public int getResourceId() {
        return R.layout.fragment_map;
    }

    @Override
    protected void onCreateViewByMe(Bundle savedInstanceState) {
        homeFragment = (HomeFragment) getActivity().getSupportFragmentManager().findFragmentByTag("homeFragment");
        mBaiduMap = mTexturemap.getMap();
        mTexturemap.showZoomControls(false);
        setLocationMap(26.274873733520508, 107.51338958740234); //定位都匀市
        setLisneter();
        initData();
        handler.postDelayed(runnable, 180000);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mTexturemap != null) {
            mTexturemap.onResume();
        }
    }

    private void initData() {
        assetBeanList = homeFragment.getAssets();//获取资产页面传过来的资产信息-1
        getDevStas();//调用状态更新的接口-2
        transferPositionData();//调用百度坐标转换接口-3
    }

    private void setLocationMap(double y, double x) {
        //设定中心点坐标
        LatLng cenpt = new LatLng(y, x);
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(cenpt)
                .zoom(14)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        MapStatusUpdateFactory.zoomTo(14.0f);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);
    }

    private void transferPositionData() {
        if (assetBeanList == null || assetBeanList.size() == 0) {
            return;
        }
        String positions = "";
        for (int i = 0; i < assetBeanList.size(); i++) {
            AssetBean assetBean = assetBeanList.get(i);
            if (i == (assetBeanList.size() - 1)) {
                positions += assetBean.getDeviceX() + "," + assetBean.getDeviceY();
            } else {
                positions += assetBean.getDeviceX() + "," + assetBean.getDeviceY() + ";";
            }
        }
        String url = Utils.getBaiDuServiceUrl(positions);
        transferPosition(url);
    }

    int count = 0;

    private void transferPosition(String url) {
        count++;
        RetrofitUtil.getInstance().createReq(IService.class)
                .transferPosition(url)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (!response.isSuccessful()) {
                            isTransform = false;
                            return;
                        }
                        if (null == response.body()) {
                            isTransform = false;
                            return;
                        }
                        try {
                            String json = response.body().string();
                            BaiduPositionBean baiduPositionBean = JSON.parseObject(json, BaiduPositionBean.class);
                            if (null == baiduPositionBean.getResult() || 0 == baiduPositionBean.getResult().size()) {
                                isTransform = false;
                                return;
                            }

                            isTransform = true;
                            transResultBeans = baiduPositionBean.getResult();
                            setAssertBean();
                            commonOperate();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d("Throwable:", t.toString());
                    }
                });
    }

    private void getDeviceInfos() {
        apiParameter.setType(1);
        PresenterManager.getInstance()
                .setmIView(this)
                .setCall(RetrofitUtil.getInstance()
                        .createReq(IService.class)
                        .getDeviceInfos(apiParameter))
                .request(JDRYDYConstants.INVOKE_API_SECOND_TIME);
    }

    private void getDevStas() {
        PresenterManager.getInstance()
                .setmIView(this)
                .setCall(RetrofitUtil.getInstance()
                        .createReq(IService.class)
                        .getDevStas())
                .request(JDRYDYConstants.INVOKE_API_DEFAULT_TIME);
    }

    @Override
    public <T> void httpSuccessRender(T t, int order) {
        hideProgress();
        CommonBean commonBean = (CommonBean) t;
        switch (order) {
            case JDRYDYConstants.INVOKE_API_DEFAULT_TIME:
                getDevStatusSuccess(commonBean);
                break;
            case JDRYDYConstants.INVOKE_API_SECOND_TIME:
                getDevInfoSuccess(commonBean);
                break;
        }
    }

    @Override
    public <T> void httpFailureRender(T t, int order) {
        toast((String) t);
        hideProgress();
    }

    private void getDevInfoSuccess(CommonBean commonBean) {
        if (null == commonBean.getData()) {
            return;
        }
        List<AssetBean> alarmBeans = JSON.parseArray(commonBean.getData().toString(), AssetBean.class);
        if (alarmBeans == null || alarmBeans.size() == 0) {
            toast("设备状态暂无更新");
            return;
        }
        assetBeanList = alarmBeans;

        getDevStas();//调用getDevStas()接口进行状态更新
        transferPositionData();//调用百度坐标转换服务

    }

    private void getDevStatusSuccess(CommonBean commonBean) {
        if (null == commonBean.getData()) {
            return;
        }
        List<AssetStatusBean> alarmBeans = JSON.parseArray(commonBean.getData().toString(), AssetStatusBean.class);
        if (alarmBeans == null || alarmBeans.size() == 0) {
            toast(JDRYDYConstants.NO_DATA_TIP);
            return;
        }

        isGetStatus = true;
        assetStatusBeanList = alarmBeans;
        setGetStatus();
        commonOperate();
    }

    private void commonOperate() {
        if (isGetStatus && isTransform) {
            clearOverlay();
            updateMarkerOverlayStatus();
            setMarksPosition();
        }
    }

    private void updateMarkerOverlayStatus() {
        for (int i = 0; i < assetBeanList.size(); i++) {
            AssetBean assetBean = assetBeanList.get(i);
            if (assetStatusBeanMap.containsKey(assetBean.getDeviceId())) {
                assetBean.setDeviceStatus(assetStatusBeanMap.get(assetBean.getDeviceId()).getDeviceStatus());
                assetBean.setA(assetStatusBeanMap.get(assetBean.getDeviceId()).getA());
                assetBean.setB(assetStatusBeanMap.get(assetBean.getDeviceId()).getB());
                assetBean.setC(assetStatusBeanMap.get(assetBean.getDeviceId()).getC());
                assetBeanList.remove(i);
                assetBeanList.add(i, assetBean);
            } else {
                assetBean.setDeviceStatus(-1);
                assetBean.setA(-1);
                assetBean.setB(-1);
                assetBean.setC(-1);
                assetBeanList.remove(i);
                assetBeanList.add(i, assetBean);
            }
        }
    }

    private void setGetStatus() {
        for (int i = 0; i < assetStatusBeanList.size(); i++) {
            AssetStatusBean statusBean = assetStatusBeanList.get(i);
            assetStatusBeanMap.put(statusBean.getDeviceId(), statusBean);
        }
    }

    private void setAssertBean() {
        //Log.d("old assetBeanList:", JSON.toJSONString(assetBeanList));
        for (int i = 0; i < assetBeanList.size(); i++) {
            AssetBean assetBean = assetBeanList.get(i);
            assetBean.setDeviceX(transResultBeans.get(i).getX());
            assetBean.setDeviceY(transResultBeans.get(i).getY());
            assetBeanList.remove(i);
            assetBeanList.add(i, assetBean);
        }
        //Log.d("new assetBeanList:", JSON.toJSONString(assetBeanList));
    }

    private void changeMarkerOverlyBg(Bundle bundle, TextView tv) {
        int status = bundle.getInt("status");
        if (status == 0) {//0：断开 1：闭合 -1：未安装
            tv.setBackgroundColor(0xFFFC7878);
        } else if (status == 1) {
            tv.setBackgroundColor(0xFF209E85);
        } else {
            tv.setBackgroundColor(0xFF19B5D4);
        }
    }

    private void setMarksPosition() {
        for (int i = 0; i < assetBeanList.size(); i++) {
            AssetBean assetBean = assetBeanList.get(i);
            LatLng latLng = new LatLng(assetBean.getDeviceY(), assetBean.getDeviceX());

            MarkerOptions ooA;
            int status = assetBean.getDeviceStatus();
            String statusDesc = "";
            if (status == 0) {//0：断开 1：闭合 2：异常
                ooA = new MarkerOptions().position(latLng).icon(offDd).zIndex(i);
                statusDesc = OPEN_STATUS;
            } else if (status == 1) {
                ooA = new MarkerOptions().position(latLng).icon(normalDb).zIndex(i);
                statusDesc = CLOSE_STATUS;
            } else {
                ooA = new MarkerOptions().position(latLng).icon(alarmDd).zIndex(i);
                statusDesc = EXCEPTION_STATUS;
            }
            // ooA.animateType(MarkerOptions.MarkerAnimateType.drop);// 掉下动画

            Marker marker = (Marker) (mBaiduMap.addOverlay(ooA));
            marker.setTitle(assetBean.getDeviceNo());

            Bundle bundle = new Bundle();
            bundle.putDouble("y", latLng.longitude);
            bundle.putDouble("x", latLng.latitude);
            bundle.putString("name", assetBean.getDeviceName());
            bundle.putInt("status", status);
            bundle.putString("statusDesc", statusDesc);
            bundle.putString("A", getABCStatusDesc(assetBean.getA()));
            bundle.putString("B", getABCStatusDesc(assetBean.getB()));
            bundle.putString("C", getABCStatusDesc(assetBean.getC()));

            marker.setExtraInfo(bundle);

            mMarkerList.add(marker);
        }
    }

    private String getABCStatusDesc(int status) {
        String statusDesc = "";
        if (status == 0) {//0：断开 1：闭合 -1：未安装
            statusDesc = OPEN_STATUS;
        } else if (status == 1) {
            statusDesc = CLOSE_STATUS;
        } else {
            statusDesc = EXCEPTION_STATUS;
        }
        return statusDesc;
    }

    private void setLisneter() {
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            public boolean onMarkerClick(final Marker marker) {
                TextView button = (TextView) View.inflate(getContext(), R.layout.map_info_win, null);

                Bundle bundle = marker.getExtraInfo();
                String name = bundle.getString("name");
                String statusDesc = bundle.getString("statusDesc");
                String A = bundle.getString("A");
                String B = bundle.getString("B");
                String C = bundle.getString("C");

                String desc = "名称:" + name + "\n状态:" + statusDesc + "\n A相：" + A + "\n B相：" + B + "\n C相：" + C;
                button.setText(desc);

                changeMarkerOverlyBg(bundle, button);

                InfoWindow.OnInfoWindowClickListener listener = new InfoWindow.OnInfoWindowClickListener() {
                    public void onInfoWindowClick() {
                        mBaiduMap.hideInfoWindow();
                    }
                };
                LatLng ll = marker.getPosition();
                mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button), ll, -120, listener);
                mBaiduMap.showInfoWindow(mInfoWindow);
                return true;
            }
        });
    }

    /**
     * 清除所有Overlay
     */
    public void clearOverlay() {
        mBaiduMap.clear();
        mMarkerList.clear();
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mTexturemap != null) {
            mTexturemap.onPause();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTexturemap != null) {
            mTexturemap.onDestroy();
        }
        // 回收 bitmap 资源
        normalDb.recycle();
        alarmDd.recycle();
        offDd.recycle();
        //关闭
        handler.removeCallbacks(runnable);
    }
}
