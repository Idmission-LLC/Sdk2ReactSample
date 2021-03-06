//
//  IDMissionSDK.m
//  idmission
//

#import "IDMissionSDK.h"
#import <React/RCTEventEmitter.h>
#import "IDMTest-Swift.h"

@interface IDMissionSDK ()

@end

@implementation IDMissionSDK

static IDMissionSDK *gInstance = NULL;

- (NSArray<NSString *> *)supportedEvents
{
  return @[@"onSessionConnect",@"DataCallback"];

}

RCT_EXPORT_METHOD(initializeSDK:(NSString *)loginId password:(NSString *)password merchantid:(NSString *)merchantid myCallback:(RCTResponseSenderBlock)callback)
{
  UIViewController *rootViewController = [UIApplication sharedApplication].delegate.window.rootViewController;
  gInstance = self;
  dispatch_async(dispatch_get_main_queue(), ^{
    [rootViewController viewDidLoad];
  });
}

RCT_EXPORT_METHOD(serviceID20)
{
  UIViewController *rootViewController = [UIApplication sharedApplication].delegate.window.rootViewController;
  
  dispatch_async(dispatch_get_main_queue(), ^{
    IDentitySDKHelper *client = [IDentitySDKHelper new];
    [client startIDValidationsWithInstances: rootViewController];
  });
}

RCT_EXPORT_METHOD(serviceID10)
{
  UIViewController *rootViewController = [UIApplication sharedApplication].delegate.window.rootViewController;
  
  dispatch_async(dispatch_get_main_queue(), ^{
    IDentitySDKHelper *client = [IDentitySDKHelper new];
    [client startIDValidationAndMatchFacesWithInstances: rootViewController];
  });
}

RCT_EXPORT_METHOD(serviceID50:(NSString *)uniqueNumber)
{
  UIViewController *rootViewController = [UIApplication sharedApplication].delegate.window.rootViewController;
  
  dispatch_async(dispatch_get_main_queue(), ^{
    IDentitySDKHelper *client = [IDentitySDKHelper new];
    [client startIDValidationAndCustomerEnrollsWithUniqueNumbers:uniqueNumber instances:rootViewController];
  });
}

RCT_EXPORT_METHOD(serviceID175:(NSString *)uniqueNumber)
{
  UIViewController *rootViewController = [UIApplication sharedApplication].delegate.window.rootViewController;
  
  dispatch_async(dispatch_get_main_queue(), ^{
    IDentitySDKHelper *client = [IDentitySDKHelper new];
    [client startCustomerEnrollBiometricssWithUniqueNumbers:uniqueNumber instances:rootViewController];
  });
}

RCT_EXPORT_METHOD(serviceID105:(NSString *)uniqueNumber)
{
  UIViewController *rootViewController = [UIApplication sharedApplication].delegate.window.rootViewController;
  
  dispatch_async(dispatch_get_main_queue(), ^{
    IDentitySDKHelper *client = [IDentitySDKHelper new];
    [client startCustomerVerificationsWithUniqueNumbers:uniqueNumber instances:rootViewController];
  });
}

RCT_EXPORT_METHOD(serviceID185)
{
  UIViewController *rootViewController = [UIApplication sharedApplication].delegate.window.rootViewController;
  
  dispatch_async(dispatch_get_main_queue(), ^{
    IDentitySDKHelper *client = [IDentitySDKHelper new];
    [client startIdentifyCustomersWithInstances: rootViewController];
  });
}

RCT_EXPORT_METHOD(serviceID660)
{
  UIViewController *rootViewController = [UIApplication sharedApplication].delegate.window.rootViewController;
  
  dispatch_async(dispatch_get_main_queue(), ^{
    IDentitySDKHelper *client = [IDentitySDKHelper new];
    [client startLiveFaceChecksWithInstances: rootViewController];
  });
}

RCT_EXPORT_MODULE(IDMissionSDK);

+ (void)getEvent: (NSString*)type dict:(NSMutableDictionary*) dict
{
 
  [gInstance sendEventWithName:type body:dict];

}

- (void) getEvent2: (NSString*)type dict:(NSMutableDictionary*) dict
{
  [gInstance sendEventWithName:type body:dict];
}

@end
