//
//  RCTUmengPush.m
//  RCTUmengPush
//
//  Created by user on 16/4/24.
//  Copyright © 2016年 react-native-umeng-push. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "RCTUmengPush.h"
#import "UMessage.h"
#import <React/RCTEventDispatcher.h>

#define UMSYSTEM_VERSION_GREATER_THAN_OR_EQUAL_TO(v)  ([[[UIDevice currentDevice] systemVersion] compare:v options:NSNumericSearch] != NSOrderedAscending)
#define _IPHONE80_ 80000

static NSString * const DidReceiveMessage = @"DidReceiveMessage";
static NSString * const DidOpenMessage = @"DidOpenMessage";
static RCTUmengPush *_instance = nil;

@interface RCTUmengPush ()
@property (nonatomic, copy) NSString *deviceToken;
@end
@implementation RCTUmengPush

@synthesize bridge = _bridge;
RCT_EXPORT_MODULE()

+ (instancetype)sharedInstance {
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        if(_instance == nil) {
            _instance = [[self alloc] init];
        }
    });
    return _instance;
}

+ (instancetype)allocWithZone:(struct _NSZone *)zone {
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        if(_instance == nil) {
            _instance = [super allocWithZone:zone];
            [_instance setupUMessage];
        }
    });
    return _instance;
}

+ (dispatch_queue_t)sharedMethodQueue {
    static dispatch_queue_t methodQueue;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        methodQueue = dispatch_queue_create("com.liuchungui.react-native-umeng-push", DISPATCH_QUEUE_SERIAL);
    });
    return methodQueue;
}

- (dispatch_queue_t)methodQueue {
    return [RCTUmengPush sharedMethodQueue];
}

- (NSDictionary<NSString *, id> *)constantsToExport {
    return @{
             DidReceiveMessage: DidReceiveMessage,
             DidOpenMessage: DidOpenMessage,
             };
}

- (void)didReceiveRemoteNotification:(NSDictionary *)userInfo {
    [self.bridge.eventDispatcher sendAppEventWithName:DidReceiveMessage body:userInfo];
}

- (void)didOpenRemoteNotification:(NSDictionary *)userInfo {
    [self.bridge.eventDispatcher sendAppEventWithName:DidOpenMessage body:userInfo];
}

RCT_EXPORT_METHOD(setAutoAlert:(BOOL)value) {
    [UMessage setAutoAlert:value];
}

RCT_EXPORT_METHOD(getDeviceToken:(RCTResponseSenderBlock)callback) {
    NSString *deviceToken = self.deviceToken;
    if(deviceToken == nil) {
        deviceToken = @"";
    }
    callback(@[deviceToken]);
}

/**
 *  初始化UM的一些配置
 */
- (void)setupUMessage {
    [UMessage setAutoAlert:NO];
}

+ (void)registerWithAppkey:(NSString *)appkey launchOptions:(NSDictionary *)launchOptions {
    //set AppKey and LaunchOptions
    [UMessage startWithAppkey:appkey launchOptions:launchOptions];
    
//#if __IPHONE_OS_VERSION_MAX_ALLOWED >= _IPHONE80_
//    if(UMSYSTEM_VERSION_GREATER_THAN_OR_EQUAL_TO(@"8.0"))
//    {
        //register remoteNotification types
        UIMutableUserNotificationAction *action1 = [[UIMutableUserNotificationAction alloc] init];
        action1.identifier = @"action1_identifier";
        action1.title=@"Accept";
        action1.activationMode = UIUserNotificationActivationModeForeground;//当点击的时候启动程序
        
        UIMutableUserNotificationAction *action2 = [[UIMutableUserNotificationAction alloc] init];  //第二按钮
        action2.identifier = @"action2_identifier";
        action2.title=@"Reject";
        action2.activationMode = UIUserNotificationActivationModeBackground;//当点击的时候不启动程序，在后台处理
        action2.authenticationRequired = YES;//需要解锁才能处理，如果action.activationMode = UIUserNotificationActivationModeForeground;则这个属性被忽略；
        action2.destructive = YES;
        
        UIMutableUserNotificationCategory *categorys = [[UIMutableUserNotificationCategory alloc] init];
        categorys.identifier = @"category1";//这组动作的唯一标示
        [categorys setActions:@[action1,action2] forContext:(UIUserNotificationActionContextDefault)];
        
        UIUserNotificationSettings *userSettings = [UIUserNotificationSettings settingsForTypes:UIUserNotificationTypeBadge|UIUserNotificationTypeSound|UIUserNotificationTypeAlert
                                                                                     categories:[NSSet setWithObject:categorys]];
        [UMessage registerForRemoteNotifications:userSettings];
        
    }
//    else{
//        //register remoteNotification types
//        [UMessage registerForRemoteNotifications:UIRemoteNotificationTypeBadge
//         |UIRemoteNotificationTypeSound
//         |UIRemoteNotificationTypeAlert];
//    }
//#else
//
//    //register remoteNotification types
//    [UMessage registerForRemoteNotificationTypes:UIRemoteNotificationTypeBadge
//     |UIRemoteNotificationTypeSound
//     |UIRemoteNotificationTypeAlert];
//
//#endif
    
    //由推送第一次打开应用时
    if(launchOptions[@"UIApplicationLaunchOptionsRemoteNotificationKey"]) {
        [self didReceiveRemoteNotificationWhenFirstLaunchApp:launchOptions[@"UIApplicationLaunchOptionsRemoteNotificationKey"]];
    }
    
#ifdef DEBUG
    [UMessage setLogEnabled:YES];
#endif
}

+ (void)application:(UIApplication *)application didRegisterDeviceToken:(NSData *)deviceToken {
    [RCTUmengPush sharedInstance].deviceToken = [[[[deviceToken description] stringByReplacingOccurrencesOfString: @"<" withString: @""]
                                                  stringByReplacingOccurrencesOfString: @">" withString: @""]
                                                 stringByReplacingOccurrencesOfString: @" " withString: @""];
    [UMessage registerDeviceToken:deviceToken];
}

+ (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo {
    [UMessage didReceiveRemoteNotification:userInfo];
    //send event
    if (application.applicationState == UIApplicationStateInactive) {
        [[RCTUmengPush sharedInstance] didOpenRemoteNotification:userInfo];
    }
    else {
        [[RCTUmengPush sharedInstance] didReceiveRemoteNotification:userInfo];
    }
}

+ (void)didReceiveRemoteNotificationWhenFirstLaunchApp:(NSDictionary *)launchOptions {
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.5 * NSEC_PER_SEC)), [self sharedMethodQueue], ^{
        //判断当前模块是否正在加载，已经加载成功，则发送事件
        if(![RCTUmengPush sharedInstance].bridge.isLoading) {
            [UMessage didReceiveRemoteNotification:launchOptions];
            [[RCTUmengPush sharedInstance] didOpenRemoteNotification:launchOptions];
        }
        else {
            [self didReceiveRemoteNotificationWhenFirstLaunchApp:launchOptions];
        }
    });
}

@end
