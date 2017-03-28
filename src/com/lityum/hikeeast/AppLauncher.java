package com.lityum.hikeeast;


import com.lityum.main.MainActivity;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.widget.ImageView;

public class AppLauncher extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_launcher);
		
		ImageView imageview= (ImageView) findViewById(R.id.imageViewLauncher);
		imageview.setImageResource(R.drawable.hikeeastguidebook);
		setTitle("Welcome");
		
		new Handler().postDelayed(new Runnable() {
	        @Override
	        public void run() {
	            final Intent mainIntent = new Intent(AppLauncher.this, MainActivity.class);
	            AppLauncher.this.startActivity(mainIntent);
	            AppLauncher.this.finish();
	        }
	    }, 3000);
		
		
		
	}

	

}
