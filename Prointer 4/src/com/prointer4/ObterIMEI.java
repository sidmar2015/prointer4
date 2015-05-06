package com.prointer4;

import android.content.Context;
import android.telephony.TelephonyManager;

public class ObterIMEI {
	
	private String IMEI;
	TelephonyManager tManager;
	

	public String getIMEI(Context context) {
		
		tManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE); 
		IMEI = tManager.getDeviceId();
		
		return IMEI;
	}

	

}
