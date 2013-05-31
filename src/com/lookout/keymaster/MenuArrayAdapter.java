package com.lookout.keymaster;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created with IntelliJ IDEA.
 * User: dhalliday
 * Date: 5/30/13
 * Time: 11:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class MenuArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    public MenuArrayAdapter(Context context, String[] values) {
        super(context, R.layout.drawer_list_item, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.drawer_list_item, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.drawerLabel);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.drawerIcon);
        textView.setText(values[position]);
        // Change the icon for Windows and iPhone
        String s = values[position];

        // Terrible terrible code here because PM

        if (s.equals("Home")) {
            imageView.setImageResource(R.drawable.ic_home);
        }
        else if (s.equals("Exchange Keys")) {
            imageView.setImageResource(R.drawable.ic_exchange);
        }
        else if (s.equals("Sync Keyring")) {
            imageView.setImageResource(R.drawable.ic_syncring);
        }
        else if (s.equals("Public Keys")) {
            imageView.setImageResource(R.drawable.ic_public);
        }
        else if (s.equals("Private Keys")) {
            imageView.setImageResource(R.drawable.ic_private);
        }

        return rowView;
    }
}
