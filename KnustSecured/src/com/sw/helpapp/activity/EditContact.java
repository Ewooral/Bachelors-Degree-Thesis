package com.sw.helpapp.activity;

import com.sdmdg.tastytoast.TastyToast;
import com.sw.helpapp.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

public class EditContact extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.edi_contact);
		
		

		Intent in = getIntent();
	Bundle bunn = in.getExtras();
		String myname =  bunn.getString("name");
		String mynumber = bunn.getString("number");
		EditText ediname = (EditText) findViewById(R.id.ediname);
    	EditText edinum = (EditText) findViewById(R.id.edinum);
		ediname.setText(myname);
     	edinum.setText(mynumber);
     	
    	TastyToast.makeText(EditContact.this, myname, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);   
	}

}
