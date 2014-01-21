package com.hanna.cases;

import java.util.ArrayList;
import org.json.JSONObject;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class TitlesFragment extends ListFragment {
	static final String TAG = "TitlesFragment";
	boolean mDualPane;
	int mCurCheckPosition = 0;
	private CustomAdapter adapter;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		Log.d(TAG, "TitlesFragment:onActivityCreated");
		adapter = new CustomAdapter(getActivity(), R.layout.title_row,
				 ((CaseApplication) getActivity().getApplication())
				 .getJArray());
		// Populate list with jsonobject array, title with its display_name
		 setListAdapter(adapter);
		 
		 
		View detailsFrame = getActivity().findViewById(R.id.details);

		Log.d(TAG, "detailsFrame " + detailsFrame);
		
		//in dualPane if detail frame exists and isVisible
		mDualPane = detailsFrame != null
		        && detailsFrame.getVisibility() == View.VISIBLE;

		Log.d(TAG, "mDualPane " + mDualPane);

		if (savedInstanceState != null) {
			// Restore last state for checked position.
			mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
		}

		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
		if (mDualPane) {
			// Make sure our UI is in the correct state.
			showDetails(mCurCheckPosition);
		} 
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.d(TAG, "onSaveInstanceState");

		outState.putInt("curChoice", mCurCheckPosition);
	}

	//opens up the proper item detail
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

		Log.d(TAG, "onListItemClick position is" + position);
		mCurCheckPosition = position;
		l.setSelection(position);
		showDetails(position);
		adapter.notifyDataSetChanged();
	}

	// Helper function to show the details of a selected item, either by
	// displaying a fragment in-place in the current UI, or starting a whole
	// new activity in which it is displayed.

	void showDetails(int index) {
		if (mDualPane) {

			// Check what fragment is currently shown, replace if needed.
			DetailsFragment details = (DetailsFragment) getFragmentManager()
			        .findFragmentById(R.id.details);
			if (details == null || details.getShownIndex() != index) {
				// Make new fragment to show this selection.

				details = DetailsFragment.newInstance(index);
				Log.d(TAG,
				        "showDetails dual-pane: create and relplace fragment");

				// Execute a transaction, replacing any existing fragment
				// with this one inside the frame.
				FragmentTransaction ft = getFragmentManager()
				        .beginTransaction();
				ft.replace(R.id.details, details);
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				ft.commit();
			}
		} else {
			//if single pane start up the new activity showing the detail

			Intent intent = new Intent();
			intent.setClass(getActivity(), DetailsActivity.class);

			// pass the current position
			intent.putExtra("index", index);
			startActivity(intent);
		}
	}

	/*
	 * Custom Adaptor to display JSONObjects read from matter.json
	 * using their display name
	 */
	class CustomAdapter extends ArrayAdapter<JSONObject> {
		private ArrayList<JSONObject> objects;
		Context context;

		public CustomAdapter(Context context, int resource,
		        ArrayList<JSONObject> objects) {
			super(context, resource, objects);
			this.objects = objects;
			this.context = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;

			if (row == null) {
				LayoutInflater inflator = ((Activity) context)
				        .getLayoutInflater();
				row = inflator.inflate(R.layout.title_row, parent, false);
			}

			JSONObject i = objects.get(position);

			if (i != null) {
				TextView title = (TextView) row.findViewById(R.id.titleText);
				TextView description = (TextView) row.findViewById(R.id.descText);
				title.setText(i.optString("display_number"));
				description.setText(i.optString("description"));
			}

			//sets the background color for the selected item if in multi-pane
			if (position == mCurCheckPosition)
				row.setBackgroundResource(R.color.lightgray);
			else 
				row.setBackgroundColor(Color.WHITE);
			return row;
		}
	}
}
