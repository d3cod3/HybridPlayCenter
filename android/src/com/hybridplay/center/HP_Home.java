package com.hybridplay.center;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class HP_Home extends Activity implements OnClickListener {
	
	Handler handler = new Handler();
	private ImageView splashScreen;
	boolean isManualEnter = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hp_home);
		
		// splash screen
		splashScreen = (ImageView) findViewById(R.id.LogoImageView);
        splashScreen.setOnClickListener(this);
		
        handler.postDelayed(new Runnable() {
            public void run() {
            	if(!isManualEnter){
            		openHPGames();
            	}
            }
        }, 3000);
	}
    
    @Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.LogoImageView:
			isManualEnter = true;
			openHPGames();
            break;
		
		}
    }
    
    public void openHPGames(){
    	Intent i = new Intent(this, HP_Games.class);
    	startActivity(i);
    	finish();
    }
    
}
