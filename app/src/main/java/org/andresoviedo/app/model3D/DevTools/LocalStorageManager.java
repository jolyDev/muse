package org.andresoviedo.app.model3D.DevTools;

import android.app.DownloadManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;

public class LocalStorageManager
{
    private static LocalStorageManager instance = new LocalStorageManager();
    public static String RootDir = Environment.DIRECTORY_DOWNLOADS;

    public static LocalStorageManager GetInstance()
    {
        if (instance == null)
            instance = new LocalStorageManager();

        return instance;
    }

    public String GetFileNameForPage(int index)
    {
        return "Atlas_page_" + index;
    }

    public String GetFilePathForPage(int index)
    {
        File path = new File(
                Environment.getExternalStoragePublicDirectory(RootDir),
                GetFileNameForPage(index));
        return path.toString();
    }

    public boolean IsPageStoredLocaly(int index)
    {
        File file = new File(GetFilePathForPage(index));
        return file.exists();
    }

    public Bitmap GetBitmapForPage(int index)
    {
        if(IsPageStoredLocaly(index))
            return BitmapFactory.decodeFile(GetFilePathForPage(index));

        return null;
    }
}
