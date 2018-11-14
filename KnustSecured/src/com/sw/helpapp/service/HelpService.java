package com.sw.helpapp.service;
import com.sdmdg.tastytoast.TastyToast;
import com.sw.helpapp.R;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.widget.Toast;

public class HelpService extends Service{
	Context ctx;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		this.ctx = this;
		TastyToast.makeText(this, " Help Service Started", TastyToast.LENGTH_LONG,TastyToast.SUCCESS);
//		MediaPlayer mp3 = MediaPlayer.create(ctx, R.raw.location);
//		mp3.start();
		double latitude= intent.getDoubleExtra("lati", 0);
		double longitude = intent.getDoubleExtra("longi", 0);
		double mylat =intent.getDoubleExtra("mylat",0);
		double mylong =intent.getDoubleExtra("mylongi",0);
		
		Uri gimmeIntent = Uri.parse("http://maps.google.com/maps?saddr="+mylat+","+mylong+"&daddr="+latitude+","+longitude);
		Intent mapIntent = new Intent(Intent.ACTION_VIEW,gimmeIntent);
		mapIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(mapIntent);
		//mp3.stop();
		
		return super.onStartCommand(intent, flags, startId);
	}

}
