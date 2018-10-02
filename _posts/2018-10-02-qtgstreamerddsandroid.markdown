---
layout: post
title: Qt+GStreamer+DDS+Android
categories:
- qt
- android
- dds
- gstreamer
---

![QML app with DDS and GStreamer on Android](/images/qmlddsgstandroid.jpg)

Greetings, web wanderer. I'm sorry to see you here, because I know your pain. Be brave, though it is almost certain that this post will not solve your problem, it may lead you to the next one.

What you see in the picture is an Android app, written in Qt, streaming video with GStreamer over DDS. It is the result of days and days of wrestling with build tools. If you have any chance to drop any of these dependencies, I wholeheartedly suggest you do. Any two of these is enough to ruin your day, any 3 is likely to take a few more days, and all 4 may not be possible at all with any sort of reproducability.

Before getting into details, I'd like to propose "de Vos third law of compilation", which states that the probability of a successful build scales with 1/N^2 where N is the number of build tools and compilation steps. This is a conservative estimate. It is suggested to take 2N when cross-compiling.

So let's have a look what we're dealing with. Qt has its own `qmake` system, which generates makefiles, which invoke `moc` for an extra code generation step, resulting in 3 steps. RTI Connext DDS has `rtiddsgen` which generates makefiles and code for your custom data types, for anther 3 steps. Android uses Gradle, with CMake for the native parts, which of course generates makefiles. GStreamer seems like a pretty normal C library, but when Android comes into play, you'd be wrong. I lost count. Let's say 12, or 24 because we're cross-compiling, resulting in a chance of success of 1/24^2*100=0.17%.

To reduce N, and tie all these incompatible build systems together, I chose to use CMake.
Let's break down the problem, and dive in.
Some customized build scripts can be found in [this gist](https://gist.github.com/pepijndevos/60a6b76e1f00b80a4826f157a24adce7).

### DDS+Qt

Probably the least interesting, and the most easy. There is a [Qt CMake manual](http://doc.qt.io/qt-5/cmake-manual.html), and the DDS part is as simple as copying some defines and running `rtiddsgen`. To do that, I found [some handy CMake file](https://github.com/ADLINK-IST/opensplice/blob/master/examples/tmp/demo/ishapes/cmake/FindOpenSplice.cmake), which I adapted from OpenSplice to RTI Connext. See above gist.

### Qt+GStreamer

This one took a bit more work. Of course, on your average Linux box you can just `apt-get` all the things, so the main challenge here is rendering the GStreamer sink inside a QtQuick2 app. I will present two ways, one that works, and one that works properly, but is more painful.

The Qt widgets way is to take a widget, get its window ID, and re-parent the sink to the widget. The problem is that QtQuick Controls 2 are no longer based on Qt widgets, so they don't have a window ID. Instead you can re-parent to the top-level window and set the bounding box.

{% highlight c++ %}
QQmlApplicationEngine* engine;
engine.load(QUrl(QStringLiteral("qrc:/main.qml")));
// ...
QObject* m_rootObject = engine->rootObjects().first();
WId wid = -1;
if(m_rootObject) {
    QWindow *window = qobject_cast<QWindow *>(m_rootObject);
    if(window) {
        wid = window->winId();
    }
}
// ...
video_sink = gst_element_factory_make("glimagesink", "data.video_sink");
// ...
gst_video_overlay_set_window_handle (GST_VIDEO_OVERLAY (video_sink), wid);
gst_video_overlay_set_render_rectangle(GST_VIDEO_OVERLAY (video_sink), x, y, width, height);
gst_video_overlay_expose(GST_VIDEO_OVERLAY (video_sink));
{% endhighlight %}

However, this only really works on Linux. On Windows the render rectangle is ignored and then it segfaults. On Android window_handle expects an `ANativeActivity*`, which is hard to come by in Qt land. The solution is to use `qmlglsink`, which sadly does not come precompiled with any GStreamer installation.

Luckily, if you download `gst-plugins-good` from a Github release rather than the official download, you'll find `ext/qt/qtplugin.pro`, which allows you to compile the plugin. At least, once you change the `DEFINES` to `HAVE_QT_X11`, `HAVE_QT_WAYLAND` and/or `HAVE_QT_EGLFS`(Android). After copying the resulting `.so` to your plugin folder, you can verify with `gst-inspect-1.0 qmlglsink` that there is a chance that it might work. There is even some [useful example code](https://github.com/GStreamer/gst-plugins-good/tree/master/tests/examples/qt/qmlsink). The key parts are as follows.

{% highlight c++ %}
QQuickItem* videoItem; // from somewhere
/* the plugin must be loaded before 
 * loading the qml file to register the
 * GstGLVideoItem qml item */
video_sink = gst_element_factory_make("qmlglsink", "data.video_sink");
g_object_set(video_sink, "widget", videoItem, NULL);
{% endhighlight %}

{% highlight qml %}
import org.freedesktop.gstreamer.GLVideoItem 1.0

GstGLVideoItem {
  id: videoView
  onItemInitializedChanged: {
    if (itemInitialized) {
      // play the video
    }
  }
}
{% endhighlight %}


### Android

This is where everything gets ten times harder. If it wasn't for [qt-android-cmake](https://github.com/LaurentGomila/qt-android-cmake), I'd have rewritten the whole thing for `qmake`. That would have reclaimed some sanity in some places, and lose some in others. As its author put it:

> When using Qt for Android development, QMake & QtCreator is the only sane option for compiling and deploying.

Take extreme care which compilers, C++ STL library, API versions, NDK versions, Qt versions, etc. you're using, because nothing works if you pick the wrong one. Both DDS and Qt are built against the ancient 10e NDK, so I suggest using that. However, the default API version is 16, which does not have things needed to compile GStreamer, so you have to use API version 21, the most modern that ships with 10e. This NDK uses `gnustl` with `gcc`, rather than the more modern `llvm` NDK, which is supported by none of these libraries.

#### DDS

This is again relatively easy. It just takes a lot of defines to point it to the correct stuff.

{% highlight bash %}
export ANDROID_SDK=/home/rove/Android/Sdk/
export ANDROID_NDK=/home/rove/Android/android-ndk-r10e/
export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
export NDDSHOME=/home/rove/rti_connext_dds-5.3.1/
cmake .. -DRTI_TARGET_ARCH=armv7aAndroid5.0gcc4.9ndkr10e -DCMAKE_TOOLCHAIN_FILE=../../qt-android-cmake/toolchain/android.toolchain.cmake -DCMAKE_MAKE_PROGRAM=$(which make) -DANDROID_NATIVE_API_LEVEL=21 -DPKG_CONFIG_EXECUTABLE=$(which pkg-config)
{% endhighlight %}

#### Qt

Qt supports Android, so this should be relatively easy too. It turns out there is a bug in CMake that can be [worked around](https://stackoverflow.com/questions/38027292/configure-a-qt5-5-7-application-for-android-with-cmake/40256862#40256862) by editing `lib/cmake/Qt5Core/Qt5CoreConfigExtras.cmake` and deleting `set_property(TARGET Qt5::Core PROPERTY INTERFACE_COMPILE_FEATURES cxx_decltype)`.

I had some problem linking the DDS libraries from earlier, so I copied all the stuff into `NDDSHOME` and put that on the `CMAKE_FIND_ROOT_PATH` inside the `qt-android-cmake` toolchain. It's also worth noting I modified the manifest XML in the toolchain to give my app the permissions it needs. Maybe there is a neater way, but this works.

With all those issues out of the way, you can get a basic Qt app going with a ton of extra defines.

{% highlight bash %}
export ANDROID_SDK=/home/rove/Android/Sdk/
export ANDROID_NDK=/home/rove/Android/android-ndk-r10e/
export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
export NDDSHOME=/home/rove/rti_connext_dds-5.3.1/
export CMAKE_PREFIX_PATH=/home/rove/Qt/5.11.1/android_armv7/
export GSTREAMER_ROOT=/home/rove/bin/gst/armv7
export PKG_CONFIG_PATH=/home/rove/bin/gst/armv7/lib/pkgconfig/
cmake .. -DRTI_TARGET_ARCH=armv7aAndroid5.0gcc4.9ndkr10e -DCMAKE_TOOLCHAIN_FILE=../../qt-android-cmake/toolchain/android.toolchain.cmake -DCMAKE_MAKE_PROGRAM=$(which make) -DANDROID_NATIVE_API_LEVEL=21 -DPKG_CONFIG_EXECUTABLE=$(which pkg-config)
{% endhighlight %}

#### GStreamer

They provide [prebuilt Android binaries](https://gstreamer.freedesktop.org/data/pkg/android/), but still...

This is really the hardest one to get working with Qt and Android. It needs some special setup, that is taken care of by a `ndk-build` thing that ties into the Gradle build system of vanilla Android apps, but since we're on Qt, we have to do this by ourselves. Better get some more tea/coffee/hot chocolate.

[Life-saving example code from ystreet](https://github.com/ystreet/qt-gstreamer-android).

Step one is some manual template expansion. Take [gstreamer_android-1.0.c.in](https://github.com/GStreamer/cerbero/blob/master/data/ndk-build/gstreamer_android-1.0.c.in), and copy it into your project. If you don't have any fonts and stuff, you can ignore `GStreamer.java` and comment out everything that refers to it at the bottom of `gstreamer_android-1.0.c`.

Next you need to replace `@PLUGINS_DECLARATION@` and `@PLUGINS_REGISTRATION@` with all the plugins you are using. Mine looks like this

{% highlight c++ %}
/* Declaration of static plugins */
GST_PLUGIN_STATIC_DECLARE(app);
GST_PLUGIN_STATIC_DECLARE(rtpmanager);
GST_PLUGIN_STATIC_DECLARE(rtp);
GST_PLUGIN_STATIC_DECLARE(libav);
GST_PLUGIN_STATIC_DECLARE(videoscale);
GST_PLUGIN_STATIC_DECLARE(opengl);
GST_PLUGIN_STATIC_DECLARE(videoconvert);
GST_PLUGIN_STATIC_DECLARE(qmlgl);
GST_PLUGIN_STATIC_DECLARE(videofilter);

/* Declaration of static gio modules */

/* Call this function to register static plugins */
void
gst_android_register_static_plugins (void)
{
 GST_PLUGIN_STATIC_REGISTER(app);
 GST_PLUGIN_STATIC_REGISTER(rtpmanager);
 GST_PLUGIN_STATIC_REGISTER(rtp);
 GST_PLUGIN_STATIC_REGISTER(libav);
 GST_PLUGIN_STATIC_REGISTER(videoscale);
 GST_PLUGIN_STATIC_REGISTER(opengl);
 GST_PLUGIN_STATIC_REGISTER(videoconvert);
 GST_PLUGIN_STATIC_REGISTER(qmlgl);
 GST_PLUGIN_STATIC_REGISTER(videofilter);
}

/* Call this function to load GIO modules */
void
gst_android_load_gio_modules (void)
{
}
{% endhighlight %}

Next you'll need some initialization code, which you can copy from ystreet or from the gist I linked to at the top.

The GStreamer Android libs come with `pkg-config` files, but they have the wrong prefix. Nothing some `sed` magic can't fix. Or you might as well just hard-code all the flags and paths.

Once you set up your include and library paths and run make, you'll get a ton of link errors. That's good. First you'll get a small number that directly mention the plugins you registered. Add the plugin to `LIBS` (qmake) or `target_link_libraries` (cmake) and try again.

Now you'll get tons and tons of reference errors. Progress! Find their source and add them. Note that order matters, so add them at the bottom. Quick tip: you can use `ndk/path/bin/android-eabi-blah-ar` to list all the symbols in a library. Or Google them. Or use trial-and-error.

##### `qmlglsink`

The above will get you a working GStreamer+Qt app. But as mentioned, getting the sink to work is not fun.
However, due to the Qt dependency, the `qmlglsink` is not precompiled as part of `gst-plugins-good`, so you're left to do that by yourself.

The provided `qmake` file in `ext/qt` is written for Windows, so we'll need to change some things and mess around. First of all, we'd like a static library, so add `CONFIG += staticlib`. Next, we're not on Windows, so add `HAVE_QT_EGLFS` to `DEFINES`. Then a bunch more defines will let `qmake` run to completion.

{% highlight bash %}
export ANDROID_NDK_ROOT=/home/rove/Android/android-ndk-r10e/
export PKG_CONFIG_DIR=
export PKG_CONFIG_LIBDIR=/home/rove/bin/gst/armv7/lib/pkgconfig/
export PKG_CONFIG_SYSROOT_DIR=/
/home/rove/Qt/5.11.1/android_armv7/bin/qmake
{% endhighlight %}

However, this uses API version 16, which won't work due to missing some OpenGL headers. I'm sure there is a good way to do it, but you can also just `vim Makefile` and `:%s/16/21/gc`. If you get link errors, add them to `LIBS` and try again.

Finally, copy the `.a` to your other Android plugins, add the declaration and registration, resolve the link errors, and **DONE**.

### Conclusion

It works, but don't do it if you have other options.

I'm sure none of this works for you, and I can't help you. You'll just have to muddle through. All I can tell you is that it is a **giant** relief when it finally works, and you see a live video stream on your Android device. The best of luck.
