'use strict';

import React, {
  NativeModules,
  DeviceEventEmitter,
} from 'react-native';

const umengPush = NativeModules.UmengPush;
const _notifHandlers = [];

export const LaunchAppEvent = umengPush.LaunchAppEvent
export const OpenUrlEvent = umengPush.OpenUrlEvent
export const OpenActivityEvent = umengPush.OpenActivityEvent
export const DealWithCustomActionEvent = umengPush.DealWithCustomActionEvent
export const GetNotificationEvent = umengPush.GetNotificationEvent
export const DealWithCustomMessageEvent = umengPush.DealWithCustomMessageEvent

export default class UmengPushNotification {
  static getRegistrationId(handler: Function) {
      umengPush.getRegistrationId(handler);
  }

  static setDebugMode(debugMode: boolean) {
    umengPush.setDebugMode(debugMode);
  }

  static addEventListener(eventName: string, handler: Function) {
    const listener = DeviceEventEmitter.addListener(eventName, (event) => {
      handler(event);
    });
  }
}
