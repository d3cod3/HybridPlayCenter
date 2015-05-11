package com.hybridplay.network;

import java.net.URI;
import java.util.HashMap;
import java.util.Vector;

import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;

import com.hybridplay.center.R;
import com.hybridplay.shared.SharedFunctions;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;

public class HPRequest {

	public static final int MSG_ERROR = 9999;
	public Vector<String> imageUrl = new Vector<String>();
	Vector<String> selectedCategories = new Vector<String>();
	private static SharedPreferences prefs;
	private static Context context;
	private static String protocol;
	private static int what;
	private static HashMap<String, Object> data;
	public static String error;
	public static String TAG = "HPRequest";
	private static Handler handler;
	
	public HPRequest(Context c, Handler h, String _protocol, HashMap<String, Object> _data, int _what) {
		context = c;
		handler = h;
		data = _data;
		protocol = _protocol;
		what = _what;
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	public void execute() {

		new getStreamTask().execute(this);

	}
	
	private static class getStreamTask extends AsyncTask<HPRequest, Boolean, Boolean> {

		private boolean success = false;
		private Object obj;

		@Override
		protected void onPostExecute(Boolean result) {

			Message msg = new Message();
			if (success) {
				msg.arg1 = 0;
				msg.obj = obj;
				msg.what = what;
			}
			else {
				msg.arg1 = R.string.error;
				msg.obj = error;
				msg.what = MSG_ERROR;
			}
			handler.sendMessage(msg);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(HPRequest... streams) {
			if(!SharedFunctions.isInternetOn(context)) {
				error = context.getString(R.string.checkSetupInternet);
				return false;
			}
			
			String password = prefs.getString(SharedFunctions.HP_PREF_PASS, null);
			String username = prefs.getString(SharedFunctions.HP_PREF_USER, null);
			String website = HPWebsite.getWebsite(context);
			
			if(username == null || website == null || password == null)
				return false;
			
			Object[] params;
			
			params = new Object[] { 
				username,
				password,
				data 
			};
			
			String uris = website+"index.php?hp_xmlrpc=true";
			
			URI uri;
			
			try {
				uri = URI.create(uris);
				
			} catch(Exception e) {
				error = context.getString(R.string.websiteSyntaxError);
				return false;
			}
			
			XMLRPCClient client = new XMLRPCClient(uri,username, password);
			
			try {
				obj = client.call(protocol, params);
				success = true;
				return true;
			} catch (final XMLRPCException e) {
				error = e.toString();
				e.printStackTrace();
			}
			return false;
		}

	}

}
