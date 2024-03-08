package ca.cmput301t05.placeholder.ui.events;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import ca.cmput301t05.placeholder.events.Event;

public class JoinedEventsAdapter extends ArrayAdapter<Event> {

    public JoinedEventsAdapter(@NonNull Context context, int resource, @NonNull List<Event> events) {
        super(context, resource, events);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        Event event = getItem(position);
        if (event != null) {
            TextView eventNameTextView = convertView.findViewById(android.R.id.text1);
            eventNameTextView.setText(event.getEventName());
        }

        return convertView;
    }
}
