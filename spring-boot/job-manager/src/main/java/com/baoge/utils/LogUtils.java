package com.baoge.utils;

import org.apache.commons.logging.LogFactory;


public class LogUtils {
    private static final org.apache.commons.logging.Log logger;
    private static final Object lock = new Object();

    static {
        synchronized (lock) {
            logger = LogFactory.getLog(LogUtils.class);
        }
    }

    public static void info(Object... msgs) {
        StringBuilder stringBuilder = new StringBuilder();
        Throwable e = null;
        for (Object msg : msgs) {
            if (msg != null) {
                if (msg instanceof Throwable) {
                    e = (Throwable) msg;
                } else {
                    stringBuilder.append(msg).append(" ");
                }
            }
        }
        logger.info(stringBuilder, e);
    }

    public static void error(Object... msgs) {
        StringBuilder stringBuilder = new StringBuilder();
        Throwable e = null;
        for (Object msg : msgs) {
            if (msg != null) {
                if (msg instanceof Throwable) {
                    e = (Throwable) msg;
                } else {
                    stringBuilder.append(msg).append(" ");
                }
            }
        }
        logger.error(stringBuilder, e);
    }

    public static void warn(Object... msgs) {
        StringBuilder stringBuilder = new StringBuilder();
        Throwable e = null;
        for (Object msg : msgs) {
            if (msg != null) {
                if (msg instanceof Throwable) {
                    e = (Throwable) msg;
                } else {
                    stringBuilder.append(msg).append(" ");
                }
            }
        }
        logger.warn(stringBuilder, e);
    }

    public static void debug(Object... msgs) {
        StringBuilder stringBuilder = new StringBuilder();
        Throwable e = null;
        for (Object msg : msgs) {
            if (msg != null) {
                if (msg instanceof Throwable) {
                    e = (Throwable) msg;
                } else {
                    stringBuilder.append(msg).append(" ");
                }
            }
        }
        logger.debug(stringBuilder, e);
    }
}