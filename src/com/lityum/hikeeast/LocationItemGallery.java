package com.lityum.hikeeast;

import java.io.IOException;

import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lityum.main.Locations;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class LocationItemGallery extends Fragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment

		View V = inflater.inflate(R.layout.location_items_gallery, container,
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
				JSONArray images = jsonObject.getJSONArray("images");
				for (int j = 0; j < images.length(); j++) {
					JSONObject image = images.getJSONObject(j);
					String imagess = image.getString("image");
					locationinfo.setImages(imagess);

					if (Locationname.equals(locationinfo.getName())) {
						String photo = locationinfo.getImages();
						int resID = getResources().getIdentifier(photo,
								"drawable", getActivity().getPackageName());
						LinearLayout linearLayout1 = (LinearLayout) V
								.findViewById(R.id.locationImages);
						Bitmap bitmapOrg = BitmapFactory.decodeResource(
								getResources(), resID);

						int width = bitmapOrg.getWidth();
						int height = bitmapOrg.getHeight();
						int newWidth = 400;
						int newHeight = 400;

						float scaleWidth = ((float) newWidth) / width;
						float scaleHeight = ((float) newHeight) / height;

						Matrix matrix = new Matrix();

						matrix.postScale(scaleWidth, scaleHeight);
						matrix.postRotate(0);

						Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg,
								0, 0, width, height, matrix, true);
						BitmapDrawable bmd = new BitmapDrawable(getResources(),
								resizedBitmap);

						ImageView imageView = new ImageView(getActivity());
						imageView.setPadding(2, 0, 9, 5);
						imageView.setImageDrawable(bmd);
						linearLayout1.addView(imageView);

						imageView.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {

								/*Intent fullScreenIntent = new Intent(v
										.getContext(), FullImage.class);
								
								 fullScreenIntent.putExtra(LocationItemsInfo.class
								 .getName(), imageId);
								 

								LocationItemsInfo.this
										.startActivity(fullScreenIntent);*/

							}
						});
					}

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
