package com.edisonwang.adktest;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;

import com.android.future.usb.UsbAccessory;
import com.android.future.usb.UsbManager;
import com.edisonwang.adktest.utils.ArduinoCommand;
import com.edisonwang.adktest.utils.SLog;

public class AccessoryService extends Service {

  private static final String ACTION_USB_PERMISSION = "com.google.android.DemoKit.action.USB_PERMISSION";

  private UsbManager mUsbManager;

  private ParcelFileDescriptor mFileDescriptor;

  private UsbAccessory mAccessory;

  private PendingIntent mPermissionIntent;

  private boolean mPermissionRequestPending;

  private FileInputStream mInputStream;

  private FileOutputStream mOutputStream;

  private final IBinder mBinder = new AccessoryServiceBinder();

  private static IntentFilter USB_PERMISSION_FILTER = new IntentFilter(ACTION_USB_PERMISSION);

  static {
    USB_PERMISSION_FILTER.addAction(UsbManager.ACTION_USB_ACCESSORY_DETACHED);
  }

  public class AccessoryServiceBinder extends Binder {

    public AccessoryService getService() {
      return AccessoryService.this;
    }
  }

  private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {

    @Override
    public void onReceive(Context context, Intent intent) {
      String action = intent.getAction();
      SLog.d("Action received: {}", action);
      if (ACTION_USB_PERMISSION.equals(action)) {
        SLog.d("Arduino attached.");
        synchronized (this) {
          UsbAccessory accessory = UsbManager.getAccessory(intent);
          if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
            openAccessory(accessory);
          } else {
            SLog.e("permission denied for accessory: {}.", accessory);
          }
          mPermissionRequestPending = false;
        }
      } else if (UsbManager.ACTION_USB_ACCESSORY_DETACHED.equals(action)) {
        SLog.d("Arduino detached");
        UsbAccessory accessory = UsbManager.getAccessory(intent);
        if (accessory != null && accessory.equals(mAccessory)) {
          closeAccessory();
        }
      }
    }
  };

  @Override
  public void onCreate() {
    super.onCreate();
    SLog.d("Service started");
    mUsbManager = UsbManager.getInstance(this);
    mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
    registerReceiver(mUsbReceiver, USB_PERMISSION_FILTER);
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    SLog.d("Service onStart.");

    closeAccessory();
    if (mInputStream != null && mOutputStream != null) {
      // Should we do something else here?
      return START_STICKY;
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
      SLog.e("mAccessory is null");
    }

    return START_STICKY;
  }

  @Override
  public void onDestroy() {
    SLog.i("Service destroyed.");
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
      SLog.d("accessory opened");
    } else {
      SLog.e("accessory open fail");
    }
  }

  private void closeAccessory() {
    SLog.i("Close Accessory.");
    try {
      mOutputStream = null;
      if (mFileDescriptor != null) {
        mFileDescriptor.close();
      }
    } catch (IOException e) {
      SLog.e("IOError on close, might be expected.", e);
    } finally {
      mFileDescriptor = null;
      mAccessory = null;
    }
  }

  public synchronized void send(ArduinoCommand i) {
    byte[] buffer = new byte[1];
    buffer[0] = (byte)(i.value());
    SLog.d("write {} {}", i.name(), buffer[0]);

    if (mOutputStream != null) {
      try {
        mOutputStream.write(buffer);
      } catch (IOException e) {
        SLog.e("write failed: " + e.getMessage());
      }
    }
  }

  @Override
  public IBinder onBind(Intent intent) {
    SLog.d("onBind");
    return mBinder;
  }
}
