package com.liuchungui.react_native_umeng_push;

import android.support.annotation.Nullable;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
//import com.umeng.message.UmengRegistrar;
import com.umeng.message.entity.UMessage;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by user on 16/4/7.
 */
public class UmengPushModule extends ReactContextBaseJavaModule implements LifecycleEventListener {
    protected static final String TAG = UmengPushModule.class.getSimpleName();
    protected static final String DidReceiveMessage = "DidReceiveMessage";
    protected static final String DidOpenMessage = "DidOpenMessage";

    private UmengPushApplication mPushApplication;
    private ReactApplicationContext mReactContext;

    public UmengPushModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mReactContext = reactContext;
        //设置module给application
        UmengPushApplication application = (UmengPushApplication)reactContext.getBaseContext();
        mPushApplication = application;
        //添加监听
        mReactContext.addLifecycleEventListener(this);
    }

    @Override
    public String getName() {
        return "UmengPush";
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        constants.put(DidReceiveMessage, DidReceiveMessage);
        constants.put(DidOpenMessage, DidOpenMessage);
        return constants;
    }

    /**
     * 获取设备id
     * @param callback
     */
    @ReactMethod
    public void getDeviceToken(Callback callback) {
        String registrationId = mPushApplication.mPushAgent.getRegistrationId();
        callback.invoke(registrationId == null || "".equals(registrationId) ? mPushApplication.mRegistrationId : registrationId);
    }

    private WritableMap convertToWriteMap(UMessage msg) {
        WritableMap map = Arguments.createMap();
        //遍历Json
        JSONObject jsonObject = msg.getRaw();
        Iterator<String> keys = jsonObject.keys();
        String key;
        while (keys.hasNext()) {
            key = keys.next();
            try {
                map.putString(key, jsonObject.get(key).toString());
            }
            catch (Exception e) {
                Log.e(TAG, "putString fail");
            }
        }
        return map;
    }


    protected void sendEvent(String eventName, UMessage msg) {
        sendEvent(eventName, convertToWriteMap(msg));
    }

    private void sendEvent(String eventName, @Nullable WritableMap params) {
        //此处需要添加hasActiveCatalystInstance，否则可能造成崩溃
        //问题解决参考: https://github.com/walmartreact/react-native-orientation-listener/issues/8
        if(mReactContext.hasActiveCatalystInstance()) {
            Log.i(TAG, "hasActiveCatalystInstance");
            mReactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(eventName, params);
        }
        else {
            Log.i(TAG, "not hasActiveCatalystInstance");
        }
    }

    @Override
    public void onHostResume() {
        mPushApplication.setmPushModule(this);
    }

    @Override
    public void onHostPause() {
    }

    @Override
    public void onHostDestroy() {
        mPushApplication.setmPushModule(null);
    }

}
