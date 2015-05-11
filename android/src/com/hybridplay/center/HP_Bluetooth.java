package com.hybridplay.center;

//import con.hybridplay.bluetooth.BlunoLibrary;

import java.lang.reflect.Method;
import java.util.ArrayList;

import com.hybridplay.bluetooth.DeviceListAdapter;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class HP_Bluetooth extends HP_Drawer {
	//public class HP_Bluetooth extends BlunoLibrary {
	
	private ArrayList<BluetoothDevice> mDeviceList = new ArrayList<BluetoothDevice>();
	
	private BluetoothAdapter mBluetoothAdapter;
	private ListView mListView;
	private DeviceListAdapter mAdapter;
	
	ProgressDialog mProgressDlg;
	TextView bluetoothStatus;
	TextView pairedStatus;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hp_bluetooth);
		
		if(savedInstanceState == null){
			initBluetoothActivity();
		}
		
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig){
	    super.onConfigurationChanged(newConfig);
	    setContentView(R.layout.activity_hp_bluetooth);
	    
	}
	
	private void initBluetoothActivity(){
		updateSelectedItem(1);
		
		bluetoothStatus = (TextView) findViewById(R.id.bluetooth_status);
		pairedStatus = (TextView) findViewById(R.id.paired_status);
		
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		
		IntentFilter filter = new IntentFilter();
		
		filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		filter.addAction(BluetoothDevice.ACTION_FOUND);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
		
		registerReceiver(mReceiver, filter);
		
		if(mBluetoothAdapter.isEnabled()){
			bluetoothStatus.setText("CONNECTED");
			bluetoothStatus.setTextColor(getResources().getColor(R.color.hp_green));
		}
		
		mProgressDlg 		= new ProgressDialog(this);
		
		mProgressDlg.setMessage("Scanning...");
		mProgressDlg.setCancelable(false);
		mProgressDlg.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        dialog.dismiss();
		        
		        mBluetoothAdapter.cancelDiscovery();
		    }
		});
		
		if (mBluetoothAdapter == null) {
			showUnsupported();
		}else{
			// start bluetooth discovery
			mBluetoothAdapter.startDiscovery();
		}
	}
	
	private void showUnsupported() {
		bluetoothStatus.setText("Bluetooth is unsupported by this device");
	}
	
	private void pairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("createBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unpairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("removeBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
	    public void onReceive(Context context, Intent intent) {	    	
	        String action = intent.getAction();
	        
	        if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
	        	final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
	        	 
	        	if (state == BluetoothAdapter.STATE_ON) {
	        		showToast("Bluetooth Enabled");
	        	 }
	        } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
	        	mDeviceList = new ArrayList<BluetoothDevice>();
	        	mProgressDlg.show();
	        } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
	        	mProgressDlg.dismiss();
	        	
	        	mListView		= (ListView) findViewById(R.id.foundDevices);
	    		
	    		mAdapter		= new DeviceListAdapter(HP_Bluetooth.this);
	    		
	    		mAdapter.setData(mDeviceList);
	    		mAdapter.setListener(new DeviceListAdapter.OnPairButtonClickListener() {			
	    			@Override
	    			public void onPairButtonClick(int position) {
	    				BluetoothDevice device = mDeviceList.get(position);
	    				
	    				if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
	    					unpairDevice(device);
	    				} else {
	    					showToast("Pairing Device...");
	    					
	    					pairDevice(device);
	    				}
	    			}
	    		});
	    		
	    		mListView.setAdapter(mAdapter);
	        	
	        } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	        	BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
		        
	        	mDeviceList.add(device);
	        	
	        	showToast("Found device " + device.getName());
	        } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {	        	
	        	 final int state 		= intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
	        	 final int prevState	= intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR);
	        	 
	        	 BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	        	 
	        	 if (state == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_BONDING) {
	        		 showToast("Paired");
	        		 
	        		 pairedStatus.setText("PAIRED WITH "+device.getName());
	        	 } else if (state == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDED){
	        		 showToast("Unpaired");
	        		 pairedStatus.setText("UNPAIRED");
	        	 }
	        	 
	        	 mAdapter.notifyDataSetChanged();
	        }
	    }
	};
	
	private void showToast(String message) {
		Toast toast2 = Toast.makeText(this, message,Toast.LENGTH_LONG);
		toast2.getView().setBackgroundColor(getResources().getColor(R.color.hp_green));
		toast2.show();
	}
	
	/*private void initBluetooth(){
		onCreateProcess();			//onCreate Process by BlunoLibrary
        
        serialBegin(115200);		//set the Uart Baudrate on BLE chip to 115200
        
        bluetoothStatus = (TextView) findViewById(R.id.bluetooth_status);
        
        buttonScanOnClickProcess();	//Alert Dialog for selecting the BLE device
	}*/
	
	protected void onResume(){
		super.onResume();
		//System.out.println("BlUNOActivity onResume");
		//onResumeProcess();														//onResume Process by BlunoLibrary
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//onActivityResultProcess(requestCode, resultCode, data);					//onActivityResult Process by BlunoLibrary
		super.onActivityResult(requestCode, resultCode, data);
	}
	
    @Override
    protected void onPause() {
    	if (mBluetoothAdapter != null) {
			if (mBluetoothAdapter.isDiscovering()) {
				mBluetoothAdapter.cancelDiscovery();
			}
		}
    	
        super.onPause();
        //onPauseProcess();														//onPause Process by BlunoLibrary
    }
	
	protected void onStop() {
		super.onStop();
		//onStopProcess();														//onStop Process by BlunoLibrary
	}
    
	@Override
    protected void onDestroy() {
		unregisterReceiver(mReceiver);
		
        super.onDestroy();
        //onDestroyProcess();														//onDestroy Process by BlunoLibrary
    }

	//@Override
	/*public void onConectionStateChange(connectionStateEnum theConnectionState) {//Once connection state changes, this function will be called
		switch (theConnectionState) {											//Four connection state
		case isConnected:
			bluetoothStatus.setText("Connected");
			break;
		case isConnecting:
			bluetoothStatus.setText("Connecting");
			break;
		case isToScan:
			bluetoothStatus.setText("Scan");
			break;
		case isScanning:
			bluetoothStatus.setText("Scanning");
			break;
		case isDisconnecting:
			bluetoothStatus.setText("isDisconnecting");
			break;
		default:
			break;
		}
	}

	@Override
	public void onSerialReceived(String theString) {							//Once connection data received, this function will be called
		serialReceivedText.append(theString);							//append the text into the EditText
		//The Serial data from the BLUNO may be sub-packaged, so using a buffer to hold the String is a good choice.
					
	}*/

}
