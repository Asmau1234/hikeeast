package com.lityum.hikeeast;

import java.io.IOException;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;

import com.lityum.adapters.HashMapAdapter;

import android.os.Bundle;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

public class LocationsTab extends Fragment {

	@SuppressWarnings({ "unchecked" })
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View V = inflater.inflate(R.layout.activity_locations_tab, container,
				false);
		ListView listview = (ListView) V.findViewById(R.id.listviewlocation);
		HashMap<String, Integer> iteratedList = new HashMap<String, Integer>();
		HashMap<String, Integer> list = new HashMap<String, Integer>();
		HashMap<String, Integer> ids = new HashMap<String, Integer>();
		Intent intent = getActivity().getIntent();
		int map = (Integer) intent.getExtras().get("id");
		String feed = loadJSONFromAsset();

		try {
			JSONObject mJsonObj = new JSONObject(feed);
			JSONArray jsonArray = mJsonObj.getJSONArray("locations");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String name = jsonObject.getString("name");
				int destination_id = jsonObject.getInt("destination_id");
				int id = jsonObject.getInt("id");
				list.put(name, destination_id);
				ids.put(name, id);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		for (Entry<String, Integer> entry : list.entrySet()) {
			String key = entry.getKey();
			Integer value = entry.getValue();
			for (Entry<String, Integer> entry1 : ids.entrySet()) {
				String key1 = entry1.getKey();
				Integer value1 = entry1.getValue();
				if (map == value && key.equals(key1)) {
					iteratedList.put(key, value1);
					HashMapAdapter adapter = new HashMapAdapter(getActivity(),
							R.layout.list_item, iteratedList);
					listview.setAdapter(adapter);
					listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							Entry<String, Integer> selectedItem = (Entry<String, Integer>) parent
									.getItemAtPosition(position);
							Intent i = new Intent(getActivity(),
									LocationItemsActivity.class);
							i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							i.putExtra("Locationname", selectedItem.getKey());
							i.putExtra("locationId", selectedItem.getValue());
							startActivity(i);

						}
					});
				}
			}

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

}
