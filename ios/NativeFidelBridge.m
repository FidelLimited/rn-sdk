#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(NativeFidelBridge, NSObject)

RCT_EXTERN_METHOD(setup:(NSDictionary *)setupInfo)

RCT_EXTERN_METHOD(start)

RCT_EXTERN_METHOD(verifyCard:(NSDictionary *)cardVerificationConfiguration)

RCT_EXTERN_METHOD(identifyMetricsDataSource:(NSString *)name version:(NSString *)version)

@end
