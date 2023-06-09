package com.example.branchmetric;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

import io.branch.referral.Branch;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Branch logging for debugging
        Branch.enableLogging();
        // Initialize the Branch SDK
        Branch.getAutoInstance(this);
    }
}
