package org.andresoviedo.app.model3D.DevTools;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;

import org.nmmu.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static androidx.core.app.ActivityCompat.requestPermissions;
import static androidx.core.content.ContextCompat.checkSelfPermission;
import static androidx.core.content.ContextCompat.getSystemService;

public class NetworkManager {

    private static NetworkManager instance = new NetworkManager();

    public static final String GitBaseLink = "https://raw.githubusercontent.com/nnmuApp/mobile_data_storage/master/";
    public static final String GitCompatIndexLink = GitBaseLink + "compat/index.txt";
    public static final String GitARIndexLink = GitBaseLink + "AR/index.txt";

    public static final String pages_link_template = "https://raw.githubusercontent.com/jolyDev/muse_data_storage/main/Atlas/p";
    public static final String pages_link_ext = ".jpg";
    public static final int PagesCount = 49;

    public static NetworkManager GetInstance()
    {
        if (instance == null)
            instance = new NetworkManager();

        return instance;
    }

    public void DownloadPage(int index, DownloadManager manager)
    {
        if (index < 0 || index >= links.size())
            return;

        String url = links.get(index);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle(LocalStorageManager.GetInstance().GetFileNameForPage(index));
        request.setDescription("Downloading page file ...");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                LocalStorageManager.GetInstance().GetFileNameForPage(index));
                //.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        manager.enqueue(request);
    }

    ArrayList<String> links = new ArrayList<String>();

    public NetworkManager()
    {
        for (int i = 0; i < PagesCount; i++)
        {
            String space_holder_char = i < 10 ? "0" : "";
            links.add(pages_link_template + space_holder_char + i + pages_link_ext);
        }
    }
}
