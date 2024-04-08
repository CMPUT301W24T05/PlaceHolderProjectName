package ca.cmput301t05.placeholder.ui.notifications;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.R;
import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.notifications.Notification;
import ca.cmput301t05.placeholder.notifications.UserNotificationAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

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

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        //Disable the dialog to be dismissed when touched outside
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
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

                //update the notifications to show they're read
                for (Notification notification : app.getUserNotifications()){

                    app.getNotificationTable().pushDocument(notification, notification.getNotificationID().toString(), new Table.DocumentCallback<Notification>() {
                        @Override
                        public void onSuccess(Notification document) {

                        }

                        @Override
                        public void onFailure(Exception e) {

                        }
                    });

                }

                dismiss();
            }
        });

        nameText.setText("Notifications");

        //Somehow duplicates are propping up, not enough time to find issue quick fix

        Set<Notification> notificationSet = new HashSet<>();
        for (Notification n : app.getUserNotifications()){
            notificationSet.add(n);
        }

        app.getUserNotifications().clear();
        app.getUserNotifications().addAll(notificationSet);

        UserNotificationAdapter adapter = new UserNotificationAdapter(context, app.getUserNotifications());

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

        app.refreshNotifications(new PlaceholderApp.appCallback() {
            @Override
            public void onSuccess() {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure() {

            }
        });

        adapter.sortList();

    }
}
