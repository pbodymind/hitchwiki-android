package org.gcourses.android.hitchwiki;

import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Mapview extends MapActivity {

	MapController mc;
    GeoPoint p;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		
		MapView mapView = (MapView) findViewById(R.id.map_view);
		mapView.setStreetView(true);
		mapView.setBuiltInZoomControls(true);
		
		String spot_id = "";
		Bundle extras = getIntent().getExtras(); 
		if(extras != null) {
			spot_id = extras.getString("spot_id");
		}
		
	       // database
        DataBaseHelper myDbHelper = new DataBaseHelper(this);
        myDbHelper = new DataBaseHelper(this);
 

        try {
        	SQLiteDatabase sqliteDB = myDbHelper.openDataBase2();   	
        	Cursor c = sqliteDB.rawQuery("SELECT location, description, gps_long, gps_lat FROM spots WHERE _id = '" + spot_id + "' LIMIT 1", null);
        	c.moveToFirst();
            
            TextView location = (TextView) findViewById(R.id.City01);
            location.setText(c.getString(c.getColumnIndex("location")));
            
            TextView desc = (TextView) findViewById(R.id.Description01);
            desc.setText(c.getString(c.getColumnIndex("description")));
        	
            mc = mapView.getController();
            String coordinates[] = {c.getString(c.getColumnIndex("gps_lat")),
            		c.getString(c.getColumnIndex("gps_long"))};
            double lat = Double.parseDouble(coordinates[0]);
            double lng = Double.parseDouble(coordinates[1]);
            p = new GeoPoint((int)(lat * 1E6), (int)(lng * 1E6));
            
            List<Overlay> mapOverlays = mapView.getOverlays();
            Drawable drawable = this.getResources().getDrawable(R.drawable.mmarker);
            CustomItemizedOverlay itemizedOverlay = new CustomItemizedOverlay(drawable, this);
            
            OverlayItem overlayitem = new OverlayItem(p, "Hello", "I'm in Athens, Greece!");
            
            itemizedOverlay.addOverlay(overlayitem);
            mapOverlays.add(itemizedOverlay);
           
            mc.animateTo(p);
            mc.setZoom(17); 
            mapView.invalidate();
            
        }catch(SQLException sqle){
        	throw sqle;
        }
        
		Button next = (Button) findViewById(R.id.Button02);
		next.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}
	
	@Override
	protected boolean isRouteDisplayed() {
	    return false;
	}
}
