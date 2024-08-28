package taco.klkl.global.util;

public class StringUtil {
	public static String trimDoubleQuote(String string) {
		return string.replaceAll("^\"|\"$", "");
	}
}
