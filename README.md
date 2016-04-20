#react-native-umeng-push
这是一个友盟推送的react-native库，暂时只支持安卓。

##android
###1、Installation
```
npm install react-native-umeng-push --save
```

###2、Linking to your gradle Project
首先，使用rnpm

```
rnpm link react-native-umeng-push
```

然后，由于这个库依赖于[react-native-umeng-sdk](https://github.com/liuchungui/react-native-umeng-sdk.git)，需要在你的工程`settings.gradle`文件中添加`PushSDK`。

```
include ':PushSDK'
project(':PushSDK').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-umeng-sdk/android/PushSDK')
```

###3、Usage
#####首先，引入库

```
import UmengPush, {
  LaunchAppEvent,
  OpenUrlEvent,
  OpenActivityEvent,
  DealWithCustomActionEvent,
  GetNotificationEvent,
  DealWithCustomMessageEvent,
} from 'react-native-umeng-push';
```
#####然后，获取deviceToken
```
    /**
     * 获取deviceToken
     */
    UmengPush.getRegistrationId(registrationId => {
      console.log("deviceToken: ", registrationId);
    })
```

####最后，监听事件
事件暂时有六个，与友盟的SDK一一对应

* LaunchAppEvent
* OpenUrlEvent
* OpenActivityEvent
* DealWithCustomActionEvent
* GetNotificationEvent
* DealWithCustomMessageEvent

代码可以参考如下：

```
    /**
     * 打开推送消息，进入app
     */
    UmengPush.addEventListener(LaunchAppEvent, (data) => {
      //启动app类型
      console.log(data);
    });
```

###4、Example
**注：可以下载代码看里面的Example**

```
import UmengPush, {
  LaunchAppEvent,
  OpenUrlEvent,
  OpenActivityEvent,
  DealWithCustomActionEvent,
  GetNotificationEvent,
  DealWithCustomMessageEvent,
} from 'react-native-umeng-push';

class InitProject extends Component {
  componentDidMount() {
    this._setupRemoteNotification();
  }
  //启动推送通知
  _setupRemoteNotification() {
    /**
     * 获取deviceToken
     */
    UmengPush.getRegistrationId(registrationId => {
      console.log("deviceToken: ", registrationId);
    })

    // UmengPush.setDebugMode(false);
    /**
     * 打开推送消息，进入app
     */
    UmengPush.addEventListener(LaunchAppEvent, (data) => {
      //启动app类型
      console.log(data);
    });

    /**
     * 打开某个Url跳入
     */
    UmengPush.addEventListener(OpenUrlEvent, (data) => {
      //启动app类型
      console.log(data);
    });

    /**
     * 打开指定页面（Activity）
     */
    UmengPush.addEventListener(OpenActivityEvent, (data) => {
      //启动app类型
      console.log(data);
    });

    /**
     * 获取推送通知和消息
     */
    UmengPush.addEventListener(GetNotificationEvent, (data) => {
      console.log(data);
    });

    /**
     * 自定义推送通知的处理
     */
    UmengPush.addEventListener(DealWithCustomActionEvent, (data) => {
      console.log(data);
    });

    /**
     * 自定义消息的处理
     */
    UmengPush.addEventListener(DealWithCustomMessageEvent, (data) => {
      console.log(data);
    });
  }
}
```
