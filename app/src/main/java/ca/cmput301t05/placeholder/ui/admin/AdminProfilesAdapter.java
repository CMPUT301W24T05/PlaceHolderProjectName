package ca.cmput301t05.placeholder.ui.admin;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdminProfilesAdapter extends RecyclerView.Adapter<AdminProfilesAdapter.ProfileHolder> {



    @NonNull
    @Override
    public ProfileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ProfileHolder extends RecyclerView.ViewHolder{

        public ProfileHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
