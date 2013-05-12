package com.mw.toolbar;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.TouchEvent;
import net.rim.device.api.ui.UiApplication;

public class ImageButtonField extends Field {
	private Bitmap currentImage;
	private Bitmap activeImage;
	private Bitmap inactiveImage;

	public ImageButtonField(Bitmap activeImage, Bitmap inactiveImage, long style) {
		super(style);
		this.activeImage = activeImage;
		this.inactiveImage = inactiveImage;
		currentImage = inactiveImage;
	}

	public int getPreferredHeight() {
		return currentImage.getHeight();
	}

	public int getPreferredWidth() {
		return currentImage.getWidth();
	}

	protected void drawFocus(Graphics graphics, boolean on) {

	}

	public void setActive(boolean v) {
		if (v)
			currentImage = inactiveImage;

		else
			currentImage = activeImage;

		invalidate();
	}

	protected boolean touchEvent(TouchEvent event) {
		boolean result = false;

		if (event.getEvent() == TouchEvent.DOWN) {
			currentImage = activeImage;
			result = true;
		} else if (event.getEvent() == TouchEvent.UP) {
			currentImage = inactiveImage;
			result = true;
		}

		if (result) {
			// Put the invalidate call on the message queue
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					ImageButtonField.this.getManager().invalidate();
				}
			});
		}

		return result;
	}

	protected void layout(int width, int height) {
		int w = Math.min(width, getPreferredWidth());
		int h = Math.min(height, getPreferredHeight());
		setExtent(w, h);
	}

	protected void paint(Graphics graphics) {
		graphics.setColor(0x000072BC);
		int width = getWidth();
		int height = getHeight();
		graphics.fillRect(0, 0, width, height);
		graphics.drawBitmap(0, 0, width, height, currentImage, 0, 0);
	}

	/**
	 * Overridden so that the Event Dispatch thread can catch this event instead
	 * of having it be caught here..
	 * 
	 * @see net.rim.device.api.ui.Field#navigationClick(int, int)
	 */

	protected boolean navigationClick(int status, int time) {

		fieldChangeNotify(1);
		return true;
	}

	protected boolean keyChar(char character, int status, int time) {
		if (character == Keypad.KEY_ENTER) {

			fieldChangeNotify(1);
			return true;
		}
		return super.keyChar(character, status, time);
	}

}