package com.jdry.zhcm.global;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Vibrator;
import android.util.Log;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.jdry.zhcm.R;
import com.jdry.zhcm.beans.DaoMaster;
import com.jdry.zhcm.beans.DaoSession;
import com.jdry.zhcm.beans.LoginBean;
import com.jdry.zhcm.listener.GlidePauseOnScrollListener;
import com.jdry.zhcm.service.LocationService;
import com.jdry.zhcm.utils.AppManager;
import com.jdry.zhcm.utils.GlideImageLoader;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ThemeConfig;


/**
 * Created by wei on 2016/5/27.
 */
public class JDRYDYApplication extends Application {

    private static JDRYDYApplication instance;
    private static DaoSession daoSession;
    private RefWatcher refWatcher;
    public LocationService locationService;
    public Vibrator mVibrator;
    private final static String TAG = JDRYDYApplication.class.getName();

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initAMap();
        initLeakCanary();
    }

    public static JDRYDYApplication getInstance() {
        return instance;
    }

    private static FunctionConfig functionConfig;

    public static RefWatcher getRefWatcher(Context context) {
        JDRYDYApplication application = (JDRYDYApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    /**
     * 初始化管理Activity工具类
     */
    private void initAppManager() {
        AppManager.getAppManager();
    }

    private void initLoginBean() {
        LoginBean.getInstance().load();
    }

    public static FunctionConfig getFunctionConfig() {
        return functionConfig;
    }

    /**
     * 配置数据库GreenDao
     */
    private void setupGreenDao() {
        //创建数据库yxx.db （创建SQLite数据库的SQLiteOpenHelper的具体实现）
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "kindergarten.db", null);
        //获取可写数据库
        SQLiteDatabase db = helper.getWritableDatabase();
        //获取数据库对象（GreenDao的顶级对象，作为数据库对象、用于创建表和删除表）
        DaoMaster daoMaster = new DaoMaster(db);
        //获取dao对象管理者（管理所有的Dao对象，Dao对象中存在着增删改查等API）
        daoSession = daoMaster.newSession();
        Log.e(JDRYDYApplication.class.getName(), "数据库当前版本号：" + daoMaster.getSchemaVersion());
    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }

    private void initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        refWatcher = LeakCanary.install(this);
        setupGreenDao();
        initAppManager();
        initLoginBean();
        initImage();
        CrashHandler.instance().init();

    }

    private void initAMap() {
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);

        locationService = new LocationService(this);
        mVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
    }

    private void initImage() {
        //设置主题
        ThemeConfig theme = new ThemeConfig.Builder()
                .setTitleBarBgColor(Color.rgb(47, 162, 203))
                .setTitleBarTextColor(Color.WHITE)
                .setTitleBarIconColor(Color.WHITE)
                .setFabNornalColor(R.color.color2fa2cb)
                .setFabPressedColor(R.color.colorAccent)
                .setCheckNornalColor(Color.WHITE)
                .setCheckSelectedColor(Color.BLACK)
                .setIconBack(R.drawable.btn_back3x)
                .setIconRotate(R.drawable.ic_action_repeat)
                .setIconCrop(R.drawable.ic_action_crop)
                .setIconCamera(R.drawable.ic_action_camera)
                .build();
        //配置功能
        functionConfig = new FunctionConfig.Builder()
                .setEnableCamera(false)
                .setEnableEdit(false)
                .setEnableCrop(false)
                .setEnableRotate(true)
                .setCropSquare(false)
                .setEnablePreview(true)
                .build();
        CoreConfig coreConfig = new CoreConfig.Builder(this, new GlideImageLoader(), theme)
                .setFunctionConfig(functionConfig)
                .setPauseOnScrollListener(new GlidePauseOnScrollListener(false, true))
                .build();
        GalleryFinal.init(coreConfig);
    }
}
