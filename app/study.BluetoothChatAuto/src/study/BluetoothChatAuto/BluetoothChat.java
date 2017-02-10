/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package study.BluetoothChatAuto;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This is the main Activity that displays the current chat session.
 */
public class BluetoothChat extends Activity {
	// Debugging
	private static final String TAG = "BluetoothChat";
	private static final boolean D = true;

	// Message types sent from the BluetoothChatService Handler
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;

	// Key names received from the BluetoothChatService Handler
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";
	public static final String QUEUE_ID = "queueId";

	// Intent request codes
	private static final int REQUEST_CONNECT_DEVICE = 2;
	private static final int REQUEST_ENABLE_BT = 3;

	// Layout Views
	private ListView mConversationView;
	private EditText mOutEditText;
	private Button mSendButton;

	// Name of the connected device
	private String mConnectedDeviceName = null;
	// Array adapter for the conversation thread
	private ArrayAdapter<String> mConversationArrayAdapter;
	// String buffer for outgoing messages
	private StringBuffer mOutStringBuffer;
	// Local Bluetooth adapter
	private BluetoothAdapter mBluetoothAdapter = null;
	// Member object for the chat services
	private BluetoothChatService mChatService = null;
	
	private Handler autoQueueConnectHandler = new Handler();
	private Handler tryConnectHandler = new Handler();
	
	private TextView statusTextView[];
	private Button statusButton[];
	private Button statusButtonOnClick;
	private int statusTextViewId[] = { R.id.textView1, R.id.textView2, R.id.textView3};
	private int statusButtonId[] = { R.id.button1, R.id.button2, R.id.button3};
		
	private int queueId = -1;
	private int tryConnect = 0;
	
	private ArrayList<String> deviceMAC = new ArrayList<String>();
	private ArrayList<String> deviceUnableConnectMAC = new ArrayList<String>();
	
	private Boolean autoMode = false;
	private SharedPreferences settings;
	
	public static String PACKAGE_NAME;
	
	private Menu menu;
	
	private long count = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (D)
			Log.e(TAG, "+++ ON CREATE +++");
		
		// 取得 Package name
		PACKAGE_NAME = getApplicationContext().getPackageName();
		
		// 不自動彈出虛擬鍵盤
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		
		// 
		SharedPreferences sPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		int size = sPrefs.getInt("size", 0);
		if (size != 0){
			for(int j=0; j<size; j++)
				deviceMAC.add(sPrefs.getString("id"+j, null));
		} else {
			for(int i=0; i<statusTextViewId.length; i++)
				deviceMAC.add(getString(R.string.add_device));
		}
		
		
		Log.d("debug", "deviceMAC: " + deviceMAC);
		
		// Set up the window layout
		setContentView(R.layout.main);	
		
		// TextView		
		statusTextView = new TextView[statusTextViewId.length];
		for(int i=0; i<statusTextViewId.length; i++){
			statusTextView[i] = (TextView) findViewById(statusTextViewId[i]);
			statusTextView[i].setTextColor(Color.WHITE);
			statusTextView[i].setText(R.string.title_not_connected);
		}
		
		// Button
		statusButton = new Button[statusButtonId.length];
		for(int i=0; i<statusButtonId.length; i++){
			// 點擊事件
			statusButtonOnClick = (Button) findViewById(statusButtonId[i]);
			statusButtonOnClick.setTag(i);
			onClickButton();
			// 顯示事件
			statusButton[i] = (Button) findViewById(statusButtonId[i]);
			statusButton[i].setText(deviceMAC.get(i));
		}
		
		// Get local Bluetooth adapter
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		// If the adapter is null, then Bluetooth is not supported
		if (mBluetoothAdapter == null) {
			Toast.makeText(this, "Bluetooth is not available",
					Toast.LENGTH_LONG).show();
			finish();
			return;
		}
	}
	
	// 
	private void onClickButton() {
		statusButtonOnClick.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				
				// 取得按鈕 Index				
				final int statusButtonOnClickTag = Integer.valueOf(v.getTag().toString());
				
				stopAutoConnect();

				menu.getItem(0).setIcon(R.drawable.ic_media_play);
				menu.getItem(0).setTitle(R.string.auto_connect);

				queueId = statusButtonOnClickTag;
				
				if (BluetoothAdapterEnabled(1)){
					Intent serverIntent = null;
					serverIntent = new Intent(BluetoothChat.this, DeviceListActivity.class);
					serverIntent.putExtra(BluetoothChat.QUEUE_ID, statusButtonOnClickTag);
					startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
				}
			}
		});
	}
	
	private Runnable autoQueueConnectRunnable = new Runnable() {
		@Override
		public void run(){
			if (BluetoothAdapterEnabled(0)){
				// 藉由 deviceMAC 中 MAC 數量來決定連線排隊順序
				queueId++;
				if(queueId > deviceMAC.size() - 1)
					queueId = 0;
				// 停止裝置連線
				mChatService.stop();
				// 指定裝置連線
				connectDeviceMAC(deviceMAC.get(queueId));
				// 每 1 秒重複 1 次	
				autoQueueConnectHandler.postDelayed(autoQueueConnectRunnable, 1000);
			}
		}
	};
	
	// 嘗試連線
	private Runnable tryConnectRunnable = new Runnable() {
		@Override
		public void run(){			
			
			// 嘗試連線 3 次
			tryConnect++;
			if (tryConnect < 3){
				// 移除 deviceUnableConnectMAC 中對應目前 deviceMAC MAC
				if (!deviceUnableConnectMAC.isEmpty()){
					int deviceUnableIndex = deviceUnableConnectMAC.indexOf(deviceMAC.get(queueId));
					if (deviceUnableIndex != -1){
						deviceUnableConnectMAC.remove(deviceUnableIndex);
					}
				}
				// 停止裝置連線
				mChatService.stop();
				// 指定裝置連線
				connectDeviceMAC(deviceMAC.get(queueId));
				// 每 1 秒重複 1 次	
				tryConnectHandler.postDelayed(tryConnectRunnable, 1000);
			} else {
				// 嘗試重新連線次數設為 0 次
				tryConnect = 0;
				// 新增連線失敗的 MAC 於 deviceUnableConnectMAC 中
				deviceUnableConnectMAC.add(deviceMAC.get(queueId));
				// 移除重新連線 Runnable
				tryConnectHandler.removeCallbacks(tryConnectRunnable);
				
				if(autoMode)
					autoQueueConnectHandler.postDelayed(autoQueueConnectRunnable, 1000);
			}
		}
	};

	@Override
	public void onStart() {
		super.onStart();
		if (D)
			Log.e(TAG, "++ ON START ++");
		
		// 如果沒有啟動藍芽，則要求使用者開啟藍芽
		// If BT is not on, request that it be enabled.
		// setupChat() will then be called during onActivityResult
		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
			// Otherwise, setup the chat session
		} else {
			if (mChatService == null)
				setupChat();
		}
	}

	@Override
	public synchronized void onResume() {
		super.onResume();
		if (D)
			Log.e(TAG, "+ ON RESUME +");

		// Performing this check in onResume() covers the case in which BT was
		// not enabled during onStart(), so we were paused to enable it...
		// onResume() will be called when ACTION_REQUEST_ENABLE activity
		// returns.
		if (mChatService != null) {
			// Only if the state is STATE_NONE, do we know that we haven't
			// started already
			if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
				// Start the Bluetooth chat services
				mChatService.start();
			}
		}
	}

	private void setupChat() {
		Log.d(TAG, "setupChat()");

		// Initialize the array adapter for the conversation thread
		mConversationArrayAdapter = new ArrayAdapter<String>(this,
				R.layout.message);
		mConversationView = (ListView) findViewById(R.id.in);
		mConversationView.setAdapter(mConversationArrayAdapter);

		// Initialize the compose field with a listener for the return key
		mOutEditText = (EditText) findViewById(R.id.edit_text_out);
		mOutEditText.setOnEditorActionListener(mWriteListener);

		// Initialize the send button with a listener that for click events
		mSendButton = (Button) findViewById(R.id.button_send);
		mSendButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// Send a message using content of the edit text widget
				TextView view = (TextView) findViewById(R.id.edit_text_out);
				String message = view.getText().toString();
				sendMessage(message);
			}
		});
		
		// 把主 Thread 的 Handler 傳給 Service 以供日後傳回 message
		// Initialize the BluetoothChatService to perform bluetooth connections
		mChatService = new BluetoothChatService(this, mHandler);

		// Initialize the buffer for outgoing messages
		mOutStringBuffer = new StringBuffer("");
	}

	@Override
	public synchronized void onPause() {
		super.onPause();
		if (D)
			Log.e(TAG, "- ON PAUSE -");
	}

	@Override
	public void onStop() {
		super.onStop();
		if (D)
			Log.e(TAG, "-- ON STOP --");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Stop the Bluetooth chat services
		if (mChatService != null)
			mChatService.stop();
		if (D)
			Log.e(TAG, "--- ON DESTROY ---");
		
		tryConnectHandler.removeCallbacks(tryConnectRunnable);
		autoQueueConnectHandler.removeCallbacks(autoQueueConnectRunnable);
	}

	private void ensureDiscoverable() {
		if (D)
			Log.d(TAG, "ensure discoverable");
		if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			Intent discoverableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(
					BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			startActivity(discoverableIntent);
		}
	}

	/**
	 * Sends a message.
	 * 
	 * @param message
	 *            A string of text to send.
	 */
	private void sendMessage(String message) {
		// Check that we're actually connected before trying anything
		if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
			Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT)
					.show();
			return;
		}

		// Check that there's actually something to send
		if (message.length() > 0) {
			// Get the message bytes and tell the BluetoothChatService to write
			byte[] send = message.getBytes();
			mChatService.write(send);

			// Reset out string buffer to zero and clear the edit text field
			mOutStringBuffer.setLength(0);
			mOutEditText.setText(mOutStringBuffer);
		}
	}

	// The action listener for the EditText widget, to listen for the return key
	private TextView.OnEditorActionListener mWriteListener = new TextView.OnEditorActionListener() {
		public boolean onEditorAction(TextView view, int actionId,
				KeyEvent event) {
			// If the action is a key-up event on the return key, send the
			// message
			if (actionId == EditorInfo.IME_NULL
					&& event.getAction() == KeyEvent.ACTION_UP) {
				String message = view.getText().toString();
				sendMessage(message);
			}
			if (D)
				Log.i(TAG, "END onEditorAction");
			return true;
		}
	};

	private final void setStatus(int resId) {
		final ActionBar actionBar = getActionBar();
		actionBar.setSubtitle(resId);
	}

	private final void setStatus(CharSequence subTitle) {
		final ActionBar actionBar = getActionBar();
		actionBar.setSubtitle(subTitle);
	}

	// The Handler that gets information back from the BluetoothChatService
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_STATE_CHANGE:
				if (D)
					Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
				switch (msg.arg1) {
				case BluetoothChatService.STATE_CONNECTED:	// 已連線
					setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
					mConversationArrayAdapter.clear();
					
					// statusTextView 已連線狀態設定
					statusTextView[queueId].setText(getString(R.string.title_connected_to, mConnectedDeviceName));
					statusTextView[queueId].setTextColor(Color.GREEN);
					// 嘗試重新連線次數設為 0 次
					tryConnect = 0;
					// 移除重新連線 Runnable
					tryConnectHandler.removeCallbacks(tryConnectRunnable);
					
					if (autoMode)
						autoQueueConnectHandler.postDelayed(autoQueueConnectRunnable, 3000);
					else
						stopAutoConnect();
					
					break;
				case BluetoothChatService.STATE_CONNECTING:
					setStatus(R.string.title_connecting);
					
					// statusTextView 連線中狀態設定
					statusTextView[queueId].setText(getString(R.string.title_connecting) + " " + (tryConnect + 1) + "/3");
					statusTextView[queueId].setTextColor(0xFFFFFF00);
					// 移除重新連線 Runnable
					tryConnectHandler.removeCallbacks(tryConnectRunnable);
					
					if (autoMode)
						autoQueueConnectHandler.removeCallbacks(autoQueueConnectRunnable);
					
					break;
				case BluetoothChatService.STATE_LISTEN:
				case BluetoothChatService.STATE_NONE:		// 未連線
					setStatus(R.string.title_not_connected);
					
					// statusTextView 狀態設定
					for(int i=0; i<statusTextView.length; i++){
						// 所有 statusTextView 設為白色 "Not Connected"，除了當前正在連線的
						if (queueId != i){
							// 設定 statusTextView
							statusTextView[i].setText(R.string.title_not_connected);
							statusTextView[i].setTextColor(Color.WHITE);
						}
						// 判斷有嘗試連線失敗 3 次對應的 statusTextView 設為紅色 "Unable Connect"
						if (!deviceUnableConnectMAC.isEmpty() && deviceUnableConnectMAC.contains(deviceMAC.get(i))){
							// 取得對應 deviceUnableIndex
							int deviceUnableIndex = deviceUnableConnectMAC.indexOf(deviceMAC.get(i));
							// 取得對應 deviceIndex
							int deviceIndex = deviceMAC.indexOf(deviceUnableConnectMAC.get(deviceUnableIndex));
							// 設定 statusTextView
							statusTextView[deviceIndex].setText(R.string.unable_connect);
							statusTextView[deviceIndex].setTextColor(Color.RED);
						}
					}
					
					if (!autoMode) {
						for(int i=0; i<statusTextView.length; i++){
							// 設定 statusTextView
							statusTextView[i].setText(R.string.title_not_connected);
							statusTextView[i].setTextColor(Color.WHITE);
						}
					}
					
					break;
				}
				break;
			case MESSAGE_WRITE:
				byte[] writeBuf = (byte[]) msg.obj;
				// construct a string from the buffer
				String writeMessage = new String(writeBuf);
				mConversationArrayAdapter.add("Me:  " + writeMessage);
				break;
			case MESSAGE_READ:
				byte[] readBuf = (byte[]) msg.obj;
				// construct a string from the valid bytes in the buffer
				String readMessage = new String(readBuf, 0, msg.arg1);
//				mConversationArrayAdapter.add(mConnectedDeviceName + ":  "
//						+ readMessage);
				
//				mConversationArrayAdapter.add(deviceMAC.get(queueId) + ":  "
//						+ readMessage);
				
				BluetoothAdapterEnabled(1);
				
				char[] mChars = "0123456789ABCDEF".toCharArray();
		        StringBuilder sb = new StringBuilder();
		        for (int n=0; n<readBuf.length; n++){
		        	
//		        	if(readBuf[n] != 0x00){
//			            sb.append(mChars[(readBuf[n] & 0xFF) >> 4]);
//			            sb.append(mChars[readBuf[n] & 0x0F]);
//			            sb.append(' ');
//			            count++;
//		        	}
		        	
		        	sb.append(mChars[(readBuf[n] & 0xFF) >> 4]);
		            sb.append(mChars[readBuf[n] & 0x0F]);
		            sb.append(' ');
		        }
		        mConversationArrayAdapter.add(deviceMAC.get(queueId) + ":  "
						+ sb.toString().trim());
				
				break;
			case MESSAGE_DEVICE_NAME:
				// save the connected device's name
				mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
				Toast.makeText(getApplicationContext(),
						"Connected to " + mConnectedDeviceName,
						Toast.LENGTH_SHORT).show();
				break;
			case MESSAGE_TOAST:
//				Toast.makeText(getApplicationContext(),
//						msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
//						.show();
				
				if(msg.getData().getBoolean("unableToConnectDevice")){				
					statusTextView[queueId].setText(R.string.unable_connect);
					statusTextView[queueId].setTextColor(Color.RED);
					tryConnectHandler.postDelayed(tryConnectRunnable, 1000);
				}	
				break;
			}
		}
	};

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (D)
			Log.d(TAG, "onActivityResult " + resultCode);
		switch (requestCode) {
		case REQUEST_CONNECT_DEVICE:
			// When DeviceListActivity returns with a device to connect
			if (resultCode == Activity.RESULT_OK) {
				connectDevice(data);
			}
			break;
		case REQUEST_ENABLE_BT:
			// When the request to enable Bluetooth returns
			if (resultCode == Activity.RESULT_OK) {
				// Bluetooth is now enabled, so set up a chat session
				setupChat();
			} else {
				// User did not enable Bluetooth or an error occurred
				Log.d(TAG, "BT not enabled");
				Toast.makeText(this, R.string.bt_not_enabled_leaving,
						Toast.LENGTH_SHORT).show();
				finish();
			}
		}
	}

	private void connectDevice(Intent data) {
		// Get the device MAC address
		String address = data.getExtras().getString(
				DeviceListActivity.EXTRA_DEVICE_ADDRESS);
		int queueIdTmp = data.getExtras().getInt(BluetoothChat.QUEUE_ID);
		
		stopAutoConnect();
		
		for(int i=0; i<deviceUnableConnectMAC.size(); i++){
			// 判斷有嘗試連線失敗 3 次對應的 statusTextView 設為紅色 "Unable Connect"
			if (!deviceUnableConnectMAC.isEmpty() && deviceUnableConnectMAC.contains(deviceMAC.get(i))){
				// 取得對應 deviceUnableIndex
				int deviceUnableIndex = deviceUnableConnectMAC.indexOf(deviceMAC.get(i));
				deviceUnableConnectMAC.remove(deviceUnableIndex);
				
				// 取得對應 deviceIndex
				int deviceIndex = deviceMAC.indexOf(deviceUnableConnectMAC.get(deviceUnableIndex));
				// 設定 statusTextView
				statusTextView[deviceIndex].setText(R.string.not_connected);
				statusTextView[deviceIndex].setTextColor(Color.WHITE);
			}
		}
		
		// 設定 MAC
		deviceMAC.set(queueIdTmp, address);
		statusButton[queueIdTmp].setText(address);
		
		// 存入 SharedPreferences
		SharedPreferences sPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor sEdit = sPrefs.edit();
		for(int i=0; i<deviceMAC.size(); i++){
			sEdit.putString("id"+i,deviceMAC.get(i));
		}
		sEdit.putInt("size",deviceMAC.size());
		sEdit.commit();
	}
	
	private void connectDeviceMAC(String address) {
		// Get the BluetoothDevice object
		BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
		// Attempt to connect to the device
		mChatService.connect(device);
	}
	
	private void stopAutoConnect() {
		mChatService.stop();
		tryConnect = 0;			
		tryConnectHandler.removeCallbacks(tryConnectRunnable);
		deviceUnableConnectMAC.removeAll(deviceUnableConnectMAC);
		autoQueueConnectHandler.removeCallbacks(autoQueueConnectRunnable);
	}
	
	private boolean BluetoothAdapterEnabled(int mode) {
		if (mBluetoothAdapter.isEnabled()){
			return true;
		} else {
			stopAutoConnect();
			
			switch (mode){
			case 0:
				Intent enableIntent = new Intent(
						BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
				break;
			case 1:
				Intent intent = getIntent();
				finish();
				startActivity(intent);
				break;
			}			
			return false;
		} 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.scan_menu, menu);
		this.menu = menu;
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.auto_connect:
			// 如果沒有啟動藍芽，則要求使用者開啟藍芽
			if (!autoMode && BluetoothAdapterEnabled(0)){
				if (deviceMAC.contains(getString(R.string.add_device))) {
					Toast.makeText(getApplicationContext(),	R.string.please_add_device, Toast.LENGTH_SHORT).show();
				} else {
					autoMode = true;
					queueId = -1;
					item.setIcon(R.drawable.ic_media_stop);
					item.setTitle(R.string.stop_connect);
					autoQueueConnectHandler.postDelayed(autoQueueConnectRunnable, 100);
				}
			} else {
				autoMode = false;
				item.setIcon(R.drawable.ic_media_play);
				item.setTitle(R.string.auto_connect);
				stopAutoConnect();
			}
			return true;
		}
		return false;
	}
}
