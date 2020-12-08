package org.andresoviedo.app.model3D.DevTools;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;

import org.andresoviedo.dddmodel2.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static androidx.core.app.ActivityCompat.requestPermissions;
import static androidx.core.content.ContextCompat.checkSelfPermission;
import static androidx.core.content.ContextCompat.getSystemService;

public class NetworkManager {

    private static NetworkManager instance = new NetworkManager();

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
        links.add("https://drive.google.com/uc?export=download&id=1nMcr9YsE3cM7biUAmUufvDduWRPAm6iS");
        links.add("https://drive.google.com/uc?export=download&id=1__qac764XcFuxiNtcu_CqmIoE0YIRQnV");
        links.add("https://drive.google.com/uc?export=download&id=1Gr3K3h5Dc-PxFIobBwLoPEAo2vzbKApb");
        links.add("https://drive.google.com/uc?export=download&id=1Ll_HaxqTwgOPycAQAfLHGiicD9UwmsPh");
        links.add("https://drive.google.com/uc?export=download&id=1TSPsbPY6jp8scoilixvOmBA31rP3G93y");
        links.add("https://drive.google.com/uc?export=download&id=1nMcr9YsE3cM7biUAmUufvDduWRPAm6iS");
    }
}
