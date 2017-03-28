package com.lityum.hikeeast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lityum.adapters.CustomListViewAdapter;
import com.lityum.adapters.RowItem;
import com.lityum.main.Partners;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

@SuppressWarnings("unused")
public class LocationItemsPartners extends Fragment {

	private GoogleMap map;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment

		View V = inflater.inflate(R.layout.location_items_partners, container,
				false);

		ListView listview = (ListView) V.findViewById(R.id.locationItemsLV);
		List<Integer> thumb_images = new ArrayList<Integer>();
		List<RowItem> rowItems;
		List<String> names = new ArrayList<String>();
		List<String> addresses = new ArrayList<String>();
		Intent intent = getActivity().getIntent();
		Integer locationID = (Integer) intent.getExtras().get("locationId");
		String partners = loadPartners();
		Partners partner = new Partners();

		try {
			JSONObject mJsonObj = new JSONObject(partners);
			JSONArray jsonArray = mJsonObj.getJSONArray("partners");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String name = jsonObject.getString("name");
				String address = jsonObject.getString("address");
				String phone=jsonObject.getString("phone");
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

					names.add(partner.getName());
					addresses.add(Html.fromHtml(partner.getAddress()).toString());
					int resID = getResources().getIdentifier(
							partner.getThumbnailimage(), "drawable",
							getActivity().getPackageName());
					thumb_images.add(resID);

					rowItems = new ArrayList<RowItem>();
					for (int x = 0; x < names.size(); x++) {
						RowItem item = new RowItem(thumb_images.get(x),
								names.get(x), addresses.get(x));
						rowItems.add(item);
					}
					CustomListViewAdapter adapter1 = new CustomListViewAdapter(
							getActivity(), R.layout.list_item_2, rowItems);
					listview.setAdapter(adapter1);
					listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							RowItem name = (RowItem) parent
									.getItemAtPosition(position);
							Intent i = new Intent(getActivity(),
									PartnersInfo.class);
							i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							i.putExtra("PartnerName", name.toString());
							startActivity(i);

						}
					});
					

				}

			}

		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return V;
	}

	public String loadPartners() {
		String json;
		try {

			InputStream is = getActivity().getAssets()
					.open("partnersJSON.json");
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
