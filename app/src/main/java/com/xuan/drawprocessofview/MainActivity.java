package com.xuan.drawprocessofview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public final static String TAG=MainActivity.class.getSimpleName();
    private TextView tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        activity -
                public void setContentView(@LayoutRes int layoutResID) {
                    getWindow().setContentView(layoutResID); 关键点
                    initWindowDecorActionBar();
                }
                getWindow() 是 PhoneWindow
                private Window mWindow;
                final void attach(Context context, ActivityThread aThread ....){
                    mWindow = new PhoneWindow(this, window, activityConfigCallback);
                }

        phoneWindow -
                public void setContentView(int layoutResID) {
                    installDecor();
                    ....
                    mLayoutInflater.inflate(layoutResID, mContentParent);
                    ....
                }
                if (mDecor == null) {
                    mDecor = generateDecor(-1);
                }
                if (mContentParent == null) {
                    mContentParent = generateLayout(mDecor);
                }

                protected DecorView generateDecor() {
                    return new DecorView(getContext(), -1);
                }
                得知mContentParent是DecorView
                protected ViewGroup generateLayout(DecorView decor) {
                    layoutResource = R.layout.screen_simple;

                    得知实际上 :
                    mContentParent是mDecor的子view，而我们自己布局对应的view，
                    是mContentParent的子view
                    DecorView是Window的最顶级的View，
                    其下有两个子View，一个标题栏bar，一个是容器content，
                }
        ActivityThread -
            handleLaunchActivity(){
                Activity a = performLaunchActivity(r, customIntent);

                if (a != null) {
                    handleResumeActivity();
                }
            }
            final void handleResumeActivity(IBinder token, ....){
                public final ActivityClientRecord performResumeActivity(IBinder token ....){
                    r.activity.performResume();
                    调用了activity的onResume()
                    ....

                    onResume()之后才开始调用
                    wm.addView(decor, l);
                    wm.updateViewLayout(decor, l);
                    这个方法把控件加载到WindowManager
                    此刻才开始 measure() layout() draw()
                }
            }

        activity -
            updateViewLayout  -
            public void onWindowAttributesChanged(WindowManager.LayoutParams params) {
                // Update window manager if: we have a view, that view is
                // attached to its parent (which will be a RootView), and
                // this activity is not embedded.
                if (mParent == null) {
                    View decor = mDecor;
                    if (decor != null && decor.getParent() != null) {
                        getWindowManager().updateViewLayout(decor, params);
                    }
                }
            }

        View -
            public boolean post(Runnable action) {
                getRunQueue().post(action);
                保存到队列中
            }

            void dispatchAttachedToWindow(AttachInfo info, int visibility) {
                if (mRunQueue != null) {
                    mRunQueue.executeActions(info.mHandler);
                    mRunQueue = null;
                }开始执行
            }

            public void executeActions(Handler handler) {
                synchronized (this) {
                    final HandlerAction[] actions = mActions;
                    for (int i = 0, count = mCount; i < count; i++) {
                        final HandlerAction handlerAction = actions[i];
                        handler.postDelayed(handlerAction.action, handlerAction.delay);
                    }这里才开始测量 所以

                    mActions = null;
                    mCount = 0;
                }
             }


        Window -
            public void setWindowManager(WindowManager wm, IBinder appToken, String appName ....){
                mWindowManager = ((WindowManagerImpl)wm).createLocalWindowManager(this);
            }

        WindowManagerImpl -
            public void addView(@NonNull View view, @NonNull ViewGroup.LayoutParams params) {
                applyDefaultToken(params);
                mGlobal.addView(view, params, mContext.getDisplay(), mParentWindow);
            }
            public void updateViewLayout(@NonNull View view, @NonNull ViewGroup.LayoutParams params) {
                applyDefaultToken(params);
                mGlobal.updateViewLayout(view, params);
            }
        WindowManagerGlobal -
            root = new ViewRootImpl(view.getContext(), display);
            view.setLayoutParams(wparams);
            mViews.add(view);
            mRoots.add(root);
            mParams.add(wparams);
            // do this last because it fires off messages to start doing things
            try {
                root.setView(view, wparams, panelParentView);
            } catch (RuntimeException e) {
            }

        ViewRootImpl -
            public void setView(View view, WindowManager.LayoutParams attrs, View panelParentView) {
            }
            public void requestLayout() {
                if (!mHandlingLayoutInLayoutRequest) {
                    checkThread();
                    mLayoutRequested = true;
                    scheduleTraversals();
                }
            }

            void scheduleTraversals() {
                doTraversal() {
            }
            void doTraversal() {
                performTraversals();
            }
            private void performTraversals() {、
                // Ask host how big it wants to be 正式开始测量
                performMeasure(childWidthMeasureSpec, childHeightMeasureSpec);
            void measureVertical(int widthMeasureSpec, int heightMeasureSpec) {
                // See how tall everyone is. Also remember max width.
                for (int i = 0; i < count; ++i) {
                    // Determine how big this child would like to be. If this or
                    // previous children have given a weight, then we allow it to
                    // use all available space (and we will shrink things later
                    // if needed).
                    final int usedHeight = totalWeight == 0 ? mTotalLength : 0;
                    measureChildBeforeLayout(child, i, widthMeasureSpec, 0,
                        heightMeasureSpec, usedHeight);
                }
            }
            void measureChildBeforeLayout(View child, int childIndex,
                    int widthMeasureSpec, int totalWidth, int heightMeasureSpec,
                    int totalHeight) {
                measureChildWithMargins(child, widthMeasureSpec, totalWidth,
                        heightMeasureSpec, totalHeight);
            }
            protected void measureChildWithMargins(View child,
                    int parentWidthMeasureSpec, int widthUsed,
                    int parentHeightMeasureSpec, int heightUsed) {
                final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

                final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
                        mPaddingLeft + mPaddingRight + lp.leftMargin + lp.rightMargin
                                + widthUsed, lp.width);
                final int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec,
                        mPaddingTop + mPaddingBottom + lp.topMargin + lp.bottomMargin
                                + heightUsed, lp.height);

                // （可能mView还是ViewGroup,那么会循环几次最后才会调用到view的measure）
                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            }

            自身宽高？如何计算？
            根据定位模式使用不同方法
            void measureHorizontal(int widthMeasureSpec, int heightMeasureSpec) {

            }
            void measureVertical(int widthMeasureSpec, int heightMeasureSpec) {

            }


            总结 :
              View的绘制流程 :
                ViewRootImpl
                    performMeasure() 开始测量宽高
                    对于ViewGroup，先测量子View，然后根据子View测量自身宽高
                    其中会根据测量模式AT_Most去相加
                    对于View，宽高是由父布局和自身决定的

                    performLayout() 开始摆放子布局
                        View - layout() - onLayout()
                        linearLayout - layoutVertical(for循环摆放 - child.layout() )

                    performDraw() 开始绘制布局（自身 子布局 背景 等）
                        draw() - drawSoftware() - mView.draw(canvas)
                        - ( drawBackground(canvas)画背景 onDraw(canvas)ViewGroup默认不调用
                        dispatchDraw(canvas)画子View .... )模板设计模式


            如何获取View高度？
                1首先掉用测量方法
                2View的绘制流程是在onResume之后开始的
                3addView setVisibility() ....会调用 requestLayout() 重新绘制
                4优化绘制？onDraw()重复调用 减少嵌套


        */
        setContentView(R.layout.activity_main);

        tv1=findViewById(R.id.tv1);

        Log.e(TAG,"onCreate height " + tv1.getMeasuredHeight());

        tv1.post(new Runnable() {
            @Override
            public void run() {
                //这个方法才可以获取到高度
                Log.e(TAG,"run height " + tv1.getMeasuredHeight());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.e(TAG,"onResume height " + tv1.getMeasuredHeight());
    }
}
