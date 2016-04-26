package fr.tenebrae.MMOCore.Utils;


import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringParser {

    static Pattern pattern = Pattern.compile("\\{([^.}]+)\\}");

    public static String parse(Player p, String string) {
        Matcher m = pattern.matcher(string);
        while (m.find()) {
            String find = m.group(1);
            String[] var = find.split("_");

            String stringToReplace = "\\{"+find+"\\}";

            switch (var[0]) {
                case "PLAYER":
                    string = string.replaceAll(stringToReplace, p.getName());
                    break;
                default:
                    break;
            }

        }
        return string;
    }
}
