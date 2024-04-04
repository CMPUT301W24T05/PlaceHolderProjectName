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

    public enum adapterType{
        HOSTED,
        ATTENDING

    }

    public interface OnItemClickListener{
        void onItemClick(Event event, adapterType type);
    }

    private adapterType type;
    private OnItemClickListener listener;


    public EventAdapter(Context context, ArrayList<Event> event, adapterType adapterType){
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
        if (this.type == adapterType.HOSTED){
            View v = LayoutInflater.from(context).inflate(R.layout.event_card_hosted, parent, false);
            return new HostedEventCardViewHolder(v);
        }
        else {
            View v = LayoutInflater.from(context).inflate(R.layout.event_card_hosted, parent, false);
            return new JoinedEventCardViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull EventCardViewHolder holder, int position) {

        try {
            holder.bindView(position, listener);
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


        public void bindView(int position, OnItemClickListener listener){
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(event, type);
                }
            });
        }
    }

    // created a subclass of the cardViewHolder for hosted event to show some statistics
    public class HostedEventCardViewHolder extends EventCardViewHolder {
        TextView interested_number, attended_number;
        ShapeableImageView posterView;
        PlaceholderApp app;
        public HostedEventCardViewHolder(@NonNull View itemView) {
            super(itemView);
            interested_number = itemView.findViewById(R.id.interested_number);
            attended_number = itemView.findViewById(R.id.checked_Max_card);
            posterView = itemView.findViewById(R.id.poster_view);
            app = (PlaceholderApp) context.getApplicationContext();
        }
        @Override
        public void bindView(int position, OnItemClickListener listener){
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

             int max = event.getMaxAttendees();
             long interested = event.getRegisteredUsersNum();
             long attended = event.getAttendeesNum();
             interested_number.setText(String.valueOf(interested));
             String attended_string =  attended + "/" + max;
             attended_number.setText(attended_string);

             // set Bit map need to use the call back function
             //posterView.setImageBitmap(event.getEventPosterBitmap());
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
                        Log.e("Hoster_Event_card View", "Error loading image: " + e.getMessage());
                    }
                });
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(event, type);
                }
            });
        }
    }
    public class JoinedEventCardViewHolder extends EventCardViewHolder {
        TextView interested_number, attended_number;
        ShapeableImageView posterView;
        ImageView attendedIcon;
        PlaceholderApp app;
        public JoinedEventCardViewHolder(@NonNull View itemView) {
            super(itemView);
            interested_number = itemView.findViewById(R.id.interested_number);
            attended_number = itemView.findViewById(R.id.checked_Max_card);
            posterView = itemView.findViewById(R.id.poster_view);
            attendedIcon = itemView.findViewById(R.id.imageView4);
            app = (PlaceholderApp) context.getApplicationContext();

        }
        @Override
        public void bindView(int position, OnItemClickListener listener){
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

            long interested = event.getRegisteredUsersNum();
            interested_number.setText(String.valueOf(interested));

            attendedIcon.setVisibility((View.INVISIBLE));
            attended_number.setVisibility(View.INVISIBLE);

            // set Bit map need to use the call back function
            //posterView.setImageBitmap(event.getEventPosterBitmap());
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
                        Log.e("Hoster_Event_card View", "Error loading image: " + e.getMessage());
                    }
                });
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(event, type);
                }
            });
        }
    }
}
