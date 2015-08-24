package ua.polohalo.zoomabletextureview;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.TextureView;
import android.view.View;

/**
 * Created by mac on 8/24/15.
 */
public class ZoomableTextureView extends TextureView {

    Context context;




    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    static final int CLICK = 3;
    int mode = NONE;

    Matrix matrix = new Matrix();

    ScaleGestureDetector mScaleDetector;

    float minScale = 1f;
    float maxScale = 5f;
    float[] m;


    PointF last = new PointF();
    PointF start = new PointF();


    float redundantXSpace, redundantYSpace;
    float width, height;
    float saveScale = 1f;
    float right, bottom, origWidth, origHeight, bmWidth, bmHeight;


    public ZoomableTextureView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public ZoomableTextureView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public ZoomableTextureView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initView();
    }

    private void initView(){
        m = new float[9];
        setOnTouchListener(new ZoomOnTouchListeners());
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());

    }

    public void updateVideoDimens(int height, int width){
        this.height = height;
        this.width = width;
        this.bmHeight = height;
        this.bmWidth = width;
    }

    private class ZoomOnTouchListeners implements View.OnTouchListener {




        public ZoomOnTouchListeners(){
            super();
            m = new float[9];
            mScaleDetector= new ScaleGestureDetector(context, new ScaleListener());

        }



        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            mScaleDetector.onTouchEvent(motionEvent);

            matrix.getValues(m);
            float x = m[Matrix.MTRANS_X];
            float y = m[Matrix.MTRANS_Y];
            PointF curr = new PointF(motionEvent.getX(), motionEvent.getY());


            switch (motionEvent.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    last.set(motionEvent.getX(), motionEvent.getY());
                    start.set(last);
                    mode = DRAG;
                    break;
                case MotionEvent.ACTION_UP:
                    mode = NONE;
                    int xDiff = (int) Math.abs(curr.x - start.x);
                    int yDiff = (int) Math.abs(curr.y - start.y);
                    //if (xDiff < CLICK && yDiff < CLICK)//TODO click event?
                    // performClick();
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    last.set(motionEvent.getX(), motionEvent.getY());
                    start.set(last);
                    mode = ZOOM;
                    break;
                case MotionEvent.ACTION_MOVE:
                    //if the mode is ZOOM or
                    //if the mode is DRAG and already zoomed
                    if (mode == ZOOM || (mode == DRAG && saveScale > minScale))
                    {
                        float deltaX = curr.x - last.x;// x difference
                        float deltaY = curr.y - last.y;// y difference
                        float scaleWidth = Math.round(origWidth * saveScale);// width after applying current scale
                        float scaleHeight = Math.round(origHeight * saveScale);// height after applying current scale
                        //if scaleWidth is smaller than the views width
                        //in other words if the image width fits in the view
                        //limit left and right movement
                        if (scaleWidth < width)
                        {
                            deltaX = 0;
                            if (y + deltaY > 0)
                                deltaY = -y;
                            else if (y + deltaY < -bottom)
                                deltaY = -(y + bottom);
                        }
                        //if scaleHeight is smaller than the views height
                        //in other words if the image height fits in the view
                        //limit up and down movement
                        else if (scaleHeight < height)
                        {
                            deltaY = 0;
                            if (x + deltaX > 0)
                                deltaX = -x;
                            else if (x + deltaX < -right)
                                deltaX = -(x + right);
                        }
                        //if the image doesnt fit in the width or height
                        //limit both up and down and left and right
                        else
                        {
                            if (x + deltaX > 0)
                                deltaX = -x;
                            else if (x + deltaX < -right)
                                deltaX = -(x + right);

                            if (y + deltaY > 0)
                                deltaY = -y;
                            else if (y + deltaY < -bottom)
                                deltaY = -(y + bottom);
                        }
                        //move the image with the matrix
                        matrix.postTranslate(deltaX, deltaY);
                        //set the last touch location to the current
                        last.set(curr.x, curr.y);
                    }
                    break;

                case MotionEvent.ACTION_POINTER_UP:
                    mode = NONE;
                    break;
            }
            ZoomableTextureView.this.setTransform(matrix);
            ZoomableTextureView.this.invalidate();
            return true;
        }

        private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener
        {

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector)
            {
                mode = ZOOM;
                return true;
            }

            @Override
            public boolean onScale(ScaleGestureDetector detector)
            {
                float mScaleFactor = detector.getScaleFactor();
                float origScale = saveScale;
                saveScale *= mScaleFactor;
                if (saveScale > maxScale)
                {
                    saveScale = maxScale;
                    mScaleFactor = maxScale / origScale;
                }
                else if (saveScale < minScale)
                {
                    saveScale = minScale;
                    mScaleFactor = minScale / origScale;
                }
                right = width * saveScale - width - (2 * redundantXSpace * saveScale);
                bottom = height * saveScale - height - (2 * redundantYSpace * saveScale);
                if (origWidth * saveScale <= width || origHeight * saveScale <= height)
                {
                    matrix.postScale(mScaleFactor, mScaleFactor, width / 2, height / 2);
                    if (mScaleFactor < 1)
                    {
                        matrix.getValues(m);
                        float x = m[Matrix.MTRANS_X];
                        float y = m[Matrix.MTRANS_Y];
                        if (mScaleFactor < 1)
                        {
                            if (Math.round(origWidth * saveScale) < width)
                            {
                                if (y < -bottom)
                                    matrix.postTranslate(0, -(y + bottom));
                                else if (y > 0)
                                    matrix.postTranslate(0, -y);
                            }
                            else
                            {
                                if (x < -right)
                                    matrix.postTranslate(-(x + right), 0);
                                else if (x > 0)
                                    matrix.postTranslate(-x, 0);
                            }
                        }
                    }
                }
                else
                {
                    matrix.postScale(mScaleFactor, mScaleFactor, detector.getFocusX(), detector.getFocusY());
                    matrix.getValues(m);
                    float x = m[Matrix.MTRANS_X];
                    float y = m[Matrix.MTRANS_Y];
                    if (mScaleFactor < 1) {
                        if (x < -right)
                            matrix.postTranslate(-(x + right), 0);
                        else if (x > 0)
                            matrix.postTranslate(-x, 0);
                        if (y < -bottom)
                            matrix.postTranslate(0, -(y + bottom));
                        else if (y > 0)
                            matrix.postTranslate(0, -y);
                    }
                }
                return true;
            }
        }
    }



    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener
    {

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector)
        {
            mode = ZOOM;
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector)
        {

            float mScaleFactor = detector.getScaleFactor();
            float origScale = saveScale;
            saveScale *= mScaleFactor;
            if (saveScale > maxScale)
            {
                saveScale = maxScale;
                mScaleFactor = maxScale / origScale;
            }
            else if (saveScale < minScale)
            {
                saveScale = minScale;
                mScaleFactor = minScale / origScale;
            }
            right = width * saveScale - width - (2 * redundantXSpace * saveScale);
            bottom = height * saveScale - height - (2 * redundantYSpace * saveScale);
            if (origWidth * saveScale <= width || origHeight * saveScale <= height)
            {
                matrix.postScale(mScaleFactor, mScaleFactor, width / 2, height / 2);
                if (mScaleFactor < 1)
                {
                    matrix.getValues(m);
                    float x = m[Matrix.MTRANS_X];
                    float y = m[Matrix.MTRANS_Y];
                    if (mScaleFactor < 1)
                    {
                        if (Math.round(origWidth * saveScale) < width)
                        {
                            if (y < -bottom)
                                matrix.postTranslate(0, -(y + bottom));
                            else if (y > 0)
                                matrix.postTranslate(0, -y);
                        }
                        else
                        {
                            if (x < -right)
                                matrix.postTranslate(-(x + right), 0);
                            else if (x > 0)
                                matrix.postTranslate(-x, 0);
                        }
                    }
                }
            }
            else
            {
                matrix.postScale(mScaleFactor, mScaleFactor, detector.getFocusX(), detector.getFocusY());
                matrix.getValues(m);
                float x = m[Matrix.MTRANS_X];
                float y = m[Matrix.MTRANS_Y];
                if (mScaleFactor < 1) {
                    if (x < -right)
                        matrix.postTranslate(-(x + right), 0);
                    else if (x > 0)
                        matrix.postTranslate(-x, 0);
                    if (y < -bottom)
                        matrix.postTranslate(0, -(y + bottom));
                    else if (y > 0)
                        matrix.postTranslate(0, -y);
                }
            }
            return true;
        }
    }

    @Override
    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec)
    {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        //Fit to screen.
        float scale;
        float scaleX =  width / bmWidth;
        float scaleY = height / bmHeight;
        scale = Math.min(scaleX, scaleY);
        matrix.setScale(scale, scale);
        //setImageMatrix(matrix);
        setTransform(matrix);
        saveScale = 1f;

        // Center the image
        redundantYSpace = height - (scale * bmHeight) ;
        redundantXSpace = width - (scale * bmWidth);
        redundantYSpace /= 2;
        redundantXSpace /= 2;

        matrix.postTranslate(redundantXSpace, redundantYSpace);

        origWidth = width - 2 * redundantXSpace;
        origHeight = height - 2 * redundantYSpace;
        right = width * saveScale - width - (2 * redundantXSpace * saveScale);
        bottom = height * saveScale - height - (2 * redundantYSpace * saveScale);
        setTransform(matrix);
    }


}
