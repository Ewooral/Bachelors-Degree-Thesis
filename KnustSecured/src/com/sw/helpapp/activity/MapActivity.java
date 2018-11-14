package com.sw.helpapp.activity;

import java.io.IOException;
import java.util.List;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.sdmdg.tastytoast.TastyToast;
import com.sw.helpapp.R;


import android.app.Dialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class MapActivity extends FragmentActivity {


private static final int GPS_ERRORDIALOG_REQUEST = 9001;
GoogleMap mMap;

private static final double KNUSTLAT =6.6762338, KNUSTLONG = -1.5582488;
private static final float zoom =15;

@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.home_gui);
  if (ServicesOk())
	{
		setContentView(R.layout.activity_map);
	if (initMap())
	{
		TastyToast.makeText(this, "Map Ready", TastyToast.LENGTH_SHORT, TastyToast.DEFAULT);	
	    gotolocation(KNUSTLAT, KNUSTLONG, zoom);
	    mMap.setMyLocationEnabled(true);
	}
	else
	{
		TastyToast.makeText(this, "Sorry!! Map not Ready", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
	}
	}
	else
	{
		setContentView(R.layout.home_gui);
	}
	
}




public boolean ServicesOk(){
	int isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
	
	if (isAvailable==ConnectionResult.SUCCESS)
	{
		return true;
	}
	else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable))
	{
		Dialog dialog = GooglePlayServicesUtil.getErrorDialog(isAvailable, this, GPS_ERRORDIALOG_REQUEST);
	     dialog.show();
	}
	else 
	{
		TastyToast.makeText(this, "Can't Connect to Google Play Services", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
	 
	}
	return false;
}
  public void geolocate(View v) throws IOException
  {
	  hideSoftKeyboard(v);
	  EditText et = (EditText) findViewById(R.id.search);
	  String location =  et.getText().toString();
	  Geocoder geo = new Geocoder(this);
	  List<Address> list = geo.getFromLocationName(location, 1);
	  Address addr = list.get(0);
	  String locality = addr.getLocality();
	  TastyToast.makeText(this, locality, TastyToast.LENGTH_SHORT, TastyToast.INFO); 
      
	  double lat = addr.getLatitude();
	  double lng = addr.getLongitude();
	  
	  gotolocation(lat, lng, zoom);
  }

   private void hideSoftKeyboard(View v) {
	// TODO Auto-generated method stub
	   InputMethodManager imm= (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
	   imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	
}

public boolean initMap()
   {
	   if (mMap== null)
	   {
		   SupportMapFragment mapfrag = 
				   (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		   mMap = mapfrag.getMap();
		   
	   }
	   return mMap != null;
   }
  private void gotolocation (double lat, double lng, float zoom)
    {
	  LatLng ll = new LatLng(lat, lng);
	  CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
	  mMap.moveCamera(update);
	
    }
  @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		
		MenuInflater min = getMenuInflater();
		min.inflate(R.menu.map_menu, menu);
		
		return super.onCreateOptionsMenu(menu);
	}
  @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
	  switch (item.getItemId()) {
	case R.id.mapTypeNormal:
		mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		break;
		case R.id.mapTypeSatellite:
			mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			
			break;
			
		case R.id.mapTypeHybrid:
			mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			break;

	default:
		break;
	}
		return super.onOptionsItemSelected(item);
	}
   }
