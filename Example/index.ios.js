/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 */

import React, {
  AppRegistry,
  Component,
  StyleSheet,
  Text,
  View,
  NativeAppEventEmitter,
  NativeModules,
} from 'react-native';

var UmengPush = NativeModules.UmengPush;

var subscription = NativeAppEventEmitter.addListener(
  'DidReceiveMessage',
  (userInfo) => {
    console.log(userInfo);
    alert("DidReceiveMessage");
  }
);

NativeAppEventEmitter.addListener(
  'DidOpenAPNSMessage',
  (userInfo) => {
    console.log(userInfo);
    alert("DidOpenAPNSMessage");
  }
);

class InitProject extends Component {
  compoentWillUnmount() {
    subscription.remove();
  }

  componentDidMount() {
    // console.log(UmengPush);
    //设置不弹提醒
    UmengPush.setAutoAlert(false);
  }
  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.welcome}>
          Welcome to React Native!
        </Text>
        <Text style={styles.instructions}>
          To get started, edit index.ios.js
        </Text>
        <Text style={styles.instructions}>
          Press Cmd+R to reload,{'\n'}
          Cmd+D or shake for dev menu
        </Text>
      </View>
    );
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
