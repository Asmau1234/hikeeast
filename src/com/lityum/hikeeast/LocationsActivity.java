package com.lityum.hikeeast;

import java.io.IOException
;


import java.io.InputStream;
import java.util.HashMap;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

@SuppressWarnings("unused")
public class LocationsActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_locations);
		
		   
			FragmentTabHost mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
			mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
			mTabHost.addTab(
					mTabHost.newTabSpec("Information").setIndicator("Information"),
					DestinationsTab.class, null);
			mTabHost.addTab(mTabHost.newTabSpec("Locations")
					.setIndicator("Locations"), LocationsTab.class, null);
			mTabHost.addTab(
					mTabHost.newTabSpec("Our Partners").setIndicator("Our Partners"),
					CategoryTab.class, null);


		HashMap<String, Integer> list = new HashMap<String, Integer>();
		HashMap<String, Integer> infos = new HashMap<String, Integer>();
		Intent intent = LocationsActivity.this.getIntent();
		int map = (Integer) intent.getExtras().get("id");
		String feed = loadJSONFromAsset();

		try {
			JSONObject mJsonObj = new JSONObject(feed);
			final JSONArray jsonArray = mJsonObj.getJSONArray("destinations");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String name = jsonObject.getString("name");
				int destId = jsonObject.getInt("id");
				String info = jsonObject.getString("string_info");
				infos.put(info, destId);
				list.put(name, destId);
			}
		}

		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Entry<String, Integer> entry : list.entrySet()) {
			String key = entry.getKey();
			Integer value = entry.getValue();
			if (map == value) {
				 setTitle(key);
			}
			else
			{
			    
			}
				

			}
		
	
		
	}

	/*public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
 
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
 
        return super.onCreateOptionsMenu(menu);
    }
	public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
        case R.id.action_search:
            // search action
            return true;
       
        default:
            return super.onOptionsItemSelected(item);
        }
    }*/

	
	public String loadJSONFromAsset() {
		String json;
		try {

			InputStream is = LocationsActivity.this.getAssets()
					.open("destinations.json");
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			json = new String(buffer, "UTF-8");

		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
		return json;

	}

}
