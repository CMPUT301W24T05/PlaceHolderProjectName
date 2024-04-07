package ca.cmput301t05.placeholder.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;

public class CustomNestedScrollView extends NestedScrollView {

    private RecyclerView controlledRecyclerView;
    private boolean canScrollRecycler;

    public CustomNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setControlledRecyclerView(RecyclerView controlledRecyclerView) {
        this.controlledRecyclerView = controlledRecyclerView;
        if (controlledRecyclerView != null) {
            controlledRecyclerView.setNestedScrollingEnabled(false);
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        // check if we've reached the bottom, and if we have, set flag to enable RecyclerView scrolling.
        if (!canScrollVertically(1)) {
            canScrollRecycler = true;
            controlledRecyclerView.setNestedScrollingEnabled(true);
            Log.d("CustomNestedScrollView", "Enabling RecyclerView scrolling");
        }
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        // Allow 'controlledRecyclerView' to scroll vertically only if 'CustomNestedScrollView' cannot scroll further down
        if (target == controlledRecyclerView) {
            if (dy > 0 && canScrollVertically(1)) {
                // When scrolling down, if the 'CustomNestedScrollView' can still scroll down, disable 'controlledRecyclerView' scrolling and scroll on 'CustomNestedScrollView'
                controlledRecyclerView.setNestedScrollingEnabled(false);
                super.onNestedPreScroll(target, dx, dy, consumed, type);
                canScrollRecycler = false;
                Log.d("CustomNestedScrollView", "CustomNest can still scroll down, disable RecyclerView scrolling");
            } else if (dy > 0 && !canScrollVertically(1)) {
                // When scrolling down, if the 'CustomNestedScrollView' cannot scroll down further, enable 'controlledRecyclerView' scrolling
                controlledRecyclerView.setNestedScrollingEnabled(true);
                canScrollRecycler = true;
                Log.d("CustomNestedScrollView", "CustomNest cannot scroll down, enable RecyclerView scrolling");
            } else if (dy < 0) {
                // When scrolling up
                if (!canScrollRecycler || !controlledRecyclerView.canScrollVertically(-1)) {
                    // If the 'controlledRecyclerView' cannot scroll up further, scroll on 'CustomNestedScrollView' and disable 'controlledRecyclerView' scrolling
                    controlledRecyclerView.setNestedScrollingEnabled(false);
                    super.onNestedPreScroll(target, dx, dy, consumed, type);
                    canScrollRecycler = false;
                    Log.d("CustomNestedScrollView", "CustomNest cannot scroll up, disable RecyclerView scrolling");
                } else {
                    // If 'controlledRecyclerView' can still scroll up, enable 'controlledRecyclerView' scrolling
                    controlledRecyclerView.setNestedScrollingEnabled(true);
                    Log.d("CustomNestedScrollView", "CustomNest can still scroll up, enable RecyclerView scrolling");
                }
            }
        } else {
            super.onNestedPreScroll(target, dx, dy, consumed, type);
        }
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        boolean intercepted = super.onInterceptTouchEvent(ev);
//        Log.d("CustomNestedScrollView", "onInterceptTouchEvent: intercepted = " + intercepted);
//        return intercepted;
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        boolean handled = super.onTouchEvent(ev);
//        Log.d("CustomNestedScrollView", "onTouchEvent: handled = " + handled);
//        return handled;
//    }
}
