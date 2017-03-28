package com.lityum.hikeeast;


import android.os.Bundle;


import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AboutUsFragment extends Fragment{
	 
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	 
	        View rootView = inflater.inflate(R.layout.fragment_aboutus, container, false);
	        String text = getResources().getString(R.string.aboutus);
			TextView aboutus = (TextView) rootView.findViewById(R.id.txtabout_us);
			aboutus.setText(Html.fromHtml(text));
			aboutus.setTextSize(14);
	        return rootView;
	    }
	
}
