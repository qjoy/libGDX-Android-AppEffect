package alex.com.gdxdemo.testcode;

/**
 * @author AleXQ
 * @Date 15/12/5
 */

public class utils {
	public static String getLineInfo()
	{
		StackTraceElement ste = new Throwable().getStackTrace()[1];
		return ste.getFileName() + ": Line " + ste.getLineNumber();
	}
}

