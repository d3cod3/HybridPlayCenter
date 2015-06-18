package com.hybridplay.center;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.support.v4.app.Fragment;

public class InstructionSlidePageFragment extends Fragment {
	
    private static final String BACKGROUND_COLOR = "color";
    private static final String INDEX = "index";
    private static final String STRINGTXT = "stringTxt";
    private static final String IMAGEN = "imagen";
    
    private int color;
    public int index;
    public String stringTxt;
    private int img;
    
    public InstructionSlidePageFragment(int color, int index, String stringTxt, int img) {
		// Save the parameters
        Bundle bundle = new Bundle();
        bundle.putInt(BACKGROUND_COLOR, color);
        bundle.putInt(INDEX, index);
        bundle.putString(STRINGTXT, stringTxt);
        bundle.putInt(IMAGEN, img);
        this.setArguments(bundle);
        this.setRetainInstance(true);
	}
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
 
        // Load parameters when the initial creation of the fragment is done
        this.color = (getArguments() != null) ? getArguments().getInt(BACKGROUND_COLOR) : Color.GRAY;
        this.index = (getArguments() != null) ? getArguments().getInt(INDEX) : -1;
        this.stringTxt = (getArguments() != null) ? getArguments().getString(STRINGTXT) : "Here should be a text";
        this.img =  (getArguments() != null) ? getArguments().getInt(IMAGEN) : -1;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
 
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.instruction_slide_page, container, false);
 
        // Change the background color
        rootView.setBackgroundColor(this.color);
        
        //show the image
        ImageView myImageView = (ImageView) rootView.findViewById(R.id.imageView);
        myImageView.setImageResource(this.img);
 
        return rootView;
    }

}
