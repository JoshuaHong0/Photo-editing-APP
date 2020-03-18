package com.meitu.meitutietie;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region.Op;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ImageCropper extends View {
    private Bitmap mBitmap;
    private RectF mBitmapRect;
    private float mInitRectPercent = 1.0F;
    private Drawable mCropDrawable;
    private RectF mCropRect;
    private float mCropBorderWidth;
    private float mMinCropRectSideLength = UnitHelper.dipToPx(this.getContext(), 20.0F);
    private float mCropRatio = -1.0F;
    private float mCropRatioBase;
    private float mCropRatioForX;
    private float mCropRatioForY;
    private Drawable mCoverDrawable;
    private boolean mAdjustLeftEdge;
    private boolean mAdjustTopEdge;
    private boolean mAdjustRightEdge;
    private boolean mAdjustBottomEdge;
    private boolean mAdjustCenterPoint;
    private float mTouchDownX;
    private float mTouchDownY;
    private float mSavedCropLeft;
    private float mSavedCropTop;
    private float mSavedCropRight;
    private float mSavedCropBottom;
    private float mOriginalRatio;
    private float mViewRatio;
    private Paint mPaint = new Paint();
    private Rect mCropRectInt = new Rect();
    private Paint mGuidelinePaint = new Paint();
    private Paint mCornerPaint = new Paint();
    private Paint mBorderPaint = new Paint();
    private Paint mCropSizePaint = new Paint();
    private Context mContext;
    private int screenWidth;
    private int screenHeight;

    public ImageCropper(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
    }

    public ImageCropper(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public ImageCropper(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.setBitmapAndCropRect();

        int heightMeasure = (int) mBitmapRect.height();
        int widthMeasure = (int) mBitmapRect.width();

        if (heightMeasure!=0&&widthMeasure!=0){
            setMeasuredDimension(widthMeasure,heightMeasure);
        }

    }

    public void setScreenSize(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public RectF getmBitmapRect() {
        return mBitmapRect;
    }

    public float getInitCropRectPercent() {
        return this.mInitRectPercent;
    }

    public void setInitCropRectPercent(float percent) {
        this.mInitRectPercent = percent;
    }

    public float getOriginalRatio() {
        return mOriginalRatio;
    }

    public float getViewRatio() {
        return mViewRatio;
    }

    public void setBitmapAndCropRect() {

        if (this.mBitmap != null) {
            float targetScale = 1.0F;

            float viewWidth = (float) screenWidth - UnitHelper.dipToPx(mContext, 32.0F);
            float viewHeight = (float) screenHeight - UnitHelper.dipToPx(mContext, 105.0F);

            if (viewWidth / (float)this.mBitmap.getWidth() * (float)this.mBitmap.getHeight() <= viewHeight) {
                targetScale = viewWidth / (float)this.mBitmap.getWidth();
            }

            if (viewHeight / (float)this.mBitmap.getHeight() * (float)this.mBitmap.getWidth() <= viewWidth) {
                targetScale = viewHeight / (float)this.mBitmap.getHeight();
            }

            float scaledImageWidth = targetScale * (float)this.mBitmap.getWidth();
            float scaledImageHeight = targetScale * (float)this.mBitmap.getHeight();

            mViewRatio = viewWidth / viewHeight;
            mOriginalRatio = scaledImageWidth / scaledImageHeight;

            float left = (viewWidth - scaledImageWidth) / 2.0F;
            float top = (viewHeight - scaledImageHeight) / 2.0F;
            this.mBitmapRect = new RectF(left, 0, left + scaledImageWidth, scaledImageHeight);
            float cropWidth = scaledImageWidth * this.mInitRectPercent;
            float cropHeight = scaledImageHeight * this.mInitRectPercent;
            if (this.mCropRatio > 0.0F) {
                if (cropHeight > cropWidth / this.mCropRatio) {
                    cropHeight = cropWidth / this.mCropRatio;
                }

                if (cropWidth > cropHeight * this.mCropRatio) {
                    cropWidth = cropHeight * this.mCropRatio;
                }
            }

            left = left + (scaledImageWidth - cropWidth) / 2.0F;
            top = (scaledImageHeight - cropHeight) / 2.0F;

            this.mCropRect = new RectF(left, top, left + cropWidth, top + cropHeight);
            this.invalidate();
        }
    }

    public Bitmap getBitmap() {
        return this.mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.mBitmap = bitmap;
        this.setBitmapAndCropRect();
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.setBitmapAndCropRect();
    }

    public Drawable getCropDrawable() {
        return this.mCropDrawable;
    }

    public void setCropDrawable(Drawable drawable) {
        this.mCropDrawable = drawable;
    }

    public void setCropDrawable(int resourceId) {
        this.mCropDrawable = this.getContext().getResources().getDrawable(resourceId);
    }

    public float getCropBorderWidth() {
        return this.mCropBorderWidth;
    }

    public void setCropBorderWidth(float width) {
        this.mCropBorderWidth = width;
    }

    public float getMinCropDrawableRectSideLength() {
        return this.mMinCropRectSideLength;
    }

    public void setMinCropDrawableRectSideLength(float minCropDrawableRectSideLength) {
        this.mMinCropRectSideLength = minCropDrawableRectSideLength;
    }

    public float getCropRatio() {
        return this.mCropRatio;
    }

    /**
     * 设置剪裁框固定比例
     * @param ratio
     */
    public void setCropRatio(float ratio) {
        this.mCropRatio = ratio;
        this.mCropRatioBase = (float)Math.sqrt((double)(this.mCropRatio * this.mCropRatio + 1.0F));
        this.mCropRatioForX = this.mCropRatio / this.mCropRatioBase;
        this.mCropRatioForY = 1.0F / this.mCropRatioBase;
        this.setBitmapAndCropRect();
    }

    public Drawable getCoverDrawable() {
        return this.mCoverDrawable;
    }

    /**
     * 设置选择框以外阴影部分颜色
     * @param drawable
     */
    public void setCoverDrawable(Drawable drawable) {
        this.mCoverDrawable = drawable;
        if (!(this.mCoverDrawable instanceof ColorDrawable) && VERSION.SDK_INT >= 11) {
            this.setLayerType(1, this.mPaint);
        }

    }

    private void saveTouchDownStates(MotionEvent event) {
        this.mTouchDownX = event.getX();
        this.mTouchDownY = event.getY();
        this.mSavedCropLeft = this.mCropRect.left;
        this.mSavedCropTop = this.mCropRect.top;
        this.mSavedCropRight = this.mCropRect.right;
        this.mSavedCropBottom = this.mCropRect.bottom;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (this.mBitmap != null && this.mCropDrawable != null) {
            switch(event.getAction()) {
                case 0:
                    this.saveTouchDownStates(event);
                    this.mAdjustLeftEdge = this.mTouchDownX <= this.mCropRect.left + this.mCropBorderWidth;
                    this.mAdjustTopEdge = this.mTouchDownY <= this.mCropRect.top + this.mCropBorderWidth;
                    this.mAdjustRightEdge = this.mTouchDownX >= this.mCropRect.right - this.mCropBorderWidth;
                    this.mAdjustBottomEdge = this.mTouchDownY >= this.mCropRect.bottom - this.mCropBorderWidth;
                    this.mAdjustCenterPoint = !this.mAdjustLeftEdge && !this.mAdjustTopEdge && !this.mAdjustRightEdge && !this.mAdjustBottomEdge;
                    if (this.mCropRatio > 0.0F) {
                        this.mAdjustLeftEdge = this.mTouchDownX < this.mCropRect.centerX();
                        this.mAdjustTopEdge = this.mTouchDownY < this.mCropRect.centerY();
                        this.mAdjustRightEdge = this.mTouchDownX > this.mCropRect.centerX();
                        this.mAdjustBottomEdge = this.mTouchDownY > this.mCropRect.centerY();
                    }
                    break;
                case 2:
                    float dx = event.getX() - this.mTouchDownX;
                    float dy = event.getY() - this.mTouchDownY;
                    float targetCropBottom;
                    float dst2;
                    float distanceChanged;
                    float xChange;
                    float yChange;
                    float diffY;
                    if (this.mAdjustCenterPoint) {
                        targetCropBottom = this.mSavedCropLeft + dx;
                        dst2 = this.mSavedCropTop + dy;
                        distanceChanged = this.mSavedCropRight + dx;
                        xChange = this.mSavedCropBottom + dy;
                        yChange = this.mSavedCropRight - this.mSavedCropLeft;
                        diffY = this.mSavedCropBottom - this.mSavedCropTop;
                        if (targetCropBottom < this.mBitmapRect.left) {
                            targetCropBottom = this.mBitmapRect.left;
                            distanceChanged = this.mBitmapRect.left + yChange;
                        }

                        if (dst2 < this.mBitmapRect.top) {
                            dst2 = this.mBitmapRect.top;
                            xChange = this.mBitmapRect.top + diffY;
                        }

                        if (distanceChanged > this.mBitmapRect.right) {
                            distanceChanged = this.mBitmapRect.right;
                            targetCropBottom = this.mBitmapRect.right - yChange;
                        }

                        if (xChange > this.mBitmapRect.bottom) {
                            xChange = this.mBitmapRect.bottom;
                            dst2 = this.mBitmapRect.bottom - diffY;
                        }

                        this.mCropRect.left = targetCropBottom;
                        this.mCropRect.top = dst2;
                        this.mCropRect.right = distanceChanged;
                        this.mCropRect.bottom = xChange;
                    } else if (this.mCropRatio > 0.0F) {
                        float targetCropRight;
                        boolean isBottomTooBig;
                        boolean isRightTooBig;
                        boolean isBottomTooSmall;
                        boolean isRightTooSmall;
                        if (this.mAdjustLeftEdge && this.mAdjustTopEdge) {
                            targetCropBottom = (float)MathHelper.distance((double)this.mTouchDownX, (double)this.mTouchDownY, (double)this.getWidth(), (double)this.getHeight());
                            dst2 = (float)MathHelper.distance((double)event.getX(), (double)event.getY(), (double)this.getWidth(), (double)this.getHeight());
                            distanceChanged = dst2 - targetCropBottom;
                            xChange = distanceChanged * this.mCropRatioForX;
                            yChange = distanceChanged * this.mCropRatioForY;
                            targetCropBottom = this.mSavedCropTop - yChange;
                            targetCropRight = this.mSavedCropLeft - xChange;
                            isBottomTooBig = targetCropBottom < this.mBitmapRect.top;
                            if (isBottomTooBig) {
                                targetCropBottom = this.mBitmapRect.top;
                                targetCropRight = this.mSavedCropRight - (this.mSavedCropBottom - targetCropBottom) * this.mCropRatio;
                                this.saveTouchDownStates(event);
                            }

                            isRightTooBig = targetCropRight < this.mBitmapRect.left;
                            if (isRightTooBig) {
                                targetCropRight = this.mBitmapRect.left;
                                targetCropBottom = this.mSavedCropBottom - (this.mSavedCropRight - targetCropRight) / this.mCropRatio;
                                this.saveTouchDownStates(event);
                            }

                            isBottomTooSmall = targetCropBottom > this.mSavedCropBottom - this.mMinCropRectSideLength;
                            if (isBottomTooSmall) {
                                targetCropBottom = this.mSavedCropBottom - this.mMinCropRectSideLength;
                                targetCropRight = this.mSavedCropRight - (this.mSavedCropBottom - targetCropBottom) * this.mCropRatio;
                                this.saveTouchDownStates(event);
                            }

                            isRightTooSmall = targetCropRight > this.mSavedCropRight - this.mMinCropRectSideLength;
                            if (isRightTooSmall) {
                                targetCropRight = this.mSavedCropRight - this.mMinCropRectSideLength;
                                targetCropBottom = this.mSavedCropBottom - (this.mSavedCropRight - targetCropRight) / this.mCropRatio;
                                this.saveTouchDownStates(event);
                            }

                            this.mCropRect.top = targetCropBottom;
                            this.mCropRect.left = targetCropRight;
                        } else if (this.mAdjustLeftEdge && this.mAdjustBottomEdge) {
                            targetCropBottom = (float)MathHelper.distance((double)this.mTouchDownX, (double)this.mTouchDownY, (double)this.getWidth(), 0.0D);
                            dst2 = (float)MathHelper.distance((double)event.getX(), (double)event.getY(), (double)this.getWidth(), 0.0D);
                            distanceChanged = dst2 - targetCropBottom;
                            xChange = distanceChanged * this.mCropRatioForX;
                            yChange = distanceChanged * this.mCropRatioForY;
                            targetCropBottom = this.mSavedCropBottom + yChange;
                            targetCropRight = this.mSavedCropLeft - xChange;
                            isBottomTooBig = targetCropBottom > this.mBitmapRect.bottom;
                            if (isBottomTooBig) {
                                targetCropBottom = this.mBitmapRect.bottom;
                                targetCropRight = this.mSavedCropRight - (targetCropBottom - this.mSavedCropTop) * this.mCropRatio;
                                this.saveTouchDownStates(event);
                            }

                            isRightTooBig = targetCropRight < this.mBitmapRect.left;
                            if (isRightTooBig) {
                                targetCropRight = this.mBitmapRect.left;
                                targetCropBottom = this.mSavedCropTop + (this.mSavedCropRight - targetCropRight) / this.mCropRatio;
                                this.saveTouchDownStates(event);
                            }

                            isBottomTooSmall = targetCropBottom < this.mSavedCropTop + this.mMinCropRectSideLength;
                            if (isBottomTooSmall) {
                                targetCropBottom = this.mSavedCropTop + this.mMinCropRectSideLength;
                                targetCropRight = this.mSavedCropRight - (targetCropBottom - this.mSavedCropTop) * this.mCropRatio;
                                this.saveTouchDownStates(event);
                            }

                            isRightTooSmall = targetCropRight > this.mSavedCropRight - this.mMinCropRectSideLength;
                            if (isRightTooSmall) {
                                targetCropRight = this.mSavedCropRight - this.mMinCropRectSideLength;
                                targetCropBottom = this.mSavedCropTop + (this.mSavedCropRight - targetCropRight) / this.mCropRatio;
                                this.saveTouchDownStates(event);
                            }

                            this.mCropRect.bottom = targetCropBottom;
                            this.mCropRect.left = targetCropRight;
                        } else if (this.mAdjustRightEdge && this.mAdjustTopEdge) {
                            targetCropBottom = (float)MathHelper.distance((double)this.mTouchDownX, (double)this.mTouchDownY, 0.0D, (double)this.getHeight());
                            dst2 = (float)MathHelper.distance((double)event.getX(), (double)event.getY(), 0.0D, (double)this.getHeight());
                            distanceChanged = dst2 - targetCropBottom;
                            xChange = distanceChanged * this.mCropRatioForX;
                            yChange = distanceChanged * this.mCropRatioForY;
                            targetCropBottom = this.mSavedCropTop - yChange;
                            targetCropRight = this.mSavedCropRight + xChange;
                            isBottomTooBig = targetCropBottom < this.mBitmapRect.top;
                            if (isBottomTooBig) {
                                targetCropBottom = this.mBitmapRect.top;
                                targetCropRight = this.mSavedCropLeft + (this.mSavedCropBottom - targetCropBottom) * this.mCropRatio;
                                this.saveTouchDownStates(event);
                            }

                            isRightTooBig = targetCropRight > this.mBitmapRect.right;
                            if (isRightTooBig) {
                                targetCropRight = this.mBitmapRect.right;
                                targetCropBottom = this.mSavedCropBottom - (targetCropRight - this.mSavedCropLeft) / this.mCropRatio;
                                this.saveTouchDownStates(event);
                            }

                            isBottomTooSmall = targetCropBottom > this.mSavedCropBottom - this.mMinCropRectSideLength;
                            if (isBottomTooSmall) {
                                targetCropBottom = this.mSavedCropBottom - this.mMinCropRectSideLength;
                                targetCropRight = this.mSavedCropLeft + (this.mSavedCropBottom - targetCropBottom) * this.mCropRatio;
                                this.saveTouchDownStates(event);
                            }

                            isRightTooSmall = targetCropRight < this.mSavedCropLeft + this.mMinCropRectSideLength;
                            if (isRightTooSmall) {
                                targetCropRight = this.mSavedCropLeft + this.mMinCropRectSideLength;
                                targetCropBottom = this.mSavedCropBottom - (targetCropRight - this.mSavedCropLeft) / this.mCropRatio;
                                this.saveTouchDownStates(event);
                            }

                            this.mCropRect.top = targetCropBottom;
                            this.mCropRect.right = targetCropRight;
                        } else if (this.mAdjustRightEdge && this.mAdjustBottomEdge) {
                            targetCropBottom = (float)MathHelper.distance((double)this.mTouchDownX, (double)this.mTouchDownY, 0.0D, 0.0D);
                            dst2 = (float)MathHelper.distance((double)event.getX(), (double)event.getY(), 0.0D, 0.0D);
                            distanceChanged = dst2 - targetCropBottom;
                            xChange = distanceChanged * this.mCropRatioForX;
                            yChange = distanceChanged * this.mCropRatioForY;
                            targetCropBottom = this.mSavedCropBottom + yChange;
                            targetCropRight = this.mSavedCropRight + xChange;
                            isBottomTooBig = targetCropBottom > this.mBitmapRect.bottom;
                            if (isBottomTooBig) {
                                targetCropBottom = this.mBitmapRect.bottom;
                                targetCropRight = this.mSavedCropLeft + (targetCropBottom - this.mSavedCropTop) * this.mCropRatio;
                                this.saveTouchDownStates(event);
                            }

                            isRightTooBig = targetCropRight > this.mBitmapRect.right;
                            if (isRightTooBig) {
                                targetCropRight = this.mBitmapRect.right;
                                targetCropBottom = this.mSavedCropTop + (targetCropRight - this.mSavedCropLeft) / this.mCropRatio;
                                this.saveTouchDownStates(event);
                            }

                            isBottomTooSmall = targetCropBottom < this.mSavedCropTop + this.mMinCropRectSideLength;
                            if (isBottomTooSmall) {
                                targetCropBottom = this.mSavedCropTop + this.mMinCropRectSideLength;
                                targetCropRight = this.mSavedCropLeft + (targetCropBottom - this.mSavedCropTop) * this.mCropRatio;
                                this.saveTouchDownStates(event);
                            }

                            isRightTooSmall = targetCropRight < this.mSavedCropLeft + this.mMinCropRectSideLength;
                            if (isRightTooSmall) {
                                targetCropRight = this.mSavedCropLeft + this.mMinCropRectSideLength;
                                targetCropBottom = this.mSavedCropTop + (targetCropRight - this.mSavedCropLeft) / this.mCropRatio;
                                this.saveTouchDownStates(event);
                            }

                            this.mCropRect.bottom = targetCropBottom;
                            this.mCropRect.right = targetCropRight;
                        }
                    } else {
                        if (this.mAdjustLeftEdge) {
                            targetCropBottom = this.mSavedCropLeft + dx;
                            if (targetCropBottom > this.mSavedCropRight - this.mMinCropRectSideLength) {
                                targetCropBottom = this.mSavedCropRight - this.mMinCropRectSideLength;
                                this.saveTouchDownStates(event);
                            }

                            if (targetCropBottom < this.mBitmapRect.left) {
                                targetCropBottom = this.mBitmapRect.left;
                                this.saveTouchDownStates(event);
                            }

                            this.mCropRect.left = targetCropBottom;
                        }

                        if (this.mAdjustTopEdge) {
                            targetCropBottom = this.mSavedCropTop + dy;
                            if (targetCropBottom > this.mSavedCropBottom - this.mMinCropRectSideLength) {
                                targetCropBottom = this.mSavedCropBottom - this.mMinCropRectSideLength;
                                this.saveTouchDownStates(event);
                            }

                            if (targetCropBottom < this.mBitmapRect.top) {
                                targetCropBottom = this.mBitmapRect.top;
                                this.saveTouchDownStates(event);
                            }

                            this.mCropRect.top = targetCropBottom;
                        }

                        if (this.mAdjustRightEdge) {
                            targetCropBottom = this.mSavedCropRight + dx;
                            if (targetCropBottom < this.mSavedCropLeft + this.mMinCropRectSideLength) {
                                targetCropBottom = this.mSavedCropLeft + this.mMinCropRectSideLength;
                                this.saveTouchDownStates(event);
                            }

                            if (targetCropBottom > this.mBitmapRect.right) {
                                targetCropBottom = this.mBitmapRect.right;
                                this.saveTouchDownStates(event);
                            }

                            this.mCropRect.right = targetCropBottom;
                        }

                        if (this.mAdjustBottomEdge) {
                            targetCropBottom = this.mSavedCropBottom + dy;
                            if (targetCropBottom < this.mSavedCropTop + this.mMinCropRectSideLength) {
                                targetCropBottom = this.mSavedCropTop + this.mMinCropRectSideLength;
                                this.saveTouchDownStates(event);
                            }

                            if (targetCropBottom > this.mBitmapRect.bottom) {
                                targetCropBottom = this.mBitmapRect.bottom;
                                this.saveTouchDownStates(event);
                            }

                            this.mCropRect.bottom = targetCropBottom;
                        }
                    }

                    this.setCropDrawableRect(true);
            }

            return true;
        } else {
            return false;
        }
    }

    public void setMultiSamplingEnabled(boolean enabled) {
        this.mPaint.setFilterBitmap(enabled);
    }

    private void setCropDrawableRect(boolean invalidate) {
        this.mCropRectInt.left = (int)(this.mCropRect.left + 0.5F);
        this.mCropRectInt.top = (int)(this.mCropRect.top + 0.5F);
        this.mCropRectInt.right = (int)(this.mCropRect.right + 0.5F);
        this.mCropRectInt.bottom = (int)(this.mCropRect.bottom + 0.5F);
        this.mCropDrawable.setBounds(this.mCropRectInt);
        if (invalidate) {
            this.invalidate();
        }

    }

    protected void onDraw(Canvas canvas) {
        if (this.mBitmap != null && this.mCropDrawable != null) {
            canvas.drawBitmap(this.mBitmap, null, this.mBitmapRect, this.mPaint);
            this.setCropDrawableRect(false);
            if (this.mCoverDrawable != null) {
                if (this.mCoverDrawable instanceof ColorDrawable) {
                    this.mCoverDrawable.setBounds(0, 0, this.getWidth(), this.mCropRectInt.top);
                    this.mCoverDrawable.draw(canvas);
                    this.mCoverDrawable.setBounds(0, this.mCropRectInt.top, this.mCropRectInt.left, this.mCropRectInt.bottom);
                    this.mCoverDrawable.draw(canvas);
                    this.mCoverDrawable.setBounds(this.mCropRectInt.right, this.mCropRectInt.top, this.getWidth(), this.mCropRectInt.bottom);
                    this.mCoverDrawable.draw(canvas);
                    this.mCoverDrawable.setBounds(0, this.mCropRectInt.bottom, this.getWidth(), this.getHeight());
                    this.mCoverDrawable.draw(canvas);
                    drawCropSize(canvas);
                    drawBorder(canvas);
                    drawGuideLines(canvas);
                    drawCorner(canvas);
                } else {
                    canvas.save();
                    canvas.clipRect(this.mCropRectInt, Op.DIFFERENCE);
                    this.mCoverDrawable.setBounds(0, 0, this.getWidth(), this.getHeight());
                    this.mCoverDrawable.draw(canvas);
                    canvas.restore();
                }
            }

            this.mCropDrawable.draw(canvas);
        }
    }

    /**
     * 绘制剪裁框中央大小文字
     * @param canvas
     */
    public void drawCropSize (Canvas canvas) {
        final float left = mCropRect.left;
        final float right = mCropRect.right;
        final float top = mCropRect.top;
        final float bottom = mCropRect.bottom;

        mCropSizePaint.setColor(Color.WHITE);
        mCropSizePaint.setStyle(Paint.Style.FILL);
        mCropSizePaint.setTextSize(30);
        mCropSizePaint.setTextAlign(Paint.Align.CENTER);
        final int x = (int)(right - left);
        final int y = (int)(bottom - top);

        Paint.FontMetrics fontMetrics = mCropSizePaint.getFontMetrics();
        float distance = (fontMetrics.bottom - fontMetrics.top)/2 - fontMetrics.bottom;
        float baseline = mCropRect.centerY() + distance;

        canvas.drawText(x+"x"+y,mCropRect.centerX(),baseline, mCropSizePaint);

    }

    /**
     * 绘制剪裁框边界
     * @param canvas
     */
    public void drawBorder (Canvas canvas) {

        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(UnitHelper.dipToPx(mContext, 2));

        mBorderPaint.setColor(Color.WHITE);
        mBorderPaint.setAlpha(200);

        final float left = mCropRect.left;
        final float right = mCropRect.right;
        final float top = mCropRect.top;
        final float bottom = mCropRect.bottom;

        canvas.drawRect(left,top,right,bottom,mBorderPaint);
    }

    /**
     * 绘制剪裁框网格
     * @param canvas
     */
    public void drawGuideLines (Canvas canvas) {

        /**
         * 设置网格线样式
         */
        mGuidelinePaint.setStyle(Paint.Style.STROKE);
        mGuidelinePaint.setStrokeWidth(UnitHelper.dipToPx(mContext, 1));
        mGuidelinePaint.setColor(Color.parseColor("#AAFFFFFF"));

        final float left = mCropRect.left;
        final float right = mCropRect.right;
        final float top = mCropRect.top;
        final float bottom = mCropRect.bottom;

        final float oneThirdCropWidth = mCropRect.width() / 3;
        final float x1 = left + oneThirdCropWidth;
        canvas.drawLine(x1,top,x1,bottom, mGuidelinePaint);
        final float x2 = right - oneThirdCropWidth;
        canvas.drawLine(x2,top,x2,bottom,mGuidelinePaint);


        final float oneThirdCropHeight = mCropRect.height() / 3;
        final float y1 = top + oneThirdCropHeight;
        canvas.drawLine(left,y1,right,y1,mGuidelinePaint);
        final float y2 = bottom - oneThirdCropHeight;
        canvas.drawLine(left,y2,right,y2,mGuidelinePaint);
    }

    /**
     * 绘制剪裁框四个角上的圆形图案
     * @param canvas
     */
    public void drawCorner (Canvas canvas) {

        /**
         * 设置剪裁框四个角上半透明圆的样式
         */
        mCornerPaint.setStyle(Paint.Style.FILL);
        mCornerPaint.setColor(Color.WHITE);
        mCornerPaint.setAlpha(200);
        final float left = mCropRect.left;
        final float right = mCropRect.right;
        final float top = mCropRect.top;
        final float bottom = mCropRect.bottom;

        canvas.drawCircle(left,top,30f,mCornerPaint);
        canvas.drawCircle(right,top,30f,mCornerPaint);
        canvas.drawCircle(left,bottom,30f,mCornerPaint);
        canvas.drawCircle(right,bottom,30f,mCornerPaint);

    }


    public RectF getCroppedRectF() {
        float x = (this.mCropRect.left - this.mBitmapRect.left) * (float)this.mBitmap.getWidth() / this.mBitmapRect.width();
        float y = (this.mCropRect.top - this.mBitmapRect.top) * (float)this.mBitmap.getHeight() / this.mBitmapRect.height();
        float w = this.mCropRect.width() * (float)this.mBitmap.getWidth() / this.mBitmapRect.width();
        float h = this.mCropRect.height() * (float)this.mBitmap.getHeight() / this.mBitmapRect.height();
        return new RectF(x, y, x + w, y + h);
    }

    public Rect getCroppedRect() {
        RectF rectf = this.getCroppedRectF();
        int l = (int)(rectf.left + 0.5F);
        int t = (int)(rectf.top + 0.5F);
        int r = (int)(rectf.right + 0.5F);
        int b = (int)(rectf.bottom + 0.5F);
        return new Rect(l, t, r, b);
    }

    public Bitmap getCroppedBitmap() {
        Rect rect = this.getCroppedRect();
        int left = rect.left;
        int top = rect.top;
        int width = rect.width();
        int height = rect.height();
        return Bitmap.createBitmap(this.mBitmap, left, top, width, height);
    }
}
