package com.hanna.cases;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class ArialTextView extends TextView {

	public ArialTextView(Context context) {
	    super(context);
	    Typeface typeface = Typeface.createFromAsset(context.getAssets(), "arial.ttf");
        setTypeface(typeface);
    }
	
	public ArialTextView(Context context, AttributeSet attrs) {
	    super(context, attrs);
	    Typeface typeface = Typeface.createFromAsset(context.getAssets(), "arial.ttf");
        setTypeface(typeface);
    }
}
