package com.lityum.hikeeast;

import java.io.IOException;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lityum.main.Partners;

import android.location.LocationManager;
import android.os.Bundle;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

@SuppressWarnings("unused")
public class MarkerClickActivity extends FragmentActivity {

	private GoogleMap map;
	private LocationManager locationManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_partners_info);

		GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getApplicationContext());

		ViewGroup mapHost = (ViewGroup) findViewById(R.id.top);
		mapHost.requestTransparentRegion(mapHost);
		map = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.PartnersMap)).getMap();
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(39.57,
				32.54), 3.0f));
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		map.setMyLocationEnabled(true);
		populatePartnersInfo();

	}

	public void populatePartnersInfo() {

		Intent intent = MarkerClickActivity.this.getIntent();
		String partner = (String) intent.getExtras().get("partner");
		String feed = loadJSONFromAsset();
		Partners partners = new Partners();
		try {
			JSONObject mJsonObj = new JSONObject(feed);
			JSONArray jsonArray = mJsonObj.getJSONArray("partners");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String name = jsonObject.getString("name");
				String address = jsonObject.getString("address");
				String phone=jsonObject.getString("phone");
				String info = jsonObject.getString("string_info");
				String thumbnail_image = jsonObject
						.getString("thumbnail_image");
				Double lat = jsonObject.getDouble("lat");
				Double lng = jsonObject.getDouble("lang");
				partners.setName(name);
				partners.setAddress(address);
				partners.setThumbnailimage(thumbnail_image);
				partners.setLat(lat);
				partners.setLang(lng);
				partners.setStringInfo(info);
				partners.setPhone(phone);

				if (partner.equals(partners.getName())) {

					TextView phoneTxt = (TextView) findViewById(R.id.partnerPhone);
					phoneTxt.setText("Phone No: "+partners.getPhone());
					setTitle(Html.fromHtml(partners.getName()));
					TextView addres = (TextView) findViewById(R.id.partnerAddress);
					addres.setText(Html.fromHtml("Address: "+partners.getAddress()));
					TextView infotext = (TextView) findViewById(R.id.partnerInfo);
					infotext.setText(Html.fromHtml(partners.getStringInfo()));
					Marker marker = map.addMarker(new MarkerOptions().position(
							new LatLng(partners.getLat(), partners.getLang()))
							.title(partners.getName()));
					String img=partners.getThumbnailimage();
					int resID = getResources().getIdentifier(
							img, "drawable",
							MarkerClickActivity.this.getPackageName());
					LinearLayout linearLayout1 = (LinearLayout) findViewById(R.id.partnerImages);
					Bitmap bitmapOrg = BitmapFactory.decodeResource(
							getResources(), resID);

					int width = bitmapOrg.getWidth();
					int height = bitmapOrg.getHeight();
					int newWidth = 200;
					int newHeight = 200;

					float scaleWidth = ((float) newWidth) / width;
					float scaleHeight = ((float) newHeight) / height;

					Matrix matrix = new Matrix();

					matrix.postScale(scaleWidth, scaleHeight);
					matrix.postRotate(0);

					Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0,
							width, height, matrix, true);
					BitmapDrawable bmd = new BitmapDrawable(getResources(),
							resizedBitmap);

					ImageView imageView = new ImageView(MarkerClickActivity.this);
					imageView.setPadding(2, 0, 9, 5);
					imageView.setImageDrawable(bmd);
					linearLayout1.addView(imageView);

					imageView.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {

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

			InputStream is = MarkerClickActivity.this.getAssets().open(
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
