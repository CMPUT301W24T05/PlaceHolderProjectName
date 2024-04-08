package ca.cmput301t05.placeholder.notifications;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.profile.Profile;
import ca.cmput301t05.placeholder.utils.CompareByDate;
import ca.cmput301t05.placeholder.utils.DateStrings;
import ca.cmput301t05.placeholder.utils.StringManip;
import ca.cmput301t05.placeholder.utils.HoldNotificationToEvent;

public class UserNotificationAdapter extends RecyclerView.Adapter<UserNotificationAdapter.UserNotificationHolder> {

    public interface UserNotificationCallback{

        void onFinish();

        void onError(Exception e);
    }


    private ArrayList<HoldNotificationToEvent> notiEvents;

    private final Context context;

    private PlaceholderApp app;

    private Map<Integer, Boolean> itemExpanded; //used to track which are expanded

    public UserNotificationAdapter(Context context, ArrayList<HoldNotificationToEvent> notifiEvents){

        this.context = context;

        this.notiEvents = notifiEvents;
        this.notiEvents.sort(new CompareByDate());

        app = (PlaceholderApp) context.getApplicationContext();
        itemExpanded = new HashMap<>();


    }


    @NonNull
    @Override
    public UserNotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.user_notification_card, parent, false);

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

    public void updateList(UserNotificationCallback callback){

        app.getProfileTable().fetchDocument(app.getUserProfile().getProfileID().toString(), new Table.DocumentCallback<Profile>() {
            @Override
            public void onSuccess(Profile document) {

                app.getNotificationTable().fetchMultipleDocuments(document.getNotifications(), new Table.DocumentCallback<ArrayList<Notification>>() {
                    @Override
                    public void onSuccess(ArrayList<Notification> document) {
                        ArrayList<String> notificationStrings = new ArrayList<>();
                        ArrayList<Notification> curNotifications = document;
                        //grabbing event info
                        for (Notification n : curNotifications){
                            notificationStrings.add(n.getFromEventID().toString());
                        }

                        app.getEventTable().fetchMultipleDocuments(notificationStrings, new Table.DocumentCallback<ArrayList<Event>>() {
                            @Override
                            public void onSuccess(ArrayList<Event> document) {


                                notiEvents = HoldNotificationToEvent.getQuickList(curNotifications, document);


                                notiEvents.sort(new CompareByDate());
                                notifyDataSetChanged();


                                callback.onFinish();

                            }

                            @Override
                            public void onFailure(Exception e) {

                            }
                        });


                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                });

            }

            @Override
            public void onFailure(Exception e) {

            }
        });

    }
}
