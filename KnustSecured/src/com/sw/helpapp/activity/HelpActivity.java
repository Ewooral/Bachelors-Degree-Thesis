package com.sw.helpapp.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sdmdg.tastytoast.TastyToast;

import com.sw.helpapp.R;
import com.sw.helpapp.classes.Contact;
import com.sw.helpapp.classes.ShakeDetector;
import com.sw.helpapp.classes.ShakeDetector.OnShakeListener;
import com.sw.helpapp.libs.GPSTracker;

import com.sw.helpapp.libs.SwipeMenu;
import com.sw.helpapp.libs.SwipeMenuCreator;
import com.sw.helpapp.libs.SwipeMenuItem;
import com.sw.helpapp.libs.SwipeMenuListView;
import com.sw.helpapp.libs.SwipeMenuListView.OnMenuItemClickListener;
import com.sw.helpapp.libs.SwipeMenuListView.OnSwipeListener;
import com.sw.helpapp.manager.ContactManager;

public class HelpActivity extends Activity{
	public static String Tag = "HelpActivity";
	Dialog dial;
	ArrayList<String> e_nums, ename;
	SwipeMenuListView lv_contacts;
	GPSTracker gps;
	Button help,trace,map;
	double latitude,longitude;
	ArrayAdapter<String> aas;
	int Postion,pos;
	ContactManager manager;
	ArrayList<Contact> allphone;
	 EditText num;
	 private SensorManager mSensorManager;
	 private Sensor mAccelerometer;
	 private ShakeDetector mShakeDetector;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_gui);
		
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mShakeDetector = new ShakeDetector();
		 mShakeDetector.setOnShakeListener(new OnShakeListener() {
			
			@Override
			public void onShake(int count) {
				// TODO Auto-generated method stub
				allphone = manager.GetPhone();
				for (Contact phon : allphone)
				{
					e_nums.add(phon.getPhone());
				}
				gps= new GPSTracker(HelpActivity.this);
				if(gps.canGetLocation()){
                    
                     latitude = gps.getLatitude();
                     longitude = gps.getLongitude();
                }else{
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }

				String smsSent="SMS Sent";
				String smsDeli = "SMS Delivered";
				
				//Toast.makeText(HelpActivity.this, "Sent Succesfully", Toast.LENGTH_LONG).show();
				PendingIntent pi = PendingIntent.getBroadcast(HelpActivity.this, 0, new Intent(smsSent), 0);
				PendingIntent dpi = PendingIntent.getBroadcast(HelpActivity.this, 0, new Intent(smsDeli), 0);

				registerReceiver(new BroadcastReceiver() {
					
					@Override
					public void onReceive(Context arg0, Intent arg1) {
						// TODO Auto-generated method stub
						switch(getResultCode()){
						case Activity.RESULT_OK:
							TastyToast.makeText(HelpActivity.this, "SMS SENT", TastyToast.LENGTH_LONG,TastyToast.SUCCESS);
							break;
						}
					}
				},new IntentFilter( smsSent));
				
				registerReceiver(new BroadcastReceiver() {
					
					@Override
					public void onReceive(Context arg0, Intent arg1) {
						// TODO Auto-generated method stub
						switch(getResultCode()){
						case Activity.RESULT_OK:
							TastyToast.makeText(HelpActivity.this, "SMS Delivered", TastyToast.LENGTH_LONG,TastyToast.SUCCESS);
							break;
						case Activity.RESULT_CANCELED:
							TastyToast.makeText(HelpActivity.this, "SMS not Delivered", TastyToast.LENGTH_LONG,TastyToast.ERROR);
						break;
					}
					}
				},new IntentFilter( smsDeli));
				
				if(e_nums.isEmpty()){
					TastyToast.makeText(HelpActivity.this, "There are no Contacts", TastyToast.LENGTH_LONG,TastyToast.ERROR);
				}else{
				for(String rec: e_nums){
					
						SmsManager smg = SmsManager.getDefault();
						smg.sendTextMessage(rec, null, "Help,"+latitude+","+longitude , pi, dpi);
					
				}
				}
			}
			});
		
		
		
		help =  (Button) findViewById(R.id.help);
		trace = (Button) findViewById(R.id.trace);
		map =   (Button) findViewById(R.id.map);
		lv_contacts =  (SwipeMenuListView) findViewById(R.id.listview);
		e_nums = new ArrayList<String>();
		ename = new ArrayList<String>();
		manager = new ContactManager(HelpActivity.this);
		registerForContextMenu(lv_contacts);
		SwipeMenuCreator create = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// TODO Auto-generated method stub
				SwipeMenuItem deleteItem = new SwipeMenuItem(HelpActivity.this);
				 deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F,
	                        0x25)));
	                // set item width
	                deleteItem.setWidth(dp2px(90));
	                // set item title
	                deleteItem.setTitle("Delete");
	                // set item title fontsize
	                deleteItem.setTitleSize(18);
	                // set item title font color
	                deleteItem.setTitleColor(Color.WHITE);
	                menu.addMenuItem(deleteItem);
			}
			
		};
		lv_contacts.setMenuCreator(create);
		lv_contacts.setOnSwipeListener(new OnSwipeListener() {
			
			@Override
			public void onSwipeStart(int position) {
				// TODO Auto-generated method stub
				Postion = position;
			}
			
			@Override
			public void onSwipeEnd(int position) {
				// TODO Auto-generated method stub
				
			}
		});
		lv_contacts.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
				// TODO Auto-generated method stub
				switch(index){
				case 0:
                  Contact mycontact = allphone.get(position);
                  manager.DeleteContact(mycontact.getPhone());
                  ename.remove(position);
				  lv_contacts.setAdapter(aas);
				  TastyToast.makeText(HelpActivity.this, "Deleted Succesfully", Toast.LENGTH_LONG, TastyToast.SUCCESS);
					 break;
				}
				return false;
			}
		});
	      lv_contacts.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				
				pos = position;
				return false;
			}
		});
		map.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent inn = new Intent(HelpActivity.this, MapActivity.class);
		         startActivity(inn);
				
			}
		});
		
		help.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			
				allphone = manager.GetPhone();
				for (Contact phon : allphone)
				{
					e_nums.add(phon.getPhone());
				}
				gps= new GPSTracker(HelpActivity.this);
				if(gps.canGetLocation()){
                    
                     latitude = gps.getLatitude();
                     longitude = gps.getLongitude();
                }else{
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }

				String smsSent="SMS Sent";
				String smsDeli = "SMS Delivered";
				
				//Toast.makeText(HelpActivity.this, "Sent Succesfully", Toast.LENGTH_LONG).show();
				PendingIntent pi = PendingIntent.getBroadcast(HelpActivity.this, 0, new Intent(smsSent), 0);
				PendingIntent dpi = PendingIntent.getBroadcast(HelpActivity.this, 0, new Intent(smsDeli), 0);

				registerReceiver(new BroadcastReceiver() {
					
					@Override
					public void onReceive(Context arg0, Intent arg1) {
						// TODO Auto-generated method stub
						switch(getResultCode()){
						case Activity.RESULT_OK:
							TastyToast.makeText(HelpActivity.this, "SMS SENT", TastyToast.LENGTH_LONG,TastyToast.SUCCESS);
							break;
						}
					}
				},new IntentFilter( smsSent));
				
				registerReceiver(new BroadcastReceiver() {
					
					@Override
					public void onReceive(Context arg0, Intent arg1) {
						// TODO Auto-generated method stub
						switch(getResultCode()){
						case Activity.RESULT_OK:
							TastyToast.makeText(HelpActivity.this, "SMS Delivered", TastyToast.LENGTH_LONG,TastyToast.SUCCESS);
							break;
						case Activity.RESULT_CANCELED:
							TastyToast.makeText(HelpActivity.this, "SMS not Delivered", TastyToast.LENGTH_LONG,TastyToast.ERROR);
						break;
					}
					}
				},new IntentFilter( smsDeli));
				
				if(e_nums.isEmpty()){
					TastyToast.makeText(HelpActivity.this, "There are no Contacts", TastyToast.LENGTH_LONG,TastyToast.ERROR);
				}else{
				for(String rec: e_nums){
					
						SmsManager smg = SmsManager.getDefault();
						smg.sendTextMessage(rec, null, "Help,"+latitude+","+longitude , pi, dpi);
					
				}
				}
			}
		
		});
		
		trace.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			
				String smsSent="SMS Sent";
				String smsDeli = "SMS Delivered";
				
				//Toast.makeText(HelpActivity.this, "Sent Succesfully", Toast.LENGTH_LONG).show();
				PendingIntent pi = PendingIntent.getBroadcast(HelpActivity.this, 0, new Intent(smsSent), 0);
				PendingIntent dpi = PendingIntent.getBroadcast(HelpActivity.this, 0, new Intent(smsDeli), 0);

				registerReceiver(new BroadcastReceiver() {
					
					@Override
					public void onReceive(Context arg0, Intent arg1) {
						// TODO Auto-generated method stub
						switch(getResultCode()){
						case Activity.RESULT_OK:
							TastyToast.makeText(HelpActivity.this, "SMS SENT", TastyToast.LENGTH_LONG,TastyToast.SUCCESS);
							break;
						}
					}
				},new IntentFilter( smsSent));
				
				registerReceiver(new BroadcastReceiver() {
					
					@Override
					public void onReceive(Context arg0, Intent arg1) {
						// TODO Auto-generated method stub
						switch(getResultCode()){
						case Activity.RESULT_OK:
							TastyToast.makeText(HelpActivity.this, "SMS DELIVERED", TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
							break;
						case Activity.RESULT_CANCELED:
							TastyToast.makeText(HelpActivity.this, "SMS not Delivered", TastyToast.LENGTH_SHORT,TastyToast.ERROR);
						
					break;
					}
					}
				},new IntentFilter( smsDeli));
				
				if(e_nums.isEmpty()){
					TastyToast.makeText(HelpActivity.this, "There are no Contacts", TastyToast.LENGTH_LONG,TastyToast.ERROR);
				}else{
				for( String rec: e_nums){
					SmsManager smg = SmsManager.getDefault();
					smg.sendTextMessage(rec, null, "Track", pi, dpi);	
				}
				
		}
				
				
		}
			
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub.
		MenuInflater mi = getMenuInflater();
		mi.inflate(R.menu.main_menu, menu);
		return super.onCreateOptionsMenu(menu);
	} 
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
	switch (item.getItemId()) {
	case R.id.econtact:
			dial = new Dialog(HelpActivity.this);
			dial.setTitle("Add Contact");
			dial.setCancelable(true);
			dial.setContentView(R.layout.dialog_contacts);
			num = (EditText) dial.findViewById(R.id.et_num);
			final EditText name = (EditText) dial.findViewById(R.id.et_name);
			Button ok = (Button) dial.findViewById(R.id.ok);
			
			ok.setOnClickListener(new OnClickListener() {
				//adding a new contact and displaying all contacts on the list view 
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if(!(num.getText().toString().equals(""))){
					boolean result=	manager.ADDContact(new Contact(name.getText().toString(), num.getText().toString()));
                   if (result)
                	 {
                	   TastyToast.makeText(HelpActivity.this, "Database Successfully", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS); 
                     }
                   else 
                   {
                	   TastyToast.makeText(HelpActivity.this, "Not Successfully", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);   
                   }
                	  
			       allphone = manager.GetPhone();
			       
			       for (Contact phone : allphone)
			       {
			    	   ename.add(phone.getName());
			    	  
			    	   
			       }
						e_nums.add(num.getText().toString());
						TastyToast.makeText(HelpActivity.this, "Added Successfully", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
						
						aas = new ArrayAdapter<>(HelpActivity.this, android.R.layout.simple_list_item_1,ename);
						lv_contacts.setAdapter(aas);
					} 
					else
					{
						TastyToast.makeText(HelpActivity.this, "Number field empty", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
						
					}
					
					dial.cancel();
				}
			});
			dial.show();
		break;
		
	case R.id.allcon:
		aas.clear();
		
	   allphone = manager.GetPhone();
		   if (allphone.isEmpty())
		   {
			TastyToast.makeText(HelpActivity.this, "There are no Contacts", TastyToast.LENGTH_LONG,TastyToast.ERROR);
		   }
			else {
		   
	       for (Contact phone : allphone)
	       {
	    	   ename.add(phone.getName());
	    	}
				e_nums.add(num.getText().toString());
	

				aas = new ArrayAdapter<>(HelpActivity.this, android.R.layout.simple_list_item_1,ename);
				lv_contacts.setAdapter(aas);
			}
		break;
	}
		return super.onOptionsItemSelected(item);
	}
	
	
	private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
	
	
	
	
	
	
	//ADDING CONTEXT MENU AND THE VARIOUS OPTION
@Override
public void onCreateContextMenu(ContextMenu menu, View v,
		ContextMenuInfo menuInfo) {
	// TODO Auto-generated method stub
	super.onCreateContextMenu(menu, v, menuInfo);
	
	MenuInflater minf = new MenuInflater(HelpActivity.this);
	minf.inflate(R.menu.listcontextmenu, menu);
	
}	@Override
    public boolean onContextItemSelected(MenuItem item) {
	// TODO Auto-generated method stub
	
	
	switch (item.getItemId()) {
	

		
	case R.id.contrace:
		
		String smsSent="SMS Sent";
		String smsDeli = "SMS Delivered";
		PendingIntent pi = PendingIntent.getBroadcast(HelpActivity.this, 0, new Intent(smsSent), 0);
		PendingIntent dpi = PendingIntent.getBroadcast(HelpActivity.this, 0, new Intent(smsDeli), 0);
		registerReceiver(new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				// TODO Auto-generated method stub
				switch(getResultCode()){
				case Activity.RESULT_OK:
					TastyToast.makeText(HelpActivity.this, "SMS SENT", TastyToast.LENGTH_LONG,TastyToast.SUCCESS);
					break;
				}
			}
		},new IntentFilter( smsSent));
		
		registerReceiver(new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				// TODO Auto-generated method stub
				switch(getResultCode()){
				case Activity.RESULT_OK:
					TastyToast.makeText(HelpActivity.this, "SMS DELIVERED", TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
					break;
				case Activity.RESULT_CANCELED:
					TastyToast.makeText(HelpActivity.this, "SMS not Delivered", TastyToast.LENGTH_SHORT,TastyToast.ERROR);
				
			break;
			}
			}
		},new IntentFilter( smsDeli));
		
		if(e_nums.isEmpty()){
			TastyToast.makeText(HelpActivity.this, "There are no Contacts", TastyToast.LENGTH_LONG,TastyToast.ERROR);
		}else{
		
			SmsManager smg = SmsManager.getDefault();
			smg.sendTextMessage(allphone.get(Postion).getPhone(), null, "Yellow", pi, dpi);	
		
		
}
		TastyToast.makeText(HelpActivity.this, "I'll help you trace soon", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);   
		break;
		
		
// TO EDIT CONTACT AND ALLOW CHANGES		
	case R.id.contedit:
		
    Contact thisphone =	allphone.get(pos);
   	String thename = thisphone.getName();
	String thenumber = thisphone.getPhone();
    Bundle bun = new Bundle();
	bun.putString("name",thename);
	bun.putString("number",thenumber);
	Intent inn = new Intent(HelpActivity.this, EditContact.class);
	inn.putExtras(bun);
	startActivity(inn);
		
		break;
	default:
		break;
	}
	return super.onContextItemSelected(item);
}
@Override
protected void onPause() {
	// TODO Auto-generated method stub
	super.onPause();
	mSensorManager.unregisterListener(mShakeDetector);
}
@Override
protected void onResume() {
	// TODO Auto-generated method stub
	super.onResume();
	mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
	
}

}
