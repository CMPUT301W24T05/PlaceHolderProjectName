package ca.cmput301t05.placeholder.notifications;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.utils.DateStrings;
import ca.cmput301t05.placeholder.utils.StringManip;
import ca.cmput301t05.placeholder.utils.holdNotiEvent;

public class UserNotificationAdapter extends RecyclerView.Adapter<UserNotificationAdapter.UserNotificationHolder> {

    public interface UserNotificationCallback{

        void onFinish();

        void onError(Exception e);
    }


    private ArrayList<holdNotiEvent> notiEvents;

    private final Context context;

    private PlaceholderApp app;

    private Map<Integer, Boolean> itemExpanded; //used to track which are expanded

    UserNotificationAdapter(Context context, ArrayList<Notification> notifications, UserNotificationCallback callback){
        this.context = context;
        this.notiEvents = new ArrayList<>();
        app = (PlaceholderApp) context.getApplicationContext();
        itemExpanded = new HashMap<>();


        HashMap<String, Notification> eventIDs = new HashMap<>();
        //grab events from the notifications
        for (Notification n : notifications){

            eventIDs.put(n.getFromEventID().toString(), n);

        }

        //implemented this way -> get a callback when we're done + fetch documents might not bring it in order

        app.getEventTable().fetchMultipleDocuments(new ArrayList<String>(eventIDs.keySet()), new Table.DocumentCallback<ArrayList<Event>>() {
            @Override
            public void onSuccess(ArrayList<Event> document) {

                for (Event e : document){

                    holdNotiEvent hNE = new holdNotiEvent(eventIDs.get(e.getEventID().toString()), e);
                    notiEvents.add(hNE);

                    for (int i = 0; i < notiEvents.size(); i++){
                        itemExpanded.put(i, false);
                    }
                }

                callback.onFinish();
            }

            @Override
            public void onFailure(Exception e) {
                callback.onError(e);
            }
        });


    }

    @NonNull
    @Override
    public UserNotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.notification_recylerlist_card, parent, false);

        return new UserNotificationAdapter.UserNotificationHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserNotificationHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return this.notiEvents.size();
    }

    public class UserNotificationHolder extends RecyclerView.ViewHolder{

        TextView eventName, notificationMessage, notificationTime;
        CardView card;

        public UserNotificationHolder(@NonNull View itemView) {
            super(itemView);

            eventName = itemView.findViewById(R.id.user_notification_card_eventName);
            notificationMessage = itemView.findViewById(R.id.user_notification_card_message);
            notificationTime = itemView.findViewById(R.id.user_notification_card_notifDate);
            card = itemView.findViewById(R.id.user_notification_card);

            View.OnClickListener expandCollapseListener = v -> {

                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    boolean isExpanded = itemExpanded.getOrDefault(position, false);
                    itemExpanded.put(position, !isExpanded);
                    notiEvents.get(position).getN().setRead(true);
                    card.setCardBackgroundColor(context.getColor(R.color.md_theme_background_highContrast));

                    notifyItemChanged(position);
                }

            };

            card.setOnClickListener(expandCollapseListener);


        }

        public void bindView(int position){

            Notification n = notiEvents.get(position).getN();
            assert n != null;

            if (!n.isRead()){

                //set card background to different colour
                card.setCardBackgroundColor(context.getColor(R.color.md_theme_inversePrimary));

            }

            eventName.setText(notiEvents.get(position).getE().getEventName());

            if (!itemExpanded.getOrDefault(position, false)){
                //truncate message
                notificationMessage.setText(StringManip.truncateString(n.getMessage(), 50));
            }   else {
                notificationMessage.setText(n.getMessage());
            }

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
            notificationTime.setText(formatNotifTime);

        }
    }
}
