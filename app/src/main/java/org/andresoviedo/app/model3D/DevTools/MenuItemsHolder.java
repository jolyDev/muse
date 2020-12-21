package org.andresoviedo.app.model3D.DevTools;

import android.app.Activity;
import android.os.Handler;

import com.google.ar.core.ArCoreApk;

import org.andresoviedo.app.model3D.view.MenuActivity;
import org.andresoviedo.lang.LanguageManager;
import org.andresoviedo.lang.Tokens;

import static com.google.ar.core.ArCoreApk.InstallStatus.INSTALLED;

public class MenuItemsHolder {
    public static String[] GetLocalObjectsMenuItems()
    {
        int size = LinkConventer.GetInstance().menuMap.keySet().size();
        String[] items = new String[size+1];
        LinkConventer.GetInstance().menuMap.keySet().toArray(items);
        for(int i = 0;i < size;i++){
            items[i] = lang.Get(items[i]);
        }
        items[size] = lang.Get(Tokens.back);

        return items;
    }

    public static String[] GetMainMenuItems(boolean isAR_mode)
    {
        if (isAR_mode)
            return new String[] {
                    lang.Get(Tokens.debug_load),
                    lang.Get(Tokens.scanQR_AR),
                    lang.Get(Tokens.AR),
                    lang.Get(Tokens.atlas),
                    lang.Get(Tokens.language),
                    lang.Get(Tokens.about),
                    lang.Get(Tokens.exit)
            };
        else
           return new String[]{
                   lang.Get(Tokens.debug_load),
                    lang.Get(Tokens.scanQR),
                    lang.Get(Tokens.viewItems),
                    lang.Get(Tokens.atlas),
                    lang.Get(Tokens.language),
                    lang.Get(Tokens.about),
                    lang.Get(Tokens.exit)
            };
    }

    private static LanguageManager lang = LanguageManager.GetInstance();
}
