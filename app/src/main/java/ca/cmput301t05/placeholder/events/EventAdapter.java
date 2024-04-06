package ca.cmput301t05.placeholder.events;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.images.BaseImageHandler;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventCardViewHolder> {
    private ArrayList<Event> eventList;
    private final Context context;

    public enum adapterType {
        HOSTED,
        ATTENDING
    }

    public interface OnItemClickListener {
        void onItemClick(Event event, adapterType type);
    }

    private final adapterType type;
    private final boolean horizontalLayout;
    private OnItemClickListener listener;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMMM dd, yyyy 'at' hh:mm a", Locale.CANADA);


    public EventAdapter(Context context, ArrayList<Event> event, adapterType adapterType) {
        this.eventList = event;
        this.context = context;
        this.type = adapterType;
        this.horizontalLayout = false;
    }

    public EventAdapter(Context context, ArrayList<Event> event, adapterType adapterType, boolean isHorizontalList) {
        this.eventList = event;
        this.context = context;
        this.type = adapterType;
        this.horizontalLayout = isHorizontalList;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public EventCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // depends on the eventType, have different cardView
        View v;
        if(horizontalLayout){
            v = LayoutInflater.from(context).inflate(R.layout.event_card_horizontal, parent, false);
        } else {
            v = LayoutInflater.from(context).inflate(R.layout.event_card_hosted, parent, false);
        }

        if (this.type == adapterType.HOSTED) {
            return new HostedEventCardViewHolder(v);
        } else {
            return new JoinedEventCardViewHolder(v);
        }
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

    @SuppressLint("NotifyDataSetChanged")
    public void setEvents(ArrayList<Event> events){
        this.eventList = events;
        notifyDataSetChanged();
    }

    public void addOrUpdateEvents(ArrayList<Event> newEvents) {
        for (Event newEvent: newEvents) {
            int existIndex = this.eventList.indexOf(newEvent);
            if (existIndex != -1) {
                // event already exists, update it
                this.eventList.set(existIndex, newEvent);
                notifyItemChanged(existIndex);
            } else {
                // event does not exist, add it
                this.eventList.add(newEvent);
                notifyItemInserted(this.eventList.size() - 1);
            }
        }
    }

    public void addEvents(ArrayList<Event> newEvents) {
        this.eventList.addAll(newEvents);
        notifyItemRangeInserted(this.eventList.size() - newEvents.size(), newEvents.size());
    }

    public void removeEvent(Event eventToRemove) {
        int removeIndex = this.eventList.indexOf(eventToRemove);
        if (removeIndex != -1) {
            this.eventList.remove(removeIndex);
            notifyItemRemoved(removeIndex);
        }
    }

    public void removeEvents(ArrayList<Event> eventsToRemove) {
        for (Event event : eventsToRemove) {
            removeEvent(event);
        }
    }

    public void deleteAllEvents() {
        int sizeOfList = this.eventList.size();
        this.eventList.clear();
        notifyItemRangeRemoved(0, sizeOfList);
    }

    public class EventCardViewHolder extends RecyclerView.ViewHolder {
        TextView eventName, eventLocation, eventDate;
        ShapeableImageView posterView;
        PlaceholderApp app;
        CardView cardView;

        public EventCardViewHolder(@NonNull View itemView) {
            super(itemView);

            // Ensure the view ids exist
            try {
                eventName = itemView.findViewById(R.id.event_card_title);
                eventLocation = itemView.findViewById(R.id.event_card_location);
                eventDate = itemView.findViewById(R.id.event_card_date);
                cardView = itemView.findViewById(R.id.event_card);
                posterView = itemView.findViewById(R.id.event_card_poster);

            } catch (NullPointerException e) {
                Log.e("ViewHolderInit", "Error loading view holder: " + e.getMessage());
            }

            app = (PlaceholderApp) context.getApplicationContext();
        }

        public void bindView(int position) {
            if (position >= 0 && position < eventList.size()) {
                Event event = eventList.get(position);
                bindBasicViews(event);
                handlePosterLoad(event);
                handleOnClick(event);
            }
        }

        public void handleOnClick(Event event) {
            if (listener != null) {
                itemView.setOnClickListener(view -> listener.onItemClick(event, type));
            }
        }

        public void bindBasicViews(Event event) {
            if (event != null) {
                eventName.setText(event.getEventName());
                eventLocation.setText(event.getLocation());

                String dateTime = DATE_FORMAT.format(event.getEventDate().getTime());
                eventDate.setText(dateTime);
            }
        }

        public void handlePosterLoad(Event event) {
            if (event != null) {
                if (event.hasEventPosterBitmap()) {
                    posterView.setImageBitmap(event.getEventPosterBitmap());
                } else {
                    app.getPosterImageHandler().getPosterPicture(event, context, new BaseImageHandler.ImageCallback() {
                        @Override
                        public void onImageLoaded(Bitmap bitmap) {
                            posterView.setImageBitmap(bitmap);
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.e("Event_card View", "Error loading image: " + e.getMessage());
                        }
                    });
                }
            }
        }
    }

    public class HostedEventCardViewHolder extends EventCardViewHolder {
        TextView interested_number, attended_number;

        public HostedEventCardViewHolder(@NonNull View itemView) {
            super(itemView);
            try {
                interested_number = itemView.findViewById(R.id.event_card_interested);
                attended_number = itemView.findViewById(R.id.event_card_attending);
            } catch (NullPointerException e) {
                Log.e("ViewHolderInit", "Error loading view holder: " + e.getMessage());
            }
        }

        @Override
        public void bindView(int position) {
            super.bindView(position);
            if (position >= 0 && position < eventList.size()) {
                Event event = eventList.get(position);
                if (event != null) {
                    long interested = event.getRegisteredUsersNum();
                    long attended = event.getAttendeesNum();
                    interested_number.setText(String.valueOf(interested));
                    String attended_string = attended + "/" + event.getMaxAttendees();
                    attended_number.setText(attended_string);
                }
            }
        }
    }

    public class JoinedEventCardViewHolder extends EventCardViewHolder {
        TextView attending_number, interested_number;
        ImageView attendedIcon, interestedIcon;

        public JoinedEventCardViewHolder(@NonNull View itemView) {
            super(itemView);
            try {
                interested_number = itemView.findViewById(R.id.event_card_interested);
                attending_number = itemView.findViewById(R.id.event_card_attending);
                interestedIcon = itemView.findViewById(R.id.event_card_interested_icon);
                attendedIcon = itemView.findViewById(R.id.event_card_attending_icon);
            } catch (NullPointerException e) {
                Log.e("ViewHolderInit", "Error loading view holder: " + e.getMessage());
            }
        }

        @Override
        public void bindView(int position) {
            super.bindView(position);
            if (position >= 0 && position < eventList.size()) {
                Event event = eventList.get(position);
                if (event != null) {
                    long interested = event.getRegisteredUsersNum();
                    interested_number.setText(String.valueOf(interested));
                    attending_number.setVisibility(View.INVISIBLE);
                    attendedIcon.setVisibility(View.INVISIBLE);
                }
            }
        }
    }
}