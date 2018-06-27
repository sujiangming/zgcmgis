package com.jdry.zhcm.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by Administrator on 2016/12/13.
 */

public class JdryCall {
    public static void dialPhoneNumber(Context context, String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }
}
