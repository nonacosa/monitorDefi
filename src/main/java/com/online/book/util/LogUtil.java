package com.online.book.util;

import com.alibaba.fastjson.JSON;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

/**
 * @author wenda.zhuang
 * @Date 2020/10/18 17:20
 * @Description ...
 * @E-mail sis.nonacosa@gmail.com
 */
public class LogUtil {

	public static final String ATTENTION_ERR_FLAG = "exception";
	/*本地测试标识*/
	public static final boolean isDetailDebug = true;
	public static String LOGGER_NAME ="codesofun";
	public static Logger logger = null;


	public static String fmtErr(Exception e) {
		String buf = "";

		if (e.getCause() != null) {
			buf += e.getCause()
					.getMessage() + "\n";
		}

		StackTraceElement b[] = e.getStackTrace();
		buf += e.toString() + "\n";
		for (int i = 0; i < b.length; i++) {
			buf += b[i].toString() + "\n";
		}
		return buf;
	}

	public static String fmtThrowable(Throwable e) {
		String buf = "";

		if (e.getCause() != null) {
			buf += e.getCause()
					.getMessage() + "\n";
		}

		StackTraceElement b[] = e.getStackTrace();
		buf += e.toString() + "\n";
		for (int i = 0; i < b.length; i++) {
			buf += b[i].toString() + "\n";
		}
		return buf;
	}

	public static void info(String str) {
		getLogger().info(str);
	}


	public static void warn(String str) {
		getLogger().warn(str);
	}

	public static void debug(String debug, Object... objs) {
		setMCD();
		getLogger().debug(debug, objs);

	}

	public static void debug(String debug) {
		setMCD();
		getLogger().debug(debug);
	}


	public static void error(String str, Object... objs) {
		setMCD();
		getLogger().error(str, objs);
	}

	public static void error(String str) {
		setMCD();
		getLogger().error(str);
	}

	public static void error(Exception e) {
		setMCD();
		getLogger().error(fmtErr(e));
	}


	public static void info(String str, Logger logger) {
		logger.info(str);
	}

	public static void warn(String str, Logger logger) {
		logger.warn(str);
	}

	public static void debug(String debug, Logger logger) {
		setMCD();
		logger.debug(debug);
	}

	public static void error(String str, Logger logger) {
		setMCD();
		logger.error(str);
	}

	public static void error(Exception e, Logger logger) {
		setMCD();
		logger.error(fmtErr(e));
	}

	public static void initLog(String logName) {
		if (StringUtils.isNotEmpty(logName)) {
			LOGGER_NAME = logName;
		}
		logger = LoggerFactory.getLogger(LOGGER_NAME);
	}

	public static Logger getLogger() {
		if (logger == null) {
			initLog(null);
		}
		return logger;
	}

	private static void setMCD() {
		Thread current = Thread.currentThread();
		String localThreadId = MessageFormat.format("{0}", String.valueOf(current.getId()));
	}

	public static String toJson(Object obj) {
		/*测试 jsonUtils 有报错情况*/
		return obj == null ? null : JSON.toJSONString(obj);
	}
}
