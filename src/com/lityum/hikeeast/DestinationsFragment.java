package com.lityum.hikeeast;

import java.io.IOException;

import java.io.Serializable;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.lityum.adapters.HashMapAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

@SuppressLint("ValidFragment")
@SuppressWarnings("unused")
public class DestinationsFragment extends Fragment implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GoogleMap map;
	private String name;
	int destId;

	final HashMap<String, Integer> list = new HashMap<String, Integer>();

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_destinations,
				container, false);

		final Context context = getActivity();

		FragmentActivity activity = (FragmentActivity) getActivity();
		map = ((SupportMapFragment) activity.getSupportFragmentManager()
				.findFragmentById(R.id.Destinationsmap)).getMap();
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(39.57, 32.54), 5.0f));
		map.setMyLocationEnabled(true);

		
		final ListView listview = (ListView) rootView.findViewById(R.id.list);
		String feed = loadJSONFromAsset();

		try {
			JSONObject mJsonObj = new JSONObject(feed);
			final JSONArray jsonArray = mJsonObj.getJSONArray("destinations");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				name = jsonObject.getString("name");
				destId = jsonObject.getInt("id");

				list.put(name, destId);
			}
			HashMapAdapter adapter = new HashMapAdapter(getActivity(),
					R.layout.list_item, list);
			listview.setAdapter(adapter);
			listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					@SuppressWarnings("unchecked")
					Entry<String, Integer> selectedItem = (Entry<String, Integer>) parent
							.getItemAtPosition(position);
					Intent i = new Intent(getActivity(),
							LocationsActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					i.putExtra("id", selectedItem.getValue());
					startActivity(i);

				}
			});

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rootView;
	}

	public void onDestroyView() {
		SupportMapFragment f = (SupportMapFragment) getActivity()
				.getSupportFragmentManager().findFragmentById(
						R.id.Destinationsmap);
		if (f.isResumed()) {
			getFragmentManager().beginTransaction().remove(f).commit();
		}

		super.onDestroy();
	}

	public String loadJSONFromAsset() {
		String json;
		try {

			InputStream is = getActivity().getAssets()
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
