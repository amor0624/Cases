package com.hanna.cases;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Fragment;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

public class DetailsFragment extends Fragment {
	// Create a new instance of DetailsFragment, initialized to show the
	// text at 'index'.

	public static DetailsFragment newInstance(int index) {
		DetailsFragment f = new DetailsFragment();

		// Supply index input as an argument.
		Bundle args = new Bundle();
		args.putInt("index", index);
		f.setArguments(args);
		return f;
	}

	public int getShownIndex() {
		return getArguments().getInt("index", 0);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {

		ScrollView scroller = new ScrollView(getActivity());
		TextView text = new ArialTextView(getActivity());
		int padding = (int) TypedValue.applyDimension(
		        TypedValue.COMPLEX_UNIT_DIP, 4, getActivity().getResources()
		                .getDisplayMetrics());
		text.setPadding(padding, padding, padding, padding);
		scroller.addView(text);
		ArrayList<JSONObject> array =   ((CaseApplication) getActivity().getApplication())
                .getJArray();
		text.setText(array.get(getShownIndex()).toString());
		return scroller;
	}
}
