package com.edisonwang.adktest;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.android.future.usb.UsbAccessory;
import com.android.future.usb.UsbManager;
import com.edisonwang.adktest.utils.SLog;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.Toast;

public class AccessoryService extends Service {

  private UsbManager mUsbManager;

  private static final String ACTION_USB_PERMISSION = "com.google.android.DemoKit.action.USB_PERMISSION";

  private ParcelFileDescriptor mFileDescriptor;

  private UsbAccessory mAccessory;

  private PendingIntent mPermissionIntent;

  private boolean mPermissionRequestPending;

  private FileInputStream mInputStream;

  private FileOutputStream mOutputStream;

  private static IntentFilter USB_PERMISSION_FILTER = new IntentFilter(ACTION_USB_PERMISSION);
  
  static {
    USB_PERMISSION_FILTER.addAction(UsbManager.ACTION_USB_ACCESSORY_DETACHED);
  }

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
            SLog.e("permission denied for accessory: {}.", accessory);
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

  @Override
  public void onCreate() {
    super.onCreate();
    mUsbManager = UsbManager.getInstance(this);
    mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
    registerReceiver(mUsbReceiver, USB_PERMISSION_FILTER);
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
    return START_STICKY;
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
      SLog.d("accessory opened");
    } else {
      SLog.e("accessory open fail");
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

  @Override
  public IBinder onBind(Intent intent) {
    // TODO Auto-generated method stub
    return null;
  }
}
