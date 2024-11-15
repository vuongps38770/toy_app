package com.project1.toystoreapp.layout;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;

public class KeyboardVisibilityUtils {

    public interface OnKeyboardVisibilityListener {
        void onKeyboardVisibilityChanged(boolean isVisible);
    }

    public static void setKeyboardVisibilityListener(View rootView, OnKeyboardVisibilityListener listener) {
        final View decorView = rootView.getRootView();
        decorView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            private boolean isKeyboardVisible = false;
            @Override
            public boolean onPreDraw() {
                int heightDiff = decorView.getHeight() - rootView.getHeight();
                if (heightDiff > 100) {
                    if (!isKeyboardVisible) {
                        isKeyboardVisible = true;
                        listener.onKeyboardVisibilityChanged(true);
                    }
                } else {
                    if (isKeyboardVisible) {
                        isKeyboardVisible = false;
                        listener.onKeyboardVisibilityChanged(false);
                    }
                }
                return true;
            }
        });
    }
}

