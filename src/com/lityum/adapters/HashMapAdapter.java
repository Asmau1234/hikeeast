package com.lityum.adapters;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import com.lityum.hikeeast.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

@SuppressWarnings( { "rawtypes", "unchecked", "unused" })
public class HashMapAdapter extends BaseAdapter {
	
	private final ArrayList mData;
	private Context context;
	private int resource;

	public HashMapAdapter(Context context, int resId, Map<String, Integer> map) {
		this.context = context;
	    this.resource = resId;
		mData = new ArrayList();
		mData.addAll(map.entrySet());
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Map.Entry<String, Integer> getItem(int position) {
		// TODO Auto-generated method stub
		return (Entry<String, Integer>) mData.get(position);

	}

	@Override
	public long getItemId(int position) {
		// TODO implement you own logic with ID
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View result;
       
		if(convertView==null){
        	result = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        }
		else {
			result=convertView;
		}
        Map.Entry<String, Integer> item = getItem(position);
       ((TextView) result.findViewById(R.id.listTextname)).setText(item.getKey());

      
        return result;
	}

}