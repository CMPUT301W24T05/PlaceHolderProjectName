package ca.cmput301t05.placeholder.ui.notifications;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.notifications.UserNotificationAdapter;

public class UserNotificationFragment extends DialogFragment {

    TextView nameText;
    ImageView back;
    RecyclerView recyclerView;

    Context context;

    PlaceholderApp app;

    public UserNotificationFragment(){
        super(R.layout.recycler_with_back);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.recycler_with_back, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getContext();
        app = (PlaceholderApp) context.getApplicationContext();

        nameText = view.findViewById(R.id.recycler_page_name_text);
        back = view.findViewById(R.id.recycler_page_back);
        recyclerView = view.findViewById(R.id.recycler_page_list);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hopefully this allows us to go back
                dismiss();
            }
        });

        nameText.setText("Notifications");


        UserNotificationAdapter adapter = new UserNotificationAdapter(context, app.getNotificationEventHolder());

        recyclerView.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);

        adapter.updateList(new UserNotificationAdapter.UserNotificationCallback() {
            @Override
            public void onFinish() {

            }

            @Override
            public void onError(Exception e) {

            }
        });


    }
}
