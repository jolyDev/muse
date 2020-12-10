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
        ConvertManager.put(partOneEasterLink, new MuseamObj(Tokens.microscope, "models/android.obj", partOneOriginLink)); //todo
        ConvertManager.put(partTwoEasterLink, new MuseamObj(Tokens.scull, "models/Scull.gltf", partTwoOriginlink));
        ConvertManager.put(t, new MuseamObj("termokauter", "models/Scull.gltf", t));
        ConvertManager.put(t2, new MuseamObj("termokaute2r", "models/Scull.gltf", t2));
        ConvertManager.put(t3, new MuseamObj("termokauter3", "models/Scull.gltf", t3));
        ConvertManager.put(t4, new MuseamObj("termokauter4", "models/Scull.gltf", t4));
    }

    public static final String partOneEasterLink = "https://raw.githubusercontent.com/jolyDev/muse_data_storage/main/EasterEggBase.png";
    public static final String partOneOriginLink = "https://drive.google.com/uc?export=download&id=1NbM3ii1LUA_AD_Nd1bmY5LdICSDCNt9M";

    public static final String partTwoEasterLink = "https://github.com/jolyDev/muse_data_storage/blob/main/TestEaster.jpg?raw=true";
    public static final String partTwoOriginlink = "https://drive.google.com/uc?export=download&id=1JHE4nigfXBfhKu762ovBbWbvdQOerpiL";

    public static final String t  = "https://drive.google.com/uc?export=download&id=1WpjKZEnIzuFj6axqwCbs-ROPmbs8oinA";
    public static final String t2 = "https://drive.google.com/uc?export=download&id=122j1mwihUwsUZxFbSq9XJnAQWcesg1kb";
    public static final String t3 = "https://drive.google.com/uc?export=download&id=1coqt-1NTWCkP3tX5sm5XjLdNBvHjNTVg";
    public static final String t4 = "https://drive.google.com/uc?export=download&id=1zeetW8TuBL4p6Qz6dXdQpKrMaiadTUbV";
}
