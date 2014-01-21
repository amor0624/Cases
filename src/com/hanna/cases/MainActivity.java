package com.hanna.cases;

import java.util.ArrayList;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MainActivity extends Activity implements
        ActionBar.OnNavigationListener {
	ArrayList<JSONObject> list;
	CaseApplication lawCaseApp;
	int[] actionbarLoc = new int[2];
	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_layout);

		lawCaseApp = (CaseApplication) getApplication();

		// Set up the action bar to show a dropdown list.
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		
		String[] navigations = getResources().getStringArray(R.array.actionBar);
		DropDownAdapter adapter = new DropDownAdapter(
		        getActionBarThemedContextCompat(), R.layout.arialtextview, 
		        android.R.id.text1, navigations);
		adapter.setDropDownViewResource(R.layout.dropdown_row);
		actionBar.setListNavigationCallbacks(adapter, this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * Backward-compatible version of {@link ActionBar#getThemedContext()} that
	 * simply returns the {@link android.app.Activity} if
	 * <code>getThemedContext</code> is unavailable.
	 */
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private Context getActionBarThemedContextCompat() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			return getActionBar().getThemedContext();
		} else {
			return this;
		}
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current dropdown position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
			        savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current dropdown position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
		        .getSelectedNavigationIndex());
	}

	@Override
	public boolean onNavigationItemSelected(int position, long id) {
		// When the given dropdown item is selected, show its contents in the
		// container view.
		if (position == 0) {
			lawCaseApp.sortByDN();
		} else {
			lawCaseApp.sortByTime();
		}

		TitlesFragment fragment = new TitlesFragment();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.container, fragment, "titleFrag");
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.commit();
		return true;
	}


	class DropDownAdapter extends ArrayAdapter<String> {
		public DropDownAdapter(Context context, int resource,
		        int textViewResourceId, String[] navigations) {
			super(context, resource, textViewResourceId, navigations);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = super.getView(position, convertView, parent);
			TextView text = (TextView) view.findViewById(android.R.id.text1);
			//sets the title of options
			text.setText("OPEN MATTERS");
			return view;
		}

		@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
		@Override
		public View getDropDownView(int position, View convertView,
		        ViewGroup parent) {
			
			//gets the row created from parent, using custom styles in styles.xml
			View row = super.getDropDownView(position, convertView, parent);
			
			//find the full screen width sets the width of the dropdown row to full screen
			Display display = getWindowManager().getDefaultDisplay();
			int width;
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
				Point size = new Point();
				display.getRealSize(size);
				width = size.x;
			} else {
				DisplayMetrics metrics = new DisplayMetrics();
				display.getMetrics(metrics);
				width = metrics.widthPixels;
			}
			row.setMinimumWidth(width);
			
			TextView text = (TextView) row.findViewById(android.R.id.text1);
			//TODO: find a better mechanism to find out the x-coord. of the action bar location
			// and text textView should be set to that location
			text.setX(70);
			return row;
		}

	}
}
