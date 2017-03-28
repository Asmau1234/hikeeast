package com.lityum.hikeeast;

import java.io.IOException;


import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lityum.main.Locations;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class LocationItemsInfo extends Fragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment

		View V = inflater.inflate(R.layout.location_items_info, container,
				false);

		Intent intent = getActivity().getIntent();
		String Locationname = (String) intent.getExtras().get("Locationname");
		Locations locationinfo = new Locations();

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

				locationinfo.setName(name);
				locationinfo.setDestionationId(destination_id);
				locationinfo.setInfo(info);
				locationinfo.setId(unique_id);
				locationinfo.setLang(lng);
				locationinfo.setLat(lat);

				if (Locationname.equals(locationinfo.getName())) {

					TextView infoText = (TextView) V
							.findViewById(R.id.txtLocationInformation);
					infoText.setText(Html.fromHtml(locationinfo.getInfo()));

				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return V;
	}

	public String loadJSONFromAsset() {
		String json;
		try {

			InputStream is = getActivity().getAssets()
					.open("locationJSON.json");
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

	public String loadImages() {
		String json;
		try {

			InputStream is = getActivity().getAssets().open("images.json");
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
