package org.andresoviedo.app.model3D.DevTools;

import android.app.Activity;
import android.os.Handler;

import com.google.ar.core.ArCoreApk;

import org.andresoviedo.lang.LanguageManager;
import org.andresoviedo.lang.Tokens;

import static com.google.ar.core.ArCoreApk.InstallStatus.INSTALLED;

public class MenuItemsHolder {
    public static String[] GetLocalObjectsMenuItems()
    {
        return new String[] {
                lang.Get(Tokens.map),
                lang.Get(Tokens.scull),
                lang.Get(Tokens.termokauter),
                lang.Get(Tokens.microscope),
                lang.Get(Tokens.back)
        };
    }

    public static String[] GetMainMenuItems(boolean isAR_mode)
    {
        if (isAR_mode)
            return new String[] {
                    lang.Get(Tokens.scanQR_AR),
                    lang.Get(Tokens.AR),
                    lang.Get(Tokens.atlas),
                    lang.Get(Tokens.language),
                    lang.Get(Tokens.about),
                    lang.Get(Tokens.exit)
            };
        else
           return new String[]{
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
