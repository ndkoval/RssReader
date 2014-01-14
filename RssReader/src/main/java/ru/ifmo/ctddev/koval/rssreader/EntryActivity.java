package ru.ifmo.ctddev.koval.rssreader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

public class EntryActivity extends ActionBarActivity {

    public static final String ARG_TITLE = "title";
    public static final String ARG_CONTENT = "content";
    public static final String ARG_LINK = "link";
    private static final String CHARSET = "UTF-8";
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getIntExtra(Constants.RECEIVER_DATA, Constants.STATUS_ERROR)) {
                case Constants.STATUS_ERROR:
                    Toast.makeText(context, R.string.update_error, Toast.LENGTH_SHORT).show();
                    break;
                case Constants.STATUS_FINISHED:
                    Toast.makeText(context, R.string.update_ok, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(getIntent().getStringExtra(ARG_TITLE));

        setContentView(R.layout.entry);

        String content = getIntent().getStringExtra(ARG_CONTENT);
        WebView entryView = (WebView) findViewById(R.id.entry_view);
        entryView.getSettings().setJavaScriptEnabled(true);
        entryView.loadData(content, "text/html", CHARSET);
        entryView.setWebChromeClient(new WebChromeClient());

        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(Constants.RECEIVER);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.entry, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.web_link:
                String link = getIntent().getStringExtra(ARG_LINK);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
