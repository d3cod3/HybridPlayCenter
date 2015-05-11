package com.hybridplay.center;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hybridplay.network.Connectivity;
import com.hybridplay.network.HPRequest;
import com.hybridplay.network.HPWebsite;
import com.hybridplay.shared.SharedFunctions;

public class HP_Games extends HP_Drawer implements OnClickListener {
	
	public Connectivity link;
	public boolean weHaveInternet, wifiConnection, mobileConnection, isFastConnection;
	GamesLoadingTask gamesListingTask = new GamesLoadingTask();
	
	private static HP_Games activity;
	private SharedPreferences prefs;
	private SharedPreferences.Editor editor;
	Calendar today;
	
	ViewPager gamesPager;
	HP_CollectionPagerAdapter hpCollectionPagerAdapter;
	
	ImageView backgroundImg;
	
	static String[] gamesTitles;
	static String[] gamesDescriptions;
	static String[] gamesImages;
	static String[] gamesStoreLink;
	
	boolean debugReset = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hp_games);
		
		if(savedInstanceState == null){
			if(debugReset){
				createUI();
				loadPreferences();
				checkInternetConnection();
				resetPreferences();
			}else{
				createGamesActivity();
			}
			
		}
		
	}
	
	public void createGamesActivity(){
		activity = this;
		
		createUI();
		
		loadPreferences();
		
		checkInternetConnection();
		
		hpg_firstLaunch();
     	
		checkUpdateCron();
     	
		updateGames();
		
		loadGamesUI();
		
		activateBluetooth();
		
	}
	
	public void activateBluetooth(){
		SharedFunctions.setBluetooth(true);
	}
	
	public void askForBluetoothActivation(View view){
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (!mBluetoothAdapter.isEnabled()){
			Intent intentBtEnabled = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);  
			int REQUEST_ENABLE_BT = 1;
			startActivityForResult(intentBtEnabled, REQUEST_ENABLE_BT);
		}
	}
	
	public void checkInternetConnection(){
		link = new Connectivity(getApplicationContext());
		weHaveInternet = link.isConnected();
		wifiConnection = link.isConnectedWifi();
		mobileConnection = link.isConnectedMobile();
		isFastConnection = link.isConnectedFast();
		
		Log.d("HPG INIT","CHECKING INTERNET CONNECTION");
	}
	
	public void createGamesPager(){
		hpCollectionPagerAdapter = new HP_CollectionPagerAdapter(getSupportFragmentManager());
		gamesPager = (ViewPager) findViewById(R.id.pager);
	}
	
	public void createUI(){
        updateSelectedItem(SharedFunctions.GAMES_MENU_INDEX);
        
        backgroundImg = (ImageView) findViewById(R.id.LogoImageView);
	}
	
	public void configurationChangedGamesPager(){
		if(prefs.getString(SharedFunctions.HP_PREF_UPDATE, "").equals("NO") && !prefs.getString(SharedFunctions.HP_PREF_UPDATE_FIRST_LAUNCH, "").isEmpty()){
			hpCollectionPagerAdapter = new HP_CollectionPagerAdapter(getSupportFragmentManager());
			gamesPager = (ViewPager) findViewById(R.id.pager);
			gamesPager.setAdapter(hpCollectionPagerAdapter);
		}
	}
	
	public void loadPreferences(){
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		editor = prefs.edit();
		
		Log.d("HPG INIT","PREFERENCES LOADING");
	}
	
	public void resetPreferences(){
		prefs.edit().clear().commit();
		
		Log.d("HPG INIT","PREFERENCES RESET");
	}
	
	public void hpg_firstLaunch(){
		if(prefs.getString(SharedFunctions.HP_PREF_UPDATE_FIRST_LAUNCH, "").isEmpty()){
			SharedFunctions.initHPStorage();
			initUpdateCron();
			initXMLRPC();
		}
	}

	public void initXMLRPC(){
 		editor.putString(SharedFunctions.HP_PREF_WEBSITE, HPWebsite.getWebsite(this));
 		editor.putString(SharedFunctions.HP_PREF_USER, getResources().getString(R.string.auto_user));
 		editor.putString(SharedFunctions.HP_PREF_PASS, getResources().getString(R.string.auto_pass));
 		editor.commit();
 		
 		Log.d("HPG INIT","XMLRPC INITED");
	}
	
	public void initUpdateCron(){
		editor.putString(SharedFunctions.HP_PREF_UPDATE,"NO");
     	editor.commit();
     	
     	Log.d("HPG INIT","CRON INITED");
	}
	
	public void checkUpdateCron(){
		
		Log.d(SharedFunctions.HP_GAMES_UPDATE_FLAG,"GMT TIME ZONE: "+SharedFunctions.timeZone());
		
		today = SharedFunctions.getTime(SharedFunctions.timeZone());
		
		if(prefs.getString(SharedFunctions.HP_PREF_UPDATE_MONTH, "").isEmpty()){
     		editor = prefs.edit();
     		editor.putInt(SharedFunctions.HP_PREF_UPDATE_DAY,today.get(Calendar.DAY_OF_MONTH));
     		editor.putString(SharedFunctions.HP_PREF_UPDATE_MONTH,String.valueOf(today.get(Calendar.MONTH)));
     		editor.commit();
     	}

     	if(!prefs.getString(SharedFunctions.HP_PREF_UPDATE_MONTH, "").equals(String.valueOf(today.get(Calendar.MONTH))) || today.get(Calendar.DAY_OF_MONTH) > prefs.getInt(SharedFunctions.HP_PREF_UPDATE_DAY, 0)+7){
     		editor = prefs.edit();
     		editor.putInt(SharedFunctions.HP_PREF_UPDATE_DAY,today.get(Calendar.DAY_OF_MONTH));
     		editor.putString(SharedFunctions.HP_PREF_UPDATE_MONTH,String.valueOf(today.get(Calendar.MONTH)));
     		editor.putString(SharedFunctions.HP_PREF_UPDATE,"YES");
     		editor.commit();

     		Log.d(SharedFunctions.HP_GAMES_UPDATE_FLAG,"UPDATE DAY: "+prefs.getInt(SharedFunctions.HP_PREF_UPDATE_DAY, 0));
     	}
     	
     	Log.d(SharedFunctions.HP_GAMES_UPDATE_FLAG,"NO TIME FOR UPDATE");
	}
	
	public void updateGames(){
		if(!weHaveInternet && prefs.getString(SharedFunctions.HP_PREF_UPDATE_FIRST_LAUNCH, "").isEmpty()){
			closeAppMessage();
		}else if(weHaveInternet){
			if(prefs.getString(SharedFunctions.HP_PREF_UPDATE, "").equals("YES") && !prefs.getString(SharedFunctions.HP_PREF_UPDATE_FIRST_LAUNCH, "").isEmpty()){
				getHPGames();
			}else if(prefs.getString(SharedFunctions.HP_PREF_UPDATE_FIRST_LAUNCH, "").isEmpty()){
				firstTimeGamesDownload();
			}
		}
	}
	
	public void loadGamesUI(){
		if(prefs.getString(SharedFunctions.HP_PREF_UPDATE, "").equals("NO") && !prefs.getString(SharedFunctions.HP_PREF_UPDATE_FIRST_LAUNCH, "").isEmpty()){
			getGamesDataFromSD();
			
			createGamesPager();
			gamesPager.setAdapter(hpCollectionPagerAdapter);
		}
	}
	
	public void getGamesDataFromSD(){
		int numGames = prefs.getInt(SharedFunctions.HP_PREF_UPDATE_NUM_GAMES, 0);
		
		gamesTitles = new String[numGames];
		gamesDescriptions = new String[numGames];
		gamesImages = new String[numGames];
		gamesStoreLink = new String[numGames];
		
		// Main Game Data
		String jsonData = SharedFunctions.getJSONFileFromExternalStorage("games.json");
		
		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(jsonData);
			JSONObject root = jsonObj.getJSONObject("HybridPlay");
			
			for(int i=0;i<numGames;i++){
				JSONObject temp = root.getJSONObject(String.valueOf(i));
				
				gamesTitles[i] = temp.getString("Title");
				gamesDescriptions[i] = temp.getString("Description");
				gamesImages[i] = temp.getString("Img_URL");
				gamesStoreLink[i] = temp.getString("Play_Store_Link");
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
        
	}
	
	public void reloadActivity(){
		finish();
		startActivity(getIntent());
	}
	
	public void createGamesJSON(String[] gTitles, String[] gDesc, String[] gImg, String[] gStore){
		try {
			String _path = Environment.getExternalStorageDirectory()+SharedFunctions.GAMES_JSON_DIR+"/games.json";
			
			int numGames = gTitles.length;
			
            JSONObject jsonFile = new JSONObject();
            JSONObject jsonObject = new JSONObject();
            
            for(int i=0;i<numGames;i++){
            	JSONObject actualGame = new JSONObject();
            	
            	actualGame.put("Title",gTitles[i]);
            	actualGame.put("Description",gDesc[i]);
            	actualGame.put("Img_URL",gImg[i]);
            	actualGame.put("Play_Store_Link",gStore[i]);
            	
            	jsonObject.put(String.valueOf(i),actualGame);
            }
            

            jsonFile.put("HybridPlay", jsonObject);
            
            String content = jsonFile.toString();
            FileWriter fw;
			try {
				fw = new FileWriter(_path);
				BufferedWriter bw = new BufferedWriter(fw);
	            bw.write(content);
	            bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
            
            Log.d("output", jsonFile.toString(2));
        } catch (JSONException e) {
            e.printStackTrace();
        }
	}
	
	public void firstTimeGamesDownload(){
		findViewById(R.id.loadingLayout).setVisibility(View.VISIBLE);
		
		HashMap<String, Object> data = new HashMap<String, Object>();
		HPRequest stream = new HPRequest(activity, mHandler, "hp.getGamesData", data, SharedFunctions.MSG_GET_GAMES);
		stream.execute();
		
		String tMsg = "Downloading HybridPlay Games List";
		Toast toast = Toast.makeText(activity, (CharSequence) tMsg,Toast.LENGTH_LONG);
		toast.getView().setBackgroundColor(getResources().getColor(R.color.hp_green));
		toast.show();
		
		Log.d(SharedFunctions.HP_GAMES_UPDATE_FLAG,"FIRST TIME DOWNLOADING GAMES DATA");
		
		editor = prefs.edit();
		editor.putString(SharedFunctions.HP_PREF_UPDATE_FIRST_LAUNCH,"1");
		editor.commit();
	}
    
    public void getHPGames(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(getResources().getString(R.string.update_games)).setCancelable(false)
		.setPositiveButton("Update", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				findViewById(R.id.loadingLayout).setVisibility(View.VISIBLE);
				
				HashMap<String, Object> data = new HashMap<String, Object>();
				HPRequest stream = new HPRequest(activity, mHandler, "hp.getGamesData", data, SharedFunctions.MSG_GET_GAMES);
				stream.execute();
				
				String tMsg = "Updating games list";
				Toast toast = Toast.makeText(activity, (CharSequence) tMsg,Toast.LENGTH_LONG);
				toast.getView().setBackgroundColor(getResources().getColor(R.color.hp_green));
				toast.show();
				
				Log.d(SharedFunctions.HP_GAMES_UPDATE_FLAG,"CHECK UPDATING GAMES");
				
			}
		})
		.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}
    
    public void closeAppMessage(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(getResources().getString(R.string.close_app_message)).setCancelable(false)
		.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				exitHybridPlayCenter();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}
    
    public void exitHybridPlayCenter(){
    	Intent intent = new Intent(Intent.ACTION_MAIN);
	    intent.addCategory(Intent.CATEGORY_HOME);
	    startActivity(intent);
		finish();
    }
    
	
	@Override
	public void onConfigurationChanged(Configuration newConfig){
	    super.onConfigurationChanged(newConfig);
	    setContentView(R.layout.activity_hp_games);
	    
	    configurationChangedGamesPager();
	}
    
    @Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.LogoImageView:
        	//splashScreen.setVisibility(View.INVISIBLE);
            break;
		
		}
    }
    
    
    @SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		
		@Override
        public void handleMessage(Message msg) {
			
			String toast = null;
			HashMap<?, ?> map;
			Object obj;
			Object[] list;
			boolean haveGames = false;
			
			Log.d("","EXECUTING STREAM CALL: "+msg.what);
			
			switch(msg.what) {
				case SharedFunctions.MSG_GET_GAMES:
					if(!(msg.obj instanceof HashMap)) {
						break;
					}
					
					map = (HashMap<?, ?>) msg.obj;
					obj = map.get("message");
					
					if(!(obj instanceof Object[])) {
						if(obj instanceof String){
							toast = (String) obj;
							break;
						}
					}
					
					list = (Object[]) obj;
					
					if(list.length > prefs.getInt(SharedFunctions.HP_PREF_UPDATE_NUM_GAMES, 0)){
						haveGames = true;
					}
					
					editor.putInt(SharedFunctions.HP_PREF_UPDATE_NUM_GAMES,list.length);
					editor.commit();
					
					gamesTitles = new String[list.length];
					gamesDescriptions = new String[list.length];
					gamesImages = new String[list.length];
					gamesStoreLink = new String[list.length];
					
					for(int i=0;i<list.length;i++){
						final HashMap<?,?> entryMap = (HashMap<?, ?>) list[i];
						
						gamesTitles[i] = entryMap.get("title").toString();
						gamesDescriptions[i] = entryMap.get("description").toString();
						gamesImages[i] = entryMap.get("image_url").toString();
						gamesStoreLink[i] = entryMap.get("play_store").toString();
						
						// SAVE IMAGES TO SD CARD
						SharedFunctions.DownloadFromUrl(gamesImages[i],gamesTitles[i].replace(" ", "")+".jpg");
						
						if(i == list.length-1){
							
							editor.putString(SharedFunctions.HP_PREF_UPDATE,"NO");
							editor.commit();
							
							findViewById(R.id.loadingLayout).setVisibility(View.INVISIBLE);
						}
					}
					
					if(haveGames){
						String tMsg = "New HybridPlay Games Loaded!";
						Toast toast2 = Toast.makeText(activity, (CharSequence) tMsg,Toast.LENGTH_LONG);
						toast2.getView().setBackgroundColor(getResources().getColor(R.color.hp_green));
						toast2.show();
						
						// SAVE DATA TO /hybridplay/json/games.json
						createGamesJSON(gamesTitles,gamesDescriptions,gamesImages,gamesStoreLink);
					}else{
						String tMsg = "There are no new HybridPlay Games available!";
						Toast toast2 = Toast.makeText(activity, (CharSequence) tMsg,Toast.LENGTH_LONG);
						toast2.getView().setBackgroundColor(getResources().getColor(R.color.hp_green));
						toast2.show();
					}
					
					reloadActivity();

				break;
			}
			
			if(toast != null){
				Toast.makeText(activity, (CharSequence) toast,Toast.LENGTH_LONG).show();
			}
			
		}
	
	};
	
	// Load games data from SD Card AsyncTask Class
	class GamesLoadingTask extends AsyncTask<Integer, Integer, Void> {
		
		boolean oneTime;

		@Override
		protected void onPreExecute() {
			oneTime = true;
			findViewById(R.id.loadingLayout).setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Integer... params) {
			if(oneTime){
				oneTime = false;
				// get games data from SD Card ---- TODO
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			
			findViewById(R.id.loadingLayout).setVisibility(View.INVISIBLE);
		}
	}
	
	
	// Custom Collection Adapter for Games Navigation
	public static class HP_CollectionPagerAdapter extends FragmentStatePagerAdapter{
		public HP_CollectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = new GameObjectFragment();
            Bundle args = new Bundle();
            args.putInt("Game Index",i);
            args.putString("Game Title",gamesTitles[i].replace(" ", ""));
            args.putString("Game Desc",gamesDescriptions[i]);
            args.putString("Game Img",gamesImages[i]);
            args.putString("Game Store",gamesStoreLink[i]);
            fragment.setArguments(args);
            
            return fragment;
        }

        @Override
        public int getCount() {
            return gamesTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return gamesTitles[position];
        }
        
	}
	
	// The Game Collection Object (UI)
	public static class GameObjectFragment extends Fragment implements OnClickListener {
		
		Bundle args;
		boolean isFloatingGame = false;
		
		RelativeLayout floatingGame;
		
		@Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.game_collection_object, container, false);
            args = getArguments();
            ((TextView) rootView.findViewById(R.id.gameTitle)).setText(args.getString("Game Title"));
            ((TextView) rootView.findViewById(R.id.gameName)).setText(args.getString("Game Title"));
            ((TextView) rootView.findViewById(R.id.gameDesc)).setText(args.getString("Game Desc"));
            
            TextView gameStore = (TextView) rootView.findViewById(R.id.gameStore);
            gameStore.setText(args.getString("Game Store"));
            gameStore.setOnClickListener(this);
            
            Bitmap temp = SharedFunctions.getBitmapFromSDCard(activity.getApplicationContext(),args.getString("Game Title")+".jpg");
            
            ImageView gameImg = (ImageView) rootView.findViewById(R.id.gameScreenshot);
            gameImg.setImageBitmap(temp);
            gameImg.setOnClickListener(this);
            
            // rounded bordered info
            floatingGame = (RelativeLayout) rootView.findViewById(R.id.gameFloatingInfo);
            floatingGame.setVisibility(View.INVISIBLE);
            
            return rootView;
        }
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.gameScreenshot:
				if(isFloatingGame){
					isFloatingGame = false;
					floatingGame.setVisibility(View.INVISIBLE);
				}else{
					isFloatingGame = true;
					floatingGame.setVisibility(View.VISIBLE);
				}
				
	            break;
			case R.id.gameStore:
				String tMsg = "Opening link in Play Store";
				Toast toast2 = Toast.makeText(activity, (CharSequence) tMsg,Toast.LENGTH_LONG);
				toast2.getView().setBackgroundColor(getResources().getColor(R.color.hp_green));
				toast2.show();
				break;
			}
	    }
	}

}
