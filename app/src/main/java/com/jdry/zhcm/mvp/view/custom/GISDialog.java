package com.jdry.zhcm.mvp.view.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.jdry.zhcm.R;


/**
 * Created by JDRY-SJM on 2018/2/27.
 */

public class GISDialog extends Dialog {

    private TextView yes;//确定按钮
    private TextView no;//取消按钮
    private TextView nav;//是否导航
    private onNoOnclickListener noOnclickListener;//取消按钮被点击了的监听器
    private onYesOnclickListener yesOnclickListener;//确定按钮被点击了的监听器
    private onNavOnclickListener onNavOnclickListener;

    public GISDialog(@NonNull Context context) {
        super(context, R.style.Dialog_Fullscreen);
    }

    public TextView getYes() {
        return yes;
    }

    public void setYes(TextView yes) {
        this.yes = yes;
    }

    public TextView getNo() {
        return no;
    }

    public void setNo(TextView no) {
        this.no = no;
    }

    public TextView getNav() {
        return nav;
    }

    public void setNav(TextView nav) {
        this.nav = nav;
    }

    /**
     * 设置取消按钮的显示内容和监听
     *
     * @param onNoOnclickListener
     */
    public void setNoOnclickListener(onNoOnclickListener onNoOnclickListener) {
        this.noOnclickListener = onNoOnclickListener;
    }

    /**
     * 设置确定按钮的显示内容和监听
     *
     * @param onYesOnclickListener
     */
    public void setYesOnclickListener(onYesOnclickListener onYesOnclickListener) {
        this.yesOnclickListener = onYesOnclickListener;
    }

    /**
     * 设置确定按钮的显示内容和监听
     *
     * @param onNavOnclickListener
     */
    public void setOnNavOnclickListener(onNavOnclickListener onNavOnclickListener) {
        this.onNavOnclickListener = onNavOnclickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gis_dialog);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        //初始化界面控件
        initView();
        //初始化界面控件的事件
        initEvent();

    }

    /**
     * 初始化界面的确定和取消监听器
     */
    private void initEvent() {
        //设置确定按钮被点击后，向外界提供监听
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yesOnclickListener != null) {
                    yesOnclickListener.onYesClick();
                }
            }
        });
        //设置取消按钮被点击后，向外界提供监听
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noOnclickListener != null) {
                    noOnclickListener.onNoClick();
                }
            }
        });

        nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onNavOnclickListener != null) {
                    onNavOnclickListener.onNavClick();
                }
            }
        });
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        yes = (TextView) findViewById(R.id.yes);
        no = (TextView) findViewById(R.id.no);
        nav = (TextView) findViewById(R.id.nav);
    }

    /**
     * 设置确定按钮和取消被点击的接口
     */
    public interface onYesOnclickListener {
        void onYesClick();
    }

    public interface onNoOnclickListener {
        void onNoClick();
    }

    public interface onNavOnclickListener {
        void onNavClick();
    }

}
