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

        ConvertManager.put(heart, new MuseamObj(
                LanguageManager.GetInstance().Get(Tokens.heart),
                "models/heart.obj", heart));
        ConvertManager.put(scull, new MuseamObj(
                LanguageManager.GetInstance().Get(Tokens.scull)
                , "models/Scull.dae", scull));
        ConvertManager.put(map, new MuseamObj(
                LanguageManager.GetInstance().Get(Tokens.map)
                , "models/globe.dae", map));
        ConvertManager.put(microscope, new MuseamObj(
        LanguageManager.GetInstance().Get(Tokens.microscope)
                , "models/microscope.obj", microscope));
        ConvertManager.put(termokauter, new MuseamObj(
                LanguageManager.GetInstance().Get(Tokens.termokauter)
                , "models/ToyPlane.obj", termokauter));
    }

    private void putInMaps(MuseamObj obj){
        ConvertManager.put(obj.ar_link, obj);
        menuMap.put(obj.name, obj.ar_link);
    }

    public static MuseamObj GetMuseamObjFromId(String item)
    {
        LanguageManager lang = LanguageManager.GetInstance();

        MuseamObj obj = new MuseamObj("none", "none", "none");
        Map<String, MuseamObj> map = GetInstance().ConvertManager;

        if (item.equals(lang.Get(Tokens.scull)))
            obj = map.get(LinkConventer.scull);
        else if (item.equals(lang.Get(Tokens.heart)))
            obj = map.get(LinkConventer.heart);
        else if (item.equals(lang.Get(Tokens.map)))
            obj = map.get(LinkConventer.map);
        else if (item.equals(lang.Get(Tokens.microscope)))
            obj = map.get(LinkConventer.microscope);
        else if (item.equals(lang.Get(Tokens.termokauter)))
            obj = map.get(LinkConventer.termokauter);
        else if (item.equals(lang.Get(Tokens.back)))
            obj = null;
        return obj;
    }


    public static final String scull = "https://drive.google.com/uc?export=download&id=1J6CzQ2KuGI2iyC4PhxzLzOMa-x15ZuGm";
    public static final String map = "https://drive.google.com/uc?export=download&id=1qmlajALjeL9zo3wFrZqUwliNUIKHca9c";
    public static final String termokauter = "https://drive.google.com/uc?export=download&id=1AYC0yRa0qyMsIOBT0FvuP1RHU4nG630o";
    public static final String microscope = "https://drive.google.com/uc?export=download&id=1zeetW8TuBL4p6Qz6dXdQpKrMaiadTUbV";
    public static final String heart = "https://drive.google.com/uc?export=download&id=1xugFN30Dw4BjwMp-yZ20DAGT276HXjfb";

}
