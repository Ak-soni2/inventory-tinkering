package com.yourpackage.GradientText; // Replace with your actual package name

import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.util.AttributeSet;

public class GradientTextView extends androidx.appcompat.widget.AppCompatTextView {

    public GradientTextView(Context context) {
        super(context);
    }

    public GradientTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GradientTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        // Apply gradient to text
        int[] colors = {0xFF3366FF, 0xFF00CCFF}; // Example gradient colors
        getPaint().setShader(new LinearGradient(0, 0, getWidth(), getHeight(), colors, null, Shader.TileMode.CLAMP));
    }
}
