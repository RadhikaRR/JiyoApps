package com.mw.customfields;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.XYRect;
import net.rim.device.api.ui.component.LabelField;

public class BackgrndLabelFieldImpl extends LabelField {
	private int fontColor = 0;
	private int bgColor = -1;

	public BackgrndLabelFieldImpl() {
		super();
	}

	public BackgrndLabelFieldImpl(Object text, long style) {
		super(text, style);
		// TODO Auto-generated constructor stub
	}

	public BackgrndLabelFieldImpl(Object text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	public BackgrndLabelFieldImpl(Object text, int string, int length, long style) {
		super(text, string, length, style);
	}

	public int getFontColor() {
		return fontColor;
	}

	public void setFontColor(int fontColor) {
		this.fontColor = fontColor;
	}

	public int getBgColor() {
		return bgColor;
	}

	public void setBgColor(int bgColor) {
		this.bgColor = bgColor;
	}

	protected void paint(Graphics graphics) {
		XYRect extent = graphics.getClippingRect();
		if (bgColor != -1) {
			graphics.setColor(bgColor);
			
			graphics.fillRect(extent.x, extent.y, extent.width, extent.height);
		}
		graphics.setColor(fontColor);
		super.paint(graphics);
		graphics.setColor(Color.BLACK);
		graphics.drawRect(extent.x, extent.y, extent.width, extent.height);
	}
}
