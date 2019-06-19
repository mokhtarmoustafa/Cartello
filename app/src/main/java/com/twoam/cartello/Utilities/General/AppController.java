package com.twoam.cartello.Utilities.General;

import android.app.Application;
import android.content.Context;

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
    }



    public static int getPendingNotificationsCount() {
        return pendingNotificationsCount;
    }

    public static void setPendingNotificationsCount(int pendingNotificationsCount) {
        AppController.pendingNotificationsCount = pendingNotificationsCount;
    }

    public static synchronized AppController getInstance() {
        return mContext;
    }

    public static Context getContext() {
        return mContext;
    }


}
