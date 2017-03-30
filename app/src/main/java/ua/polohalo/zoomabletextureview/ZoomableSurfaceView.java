package ua.polohalo.zoomabletextureview;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.SurfaceView;


public class ZoomableSurfaceView extends SurfaceView {
    public ZoomableSurfaceView(Context context) {
        super(context);
    }

    public ZoomableSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ZoomableSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ZoomableSurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
