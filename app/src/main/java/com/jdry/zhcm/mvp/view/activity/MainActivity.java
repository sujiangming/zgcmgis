package com.jdry.zhcm.mvp.view.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.jdry.zhcm.R;
import com.jdry.zhcm.beans.JingWeidu;
import com.jdry.zhcm.global.JDRYDYApplication;
import com.jdry.zhcm.mvp.view.fragment.MapFragment;
import com.jdry.zhcm.mvp.view.fragment.ScanFragment;
import com.jdry.zhcm.mvp.view.fragment.UserFragment;
import com.jdry.zhcm.rxbus.RxBus;
import com.jdry.zhcm.service.LocationService;
import com.jdry.zhcm.utils.AppManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by JDRY-SJM on 2017/9/29.
 */

public class MainActivity extends SjmBaseActivity {
    @BindView(R.id.ll_fragment_container)
    LinearLayout llFragmentContainer;
    @BindView(R.id.tv_jz)
    TextView tvJz;
    @BindView(R.id.tv_my)
    TextView tvMy;
    @BindView(R.id.iv_live)
    ImageView ivLive;
    @BindView(R.id.tv_live)
    TextView tvLive;
    @BindView(R.id.ll_live)
    LinearLayout llLive;
    @BindView(R.id.ll_lesson)
    RelativeLayout llLesson;
    @BindView(R.id.ll_user)
    LinearLayout llUser;
    @BindView(R.id.iv_tab_lecture)
    ImageView ivTabLecture;
    @BindView(R.id.iv_tab_user)
    ImageView ivTabUser;
    @BindView(R.id.iv_msg)
    ImageView ivMsg;

    public static boolean isForeground = false;

    private FragmentManager fragmentManager;
    private ScanFragment lectureFragment = null;
    private UserFragment userFragment = null;
    private MapFragment liveVideoFragment = null;
    private ImageView[] imageViews = new ImageView[3];
    private TextView[] textViews = new TextView[3];
    private int[] imageViewNormalRes = {R.drawable.map_mark_gray, R.drawable.mark_gray, R.drawable.user_gray};
    private int[] imageViewChangeRes = {R.drawable.map_mark_blue, R.drawable.mark_blue, R.drawable.user_blue};

    private int index = 0;

    @Override
    public int getResouceId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreateByMe(Bundle savedInstanceState) {
        fragmentManager = getSupportFragmentManager();
        initFragments();
        getPersimmions();
    }

    private void initFragments() {
        initImageViews(); //初始化imageview数组
        initTextViews();
        changeFragment(index);//显示主页
    }

    private LocationService locationService;
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
                //JDRYUtils.getLocationInfo(location);
                //判断是否已经获得到当前的坐标
                if (Double.isNaN(location.getLatitude()) && Double.isNaN(location.getLongitude())) {
                    return;
                }
                JingWeidu jingWeidu = JDRYDYApplication.getDaoSession().getJingWeiduDao().queryBuilder().unique();
                if (null != jingWeidu) {
                    if (!jingWeidu.getCityName().equals(location.getCity())) {
                        jingWeidu.setCityName(location.getCity());
                        JDRYDYApplication.getDaoSession().getJingWeiduDao().insertOrReplace(jingWeidu);
                        post(jingWeidu);
                    }
                    return;
                }
                jingWeidu = new JingWeidu();
                jingWeidu.setUid(1);
                jingWeidu.setCityName(location.getCity());
                jingWeidu.setX(location.getLongitude());
                jingWeidu.setY(location.getLatitude());
                JDRYDYApplication.getDaoSession().getJingWeiduDao().insertOrReplace(jingWeidu);
                post(jingWeidu);

            }
        }
    };

    @OnClick({R.id.ll_live, R.id.ll_lesson, R.id.ll_user})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_live:
                changeNavStyle(view);
                changeIndexTextViewColor(0);
                break;
            case R.id.ll_lesson:
                changeNavStyle(view);
                changeIndexTextViewColor(1);
                break;
            case R.id.ll_user:
                changeNavStyle(view);
                changeIndexTextViewColor(2);
                break;
        }
    }

    private void initImageViews() {
        imageViews[0] = ivLive;
        imageViews[1] = ivTabLecture;
        imageViews[2] = ivTabUser;
    }

    private void changeNavStyle(View view) {
        String tag = (String) view.getTag();
        index = Integer.parseInt(tag);
        changeFragment(index);
        changeImageViewRes(index);
    }

    private void changeImageViewRes(int index) {
        imageViews[index].setImageResource(imageViewChangeRes[index]);
        int len = imageViews.length;
        for (int i = 0; i < len; ++i) {
            if (i != index) {
                imageViews[i].setImageResource(imageViewNormalRes[i]);
            }
        }
    }

    private void changeIndexTextViewColor(int index) {
        textViews[index].setTextColor(0xFF49649A);
        for (int i = 0; i < textViews.length; ++i) {
            if (i != index) {
                textViews[i].setTextColor(0xFF686868);
            }
        }
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            //两秒之内按返回键就会退出
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                toast("再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                AppManager.getAppManager().finishAllActivity();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private String permissionInfo;
    private final int SDK_PERMISSION_REQUEST = 127;

    private void initTextViews() {
        textViews[0] = tvLive;
        textViews[1] = tvJz;
        textViews[2] = tvMy;
    }

    private void changeFragment(int indexTmp) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (indexTmp) {
            case 0:
                if (null == liveVideoFragment) {
                    liveVideoFragment = new MapFragment();
                    transaction.add(R.id.ll_fragment_container, liveVideoFragment, "liveVideoFragment");
                } else {
                    transaction.show(liveVideoFragment);
                }
                break;
            case 1:
                if (null == lectureFragment) {
                    lectureFragment = new ScanFragment();
                    transaction.add(R.id.ll_fragment_container, lectureFragment, "lectureFragment");
                } else {
                    transaction.show(lectureFragment);
                }
                break;
            case 2:
                if (null == userFragment) {
                    userFragment = new UserFragment();
                    transaction.add(R.id.ll_fragment_container, userFragment, "userFragment");
                } else {
                    transaction.show(userFragment);
                }
                break;
        }
        transaction.commit();
    }

    @TargetApi(26)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)) {
                return true;
            } else {
                permissionsList.add(permission);
                return false;
            }

        } else {
            return true;
        }
    }

    @TargetApi(26)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (null != lectureFragment) {
            transaction.hide(lectureFragment);
        }
        if (null != liveVideoFragment) {
            transaction.hide(liveVideoFragment);
        }
        if (null != userFragment) {
            transaction.hide(userFragment);
        }
    }

    @TargetApi(26)
    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            /*
             * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
             */
            // 读写权限
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }
            // 读取电话状态权限
            if (addPermission(permissions, Manifest.permission.READ_PHONE_STATE)) {
                permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
            }

            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getLocation();
    }

    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
    }


    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onStop();
    }

    private void getLocation() {
        locationService = ((JDRYDYApplication) getApplication()).locationService;
        locationService.registerListener(mListener); //注册监听
        locationService.setLocationOption(locationService.getOption());
        locationService.start();// 定位SDK
    }

    private void post(JingWeidu jingWeidu) {
        RxBus.getDefault().post(jingWeidu);
    }


}
