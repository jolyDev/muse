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
    public Map<String, String> menuMap = new HashMap<String, String>();

    public static LinkConventer GetInstance()
    {
        if (instance == null)
            instance = new LinkConventer();

        return instance;
    }

    private LinkConventer()
    {
        putInMaps(new MuseamObj(Tokens.microscope, "models/android.obj", microscope));
        putInMaps(new MuseamObj(Tokens.scull, "models/Scull.gltf", scull));
        putInMaps(new MuseamObj(Tokens.map, "models/Scull.gltf", map));
        putInMaps(new MuseamObj(Tokens.termokauter, "models/Scull.gltf", termokauter));
        putInMaps(new MuseamObj(Tokens.heart, "models/Scull.gltf", heart));
    }

    private void putInMaps(MuseamObj obj){
        ConvertManager.put(obj.ar_link, obj);
        menuMap.put(obj.name, obj.ar_link);
    }

    public static final String partOneEasterLink = "https://raw.githubusercontent.com/jolyDev/muse_data_storage/main/EasterEggBase.png";
    public static final String partOneOriginLink = "https://drive.google.com/uc?export=download&id=1xugFN30Dw4BjwMp-yZ20DAGT276HXjfb";

    public static final String partTwoEasterLink = "https://github.com/jolyDev/muse_data_storage/blob/main/TestEaster.jpg?raw=true";
    public static final String partTwoOriginlink = "https://storage.googleapis.com/museum-storage/Scull_scaled.gltf";

    public static final String t  = "https://drive.google.com/uc?export=download&id=1WpjKZEnIzuFj6axqwCbs-ROPmbs8oinA";
    public static final String t2 = "https://drive.google.com/uc?export=download&id=122j1mwihUwsUZxFbSq9XJnAQWcesg1kb";
    public static final String t3 = "https://drive.google.com/uc?export=download&id=1coqt-1NTWCkP3tX5sm5XjLdNBvHjNTVg";
    public static final String t4 = "https://drive.google.com/uc?export=download&id=1zeetW8TuBL4p6Qz6dXdQpKrMaiadTUbV";

    public static final String scull = "https://drive.google.com/uc?export=download&id=1J6CzQ2KuGI2iyC4PhxzLzOMa-x15ZuGm";
    public static final String map = "https://drive.google.com/uc?export=download&id=1qmlajALjeL9zo3wFrZqUwliNUIKHca9c";
    public static final String termokauter = "https://drive.google.com/uc?export=download&id=1AYC0yRa0qyMsIOBT0FvuP1RHU4nG630o";
    public static final String microscope = "https://drive.google.com/uc?export=download&id=1zeetW8TuBL4p6Qz6dXdQpKrMaiadTUbV";
    public static final String heart = "https://drive.google.com/uc?export=download&id=1xugFN30Dw4BjwMp-yZ20DAGT276HXjfb";

}
