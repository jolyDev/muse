package org.andresoviedo.app.model3D.DevTools;

import org.andresoviedo.lang.Tokens;

import java.util.HashMap;
import java.util.Map;

public class LinkConventer {

    public static class MuseamObj
    {
    public final String ar_link;
    public final String local_link;
    public final String name;

    public MuseamObj(String name, String local_link, String ar_link)
    {
        this.ar_link = ar_link;
        this.local_link = local_link;
        this.name = name;
    }
    }

    private static LinkConventer instance;
    public Map<String, MuseamObj> ConvertManager = new HashMap<String, MuseamObj>();

    public static LinkConventer GetInstance()
    {
        if (instance == null)
            instance = new LinkConventer();

        return instance;
    }

    private LinkConventer()
    {
        ConvertManager.put(partOneEasterLink, new MuseamObj("Android", "models/android.obj", partOneOriginLink));
        ConvertManager.put(partTwoEasterLink, new MuseamObj(Tokens.scull, "models/Scull.gltf", partTwoOriginlink));
    }

    public static final String partOneEasterLink = "https://raw.githubusercontent.com/jolyDev/muse_data_storage/main/EasterEggBase.png";
    public static final String partOneOriginLink = "https://github.com/jolyDev/muse_data_storage/blob/main/android.glb?raw=true";

    public static final String partTwoEasterLink = "https://github.com/jolyDev/muse_data_storage/blob/main/TestEaster.jpg?raw=true";
    public static final String partTwoOriginlink = "https://github.com/jolyDev/muse_data_storage/blob/main/Scull.gltf?raw=true";
}
