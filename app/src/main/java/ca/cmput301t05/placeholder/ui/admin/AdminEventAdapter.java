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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Calendar;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.DatabaseManager;
import ca.cmput301t05.placeholder.database.images.BaseImageHandler;
import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.events.EventAdapter;
import ca.cmput301t05.placeholder.profile.Profile;
import ca.cmput301t05.placeholder.utils.DateStrings;
import ca.cmput301t05.placeholder.utils.StringManip;

public class AdminEventAdapter extends RecyclerView.Adapter<AdminEventAdapter.EventHolder> {

    private ArrayList<Event> events;
    private DocumentSnapshot lastViewed;
    private PlaceholderApp app;

    private boolean isLoading = false;

    private Context context;

    public interface OnItemClickListener{
        void onItemClick(Event event);
    }
    private OnItemClickListener listener;
    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public AdminEventAdapter(Context context){
        this.context = context;
        this.app = (PlaceholderApp) this.context.getApplicationContext();

        events = new ArrayList<>();
        loadEvents();
    }


    @NonNull
    @Override
    public EventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.admin_event_card, parent, false);

        return new AdminEventAdapter.EventHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EventHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class EventHolder extends RecyclerView.ViewHolder{

        private ImageView poster, menu;
        private TextView name, id, date, location, registeredNumber, attendingNumber, description, organizer;

        public EventHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.admin_event_card_event_name);
            id = itemView.findViewById(R.id.admin_event_card_event_id);
            date = itemView.findViewById(R.id.admin_event_card_event_date);
            location = itemView.findViewById(R.id.admin_event_card_event_location);
            registeredNumber = itemView.findViewById(R.id.admin_event_card_registered_num);
            attendingNumber = itemView.findViewById(R.id.admin_event_card_attending_num);
            //description = itemView.findViewById(R.id.admin_event_card_event_description);
            organizer = itemView.findViewById(R.id.admin_event_card_organizer);
            poster = itemView.findViewById(R.id.admin_event_card_poster);
            menu = itemView.findViewById(R.id.admin_event_card_menu);


        }

        public void bindView(int position){

            Event event = events.get(position);

            name.setText(event.getEventName());
            id.setText(event.getEventID().toString());

            Calendar c = event.getEventDate();

            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            int hour = c.get(Calendar.HOUR);
            int amPM = c.get(Calendar.AM_PM);

            String monthName = DateStrings.getMonthName(month);
            String dayString = String.valueOf(day);

            String amOrPmString = DateStrings.getAmPM(amPM);
            String hourString = String.valueOf(hour);

            String formatTime = hourString + " " + amOrPmString + ",  " + monthName + " " + dayString;

            date.setText(formatTime);

            location.setText(event.getEventLocation());
            registeredNumber.setText(String.valueOf(event.getRegisteredUsersNum()));
            attendingNumber.setText(String.valueOf(event.getAttendeesNum()));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(event);
                }
            });

            //description.setText(StringManip.truncateString(event.getEventInfo(), 50));

            app.getProfileTable().fetchDocument(event.getEventCreator().toString(), new Table.DocumentCallback<Profile>() {
                @Override
                public void onSuccess(Profile document) {
                    String organizerFormat = "Organizer: " + document.getName();
                    organizer.setText(organizerFormat);
                }

                @Override
                public void onFailure(Exception e) {
                    Log.e("Event_Adapter_Profile", e.getMessage());
                }
            });

            app.getPosterImageHandler().getPosterPicture(event, context.getApplicationContext(), new BaseImageHandler.ImageCallback() {
                @Override
                public void onImageLoaded(Bitmap bitmap) {
                    Glide.with(context)
                            .load(bitmap)
                            .centerCrop()
                            .apply(new RequestOptions().override(800, 800))
                            .into(poster);
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


                                if (event.getEventPosterID() != null){
                                    //remove image

                                    app.getPosterImageHandler().removeEventPoster(event, context.getApplicationContext(), new BaseImageHandler.ImageDeletionCallback() {
                                        @Override
                                        public void onImageDeleted() {


                                            app.getEventTable().deleteDocument(event.getEventID().toString(), new Table.DocumentCallback() {
                                                @Override
                                                public void onSuccess(Object document) {
                                                    events.remove(position);
                                                    notifyDataSetChanged();

                                                    for (String p : event.getAttendees()){

                                                        //remove the event from the profiles array list
                                                        DocumentReference docref = DatabaseManager.getInstance().getDb().collection("profiles").document(p);

                                                        docref.update("joinedEvents", FieldValue.arrayRemove(event.getEventID().toString())).addOnCompleteListener(task -> {

                                                           if (!task.isSuccessful()){
                                                               Log.d("Ghost_Event", "Could not be deleted");
                                                           }

                                                        });

                                                        DatabaseManager.getInstance().getDb().collection("profiles").document(p);

                                                        docref.update("hostedEvents", FieldValue.arrayRemove(event.getEventID().toString())).addOnCompleteListener(task -> {

                                                            if (!task.isSuccessful()){
                                                                Log.d("Ghost_Event", "Could not be deleted");
                                                            }

                                                        });



                                                    }

                                                }

                                                @Override
                                                public void onFailure(Exception e) {
                                                    Log.d("Event", e.getMessage());
                                                }
                                            });

                                        }

                                        @Override
                                        public void onError(Exception e) {
                                            Log.d("Event", e.getMessage());
                                        }
                                    });

                                }  else {

                                    app.getEventTable().deleteDocument(event.getEventID().toString(), new Table.DocumentCallback() {
                                        @Override
                                        public void onSuccess(Object document) {
                                            events.remove(position);
                                            notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onFailure(Exception e) {
                                            Log.d("Event", e.getMessage());
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

    private void loadEvents(){

        if (isLoading){
            return;
        }

        Log.d("Event_Adapter", "Loading Events");

        isLoading = true;

        Query query = app.getEventTable().getCollectionReference().limit(10);

        //essentially so we start after our last viewed image
        if (lastViewed != null){
            query.startAfter(lastViewed);
        }

        query.get().addOnCompleteListener(task -> {

            if (task.isSuccessful()){
                //put all images we're looking at into list
                for (DocumentSnapshot document : task.getResult()){

                    Event event = new Event(document);
                    events.add(event);
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

    public void setEventRefresh(RecyclerView recyclerView) {

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
                    loadEvents();
                }
            }
        });

    }

}

