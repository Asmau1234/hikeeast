package com.lityum.hikeeast;

import java.io.IOException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.common.GooglePlayServicesUtil;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lityum.adapters.CustomListViewAdapter;
import com.lityum.main.Locations;
import com.lityum.main.Partners;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

@SuppressWarnings("unused")
public class LocationItemsActivity extends FragmentActivity implements
		OnMarkerClickListener {
	private GoogleMap map;
	private LocationManager locationManager;
	private FragmentTabHost mTabHost;
	private Marker marker;

	@Override
	protected void onCreate(Bundle bundle) {

		super.onCreate(bundle);
		setContentView(R.layout.activity_location_items);
		GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getApplicationContext());

		ViewGroup mapHost = (ViewGroup) findViewById(R.id.bottomcenter);
		mapHost.requestTransparentRegion(mapHost);
		map = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.Locationmap)).getMap();
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(39.57,
				32.54), 5.0f));
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		map.setMyLocationEnabled(true);

		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
		mTabHost.addTab(
				mTabHost.newTabSpec("Location Info").setIndicator(
						"Location Info"), LocationItemsInfo.class, null);
		mTabHost.addTab(mTabHost.newTabSpec("Partners")
				.setIndicator("Partners"), LocationItemsPartners.class, null);
		mTabHost.addTab(mTabHost.newTabSpec("Gallery").setIndicator("Gallery"),
				LocationItemGallery.class, null);

		titleLocation();
		loadMarkers();

	}

	public void titleLocation() {

		Locations locationinfo = new Locations();
		Intent intent = LocationItemsActivity.this.getIntent();
		String Locationname = (String) intent.getExtras().get("Locationname");
		String feed = loadJSONFromAsset();
		try {
			JSONObject mJsonObj = new JSONObject(feed);
			JSONArray jsonArray = mJsonObj.getJSONArray("locations");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String name = jsonObject.getString("name");
				int destination_id = jsonObject.getInt("destination_id");
				String info = jsonObject.getString("string_info");
				int unique_id = jsonObject.getInt("id");
				Double lat = jsonObject.getDouble("lat");
				Double lng = jsonObject.getDouble("lang");
				;
				locationinfo.setName(name);
				locationinfo.setDestionationId(destination_id);
				locationinfo.setInfo(info);
				locationinfo.setId(unique_id);
				locationinfo.setLang(lng);
				locationinfo.setLat(lat);
				
				if (Locationname.equals(locationinfo.getName())) {

					setTitle(Html.fromHtml(locationinfo.getName()));

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void loadMarkers() {
		Intent intent1 = LocationItemsActivity.this.getIntent();
		Integer locationID = (Integer) intent1.getExtras().get("locationId");
		String partners = loadPartners();
		final Partners partner = new Partners();

		try {
			JSONObject mJsonObj = new JSONObject(partners);
			JSONArray jsonArray = mJsonObj.getJSONArray("partners");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String name = jsonObject.getString("name");
				String address = jsonObject.getString("address");
				String thumbnail_image = jsonObject
						.getString("thumbnail_image");
				int locname = jsonObject.getInt("location_name");
				Double lat = jsonObject.getDouble("lat");
				Double lng = jsonObject.getDouble("lang");
				partner.setName(name);
				partner.setAddress(address);
				partner.setThumbnailimage(thumbnail_image);
				partner.setLocationName(locname);
				partner.setLat(lat);
				partner.setLang(lng);

				if (locationID == partner.getLocationName()) {

					marker = map.addMarker(new MarkerOptions().position(
							new LatLng(partner.getLat(), partner.getLang()))
							.title(Html.fromHtml(partner.getName()).toString()));
					map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
						@Override
						public void onInfoWindowClick(Marker markers) {

							Intent intent = new Intent(LocationItemsActivity.this,
									MarkerClickActivity.class);

							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							intent.putExtra("partner", markers.getTitle());
							startActivity(intent);
							
							

						}
					});


				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*
	 * public boolean onCreateOptionsMenu(Menu menu) { MenuInflater inflater =
	 * getMenuInflater(); inflater.inflate(R.menu.main, menu);
	 * 
	 * // Associate searchable configuration with the SearchView SearchManager
	 * searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	 * SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
	 * .getActionView(); searchView.setSearchableInfo(searchManager
	 * .getSearchableInfo(getComponentName()));
	 * 
	 * return super.onCreateOptionsMenu(menu); }
	 * 
	 * public boolean onOptionsItemSelected(MenuItem item) { // Take appropriate
	 * action for each action item click switch (item.getItemId()) { case
	 * R.id.action_search: // search action return true;
	 * 
	 * default: return super.onOptionsItemSelected(item); }
	 * 
	 * }
	 */

	public String loadJSONFromAsset() {
		String json;
		try {

			InputStream is = LocationItemsActivity.this.getAssets().open(
					"locationJSON.json");
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

	public String loadPartners() {
		String json;
		try {

			InputStream is = LocationItemsActivity.this.getAssets().open(
					"partnersJSON.json");
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

	@Override
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
		return false;
	}

}
