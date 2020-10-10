import 'dart:async';
import 'package:flutter/services.dart';
import 'package:meta/meta.dart';

class AudioNotification {
  static const MethodChannel _channel =
      const MethodChannel('audio_notification');
  static Map<String, Function> _listeners = new Map();

  static Future<dynamic> _myUtilsHandler(MethodCall methodCall) async {
    _listeners.forEach((event, callback) {
      if (methodCall.method == event) {
        callback();
        return true;
      }
    });
  }

  static Future show({
    @required cover,
    @required title,
    @required author,
    @required play,
    @required like,
  }) async {
    final Map<String, dynamic> params = <String, dynamic>{
      'cover': cover,
      'title': title,
      'author': author,
      'play': play,
      'like': like
    };
    await _channel.invokeMethod('show', params);

    _channel.setMethodCallHandler(_myUtilsHandler);
  }

  static Future hide() async {
    await _channel.invokeMethod('hide');
  }

  static setListener(String event, Function callback) {
    _listeners.addAll({event: callback});
  }
}
