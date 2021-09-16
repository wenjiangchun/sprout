package com.sprout.web.util;

import com.sprout.core.spring.SpringContextUtils;

import java.io.Serializable;
import java.util.Locale;

/**
 * Web信息类，使用该类向页面输出系统信息
 * 
 * @author sofar
 *
 */
public class WebMessage implements Serializable {

    public static final String ACTION_SUCCESS_MESSAGE = "SUCCESS";

    public static final String ACTION_ERROR_MESSAGE = "ERROR";

	private static final long serialVersionUID = 1L;
	
	/**
	 * 信息内容
	 */
	private String content;
	
	/**
	 * 信息类型
	 */
	private String messageType;


    public WebMessage() {
		
	}
	public WebMessage(String content, String messageType) {
		this.content = content;
		this.messageType = messageType;
	}


    public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}


	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	/**
     * 创建操作成功信息提示
     * @return WebMessage
     */
    public static WebMessage createSuccessWebMessage() {
        return new WebMessage(ACTION_SUCCESS_MESSAGE, ACTION_SUCCESS_MESSAGE);
    }


    /**
     * 创建操作失败信息提示
     * @param errorMessage 错误信息内容
     * @return WebMessage
     */
    public static WebMessage createErrorWebMessage(String errorMessage) {
        return new WebMessage(ACTION_ERROR_MESSAGE + errorMessage, ACTION_ERROR_MESSAGE);
    }

    /**
     * 创建国际化操作成功信息提示
     * @return WebMessage
     */
    public static WebMessage createLocaleSuccessWebMessage(Locale locale) {
        String message = SpringContextUtils.getMessage(ACTION_SUCCESS_MESSAGE, null, locale);
        return new WebMessage(message, ACTION_ERROR_MESSAGE);
    }

    /**
     * 创建国际化操作失败信息提示
     * @param errorMessage 错误信息内容
     * @return WebMessage
     */
    public static WebMessage createErrorWebMessage(String errorMessage, Locale locale) {
        String message = SpringContextUtils.getMessage(ACTION_ERROR_MESSAGE, new String[]{errorMessage}, locale);
        return new WebMessage(message, ACTION_ERROR_MESSAGE);
    }
}
