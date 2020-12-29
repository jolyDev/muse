package org.andresoviedo.lang;

public class Tokens
{
    static public final String en_flag = "\uD83C\uDDEC\uD83C\uDDE7 ";
    static public final String ua_flag = "\uD83C\uDDFA\uD83C\uDDE6 ";
    static public final String ru_flag = "\uD83C\uDDF7\uD83C\uDDFA ";

    static public final String AR = "View Items";
    static public final String scanQR_AR = "Scan QR";
    static public final String menu = "Menu";
    static public final String debug_load = "Load [Debug]";
    static public final String atlas = "Atlas";
    static public final String settings = "Settings";
    static public final String language = "Language";
    static public final String loading = "Loading...";
    static public final String english = en_flag + "English";
    static public final String ukrainian = ua_flag + "Ukrainian";
    static public final String russian = ru_flag + "Russian";
    static public final String help = "Help";
    static public final String about = "About";
    static public final String exit = "Exit";
    static public final String load = "Load";
    static public final String scanQR = "Scan QR";
    static public final String viewItems = "View Items";
    static public final String theme = "Theme";
    static public final String deviceDefault = "Device default";
    static public final String light = "Light";
    static public final String dark = "Dark";
    static public final String holo = "Holo";
    static public final String info = "Info";
    static public final String incorrectQR = "incorrect QR was captured";
    static public final String back = "Back";
    public static final String imageNotFound = "Image cannot be loaded. Please check if the url is correct";
    public static final String noUrl = "No url passed to image viewer";
    public static final String museName = "NATIONAL MUSEUM OF MEDICINE OF UKRAINE";

    // items
    static public final String items = "Items";
    static public final String scull = "Skull with trepanation hole";
    static public final String termokauter = "Сautery";
    static public final String heart = "Heart with an artificial valve";
    static public final String map = "The route of Dr. Zabolotny's expedition";
    static public final String microscope = "Microscope";

    static public final String aboutDescription = new StringBuilder()
            .append("    The National Museum of Medicine of Ukraine is one of the largest medical museums in Ukraine and Europe.\n")
            .append("The museum presents the development of medicine in Ukraine from ancient times till present day.")
            .append("The organization of the museum was based on the latest concept of museum studies, which allowed the use of scientific and methodological and documentary materials and exhibits in combination with architectural, artistic, technical and audiovisual means. In addition to the poster exposition, the museum has created original interiors with portrait figures of famous scientists and doctors, and dioramas dedicated to the most significant events in Ukrainian medicine. The museum's exposition also includes works of Ukrainian fine arts related to medical topics.\n")
            .append("    The work on the creation of this museum was in 1983 awarded the State Prize of Ukraine in the field of science and technology. The decree of the President of Ukraine of February 15, 1999 granted the status of National.\n")
            .append("    Application was developed by students from National Technical University of Ukraine «Igor Sikorsky Kyiv Polytechnic Institute».")
            .toString();
}
