package com.superfunapp.ticketvendor.sharedPrefrns;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import java.util.ArrayList;

public class SharedPrefrnceSuperFun {

	public static final String PREFRENCE_NAME = "SuperFunApp_Vendor_Tickets";
	public static ArrayList<Bitmap> imagePicturebyte = new ArrayList<Bitmap>();

	public static String getSharedPrefData(Activity activity, String key) {
		SharedPreferences prefs = activity.getSharedPreferences(PREFRENCE_NAME,
				Context.MODE_PRIVATE);
		String value = null;
		if (prefs != null && prefs.contains(key)) {
			value = prefs.getString(key, "null");
		}
		return value;
	}

	public static void setDataInSharedPrefrence(Activity activity, String key,
			String value) {
		SharedPreferences prefs = activity.getSharedPreferences(PREFRENCE_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static void deletePrefrenceData(Activity activity) {
		SharedPreferences prefs = activity.getSharedPreferences(PREFRENCE_NAME,
				Context.MODE_PRIVATE);
		prefs.edit().clear().commit();
	}
}
