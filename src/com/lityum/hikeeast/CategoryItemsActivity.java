package com.lityum.hikeeast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lityum.adapters.CustomListViewAdapter;
import com.lityum.adapters.HashMapAdapter;
import com.lityum.adapters.RowItem;
import com.lityum.main.Partners;

@SuppressWarnings("unused")
public class CategoryItemsActivity extends FragmentActivity {
	private GoogleMap map;
	private LocationManager locationManager;

	@Override
	protected void onCreate(Bundle bundle) {

		super.onCreate(bundle);
		setContentView(R.layout.activity_category_items);
		GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getApplicationContext());
		populatePartners();

	}

	

	public void populatePartners() {

		map = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.categoriesMap)).getMap();
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(39.57,
				32.54), 5.0f));
		
		map.setMyLocationEnabled(true);

		List<RowItem> rowItems;
		List<String> names = new ArrayList<String>();
		List<String> addresses = new ArrayList<String>();
		List<Integer> images = new ArrayList<Integer>();
		ListView listview = (ListView) findViewById(R.id.Listcategories);
		Intent intent = CategoryItemsActivity.this.getIntent();
		String category = (String) intent.getExtras().get("categoryName");
		int dest_id = (Integer) intent.getExtras().get("destinationID");
		String feed = loadJSONFromAsset();
		Partners partner = new Partners();
		try {
			JSONObject mJsonObj = new JSONObject(feed);
			JSONArray jsonArray = mJsonObj.getJSONArray("partners");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String name = jsonObject.getString("name");
				String categoryname = jsonObject.getString("category_name");
				Double lat = jsonObject.getDouble("lat");
				Double lng = jsonObject.getDouble("lang");
				String address = jsonObject.getString("address");
				int destid = jsonObject.getInt("destination_id");
				String thumbnail_image = jsonObject
						.getString("thumbnail_image");
				partner.setName(name);
				partner.setAddress(address);
				partner.setThumbnailimage(thumbnail_image);
				partner.setDestinationId(destid);
				partner.setCategoryname(categoryname);
				partner.setLat(lat);
				partner.setLang(lng);
				
				if (dest_id == partner.getDestinationId() && category.equals(partner.getCategoryname())) {
					names.add(partner.getName());
					setTitle(Html.fromHtml(partner.getCategoryname()));
					addresses.add(Html.fromHtml(partner.getAddress()).toString());
					int resID = getResources().getIdentifier(
							partner.getThumbnailimage(), "drawable",
							CategoryItemsActivity.this.getPackageName());
					images.add(resID);
					Marker marker = map.addMarker(new MarkerOptions().position(
							new LatLng(partner.getLat(), partner.getLang()))
							.title(Html.fromHtml(partner.getName()).toString()));
					map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
						@Override
						public void onInfoWindowClick(Marker markers) {

							Intent intent = new Intent(CategoryItemsActivity.this,
									MarkerClickActivity.class);

							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							intent.putExtra("partner", markers.getTitle());
							startActivity(intent);
							
							

						}
					});

					rowItems = new ArrayList<RowItem>();
					for (int x = 0; x < names.size(); x++) {
						RowItem item = new RowItem(images.get(x), names.get(x),
								addresses.get(x));
						rowItems.add(item);
					}
					CustomListViewAdapter adapter1 = new CustomListViewAdapter(
							this, R.layout.list_item_2, rowItems);
					listview.setAdapter(adapter1);
					listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							RowItem name = (RowItem) parent
									.getItemAtPosition(position);
							Intent i = new Intent(CategoryItemsActivity.this,
									PartnersInfo.class);
							i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							i.putExtra("PartnerName", name.toString());
							startActivity(i);

						}
					});

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
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

			InputStream is = CategoryItemsActivity.this.getAssets().open(
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

}
