package com.edisonwang.adktest.utils;

import android.util.Log;

import com.lslk.sleepbot.utils.sl4j.FormattingTuple;
import com.lslk.sleepbot.utils.sl4j.MessageFormatter;

public class SLog {

  private static final String TAG = "sleepbot.";

  private static final boolean DEBUG = true;

  public static void v(String msg) {
    if (SLog.DEBUG) {
      Log.v(TAG + getCallingClass(), getCallingLine() + msg);
    }
  }

  public static void v(String format, Object arg) {
    if (SLog.DEBUG) {
      FormattingTuple ft = MessageFormatter.format(format, arg);
      Log.v(TAG + getCallingClass(), getCallingLine() + ft.getMessage());
    }
  }

  public static void v(String format, Object arg1, Object arg2) {
    if (SLog.DEBUG) {
      FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
      Log.v(TAG + getCallingClass(), getCallingLine() + ft.getMessage());
    }
  }

  public static void v(String format, Object[] args) {
    if (SLog.DEBUG) {
      FormattingTuple ft = MessageFormatter.format(format, args);
      Log.v(TAG + getCallingClass(), getCallingLine() + ft.getMessage());
    }
  }

  public static void v(String msg, Throwable throwable) {
    if (SLog.DEBUG) {
      Log.v(TAG + getCallingClass(), getCallingLine() + msg, throwable);
    }
  }

  public static void d(String msg) {
    if (SLog.DEBUG) {
      Log.d(TAG + getCallingClass(), getCallingLine() + msg);
    }
  }

  public static void d(String format, Object arg) {
    if (SLog.DEBUG) {
      FormattingTuple ft = MessageFormatter.format(format, arg);
      Log.d(TAG + getCallingClass(), getCallingLine() + ft.getMessage());
    }
  }

  public static void d(String format, Object arg1, Object arg2) {
    if (SLog.DEBUG) {
      FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
      Log.d(TAG + getCallingClass(), getCallingLine() + ft.getMessage());
    }
  }

  public static void d(String format, Object[] args) {
    if (SLog.DEBUG) {
      FormattingTuple ft = MessageFormatter.format(format, args);
      Log.d(TAG + getCallingClass(), getCallingLine() + ft.getMessage());
    }
  }

  public static void d(String msg, Throwable throwable) {
    if (SLog.DEBUG) {
      Log.d(TAG + getCallingClass(), getCallingLine() + msg, throwable);
    }
  }

  public static void i(String msg) {
    if (SLog.DEBUG) {
      Log.i(TAG + getCallingClass(), getCallingLine() + msg);
    }
  }

  public static void i(String format, Object arg) {
    if (SLog.DEBUG) {
      FormattingTuple ft = MessageFormatter.format(format, arg);
      Log.i(TAG + getCallingClass(), getCallingLine() + ft.getMessage());
    }
  }

  public static void i(String format, Object arg1, Object arg2) {
    if (SLog.DEBUG) {
      FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
      Log.i(TAG + getCallingClass(), getCallingLine() + ft.getMessage());
    }
  }

  public static void i(String format, Object[] args) {
    if (SLog.DEBUG) {
      FormattingTuple ft = MessageFormatter.format(format, args);
      Log.i(TAG + getCallingClass(), getCallingLine() + ft.getMessage());
    }
  }

  public static void i(String msg, Throwable throwable) {
    if (SLog.DEBUG) {
      Log.i(TAG + getCallingClass(), getCallingLine() + msg, throwable);
    }
  }

  public static void w(String msg) {
    if (SLog.DEBUG) {
      Log.w(TAG + getCallingClass(), getCallingLine() + msg);
    }
  }

  public static void w(String format, Object arg) {
    if (SLog.DEBUG) {
      FormattingTuple ft = MessageFormatter.format(format, arg);
      Log.w(TAG + getCallingClass(), getCallingLine() + ft.getMessage());
    }
  }

  public static void w(String format, Object[] args) {
    if (SLog.DEBUG) {
      FormattingTuple ft = MessageFormatter.format(format, args);
      Log.w(TAG + getCallingClass(), getCallingLine() + ft.getMessage());
    }
  }

  public static void w(String format, Object arg1, Object arg2) {
    if (SLog.DEBUG) {
      FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
      Log.w(TAG + getCallingClass(), getCallingLine() + ft.getMessage());
    }
  }

  public static void w(String msg, Throwable throwable) {
    if (SLog.DEBUG) {
      Log.w(TAG + getCallingClass(), getCallingLine() + msg, throwable);
    }
  }

  public static void e(String msg) {
    if (SLog.DEBUG) {
      Log.e(TAG + getCallingClass(), getCallingLine() + msg);
    }
  }

  public static void e(String format, Object arg) {
    if (SLog.DEBUG) {
      FormattingTuple ft = MessageFormatter.format(format, arg);
      Log.e(TAG + getCallingClass(), getCallingLine() + ft.getMessage());
    }
  }

  public static void e(String format, Object arg1, Object arg2) {
    if (SLog.DEBUG) {
      FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
      Log.e(TAG + getCallingClass(), getCallingLine() + ft.getMessage());
    }
  }

  public static void e(String format, Object arg, Throwable throwable) {
    if (SLog.DEBUG) {
      FormattingTuple ft = MessageFormatter.format(format, arg);
      Log.e(TAG + getCallingClass(), getCallingLine() + ft.getMessage(), throwable);
    }
  }

  public static void e(String format, Object[] args) {
    if (SLog.DEBUG) {
      FormattingTuple ft = MessageFormatter.format(format, args);
      Log.e(TAG + getCallingClass(), ft.getMessage());
    }
  }

  public static void e(String msg, Throwable throwable) {
    if (SLog.DEBUG) {
      Log.e(TAG + getCallingClass(), getCallingLine() + msg, throwable);
    }
  }

  private static String getCallingClass() {
    String className = Thread.currentThread().getStackTrace()[4].getClassName();
    int lastPeriod = className.lastIndexOf('.');
    if (lastPeriod >= className.length() - 1) {
      className = "";
    } else if (lastPeriod >= 0) {
      className = className.substring(lastPeriod + 1);
    }
    return className;
  }

  private static String getCallingLine() {
    return Thread.currentThread().getStackTrace()[4].getMethodName() + "()" + ":"
        + Thread.currentThread().getStackTrace()[4].getLineNumber() + " - ";
  }

}