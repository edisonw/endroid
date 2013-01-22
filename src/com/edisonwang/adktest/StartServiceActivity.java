package com.edisonwang.adktest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class StartServiceActivity extends Activity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Intent intent = new Intent(this, AccessoryService.class);
    intent.fillIn(getIntent(), 0);
    startService(intent);
    finish();
  }
}