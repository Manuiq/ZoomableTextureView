package ua.polohalo.zoomabletextureview;

import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.TextureView;
import android.widget.RelativeLayout;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener, MediaPlayer.OnPreparedListener {

    private ZoomableTextureView textureView;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textureView = (ZoomableTextureView) findViewById(R.id.textureView);
        textureView.setSurfaceTextureListener(this);

    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        prepareTextureView(surfaceTexture);

    }

    private void prepareTextureView(SurfaceTexture surface) {
        Surface s = new Surface(surface);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setSurface(s);
        String path = "http://download.blender.org/peach/bigbuckbunny_movies/BigBuckBunny_320x180.mp4";
        try {
            mediaPlayer.setDataSource(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.prepareAsync();
        mediaPlayer.setOnPreparedListener(this);
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

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        stretchVideo();
        mediaPlayer.start();

    }
    private void stretchVideo(){
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int maxHeight = metrics.heightPixels;
        int maxWidth = metrics.widthPixels;
        textureView.setLayoutParams(new RelativeLayout.LayoutParams(maxWidth, maxHeight));
        textureView.updateVideoDimens(maxHeight, maxWidth);
    }
}
