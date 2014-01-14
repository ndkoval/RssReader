package ru.ifmo.ctddev.koval.rssreader;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import ru.ifmo.ctddev.koval.rssreader.rss.Channel;
import ru.ifmo.ctddev.koval.rssreader.store.ChannelList;
import ru.ifmo.ctddev.koval.rssreader.store.EntryStore;

public class TitlesActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            View update = findViewById(R.id.update_feeds);
            switch (intent.getIntExtra(Constants.RECEIVER_DATA, Constants.STATUS_ERROR)) {
                case Constants.STATUS_RUNNING:
                    update.setEnabled(false);
                    break;
                case Constants.STATUS_ERROR:
                    update.setEnabled(true);
                    Toast.makeText(context, R.string.update_error, Toast.LENGTH_SHORT).show();
                    break;
                case Constants.STATUS_FINISHED:
                    update.setEnabled(true);
                    Toast.makeText(context, R.string.update_ok, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);


        //set mTitle to 1st section title
        onSectionAttached(1);

        //Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        if (getIntent().getData() != null) {
            showAddFeedDialog(getIntent().getData().toString());
        }

        //Set up alarm manager
        MyApp.restartAlarmManager();
    }

    void showAddFeedDialog(String url) {
        mNavigationDrawerFragment.addChannel(url);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // upgrade the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        if (number == 0 || ChannelList.getInstance().isEmpty()) {
            mTitle = getString(R.string.app_name);
            return;
        }

        mTitle = ChannelList.getInstance().get(number - 1).getTitle();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.update_feeds:
                updateFeeds();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateFeeds() {
        final Intent intent = new Intent(this, UpdateFeedsIntentService.class);
        startService(intent);
        MyApp.restartAlarmManager();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        findViewById(R.id.update_feeds).setEnabled(true);
        IntentFilter intentFilter = new IntentFilter(Constants.RECEIVER);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.categories, menu);
            restoreActionBar();
            return true;
        }
        boolean res = super.onCreateOptionsMenu(menu);
        return res;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            int position = getArguments().getInt(ARG_SECTION_NUMBER) - 1;

            if (position == -1 || ChannelList.getInstance().isEmpty()) {
                rootView = inflater.inflate(R.layout.start_fragment_main, null, false);
                return rootView;
            }

            ListView entries = (ListView) rootView.findViewById(R.id.entries);
            Channel channel = ChannelList.getInstance().get(position);
            TitleCursorAdapter mAdapter = new TitleCursorAdapter(getActivity(), EntryStore.getInstance().getItemsCursor(channel.getId()));
            entries.setAdapter(mAdapter);

            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            ((TitlesActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
            super.onAttach(activity);
        }

    }


}
