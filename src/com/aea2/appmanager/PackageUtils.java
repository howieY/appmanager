package com.aea2.appmanager;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class PackageUtils {
	private static final String TAG = PackageUtils.class.getSimpleName();

	public String getPackageSignatures(Context context, String packageName) {
		//FLAG_SYSTEM
		StringBuffer sf = new StringBuffer();
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(
					packageName, PackageManager.GET_SIGNATURES);
			Log.d(TAG, packageName + " " + info.versionCode + " "
					+ info.versionName);
			for (Signature signature : info.signatures) {
				Log.d(TAG, "" + signature.toCharsString());
				sf.append(signature.toCharsString());
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return sf.toString();
	}

}
