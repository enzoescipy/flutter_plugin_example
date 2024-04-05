import 'mybatteryplugin_platform_interface.dart';

class Mybatteryplugin {
  Future<String?> getPlatformVersion() {
    return MybatterypluginPlatform.instance.getPlatformVersion();
  }

  Future<num?> getBatteryLevel() {
    return MybatterypluginPlatform.instance.getBatteryLevel();
  }

  Stream? getBatteryLevelStream() {
    return MybatterypluginPlatform.instance.getBatteryLevelStream();
  }
}
