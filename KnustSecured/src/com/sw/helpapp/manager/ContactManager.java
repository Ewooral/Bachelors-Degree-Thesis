package com.sw.helpapp.manager;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.sw.helpapp.classes.Contact;
import com.sw.helpapp.db.ContactDb;

public class ContactManager {

	ContactDb h_db;
	SQLiteDatabase rdb;
	SQLiteDatabase wdb;
	
	public ContactManager(Context ctx) {
		// TODO Auto-generated constructor stub
		h_db = new ContactDb(ctx, ContactDb.DB_NAME, null, ContactDb.VERSION);
		rdb = h_db.getReadableDatabase();
		wdb = h_db.getWritableDatabase();
	}
	
	public boolean ADDContact(Contact c){
		
		ContentValues cv = new ContentValues();
		cv.put("name", c.getName());
		cv.put("phone", c.getPhone());
		
		long res =wdb.insert("contact", null, cv);
		
		if(res==-1){
			return false;
			
		}else{
			return true;
		}
	}
	
	public ArrayList<Contact> GetPhone(){
		ArrayList<Contact> contacts = new ArrayList<Contact>();
		Cursor cs  = rdb.query("contact", new String[]{"*"}, null, null, null, null, null);
	
			while(cs.moveToNext()){
				contacts.add(new Contact(cs.getString(0),cs.getString(1)));
			    
			}
	
		
		
		return contacts;
}
	public Contact GetMyPhone(String name)
	{    Contact myphone = new Contact();
		Cursor result = rdb.rawQuery("SELECT COUNT(*) AS MyCount FROM CONTACT WHERE NAME = '" + name + "'", null);
		result.moveToFirst();
		
		if  (result.getInt(0) != 0)
		{
			myphone.setName(result.getColumnName(0));
			myphone.setPhone(result.getColumnName(1));
			
			return myphone;
		}
		else 
			return null;
		
		
	}
	
	
	public boolean LoginCheck(String agentnum, String username, String password) {
		
	Cursor result = rdb.rawQuery("SELECT COUNT(*) AS MyCount FROM AGENT WHERE AgentNum = '" + agentnum + "' AND Username = '" + username + "' AND Password = '" + password + "'", null);

		result.moveToFirst();
		
		if (result.getInt(0) != 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	
	
	
	  public boolean DeleteContact(String myphone)
	  {
		 
		   long res = wdb.delete("contact", "phone=?", new String[]{myphone+""});
		   if (res == -1)
		   
			   return false;
		   else 
			   return true;
		  
	  }
	 
}
