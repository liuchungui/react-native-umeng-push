package com.liuchungui.react_native_umeng_push;

import android.app.Notification;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.UmengRegistrar;
import com.umeng.message.entity.UMessage;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by user on 16/4/7.
 */
public class UmengPushModule extends ReactContextBaseJavaModule {
    protected static final String TAG = UmengPushModule.class.getSimpleName();
    private static final String LaunchAppEvent = "didLaunchApp";
    private static final String OpenUrlEvent = "didOpenUrl";
    private static final String OpenActivityEvent = "didOpenActivity";
    private static final String DealWithCustomActionEvent = "didDealWithCustomAction";
    private static final String GetNotificationEvent = "getNotification";
    private static final String DealWithCustomMessageEvent = "didDealWithCustomMessage";

    private String mRegistrationId;
    private PushAgent mPushAgent;
    private ReactApplicationContext mReactContext;

    public UmengPushModule(ReactApplicationContext reactContext) {
        super(reactContext);
        Log.i(TAG, "load push module");
        mReactContext = reactContext;
        enablePush(reactContext);
    }

    @Override
    public String getName() {
        return "UmengPush";
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        constants.put("LaunchAppEvent", LaunchAppEvent);
        constants.put("OpenUrlEvent", OpenUrlEvent);
        constants.put("OpenActivityEvent", OpenActivityEvent);
        constants.put("DealWithCustomActionEvent", DealWithCustomActionEvent);
        constants.put("GetNotificationEvent", GetNotificationEvent);
        constants.put("DealWithCustomMessageEvent", DealWithCustomMessageEvent);
        return constants;
    }

    /**
     * 获取设备id
     * @param callback
     */
    @ReactMethod
    public void getRegistrationId(Callback callback) {
        String registrationId = UmengRegistrar.getRegistrationId(mReactContext);
        callback.invoke(registrationId == null ? mRegistrationId : registrationId);
    }

    @ReactMethod
    public void setDebugMode(boolean debugMode) {
        mPushAgent.setDebugMode(debugMode);
    }

    //开启推送
    private void enablePush(final ReactApplicationContext reactContext) {
        //开启推送
        mPushAgent = PushAgent.getInstance(reactContext);
        mPushAgent.enable(new IUmengRegisterCallback() {
            @Override
            public void onRegistered(final String s) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "enable push, registrationId = " + s);
                        mRegistrationId = s;
                    }
                });
            }
        });
        //统计应用启动数据
        PushAgent.getInstance(reactContext).onAppStart();

        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void launchApp(Context context, UMessage msg) {
                super.launchApp(context, msg);
                Log.i(TAG, "launchApp");
                sendEvent(reactContext, LaunchAppEvent, msg);
            }

            @Override
            public void openUrl(Context context, UMessage msg) {
                super.openUrl(context, msg);
                sendEvent(reactContext, OpenUrlEvent, msg);
            }

            @Override
            public void openActivity(Context context, UMessage msg) {
                super.openActivity(context, msg);
                sendEvent(reactContext, OpenActivityEvent, msg);
            }

            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                super.dealWithCustomAction(context, msg);
                sendEvent(reactContext, DealWithCustomActionEvent, msg);
            }
        };

        //设置通知点击处理者
        mPushAgent.setNotificationClickHandler(notificationClickHandler);

        //设置消息和通知的处理
        mPushAgent.setMessageHandler(new UmengMessageHandler() {
            @Override
            public Notification getNotification(Context context, UMessage msg) {
                sendEvent(mReactContext, GetNotificationEvent, msg);
                return super.getNotification(context, msg);
            }

            @Override
            public void dealWithCustomMessage(Context context, UMessage msg) {
                super.dealWithCustomMessage(context, msg);
                sendEvent(mReactContext, DealWithCustomMessageEvent, msg);
            }
        });
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


    private void sendEvent(ReactContext reactContext,
                           String eventName,
                           UMessage msg) {
        sendEvent(reactContext, eventName, convertToWriteMap(msg));
    }

    private void sendEvent(ReactContext reactContext,
                           String eventName,
                           @Nullable WritableMap params) {
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(eventName, params);
    }
}