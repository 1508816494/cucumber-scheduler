package com.jd.cucumber.scheduler.core.util;

import com.jd.cucumber.scheduler.core.exception.CucumberException;

public abstract class ExceptionHelper {

    private static final int MAX_STACK_TRACE_DEEP = 20;

    private static final String DEFAULT_END_STRING = "...";

    private static final int MAX_LENGTH = 5000 - DEFAULT_END_STRING.length();

    /**
     * 获取throwable对象的堆栈信息
     *
     * @param throwable 异常
     * @param isHtmlStyle 是否需要html格式
     * @return 堆栈信息
     */
    public static String getStackTrace(Throwable throwable, boolean isHtmlStyle) {
        AssertHelper.notNull(throwable, "throwable can't be null.");
        String line = isHtmlStyle ? "<br/>" : "\r\n";
        String tab = isHtmlStyle ? "&nbsp;&nbsp;&nbsp;&nbsp;" : "\t";
        while (throwable instanceof CucumberException) {
            throwable = throwable.getCause();
        }
        StringBuffer stringBuffer = new StringBuffer(throwable.getClass().getName()).append(":");
        if (throwable.getMessage() != null) {
            stringBuffer.append(throwable.getMessage()).append(line);
        } else {
            stringBuffer.append(line);
        }
        try {
            StackTraceElement[] stackElements = throwable.getStackTrace();
            if (stackElements != null) {
                for (int i = 0; i < stackElements.length && i < MAX_STACK_TRACE_DEEP; i++) {
                    stringBuffer.append(tab);
                    stringBuffer.append(stackElements[i].getClassName()).append('.');
                    stringBuffer.append(stackElements[i].getMethodName()).append('(');
                    stringBuffer.append(stackElements[i].getFileName()).append(':');
                    stringBuffer.append(stackElements[i].getLineNumber()).append(')');
                    stringBuffer.append(line);
                }
            }
        } catch (Exception e) {
            //ignored
        }
        if (isHtmlStyle) {
            return stringBuffer.length() > MAX_LENGTH ? (stringBuffer.substring(0, stringBuffer.lastIndexOf(line)) + DEFAULT_END_STRING) : stringBuffer.toString();
        } else {
            return stringBuffer.length() > MAX_LENGTH ? (stringBuffer.substring(0, MAX_LENGTH) + DEFAULT_END_STRING) : stringBuffer.toString();
        }
    }

    /**
     * @see ExceptionHelper#getStackTrace(Throwable, boolean)
     */
    public static String getStackTrace(Throwable throwable) {
        return getStackTrace(throwable, false);
    }

    /**
     * @see ExceptionHelper#getStackTrace(Throwable)
     */
    public static String getStackTrace() {
        return getStackTrace(new Throwable());
    }

}
