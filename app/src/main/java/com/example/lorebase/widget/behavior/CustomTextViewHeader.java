package com.example.lorebase.widget.behavior;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

@SuppressLint("AppCompatCustomView")
public class CustomTextViewHeader extends TextView {
    public CustomTextViewHeader(Context context) {
        super(context);
        init(context);
    }

    public CustomTextViewHeader(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomTextViewHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        AssetManager assets = context.getAssets();//获取资源文件
        Typeface font = Typeface.createFromAsset(assets, "fonts/FZLanTingHeiS-DB1-GB-Regular.TTF");
        setTypeface(font);
    }
}
