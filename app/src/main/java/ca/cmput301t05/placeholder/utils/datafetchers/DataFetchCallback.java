package ca.cmput301t05.placeholder.utils.datafetchers;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import ca.cmput301t05.placeholder.PlaceholderApp;
import ca.cmput301t05.placeholder.database.images.BaseImageHandler;
import ca.cmput301t05.placeholder.database.tables.Table;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.profile.Profile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public interface DataFetchCallback {
    void onProfileFetched(Profile profile);

    void onPictureLoaded(Bitmap bitmap);

    void onProfileFetchFailure(Exception exc);

    void onNoIdFound();

    void onEventFetched(Profile profile);

    void onEventFetchError(Exception exception);
}

