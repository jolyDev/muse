package org.andresoviedo.lang;

import java.util.*;

public class LanguageManager {
    public static final int ENG = 0;
    public static final int UA = 1;
    public static final int RUS = 2;
    public final int languages_count = 3;
    public static int code = ENG;

    public static String GetPrefix()
    {
       if (code == ENG)
           return "en_";
       if (code == UA)
           return "ua_";
       if (code == RUS)
           return "rus_";

       return "";
    }

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
        data.put(Tokens.AR, InitUIText(Tokens.AR, "Доповнена реальність", "Дополненная реальность"));
        data.put(Tokens.scanQR_AR, InitUIText(Tokens.scanQR_AR, "Сканувати QR-код", "Сканировать QR-код"));
        data.put(Tokens.menu, InitUIText(Tokens.menu, "Меню", "Меню"));
        data.put(Tokens.language, InitUIText(Tokens.language, "Мова", "Язык"));
        data.put(Tokens.settings, InitUIText(Tokens.settings, "Мова", "Язык"));
        data.put(Tokens.loading, InitUIText(Tokens.loading, "Завантаження", "Загрузка"));
        data.put(Tokens.english, InitUIText(Tokens.english, Tokens.en_flag + "Англійська", Tokens.en_flag + "Английский"));
        data.put(Tokens.ukrainian, InitUIText(Tokens.ukrainian, Tokens.ua_flag + "Українська", Tokens.ua_flag + "Украинский"));
        data.put(Tokens.russian, InitUIText(Tokens.russian, Tokens.ru_flag + "Російська", Tokens.ru_flag + "Русский"));
        data.put(Tokens.help, InitUIText(Tokens.help, "Допомога", "Помощь"));
        data.put(Tokens.about, InitUIText(Tokens.about, "Про нас", "О нас"));
        data.put(Tokens.load, InitUIText(Tokens.load, "Завантажити", "Загрузить"));
        data.put(Tokens.exit, InitUIText(Tokens.exit, "Вихід", "Выход"));
        data.put(Tokens.noUrl, InitUIText(Tokens.noUrl, "Ніякого url не було передано до відображувача картинок", "Никакого url не было передано в отображатель картинок"));
        data.put(Tokens.imageNotFound, InitUIText(Tokens.imageNotFound, "Проблеми із завантаженням. Перевірте правильність url", "Проблемы с загрузкой. Проверьте правильность url"));
        data.put(Tokens.scanQR, InitUIText(Tokens.scanQR, "Сканувати QR-код", "Сканировать QR-код"));
        data.put(Tokens.viewItems, InitUIText(Tokens.viewItems, "Експонати", "Экспонаты"));
        data.put(Tokens.theme, InitUIText(Tokens.theme, "Стиль", "Стиль"));
        data.put(Tokens.deviceDefault, InitUIText(Tokens.deviceDefault, "Стиль системи", "Стиль системы"));
        data.put(Tokens.light, InitUIText(Tokens.light, "Світлий", "Светлый"));
        data.put(Tokens.dark, InitUIText(Tokens.dark, "Темний", "Темный"));
        data.put(Tokens.holo, InitUIText(Tokens.holo, "Голограма", "Голограмма"));
        data.put(Tokens.info, InitUIText(Tokens.info, "Інфо", "Инфо"));
        data.put(Tokens.back, InitUIText(Tokens.back, "Назад", "Назад"));
        data.put(Tokens.items, InitUIText(Tokens.items, "Моделі", "Модели"));
        data.put(Tokens.incorrectQR, InitUIText(Tokens.incorrectQR, "Не коректний QR-код", "Не корректный QR-код"));
        data.put(Tokens.museName, InitUIText(Tokens.museName, "НАЦІОНАЛЬНИЙ МУЗЕЙ МЕДИЦИНИ УКРАЇНИ", "НАЦИОНАЛЬНЫЙ МУЗЕЙ МЕДИЦИНЫ УКРАИНЫ"));

        // items
        data.put(Tokens.atlas, InitUIText(Tokens.atlas, "Атлас", "Атлас"));
        data.put(Tokens.scull, InitUIText(Tokens.scull, "Череп з трепанаційним отвором", "Череп з трепанаційним отвором"));
        data.put(Tokens.map, InitUIText(Tokens.map, "Маршрут експедиції д. Заболотного", "Маршрут экспедиции врача Заболотного"));
        data.put(Tokens.heart, InitUIText(Tokens.heart, "Серце із штучним клапаном", "Сердце с искусственным клапаном"));
        data.put(Tokens.termokauter, InitUIText(Tokens.termokauter, "Термокаутер", "Термокаутер"));
        data.put(Tokens.microscope, InitUIText(Tokens.microscope, "Мікроскоп", "Микроскоп"));

        // description
        data.put(Tokens.aboutDescription, InitUIText(Tokens.aboutDescription,
                new StringBuilder()
                        .append("    Національний музей медицини України є одним з найбільших медичних музеїв не тільки в Україні, але і у Європі.\n")
                        .append("    В музеї представлено розвиток медицини в Україні з стародавніх часів до наших днів. В основу організації музею була покладена новітня концепція музеєзнавства, яка дозволила використати науково-методичні і документальні матеріали та експонати у комплексі з архітектурними, художньо-технічними і аудіовізуальними засобами. Крім стендової експозиції, в музеї створені оригінальні інтер'єри, з портретними фігурами відомих вчених і лікарів, та діорами, присвячені найбільш визначним подіям в українській медицині. В експозиції музею широко представлені і твори українського образотворчого мистецтва, що пов'язане з медичною тематикою.\n")
                        .append("    Робота по створенню цього музею була у 1983 р. відзначена Державною премією України у галузі науки і техніки. Указом Президента України від 15 лютого 1999 р. наданий статус Національного.\n")
                        .append("    Додаток було сворено студентами Національного технічного університету України «Київський політехнічний інститу́т імені Ігоря Сікорського»")
                        .toString(),
                new StringBuilder()
                        .append("    Национальный музей медицины Украины является одним из крупнейших медицинских музеев не только в Украине, но и в Европе.\n")
                        .append("    В музее представлено развитие медицины в Украине с древнейших времен до наших дней. В основу организации музея была положена новейшая концепция музееведения, которая позволила использовать научно-методические и документальные материалы и экспонаты в комплексе с архитектурными, художественно-техническими и аудиовизуальными средствами. Кроме стендовой экспозиции, в музее созданы оригинальные интерьеры, с портретными фигурами известных ученых и врачей, и диорамы, посвященные наиболее выдающимся событиям в украинской медицине. В экспозиции музея широко представлены и произведения украинского изобразительного искусства, связано с медицинской тематикой.\n")
                        .append("    Работа по созданию этого музея была в 1983 отмечена Государственной премией Украины в области науки и техники. Указом Президента Украины от 15 февраля 1999 присвоен статус Национального.\n")
                        .append("    Приложения было создано студентами Национального технического университета Украины «Киевский политехнический институт имени Игоря Сикорского»")
                        .toString()));
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