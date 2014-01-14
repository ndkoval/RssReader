package ru.ifmo.ctddev.koval.rssreader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import ru.ifmo.ctddev.koval.rssreader.rss.Channel;
import ru.ifmo.ctddev.koval.rssreader.store.ChannelList;


public class ChannelAdapter extends BaseAdapter {

    private Context context;

    public ChannelAdapter(Context context) {
        this.context = context;
    }

    void remove(int position) {
        ChannelList.getInstance().remove(position);
        notifyDataSetInvalidated();
    }

    @Override
    public int getCount() {
        return ChannelList.getInstance().size();
    }

    @Override
    public Object getItem(int i) {
        return ChannelList.getInstance().get(i);
    }

    @Override
    public long getItemId(int i) {
        return ChannelList.getInstance().get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(android.R.layout.simple_list_item_activated_1, viewGroup, false);
        }

        Channel channel = (Channel) getItem(i);

        if (channel != null) {
            ((TextView) v).setText(channel.getTitle());
        }

        return v;
    }
}
