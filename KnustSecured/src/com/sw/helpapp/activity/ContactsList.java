package com.sw.helpapp.activity;

import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListView;

public class ContactsList extends ListActivity {
	ListView lv_contacts;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		lv_contacts = getListView();

	}
}
