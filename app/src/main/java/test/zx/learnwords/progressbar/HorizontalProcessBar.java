package test.zx.learnwords.progressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

import test.zx.learnwords.R;

/**
 * Created by THink on 2018/1/17.
 */

public class HorizontalProcessBar extends ProgressBar {
    private static final int DEFAULT_TEXT_SIZE=10;
    private static final int DEFAULT_TEXT_COLOR=0xfffc00d1;
    private static final int DEFAULT_COLOR_UNREACHED=0xffd3d6da;
    private static final int DEFAULT_HEIGHT_UNREACHED=2;
    private static final int DEFAULT_COLOR_REACHED=DEFAULT_TEXT_COLOR;
    private static final int DEFAULT_HEIGHT_REACHED=2;
    private static final int DEFAULT_TEXT_OFFSET=10;
    private int mTextSize=sp2px(DEFAULT_TEXT_SIZE);
    private int mTextColor=DEFAULT_TEXT_COLOR;
    private int mUnreachedColor=DEFAULT_COLOR_UNREACHED;
    private int mUnreachedHeight=dp2px(DEFAULT_HEIGHT_UNREACHED);
    private int mReachedColor=DEFAULT_COLOR_REACHED;
    private int mReachedHeight=dp2px(DEFAULT_HEIGHT_REACHED);
    private int mTextOffset=dp2px(DEFAULT_TEXT_OFFSET);
    private Paint mPaint=new Paint();
    private int mRealWidth;

    public HorizontalProcessBar(Context context) {
        this(context,null);
    }

    public HorizontalProcessBar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }
    public HorizontalProcessBar(Context context,AttributeSet attrs,int defStyle){
        super(context, attrs,defStyle);
    }
    private int dp2px(int dpVal){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal,getResources().getDisplayMetrics());
    }
    private int sp2px(int spVal){
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,spVal,
                                           getResources().getDisplayMetrics());
    }
    private void obtainStyledAttrs(AttributeSet attrs){
        TypedArray ta=getContext().obtainStyledAttributes(attrs, R.styleable.HorizontalProcessBar);
        mTextSize=(int)ta.getDimension(R.styleable.HorizontalProcessBar_progress_text_size,mTextSize);
        mTextColor=ta.getColor(R.styleable.HorizontalProcessBar_progress_text_color,mTextColor);
        mTextOffset=(int)ta.getDimension(R.styleable.HorizontalProcessBar_progress_text_offset,mTextOffset);
        mUnreachedColor=ta.getColor(R.styleable.HorizontalProcessBar_progress_unreached_color,mUnreachedColor);
        mUnreachedHeight= (int) ta.getDimension(R.styleable.HorizontalProcessBar_progress_unreached_height,mUnreachedHeight);
        ta.recycle();
        mPaint.setTextSize(mTextSize);
    }
    protected synchronized void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);
        int widthVal=MeasureSpec.getSize(widthMeasureSpec);
        int height=measuredHeight(heightMeasureSpec);
        setMeasuredDimension(widthVal, height);
        mRealWidth=getMeasuredWidth()-getPaddingLeft()-getPaddingRight();
    }
    private int measuredHeight(int heightMeasureSpec){
        int result=0;
        int mode=MeasureSpec.getMode(heightMeasureSpec);
        int size=MeasureSpec.getSize(heightMeasureSpec);
        if(mode==MeasureSpec.EXACTLY){
            result=size;
        }else{
            int textHeight=(int)(mPaint.descent()-mPaint.ascent());
            result=getPaddingTop()+getPaddingBottom()
                    +Math.max(Math.max(mReachedHeight,mUnreachedHeight),
                    Math.abs(textHeight));
            if(mode==MeasureSpec.AT_MOST){
                result=Math.min(result,size);
            }
        }
        return result;
    }
    protected synchronized void onDraw(Canvas canvas){
        canvas.save();
        canvas.translate(getPaddingLeft(),getHeight()/2);
        boolean noNeedIfUnreached=false;
        String text=getProgress()+"%";
        float radio=getProgress()*1.0f/getMax();
        int textWidth=(int)mPaint.measureText(text);
        float progressX=radio*mRealWidth;
        if(progressX+textWidth>mRealWidth){
            progressX=mRealWidth-textWidth;
            noNeedIfUnreached=true;
        }
        float endX=radio*mRealWidth-mTextOffset/2;
        if(endX>0){
            mPaint.setColor(mReachedColor);
            mPaint.setStrokeWidth(mReachedHeight);
            canvas.drawLine(0,0,endX,0,mPaint);
        }

        //draw text
        mPaint.setColor(mTextColor);
        int y=(int)(-(mPaint.descent()+mPaint.ascent())/2);
        canvas.drawText(text,progressX,y,mPaint);
        //draw unreached bar
        if(!noNeedIfUnreached){
            float start=progressX+mTextOffset/2+textWidth;
            mPaint.setColor(mUnreachedColor);
            mPaint.setStrokeWidth(mUnreachedHeight);
            canvas.drawLine(start,0,mRealWidth,0,mPaint);
        }
        canvas.restore();
    }
}
