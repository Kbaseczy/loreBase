package com.example.lorebase.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lorebase.R;

public class ToastUtil {
    private static Toast toast;//实现不管我们触发多少次Toast调用，都只会持续一次Toast显示的时长

    /**
     * 短时间显示Toast【居下】
     *
     * @param msg 显示的内容-字符串
     */
    public static void showShortToast(String msg, Context context) {
        toast = new Toast(context);
        View view = LayoutInflater.from(context).inflate(R.layout.toast_view, null);
        TextView textView = view.findViewById(R.id.tv_message_toast);
        textView.setText(msg);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * 短时间显示Toast【居中】
     *
     * @param msg 显示的内容-字符串
     */
    public static void showShortToastCenter(String msg, Context context) {
        toast = new Toast(context);
        View view = LayoutInflater.from(context).inflate(R.layout.toast_view, null);
        TextView textView = view.findViewById(R.id.tv_message_toast);
        textView.setText(msg);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * 短时间显示Toast【居上】
     *
     * @param msg 显示的内容-字符串
     */
    public static void showShortToastTop(String msg, Context context) {
        toast = new Toast(context);
        View view = LayoutInflater.from(context).inflate(R.layout.toast_view, null);
        TextView textView = view.findViewById(R.id.tv_message_toast);
        textView.setText(msg);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * 长时间显示Toast【居下】
     *
     * @param msg 显示的内容-字符串
     */
    public static void showLongToast(String msg, Context context) {
        toast = new Toast(context);
        View view = LayoutInflater.from(context).inflate(R.layout.toast_view, null);
        TextView textView = view.findViewById(R.id.tv_message_toast);
        textView.setText(msg);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * 长时间显示Toast【居中】
     *
     * @param msg 显示的内容-字符串
     */
    public static void showLongToastCenter(String msg, Context context) {
        toast = new Toast(context);
        View view = LayoutInflater.from(context).inflate(R.layout.toast_view, null);
        TextView textView = view.findViewById(R.id.tv_message_toast);
        textView.setText(msg);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }

    /**
     * 长时间显示Toast【居上】
     *
     * @param msg 显示的内容-字符串
     */
    public static void showLongToastTop(String msg, Context context) {
        toast = new Toast(context);
        View view = LayoutInflater.from(context).inflate(R.layout.toast_view, null);
        TextView textView = view.findViewById(R.id.tv_message_toast);
        textView.setText(msg);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }
}
