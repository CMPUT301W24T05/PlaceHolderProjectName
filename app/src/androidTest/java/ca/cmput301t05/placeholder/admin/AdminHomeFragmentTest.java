package ca.cmput301t05.placeholder.admin;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.Query;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ca.cmput301t05.placeholder.ui.admin.AdminHomeFragment;

public class AdminHomeFragmentTest {

    private AdminHomeFragment fragment;

    @Before
    public void setUp() {
        fragment = new AdminHomeFragment();
    }

    @Test
    public void testProfileCount() {
        // Mock the fragment's view
        View mockView = Mockito.mock(View.class);
        fragment.onViewCreated(mockView, null);

        // Mock TextView for profile count
        TextView profileCount = Mockito.mock(TextView.class);
        when(mockView.findViewById(anyInt())).thenReturn(profileCount);

        // Mock queries and tasks
        Query profilesQuery = Mockito.mock(Query.class);
        AggregateQuery profilesCountQuery = Mockito.mock(AggregateQuery.class);
        Task<AggregateQuerySnapshot> profilesTask = Mockito.mock(Task.class);
        AggregateQuerySnapshot profileSnapshot = Mockito.mock(AggregateQuerySnapshot.class);

        when(profilesQuery.count()).thenReturn(profilesCountQuery);
        when(profilesCountQuery.get(AggregateSource.SERVER)).thenReturn(profilesTask);
        when(profilesTask.isSuccessful()).thenReturn(true);
        when(profilesTask.getResult()).thenReturn(profileSnapshot);
        when(profileSnapshot.getCount()).thenReturn(5L);

        // Invoke the method under test
        fragment.onViewCreated(mockView, null);

        // Verify that the TextView is updated with the correct count
        verify(profileCount).setText("5");
    }
}
