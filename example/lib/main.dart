import 'package:audio_notification/audio_notification.dart';
import 'package:flutter/material.dart';
import 'dart:async';
import 'package:flutter/services.dart';

void main() => runApp(new MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => new _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String status = 'hidden';

  @override
  void initState() {
    super.initState();

    AudioNotification.setListener('pause', () {
      setState(() => status = 'pause');
    });

    AudioNotification.setListener('play', () {
      setState(() => status = 'play');
    });

    AudioNotification.setListener('like', () {
      show(
        'https://mcontent.migu.cn/newlv2/new/album/20201001/1135110518/m_fdR03cgQXXExhYvk.jpg',
        'Title',
        'Song author',
        true,
      );
    });

    AudioNotification.setListener('unlike', () {
      show(
        'https://mcontent.migu.cn/newlv2/new/album/20201001/1135110518/m_fdR03cgQXXExhYvk.jpg',
        'Title',
        'Song author',
        false,
      );
    });

    AudioNotification.setListener('next', () {});

    AudioNotification.setListener('prev', () {});

    AudioNotification.setListener('select', () {});
  }

  Future<void> hide() async {
    try {
      await AudioNotification.hide();
      setState(() => status = 'hidden');
    } on PlatformException {}
  }

  Future<void> show(cover, title, author, like) async {
    try {
      await AudioNotification.show(
        cover: cover,
        title: title,
        author: author,
        play: true,
        like: like,
      );
      setState(() => status = 'play');
    } on PlatformException {}
  }

  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      home: new Scaffold(
        appBar: new AppBar(
          title: const Text('Plugin example app'),
        ),
        body: new Center(
            child: Container(
          height: 250.0,
          child: Column(
            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
            children: <Widget>[
              FlatButton(
                child: Text('Show notification'),
                onPressed: () => show(
                  'https://mcontent.migu.cn/newlv2/new/album/20201001/1135110518/m_fdR03cgQXXExhYvk.jpg',
                  'Title',
                  'Song author',
                  false,
                ),
              ),
              FlatButton(
                child: Text('Update notification'),
                onPressed: () => show(
                  'https://mcontent.migu.cn/newlv2/new/album/20200930/1135580872/m_2kdPoebjOgSvCJ2O.jpg',
                  'New title',
                  'New song author',
                  true,
                ),
              ),
              FlatButton(
                child: Text('Hide notification'),
                onPressed: hide,
              ),
              Text('Status: ' + status)
            ],
          ),
        )),
      ),
    );
  }
}
