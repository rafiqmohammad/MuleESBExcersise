package com.wsl.mulenotifications;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.mule.api.context.notification.ExceptionNotificationListener;
import org.mule.context.notification.ExceptionNotification;

/**
 * @author Mohammad Rafiq
 * 
 */

public class CustomExceptionNotification implements
		ExceptionNotificationListener<ExceptionNotification> {

	
	List<Object> notificationArrayL = new ArrayList<Object>();
	private static Logger log = Logger.getLogger(CustomExceptionNotification.class.getName());

	public void onNotification(final ExceptionNotification notification) {
		log.info("--------ExceptionNotification------------------");
		log.info("getActionName " + notification.getActionName());
		log.info("getType " + notification.getType());
		log.info("getClass " + notification.getClass());
		log.info("getSource " + notification.getSource().toString());
		log.info("Exception " + notification.getException().getCause().toString());
		log.debug("Exception  in debug " + notification.getException().getMessage().toString());
		log.info("EVENT_NAME " + notification.EVENT_NAME);
		log.info("--------------------------");
	}
	
}
