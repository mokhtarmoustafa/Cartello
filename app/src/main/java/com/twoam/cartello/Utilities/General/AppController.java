package com.twoam.cartello.Utilities.General;

import android.app.Application;
import android.content.Context;

import com.twoam.cartello.R;
import com.twoam.cartello.Utilities.DB.PreferenceController;

import java.util.Locale;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by mokhtar on 04/15/2019.
 */

public class AppController extends Application {
    private static AppController mContext;
    private static int pendingNotificationsCount = 0;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        if (!PreferenceController.Companion.getInstance(this).get(AppConstants.INSTANCE.getLANGUAGE()).equals(AppConstants.INSTANCE.getENGLISH())) {
            LanguageUtil.changeLanguageType(mContext, new Locale(AppConstants.INSTANCE.getARABIC()));
        } else {
            LanguageUtil.changeLanguageType(mContext, Locale.ENGLISH);
        }

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Montserrat-Regular.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }


    public static synchronized AppController getInstance() {
        return mContext;
    }

    public static Context getContext() {
        return mContext;
    }


}
