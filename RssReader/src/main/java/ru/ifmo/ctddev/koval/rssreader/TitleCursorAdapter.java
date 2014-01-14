package ru.ifmo.ctddev.koval.rssreader;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import ru.ifmo.ctddev.koval.rssreader.store.EntryDbOpenHelper;

public class TitleCursorAdapter extends ResourceCursorAdapter {

    private LayoutInflater mInflater;
    private Context context;

    public TitleCursorAdapter(Context context, Cursor c) {
        super(context, R.layout.entry_list_item, c);
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return mInflater.inflate(R.layout.entry_list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final String title = cursor.getString(cursor.getColumnIndex(EntryDbOpenHelper.TITLE_COLUMN));
        final String link = cursor.getString(cursor.getColumnIndex(EntryDbOpenHelper.URL_COLUMN));
        final String description = cursor.getString(cursor.getColumnIndex(EntryDbOpenHelper.DESCRIPTION_COLUMN));

        ((TextView) view.findViewById(R.id.entry_title)).setText(title);

        view.findViewById(R.id.web_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                context.startActivity(browserIntent);
            }
        });

        view.findViewById(R.id.show_entry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EntryActivity.class);
                intent.putExtra(EntryActivity.ARG_TITLE, title);
                intent.putExtra(EntryActivity.ARG_CONTENT, description);
                intent.putExtra(EntryActivity.ARG_LINK, link);
                context.startActivity(intent);
            }
        });
    }
}
