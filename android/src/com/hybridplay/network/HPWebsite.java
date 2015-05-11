package com.hybridplay.network;

import java.net.URI;

import com.hybridplay.shared.SharedFunctions;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class HPWebsite {

	// HybridPlay Games Center WP based website
	public final static String CUSTOM_WEBSITE = "http://games.hybridplay.com/";

	public static String getWebsite(Context context) {

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

		String website = CUSTOM_WEBSITE  != null ? CUSTOM_WEBSITE : prefs.getString(SharedFunctions.HP_PREF_WEBSITE, "");
		if(website.length() == 0)
			return null;

		website = sanitizeWebsite(website);

		return website;
	}

	public static String sanitizeWebsite(String website) {
		if(!website.startsWith("http"))
			website = "http://"+website;

		// potential problems

		website = website.replaceAll("\\?.*", "").replaceAll("index.php$","");

		if(!website.endsWith("/"))
			website = website+"/";

		return website;

	}

	public static boolean isValidWebsite(String link) {
		try {
			URI.create(link);

		} catch(Exception e) {
			return false;
		}

		return true;
	}

}
