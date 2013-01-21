package com.edisonwang.adktest;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ToggleButton;

import com.android.future.usb.UsbAccessory;
import com.android.future.usb.UsbManager;

public class ArduinoBlinkLEDActivity extends Activity {

  private static final String TAG = "Endroid";

  private static final String ACTION_USB_PERMISSION = "com.google.android.DemoKit.action.USB_PERMISSION";

  private UsbManager mUsbManager;

  private PendingIntent mPermissionIntent;

  private boolean mPermissionRequestPending;

  private ToggleButton buttonLED;

  private UsbAccessory mAccessory;

  private ParcelFileDescriptor mFileDescriptor;

  private FileInputStream mInputStream;

  private FileOutputStream mOutputStream;

  private Button upButton;

  private Button downButton;

  private Button leftButton;

  private Button rightButton;

  private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {

    @Override
    public void onReceive(Context context, Intent intent) {
      String action = intent.getAction();
      if (ACTION_USB_PERMISSION.equals(action)) {
        synchronized (this) {
          UsbAccessory accessory = UsbManager.getAccessory(intent);
          if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
            openAccessory(accessory);
          } else {
            Log.d(TAG, "permission denied for accessory " + accessory);
          }
          mPermissionRequestPending = false;
        }
      } else if (UsbManager.ACTION_USB_ACCESSORY_DETACHED.equals(action)) {
        UsbAccessory accessory = UsbManager.getAccessory(intent);
        if (accessory != null && accessory.equals(mAccessory)) {
          closeAccessory();
        }
      }
    }
  };

  @SuppressWarnings("deprecation")
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mUsbManager = UsbManager.getInstance(this);
    mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
    IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
    filter.addAction(UsbManager.ACTION_USB_ACCESSORY_DETACHED);
    registerReceiver(mUsbReceiver, filter);

    if (getLastNonConfigurationInstance() != null) {
      mAccessory = (UsbAccessory)getLastNonConfigurationInstance();
      openAccessory(mAccessory);
    }

    setContentView(R.layout.main);
    buttonLED = (ToggleButton)findViewById(R.id.toggleButtonLED);

    upButton = (Button)findViewById(R.id.up);
    upButton.setOnTouchListener(new OnTouchListener() {

      @Override
      public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
          send(2);
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
          send(3);
        }
        return true;
      }

    });
    downButton = (Button)findViewById(R.id.down);
    downButton.setOnTouchListener(new OnTouchListener() {

      @Override
      public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
          send(4);
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
          send(5);
        }
        return true;
      }

    });
    leftButton = (Button)findViewById(R.id.left);
    leftButton.setOnTouchListener(new OnTouchListener() {

      @Override
      public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
          send(6);
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
          send(7);
        }
        return true;
      }

    });
    rightButton = (Button)findViewById(R.id.right);
    rightButton.setOnTouchListener(new OnTouchListener() {

      @Override
      public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
          send(8);
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
          send(9);
        }
        return true;
      }

    });

  }

  @SuppressWarnings("deprecation")
  @Override
  public Object onRetainNonConfigurationInstance() {
    if (mAccessory != null) {
      return mAccessory;
    } else {
      return super.onRetainNonConfigurationInstance();
    }
  }

  @Override
  public void onResume() {
    super.onResume();

    if (mInputStream != null && mOutputStream != null) {
      return;
    }

    UsbAccessory[] accessories = mUsbManager.getAccessoryList();
    UsbAccessory accessory = (accessories == null ? null : accessories[0]);
    if (accessory != null) {
      if (mUsbManager.hasPermission(accessory)) {
        openAccessory(accessory);
      } else {
        synchronized (mUsbReceiver) {
          if (!mPermissionRequestPending) {
            mUsbManager.requestPermission(accessory, mPermissionIntent);
            mPermissionRequestPending = true;
          }
        }
      }
    } else {
      Log.d(TAG, "mAccessory is null");
    }
  }

  @Override
  public void onPause() {
    super.onPause();
    closeAccessory();
  }

  @Override
  public void onDestroy() {
    unregisterReceiver(mUsbReceiver);
    super.onDestroy();
  }

  private void openAccessory(UsbAccessory accessory) {
    mFileDescriptor = mUsbManager.openAccessory(accessory);
    if (mFileDescriptor != null) {
      mAccessory = accessory;
      FileDescriptor fd = mFileDescriptor.getFileDescriptor();
      mInputStream = new FileInputStream(fd);
      mOutputStream = new FileOutputStream(fd);
      Log.d(TAG, "accessory opened");
    } else {
      Log.d(TAG, "accessory open fail");
    }
  }

  private void closeAccessory() {
    try {
      if (mFileDescriptor != null) {
        mFileDescriptor.close();
      }
    } catch (IOException e) {
    } finally {
      mFileDescriptor = null;
      mAccessory = null;
    }
  }

  public synchronized void send(int i) {
    byte[] buffer = new byte[1];
    buffer[0] = (byte)i;
    Log.d(TAG, "write: " + buffer[0]);

    if (mOutputStream != null) {
      try {
        mOutputStream.write(buffer);
      } catch (IOException e) {
        Log.e(TAG, "write failed", e);
      }
    }
  }

  public void blinkLED(View v) {

    if (buttonLED.isChecked()) {
      send(0);
    } else {
      send(1);
    }
  }

}