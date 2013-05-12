package com.mw.control;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class dialogForNewsFeedIndicator extends PopupScreen {

	private LabelField msgTextHeading, msgTextMessage;
	private ButtonField ok;

	public dialogForNewsFeedIndicator(String msg) {

		super(new VerticalFieldManager(), Field.FOCUSABLE);

		VerticalFieldManager vfm = new VerticalFieldManager(
				Manager.VERTICAL_SCROLL);
		
		HorizontalFieldManager hfm = new HorizontalFieldManager();	

		msgTextHeading = new LabelField("New JiyoApp 'NewsFeed'",
				Field.USE_ALL_WIDTH | Field.FIELD_HCENTER | Field.NON_FOCUSABLE |DrawStyle.HCENTER);	
		
		hfm.add(new BitmapField(Bitmap.getBitmapResource("53X48pxmisdata2icon.png")));		
		hfm.add(msgTextHeading);		

		msgTextMessage = new LabelField(msg, Field.USE_ALL_WIDTH
				| Field.FIELD_HCENTER | Field.NON_FOCUSABLE);

		ok = new ButtonField("OK", Field.FIELD_HCENTER);
		ok.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				if (field == ok) {
					close();				
				}
			}
		});

		vfm.add(hfm);	
		vfm.add(new SeparatorField());
		vfm.add(msgTextMessage);		
		vfm.add(ok);

		add(vfm);
	}
}
