package com.jdry.zhcm.global;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Vibrator;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.jdry.zhcm.beans.DaoMaster;
import com.jdry.zhcm.beans.DaoSession;
import com.jdry.zhcm.beans.LoginBean;
import com.jdry.zhcm.service.LocationService;
import com.jdry.zhcm.utils.AppManager;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import cn.jpush.android.api.JPushInterface;


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
        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);    // 初始化 JPush
        initAMap();
        initLeakCanary();
    }

    public static JDRYDYApplication getInstance() {
        return instance;
    }

    private void initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        refWatcher = LeakCanary.install(this);
        setupGreenDao();
        initAppManager();
        initLoginBean();

    }

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

    private void initAMap() {
        locationService = new LocationService(getApplicationContext());
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());
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
}
