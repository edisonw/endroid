package com.edisonwang.adktest.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import com.edisonwang.adktest.MasterActivity;
import com.edisonwang.adktest.R;
import com.edisonwang.adktest.utils.ArduinoCommand;

public class MotorControlFragment extends Fragment {

  private ToggleButton buttonLED;

  private Button upButton;

  private Button downButton;

  private Button leftButton;

  private Button rightButton;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    RelativeLayout root = (RelativeLayout)inflater.inflate(R.layout.main, null);
    buttonLED = (ToggleButton)root.findViewById(R.id.toggleButtonLED);
    buttonLED.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        if (buttonLED.isChecked()) {
          send(ArduinoCommand.LED_ON);
        } else {
          send(ArduinoCommand.LED_OFF);
        }
      }

    });
    upButton = (Button)root.findViewById(R.id.up);
    upButton.setOnTouchListener(new OnTouchListener() {

      @Override
      public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
          send(ArduinoCommand.FORWARD);
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
          send(ArduinoCommand.STOP_ALL);
        }
        return false;
      }
    });
    downButton = (Button)root.findViewById(R.id.down);
    downButton.setOnTouchListener(new OnTouchListener() {

      @Override
      public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
          send(ArduinoCommand.BACKWARDS);
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
          send(ArduinoCommand.STOP_ALL);
        }
        return false;
      }

    });
    leftButton = (Button)root.findViewById(R.id.left);
    leftButton.setOnTouchListener(new OnTouchListener() {

      @Override
      public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
          send(ArduinoCommand.LEFT_TURN);
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
          send(ArduinoCommand.STOP_ALL);
        }
        return false;
      }

    });
    rightButton = (Button)root.findViewById(R.id.right);
    rightButton.setOnTouchListener(new OnTouchListener() {

      @Override
      public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
          send(ArduinoCommand.RIGHT_TURN);
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
          send(ArduinoCommand.STOP_ALL);
        }
        return false;
      }

    });
    return root;
  }

  private void send(ArduinoCommand i) {
    ((MasterActivity)getActivity()).accessoryService.send(i);
  }
}
