package ca.cmput301t05.placeholder.notifications;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.utils.DateStrings;
import ca.cmput301t05.placeholder.utils.StringManip;

/**
 * Recycler View for notification objects
 *
 * TODO: Add functionality to maybe delete / pin events with the 3 dots.
 */

public class EventNotificationAdapter extends RecyclerView.Adapter<EventNotificationAdapter.NotificationCardViewHolder> {


    private ArrayList<Notification> notificationList;

    private final Context context;

    private final EventAdapterType eventAdapterType;

    private Map<Integer, Boolean> itemExpanded; //used to track which are expanded
    /**
     * Constructs a new EventNotificationAdapter with the given context, list of notifications, and adapter type.
     *
     * @param context             The context of the application or activity.
     * @param notifications       The list of notifications to be displayed.
     * @param type                The adapter type (HOST or ATTENDEE).
     */
    public EventNotificationAdapter(Context context, ArrayList<Notification> notifications, EventAdapterType type){

        this.notificationList = notifications;
        this.eventAdapterType = type;
        this.context = context;

        itemExpanded = new HashMap<>();

        Log.d("Adapter", "First Being Created");

        //init itemexpanded as false
        for (int i = 0; i < notifications.size(); i++) {
            itemExpanded.put(i, false); //position = key, everything not expanded
        }


    }
    /**
     * Inflates the layout for a notification card and returns a new ViewHolder.
     *
     * @param parent   The ViewGroup into which the new View will be added.
     * @param viewType The type of the new View.
     * @return A new ViewHolder that holds a View representing a notification card.
     */
    @NonNull
    @Override
    public NotificationCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.notification_recylerlist_card, parent, false);

        return new NotificationCardViewHolder(v);
    }
    /**
     * Binds data to the ViewHolder at the specified position.
     *
     * @param holder   The ViewHolder to bind data to.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull NotificationCardViewHolder holder, int position) {

        try {
            holder.bindView(position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Returns the total number of notifications in the data set held by the adapter.
     *
     * @return The total number of notifications.
     */
    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    /**
     * ViewHolder class for notification cards.
     */
    public class NotificationCardViewHolder extends RecyclerView.ViewHolder {

        TextView notification_time, notification_message;

        CardView notification_card;
        ImageView menu, pin; //this will be used to pin

        /**
         * Constructs a new NotificationCardViewHolder.
         *
         * @param itemView The View object corresponding to the notification card layout.
         */
        public NotificationCardViewHolder(@NonNull View itemView) {
            super(itemView);


            notification_time = itemView.findViewById(R.id.notification_card_time);
            notification_message = itemView.findViewById(R.id.notification_card_message);

            menu = itemView.findViewById(R.id.notification_card_triple);
            pin = itemView.findViewById(R.id.notification_card_pin);
            notification_card = itemView.findViewById(R.id.notification_card);

            // Listener for expanding/collapsing the text views
            View.OnClickListener expandCollapseListener = v -> {
                // Get the position of the item clicked
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    // Toggle the expansion state
                    boolean isExpanded = itemExpanded.getOrDefault(position, false);
                    itemExpanded.put(position, !isExpanded);

                    notificationList.get(position).setRead(true);
                    // Notify the adapter to rebind the view, which will update its appearance
                    notifyItemChanged(position);
                }
            };

            notification_time.setOnClickListener(expandCollapseListener);
            notification_message.setOnClickListener(expandCollapseListener);


        }

        public void bindView(int position){
            Notification n = notificationList.get(position);
            assert n != null;

            if(eventAdapterType == EventAdapterType.ATTENDEE){

                menu.setVisibility(View.GONE);

            }

            PlaceholderApp app = (PlaceholderApp) context.getApplicationContext();

            if (n.isPinned()){
                pin.setVisibility(View.VISIBLE);
                //TODO maybe set the background to a different colour too
            }   else {
                pin.setVisibility(View.INVISIBLE);
            }

            //format the event time here dont need to check for timezone because calendar does that automatically
            Calendar c = n.getTimeCreated();

            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            int hour = c.get(Calendar.HOUR);
            int amPM = c.get(Calendar.AM_PM);

            String monthName = DateStrings.getMonthName(month);
            String dayString = String.valueOf(day);

            String amOrPmString = DateStrings.getAmPM(amPM);
            String hourString = String.valueOf(hour);

            String formatNotifTime = hourString + " " + amOrPmString + ",  " + monthName + " " + dayString;

            notification_time.setText(formatNotifTime);

            boolean isExpanded = itemExpanded.getOrDefault(position, false);

            if (!isExpanded){
                //truncate message
                notification_message.setText(StringManip.truncateString(n.getMessage(), 50)); //max length 50 chars (then adds ...) so 53 total
            }   else {
                notification_message.setText(n.getMessage());
            }

            menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    PopupMenu popupMenu = new PopupMenu(context, view);
                    popupMenu.getMenuInflater().inflate(R.menu.admin_image_card_menu, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {

                            if (menuItem.getItemId() == R.id.admin_image_card_menu_delete){

                                //TODO Make a confirmation dialog pop up


                                if (n.getNotificationID() != null) {


                                    app.getNotificationTable().deleteDocument(n.getNotificationID().toString(), new Table.DocumentCallback() {
                                        @Override
                                        public void onSuccess(Object document) {
                                            notificationList.remove(position);
                                            notifyItemRemoved(position);
                                        }

                                        @Override
                                        public void onFailure(Exception e) {

                                        }
                                    });
                                }

                                return true;
                            }


                            return true;
                        }


                    });
                    popupMenu.show();

                }
            });


        }


    }

}
