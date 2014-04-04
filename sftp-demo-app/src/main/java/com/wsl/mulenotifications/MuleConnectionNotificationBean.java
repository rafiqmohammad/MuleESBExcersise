package com.wsl.mulenotifications;

import org.mule.api.context.notification.ConnectionNotificationListener;
import org.mule.context.notification.ConnectionNotification;

public class MuleConnectionNotificationBean implements ConnectionNotificationListener<ConnectionNotification>{
	
	public void onNotification(ConnectionNotification notification) {
		System.out.println("------------------------ConnectionNotification---------------");
		System.out.println("notification.getSource: "+notification.getSource());
		System.out.println("notification.getActionName: "+notification.getActionName());
		System.out.println("notification.getTimestamp: "+notification.getTimestamp());
		System.out.println("notification.getServerId: "+notification.getServerId());
		System.out.println("---------------------------------------");
		
	}

}
