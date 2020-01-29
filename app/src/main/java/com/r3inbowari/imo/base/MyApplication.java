package com.r3inbowari.imo.base;

import android.app.Application;
import android.content.Context;

import com.r3inbowari.imo.imomoeAPI.ImomoeSearch;
import com.tencent.bugly.Bugly;

import java.util.ArrayList;

/**
 * Created by tanbiheng on 2018/1/24.
 */

public class MyApplication extends Application {

    private static MyApplication INSTANCE = null;

    public static ArrayList<ImomoeSearch> arrayList = new ArrayList<>();

    public static Application getInstance(){
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        arrayList.add(new ImomoeSearch());

        INSTANCE = this;
        Bugly.init(this, "244ca6b730", false);
    }
}
