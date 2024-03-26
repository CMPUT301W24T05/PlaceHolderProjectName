package ca.cmput301t05.placeholder.ui.admin;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewAllImagesAdapter extends RecyclerView.Adapter<ViewAllImagesAdapter.ViewAllImagesHolder> {


    @NonNull
    @Override
    public ViewAllImagesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewAllImagesHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewAllImagesHolder extends RecyclerView.ViewHolder{

        public ViewAllImagesHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
