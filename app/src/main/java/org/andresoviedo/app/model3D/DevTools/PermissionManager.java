package org.andresoviedo.app.model3D.DevTools;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import org.andresoviedo.util.android.AndroidUtils;

import static androidx.core.app.ActivityCompat.requestPermissions;

public class PermissionManager {
    public static void CheckAndAskAll(Activity activity)
    {
        if (!AndroidUtils.checkPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE))
            AndroidUtils.requestPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, 1);

        if (!AndroidUtils.checkPermission(activity, android.Manifest.permission.INTERNET))
            AndroidUtils.requestPermission(activity, android.Manifest.permission.INTERNET, 1);

        if (!AndroidUtils.checkPermission(activity, android.Manifest.permission.CAMERA))
            AndroidUtils.requestPermission(activity, android.Manifest.permission.CAMERA, 1);
    }
}
