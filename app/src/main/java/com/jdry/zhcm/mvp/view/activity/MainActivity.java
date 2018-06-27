package com.jdry.zhcm.mvp.view.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jdry.zhcm.R;
import com.jdry.zhcm.jpush.ExampleUtil;
import com.jdry.zhcm.jpush.Logger;
import com.jdry.zhcm.mvp.view.fragment.AlarmFragment;
import com.jdry.zhcm.mvp.view.fragment.HomeFragment;
import com.jdry.zhcm.mvp.view.fragment.MapFragment;
import com.jdry.zhcm.mvp.view.fragment.UserFragment;
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
    @BindView(R.id.iv_home)
    ImageView ivHome;
    @BindView(R.id.tv_home)
    TextView tvHome;
    @BindView(R.id.tv_jz)
    TextView tvJz;
    @BindView(R.id.tv_my)
    TextView tvMy;
    @BindView(R.id.ll_home)
    LinearLayout llHome;
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

    @OnClick({R.id.ll_home, R.id.ll_live, R.id.ll_lesson, R.id.ll_user})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_home:
                changeNavStyle(view);
                changeIndexTextViewColor(0);
                break;
            case R.id.ll_live:
                changeNavStyle(view);
                changeIndexTextViewColor(1);
                break;
            case R.id.ll_lesson:
                changeNavStyle(view);
                changeIndexTextViewColor(2);
                break;
            case R.id.ll_user:
                changeNavStyle(view);
                changeIndexTextViewColor(3);
                break;
        }
    }

    private FragmentManager fragmentManager;
    private AlarmFragment lectureFragment = null;
    private HomeFragment homeFragment = null;
    private UserFragment userFragment = null;
    private MapFragment liveVideoFragment = null;
    private ImageView[] imageViews = new ImageView[4];
    private TextView[] textViews = new TextView[4];
    private int[] imageViewNormalRes = {R.drawable.index_1_normal, R.drawable.index_2_normal, R.drawable.index_3_normal, R.drawable.index_4_normal};
    private int[] imageViewChangeRes = {R.drawable.index_1_press, R.drawable.index_2_press, R.drawable.index_3_press, R.drawable.index_4_press};

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
        registerMessageReceiver();  // used for receive msg
    }

    private void initFragments() {
        initImageViews(); //初始化imageview数组
        initTextViews();
        changeFragment(index);//显示主页
    }

    private void initImageViews() {
        imageViews[0] = ivHome;
        imageViews[1] = ivLive;
        imageViews[2] = ivTabLecture;
        imageViews[3] = ivTabUser;
    }

    private void initTextViews() {
        textViews[0] = tvHome;
        textViews[1] = tvLive;
        textViews[2] = tvJz;
        textViews[3] = tvMy;
    }

    private void changeFragment(int indexTmp) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (indexTmp) {
            case 0:
                if (null == homeFragment) {
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.ll_fragment_container, homeFragment, "homeFragment");
                } else {
                    transaction.show(homeFragment);
                }

                break;
            case 1:
                if (null == liveVideoFragment) {
                    liveVideoFragment = new MapFragment();
                    transaction.add(R.id.ll_fragment_container, liveVideoFragment, "liveVideoFragment");
                } else {
                    transaction.show(liveVideoFragment);
                }
                break;
            case 2:
                if (null == lectureFragment) {
                    lectureFragment = new AlarmFragment();
                    transaction.add(R.id.ll_fragment_container, lectureFragment, "lectureFragment");
                } else {
                    transaction.show(lectureFragment);
                }
                break;
            case 3:
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

    private void hideFragments(FragmentTransaction transaction) {
        if (null != homeFragment) {
            transaction.hide(homeFragment);
        }
        if (null != liveVideoFragment) {
            transaction.hide(liveVideoFragment);
        }
        if (null != lectureFragment) {
            transaction.hide(lectureFragment);
        }
        if (null != userFragment) {
            transaction.hide(userFragment);
        }
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


    public static boolean isForeground = false;

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
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String messge = intent.getStringExtra(KEY_MESSAGE);
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    StringBuilder showMsg = new StringBuilder();
                    showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                    if (!ExampleUtil.isEmpty(extras)) {
                        showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                    }
                    setCostomMsg(showMsg.toString());
                }
            } catch (Exception e) {
            }
        }
    }

    private void setCostomMsg(String msg) {
        Logger.d("main_activity:", msg);
    }
}
