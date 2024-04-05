import 'package:flutter_test/flutter_test.dart';
import 'package:mybatteryplugin/mybatteryplugin.dart';
import 'package:mybatteryplugin/mybatteryplugin_platform_interface.dart';
import 'package:mybatteryplugin/mybatteryplugin_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockMybatterypluginPlatform
    with MockPlatformInterfaceMixin
    implements MybatterypluginPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');

  @override
  Future<num?> getBatteryLevel() => Future.value(21);
}

void main() {
  final MybatterypluginPlatform initialPlatform = MybatterypluginPlatform.instance;

  test('$MethodChannelMybatteryplugin is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelMybatteryplugin>());
  });

  test('getBatteryLevel', () async {
    Mybatteryplugin mybatterypluginPlugin = Mybatteryplugin();
    MockMybatterypluginPlatform fakePlatform = MockMybatterypluginPlatform();
    MybatterypluginPlatform.instance = fakePlatform;

    expect(await mybatterypluginPlugin.getBatteryLevel(), 42);
  });
}
