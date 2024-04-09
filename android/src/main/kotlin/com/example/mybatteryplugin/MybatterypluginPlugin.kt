package com.example.mybatteryplugin

import android.content.Context
import android.os.BatteryManager
import android.os.Build
import androidx.annotation.RequiresApi
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.observables.ConnectableObservable
import java.util.concurrent.TimeUnit

/** MybatterypluginPlugin */
class MybatterypluginPlugin: FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel
  private lateinit var batteryManager: BatteryManager

  private lateinit var eventChannel : EventChannel
  private var disposable: Disposable? = null

  private val BATTERY_LEVEL_EVENT_CHANNEL_NAME = "battery_level_event_channel_name"

  override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "mybatteryplugin")
    channel.setMethodCallHandler(this)

    batteryManager = flutterPluginBinding.applicationContext.getSystemService(Context.BATTERY_SERVICE) as BatteryManager

    eventChannel = EventChannel(flutterPluginBinding.binaryMessenger, BATTERY_LEVEL_EVENT_CHANNEL_NAME)
    eventChannel.setStreamHandler(object: EventChannel.StreamHandler {
      @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
      override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
        observeBatteryLevel(events)
      }

      override fun onCancel(arguments: Any?) {
        disposable?.dispose()
      }
    })
  }

  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  private fun observeBatteryLevel(events: EventChannel.EventSink?) {
    if (disposable == null) {
      disposable = ConnectableObservable.interval(0L, 1000L, TimeUnit.MILLISECONDS)
        .flatMapSingle {
        Single.create{
          try {
            val batteryLevel = getBatteryLevel()
            it.onSuccess(batteryLevel)
          } catch (e: Exception) {
            it.onError(e)
          }
        }
      }.doFinally {
        disposable = null
      } .observeOn(AndroidSchedulers.mainThread()).subscribe({
          events?.success(it)
        }, {
          events?.error(it.javaClass.simpleName, it.message, null)
        })
    }
  }

  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  private fun getBatteryLevel(): Int {
    return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
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

    eventChannel.setStreamHandler(null)
    disposable?.dispose()
  }
}
