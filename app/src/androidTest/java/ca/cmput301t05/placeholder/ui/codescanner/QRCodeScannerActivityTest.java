package ca.cmput301t05.placeholder.ui.codescanner;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
//import org.robolectric.annotation.Config;
import static java.util.EnumSet.allOf;
import static java.util.function.Predicate.not;

import android.app.Instrumentation;
import android.content.Context;
import android.content.pm.PackageManager;

import org.junit.Rule;
import org.junit.jupiter.api.Nested;
import org.junit.runner.RunWith;
//import org.robolectric.Robolectric;
import androidx.activity.result.ActivityResultLauncher;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
//import androidx.test.core.app.ApplicationProvider.getApplicationContext;
//import org.robolectric.Robolectric.buildActivity;

import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

//import com.google.ar.core.Config;

import com.budiyev.android.codescanner.CodeScanner;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import ca.cmput301t05.placeholder.MainActivity;
import ca.cmput301t05.placeholder.R;
//import ca.cmput301t05.placeholder.Manifest;
//import ca.cmput301t05.placeholder.Manifest;
import android.Manifest;
import android.util.Log;
import android.view.View;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class QRCodeScannerActivityTest {

  private ActivityScenario<QRCodeScannerActivity> scenario;



    @Rule
    public ActivityScenarioRule<QRCodeScannerActivity> rule = new
            ActivityScenarioRule<QRCodeScannerActivity>(QRCodeScannerActivity.class); // Will launch the following activity before each test
    @Rule
    public GrantPermissionRule permissionCamera = GrantPermissionRule.grant(Manifest.permission.CAMERA);
     //Will automatically grant camera permissions

    @Test
    public void testCameraFeedBeginAfterGrantPermissions() {

        GrantPermissionRule permissionCamera = GrantPermissionRule.grant(Manifest.permission.CAMERA);
        // Tests that the camera preview is displayed after permission are granted
        onView(ViewMatchers.withId(R.id.scanner_view))
                .check(matches(isDisplayed()));
    }

    @Nested
    @RunWith(MockitoJUnitRunner.class)
    public static class QRCodeScannerPermissionDenied  {



        public QRCodeScannerPermissionDenied() {
        }

        @Rule
        public ActivityScenarioRule<QRCodeScannerActivity> activityScenarioRule =
                new ActivityScenarioRule<>(QRCodeScannerActivity.class);

        @Test
        public void testPermissionDeniedDialogDisplayed() {
            // Get the activity scenario
            ActivityScenario<QRCodeScannerActivity> scenario = activityScenarioRule.getScenario();

            // Simulate denial of camera permissions by directly invoking the method
            scenario.onActivity(activity -> {

                activity.showPermissionDeniedDialog();
            });

            // Check if the dialog is displayed
            onView(withText("Permission Denied")).check(matches(isDisplayed()));
            onView(withText("This feature requires camera permission. Please enable it in the app settings.")).check(matches(isDisplayed()));

            // Click OK button on the dialog to dismiss it
            onView(withText("OK")).perform(click());
        }
    }













}

