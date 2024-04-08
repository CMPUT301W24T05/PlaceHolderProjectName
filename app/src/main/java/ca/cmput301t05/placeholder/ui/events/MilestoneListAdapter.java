package ca.cmput301t05.placeholder.ui.events;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.notifications.Milestone;
import ca.cmput301t05.placeholder.ui.events.organizer_info.AttendanceViewAdapter;
/**
 * Adapter for populating a RecyclerView with milestone data.
 */
public class MilestoneListAdapter extends RecyclerView.Adapter<MilestoneListAdapter.ViewHolder> {

    private ArrayList<String> mData;
    private LayoutInflater mInflater;

    private final Context context;

    /**
     * Constructor for MilestoneListAdapter.
     * @param context The context of the activity or fragment.
     * @param data List of milestone data to be displayed.
     */
    MilestoneListAdapter(Context context, ArrayList<String> data) {
        this.context = context;
        this.mData = data;
    }


    @NonNull
    @Override
    public MilestoneListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.milestone_card, parent, false);
        return new MilestoneListAdapter.ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull MilestoneListAdapter.ViewHolder holder, int position) {
        try {
            holder.bindView(position);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public int getItemCount() {

        if (mData== null)
            return 0;
        else{
            return mData.size();
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView milestoneName;

        ViewHolder(View itemView) {
            super(itemView);
            milestoneName = itemView.findViewById(R.id.miles_text);
        }


        public void bindView(int position){
            String milestoneText = mData.get(position);

            milestoneName.setText(mData.get(position));


        }


    }
}
