package org.sijbesma.bp.utils;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DebugLogger {
	private static Logger logger;
	private static boolean debugFlag;
	
	public DebugLogger(Logger logger, boolean debugFlag) {
		DebugLogger.logger = logger;
		DebugLogger.debugFlag = debugFlag;
	}
	
	public static void debug(String message) {
		if (debugFlag) {
			logger.log(Level.INFO, message);
		}
	}

	public static void debug(String message, boolean header) {
		debug(message, header, '-');
	}
	
	
	public static void debug(String message, boolean header, char character) {
		if (header) {
			int length = 20;
			int padLength = (length - message.length() / 2);
			char[] padChar = new char[padLength];
			Arrays.fill(padChar, character);
			String padding = new String(padChar);
			if(message.length() % 2 == 1) {
				debug(padding + message + padding);
			}else {
				debug(padding + message + padding + character);
			}
		} else {
			debug(message);
		}
	}

	public static void setDebugFlag(boolean debugFlag) {
		DebugLogger.debugFlag = debugFlag;
	}
	
	public static void printStackTrace() {
		new Exception().printStackTrace();
	}
	
	
}
