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
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DestinationsTab extends Fragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View V = inflater.inflate(R.layout.activity_destinations_tab,
				container, false);
		Intent intent = getActivity().getIntent();
		int map = (Integer) intent.getExtras().get("id");
		String feed = loadJSONFromAsset();
		Locations location = new Locations();

		try {
			JSONObject mJsonObj = new JSONObject(feed);
			final JSONArray jsonArray = mJsonObj.getJSONArray("destinations");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String name = jsonObject.getString("name");
				int destId = jsonObject.getInt("id");
				String info = jsonObject.getString("string_info");
				location.setName(name);
				location.setId(destId);
				location.setInfo(info);
				JSONArray Jsonimages = jsonObject.getJSONArray("images");
				for (int j = 0; j < Jsonimages.length(); j++) {
					JSONObject image = Jsonimages.getJSONObject(j);
					String imagess = image.getString("image");
					location.setImages(imagess);
					if (map == location.getId()) {
						TextView infoText = (TextView) V
								.findViewById(R.id.txtInformation);
						infoText.setText(Html.fromHtml(location.getInfo()));
						String photo = location.getImages();
						int resID = getResources().getIdentifier(photo,
								"drawable", getActivity().getPackageName());
						LinearLayout linearLayout1 = (LinearLayout) V
								.findViewById(R.id.destinationsImages);
						Bitmap bitmapOrg = BitmapFactory.decodeResource(
								getResources(), resID);

						int width = bitmapOrg.getWidth();
						int height = bitmapOrg.getHeight();
						int newWidth = 300;
						int newHeight = 300;

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

							}
						});

					}

				}

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return V;

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
