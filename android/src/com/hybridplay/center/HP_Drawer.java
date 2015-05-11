package com.hybridplay.center;

import com.hybridplay.shared.SharedFunctions;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

public class HP_Drawer extends FragmentActivity {
	
	private Activity activity;
	
	protected FrameLayout frameLayout;
	
	private ActionBar actionBar;
	private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mMenuTitles;
    
    int mSelectedItem;
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	activity = this;
    }

	@Override
	public void setContentView(int layoutResID) {
		mDrawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_drawer, null);
	    frameLayout = (FrameLayout) mDrawerLayout.findViewById(R.id.content_frame);

	    getLayoutInflater().inflate(layoutResID, frameLayout, true);

	    super.setContentView(mDrawerLayout);

	    // Left drawer menu
        initDrawerMenu();

	}
	
	public void initDrawerMenu(){
		mTitle = mDrawerTitle = getTitle();
		mMenuTitles = getResources().getStringArray(R.array.menu_array);
        //mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,R.layout.menu_list_item, mMenuTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        
        mDrawerList.setSelector(R.drawable.menu_item_selector);
        
        // Set up the action bar.
        actionBar = getActionBar();

        // enable ActionBar app icon to behave as action to toggle nav drawer
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
            	super.onDrawerClosed(view);
            	actionBar.setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
            	super.onDrawerOpened(drawerView);
            	actionBar.setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         // The action bar home/up action should open or close the drawer.
         // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        return super.onOptionsItemSelected(item);
    }
    
    /* The click listener for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
            mSelectedItem = position;
        }
    }

    private void selectItem(int position) {
    	Intent intent;
        switch (position){
        case 0: // games
        	intent = new Intent(activity, HP_Games.class);
        	this.startActivity(intent);
        	finish();
        	break;
        case 1: // Bluetooth
        	intent = new Intent(activity, HP_Bluetooth.class);
        	this.startActivity(intent);
        	finish();
        	break;
        case 2: // Calibration
        	intent = new Intent(activity, HP_Calibrator.class);
        	this.startActivity(intent);
        	finish();
        	break;
        case 3: // Instructions
        	intent = new Intent(activity, HP_Instructions.class);
        	this.startActivity(intent);
        	finish();
        	break;
        case 4: // Credits
        	intent = new Intent(activity, HP_Credits.class);
        	this.startActivity(intent);
        	finish();
        	break;
        case 5: // Exit
        	SharedFunctions.setBluetooth(false);
        	intent = new Intent(Intent.ACTION_MAIN);
        	intent.addCategory(Intent.CATEGORY_HOME);
    	    startActivity(intent);
    		finish();
        	break;
        }

        // update selected item and title, then close the drawer
        /*mDrawerList.setItemChecked(position, true);
        setTitle(mMenuTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);*/
    }
    
    protected void updateSelectedItem(int position){
    	mDrawerList.setItemChecked(position, true);
    	setTitle(mMenuTitles[position]);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        actionBar.setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

}
