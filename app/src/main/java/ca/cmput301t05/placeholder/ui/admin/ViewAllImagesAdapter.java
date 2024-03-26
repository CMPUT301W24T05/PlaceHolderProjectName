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
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.ImageDetails.ImageDetails;

public class ViewAllImagesAdapter extends RecyclerView.Adapter<ViewAllImagesAdapter.ViewAllImagesHolder> {

    private ArrayList<ImageDetails> imageDetails;
    private DocumentSnapshot lastViewed;
    private boolean isLoading = false;

    private Context context;

    private PlaceholderApp app;

    public ViewAllImagesAdapter(Context context){
        this.context = context;
        this.app = (PlaceholderApp) this.context.getApplicationContext();


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
            menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(context, view);
                    popupMenu.getMenuInflater().inflate(R.menu.admin_image_card_menu, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {

                            if (menuItem.getItemId() == R.id.admin_image_card_menu_delete){

                                //handle deleting the photo




                                return true;
                            }
                            return false;
                        }
                    });
                }
            });


        }
    }

    public void loadImages(){
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
}
