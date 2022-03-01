package com.example.testapp.permissions.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.testapp.R;

public class ContactsAdapter  extends CursorAdapter {

    public ContactsAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.contact_row, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView contactId = (TextView) view.findViewById(R.id.contact_row_id);
        TextView contactName = (TextView) view.findViewById(R.id.contact_row_name);
        int id = cursor.getInt(0);
        String name = cursor.getString(1);
        contactId.setText(String.valueOf(id));
        contactName.setText(name);
    }
}
