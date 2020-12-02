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
        ConvertManager.put(t, new MuseamObj("termokauter", "models/Scull.gltf", t));
        ConvertManager.put(t2, new MuseamObj("termokaute2r", "models/Scull.gltf", t2));
        ConvertManager.put(t3, new MuseamObj("termokauter3", "models/Scull.gltf", t3));
    }

    public static final String partOneEasterLink = "https://raw.githubusercontent.com/jolyDev/muse_data_storage/main/EasterEggBase.png";
    public static final String partOneOriginLink = "https://github.com/jolyDev/muse_data_storage/blob/main/android.glb?raw=true";

    public static final String partTwoEasterLink = "https://github.com/jolyDev/muse_data_storage/blob/main/TestEaster.jpg?raw=true";
    public static final String partTwoOriginlink = "https://github.com/jolyDev/muse_data_storage/blob/main/Scull.gltf?raw=true";

    public static final String t  = "https://drive.google.com/uc?export=download&id=1WpjKZEnIzuFj6axqwCbs-ROPmbs8oinA";
    public static final String t2 = "https://drive.google.com/uc?export=download&id=122j1mwihUwsUZxFbSq9XJnAQWcesg1kb";
    public static final String t3 = "https://drive.google.com/uc?export=download&id=1coqt-1NTWCkP3tX5sm5XjLdNBvHjNTVg";
}
