package com.example.wokeshihuimaopaodekafei.myapplication2;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtil {

	public static final String IMAGE_URL = Environment.getExternalStorageState()+"/cache/images";
	
	//public static final int FORMAT_PNG =1;
	public static final int FORMAT_JPEG =1;

	public static boolean isMounted(){
		String state = Environment.getExternalStorageState();
	
		
		return state.equals(Environment.MEDIA_MOUNTED);
		
		
	}

	public static String getFileName(String url){
		
		return url.substring(url.lastIndexOf("/")+1);
		
	}
	
	public static void saveImage(String url , Bitmap bitmap,int format) throws IOException{
		if(!isMounted()){
			return;
		}
		File dir = new File(IMAGE_URL);
		if(!dir.exists()){
			dir.mkdir();
		}
		FileOutputStream fos = new FileOutputStream(new File(dir,getFileName(url)));
		
		bitmap.compress(format == 1? CompressFormat.PNG:CompressFormat.JPEG, 100, fos);
	
	   fos.close();
	}
	public static Bitmap readImage(String url){
		if(!isMounted()){
			return null;
		}
		File file = new File(IMAGE_URL);
		Bitmap bitmap = null;
		if(file.exists()){
			bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
		}
		return bitmap;
		
	}

//	public static void clear(){
//		File dir = new File(IMAGE_URL);
//		if(dir.exists()){
//			File[] file = dir.listFiles();
//			for(File files : file){
//				files.delete();
//			}
//		}
//	}

	public static long getSize(){
		StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
		int count = statFs.getFreeBlocks();
		int size = statFs.getBlockCount();
		
		long ssize = (count*size)/1024/1024;
		return ssize;
	}
	
}
