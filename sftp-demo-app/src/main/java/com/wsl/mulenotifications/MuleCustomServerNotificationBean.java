package com.wsl.mulenotifications;

import org.mule.api.context.notification.ServerNotification;
import org.mule.api.context.notification.ServerNotificationListener;

public class MuleCustomServerNotificationBean implements ServerNotificationListener<ServerNotification>{
	
	public void onNotification(ServerNotification notification) {
		System.out.println("------------------------ServerNotification---------------");
		System.out.println("notification.getSource: "+notification.getSource());
		System.out.println("notification.getActionName: "+notification.getActionName());
		System.out.println("notification.getTimestamp: "+notification.getTimestamp());
		System.out.println("notification.getServerId: "+notification.getServerId());
		System.out.println("---------------------------------------");
		
	}

}
