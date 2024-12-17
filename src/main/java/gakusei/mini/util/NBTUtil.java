package gakusei.mini.util;

import gakusei.mini.Gakumini;
import gakusei.mini.GakuminiTags;

import java.util.ArrayList;
import java.util.List;

public class NBTUtil {
    public static String addToStringlist(String baseString, String add, boolean removeDuplicates)
    {
        if (removeDuplicates && getStringList(baseString).contains(add)) {
            String str = "";
            List<String> f = getStringList(baseString);
            f.remove(add);
            f.add(add);
            for (String s : f) {
                str = str + s + ";";
            }
            return str;
        }
        return baseString + add + ";";
    }
    public static List<String> getStringList(String list)
    {
        List<String> f = new ArrayList<>();
        for (String s : list.split(";"))
        {
            if (!s.isEmpty()) f.add(s);
        }
        return f;
    }
}
