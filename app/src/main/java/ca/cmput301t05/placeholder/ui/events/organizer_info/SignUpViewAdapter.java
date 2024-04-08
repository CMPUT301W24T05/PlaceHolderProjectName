package ca.cmput301t05.placeholder.ui.events.organizer_info;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ca.cmput301t05.placeholder.R;
/**
 * Adapter for displaying sign-up information in a RecyclerView.
 */
public class SignUpViewAdapter extends RecyclerView.Adapter<SignUpViewAdapter.SignupCardViewHolder>{

    private ArrayList<String> signupNames;

    private final Context context;
    /**
     * Constructs a SignUpViewAdapter with the provided context and list of sign-up names.
     * @param context The context in which the adapter will be used.
     * @param signupNames A list containing the names of users who signed up for the event.
     */
    public SignUpViewAdapter(Context context, ArrayList<String> signupNames){
        Log.e("amirza2","IM IN ADAPTER!!");
        this.signupNames = signupNames;
        this.context = context;
    }

    @NonNull
    @Override
    public SignUpViewAdapter.SignupCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.signup_item_card, parent, false);

        return new SignUpViewAdapter.SignupCardViewHolder(v);
    }




    @Override
    public void onBindViewHolder(@NonNull SignUpViewAdapter.SignupCardViewHolder holder, int position) {
        try {
            holder.bindView(position);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        if (signupNames == null) return 0;
        else return signupNames.size();
    }

    public class SignupCardViewHolder extends RecyclerView.ViewHolder {

        TextView signeeName;

        CardView cardView;

        public SignupCardViewHolder(@NonNull View itemView) {
            super(itemView);

            signeeName = itemView.findViewById(R.id.signup_name);
            cardView = itemView.findViewById(R.id.signup_card);
            Log.e("amirza2","Got into this method");
        }

        public void bindView(int position){

            String name = signupNames.get(position);
            signeeName.setText(name); // Set each card to a name

        }
    }



}
