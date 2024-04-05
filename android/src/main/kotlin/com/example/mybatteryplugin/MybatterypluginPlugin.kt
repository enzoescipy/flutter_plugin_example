package com.example.mybatteryplugin

import android.content.Context
import android.os.BatteryManager
import androidx.annotation.NonNull

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** MybatterypluginPlugin */
class MybatterypluginPlugin: FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel
  private lateinit var batteryManager: BatteryManager

  override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "mybatteryplugin")
    channel.setMethodCallHandler(this)

    batteryManager = flutterPluginBinding.applicationContext.getSystemService(Context.BATTERY_SERVICE) as BatteryManager

  }

  override fun onMethodCall(call: MethodCall, result: Result) {
    if (call.method == "getBatteryLevel") {
      if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
        result.success(batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY));
      } else {
        result.error("WRONG_VERSION", "android version not supported", "");
      }
    } else {
      result.notImplemented()
    }
  }


  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }
}
