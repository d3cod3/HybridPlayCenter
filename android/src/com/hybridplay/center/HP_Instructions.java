package com.hybridplay.center;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

public class HP_Instructions extends HP_Drawer {
	
	/**
	 * The pager widget, which handles animation and allows swiping horizontally
	 * to access previous and next pages.
	 */
	ViewPager pager = null;

	/**
	 * The pager adapter, which provides the pages to the view pager widget.
	 */
	InstructionsPagerAdapter pagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hp_instructions);
		
		// Instantiate a ViewPager
				this.pager = (ViewPager) this.findViewById(R.id.pager);
				
				pagerAdapter = new InstructionsPagerAdapter(getSupportFragmentManager());
				
				pagerAdapter.addFragment(new InstructionSlidePageFragment(Color.WHITE, 0, "null", R.drawable.logo));
				pagerAdapter.addFragment(new InstructionSlidePageFragment(Color.WHITE, 1, "null", R.drawable.intro1));
				pagerAdapter.addFragment(new InstructionSlidePageFragment(Color.WHITE, 2, "null", R.drawable.intro2));
				pagerAdapter.addFragment(new InstructionSlidePageFragment(Color.WHITE, 3, "null", R.drawable.intro3));
				pagerAdapter.addFragment(new InstructionSlidePageFragment(Color.WHITE, 4, "null", R.drawable.intro4));
				pagerAdapter.addFragment(new InstructionSlidePageFragment(Color.WHITE, 5, "null", R.drawable.logo));

				this.pager.setAdapter(pagerAdapter);
				
				//detecting the page where we are
				this.pager.setOnPageChangeListener(new OnPageChangeListener(){

					@Override
					public void onPageScrollStateChanged(int arg0) { }

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) { }

					@Override
					public void onPageSelected(int position) {
						
					}        

				});
		
		updateSelectedItem(1);
	}

}
