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

        /*
        links.add("https://drive.google.com/uc?export=download&id=1nMcr9YsE3cM7biUAmUufvDduWRPAm6iS");
        links.add("https://drive.google.com/uc?export=download&id=1__qac764XcFuxiNtcu_CqmIoE0YIRQnV");
        links.add("https://drive.google.com/uc?export=download&id=1tkKpEnO-weWX6bai1GnckPAF6nnKd5Og");
        links.add("https://drive.google.com/uc?export=download&id=1Kumx0gMDApzIdVqLbamLQm9VvzIsC3o3");
        links.add("https://drive.google.com/uc?export=download&id=1HBEqOCTNkVtcMCWQKSW93JmLxNLvf826");
        links.add("https://drive.google.com/uc?export=download&id=1oaWEDnZXoraFuSCX6OIkUS_gdVb8s5A7");
        links.add("https://drive.google.com/uc?export=download&id=1O39prtHb_Rb0qyZB-sdNP3iPM-r5FC-b");
        links.add("https://drive.google.com/uc?export=download&id=10hWaCQ9BUKq1OmStBAFl-KvEILwY3WJh");
        links.add("https://drive.google.com/uc?export=download&id=1wPEyCUoI-tHMvpRFXqY1wwFUtN7bwt00");
        links.add("https://drive.google.com/uc?export=download&id=1e7SN6n5GVmuf0SbJSEPMCvJyRGlcyH7p");
        links.add("https://drive.google.com/uc?export=download&id=18HAChE8hAKh5lAsib0O9nHmiJphxTgvN");
        links.add("https://drive.google.com/uc?export=download&id=1aw0uCin5eA4QKSiof_kbpOkHTsPhVgkS");
        links.add("https://drive.google.com/uc?export=download&id=1_wyMilBow7x94kq6iycu_Pv4vh8mg__V");
        links.add("https://drive.google.com/uc?export=download&id=1wjGGRMbFFFzZyUsJVIs_WkyTQ7U8DAku");
        links.add("https://drive.google.com/uc?export=download&id=1zLjDg6Ga9qnTMIkfIybASY_MlCGmDaQ7");
        links.add("https://drive.google.com/uc?export=download&id=10i8MTb66w69JmQ1Was2FEJikpP21BsqQ");
        links.add("https://drive.google.com/uc?export=download&id=1IgfnLicyT-e04bW9eg1LQ7FU6779hHQO");
        links.add("https://drive.google.com/uc?export=download&id=1Cn_HjudgbeNeSb3hWxkM-2Qt2dqDIwn4");
        links.add("https://drive.google.com/uc?export=download&id=15H-D3FP-IncbbzwoIvoYpcCjRws9JQIz");
        links.add("https://drive.google.com/uc?export=download&id=1aNp6xOBut6W15GSu38oyYQ-l7Y2TjsmC");
        links.add("https://drive.google.com/uc?export=download&id=1VNA4tVZamIBbI-aNomF3ceBIkUtzHfCB");
        links.add("https://drive.google.com/uc?export=download&id=12re9Orly7EPDCRN6jENiZntHAqdKvMOJ");
        links.add("https://drive.google.com/uc?export=download&id=1untB8QcEU9opU1wbwAAA3-pfOGC-4cYn");
        links.add("https://drive.google.com/uc?export=download&id=1E-sPQJCY783jG3-omLPziw3Q2zW1PJeq");
        links.add("https://drive.google.com/uc?export=download&id=1vFd_rkC1oggi-rnGNpIy_DKC6-k1XK4i");
        links.add("https://drive.google.com/uc?export=download&id=1QWF49gtTmWbBGbbif_KXItVKCGFsdX0c");
        links.add("https://drive.google.com/uc?export=download&id=1S6DCxN5PNTeDyw_2qHK1YGEpG8Dyz1OK");
        links.add("https://drive.google.com/uc?export=download&id=17GY3GMXr5whuebQZ_1yK3O9RbkNjys3E");
        links.add("https://drive.google.com/uc?export=download&id=1H8-bYmuuZZt6UUhwxXgFQJiQUDUzNpor");
        links.add("https://drive.google.com/uc?export=download&id=1452CIxbC_My_KPK-04dlQa99IbGOejSU");
        links.add("https://drive.google.com/uc?export=download&id=1ys0ACrjl5nB5LjKHpD1QqOQc7QgJZE2x");
        links.add("https://drive.google.com/uc?export=download&id=1ynzSxOoPQbpqAMa5Z9_j7xcCNq2beQxy");
        links.add("https://drive.google.com/uc?export=download&id=1NzBf1Z5BHVojCze1YTleYW-BHcN5QmWO");
        links.add("https://drive.google.com/uc?export=download&id=1P4TKoCNq67TTEAzhrJqBVFCXd9y83Qc3");
        links.add("https://drive.google.com/uc?export=download&id=1bI0Q5cQp-hwJN8OV1fNs-xA47HuUzSI9");
        links.add("https://drive.google.com/uc?export=download&id=1XS9ADy_2GWoooByFJYRmYK7W3-rWAbDl");
        links.add("https://drive.google.com/uc?export=download&id=1ebphFNhpBsNnKgNoRYfpSrxfFZfJ9sln");
        links.add("https://drive.google.com/uc?export=download&id=1WCxB9gPWV7wkG65I09i4rptS2lNDQVkp");
        links.add("https://drive.google.com/uc?export=download&id=1hIhz3VPjEFxUPVYDJlf6W4RkorHHDW4f");
        links.add("https://drive.google.com/uc?export=download&id=1P2JkjSH_7Ngu7jxenDJxXTh8FDdqnx7N");
        links.add("https://drive.google.com/uc?export=download&id=15hySI5lhvfkdFcPjOmGRr2mhaTimgqs1");
        links.add("https://drive.google.com/uc?export=download&id=12x1wMw0hrXo-Db3uImdNXmCXdf_isJzM");
        links.add("https://drive.google.com/uc?export=download&id=1Udv16_-OzSx56wnIj5tuAn_BSGp8GnAF");
        links.add("https://drive.google.com/uc?export=download&id=1e29GF8jkbff9vc_P3onVeKv7aoz2Fyj9");
        links.add("https://drive.google.com/uc?export=download&id=180s8-T5R6aY76CRazBv6DHYU0rquvw72");
        links.add("https://drive.google.com/uc?export=download&id=1hwLaE-vN78JZpshVzGnfMbbqLagNsixI");
        links.add("https://drive.google.com/uc?export=download&id=1Q60tINX9wcmAeuzKgSb_lwBV9HkrqwLQ");
        links.add("https://drive.google.com/uc?export=download&id=15QwPSgZ3n3X6n7sfOMI8mOUpNdShd3FV");
        links.add("https://drive.google.com/uc?export=download&id=1qbLPLRBdF-lso-7PnhFtOmDwsVSirEzz");
        */
    }
}
