ZoomableTextureView
-------

Enables pinch to zoom and scroll gestures on video while it's playing. Can be used with MediaPlayer and the new ExoPlayer API. Inspired by similar implementation for ImageView http://stackoverflow.com/questions/6650398/android-imageview-zoom-in-and-zoom-out

![](https://github.com/Manuiq/ZoomableTextureView/raw/master/example.gif)

Usage
-------

Library is distributed via jCenter
```groovy
dependencies {
  implementation 'ua.polohalo.zoomabletextureview:zoomabletextureview:1.0.0'
}
```

Just include the custom view in your XML layout:
```xml
    <ua.polohalo.zoomabletextureview.ZoomableTextureView
        android:id="@+id/textureView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:maxScale="6" />
```

 **app:maxScale="6"** determines that the max zooming factor will be 600%.

Main limitation of this library is the fact that it doesn't properly handle "wrap_content" size so you need to set it manually

In case you want to use it with the new SimpleExoPlayerView instead of ExoPlayer(provides controls out of the box) there is a sample on how to do just that [here.](https://github.com/Manuiq/ZoomableTextureView/blob/master/app/src/main/java/ua/polohalo/zoomabletextureview/sample/ZoomableExoPlayerView.java)

License
-------

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


 [1]: http://square.github.io/okhttp
 [2]: https://github.com/square/okhttp/wiki
 [3]: https://search.maven.org/remote_content?g=com.squareup.okhttp3&a=okhttp&v=LATEST
 [4]: https://search.maven.org/remote_content?g=com.squareup.okhttp3&a=mockwebserver&v=LATEST
 [snap]: https://oss.sonatype.org/content/repositories/snapshots/
