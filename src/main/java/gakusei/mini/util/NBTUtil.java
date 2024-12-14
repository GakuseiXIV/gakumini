package gakusei.mini.util;

import gakusei.mini.Gakumini;
import gakusei.mini.GakuminiTags;

import java.util.ArrayList;
import java.util.List;

public class NBTUtil {
    public static String addToStringlist(String baseString, String add)
    {
        return baseString + add + ";";
    }
    public static List<String> getStringList(String list)
    {
        List<String> f = new ArrayList<>();
        for (String s : list.split(";"))
        {
            if (!s.isEmpty()) f.add(s);
            Gakumini.LOGGER.info(s);
        }
        return f;
    }
}
