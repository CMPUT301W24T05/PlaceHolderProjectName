package ca.cmput301t05.placeholder.ui.events;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.notifications.Milestone;

public class MilestoneListAdapter extends RecyclerView.Adapter<MilestoneListAdapter.ViewHolder> {

    private List<Milestone> mData;
    private LayoutInflater mInflater;

    // Data is passed into the constructor
    MilestoneListAdapter(Context context, List<Milestone> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // Inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.milestone_card, parent, false);
        return new ViewHolder(view);
    }

    // Binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Milestone milestone = mData.get(position);
        holder.milestoneName.setText(milestone.getMessage());
    }

    // Total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // Stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView milestoneName;

        ViewHolder(View itemView) {
            super(itemView);
            milestoneName = itemView.findViewById(R.id.miles_text);
        }
    }
}
