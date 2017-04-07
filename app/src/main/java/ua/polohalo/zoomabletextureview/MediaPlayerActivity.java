package ua.polohalo.zoomabletextureview;

import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Surface;
import android.view.TextureView;

import java.io.IOException;

public class MediaPlayerActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener, MediaPlayer.OnPreparedListener {

    private MediaPlayer mediaPlayerTexture;

    String path = "http://download.blender.org/peach/bigbuckbunny_movies/BigBuckBunny_320x180.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_textureview);
        ZoomableTextureView textureView = (ZoomableTextureView) findViewById(R.id.textureView);
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

    /**
     * MediaPlayer
     **/
    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayerTexture != null) {
            mediaPlayerTexture.release();
            mediaPlayerTexture = null;
        }
    }
}
