package com.jdry.zhcm.mvp.view.activity;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.baoyz.actionsheet.ActionSheet;
import com.jdry.zhcm.R;
import com.jdry.zhcm.beans.CommonBean;
import com.jdry.zhcm.global.JDRYDYApplication;
import com.jdry.zhcm.http.IService;
import com.jdry.zhcm.http.RetrofitUtil;
import com.jdry.zhcm.mvp.presenter.UploadFilePresenter;
import com.jdry.zhcm.mvp.view.custom.RichText;
import com.jdry.zhcm.utils.GlideImageLoader;
import com.jdry.zhcm.utils.JDRYUtils;
import com.jdry.zhcm.utils.JdryTime;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanQRActivity extends SjmBaseActivity implements EasyPermissions.PermissionCallbacks {
    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;
    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;
    @BindView(R.id.rtv_top_bar_back)
    RichText rtvTopBarBack;
    @BindView(R.id.tv_top_bar_title)
    TextView tvTopBarTitle;
    @BindView(R.id.tv_device_id)
    TextView tvDeviceId;
    @BindView(R.id.tv_device_address)
    TextView tvDeviceAddress;
    @BindView(R.id.tv_xunjian_name)
    TextView tvXunjianName;
    @BindView(R.id.tv_x)
    TextView tvX;
    @BindView(R.id.tv_y)
    TextView tvY;
    @BindView(R.id.tv_type_normal)
    TextView tvTypeNormal;
    @BindView(R.id.tv_type_repair)
    TextView tvTypeRepair;
    @BindView(R.id.tv_type_replace)
    TextView tvTypeReplace;
    @BindView(R.id.tv_device_status)
    TextView tvDeviceStatus;
    @BindView(R.id.tv_status_normal)
    TextView tvStatusNormal;
    @BindView(R.id.tv_status_exception)
    TextView tvStatusException;
    @BindView(R.id.ll_imgs_wrap)
    LinearLayout llImgsWrap;
    @BindView(R.id.iv_add_pic)
    ImageView ivAddPic;
    @BindView(R.id.jz_btn)
    Button jzBtn;
    private String deviceType = "";
    private int deviceStatus = 0;//设备状态（0正常，1故障，2下线）
    private JSONObject jsonObject = new JSONObject();
    private int maxSizeUPloadImage = 3;
    private GlideImageLoader glideImageLoader = new GlideImageLoader();
    private FunctionConfig functionConfig = JDRYDYApplication.getFunctionConfig();
    private List<PhotoInfo> mResultList = new ArrayList<>();
    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new MyOnHanlderResultCallback();

    @OnClick({R.id.tv_type_normal, R.id.tv_type_repair, R.id.tv_type_replace, R.id.tv_status_normal, R.id.tv_status_exception, R.id.iv_add_pic, R.id.jz_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_type_normal:
                deviceType = "0";
                tvTypeNormal.setTextColor(0xFF2fa2cb);
                tvTypeRepair.setTextColor(0xFF353535);
                tvTypeReplace.setTextColor(0xFF353535);
                tvTypeNormal.setBackgroundResource(R.drawable.blue_with_border_bg);
                tvTypeRepair.setBackgroundResource(R.drawable.gray_with_border_bg);
                tvTypeReplace.setBackgroundResource(R.drawable.gray_with_border_bg);
                break;
            case R.id.tv_type_repair:
                deviceType = "1";
                tvTypeNormal.setTextColor(0xFF353535);
                tvTypeRepair.setTextColor(0xFF2fa2cb);
                tvTypeReplace.setTextColor(0xFF353535);
                tvTypeNormal.setBackgroundResource(R.drawable.gray_with_border_bg);
                tvTypeRepair.setBackgroundResource(R.drawable.blue_with_border_bg);
                tvTypeReplace.setBackgroundResource(R.drawable.gray_with_border_bg);
                break;
            case R.id.tv_type_replace:
                deviceType = "2";
                tvTypeNormal.setTextColor(0xFF353535);
                tvTypeRepair.setTextColor(0xFF353535);
                tvTypeReplace.setTextColor(0xFF2fa2cb);
                tvTypeNormal.setBackgroundResource(R.drawable.gray_with_border_bg);
                tvTypeRepair.setBackgroundResource(R.drawable.gray_with_border_bg);
                tvTypeReplace.setBackgroundResource(R.drawable.blue_with_border_bg);
                break;
            case R.id.tv_status_normal:
                deviceStatus = 0;
                tvStatusNormal.setTextColor(0xFF2fa2cb);
                tvStatusException.setTextColor(0xFF353535);
                tvStatusNormal.setBackgroundResource(R.drawable.blue_with_border_bg);
                tvStatusException.setBackgroundResource(R.drawable.gray_with_border_bg);
                break;
            case R.id.tv_status_exception:
                deviceStatus = 1;
                tvStatusNormal.setTextColor(0xFF353535);
                tvStatusException.setTextColor(0xFF2fa2cb);
                tvStatusNormal.setBackgroundResource(R.drawable.gray_with_border_bg);
                tvStatusException.setBackgroundResource(R.drawable.blue_with_border_bg);
                break;
            case R.id.iv_add_pic:
                showPhoto();
                break;
            case R.id.jz_btn:
                publish();
                break;
        }
    }

    @Override
    public int getResouceId() {
        return R.layout.activity_scan_detail;
    }

    @Override
    protected void onCreateByMe(Bundle savedInstanceState) {
        setTopBar(this, tvTopBarTitle, "巡检", rtvTopBarBack);
        initData();
    }

    private void initData() {
        jsonObject.put("devNo", "DKHKH8989898");
        jsonObject.put("devName", JDRYUtils.encode("苏江明-测试保存设备"));
        jsonObject.put("x", "109.7987979");
        jsonObject.put("y", "46.898989");
        jsonObject.put("inspectTime", JdryTime.transferLongToString(new Date().getTime(), "yyyy-MM-dd:HH:mm:ss"));
        jsonObject.put("inspectUser", JDRYUtils.encode("卜小超"));
        jsonObject.put("inspectUserName", JDRYUtils.encode("卜小超"));
    }

    private void uploadFile(List<PhotoInfo> resultList) {
        List<File> fileArrayList = new ArrayList<>();
        for (int i = 0; i < resultList.size(); i++) {
            File file = new File(resultList.get(i).getPhotoPath());
            fileArrayList.add(file);
        }
        showProgress();
        new UploadFilePresenter(this).upload(fileArrayList, "xj");
    }

    @Override
    public void uploadSuccess(String filePath) {
        saveDeviceInspection(filePath);
    }

    @Override
    public void uploadFailure(String msg) {
        toast(msg);
        hideProgress();
    }

    private void publish() {
        if (0 == mResultList.size()) { //没有图片
            showProgress();
            saveDeviceInspection("");
        } else {
            uploadFile(mResultList);
        }
    }

    private void saveDeviceInspection(String filePath) {
        jsonObject.put("devImgs", filePath);
        jsonObject.put("inspectType", deviceType);
        jsonObject.put("devStatus", deviceStatus);
        RetrofitUtil retrofitUtil = new RetrofitUtil();
        retrofitUtil.createReq(IService.class).saveDeviceInspection(jsonObject.toJSONString()).enqueue(new Callback<CommonBean>() {
            @Override
            public void onResponse(Call<CommonBean> call, Response<CommonBean> response) {
                hideProgress();
                if (!response.isSuccessful()) {
                    toast(response.message());
                    return;
                }
                CommonBean commonBean = response.body();
                if (null == commonBean) {
                    toast(response.message());
                    return;
                }
                if (0 == commonBean.getStatus()) {
                    toast(commonBean.getMessage());
                    return;
                }
                toast(commonBean.getMessage());
                jzBtn.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        closeActivity();
                    }
                }, 1500);
            }

            @Override
            public void onFailure(Call<CommonBean> call, Throwable t) {
                t.getLocalizedMessage();
                hideProgress();
            }
        });
    }

    private void showPhoto() {
        maxSizeUPloadImage = 3 - mResultList.size();
        new ActionSheet.Builder(this, getSupportFragmentManager())
                .setCancelButtonTitle("取消")
                .setOtherButtonTitles("打开相册", "拍照")
                .setCancelableOnTouchOutside(true)
                .setListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

                    }

                    @Override
                    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                        switch (index) {
                            case 0:
                                GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, maxSizeUPloadImage, mOnHanlderResultCallback);
                                break;
                            case 1:
                                requestCodeQRCodePermissions();
                                break;
                            default:
                                break;
                        }
                    }
                }).show();
    }

    private void addPics(List<PhotoInfo> resultList) {
        for (int i = 0; i < resultList.size(); ++i) {
            addPicItem(i, resultList.get(i));
        }
    }

    private void addPicItem(final int pos, final PhotoInfo photoInfo) {
        final View rootView = View.inflate(this, R.layout.image_display, null);
        ImageView imageView = rootView.findViewById(R.id.iv_main);
        ImageView imageViewDelete = rootView.findViewById(R.id.iv_delete);
        glideImageLoader.displayImage(this, photoInfo.getPhotoPath(), imageView);
        llImgsWrap.addView(rootView, pos);
        imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rootView.setVisibility(View.GONE);
                llImgsWrap.removeView(rootView);
                mResultList.remove(photoInfo);
                int childCount = llImgsWrap.getChildCount();
                if (childCount < 4) {
                    if (ivAddPic.getVisibility() == View.GONE) {
                        ivAddPic.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private void openCamera() {
        GalleryFinal.openCamera(REQUEST_CODE_CAMERA, functionConfig, mOnHanlderResultCallback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
    }

    @AfterPermissionGranted(REQUEST_CODE_QRCODE_PERMISSIONS)
    private void requestCodeQRCodePermissions() {
        String[] perms = {
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "需要打开相机的权限", REQUEST_CODE_QRCODE_PERMISSIONS, perms);
        } else {
            openCamera();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mOnHanlderResultCallback = null;
    }

    public class MyOnHanlderResultCallback implements GalleryFinal.OnHanlderResultCallback {

        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null) {
                int childCount = llImgsWrap.getChildCount();
                if (childCount <= 4) {
                    if (childCount == 4) {
                        toast("最多只能上传3张图片");
                        return;
                    }
                    mResultList.addAll(resultList);
                    int sizew = mResultList.size();
                    if (sizew == 3) {
                        ivAddPic.setVisibility(View.GONE);
                    }
                    addPics(resultList);
                } else {
                    toast("最多只能上传3张图片");
                }
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {

        }
    }

}
