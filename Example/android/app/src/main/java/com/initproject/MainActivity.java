package com.initproject;

import android.os.Bundle;
import android.util.Log;

import com.facebook.react.ReactActivity;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import com.liuchungui.react_native_umeng_push.UmengPushPackage;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class MainActivity extends ReactActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("UmengPushModule", "onCreate");
        Bundle bun = getIntent().getExtras();
        if(bun != null) {
            Set<String> keySet = bun.keySet();
            Log.i("UmengPushModule", keySet.toString());
            for(String key: keySet) {
                String value = bun.getString(key);
            }
        }
    }

    /**
     * Returns the name of the main component registered from JavaScript.
     * This is used to schedule rendering of the component.
     */
    @Override
    protected String getMainComponentName() {
        return "InitProject";
    }

    /**
     * Returns whether dev mode should be enabled.
     * This enables e.g. the dev menu.
     */
    @Override
    protected boolean getUseDeveloperSupport() {
        return BuildConfig.DEBUG;
    }

    /**
     * A list of packages used by the app. If the app uses additional views
     * or modules besides the default ones, add more packages here.
     */
    @Override
    protected List<ReactPackage> getPackages() {
        return Arrays.<ReactPackage>asList(
            new MainReactPackage(),
            new UmengPushPackage()
        );
    }
}
