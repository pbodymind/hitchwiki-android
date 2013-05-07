package org.gcourses.android.hitchwiki;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class Listview extends Activity{
    /** Called when the activity is first created. */
   
	private DataBaseHelper myDbHelper;
	private SimpleCursorAdapter adapter;
	private ListView listview;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
               
        // database
        myDbHelper = new DataBaseHelper(this);
        myDbHelper = new DataBaseHelper(this);
 
        try {
        	myDbHelper.createDatabase();
        } catch (IOException ioe) {
        	throw new Error("Unable to create database");
        }
 
        try {
        	SQLiteDatabase sqliteDB = myDbHelper.openDataBase2();
        	Cursor crsr = sqliteDB.rawQuery("SELECT location, _id FROM spots WHERE 1 ORDER BY location ASC", null);     	
        	adapter = new SimpleCursorAdapter (
                    this,
                    R.layout.list_item,
                    crsr,
                    new String[] {"location", "_id"},
                    new int[] {R.id.text1, R.id.spots_id});
        	
        	listview = (ListView)findViewById(R.id.listItems);
        
        	listview.setOnItemClickListener(new OnItemClickListener() {
   						public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
							Cursor c = (Cursor) listview.getItemAtPosition(arg2);
							int row_id = c.getInt(c.getColumnIndex("_id"));
							Intent myIntent = new Intent(arg1.getContext(), Mapview.class);
							myIntent.putExtra("spot_id", new Integer(row_id).toString());
							startActivityForResult(myIntent, 0);
						}   
        	        }       
        	);
        	
        	listview.setAdapter(adapter);
            
            adapter.setFilterQueryProvider(new FilterQueryProvider() {
                public Cursor runQuery(CharSequence constraint) {
                    String partialValue = constraint.toString();
                    return myDbHelper.filterList(partialValue);
                }
            });
            
            EditText searchedit = (EditText)findViewById(R.id.search_edit);
            searchedit.addTextChangedListener(new TextWatcher() {
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
                public void beforeTextChanged(CharSequence s, int start, int count,
                        int after) {
                }
                public void afterTextChanged(Editable s) {
                	adapter.getFilter().filter(s.toString());
                }
            });
        }catch(SQLException sqle){
        	throw sqle;
        }

    }
}