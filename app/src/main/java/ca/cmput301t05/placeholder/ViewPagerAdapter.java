package ca.cmput301t05.placeholder;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * A RecyclerView adapter for displaying ViewPager items.
 * This adapter is responsible for inflating the layout for each ViewPager item
 * and binding the data to the corresponding views.
 */
public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.Pager2ViewHolder> {
    private List<String> title;
    private List<Bitmap> qrImage;


    public ViewPagerAdapter(List<String> title, List<Bitmap> qrImage) {
        this.title = title;
        this.qrImage = qrImage;
    }

    @NonNull
    @Override
    public ViewPagerAdapter.Pager2ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewqr_fragment, parent, false);
        return new Pager2ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPagerAdapter.Pager2ViewHolder holder, int position) {
        holder.itemTitle.setText(title.get(position));
        holder.itemImage.setImageBitmap(qrImage.get(position));
    }

    @Override
    public int getItemCount() {
        return title.size();
    }

    public class Pager2ViewHolder extends RecyclerView.ViewHolder {

        TextView itemTitle;
        ImageView itemImage;

        public Pager2ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemTitle = itemView.findViewById(R.id.displayqr_type);
            itemImage = itemView.findViewById(R.id.qr_display);

        }



    }

}
