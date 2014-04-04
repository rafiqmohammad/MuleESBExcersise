package com.wsl.mulenotifications;

import org.mule.module.client.remoting.notification.RemoteDispatcherNotification;
import org.mule.module.client.remoting.notification.RemoteDispatcherNotificationListener;

public class RemoteDispatcherNotificationBean implements RemoteDispatcherNotificationListener<RemoteDispatcherNotification>{
	
	public void onNotification(RemoteDispatcherNotification notification) {
		System.out.println("------------------------RemoteDispatcherNotification---------------");
		System.out.println("notification.getSource: "+notification.getSource());
		System.out.println("notification.getActionName: "+notification.getActionName());
		System.out.println("notification.getTimestamp: "+notification.getTimestamp());
		System.out.println("notification.getServerId: "+notification.getServerId());
		System.out.println("---------------------------------------");
		
	}

}
