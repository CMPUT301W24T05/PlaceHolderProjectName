package ca.cmput301t05.placeholder.events;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
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
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

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

    private adapterType type;
    private OnItemClickListener listener;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMMM dd, yyyy 'at' hh:mm a", Locale.CANADA);


    public EventAdapter(Context context, ArrayList<Event> event, adapterType adapterType) {
        this.eventList = event;
        this.context = context;
        this.type = adapterType;

    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public EventCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // depends on the eventType, have different cardView
        if (this.type == adapterType.HOSTED) {
            View v = LayoutInflater.from(context).inflate(R.layout.event_card_hosted, parent, false);
            return new HostedEventCardViewHolder(v);
        } else {
            View v = LayoutInflater.from(context).inflate(R.layout.event_card_hosted, parent, false);
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

    public void setEvents(ArrayList<Event> events) {
        this.eventList = events;
        notifyDataSetChanged();
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
                eventName = itemView.findViewById(R.id.event_name);
                eventLocation = itemView.findViewById(R.id.event_location);
                eventDate = itemView.findViewById(R.id.event_date);
                cardView = itemView.findViewById(R.id.event_card);
                posterView = itemView.findViewById(R.id.poster_view);

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
                interested_number = itemView.findViewById(R.id.interested_number);
                attended_number = itemView.findViewById(R.id.checked_Max_card);
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
        TextView interested_number;
        ImageView attendedIcon;

        public JoinedEventCardViewHolder(@NonNull View itemView) {
            super(itemView);
            try {
                interested_number = itemView.findViewById(R.id.interested_number);
                attendedIcon = itemView.findViewById(R.id.imageView4);
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
                    attendedIcon.setVisibility(View.INVISIBLE);
                }
            }
        }
    }
}