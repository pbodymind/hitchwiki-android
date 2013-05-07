package org.gcourses.android.hitchwiki;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.*;

public class DataBaseHelper extends SQLiteOpenHelper {
	private static String DB_PATH = "/data/data/org.gcourses.android.hitchwiki/databases/";
	private static String DB_NAME = "spots.db";
	private SQLiteDatabase myDataBase;
	private final Context myContext;

	public DataBaseHelper (Context context) {
		super(context, DB_NAME, null, 1);
		this.myContext = context;
	}

	public void createDatabase() throws IOException {
		boolean dbExist = checkDataBase();
		if (!dbExist) {
			this.getReadableDatabase();
			try {
				copyDataBase();
			} catch (IOException e) {
				throw new Error ("Error copying database!");
			}
		}
	}
	
	private boolean checkDataBase() {
		SQLiteDatabase checkDB = null;
		try {
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
		} catch (SQLiteException e) {
			// db does not exit
		}
		
		if (checkDB != null) {
			checkDB.close();
		}
		
		return checkDB != null ? true : false;
	}
	
	private void copyDataBase() throws IOException {
		InputStream myInput = myContext.getAssets().open(DB_NAME);
		String outFileName = DB_PATH + DB_NAME;
		OutputStream myOutput = new FileOutputStream(outFileName);
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer))>0) {
			myOutput.write(buffer, 0, length);
		}
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}
	
	public void openDataBase() throws SQLException {
		String myPath = DB_PATH + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
	}
	
	public SQLiteDatabase openDataBase2() throws SQLException {
		String myPath = DB_PATH + DB_NAME;
		return myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public synchronized void close() {
		if (myDataBase != null)
			myDataBase.close();
		super.close();
	}
	
	public Cursor filterList(String value) {
		Cursor search = myDataBase.rawQuery("SELECT location, _id FROM spots WHERE location LIKE '%"+value+"%' ORDER BY location ASC", null);
		return search;
	}
}
