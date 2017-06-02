# react-native-umeng-push
## 安装
```
rnpm install react-native-umeng-push
```

## 集成到iOS
在`Appdelegate.m`中对应的位置添加如下三个API：

```
#import "RCTUmengPush.h"

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
  //注册友盟推送
  [RCTUmengPush registerWithAppkey:@"your app key" launchOptions:launchOptions];
}

- (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken
{
  //获取deviceToken
  [RCTUmengPush application:application didRegisterDeviceToken:deviceToken];
}

- (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo
{
  //获取远程推送消息
  [RCTUmengPush application:application didReceiveRemoteNotification:userInfo];
}
```
参考：
https://developer.apple.com/library/content/documentation/IDEs/Conceptual/AppDistributionGuide/AddingCapabilities/AddingCapabilities.html#//apple_ref/doc/uid/TP40012582-CH26-SW6

启用推送设置 Enabling Push Notifications（否则会报 iOS device_token 无效）

## 集成到android
注意：0.29版本以后，reactNative会自动创建MainApplication，并且将添加原生模块从MainActivity移到了MainApplication，详情请见[http://reactnative.cn/post/1774](http://reactnative.cn/post/1774)，所以我们的这里继承也有些变化，如果你的reactNative版本是0.29以下，请点击[README-pre.md](https://github.com/liuchungui/react-native-umeng-push/blob/master/README-pre.md)

#### 1、添加PushSDK
由于这个库依赖于[react-native-umeng-sdk](https://github.com/liuchungui/react-native-umeng-sdk.git)，需要在你的工程`settings.gradle`文件中添加`PushSDK`。

```
include ':PushSDK'
project(':PushSDK').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-umeng-sdk/android/PushSDK')
```

#### 2、设置Application
新版本的react-native工程已经存在`MainApplication`，我们只需要将`MainApplication`继承`UmengApplication`，然后添加对应的`UmengPushPackage`进去就行了，如下所示：

```java
import com.liuchungui.react_native_umeng_push.UmengPushApplication;
import com.liuchungui.react_native_umeng_push.UmengPushPackage;

public class MainApplication extends UmengPushApplication implements ReactApplication {

  private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {
    @Override
    protected boolean getUseDeveloperSupport() {
      return BuildConfig.DEBUG;
    }

    @Override
    protected List<ReactPackage> getPackages() {
      return Arrays.<ReactPackage>asList(
          new MainReactPackage(),
          new UmengPushPackage()
      );
    }
  };
```   

注：这一步主要是因为友盟推送需要在Application当中接收推送，`UmengPushApplication`封装了友盟推送的内容。如果友盟推送如果不放在Application当中，退出应用之后是无法接收到推送的。

#### 3、添加AppKey & Umeng Message Secret
在项目工程的`AndroidManifest.xml`中的`Application`标签下添加:

```
<meta-data
    android:name="UMENG_APPKEY"
    android:value="xxxxxxxxxxxxxxxxxxxxxxxxxxxx" >
</meta-data>
<meta-data
    android:name="UMENG_MESSAGE_SECRET"
    android:value="xxxxxxxxxxxxxxxxxxxxxxxxxxxx" >
</meta-data>
```

#### 4、安卓集成获取不到deviceToken问题
* 确定是否将appkey、MessageSecret、以及包名都更换为开发者所申请的相应值
* 如果获取不到deviceToken也接收不到推送，请查看友盟后台的包名是否一致，当前设备是否添加到测试设备当中
* Android Studio中gradle的版本需要在1.5.0或者以上

更多DeviceToken相关问题，请参考[Device_token 相关问题整理【安卓版】](http://bbs.umeng.com/thread-15233-1-1.html)

#### 5、其它
**注：**如果是android6.0以上的api编译，需要在PushSDK的build.gradle文件的android{}块内添加useLibrary 'org.apache.http.legacy'，并把compileSdkVersion的版本号改为23。

详情参考：[友盟安卓SDK集成指南](http://dev.umeng.com/push/android/integration)

## API

| API | Note |    
|---|---|
| `getDeviceToken` | 获取DeviceToken |
| `didReceiveMessage` | 接收到推送消息回调的方法 |
| `didOpenMessage` | 点击推送消息打开应用回调的方法 |


## Usage

```
import UmengPush from 'react-native-umeng-push';

//获取DeviceToken
UmengPush.getDeviceToken(deviceToken => {
    console.log("deviceToken: ", deviceToken);
});

//接收到推送消息回调
UmengPush.didReceiveMessage(message => {
    console.log("didReceiveMessage:", message);
});

//点击推送消息打开应用回调
UmengPush.didOpenMessage(message => {
    console.log("didOpenMessage:", message);
});

```
**具体使用详情，请下载代码查看Example**

## Example
```
git clone https://github.com/liuchungui/react-native-umeng-push.git
cd react-native-umeng-push/Example
npm install --save
```


## More
* 欢迎大家Pull Request
* 有什么疑问，欢迎提问题
* 觉得好的，来一个star
