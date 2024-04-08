package ca.cmput301t05.placeholder.ui.events;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.milestones.Milestone;


import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.UUID;

public class ViewMilestonesActivity extends AppCompatActivity {


    private PlaceholderApp app;
    private Event curEvent;
    private ArrayList<ca.cmput301t05.placeholder.milestones.Milestone> milestones;
    private Button back;


    private ImageView check_sign, check_attendee, check_halfway, check_full, check_start;

    private int progress;
    private ProgressBar milestoneBar;



    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_milestones);
        app = (PlaceholderApp) getApplicationContext();
        curEvent = app.getCachedEvent();


        milestones = getMilestones(curEvent);
        milestoneBar = (ProgressBar) findViewById(R.id.simpleProgressBar);
        progress = 0;


        //setMilestones();
        //setCheckBoxes();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    public ArrayList<Milestone> getMilestones(Event event){
        ArrayList<Milestone> Miles = new ArrayList<Milestone>();
        UUID eventID = event.getEventID();
        for(Milestone mile : app.getUserMilestones()){
            if (mile.getEventID().compareTo(eventID.toString()) == 0){
                Miles.add(mile);
            }
        }
        return Miles;

    }

    public void setMilestonesBar(){
        if (milestones != null) {
            for (Milestone milestone : milestones) {
                switch (milestone.getMType()) {
                    case FIRSTATTENDEE:
                        progress = progress +10;
                        break;
                    case FIRSTSIGNUP:
                        progress += 10;
                        break;
                    case HALFWAY:
                        progress += 10;
                        break;
                    case FULLCAPACITY:
                        progress += 10;
                        break;
                        //add checkbox for event start
                }
            }
            milestoneBar.setProgress(progress);

        }
    }
}
