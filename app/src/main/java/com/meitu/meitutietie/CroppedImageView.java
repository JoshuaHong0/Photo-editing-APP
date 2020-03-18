package com.meitu.meitutietie;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class CroppedImageView extends View {

    private Paint paintEdge = new Paint();
    private Matrix matrix = new Matrix();
    private Matrix downMatrix = new Matrix();
    private Matrix moveMatrix = new Matrix();
    private Bitmap srcImage;
    private PointF midPoint = new PointF();

    private int mode;
    private static final int NONE = 0;
    private static final int TRANS = 1;
    private static final int ZOOM = 2;

    private boolean withinBorder;

    public CroppedImageView(Context context) {
        super(context);
    }

    public CroppedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CroppedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setSrcImage(Bitmap srcImage) {
        this.srcImage = srcImage;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float[] points = getBitmapPoints(srcImage, matrix);
        float x1 = points[0];
        float y1 = points[1];
        float x2 = points[2];
        float y2 = points[3];
        float x3 = points[4];
        float y3 = points[5];
        float x4 = points[6];
        float y4 = points[7];
        // 画边框
        canvas.drawLine(x1, y1, x2, y2, paintEdge);
        canvas.drawLine(x2, y2, x4, y4, paintEdge);
        canvas.drawLine(x4, y4, x3, y3, paintEdge);
        canvas.drawLine(x3, y3, x1, y1, paintEdge);
        // 画图片
        canvas.drawBitmap(srcImage, matrix, null);
    }

    private float downX;
    private float downY;
    private float oldDistanct;
    private float oldRotation;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mode = TRANS;
                downX = event.getX();
                downY = event.getY();
                downMatrix.set(matrix);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.d("Hzz","Two fingers");
                mode = ZOOM;
                oldDistanct = getSpaceDistance(event);
                oldRotation = getSpaceRotation(event);
                downMatrix.set(matrix);
                midPoint = getMidPoint(event);
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == ZOOM) {
                    moveMatrix.set(downMatrix);
                    float deltaRotation = getSpaceRotation(event) - oldRotation;
                    float scale = getSpaceDistance(event) / oldDistanct;
                    moveMatrix.postScale(scale, scale, midPoint.x, midPoint.y);
                    moveMatrix.postRotate(deltaRotation, midPoint.x, midPoint.y);
                    matrix.set(moveMatrix);
                    invalidate();
                } else if (mode == TRANS) {
                    moveMatrix.set(downMatrix);
                    moveMatrix.postTranslate(event.getX() - downX, event.getY() - downY);
                    matrix.set(moveMatrix);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 获取手指的旋转角度
     *
     * @param event
     * @return
     */
    private float getSpaceRotation(MotionEvent event) {
        double deltaX = event.getX(0) - event.getX(1);
        double deltaY = event.getY(0) - event.getY(1);
        double radians = Math.atan2(deltaY, deltaX);
        return (float) Math.toDegrees(radians);
    }

    /**
     * 获取手指间的距离
     *
     * @param event
     * @return
     */
    private float getSpaceDistance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * 获取手势中心点
     *
     * @param event
     */
    private PointF getMidPoint(MotionEvent event) {
        PointF point = new PointF();
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
        return point;
    }

    /**
     * 将matrix的点映射成坐标点
     *
     * @return
     */
    protected float[] getBitmapPoints(Bitmap bitmap, Matrix matrix) {
        float[] dst = new float[8];
        float[] src = new float[]{
                0, 0,
                bitmap.getWidth(), 0,
                0, bitmap.getHeight(),
                bitmap.getWidth(), bitmap.getHeight()
        };
        matrix.mapPoints(dst, src);
        return dst;
    }

}
