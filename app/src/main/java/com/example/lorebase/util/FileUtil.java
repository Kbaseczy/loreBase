package com.example.lorebase.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import com.example.lorebase.contain_const.ConstName;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {

    public static void save(String fileDir, Bitmap bitmap) {
        try {
            //如果手机已插入sd卡,且app具有读写sd卡的权限
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File dirFile = new File(fileDir);
                if (!dirFile.exists())
                    dirFile.mkdirs();
                File file = new File(fileDir, ConstName.IMAGE_NAME);
                FileOutputStream output;
                output = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
                output.flush();
                output.close();
                L.v("filename:" + file.getName());
            } else {
                L.v("copy fail");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    public static Bitmap Bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    //bitmap --> drawable
    public static Drawable getDrawableImage(Context context) {
        Bitmap bitmap = null;
        String path = ConstName.IMAGE_PATH_PRE + ConstName.IMAGE_NAME;
        if (fileIsExists(path)) {
            bitmap = BitmapFactory.decodeFile(path);
        }
        return new BitmapDrawable(context.getResources(), bitmap);
    }

    public static boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
