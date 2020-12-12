package org.andresoviedo.app.model3D.DevTools;

import org.andresoviedo.lang.LanguageManager;
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
        ConvertManager.put(heart_easter, new MuseamObj(
                LanguageManager.GetInstance().Get(Tokens.heart),
                "models/heart.dae", heart_origin));
        ConvertManager.put(skull_easter, new MuseamObj(
                LanguageManager.GetInstance().Get(Tokens.scull)
                , "models/Scull.obj", skull_origin));
        ConvertManager.put(map_easter, new MuseamObj(
                LanguageManager.GetInstance().Get(Tokens.map)
                , "models/map.dae", map_origin));
        ConvertManager.put(microscope_easter, new MuseamObj(
                LanguageManager.GetInstance().Get(Tokens.microscope)
                , "models/microscope.obj", microscope_origin));

        ConvertManager.put(t, new MuseamObj("termokauter", "models/Scull.gltf", t));
        ConvertManager.put(t2, new MuseamObj("termokaute2r", "models/Scull.gltf", t2));
        ConvertManager.put(t3, new MuseamObj("termokauter3", "models/Scull.gltf", t3));
        ConvertManager.put(t4, new MuseamObj("termokauter4", "models/Scull.gltf", t4));
    }

    public static final String skull_easter = "https://raw.githubusercontent.com/jolyDev/muse_data_storage/main/EasterEggBase.png";
    public static final String skull_origin = "https://github.com/jolyDev/muse_data_storage/blob/main/android.glb?raw=true";

    public static final String heart_easter = "https://github.com/jolyDev/muse_data_storage/blob/main/TestEaster.jpg?raw=true";
    public static final String heart_origin = "https://github.com/jolyDev/muse_data_storage/blob/main/Scull.gltf?raw=true";

    public static final String map_easter = "https://github.com/jolyDev/muse_data_storage/blob/main/TestEaster2.jpg?raw=true";
    public static final String map_origin = "https://github.com/jolyDev/muse_data_storage/blob/main/Scull.gltf?raw=true";

    public static final String microscope_easter = "https://github.com/jolyDev/muse_data_storage/blob/main/TestEaster2.jpg?raw=true";
    public static final String microscope_origin = "https://github.com/jolyDev/muse_data_storage/blob/main/Scull.gltf?raw=true";

    public static final String t  = "https://drive.google.com/uc?export=download&id=1WpjKZEnIzuFj6axqwCbs-ROPmbs8oinA";
    public static final String t2 = "https://drive.google.com/uc?export=download&id=122j1mwihUwsUZxFbSq9XJnAQWcesg1kb";
    public static final String t3 = "https://drive.google.com/uc?export=download&id=1coqt-1NTWCkP3tX5sm5XjLdNBvHjNTVg";
    public static final String t4 = "https://drive.google.com/uc?export=download&id=1zeetW8TuBL4p6Qz6dXdQpKrMaiadTUbV";
}
