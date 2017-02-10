package study.BluetoothVFWE;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BluetoothChat extends Activity {
	// Debugging
	static final String TAG = "OBDtest";
	static final boolean D = true;
	char enter=0x0D;
	// Message types sent from the BluetoothChatService Handler
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;
	public static final int BT_READ0 = 6;

	// Key names received from the BluetoothChatService Handler
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";

	// Intent request codes
	static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
	static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
	static final int REQUEST_ENABLE_BT = 3;

	// Layout Views
	TextView mTitle;
	ListView mConversationView;
	EditText mOutEditText;
	Button mSendButton;
	TextView showmod,rssi_msg;
	// Name of the connected device
	String mConnectedDeviceName = null;
	// String buffer for outgoing messages
	StringBuffer mOutStringBuffer;
	// Local Bluetooth adapter
	BluetoothAdapter mBluetoothAdapter = null;
	// Member object for the chat services
	BluetoothChatService mChatService = null;

	private String sendMessagesave = "0";
	private String readMessagesave = "0";
	private String time = "00";
	private String uniqueOutFile = "0";

	private int modn = 0;
	private BluetoothAdapter BTAdapter = BluetoothAdapter.getDefaultAdapter();
	
	private TextView textView1b;
	private TextView textView2b;
	private TextView textView3b;
	private TextView textView4b;
	
	private TextView textView1bOn;
	private TextView textView2bOn;
	private TextView textView3bOn;
	private TextView textView4bOn;
	
	Button button1;
	
	private long debugTmp = 0;
	private long countRead = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(D) Log.e(TAG, "+++ ON CREATE +++");
		
		// Set up the window layout
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.main_status);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
		
		// Get layout
		textView1b = (TextView) findViewById(R.id.textView1b);
		textView2b = (TextView) findViewById(R.id.textView2b);
		textView3b = (TextView) findViewById(R.id.textView3b);
		textView4b = (TextView) findViewById(R.id.textView4b);
		textView1bOn = (TextView) findViewById(R.id.textView1bOn);
		textView2bOn = (TextView) findViewById(R.id.textView2bOn);
		textView3bOn = (TextView) findViewById(R.id.textView3bOn);
		textView4bOn = (TextView) findViewById(R.id.textView4bOn);	
		
		button1 = (Button) findViewById(R.id.button1);	
		
		File directory = new File(Environment.getExternalStorageDirectory()
		                          .toString() + "/BluetoothChat/");
		if (!directory.exists())directory.mkdirs();

		SimpleDateFormat formatter = new SimpleDateFormat("HH_mm_ss");

		Date curDate = new Date(System.currentTimeMillis()) ; // 獲取當前時間

		time = formatter.format(curDate);


		// Set up the custom title
		mTitle = (TextView) findViewById(R.id.title_left_text);
		mTitle.setText(R.string.app_name);
		mTitle = (TextView) findViewById(R.id.title_right_text);

		// Get local Bluetooth adapter
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		// If the adapter is null, then Bluetooth is not supported
		if (mBluetoothAdapter == null) {
			Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
			finish();
			return;
		}
		
//		int digiOutput = 6;
//		
//		if (digiOutput % 4 == 2){
//			textView1bOn.setTextColor(Color.GREEN);
//			textView1bOn.setText("ON");
//		}

	}
	
	public void buttonOnClick(View view){
//		debugTmp++;
		countRead = 0;
		button1.setText("");
	}

	@Override
	public void onStart() {
		super.onStart();
		if(D) Log.e(TAG, "++ ON START ++");

		// If BT is not on, request that it be enabled.
		// setupChat() will then be called during onActivityResult
		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
			// Otherwise, setup the chat session
		} else {
			if (mChatService == null) setupChat();
		}
	}

	@Override
	public synchronized void onResume() {
		super.onResume();
		if(D) Log.e(TAG, "+ ON RESUME +");

		// Performing this check in onResume() covers the case in which BT was
		// not enabled during onStart(), so we were paused to enable it...
		// onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
		if (mChatService != null) {
			// Only if the state is STATE_NONE, do we know that we haven't started already
			if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
				// Start the Bluetooth chat services
				mChatService.start();
			}
		}
	}

	void setupChat() {
		Log.d(TAG, "setupChat()");

		// Initialize the BluetoothChatService to perform bluetooth connections
		mChatService = new BluetoothChatService(this, mHandler);

		// Initialize the buffer for outgoing messages
		mOutStringBuffer = new StringBuffer("");
	}

	@Override
	public synchronized void onPause() {
		super.onPause();
		if(D) Log.e(TAG, "- ON PAUSE -");
	}

	@Override
	public void onStop() {
		super.onStop();
		if(D) Log.e(TAG, "-- ON STOP --");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Stop the Bluetooth chat services
		if (mChatService != null) mChatService.stop();
		if(D) Log.e(TAG, "--- ON DESTROY ---");
	}

	void ensureDiscoverable() {
		if(D) Log.d(TAG, "ensure discoverable");
		if (mBluetoothAdapter.getScanMode() !=
		        BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			startActivity(discoverableIntent);
		}
	}

	/**
	 * Sends a message.
	 * @param message  A string of text to send.
	 */
	void sendMessage(String message) {
		// Check that we're actually connected before trying anything
		if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
			Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
			return;
		}

		// Check that there's actually something to send
		if (message.length() > 0) {
			// Get the message bytes and tell the BluetoothChatService to write
			byte[] send = message.getBytes();
			mChatService.write(send);

			// Reset out string buffer to zero and clear the edit text field
			mOutStringBuffer.setLength(0);
//            mOutEditText.setText(mOutStringBuffer);
		}
	}

	// The action listener for the EditText widget, to listen for the return key
	TextView.OnEditorActionListener mWriteListener =
	new TextView.OnEditorActionListener() {
		public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
			// If the action is a key-up event on the return key, send the message
			if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
				String message = view.getText().toString();
				sendMessage(message);
			}
			if(D) Log.i(TAG, "END onEditorAction");
			return true;
		}
	};

	// The Handler that gets information back from the BluetoothChatService
	final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
			case MESSAGE_STATE_CHANGE:
				
				Log.e(TAG, "msg" + msg);
				
				if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
				switch (msg.arg1) {
				case BluetoothChatService.STATE_CONNECTED:
					mTitle.setText(R.string.title_connected_to);
					mTitle.append(mConnectedDeviceName);
					break;
				case BluetoothChatService.STATE_CONNECTING:
					mTitle.setText(R.string.title_connecting);
					break;
				case BluetoothChatService.STATE_LISTEN:
				case BluetoothChatService.STATE_NONE:
					mTitle.setText(R.string.title_not_connected);
					break;
				}
				break;
			case MESSAGE_WRITE:
				byte[] writeBuf = (byte[]) msg.obj;
				// construct a string from the buffer
//				String writeMessage = new String(writeBuf);
//				sendMessagesave = "Me:  " + writeMessage;
//				Log.v("me", writeMessage);
				break;
			case MESSAGE_READ:
				String readBuf = (String) msg.obj;
				// construct a string from the valid bytes in the buffer
//                String readMessage = new String(readBuf, 0, msg.arg1);
//				readMessagesave = mConnectedDeviceName+":  " + readBuf;
//				Log.v("me", readBuf);
				break;
			case MESSAGE_DEVICE_NAME:
				// save the connected device's name
				mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
				Toast.makeText(getApplicationContext(), "Connected to "
				               + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
				break;
			case MESSAGE_TOAST:
				Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
				               Toast.LENGTH_SHORT).show();
				break;
			case BT_READ0:
				final ArrayList<Integer> tmpData = (ArrayList) msg.obj;
								
//				for(int i=0; i<tmpData.size(); i++)
//					Log.e("debug", "tmpData.get(" + i + "): " + tmpData.get(i));
				
				textView1b.setText(tmpData.get(1) + "");
				textView2b.setText(tmpData.get(2) + "");
				textView3b.setText(tmpData.get(3) + "");
				textView4b.setText(tmpData.get(4) + "");
				
				int digiOutput = tmpData.get(5);
				
//				int digiOutput = debugTmp;
//				if(debugTmp == 16)
//					debugTmp = 0;
//				Log.e("debug", "debugTmp: " + debugTmp);
				
				button1.setText("Count: " + countRead++);
				
				textView1bOn.setTextColor(Color.WHITE);
				textView2bOn.setTextColor(Color.WHITE);
				textView3bOn.setTextColor(Color.WHITE);
				textView4bOn.setTextColor(Color.WHITE);
				
				textView1bOn.setText("OFF");
				textView2bOn.setText("OFF");
				textView3bOn.setText("OFF");
				textView4bOn.setText("OFF");
				
				// 聲音
				if ((digiOutput % 2) > 0){
					textView1bOn.setTextColor(Color.GREEN);
					textView1bOn.setText("ON");
				}
				
				// 火焰
				if ((digiOutput % 4) > 1){
					textView2bOn.setTextColor(Color.GREEN);
					textView2bOn.setText("ON");
				}
				
				// 水位
				if ((digiOutput % 8) > 3){
					textView3bOn.setTextColor(Color.GREEN);
					textView3bOn.setText("ON");
				}
				
				// 土壤
				if ((digiOutput % 16) > 7){
					textView4bOn.setTextColor(Color.GREEN);
					textView4bOn.setText("ON");
				}

				break;
			}
		}
	};

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(D) Log.d(TAG, "onActivityResult " + resultCode);
		switch (requestCode) {
		case REQUEST_CONNECT_DEVICE_SECURE:
			// When DeviceListActivity returns with a device to connect
			if (resultCode == Activity.RESULT_OK) {
				connectDevice(data, true);
			}
			break;
		case REQUEST_CONNECT_DEVICE_INSECURE:
			// When DeviceListActivity returns with a device to connect
			if (resultCode == Activity.RESULT_OK) {
				connectDevice(data, false);
			}
			break;
		case REQUEST_ENABLE_BT:
			// When the request to enable Bluetooth returns
			if (resultCode == Activity.RESULT_OK) {
				// Bluetooth is now enabled, so set up a chat session
				setupChat();
			} else {
				// User did not enable Bluetooth or an error occured
				Log.d(TAG, "BT not enabled");
				Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
				finish();
			}
		}
	}

	void connectDevice(Intent data, boolean secure) {
		// Get the device MAC address
		String address = data.getExtras()
		                 .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
		// Get the BLuetoothDevice object
		BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
		// Attempt to connect to the device
		mChatService.connect(device, secure);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.option_menu, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent serverIntent = null;
		switch (item.getItemId()) {
		case R.id.secure_connect_scan:
			// Launch the DeviceListActivity to see devices and do scan
			serverIntent = new Intent(this, DeviceListActivity.class);
			startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
			return true;
		case R.id.insecure_connect_scan:
			// Launch the DeviceListActivity to see devices and do scan
			serverIntent = new Intent(this, DeviceListActivity.class);
			startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
			return true;
		case R.id.discoverable:
			// Ensure this device is discoverable by others
			ensureDiscoverable();
			return true;
		}
		return false;
	}
}