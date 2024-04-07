package ca.cmput301t05.placeholder.utils.datafetchers;

import android.content.Context;
import androidx.annotation.NonNull;
import ca.cmput301t05.placeholder.PlaceholderApp;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFetcher {
    protected final PlaceholderApp app;
    @NonNull
    protected final Context context;
    protected final List<DataFetchCallback> callbacks;

    public AbstractFetcher(PlaceholderApp app, @NonNull Context context) {
        this.app = app;
        this.context = context;
        this.callbacks = new ArrayList<>();
    }

    public void addCallback(DataFetchCallback callback) {
        this.callbacks.add(callback);
    }

    public void removeCallback(DataFetchCallback callback) {
        this.callbacks.remove(callback);
    }
}
