package com.wsl.mulenotifications;

import org.mule.api.context.notification.MuleContextNotificationListener;
import org.mule.api.context.notification.ServerNotification;
import org.mule.context.notification.MuleContextNotification;

public class MuleCustomContextNotificationBean implements MuleContextNotificationListener<MuleContextNotification>{
	
	public void onNotification(MuleContextNotification notification) {
		System.out.println("------------------------MuleContextNotification---------------");
		System.out.println("notification.getSource: "+notification.getSource());
		System.out.println("notification.getActionName: "+notification.getActionName());
		System.out.println("notification.getTimestamp: "+notification.getTimestamp());
		System.out.println("notification.getServerId: "+notification.getServerId());
		System.out.println("---------------------------------------");
		
	}

}
