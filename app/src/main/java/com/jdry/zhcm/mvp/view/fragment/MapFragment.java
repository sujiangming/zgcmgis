package com.jdry.zhcm.mvp.view.fragment;

import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.baidu.mapapi.animation.Animation;
import com.baidu.mapapi.animation.Transformation;
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
import com.jdry.zhcm.beans.CommonBean;
import com.jdry.zhcm.beans.DeviceBean;
import com.jdry.zhcm.beans.JingWeidu;
import com.jdry.zhcm.global.JDRYDYApplication;
import com.jdry.zhcm.http.IService;
import com.jdry.zhcm.http.RetrofitUtil;
import com.jdry.zhcm.mvp.view.custom.GISDialog;
import com.jdry.zhcm.rxbus.RxBus;
import com.jdry.zhcm.rxbus.RxBusSubscriber;
import com.jdry.zhcm.rxbus.RxSubscriptions;
import com.jdry.zhcm.utils.LogUtils;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscription;
import rx.functions.Func1;


/**
 * Created by JDRY-SJM on 2017/9/6.
 */

public class MapFragment extends SjmBaseFragment {

    @BindView(R.id.mTexturemap)
    TextureMapView mTexturemap;

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

    private RetrofitUtil retrofitUtil = new RetrofitUtil();
    private JSONObject jsonObject = new JSONObject();
    private List<DeviceBean.DataBean> deviceBeanList = new ArrayList<>();

    private static final String OPEN_STATUS = "断开";
    private static final String CLOSE_STATUS = "闭合";
    private static final String EXCEPTION_STATUS = "未安装";

    @Override
    public int getResourceId() {
        return R.layout.fragment_map;
    }

    private GISDialog selfDialog = null;

    @Override
    public void onResume() {
        super.onResume();
        if (mTexturemap != null) {
            mTexturemap.onResume();
        }
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

    private Marker tempCommonMark = null;
    private Subscription mRxSub;

    @Override
    protected void onCreateViewByMe(Bundle savedInstanceState) {
        showProgress();
        mBaiduMap = mTexturemap.getMap();
        mBaiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                jsonObject.put("search", "");
                getDeviceAllList(jsonObject.toJSONString());
            }
        });

        mTexturemap.showZoomControls(false);

        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus status) {

            }

            @Override
            public void onMapStatusChangeStart(MapStatus status, int reason) {

            }

            @Override
            public void onMapStatusChange(MapStatus status) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus status) {
                if (null == tempCommonMark) {
                    return;
                }
                Log.d("MapStatus", status.toString());
                tempCommonMark.setPosition(status.target);
                tempCommonMark.setDraggable(false);
                tempCommonMark.setAnimation(getTransformationPoint(status, tempCommonMark, tempCommonMark.getFixedPosition()));
                tempCommonMark.startAnimation();
            }
        });

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            public boolean onMarkerClick(final Marker marker) {
                showDeleteDialog(marker);
                return true;
            }
        });

        JingWeidu jingWeidu = JDRYDYApplication.getDaoSession().getJingWeiduDao().queryBuilder().unique();
        if (null != jingWeidu) {
            setLocationMap(jingWeidu.getY(), jingWeidu.getX()); //定位当前城市
        } else {
            setLocationMap(26.274873733520508, 107.51338958740234); //定位都匀市
        }

        selfDialog = new GISDialog(getContext());
    }

    private void getDeviceAllList(String data) {
        retrofitUtil.createReq(IService.class).getDeviceAllList(data).enqueue(new Callback<DeviceBean>() {
            @Override
            public void onResponse(Call<DeviceBean> call, Response<DeviceBean> response) {
                hideProgress();
                if (!response.isSuccessful()) {
                    toast(response.message());
                    return;
                }
                DeviceBean deviceBean = response.body();
                if (null == deviceBean) {
                    toast(response.message());
                    return;
                }
                if (0 == deviceBean.getStatus()) {
                    toast(deviceBean.getMessage());
                    return;
                }
                clearOverlay();
                deviceBeanList.clear();
                deviceBeanList.addAll(deviceBean.getData());
                setMarksPosition();
            }

            @Override
            public void onFailure(Call<DeviceBean> call, Throwable t) {
                hideProgress();
                t.getLocalizedMessage();
            }
        });
    }

    private void setMarksPosition() {
        for (int i = 0; i < deviceBeanList.size(); i++) {
            DeviceBean.DataBean assetBean = deviceBeanList.get(i);
            LatLng latLng = new LatLng(assetBean.getY(), assetBean.getX());

            MarkerOptions ooA;
            int status = assetBean.getDevStatus();
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

            Marker marker = (Marker) (mBaiduMap.addOverlay(ooA));
            marker.setTitle(assetBean.getDevNo());

            Bundle bundle = new Bundle();
            bundle.putDouble("y", latLng.longitude);
            bundle.putDouble("x", latLng.latitude);
            bundle.putString("name", assetBean.getDevName());
            bundle.putInt("status", status);
            bundle.putString("statusDesc", statusDesc);
            //bundle.putString("A", getABCStatusDesc(assetBean.getAbc()));

            marker.setExtraInfo(bundle);

            mMarkerList.add(marker);
        }
    }

    private void updateDeviceLocation(String data) {
        showProgress();
        retrofitUtil.createReq(IService.class).updateDeviceLocation(data).enqueue(new Callback<CommonBean>() {
            @Override
            public void onResponse(Call<CommonBean> call, Response<CommonBean> response) {
                hideProgress();
                if (!response.isSuccessful()) {
                    toast(response.message());
                    return;
                }
                CommonBean deviceBean = response.body();
                if (null == deviceBean) {
                    toast(response.message());
                    return;
                }
                if (0 == deviceBean.getStatus()) {
                    toast(deviceBean.getMessage());
                    return;
                }
                tempCommonMark = null;
                getDeviceAllList(jsonObject.toJSONString());
            }

            @Override
            public void onFailure(Call<CommonBean> call, Throwable t) {
                hideProgress();
                t.getLocalizedMessage();
            }
        });
    }

    private void showDeleteDialog(final Marker marker) {
        selfDialog.setYesOnclickListener(new GISDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                selfDialog.dismiss();
                if (null != mBaiduMap.getMapStatus()) {
                    LatLng llF = mBaiduMap.getMapStatus().target;
                    Point mScreenCenterPoint = mBaiduMap.getProjection().toScreenLocation(llF);
                    marker.setPerspective(true);
                    marker.setFixedScreenPosition(mScreenCenterPoint);
                    tempCommonMark = marker;
                }
            }
        });

        selfDialog.setNoOnclickListener(new GISDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                selfDialog.dismiss();
                showMarkInfoWin(marker);
            }
        });

        selfDialog.setOnNavOnclickListener(new GISDialog.onNavOnclickListener() {
            @Override
            public void onNavClick() {
                goNavigation(marker.getTitle(), marker.getPosition().latitude, marker.getPosition().longitude);
            }
        });
        selfDialog.show();
    }

    private void showMarkInfoWin(Marker marker) {
        TextView button = (TextView) View.inflate(getContext(), R.layout.map_info_win, null);
        Bundle bundle = marker.getExtraInfo();
        String name = bundle.getString("name");
        String statusDesc = bundle.getString("statusDesc");

        String desc = "名称:" + name + "\n状态:" + statusDesc;
        button.setText(desc);

        InfoWindow.OnInfoWindowClickListener listener = new InfoWindow.OnInfoWindowClickListener() {
            public void onInfoWindowClick() {
                mBaiduMap.hideInfoWindow();
            }
        };
        LatLng ll = marker.getPosition();
        mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button), ll, -120, listener);
        mBaiduMap.showInfoWindow(mInfoWindow);
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

    /**
     * 创建平移坐标动画
     */
    private Animation getTransformationPoint(final MapStatus status, final Marker marker, Point mScreenCenterPoint) {

        if (null != mScreenCenterPoint) {
            final Point pointTo = new Point(mScreenCenterPoint.x, mScreenCenterPoint.y - 100);
            Transformation mTransforma = new Transformation(mScreenCenterPoint, pointTo, mScreenCenterPoint);
            mTransforma.setDuration(500);
            mTransforma.setRepeatMode(Animation.RepeatMode.RESTART);//动画重复模式
            mTransforma.setRepeatCount(1);//动画重复次数
            mTransforma.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart() {

                }

                @Override
                public void onAnimationEnd() {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("devNo", marker.getTitle());
                    jsonObject.put("x", status.target.longitude);
                    jsonObject.put("y", status.target.latitude);
                    updateDeviceLocation(jsonObject.toJSONString());
                }

                @Override
                public void onAnimationCancel() {
                }

                @Override
                public void onAnimationRepeat() {

                }
            });
            return mTransforma;
        }

        return null;
    }

    @Override
    public void onStart() {
        super.onStart();
        subscribeEvent();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxSubscriptions.remove(mRxSub);
        if (mTexturemap != null) {
            mTexturemap.onDestroy();
        }
        // 回收 bitmap 资源
        normalDb.recycle();
        alarmDd.recycle();
        offDd.recycle();
    }

    private void subscribeEvent() {
        RxSubscriptions.remove(mRxSub);
        mRxSub = RxBus.getDefault().toObservable(JingWeidu.class)
                .map(new Func1<JingWeidu, JingWeidu>() {
                    @Override
                    public JingWeidu call(JingWeidu event) {
                        // 变换等操作
                        return event;
                    }
                })
                .subscribe(new RxBusSubscriber<JingWeidu>() {
                    @Override
                    protected void onEvent(JingWeidu myEvent) {
                        if (null == myEvent) {
                            return;
                        }
                        LogUtils.d(JingWeidu.class.getName(), "onNext--->" + myEvent.getCityName());
                        setLocationMap(myEvent.getY(), myEvent.getX());
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        /**
                         * 这里注意: 一旦订阅过程中发生异常,走到onError,则代表此次订阅事件完成,后续将收不到onNext()事件,
                         * 即 接受不到后续的任何事件,实际环境中,我们需要在onError里 重新订阅事件!
                         */
                        subscribeEvent();
                    }
                });
        RxSubscriptions.add(mRxSub);
    }

    public void goNavigation(String name, double lat, double lon) {
        if (isInstallByread("com.autonavi.minimap")) {
            Intent intent1 = new Intent();
            intent1.setAction(Intent.ACTION_VIEW);
            intent1.addCategory(Intent.CATEGORY_DEFAULT);
            //将功能Scheme以URI的方式传入data
            Uri uri = Uri.parse("androidamap://navi?sourceApplication=appname&poiname=fangheng&lat=" + lat + "&lon=" + lon + "&dev=1&style=2");
            intent1.setData(uri);
            //启动该页面即可
            startActivity(intent1);
            return;
        }

        if (isInstallByread("com.baidu.BaiduMap")) {
            Intent intent = null;
            try {
                String url = "intent://map/direction?origin=我的位置&destination=name:" + name + "|latlng:" + lat + "," + lon + "&mode=driving&region=都匀&src=thirdapp.navi.yourCompanyName.yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end";
                intent = Intent.getIntent(url);
                startActivity(intent); //启动调用
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            return;
        }

        toast("请先安装百度地图或者高德地图");
    }

    /**
     * 判断是否安装目标应用
     *
     * @param packageName 目标应用安装后的包名
     * @return 是否已安装目标应用
     */
    private boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }
}
