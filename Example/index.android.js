/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 */

import React, {
  AppRegistry,
  Component,
  StyleSheet,
  Text,
  View
} from 'react-native';

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
  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.welcome}>
          Welcome to React Native!
        </Text>
        <Text style={styles.instructions}>
          To get started, edit index.android.js
        </Text>
        <Text style={styles.instructions}>
          Shake or press menu button for dev menu
        </Text>
      </View>
    );
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

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
});

AppRegistry.registerComponent('InitProject', () => InitProject);
