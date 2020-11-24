package org.andresoviedo.lang;

import java.util.*;

public class LanguageManager {
    public final int ENG = 0;
    public final int UA = 1;
    public final int RUS = 2;
    public final int languages_count = 3;
    public int code = ENG;

    public String Get(String key)
    {
        UIText value = data.get(key);
        if (value == null)
            return "#" + key;

        return value.GetToken(code);
    }

    public String GetInfo(String objId)
    {
        return "$ Unimplemented";
    }

    static private LanguageManager instance = new LanguageManager();

    static public LanguageManager GetInstance()
    {
    if (instance == null)
        instance = new LanguageManager();

        return instance;
    }
    private LanguageManager()
    {
        data.put(Tokens.AR, InitUIText(Tokens.AR, "☆ Доповнена Реальність ☆", "☆ Дополненная Реальность ☆"));
        data.put(Tokens.menu, InitUIText(Tokens.menu, "Меню", "Меню"));
        data.put(Tokens.language, InitUIText(Tokens.language, "Мова", "Язык"));
        data.put(Tokens.settings, InitUIText(Tokens.settings, "Мова", "Язык"));
        data.put(Tokens.loading, InitUIText(Tokens.loading, "Завантаження", "Загрузка"));
        data.put(Tokens.english, InitUIText(Tokens.english, "Англійська", "Английский"));
        data.put(Tokens.ukrainian, InitUIText(Tokens.ukrainian, "\uD83C\uDDFA\uD83C\uDDE6 Українська", "Украинский"));
        data.put(Tokens.russian, InitUIText(Tokens.russian, "Російська", "\uD83C\uDDF7\uD83C\uDDFA Русский"));
        data.put(Tokens.help, InitUIText(Tokens.help, "Допомога", "Помощь"));
        data.put(Tokens.about, InitUIText(Tokens.about, "Інфо", "Інфо"));
        data.put(Tokens.load, InitUIText(Tokens.load, "Завантажити", "Загрузить"));
        data.put(Tokens.exit, InitUIText(Tokens.exit, "Вихід", "Выход"));
        data.put(Tokens.scanQR, InitUIText(Tokens.scanQR, "Сканувати QR", "Сканировать"));
        data.put(Tokens.viewItems, InitUIText(Tokens.viewItems, "Експонати", "Экспонаты"));
        data.put(Tokens.theme, InitUIText(Tokens.theme, "Стиль", "Стиль"));
        data.put(Tokens.deviceDefault, InitUIText(Tokens.deviceDefault, "Стиль системи", "Стиль системы"));
        data.put(Tokens.light, InitUIText(Tokens.light, "Світлий", "Светлый"));
        data.put(Tokens.dark, InitUIText(Tokens.dark, "Темний", "Темный"));
        data.put(Tokens.holo, InitUIText(Tokens.holo, "Голограма", "Голограмма"));
        data.put(Tokens.info, InitUIText(Tokens.info, "Інфо", "Инфо"));
    }

    private Map<String, UIText> data = new HashMap<String, UIText>();

    private class UIText {

        UIText(String[] i_tokens)
        {
            tokens = i_tokens;
        }

        public String[] tokens;

        String GetToken(int index)
        {
            if (index >= 0 && index < languages_count)
                return tokens[index];
            else
                return "#Wrong Lang Code";
        }
    }

    private UIText InitUIText(String eng, String ua, String rus)
    {
        return new UIText(new String[]{eng, ua, rus});
    }
};