package ca.cmput301t05.placeholder.ui.events.organizer_info;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.events.EventAdapter;

/**
 * Adapter for displaying attendee check-in information in a RecyclerView.

 */
public class AttendanceViewAdapter extends RecyclerView.Adapter<AttendanceViewAdapter.AttendanceCardViewHolder>{

    private HashMap<String, Double> attendDisplayed;
    private final Context context;

    public AttendanceViewAdapter(Context context, HashMap<String, Double> attendDisplayed){
        Log.e("amirza2","IM IN ADAPTER!!");
        this.attendDisplayed = attendDisplayed;
        this.context = context;
    }


    @NonNull
    @Override
    public AttendanceViewAdapter.AttendanceCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.attendee_checkin_item, parent, false);

        return new AttendanceViewAdapter.AttendanceCardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceViewAdapter.AttendanceCardViewHolder holder, int position) {
        try {
            holder.bindView(position);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        if (attendDisplayed == null) return 0;
        else return attendDisplayed.size();
    }

    public class AttendanceCardViewHolder extends RecyclerView.ViewHolder {

        TextView attendeeName, checkinCount;
        LinearLayout hiddenLinearLayout;
        CardView cardView;

        public AttendanceCardViewHolder(@NonNull View itemView) {
            super(itemView);

            attendeeName = itemView.findViewById(R.id.attendee_name);
            checkinCount = itemView.findViewById(R.id.hiddenCountText);
            hiddenLinearLayout = itemView.findViewById(R.id.lytHidden);

            cardView = itemView.findViewById(R.id.attendee_card);
            Log.e("amirza2","Got into this method");
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("amirza2","CLICKED");

                    if (hiddenLinearLayout.getVisibility() == View.VISIBLE) {
                        Log.e("amirza2","1st if");
                        hiddenLinearLayout.setVisibility(View.GONE);
                    } else {
                        hiddenLinearLayout.setVisibility(View.VISIBLE);
                        Log.e("amirza2","CLICKED");
                    }
                }
            });


        }

        public void bindView(int position){

            List<String> keysList = new ArrayList<>(attendDisplayed.keySet());
            String name = keysList.get(position);
            attendeeName.setText(name);
            Log.e("amirza2","ABOUT TO SET THIS TEXT!");
            int check_in_count = (int) Double.parseDouble(attendDisplayed.get(name).toString());
            if (check_in_count == 1){
                checkinCount.setText("Checked in "+ check_in_count+" time.");
            }
            else {
                checkinCount.setText("Checked in " + check_in_count + " times.");
                Log.e("amirza2", "SET THE TEXT!");
            }

        }
    }
}
