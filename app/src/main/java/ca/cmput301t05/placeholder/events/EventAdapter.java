package ca.cmput301t05.placeholder.events;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import ca.cmput301t05.placeholder.R;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventCardViewHolder> {

    private ArrayList<Event> eventList;
    private final Context context;

    public EventAdapter(Context context, ArrayList<Event> event){
        this.eventList = event;
        this.context = context;
    }

    @NonNull
    @Override
    public EventCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.event_card, parent, false);
        return new EventCardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EventCardViewHolder holder, int position) {
        try {
            holder.bindView(position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (eventList == null) return 0;
        else return eventList.size();
    }

    public void setEvents(ArrayList<Event> events) {
        this.eventList = events;
        notifyDataSetChanged();
    }

    public class EventCardViewHolder extends RecyclerView.ViewHolder {

        TextView eventName, eventLocation, eventDate, eventTime;
        CardView cardView;

        public EventCardViewHolder(@NonNull View itemView) {
            super(itemView);

            eventName = itemView.findViewById(R.id.event_name);
            eventLocation = itemView.findViewById(R.id.event_location);
            eventDate = itemView.findViewById(R.id.event_date);
            eventTime = itemView.findViewById(R.id.event_time);

            cardView = itemView.findViewById(R.id.event_card);
        }

        public void bindView(int position){
            Event event = eventList.get(position);
            assert event != null;
            eventName.setText(event.getEventName());
            eventLocation.setText(event.getLocation());

            Calendar calendar = event.getEventDate();

            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.CANADA);
            String formattedDate = dateFormat.format(calendar.getTime());
            eventDate.setText(formattedDate);

            SimpleDateFormat timeFormat = new SimpleDateFormat("h:mma", Locale.CANADA);
            String formattedTime = timeFormat.format(calendar.getTime()).toLowerCase();
            eventTime.setText(formattedTime);
        }
    }
}
