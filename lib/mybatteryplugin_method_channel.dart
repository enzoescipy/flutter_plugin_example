import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'mybatteryplugin_platform_interface.dart';

/// An implementation of [MybatterypluginPlatform] that uses method channels.
class MethodChannelMybatteryplugin extends MybatterypluginPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('mybatteryplugin');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  @override
  Future<num?> getBatteryLevel() {
    return methodChannel.invokeMethod<num?>('getBatteryLevel');
  }
}
