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
        data.put(Tokens.AR, InitUIText(Tokens.AR, "☆ AR ☆", "☆ AR ☆"));
        data.put(Tokens.scanQR_AR, InitUIText(Tokens.scanQR_AR, "☆ Сканувати QR ☆", "☆ Сканировать QR ☆"));
        data.put(Tokens.menu, InitUIText(Tokens.menu, "Меню", "Меню"));
        data.put(Tokens.language, InitUIText(Tokens.language, "Мова", "Язык"));
        data.put(Tokens.settings, InitUIText(Tokens.settings, "Мова", "Язык"));
        data.put(Tokens.loading, InitUIText(Tokens.loading, "Завантаження", "Загрузка"));
        data.put(Tokens.english, InitUIText(Tokens.english, Tokens.en_flag + "Англійська", Tokens.en_flag + "Английский"));
        data.put(Tokens.ukrainian, InitUIText(Tokens.ukrainian, Tokens.ua_flag + "Українська", Tokens.ua_flag + "Украинский"));
        data.put(Tokens.russian, InitUIText(Tokens.russian, Tokens.ru_flag + "Російська", Tokens.ru_flag + "Русский"));
        data.put(Tokens.help, InitUIText(Tokens.help, "Допомога", "Помощь"));
        data.put(Tokens.about, InitUIText(Tokens.about, "Інфо", "Інфо"));
        data.put(Tokens.load, InitUIText(Tokens.load, "Завантажити", "Загрузить"));
        data.put(Tokens.exit, InitUIText(Tokens.exit, "Вихід", "Выход"));
        data.put(Tokens.scanQR, InitUIText(Tokens.scanQR, "Сканувати QR", "Сканировать QR"));
        data.put(Tokens.viewItems, InitUIText(Tokens.viewItems, "Експонати", "Экспонаты"));
        data.put(Tokens.theme, InitUIText(Tokens.theme, "Стиль", "Стиль"));
        data.put(Tokens.deviceDefault, InitUIText(Tokens.deviceDefault, "Стиль системи", "Стиль системы"));
        data.put(Tokens.light, InitUIText(Tokens.light, "Світлий", "Светлый"));
        data.put(Tokens.dark, InitUIText(Tokens.dark, "Темний", "Темный"));
        data.put(Tokens.holo, InitUIText(Tokens.holo, "Голограма", "Голограмма"));
        data.put(Tokens.info, InitUIText(Tokens.info, "Інфо", "Инфо"));
        data.put(Tokens.back, InitUIText(Tokens.back, "Назад", "Назад"));
        data.put(Tokens.plane, InitUIText(Tokens.plane, "Літак", "Самолет"));
        data.put(Tokens.incorrectQR, InitUIText(Tokens.incorrectQR, "Не корректний QR-код", "Не корректный QR-код"));
        data.put(Tokens.atlas, InitUIText(Tokens.atlas, "Атлас", "Атлас"));
        data.put(Tokens.teapot, InitUIText(Tokens.teapot, "Чайник", "Чайник"));
        data.put(Tokens.scull, InitUIText(Tokens.scull, "Череп", "Череп"));
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