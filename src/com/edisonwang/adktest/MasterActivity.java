package com.edisonwang.adktest;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edisonwang.adktest.fragments.MotorControlFragment;
import com.edisonwang.adktest.utils.SLog;

public class MasterActivity extends FragmentActivity {

  /**
   * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the sections. We use a
   * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which will keep every loaded fragment in memory. If
   * this becomes too memory intensive, it may be best to switch to a
   * {@link android.support.v4.app.FragmentStatePagerAdapter}.
   */
  SectionsPagerAdapter mSectionsPagerAdapter;

  /**
   * The {@link ViewPager} that will host the section contents.
   */
  ViewPager mViewPager;

  private final ServiceConnection accessoryServiceConnection = new AccessoryServiceConnection();

  public AccessoryService accessoryService;

  private boolean mIsBindedWithAccessoryService;

  public class AccessoryServiceConnection implements ServiceConnection {

    @Override
    public void onServiceConnected(ComponentName className, IBinder service) {
      SLog.d("OnAccessoryServiceConnected.");
      accessoryService = ((AccessoryService.AccessoryServiceBinder)service).getService();
      mIsBindedWithAccessoryService = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName className) {
      SLog.d("OnAccessoryServiceDisconnected");
      accessoryService = null;
      mIsBindedWithAccessoryService = false;
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    bindWithAccessoryService();
    mViewPager.setCurrentItem(1);
  }

  @Override
  protected void onPause() {
    super.onPause();
    unbindWithAccessoryService();
  }

  private void bindWithAccessoryService() {
    SLog.d("Attempt to bind with service");
    if (!mIsBindedWithAccessoryService) {
      bindService(new Intent(this, AccessoryService.class), accessoryServiceConnection, Context.BIND_AUTO_CREATE);
    }
  }

  private void unbindWithAccessoryService() {
    SLog.d("Attempt to unbind with service");
    if (mIsBindedWithAccessoryService) {
      unbindService(accessoryServiceConnection);
      mIsBindedWithAccessoryService = false;
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.master);

    // Create the adapter that will return a fragment for each of the three
    // primary sections of the app.
    mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

    // Set up the ViewPager with the sections adapter.
    mViewPager = (ViewPager)findViewById(R.id.pager);
    mViewPager.setAdapter(mSectionsPagerAdapter);

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.master, menu);
    return true;
  }

  /**
   * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the sections/tabs/pages.
   */
  public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      if (position == 0) {
        return new MotorControlFragment();
      } else {
        Fragment fragment = new DummySectionFragment();
        Bundle args = new Bundle();
        args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
        fragment.setArguments(args);
        return fragment;
      }
    }

    @Override
    public int getCount() {
      // Show 3 total pages.
      return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
      switch (position) {
        case 0:
          return getString(R.string.title_section1);
        case 1:
          return getString(R.string.title_section2);
        case 2:
          return getString(R.string.title_section3);
      }
      return null;
    }
  }

  /**
   * A dummy fragment representing a section of the app, but that simply displays dummy text.
   */
  public static class DummySectionFragment extends Fragment {

    /**
     * The fragment argument representing the section number for this fragment.
     */
    public static final String ARG_SECTION_NUMBER = "section_number";

    public DummySectionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      // Create a new TextView and set its text to the fragment's section
      // number argument value.
      TextView textView = new TextView(getActivity());
      textView.setGravity(Gravity.CENTER);
      textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
      return textView;
    }
  }

}
