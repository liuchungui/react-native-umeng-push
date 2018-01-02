//
//  RCTUmengPush.h
//  RCTUmengPush
//
//  Created by user on 16/4/24.
//  Copyright © 2016年 react-native-umeng-push. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <React/RCTBridgeModule.h>
#import <UserNotifications/UserNotifications.h>

@interface RCTUmengPush : NSObject <RCTBridgeModule, UNUserNotificationCenterDelegate>
+ (void)registerWithAppkey:(NSString *)appkey launchOptions:(NSDictionary *)launchOptions;
+ (void)application:(UIApplication *)application didRegisterDeviceToken:(NSData *)deviceToken;
+ (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo;
@end
