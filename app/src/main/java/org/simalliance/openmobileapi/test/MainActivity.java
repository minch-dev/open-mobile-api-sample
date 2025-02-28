package org.simalliance.openmobileapi.test;

import android.se.omapi.Channel;
import android.se.omapi.Reader;
import android.se.omapi.SEService;
import android.se.omapi.Session;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.concurrent.Executors;

/**
 * Open Mobile API sample to demonstrate how a Secure Element based
 * Android application can be realized with the G&D SmartCard API .<br>
 *
 * SmartCard API implements the SIMalliance Open Mobile API ({@link <a href="https://www.simalliance.org">...</a>}).
 */
public class MainActivity extends Activity {

	SEService _service = null;
	Session _session = null;

	TextView _textview = null;
	ScrollView _scrollview = null;

	private static final byte[] ISD_AID = new byte[] { (byte) 0xA0, 0x00, 0x00, 0x00, 0x03, 0x00, 0x00, 0x00 };

	
	private static String bytesToString(byte[] bytes) {
		StringBuffer sb = new StringBuffer();
		for (byte b : bytes)
			sb.append(String.format("%02x ", b & 0xFF));
		
		return sb.toString();
	}

	private void logText(String message) {
		_scrollview.post(new Runnable() {
			public void run() {
				_scrollview.fullScroll(ScrollView.FOCUS_DOWN);
			}

		});
		_textview.append(message);
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LinearLayout layout = new LinearLayout(this);
		layout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		_scrollview = new ScrollView(this);
		_textview = new TextView(this);
		_textview.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		_scrollview.addView(_textview);
		layout.addView(_scrollview);
		setContentView(layout);
		logText("Establishing connection to SEService:  \n --- \n");

		SEServiceCallback callback = new SEServiceCallback();
		_service = new SEService(this, Executors.newSingleThreadExecutor(),callback);
	//CalledFromWrongThreadException: Only the original thread that created a view hierarchy can touch its views.
	}

	/**
	 * Callback interface if informs that this SEService is connected to the SmartCardService
	 */
	public class SEServiceCallback implements SEService.OnConnectedListener {

		public void onConnected() {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					//logText("connection established!  \n");
					performTest();
				}
			});

		}
	}

	private void performTest() {
		logText("Testing...  \n");
		Reader[] readers = _service.getReaders();
		logText("Available readers:  \n");
		for (Reader reader : readers)
			logText("	" + reader.getName() + "   - " + ((reader.isSecureElementPresent()) ? "present" : "absent") + "\n");

		if (readers.length == 0) {
			logText("No reader available \n");
			return;
		}

		for (Reader reader : readers) {
			if (!reader.isSecureElementPresent())
				continue;

			logText("\n--------------------------------\nSelected reader: \"" + reader.getName() + "\"\n");
			
			try {
				_session = reader.openSession();
			} catch (Exception e) {
				logText(e.getMessage());
			}
			if (_session == null)
				continue;
			
			try {
				byte[] atr = _session.getATR();
				logText("ATR: " + ((atr == null) ? "unavailable" : bytesToString(atr)) + "\n\n");
			} catch (Exception e) {
				logText("Exception on getATR(): " + e.getMessage() + "\n\n");
			}

			testBasicChannel(null);
			testBasicChannel(ISD_AID);
			
			testLogicalChannel(null);
			testLogicalChannel(ISD_AID);
			
			_session.close();
		}
	}
	
	void testBasicChannel(byte[] aid) {
		try {
			logText("BasicChannel test: " + ((aid == null) ? "default applet" : bytesToString(aid)) + "\n");
			Channel channel = _session.openBasicChannel(aid);

			byte[] cmd = new byte[] { (byte) 0x80, (byte) 0xCA, (byte) 0x9F, 0x7F, 0x00 };
			logText(" -> " + bytesToString(cmd) + "\n");
			byte[] rsp = channel.transmit(cmd);
			logText(" <- " + bytesToString(rsp) + "\n\n");

			channel.close();
		} catch (Exception e) {
			logText("Exception on BasicChannel: " + e.getMessage() + "\n\n");
		}
	}
		
	void testLogicalChannel(byte[] aid) {
		try {
			logText("LogicalChannel test: " + ((aid == null) ? "default applet" : bytesToString(aid)) + "\n");
			Channel channel = _session.openLogicalChannel(aid);

			byte[] cmd = new byte[] { (byte) 0x80, (byte) 0xCA, (byte) 0x9F, 0x7F, 0x00 };
			logText(" -> " + bytesToString(cmd) + "\n");
			byte[] rsp = channel.transmit(cmd);
			logText(" <- " + bytesToString(rsp) + "\n\n");

			channel.close();
		} catch (Exception e) {
			logText("Exception on LogicalChannel: " + e.getMessage() + "\n\n");
		}
	}

	@Override
	protected void onDestroy() {
		if (_service != null) {
			_service.shutdown();
		}
		super.onDestroy();
	}
}