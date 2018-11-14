package com.sw.helpapp.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.sdmdg.tastytoast.TastyToast;
import com.sw.helpapp.libs.GPSTracker;
import com.sw.helpapp.service.HelpService;
import com.sw.helpapp.service.TrackService;

public class HelpReceiver extends BroadcastReceiver{
	public double latitude,mylat;
	public double longitude,mylong;
	SmsManager sms = SmsManager.getDefault();
	String mynum;

	Context ctx;
	GPSTracker gps;
	
	@Override
	public void onReceive(Context ctx, Intent in) {
		// TODO Auto-generated method stub
		final Bundle b = in.getExtras();
		
		gps= new GPSTracker(ctx);
		
		if(gps.canGetLocation()){
			mylat=gps.getLatitude();
			mylong=gps.getLongitude();
		}else{
			gps.showSettingsAlert();
		}
		
		
		
		if(b!=null){
			Object [] pdus_obj = (Object[]) b.get("pdus");
			SmsMessage[] msg= null;
			String[] words= null;
			String message ="";
			String phone="";
			
			msg = new SmsMessage[pdus_obj.length];
			
			for(int i =0;i<msg.length;i++){
				 msg[0] =  SmsMessage.createFromPdu((byte[]) (pdus_obj[i]));	
				 message = msg[0].getMessageBody().toString();
				 phone =msg[0].getOriginatingAddress().toString();
				 
			}
			
				words= message.split(",");
			 if(words[0].equalsIgnoreCase("help")||words[0].equalsIgnoreCase("found")){
				 latitude = Double.parseDouble(words[1]);
				 longitude = Double.parseDouble(words[2]);
				 TastyToast.makeText(ctx, "Message by: "+phone+", Message:"+message, TastyToast.LENGTH_LONG,TastyToast.INFO);
				 in = new Intent(ctx, HelpService.class);
				 in.putExtra("lati", latitude);
				 in.putExtra("longi", longitude);
				 in.putExtra("mylat", mylat);
				 in.putExtra("mylongi", mylong);
				 ctx.startService(in);
				 
			 }
			

			 else if( message.equalsIgnoreCase("Track")){
				
				 TastyToast.makeText(ctx, "Message by: "+phone+", Message:"+message, TastyToast.LENGTH_LONG,TastyToast.INFO);
				in = new Intent(ctx, TrackService.class);
				in.putExtra("mylat", mylat);
				in.putExtra("mylongi",mylong);
				in.putExtra("number", phone);
				ctx.startService(in);
					
			}
			 
			 else if (message.equalsIgnoreCase("Yellow")){
				 
				 sms.sendTextMessage(phone, null, "Track", null, null);
			 }
		}
	}

	
	
}