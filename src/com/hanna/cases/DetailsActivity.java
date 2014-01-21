package com.hanna.cases;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class DetailsActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			// If the screen is now in landscape mode, we don't need this activity
			finish();
			return;
		}

		if (savedInstanceState == null) {

			this.getActionBar().setDisplayShowCustomEnabled(true);
			this.getActionBar().setDisplayShowTitleEnabled(false);

			LayoutInflater inflator = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = inflator.inflate(R.layout.arialtextview, null);

			//if you need to customize anything else about the text, do it here.
			//I'm using a custom TextView with a custom font in my layout xml so all I need to do is set title
			((TextView)v.findViewById(android.R.id.text1)).setText(this.getTitle());

			//assign the view to the actionbar
			this.getActionBar().setCustomView(v);
			
			// create fragment
			DetailsFragment details = new DetailsFragment();

			// get and set the position input by user (i.e., "index")
			details.setArguments(getIntent().getExtras());

			getFragmentManager().beginTransaction()
					.add(android.R.id.content, details).commit();
		}
	}
}
