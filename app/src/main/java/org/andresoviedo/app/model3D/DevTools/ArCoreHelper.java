package org.andresoviedo.app.model3D.DevTools;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.ar.core.ArCoreApk;
import com.google.ar.core.exceptions.UnavailableDeviceNotCompatibleException;
import com.google.ar.core.exceptions.UnavailableUserDeclinedInstallationException;

import org.andresoviedo.app.model3D.view.MenuActivity;
import org.andresoviedo.lang.Tokens;

import static com.google.ar.core.ArCoreApk.InstallStatus.INSTALLED;

public class ArCoreHelper {
    /**
     * Check ArCoreApk.checkAvailability() to determine if the specific AR functionality can be used.
     */
    public static boolean isArCoreSupported(final android.content.Context context) {
        ArCoreApk.Availability availability = ArCoreApk.getInstance().checkAvailability(context);
        if (availability.isTransient()) {
            // Re-query at 5Hz while compatibility is checked in the background.
            new Handler()
                    .postDelayed(
                            new Runnable() {
                                @Override
                                public void run() {
                                    isArCoreSupported(context);
                                }
                            },
                            200);
        }
        if (!availability.isSupported()) { // Unsupported or unknown.
            Toast.makeText(context, Tokens.holo, Toast.LENGTH_LONG)
                    .show();
            return false;
        }
        return true;
    }

    /**
     * Check ArCoreApk.checkAvailability() to determine if the specific AR functionality can be used.
     */
    public static boolean isArCoreInstalled(Activity applicationActivity) {
        try {
            switch (ArCoreApk.getInstance().requestInstall(applicationActivity, true)) {
                case INSTALLED:
                    // Success, create the AR session.
                    return true;
                case INSTALL_REQUESTED:
                    return false;
            }
        } catch (UnavailableUserDeclinedInstallationException
                | UnavailableDeviceNotCompatibleException e) {
            Log.e(applicationActivity.getClass().getSimpleName(), "Exception: " + e.toString());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void showArObject(final android.content.Context context, String file, String title) {
        Intent sceneViewerIntent = new Intent(Intent.ACTION_VIEW);
        Uri intentUri =
                Uri.parse("https://arvr.google.com/scene-viewer/1.0")
                        .buildUpon()
                        .appendQueryParameter(
                                "file",
                                file)
                        .build();
        Log.i("in helper", "link=" + intentUri);
        sceneViewerIntent.setData(intentUri);
        sceneViewerIntent.setPackage("com.google.android.googlequicksearchbox");
        context.startActivity(sceneViewerIntent);
    }


    public static boolean checkAR_Permission(MenuActivity activity)
    {
        ArCoreApk.Availability availability = ArCoreApk.getInstance().checkAvailability(activity.getApplicationContext());
        if(availability.isSupported())
        {
            try {
                return ArCoreApk.getInstance().requestInstall(activity, true) == INSTALLED;
            }
            catch (Exception e) {
                // uninstalled for somereason
                return false;
            }
        }
        else if (availability.isTransient()) {
            // Re-query at 5Hz while compatibility is checked in the background.
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkAR_Permission(activity);
                    activity.UpdateMenuItems();
                }
            }, 200);
        }
        return false;
    }

}

