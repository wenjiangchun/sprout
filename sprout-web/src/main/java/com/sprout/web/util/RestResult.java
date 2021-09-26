package com.sprout.web.util;

public class RestResult {

    private Object result;
    private boolean flag = true;
    private String content;

    public RestResult(Object result, boolean flag, String content) {
        this.result = result;
        this.flag = flag;
        this.content = content;
    }

    public RestResult(boolean flag, String content) {
        this.flag = flag;
        this.content = content;
    }



    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static RestResult createSuccessResult() {
        return new RestResult(true, null);
    }

    public static RestResult createSuccessResult(String content) {
        return new RestResult(true, content);
    }

    public static RestResult createSuccessResult(Object result, String content) {
        return new RestResult(result, true, content);
    }

    public static RestResult createErrorResult(String content) {
        return new RestResult(false, content);
    }

    public static RestResult createErrorResult(Object result, String content) {
        return new RestResult(result, false, content);
    }
}
