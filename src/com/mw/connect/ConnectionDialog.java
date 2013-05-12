package com.mw.connect;

import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.RadioButtonField;
import net.rim.device.api.ui.component.RadioButtonGroup;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.component.Status;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.mw.constants.Constant;
import com.mw.control.LogEventClass;
import com.mw.control.NewAuthenticator;

public class ConnectionDialog extends PopupScreen {
	
	private LabelField msgTextHeading;
	private RadioButtonGroup buttonGroup;
	private RadioButtonField bis;
	private RadioButtonField tcpIp;
	private ButtonField connect;

	public ConnectionDialog() {
		super(new VerticalFieldManager());
		
		VerticalFieldManager vfm = new VerticalFieldManager(
				Manager.VERTICAL_SCROLL);
		
		HorizontalFieldManager hfm = new HorizontalFieldManager();	
		
		msgTextHeading = new LabelField("Select the Connection Type",
				Field.USE_ALL_WIDTH | Field.FIELD_HCENTER | Field.NON_FOCUSABLE |DrawStyle.HCENTER);	
		
		hfm.add(msgTextHeading);	
		
		buttonGroup = new RadioButtonGroup();
		tcpIp = new RadioButtonField("TCP/IP(GPRS connection)", buttonGroup, true);
		bis = new RadioButtonField("BIS(BlackberryInternetService)", buttonGroup, false);
		
		connect = new ButtonField("Connect",FIELD_HCENTER) {
			protected boolean navigationClick(int status, int time) {
				int selectedIndex = buttonGroup.getSelectedIndex();
				switch (selectedIndex) {
				case 0:	
//					Constant.connectTCP = true;
//					LogEventClass.logAlwaysEvent("TCP is selected");
//					NewAuthenticator.INSTANCE.authenticate("Authenticating User..");
//					break;

				case 1:
//					Constant.connectBIS = true;
//					LogEventClass.logAlwaysEvent("BIS is selected");
//					NewAuthenticator.INSTANCE.authenticate("Authenticating User..");
//					break;
				}
				return true;
			};
		};
		
		LabelField forgotPassLabel = new LabelField("Help?", FIELD_RIGHT
				| FOCUSABLE) {
			protected boolean navigationClick(int status, int time) {
				Status.show("Why Forgot Password?");
				// Dialog.alert("Why Forgot Password?");
				return true;
			}
		};
		forgotPassLabel.setMargin(0, 10, 0, 0);
		
		vfm.add(hfm);	
		vfm.add(new SeparatorField());
		vfm.add(tcpIp);	
		vfm.add(bis);	
		vfm.add(connect);
		vfm.add(forgotPassLabel);
		
		add(vfm);
	}
}
