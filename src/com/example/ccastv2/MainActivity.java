package com.example.ccastv2;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	private Fragment mVisible;
	private SupportMapFragment mMapFragment;
	private Fragment mFriendsFragment;
	private GoogleMap mMap;
	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));

		setUpFragments();
		mVisible = mMapFragment;
		mTitle = getString(R.string.title_map);
	}

	@Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

	// Set different Fragments Here
	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments

		switch(position)
		{
			case 0:
				showFragment(mMapFragment);
				mTitle = getString(R.string.title_map);
				break;
			default:
				showFragment(mFriendsFragment);
				mTitle = getString(R.string.title_friends);
		}
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.title_map);
			break;
		case 2:
			mTitle = getString(R.string.title_friends);
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class FriendsFragment extends Fragment {
		public static final String TAG = "friends";
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static FriendsFragment newInstance(String friendName) {
			FriendsFragment fragment = new FriendsFragment();
			Bundle args = new Bundle();
			args.putString(ARG_SECTION_NUMBER, friendName);
			fragment.setArguments(args);
			return fragment;
		}

		public FriendsFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);

			TextView textView = (TextView) rootView
					.findViewById(R.id.section_label);
			textView.setText(getArguments().getString(
					ARG_SECTION_NUMBER));
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class MFragment extends SupportMapFragment {
		public static final String TAG = "map";
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static MFragment newInstance() {

			return new MFragment();
		}

		public MFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			super.onCreateView(inflater, container, savedInstanceState);
			View rootView = inflater.inflate(R.layout.fragment_map, container,
					false);
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
		}
	}

	private void showFragment(Fragment fragmentIn) {
        if (fragmentIn == null) return;

        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        if (mVisible != null) ft.hide(mVisible);

        ft.show(fragmentIn).commit();
        mVisible = fragmentIn;
    }

	private void setUpFragments() {
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        // If the activity is killed while in BG, it's possible that the
        // fragment still remains in the FragmentManager, so, we don't need to
        // add it again.
        mMapFragment = (MFragment) getSupportFragmentManager().findFragmentByTag(MFragment.TAG);
        if (mMapFragment == null) {
            mMapFragment = MFragment.newInstance();
            ft.add(R.id.container, mMapFragment, MFragment.TAG);
        }
        ft.show(mMapFragment);

        mFriendsFragment = (FriendsFragment) getSupportFragmentManager().findFragmentByTag(FriendsFragment.TAG);
        if (mFriendsFragment == null) {
            mFriendsFragment = FriendsFragment.newInstance("Gary");
            ft.add(R.id.container, mFriendsFragment, FriendsFragment.TAG);
        }
        ft.hide(mFriendsFragment);

        ft.commit();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
                mMap.setBuildingsEnabled(true);
                mMap.setIndoorEnabled(true);
            }
        }
    }
}
