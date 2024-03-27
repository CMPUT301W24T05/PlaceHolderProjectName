package ca.cmput301t05.placeholder.ui.admin;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Calendar;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.ImageDetails.ImageDetails;
import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.utils.DateStrings;


//TODO maybe add a feature to refresh the page
public class ViewAllImagesAdapter extends RecyclerView.Adapter<ViewAllImagesAdapter.ViewAllImagesHolder> {

    private ArrayList<ImageDetails> imageDetails;
    private DocumentSnapshot lastViewed;
    private boolean isLoading = false;

    private Context context;

    private PlaceholderApp app;

    public ViewAllImagesAdapter(Context context){
        this.context = context;
        this.app = (PlaceholderApp) this.context.getApplicationContext();
        imageDetails = new ArrayList<>();
        loadImages();

    }

    @NonNull
    @Override
    public ViewAllImagesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.admin_image_card, parent, false);

        return new ViewAllImagesHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewAllImagesHolder holder, int position) {
        try {
            holder.bindView(position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return imageDetails.size();
    }

    public class ViewAllImagesHolder extends RecyclerView.ViewHolder{

         private TextView date;
         private ImageView menu;
         private ImageView picture;


        public ViewAllImagesHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.admin_image_card_date);
            menu = itemView.findViewById(R.id.admin_image_card_menu);
            picture = itemView.findViewById(R.id.admin_image_card_image);


        }

        public void bindView(int position) {

            ImageDetails details = imageDetails.get(position);


            Calendar c = details.getUploadTime();

            String month = DateStrings.getMonthName(c.get(Calendar.MONTH));
            String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
            String amPm = DateStrings.getAmPM(c.get(Calendar.AM_PM));
            String hour = String.valueOf(c.get(Calendar.HOUR));
            String year = String.valueOf(c.get(Calendar.YEAR));

            String dateText = hour + amPm + " " + day + " " + month + " " + year;
            date.setText(dateText);

            //load into imageview
            Glide.with(context).load(details.getImageUri()).centerCrop().into(picture);

            //allow for deletion
            menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("ImageAdapter", "Menu clicked at position: " + position);
                    PopupMenu popupMenu = new PopupMenu(context, view);
                    popupMenu.getMenuInflater().inflate(R.menu.admin_image_card_menu, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {

                            if (menuItem.getItemId() == R.id.admin_image_card_menu_delete){

                                //TODO Make a confirmation dialog pop up
                                Log.d("menu", "Deleting image");

                                app.getImageDetailTable().deleteImage(details, new Table.DocumentCallback() {
                                    @Override
                                    public void onSuccess(Object document) {
                                        int curpos = getAdapterPosition();
                                        imageDetails.remove(curpos);
                                        notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onFailure(Exception e) {

                                    }
                                });



                                return true;
                            }
                            return false;
                        }
                    });

                    popupMenu.show();
                }
            });


        }
    }

    private void loadImages(){
        //basically so we dont keep loading
        if (isLoading){
            return;
        }
        isLoading = true;

        //grab the first 10 images, might have to reduce to whatever is fitting on the screen
        Query query = app.getImageDetailTable().getCollectionReference()
                .orderBy("uploadTime", Query.Direction.DESCENDING)
                .limit(10);

        //essentially so we start after our last viewed image
        if (lastViewed != null){
            query.startAfter(lastViewed);
        }

        query.get().addOnCompleteListener(task -> {

            if (task.isSuccessful()){
                //put all images we're looking at into list
                for (DocumentSnapshot document : task.getResult()){

                    ImageDetails details = new ImageDetails(document);
                    imageDetails.add(details);
                }

                notifyDataSetChanged();

                //now get the last indice so we can set it as the last thing we viewed
                int lastVisible = task.getResult().size() - 1;

                if (lastVisible >= 0) {
                    lastViewed = task.getResult().getDocuments().get(lastVisible);
                }

                isLoading = false;


            }   else {

                Log.e("ViewAllImages", "Error getting documents: ", task.getException());
                isLoading = false;
            }


        });


    }


    public void setImageRefresh(RecyclerView recyclerView){

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (!(recyclerView.getLayoutManager() instanceof LinearLayoutManager)) {
                    return; // Only works with LinearLayoutManager and its subclasses
                }
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItemCount = layoutManager.getItemCount();
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();

                if (!isLoading && totalItemCount <= (lastVisibleItemPosition + 3)) { // 3 images before end we reload images
                    loadImages();
                }
            }
        });

    }
}
