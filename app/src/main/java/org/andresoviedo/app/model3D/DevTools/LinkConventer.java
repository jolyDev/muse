package org.andresoviedo.app.model3D.DevTools;

import org.andresoviedo.lang.LanguageManager;
import org.andresoviedo.lang.Tokens;

import java.util.HashMap;
import java.util.Map;

public class LinkConventer {

    private static LinkConventer instance;

    public static LinkConventer GetInstance()
    {
        if (instance == null)
            instance = new LinkConventer();

        return instance;
    }

    public static String GetObjLinkFromId(String item)
    {
        LanguageManager lang = LanguageManager.GetInstance();

        if (item.equals(lang.Get(Tokens.microscope)))
            return "models/micro.dae";
        else if (item.equals(lang.Get(Tokens.scull)))
            return "models/Scull.dae";
        else if (item.equals(lang.Get(Tokens.map)))
            return "models/globe.dae";
        else if (item.equals(lang.Get(Tokens.termokauter)))
            return "models/termocauter.dae";

        return null;
    }
}
