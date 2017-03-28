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

public class CategoryTab extends Fragment {

	@SuppressWarnings({ "unchecked", "unused" })
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View V = inflater.inflate(R.layout.activity_category_tab, container,
				false);
		ListView listview = (ListView) V.findViewById(R.id.listviewCategories);
		HashMap<String, Integer> iteratedList = new HashMap<String, Integer>();
		HashMap<String, Integer> list = new HashMap<String, Integer>();
		Intent intent = getActivity().getIntent();
		int map = (Integer) intent.getExtras().get("id");
		String feed = loadJSONFromAsset();

		try {
			JSONObject mJsonObj = new JSONObject(feed);
			JSONArray jsonArray = mJsonObj.getJSONArray("categories");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String name = jsonObject.getString("name");
				list.put(name, map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		HashMapAdapter adapter = new HashMapAdapter(getActivity(),
				R.layout.list_item, list);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Entry<String, Integer> selectedItem = (Entry<String, Integer>) parent
						.getItemAtPosition(position);
				Intent i = new Intent(getActivity(),
						CategoryItemsActivity.class);
				i.putExtra("categoryName", selectedItem.getKey());
				i.putExtra("destinationID", selectedItem.getValue());
				startActivity(i);

			}

		});

		return V;
	}

	public String loadJSONFromAsset() {
		String json;
		try {

			InputStream is = getActivity().getAssets().open(
					"categoriesJSON.json");
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
