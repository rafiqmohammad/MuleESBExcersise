package com.wsl.mulenotifications;

import org.mule.api.context.notification.RoutingNotificationListener;
import org.mule.api.context.notification.ServerNotification;
import org.mule.context.notification.RoutingNotification;

public class MuleCustomRoutingNotificationBean implements RoutingNotificationListener<RoutingNotification>{
	
	public void onNotification(RoutingNotification notification) {
		System.out.println("------------------------RoutingNotification---------------");
		System.out.println("notification.getSource: "+notification.getSource());
		System.out.println("notification.getActionName: "+notification.getActionName());
		System.out.println("notification.getTimestamp: "+notification.getTimestamp());
		System.out.println("notification.getServerId: "+notification.getServerId());
		
		System.out.println("---------------------------------------");
		
	}

}
