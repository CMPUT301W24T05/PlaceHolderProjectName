package ca.cmput301t05.placeholder.ui.events.organizer_info;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
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

public class AttendanceViewAdapter extends RecyclerView.Adapter<AttendanceViewAdapter.AttendanceCardViewHolder>{

    private HashMap<String, Double> attendDisplayed;
    private final Context context;

    public AttendanceViewAdapter(Context context, HashMap<String, Double> attendDisplayed){
        this.attendDisplayed = attendDisplayed;
        this.context = context;
    }


    @NonNull
    @Override
    public AttendanceViewAdapter.AttendanceCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.event_card, parent, false);

        return new AttendanceViewAdapter.AttendanceCardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceViewAdapter.AttendanceCardViewHolder holder, int position) {
        try {
            holder.bindView(position);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkinCount.getVisibility() == View.VISIBLE) {
                    holder.checkinCount.setVisibility(View.GONE);
                } else {
                    holder.checkinCount.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (attendDisplayed == null) return 0;
        else return attendDisplayed.size();
    }

    public class AttendanceCardViewHolder extends RecyclerView.ViewHolder {

        TextView attendeeName, checkinCount;
        CardView cardView;

        public AttendanceCardViewHolder(@NonNull View itemView) {
            super(itemView);

            attendeeName = itemView.findViewById(R.id.attendee_name);
            checkinCount = itemView.findViewById(R.id.hiddenCountText);
            cardView = itemView.findViewById(R.id.attendee_card);
        }

        public void bindView(int position){

            List<String> keysList = new ArrayList<>(attendDisplayed.keySet());
            String name = keysList.get(position);
            attendeeName.setText(name);
            checkinCount.setText(attendDisplayed.get(name).toString());

        }
    }
}
