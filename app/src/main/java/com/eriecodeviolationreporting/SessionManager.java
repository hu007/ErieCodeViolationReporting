package com.eriecodeviolationreporting;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;

public class SessionManager {

	Context context;

	SharedPreferences pref;

	Editor editor;

	String PREF_NAME = "pref_name";

	int PREF_MODE = 0;

	static final String LATITUDE = "latitude";
	static final String LONGITUDE = "longitude";
	static final String ADDRESS = "address";
	static final String FILE_LINK = "fileAttach";
	static final String DATE = "date";
	static final String TIME = "time";

	public SessionManager(Context _context) {
		super();
		this.context = _context;
		pref = context.getSharedPreferences(PREF_NAME, PREF_MODE);
		editor = pref.edit();
	}


	public void ShareValues(String latitude, String longitude, String address, String date, String time){


		editor.putString(LATITUDE, latitude);
		editor.putString(LONGITUDE, longitude);
		editor.putString(ADDRESS, address);
		editor.putString(DATE, date);
		editor.putString(TIME, time);
		editor.commit();
	}

	public HashMap<String, String> getUserDetails(){
		HashMap<String, String> user = new HashMap<String, String>();
		user.put(LATITUDE, pref.getString(LATITUDE, null));
		user.put(LONGITUDE, pref.getString(LONGITUDE, null));
		user.put(ADDRESS, pref.getString(ADDRESS, null));
		user.put(DATE, pref.getString(DATE, null));
		user.put(TIME, pref.getString(TIME, null));
		return user;
	}



}
