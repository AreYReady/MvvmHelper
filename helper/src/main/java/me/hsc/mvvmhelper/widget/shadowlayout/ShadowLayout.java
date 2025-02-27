package me.hsc.mvvmhelper.widget.shadowlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import me.hsc.mvvmhelper.R;


/**
 * 作者　: hegaojian
 * 时间　: 2022/8/19
 * 描述　:
 */
public class ShadowLayout extends FrameLayout {
    private Drawable clickAbleFalseDrawable;
    private int clickAbleFalseColor = -101;

    private Drawable layoutBackground;
    private Drawable layoutBackground_true;
    private View firstView;

    private int mBackGroundColor;
    private int mBackGroundColor_true = -101;
    private int mShadowColor;
    private float mShadowLimit;
    private float mCornerRadius;
    private float mDx;
    private float mDy;
    private boolean leftShow;
    private boolean rightShow;
    private boolean topShow;
    private boolean bottomShow;
    private Paint shadowPaint;
    private Paint paint;

    private int leftPadding;
    private int topPadding;
    private int rightPadding;
    private int bottomPadding;
    //阴影布局子空间区域
    private RectF rectf = new RectF();

    //ShadowLayout的样式，是只需要pressed还是selected。默认是pressed.
    private int selectorType = 1;
    private boolean isShowShadow = true;
    private boolean isSym;

    //增加各个圆角的属性
    private float mCornerRadius_leftTop;
    private float mCornerRadius_rightTop;
    private float mCornerRadius_leftBottom;
    private float mCornerRadius_rightBottom;

    //边框画笔
    private Paint paint_stroke;
    private float stroke_with;
    private int stroke_color;
    private int stroke_color_true;

    private boolean isClickable;

    public ShadowLayout(Context context) {
        this(context, null);
    }

    public ShadowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public ShadowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }


    public void setClickable(boolean clickable) {
        super.setClickable(clickable);
        this.isClickable = clickable;
        changeSwitchClickable();
    }


    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        if (isClickable) {
            super.setOnClickListener(l);
        }
    }

    public void changeSwitchClickable() {
        //不可点击的状态只在press mode的模式下生效
        if (selectorType == 1 && firstView != null) {

            //press mode
            if (!isClickable) {
                //不可点击的状态。
                if (clickAbleFalseColor != -101) {
                    //说明设置了颜色
                    if (layoutBackground != null) {
                        //说明此时是设置了图片的模式
                        firstView.getBackground().setAlpha(0);
                    }
                    paint.setColor(clickAbleFalseColor);
                    postInvalidate();


                } else if (clickAbleFalseDrawable != null) {
                    //说明设置了背景图
                    setmBackGround(clickAbleFalseDrawable);
                    paint.setColor(Color.parseColor("#00000000"));
                    postInvalidate();
                }
            } else {
                //可点击的状态
                if (layoutBackground != null) {
                    setmBackGround(layoutBackground);
                } else {
                    if (firstView.getBackground() != null) {
                        firstView.getBackground().setAlpha(0);
                    }
                }
                paint.setColor(mBackGroundColor);
                postInvalidate();
            }
        }
    }

    //增加selector样式
    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (selectorType == 2) {
            if (selected) {
                if (mBackGroundColor_true != -101) {
                    paint.setColor(mBackGroundColor_true);
                }
                if (stroke_color_true != -101) {
                    paint_stroke.setColor(stroke_color_true);
                }
                if (layoutBackground_true != null) {
                    setmBackGround(layoutBackground_true);
                }
            } else {
                paint.setColor(mBackGroundColor);

                if (stroke_color != -101) {
                    paint_stroke.setColor(stroke_color);
                }

                if (layoutBackground != null) {
                    setmBackGround(layoutBackground);
                }

            }
            postInvalidate();
        }
    }


    public void setShowShadow(boolean isShowShadow) {
        this.isShowShadow = isShowShadow;
        if (getWidth() != 0 && getHeight() != 0) {
            setBackgroundCompat(getWidth(), getHeight());
        }
    }

    //动态设置x轴偏移量
    public void setMDx(float mDx) {
        if (Math.abs(mDx) > mShadowLimit) {
            if (mDx > 0) {
                this.mDx = mShadowLimit;
            } else {
                this.mDx = -mShadowLimit;
            }
        } else {
            this.mDx = mDx;
        }
        setPading();
    }

    //动态设置y轴偏移量
    public void setMDy(float mDy) {
        if (Math.abs(mDy) > mShadowLimit) {
            if (mDy > 0) {
                this.mDy = mShadowLimit;
            } else {
                this.mDy = -mShadowLimit;
            }
        } else {
            this.mDy = mDy;
        }
        setPading();
    }


    public float getmCornerRadius() {
        return mCornerRadius;
    }

    //动态设置 圆角属性
    public void setmCornerRadius(int mCornerRadius) {
        this.mCornerRadius = mCornerRadius;
        if (getWidth() != 0 && getHeight() != 0) {
            setBackgroundCompat(getWidth(), getHeight());
        }
    }

    public float getmShadowLimit() {
        return mShadowLimit;
    }

    //动态设置阴影扩散区域
    public void setmShadowLimit(int mShadowLimit) {
        this.mShadowLimit = mShadowLimit;
        setPading();
    }

    //动态设置阴影颜色值
    public void setmShadowColor(int mShadowColor) {
        this.mShadowColor = mShadowColor;
        if (getWidth() != 0 && getHeight() != 0) {
            setBackgroundCompat(getWidth(), getHeight());
        }
    }


    public void setLeftShow(boolean leftShow) {
        this.leftShow = leftShow;
        setPading();
    }

    public void setRightShow(boolean rightShow) {
        this.rightShow = rightShow;
        setPading();
    }

    public void setTopShow(boolean topShow) {
        this.topShow = topShow;
        setPading();
    }

    public void setBottomShow(boolean bottomShow) {
        this.bottomShow = bottomShow;
        setPading();
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        firstView = getChildAt(0);
        if (firstView == null) {
            firstView = ShadowLayout.this;
            //当子View都没有的时候。默认不使用阴影
            isShowShadow = false;
        }

        if (firstView != null) {

            //selector样式不受clickable的影响

            if (selectorType == 2) {
                //如果是selector的模式下
                if (this.isSelected()) {
                    //这个方法内已经判断了是否为空
                    setmBackGround(layoutBackground_true);
                } else {
                    setmBackGround(layoutBackground);
                }
            } else {
                if (isClickable) {
                    setmBackGround(layoutBackground);
                } else {
                    setmBackGround(clickAbleFalseDrawable);
                    if (clickAbleFalseColor != -101) {
                        paint.setColor(clickAbleFalseColor);
                    }
                }
            }

        }

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w > 0 && h > 0) {
            setBackgroundCompat(w, h);
        }
    }

    private void initView(Context context, AttributeSet attrs) {
        initAttributes(attrs);
        shadowPaint = new Paint();
        shadowPaint.setAntiAlias(true);
        shadowPaint.setStyle(Paint.Style.FILL);


        paint_stroke = new Paint();
        paint_stroke.setAntiAlias(true);
        paint_stroke.setStyle(Paint.Style.STROKE);
        paint_stroke.setStrokeWidth(stroke_with);
        if (stroke_color != -101) {
            paint_stroke.setColor(stroke_color);
        }


        //矩形画笔
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(mBackGroundColor);

        setPading();
    }


    public int dip2px(float dipValue) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public void setPading() {
        if (isShowShadow && mShadowLimit > 0) {
            //控件区域是否对称，默认是对称。不对称的话，那么控件区域随着阴影区域走
            if (isSym) {
                int xPadding = (int) (mShadowLimit + Math.abs(mDx));
                int yPadding = (int) (mShadowLimit + Math.abs(mDy));

                if (leftShow) {
                    leftPadding = xPadding;
                } else {
                    leftPadding = 0;
                }

                if (topShow) {
                    topPadding = yPadding;
                } else {
                    topPadding = 0;
                }


                if (rightShow) {
                    rightPadding = xPadding;
                } else {
                    rightPadding = 0;
                }

                if (bottomShow) {
                    bottomPadding = yPadding;
                } else {
                    bottomPadding = 0;
                }
            } else {
                if (Math.abs(mDy) > mShadowLimit) {
                    if (mDy > 0) {
                        mDy = mShadowLimit;
                    } else {
                        mDy = 0 - mShadowLimit;
                    }
                }


                if (Math.abs(mDx) > mShadowLimit) {
                    if (mDx > 0) {
                        mDx = mShadowLimit;
                    } else {
                        mDx = 0 - mShadowLimit;
                    }
                }

                if (topShow) {
                    topPadding = (int) (mShadowLimit - mDy);
                } else {
                    topPadding = 0;
                }

                if (bottomShow) {
                    bottomPadding = (int) (mShadowLimit + mDy);
                } else {
                    bottomPadding = 0;
                }


                if (rightShow) {
                    rightPadding = (int) (mShadowLimit - mDx);
                } else {
                    rightPadding = 0;
                }


                if (leftShow) {
                    leftPadding = (int) (mShadowLimit + mDx);
                } else {
                    leftPadding = 0;
                }
            }
            setPadding(leftPadding, topPadding, rightPadding, bottomPadding);
        }
    }

    @SuppressWarnings("deprecation")
    private void setBackgroundCompat(int w, int h) {
        if (isShowShadow) {
            //判断传入的颜色值是否有透明度
            isAddAlpha(mShadowColor);
            Bitmap bitmap = createShadowBitmap(w, h, mCornerRadius, mShadowLimit, mDx, mDy, mShadowColor, Color.TRANSPARENT);
            BitmapDrawable drawable = new BitmapDrawable(bitmap);
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
                setBackgroundDrawable(drawable);
            } else {
                setBackground(drawable);
            }
        } else {
            if (getChildAt(0) == null) {
                if (layoutBackground != null) {
                    firstView = ShadowLayout.this;
                    if (isClickable) {
                        setmBackGround(layoutBackground);
                    } else {
                        changeSwitchClickable();
                    }
                } else {
                    //解决不执行onDraw方法的bug就是给其设置一个透明色
                    this.setBackgroundColor(Color.parseColor("#00000000"));
                }
            } else {
                this.setBackgroundColor(Color.parseColor("#00000000"));
            }


        }

    }


    private void initAttributes(AttributeSet attrs) {
        TypedArray attr = getContext().obtainStyledAttributes(attrs, R.styleable.ShadowLayout);
        if (attr == null) {
            return;
        }

        try {
            //默认是显示
            isShowShadow = !attr.getBoolean(R.styleable.ShadowLayout_hl_shadowHidden, false);
            leftShow = !attr.getBoolean(R.styleable.ShadowLayout_hl_shadowHiddenLeft, false);
            rightShow = !attr.getBoolean(R.styleable.ShadowLayout_hl_shadowHiddenRight, false);
            bottomShow = !attr.getBoolean(R.styleable.ShadowLayout_hl_shadowHiddenBottom, false);
            topShow = !attr.getBoolean(R.styleable.ShadowLayout_hl_shadowHiddenTop, false);
            mCornerRadius = attr.getDimension(R.styleable.ShadowLayout_hl_cornerRadius, getResources().getDimension(R.dimen.helper_dp_0));
            mCornerRadius_leftTop = attr.getDimension(R.styleable.ShadowLayout_hl_cornerRadius_leftTop, -1);
            mCornerRadius_leftBottom = attr.getDimension(R.styleable.ShadowLayout_hl_cornerRadius_leftBottom, -1);
            mCornerRadius_rightTop = attr.getDimension(R.styleable.ShadowLayout_hl_cornerRadius_rightTop, -1);
            mCornerRadius_rightBottom = attr.getDimension(R.styleable.ShadowLayout_hl_cornerRadius_rightBottom, -1);

            //默认扩散区域宽度
            mShadowLimit = attr.getDimension(R.styleable.ShadowLayout_hl_shadowLimit, 0);
            if (mShadowLimit == 0) {
                //如果阴影没有设置阴影扩散区域，那么默认隐藏阴影
                isShowShadow = false;
            }

            //x轴偏移量
            mDx = attr.getDimension(R.styleable.ShadowLayout_hl_shadowOffsetX, 0);
            //y轴偏移量
            mDy = attr.getDimension(R.styleable.ShadowLayout_hl_shadowOffsetY, 0);
            mShadowColor = attr.getColor(R.styleable.ShadowLayout_hl_shadowColor, getResources().getColor(R.color.default_shadow_color));

            selectorType = attr.getInt(R.styleable.ShadowLayout_hl_shapeMode, 1);
            isSym = attr.getBoolean(R.styleable.ShadowLayout_hl_shadowSymmetry, true);

            //背景颜色的点击(默认颜色为白色)
            mBackGroundColor = getResources().getColor(R.color.default_shadowback_color);

            Drawable background = attr.getDrawable(R.styleable.ShadowLayout_hl_layoutBackground);
            if (background != null) {
                if (background instanceof ColorDrawable) {
                    ColorDrawable colordDrawable = (ColorDrawable) background;
                    mBackGroundColor = colordDrawable.getColor();

                } else {
                    layoutBackground = background;
                }
            }

            Drawable trueBackground = attr.getDrawable(R.styleable.ShadowLayout_hl_layoutBackground_true);
            if (trueBackground != null) {
                if (trueBackground instanceof ColorDrawable) {
                    ColorDrawable colordDrawableTrue = (ColorDrawable) trueBackground;
                    mBackGroundColor_true = colordDrawableTrue.getColor();

                } else {
                    layoutBackground_true = trueBackground;
                }
            }

            if (mBackGroundColor_true != -101 && layoutBackground != null) {
                throw new UnsupportedOperationException("使用了ShadowLayout_hl_layoutBackground_true属性，必须先设置ShadowLayout_hl_layoutBackground属性。且设置颜色时，必须保持都为颜色");
            }

            if (layoutBackground == null && layoutBackground_true != null) {
                throw new UnsupportedOperationException("使用了ShadowLayout_hl_layoutBackground_true属性，必须先设置ShadowLayout_hl_layoutBackground属性。且设置图片时，必须保持都为图片");
            }

            //边框颜色的点击
            stroke_color = attr.getColor(R.styleable.ShadowLayout_hl_strokeColor, -101);
            stroke_color_true = attr.getColor(R.styleable.ShadowLayout_hl_strokeColor_true, -101);

            if (stroke_color == -101 && stroke_color_true != -101) {
                throw new UnsupportedOperationException("使用了ShadowLayout_hl_strokeColor_true属性，必须先设置ShadowLayout_hl_strokeColor属性");
            }

            stroke_with = attr.getDimension(R.styleable.ShadowLayout_hl_strokeWith, dip2px(1));
            //规定边框长度最大不错过7dp
            if (stroke_with > dip2px(7)) {
                stroke_with = dip2px(5);
            }


            Drawable clickAbleFalseBackground = attr.getDrawable(R.styleable.ShadowLayout_hl_layoutBackground_clickFalse);
            if (clickAbleFalseBackground != null) {
                if (clickAbleFalseBackground instanceof ColorDrawable) {
                    ColorDrawable colordDrawableClickableFalse = (ColorDrawable) clickAbleFalseBackground;
                    clickAbleFalseColor = colordDrawableClickableFalse.getColor();
                } else {
                    clickAbleFalseDrawable = clickAbleFalseBackground;
                }
            }

            isClickable = attr.getBoolean(R.styleable.ShadowLayout_clickable, true);
            setClickable(isClickable);


        } finally {
            attr.recycle();
        }
    }


    private Bitmap createShadowBitmap(int shadowWidth, int shadowHeight, float cornerRadius, float shadowRadius,
                                      float dx, float dy, int shadowColor, int fillColor) {
        //优化阴影bitmap大小,将尺寸缩小至原来的1/4。
        dx = dx / 4;
        dy = dy / 4;
        shadowWidth = shadowWidth / 4;
        shadowHeight = shadowHeight / 4;
        cornerRadius = cornerRadius / 4;
        shadowRadius = shadowRadius / 4;

        Bitmap output = Bitmap.createBitmap(shadowWidth, shadowHeight, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(output);

        //这里缩小limit的是因为，setShadowLayer后会将bitmap扩散到shadowWidth，shadowHeight
        RectF shadowRect = new RectF(
                shadowRadius,
                shadowRadius,
                shadowWidth - shadowRadius,
                shadowHeight - shadowRadius);

        if (isSym) {
            if (dy > 0) {
                shadowRect.top += dy;
                shadowRect.bottom -= dy;
            } else if (dy < 0) {
                shadowRect.top += Math.abs(dy);
                shadowRect.bottom -= Math.abs(dy);
            }

            if (dx > 0) {
                shadowRect.left += dx;
                shadowRect.right -= dx;
            } else if (dx < 0) {

                shadowRect.left += Math.abs(dx);
                shadowRect.right -= Math.abs(dx);
            }
        } else {
            shadowRect.top -= dy;
            shadowRect.bottom -= dy;
            shadowRect.right -= dx;
            shadowRect.left -= dx;
        }


        shadowPaint.setColor(fillColor);
        if (!isInEditMode()) {//dx  dy
            shadowPaint.setShadowLayer(shadowRadius, dx, dy, shadowColor);
        }

        if (mCornerRadius_leftBottom == -1 && mCornerRadius_leftTop == -1 && mCornerRadius_rightTop == -1 && mCornerRadius_rightBottom == -1) {
            //如果没有设置整个属性，那么按原始去画
            canvas.drawRoundRect(shadowRect, cornerRadius, cornerRadius, shadowPaint);
        } else {
            //目前最佳的解决方案
            rectf.left = leftPadding;
            rectf.top = topPadding;
            rectf.right = getWidth() - rightPadding;
            rectf.bottom = getHeight() - bottomPadding;


            shadowPaint.setAntiAlias(true);
            int leftTop;
            if (mCornerRadius_leftTop == -1) {
                leftTop = (int) mCornerRadius / 4;
            } else {
                leftTop = (int) mCornerRadius_leftTop / 4;
            }
            int leftBottom;
            if (mCornerRadius_leftBottom == -1) {
                leftBottom = (int) mCornerRadius / 4;
            } else {
                leftBottom = (int) mCornerRadius_leftBottom / 4;
            }

            int rightTop;
            if (mCornerRadius_rightTop == -1) {
                rightTop = (int) mCornerRadius / 4;
            } else {
                rightTop = (int) mCornerRadius_rightTop / 4;
            }

            int rightBottom;
            if (mCornerRadius_rightBottom == -1) {
                rightBottom = (int) mCornerRadius / 4;
            } else {
                rightBottom = (int) mCornerRadius_rightBottom / 4;
            }

            float[] outerR = new float[]{leftTop, leftTop, rightTop, rightTop, rightBottom, rightBottom, leftBottom, leftBottom};//左上，右上，右下，左下
            Path path = new Path();
            path.addRoundRect(shadowRect, outerR, Path.Direction.CW);
            canvas.drawPath(path, shadowPaint);
        }

        return output;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        rectf.left = leftPadding;
        rectf.top = topPadding;
        rectf.right = getWidth() - rightPadding;
        rectf.bottom = getHeight() - bottomPadding;
        int trueHeight = (int) (rectf.bottom - rectf.top);
        //如果都为0说明，没有设置特定角，那么按正常绘制
        if (getChildAt(0) != null) {
            if (mCornerRadius_leftTop == -1 && mCornerRadius_leftBottom == -1 && mCornerRadius_rightTop == -1 && mCornerRadius_rightBottom == -1) {
                if (mCornerRadius > trueHeight / 2) {


                    //画圆角矩形
                    canvas.drawRoundRect(rectf, trueHeight / 2, trueHeight / 2, paint);
                    if (stroke_color != -101) {
                        RectF rectFStroke = new RectF(rectf.left + stroke_with / 2, rectf.top + stroke_with / 2, rectf.right - stroke_with / 2, rectf.bottom - stroke_with / 2);
                        canvas.drawRoundRect(rectFStroke, trueHeight / 2, trueHeight / 2, paint_stroke);
                    }

                } else {

                    canvas.drawRoundRect(rectf, mCornerRadius, mCornerRadius, paint);
                    if (stroke_color != -101) {
                        RectF rectFStroke = new RectF(rectf.left + stroke_with / 2, rectf.top + stroke_with / 2, rectf.right - stroke_with / 2, rectf.bottom - stroke_with / 2);
                        canvas.drawRoundRect(rectFStroke, mCornerRadius, mCornerRadius, paint_stroke);
                    }
                }
            } else {
                setSpaceCorner(canvas, trueHeight);
            }
        }

    }


    //这是自定义四个角的方法。
    private void setSpaceCorner(Canvas canvas, int trueHeight) {
        int leftTop;
        int rightTop;
        int rightBottom;
        int leftBottom;
        if (mCornerRadius_leftTop == -1) {
            leftTop = (int) mCornerRadius;
        } else {
            leftTop = (int) mCornerRadius_leftTop;
        }

        if (leftTop > trueHeight / 2) {
            leftTop = trueHeight / 2;
        }

        if (mCornerRadius_rightTop == -1) {
            rightTop = (int) mCornerRadius;
        } else {
            rightTop = (int) mCornerRadius_rightTop;
        }

        if (rightTop > trueHeight / 2) {
            rightTop = trueHeight / 2;
        }

        if (mCornerRadius_rightBottom == -1) {
            rightBottom = (int) mCornerRadius;
        } else {
            rightBottom = (int) mCornerRadius_rightBottom;
        }

        if (rightBottom > trueHeight / 2) {
            rightBottom = trueHeight / 2;
        }


        if (mCornerRadius_leftBottom == -1) {
            leftBottom = (int) mCornerRadius;
        } else {
            leftBottom = (int) mCornerRadius_leftBottom;
        }

        if (leftBottom > trueHeight / 2) {
            leftBottom = trueHeight / 2;
        }

        float[] outerR = new float[]{leftTop, leftTop, rightTop, rightTop, rightBottom, rightBottom, leftBottom, leftBottom};//左上，右上，右下，左下

        if (stroke_color != -101) {


            ShapeDrawable mDrawables = new ShapeDrawable(new RoundRectShape(outerR, null, null));
            mDrawables.getPaint().setColor(paint.getColor());
//            mDrawables.setBounds((int) (leftPadding + stroke_with), (int) (topPadding + stroke_with), (int) (getWidth() - rightPadding - stroke_with), (int) (getHeight() - bottomPadding - stroke_with));
            mDrawables.setBounds(leftPadding, topPadding, getWidth() - rightPadding, getHeight() - bottomPadding);
            mDrawables.draw(canvas);

            ShapeDrawable mDrawablesStroke = new ShapeDrawable(new RoundRectShape(outerR, null, null));
            mDrawablesStroke.getPaint().setColor(paint_stroke.getColor());
            mDrawablesStroke.getPaint().setStyle(Paint.Style.STROKE);
            mDrawablesStroke.getPaint().setStrokeWidth(stroke_with);
//            mDrawablesStroke.setBounds(leftPadding, topPadding, getWidth() - rightPadding, getHeight() - bottomPadding);
            mDrawablesStroke.setBounds((int) (leftPadding + stroke_with / 2), (int) (topPadding + stroke_with / 2), (int) (getWidth() - rightPadding - stroke_with / 2), (int) (getHeight() - bottomPadding - stroke_with / 2));
            mDrawablesStroke.draw(canvas);
        } else {
            ShapeDrawable mDrawables = new ShapeDrawable(new RoundRectShape(outerR, null, null));
            mDrawables.getPaint().setColor(paint.getColor());
            mDrawables.setBounds(leftPadding, topPadding, getWidth() - rightPadding, getHeight() - bottomPadding);
            mDrawables.draw(canvas);
        }

    }


    public void isAddAlpha(int color) {
        //获取单签颜色值的透明度，如果没有设置透明度，默认加上#2a
        if (Color.alpha(color) == 255) {
            String red = Integer.toHexString(Color.red(color));
            String green = Integer.toHexString(Color.green(color));
            String blue = Integer.toHexString(Color.blue(color));

            if (red.length() == 1) {
                red = "0" + red;
            }

            if (green.length() == 1) {
                green = "0" + green;
            }

            if (blue.length() == 1) {
                blue = "0" + blue;
            }
            String endColor = "#2a" + red + green + blue;
            mShadowColor = convertToColorInt(endColor);
        }
    }


    public static int convertToColorInt(String argb)
            throws IllegalArgumentException {

        if (!argb.startsWith("#")) {
            argb = "#" + argb;
        }

        return Color.parseColor(argb);
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mBackGroundColor_true != -101 || stroke_color_true != -101 || layoutBackground_true != null) {
            if (isClickable) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (selectorType == 1) {
                            if (mBackGroundColor_true != -101) {
                                paint.setColor(mBackGroundColor_true);
                            }
                            if (stroke_color_true != -101) {
                                paint_stroke.setColor(stroke_color_true);
                            }

                            if (layoutBackground_true != null) {
                                setmBackGround(layoutBackground_true);
                            }
                            postInvalidate();
                        }
                        break;

                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        if (selectorType == 1) {
                            paint.setColor(mBackGroundColor);
                            if (stroke_color != -101) {
                                paint_stroke.setColor(stroke_color);
                            }

                            if (layoutBackground != null) {
                                setmBackGround(layoutBackground);
                            }
                            postInvalidate();
                        }
                        break;
                }
            }
        }
        return super.onTouchEvent(event);
    }


    public void setmBackGround(Drawable drawable) {

        if (firstView != null && drawable != null) {
            if (mCornerRadius_leftTop == -1 && mCornerRadius_leftBottom == -1 && mCornerRadius_rightTop == -1 && mCornerRadius_rightBottom == -1) {
//                GlideRoundUtils.setRoundCorner(firstView, drawable, mCornerRadius);
            } else {
                int leftTop;
                if (mCornerRadius_leftTop == -1) {
                    leftTop = (int) mCornerRadius;
                } else {
                    leftTop = (int) mCornerRadius_leftTop;
                }
                int leftBottom;
                if (mCornerRadius_leftBottom == -1) {
                    leftBottom = (int) mCornerRadius;
                } else {
                    leftBottom = (int) mCornerRadius_leftBottom;
                }

                int rightTop;
                if (mCornerRadius_rightTop == -1) {
                    rightTop = (int) mCornerRadius;
                } else {
                    rightTop = (int) mCornerRadius_rightTop;
                }

                int rightBottom;
                if (mCornerRadius_rightBottom == -1) {
                    rightBottom = (int) mCornerRadius;
                } else {
                    rightBottom = (int) mCornerRadius_rightBottom;
                }

//                GlideRoundUtils.setCorners(firstView, drawable, leftTop, leftBottom, rightTop, rightBottom);
            }
        }
    }
}