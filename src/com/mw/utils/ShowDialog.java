package com.mw.utils;

import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;

public class ShowDialog {
	
	public static ShowDialog INSTANCE = new ShowDialog();
	
	public void show(final String message){
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			
			public void run() {
			Screen activeScreen = UiApplication.getUiApplication().getActiveScreen();
			UiApplication.getUiApplication().popScreen(activeScreen);
	        Dialog.alert(message);
	        
			}
		});
	}

}
