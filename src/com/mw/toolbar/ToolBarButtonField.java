package com.mw.toolbar;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Field;

public class ToolBarButtonField extends ImageButtonField {
	private static final int WIDTH = 81;
	private static final int HEIGHT = 40;
	int prefWidth = WIDTH;
	int prefHeight = HEIGHT;

	public ToolBarButtonField(Bitmap focusImage, Bitmap unFocusImage) {
		super(focusImage, unFocusImage, Field.FOCUSABLE);
		//super.setPadding(0, -10, 0, -10);
		if (focusImage.getWidth() < WIDTH)
			prefWidth = focusImage.getWidth();

		if (focusImage.getHeight() < HEIGHT)
			prefHeight = focusImage.getHeight();
	}

	public int getPreferredHeight() {
		return prefHeight;
	}

	public int getPreferredWidth() {
		return prefWidth;
	}
}
