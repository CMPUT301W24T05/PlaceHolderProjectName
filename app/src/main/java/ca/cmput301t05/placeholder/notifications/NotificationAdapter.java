package ca.cmput301t05.placeholder.notifications;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.utils.DateStrings;
import ca.cmput301t05.placeholder.utils.StringManip;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationCardViewHolder> {


    private ArrayList<Notification> notificationList;

    private final Context context;

    public NotificationAdapter(Context context, ArrayList<Notification> notifications){

        this.notificationList = new ArrayList<>();
        this.notificationList.addAll(notifications);

        this.context = context;

    }

    @NonNull
    @Override
    public NotificationCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.notification_recylerlist_card, parent, false);

        return new NotificationCardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationCardViewHolder holder, int position) {
        try {
            holder.bindView(position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (notificationList == null) return 0;
        else return notificationList.size();
    }


    public class NotificationCardViewHolder extends RecyclerView.ViewHolder {

        TextView notification_time, notification_message;
        ImageView menu; //this will be used to pin


        public NotificationCardViewHolder(@NonNull View itemView) {
            super(itemView);

            notification_time = itemView.findViewById(R.id.notification_card_time);
            notification_message = itemView.findViewById(R.id.notification_card_message);

            menu = itemView.findViewById(R.id.notification_card_triple);
        }

        public void bindView(int position){
            Notification n = notificationList.get(position);
            assert n != null;

            //format the event time here dont need to check for timezone because calendar does that automatically
            Calendar c = n.getTimeCreated();

            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            int hour = c.get(Calendar.HOUR_OF_DAY);
            int amPM = c.get(Calendar.AM_PM);

            String monthName = DateStrings.getMonthName(month);
            String dayString = String.valueOf(day);

            String amOrPmString = DateStrings.getAmPM(amPM);
            String hourString = String.valueOf(hour);

            String formatNotifTime = hourString + " " + amOrPmString + "  " + dayString + ", " + monthName;

            notification_time.setText(formatNotifTime);

            //truncate message
            notification_message.setText(StringManip.truncateString(n.getMessage(), 50)); //max length 50 chars (then adds ...) so 53 total
        }


    }

}
