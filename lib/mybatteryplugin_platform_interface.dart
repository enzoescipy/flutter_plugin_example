import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'mybatteryplugin_method_channel.dart';

abstract class MybatterypluginPlatform extends PlatformInterface {
  /// Constructs a MybatterypluginPlatform.
  MybatterypluginPlatform() : super(token: _token);

  static final Object _token = Object();

  static MybatterypluginPlatform _instance = MethodChannelMybatteryplugin();

  /// The default instance of [MybatterypluginPlatform] to use.
  ///
  /// Defaults to [MethodChannelMybatteryplugin].
  static MybatterypluginPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [MybatterypluginPlatform] when
  /// they register themselves.
  static set instance(MybatterypluginPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }

  Future<num?> getBatteryLevel() {
    throw UnimplementedError('getBatteryLevel() has not been implemented.');
  }

  Stream? getBatteryLevelStream() {
    throw UnimplementedError('getBatteryLevelStream() has not been implemented.');
  }
}
