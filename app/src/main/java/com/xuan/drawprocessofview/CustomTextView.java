package com.xuan.drawprocessofview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * com.xuan.drawprocessofview
 *
 * @author by xuan on 2018/5/22
 * @version [版本号, 2018/5/22]
 * @update by xuan on 2018/5/22
 * @descript
 */
public class CustomTextView extends View {
    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /*
        widthMeasureSpec heightMeasureSpec 其实就是ViewGroup的
        final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
                        mPaddingLeft + mPaddingRight + lp.leftMargin + lp.rightMargin
                                + widthUsed, lp.width);
                final int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec,
                        mPaddingTop + mPaddingBottom + lp.topMargin + lp.bottomMargin
                                + heightUsed, lp.height);
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);



        getChildMeasureSpec(parentHeightMeasureSpec,
                        mPaddingTop + mPaddingBottom + lp.topMargin + lp.bottomMargin
                                + heightUsed, lp.height);
                                这个方法里面判断的模式

        switch (specMode) {
        子布局的模式是由父布局的模式和自己的大小决定

        // Parent has imposed an exact size on us
        case MeasureSpec.EXACTLY:
            if (childDimension >= 0) {
                resultSize = childDimension;
                resultMode = MeasureSpec.EXACTLY;
            } else if (childDimension == LayoutParams.MATCH_PARENT) {
                // Child wants to be our size. So be it.
                resultSize = size;
                resultMode = MeasureSpec.EXACTLY;
            } else if (childDimension == LayoutParams.WRAP_CONTENT) {
                // Child wants to determine its own size. It can't be
                // bigger than us.
                resultSize = size;
                resultMode = MeasureSpec.AT_MOST;
            }
            break;

        // Parent has imposed a maximum size on us
        case MeasureSpec.AT_MOST:
            if (childDimension >= 0) {
                // Child wants a specific size... so be it
                resultSize = childDimension;
                resultMode = MeasureSpec.EXACTLY;
            } else if (childDimension == LayoutParams.MATCH_PARENT) {
                // Child wants to be our size, but our size is not fixed.
                // Constrain child to not be bigger than us.
                resultSize = size;
                resultMode = MeasureSpec.AT_MOST;
            } else if (childDimension == LayoutParams.WRAP_CONTENT) {
                // Child wants to determine its own size. It can't be
                // bigger than us.
                resultSize = size;
                resultMode = MeasureSpec.AT_MOST;
            }
            break;

        // Parent asked to see how big we want to be
        case MeasureSpec.UNSPECIFIED:
            if (childDimension >= 0) {
                // Child wants a specific size... let him have it
                resultSize = childDimension;
                resultMode = MeasureSpec.EXACTLY;
            } else if (childDimension == LayoutParams.MATCH_PARENT) {
                // Child wants to be our size... find out how big it should
                // be
                resultSize = View.sUseZeroUnspecifiedMeasureSpec ? 0 : size;
                resultMode = MeasureSpec.UNSPECIFIED;
            } else if (childDimension == LayoutParams.WRAP_CONTENT) {
                // Child wants to determine its own size.... find out how
                // big it should be
                resultSize = View.sUseZeroUnspecifiedMeasureSpec ? 0 : size;
                resultMode = MeasureSpec.UNSPECIFIED;
            }
            break;
        }

    View -
        这个时候才是赋值给了宽高
        protected final void setMeasuredDimension(int measuredWidth, int measuredHeight) {
            boolean optical = isLayoutModeOptical(this);
            if (optical != isLayoutModeOptical(mParent)) {
                Insets insets = getOpticalInsets();
                int opticalWidth  = insets.left + insets.right;
                int opticalHeight = insets.top  + insets.bottom;

                measuredWidth  += optical ? opticalWidth  : -opticalWidth;
                measuredHeight += optical ? opticalHeight : -opticalHeight;
            }
            setMeasuredDimensionRaw(measuredWidth, measuredHeight);
        }

         */

    }
}
