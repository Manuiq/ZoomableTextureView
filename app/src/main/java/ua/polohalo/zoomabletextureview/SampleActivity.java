package ua.polohalo.zoomabletextureview;

import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.TextureView;
import android.widget.LinearLayout;

import java.io.IOException;
//todo convert to lib
public class SampleActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener, MediaPlayer.OnPreparedListener, SurfaceHolder.Callback {

    private ZoomableTextureView textureView;
    private ZoomableSurfaceView surfaceView;
    private MediaPlayer mediaPlayerSurface;
    private MediaPlayer mediaPlayerTexture;

    String path = "http://download.blender.org/peach/bigbuckbunny_movies/BigBuckBunny_320x180.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textureView = (ZoomableTextureView) findViewById(R.id.textureView);
        surfaceView = (ZoomableSurfaceView) findViewById(R.id.surfaceView);
        surfaceView.getHolder().addCallback(this);

        textureView.setSurfaceTextureListener(this);
    }

    private void prepareTextureView(SurfaceTexture surface) {
        Surface s = new Surface(surface);
        mediaPlayerTexture = new MediaPlayer();
        mediaPlayerTexture.setSurface(s);
        try {
            mediaPlayerTexture.setDataSource(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayerTexture.prepareAsync();
        mediaPlayerTexture.setOnPreparedListener(this);
    }

    private void stretchVideo(){//todo check why it isn't drawn by itself properly
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int maxHeight = metrics.heightPixels;
        int maxWidth = metrics.widthPixels;
        textureView.setLayoutParams(new LinearLayout.LayoutParams(maxWidth, maxHeight));
        textureView.updateVideoDimens(maxHeight, maxWidth);
    }


    /** TextureView **/
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        prepareTextureView(surfaceTexture);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }

    /** MediaPlayer **/
    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        stretchVideo();
        mediaPlayer.start();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayerSurface != null) {
            mediaPlayerSurface.release();
            mediaPlayerSurface = null;
        }
        if (mediaPlayerTexture != null) {
            mediaPlayerTexture.release();
            mediaPlayerTexture = null;
        }
    }

    /** SurfaceView **/
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mediaPlayerSurface = MediaPlayer.create(getApplicationContext(), Uri.parse(path), surfaceHolder);
        mediaPlayerSurface.prepareAsync();
        mediaPlayerSurface.setOnPreparedListener(this);

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
}
