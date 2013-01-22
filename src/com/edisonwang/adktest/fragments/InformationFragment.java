package com.edisonwang.adktest.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.edisonwang.adktest.R;

public class InformationFragment extends Fragment {

  private LinearLayout root;

  private BroadcastReceiver receiver = new BroadcastReceiver() {

    @Override
    public void onReceive(Context context, Intent intent) {

    }

  };

  private static IntentFilter FILTERS = new IntentFilter();
  {

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    root = (LinearLayout)inflater.inflate(R.layout.information, null);
    return root;
  }

  public void onResume() {
    super.onResume();
    getActivity().registerReceiver(receiver, FILTERS);
  }
}
