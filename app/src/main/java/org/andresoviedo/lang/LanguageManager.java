package org.andresoviedo.lang;

import java.util.*;

public class LanguageManager {
    public enum ELangCode {
        ENG,
        UA,
        RUS
    }

    public ELangCode code = ELangCode.ENG;

    public String Get(String key)
    {
        UIText value = data.get(key);
        if (value == null)
            return "#" + key;

        return value.GetToken(code);
    }

    public LanguageManager()
    {
        data.put(Tokens.menu, InitUIText(Tokens.menu, "Меню", "Меню"));
        data.put(Tokens.language, InitUIText(Tokens.language, "Мова", "Язык"));
        data.put(Tokens.settings, InitUIText(Tokens.settings, "Мова", "Язык"));
        data.put(Tokens.loading, InitUIText(Tokens.loading, "Завантаження", "Загрузка"));
    }

    private Map<String, UIText> data = new HashMap<String, UIText>();

    private class UIText {

        UIText(String[] i_tokens)
        {
            tokens = i_tokens;
        }

        public String[] tokens;

        String GetToken(ELangCode code)
        {
            switch(code)
            {
                case ENG:
                    return tokens[0];
                case UA:
                    return tokens[1];
                case RUS:
                    return tokens[2];
                default:
                    return "#Wrong Lang Code";
            }

        }
    }

    private UIText InitUIText(String eng, String ua, String rus)
    {
        return new UIText(new String[]{eng, ua, rus});
    }
};