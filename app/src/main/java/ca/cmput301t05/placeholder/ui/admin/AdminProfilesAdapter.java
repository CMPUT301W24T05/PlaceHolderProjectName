package ca.cmput301t05.placeholder.ui.admin;

import android.content.Context;
import android.graphics.Bitmap;
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
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.ImageDetails.ImageDetails;
import ca.cmput301t05.placeholder.database.images.BaseImageHandler;
import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.profile.Profile;

public class AdminProfilesAdapter extends RecyclerView.Adapter<AdminProfilesAdapter.ProfileHolder> {

    private ArrayList<Profile> profiles;
    private PlaceholderApp app;
    private DocumentSnapshot lastViewed;
    private boolean isLoading = false;

    private Context context;
    /**
     * Constructs a new AdminProfilesAdapter with the given context.
     * @param context The context in which the adapter is being used.
     */
    public AdminProfilesAdapter(Context context){
        this.context = context;
        this.app = (PlaceholderApp) this.context.getApplicationContext();

        profiles = new ArrayList<>();

        loadProfiles();
    }
    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ProfileHolder that holds a View with the profile information.
     */
    @NonNull
    @Override
    public ProfileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.profile_card, parent, false);

        return new AdminProfilesAdapter.ProfileHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileHolder holder, int position) {
        try {
            holder.bindView(position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Returns the total number of profiles in the data set held by the adapter.
     * @return The total number of profiles in the data set.
     */
    @Override
    public int getItemCount() {
        return profiles.size();
    }

    public class ProfileHolder extends RecyclerView.ViewHolder{

        ImageView pic, menu;
        TextView name, id, contactInfo, homepage;

        public ProfileHolder(@NonNull View itemView) {
            super(itemView);

            pic = itemView.findViewById(R.id.profile_card_image);
            menu = itemView.findViewById(R.id.profile_card_menu);

            name = itemView.findViewById(R.id.profile_card_name);
            id = itemView.findViewById(R.id.profile_card_profile_id);
            contactInfo = itemView.findViewById(R.id.profile_card_contact);
            homepage = itemView.findViewById(R.id.profile_card_homepage);

        }

        public void bindView(int position){

             Profile profile = profiles.get(position);


            name.setText(profile.getName());
            id.setText(profile.getProfileID().toString());

            contactInfo.setText(profile.getContactInfo());
            homepage.setText(profile.getHomePage());

            //load images
            app.getProfileImageHandler().getProfilePicture(profile, context, new BaseImageHandler.ImageCallback() {
                @Override
                public void onImageLoaded(Bitmap bitmap) {

                    Glide.with(context)
                            .load(bitmap)
                            .centerCrop()
                            .apply(new RequestOptions().override(500, 500))
                            .into(pic);

                }

                @Override
                public void onError(Exception e) {

                }
            });




             menu.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {

                     PopupMenu popupMenu = new PopupMenu(context, view);
                     popupMenu.getMenuInflater().inflate(R.menu.admin_image_card_menu, popupMenu.getMenu());
                     popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                         @Override
                         public boolean onMenuItemClick(MenuItem menuItem) {

                             if (menuItem.getItemId() == R.id.admin_image_card_menu_delete){

                                 //TODO Make a confirmation dialog pop up


                                 if (profile.getProfilePictureID() != null){
                                     //remove image

                                     app.getProfileImageHandler().removeProfilePic(profile, context, new BaseImageHandler.ImageDeletionCallback() {
                                         @Override
                                         public void onImageDeleted() {


                                             app.getProfileTable().deleteDocument(profile.getProfileID().toString(), new Table.DocumentCallback() {
                                                 @Override
                                                 public void onSuccess(Object document) {
                                                     profiles.remove(position);
                                                     notifyDataSetChanged();

                                                 }

                                                 @Override
                                                 public void onFailure(Exception e) {
                                                     Log.d("Profile", e.getMessage());
                                                 }
                                             });

                                         }

                                         @Override
                                         public void onError(Exception e) {
                                             Log.d("Profile", e.getMessage());
                                         }
                                     });

                                 }  else {

                                     app.getProfileTable().deleteDocument(profile.getProfileID().toString(), new Table.DocumentCallback() {
                                         @Override
                                         public void onSuccess(Object document) {
                                             profiles.remove(position);
                                             notifyDataSetChanged();
                                         }

                                         @Override
                                         public void onFailure(Exception e) {
                                             Log.d("Profile", e.getMessage());
                                         }
                                     });
                                 }

                                 return true;
                             }


                             return true;
                         }


                     });
                     popupMenu.show();

                 }
             });



        }


    }
    /**
     * Loads profiles from Firestore database.
     */
    private void loadProfiles(){

        if (isLoading){
            return;
        }

        isLoading = true;

        Query query = app.getProfileTable().getCollectionReference().limit(10);

        //essentially so we start after our last viewed image
        if (lastViewed != null){
            query.startAfter(lastViewed);
        }

        query.get().addOnCompleteListener(task -> {

            if (task.isSuccessful()){
                //put all images we're looking at into list
                for (DocumentSnapshot document : task.getResult()){

                    Profile profile = new Profile(document);
                    profiles.add(profile);
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
    /**
     * Sets up a scroll listener for the RecyclerView to trigger profile data refresh when scrolling near the end of the list.
     * @param recyclerView The RecyclerView to which the scroll listener will be attached.
     */
    public void setProfileRefresh(RecyclerView recyclerView){

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
                    loadProfiles();
                }
            }
        });


    }
}


