#react-native-umeng-push
##安装
```
npm install react-native-umeng-push
```

##集成到iOS
在`Appdelegate.m`中对应的位置添加如下三个API：

```
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
 
##集成到android
####1、添加PushSDK
由于这个库依赖于[react-native-umeng-sdk](https://github.com/liuchungui/react-native-umeng-sdk.git)，需要在你的工程`settings.gradle`文件中添加`PushSDK`。

```
include ':PushSDK'
project(':PushSDK').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-umeng-sdk/android/PushSDK')
```

####2、设置Application
创建一个Application类，并继承`UmengPushApplication`，在主项目中的`AndroidManifest.xml`文件中指定application的名字为你创建的Application。   

注：这一步主要是因为友盟推送需要在Application当中接收推送，`UmengPushApplication`封装了友盟推送的内容。如果友盟推送如果不放在Application当中，退出应用之后是无法接收到推送的。

####3、添加AppKey & Umeng Message Secret
在项目工程的`AndroidManifest.xml`中的<Application>标签下添加:

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

####4、在项目的build.gradle里面配置applicationId
在自己项目的build.gradle里面一定要配置applicationId，`PushSDK`下的`AndroidManifest.xml`里面的${applicationId}会引用到applicationId。

如下所示： 

```
defaultConfig { 
applicationId "应用的包名" 
minSdkVersion 8 
targetSdkVersion 22 
}
```
**注：**如果是android6.0以上的api编译，需要在PushSDK的build.gradle文件的android{}块内添加useLibrary 'org.apache.http.legacy'，并把compileSdkVersion的版本号改为23。

详情参考：[友盟安卓SDK集成指南](http://dev.umeng.com/push/android/integration)

##API
* getDeviceToken 获取DeviceToken
* didReceiveMessage 接收到推送消息
* didOpenMessage 打开推送消息


##Usage
首先，引入库

```
import UmengPush from 'react-native-umeng-push';
```

然后，获取deviceToken

```
    UmengPush.getDeviceToken(deviceToken => {
      console.log("deviceToken: ", deviceToken);
    })
```

最后，监听事件

```
    UmengPush.didReceiveMessage(message => {
      console.log("didReceiveMessage:", message);
    });
    UmengPush.didOpenMessage(message => {
      console.log("didOpenMessage:", message);
    });
```
**使用详情，请下载代码查看Example**
