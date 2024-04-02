package ca.cmput301t05.placeholder.ui.admin;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.profile.Profile;

public class AdminEventAdapter extends RecyclerView.Adapter<AdminEventAdapter.EventHolder> {

    private ArrayList<Event> events;
    private DocumentSnapshot lastViewed;
    private PlaceholderApp app;

    private boolean isLoading = false;

    private Context context;



    public AdminEventAdapter(Context context){
        this.context = context;
        this.app = (PlaceholderApp) this.context.getApplicationContext();

        events = new ArrayList<>();
        loadEvents();
    }


    @NonNull
    @Override
    public EventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull EventHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class EventHolder extends RecyclerView.ViewHolder{

        private ImageView poster, menu;
        private TextView name, id, date, location, registeredNumber, attendingNumber, description, organizer;

        public EventHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private void loadEvents(){

        if (isLoading){
            return;
        }

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
}
