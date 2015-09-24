package com.superfunapp.ticketvendor.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Locale;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.StrictMode;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;

public class Utility {

	
	
	public static String getSimNumber(Context ctx) {

		TelephonyManager telemamanger = (TelephonyManager) ctx
				.getSystemService(Context.TELEPHONY_SERVICE);
		String getSimSerialNumber = telemamanger.getSimSerialNumber();

		return getSimSerialNumber;

	}

	@SuppressWarnings("deprecation")
	public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return Formatter.formatIpAddress(inetAddress.hashCode());
					}
				}
			}
		} catch (Exception ex) {
			Log.e("IP Address", ex.toString());
		}
		return null;
	}

	public static void changeLangauge(String langName, Context ctx) {
		Locale locale = null;
		if (langName.equals("ar")) {

			locale = new Locale("ar");
			Locale.setDefault(locale);
		} else {
			locale = new Locale("en");
			Locale.setDefault(locale);
		}

		Configuration config = new Configuration();
		config.locale = locale;
		ctx.getResources().updateConfiguration(config,
				ctx.getResources().getDisplayMetrics());
	}

	public static String getDeviceId(Context ctx) {

		String device_uuid = Secure.getString(ctx.getContentResolver(),
				Secure.ANDROID_ID);

		return device_uuid;
	}
	

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public static void setThreadPolicy() {
		try {
			if (android.os.Build.VERSION.SDK_INT > 9) {
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
						.permitAll().build();
				StrictMode.setThreadPolicy(policy);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
