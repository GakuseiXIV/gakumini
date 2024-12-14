package gakusei.mini.util;

public class ColorUtil {

    public static int IntFromColor(int red, int green, int blue) {
        // surely this must be a minecraft function somewhere, right?
        return red << 16 | green << 8 | blue;
    }

    public static int IntFromHex(String hex)
    {
        return Integer.parseInt(hex, 16);
    }
}
