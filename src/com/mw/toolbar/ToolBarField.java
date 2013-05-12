package com.mw.toolbar;

import java.util.Enumeration;
import java.util.Vector;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.TouchEvent;
import net.rim.device.api.ui.XYRect;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;

public class ToolBarField extends HorizontalFieldManager {
	private static final int DefaultButtonHeight = 40;

	private Vector leftJustifiedButtons = new Vector();
	private Vector rightJustifiedButtons = new Vector();
	private int preferredHeight = DefaultButtonHeight;
	private int sideMargin = 0;
	private int buttonSpacing = 2;
	private int preferredWidth = Display.getWidth();
	private Bitmap bg = null;

	public ToolBarField() {
		this
				.setBackground(BackgroundFactory
						.createSolidBackground(Color.BLACK));
		bg = Bitmap.getBitmapResource("newtoolbarback.PNG");
	}

	public ToolBarField(Bitmap backgroundImage, int lrMargin, int buttonSpace) {
		this
				.setBackground(BackgroundFactory
						.createSolidBackground(Color.BLACK));
		bg = backgroundImage;
		sideMargin = lrMargin;
		buttonSpacing = buttonSpace;
	}

	public int getButtonSpacing() {
		return buttonSpacing;
	}

	public void setButtonSpacing(int buttonSpacing) {
		this.buttonSpacing = buttonSpacing;
	}

	public int getPreferredHeight() {
		return preferredHeight;
	}

	public int getPreferredWidth() {
		return preferredWidth;
	}

	public void addButton(ToolBarButtonField button, boolean leftJustified) {
		super.add(button);

		if (button.getPreferredHeight() > preferredHeight)
			preferredHeight = button.getPreferredHeight();

		if (leftJustified) {
			leftJustifiedButtons.addElement(button);
		} else {
			rightJustifiedButtons.addElement(button);
		}
	}

	public void onUnfocus() {
		for (int f = 0; f < this.getFieldCount(); f++) {
			Field field = getField(f);

			if (field instanceof ToolBarButtonField) {
				((ToolBarButtonField) field).setActive(true);
			}
		}
	}

	public boolean isFocusable() {
		return true;
	}

	//
	// Override to render the toolbar background
	//
	protected void subpaint(Graphics graphics) {
		if (bg != null) {
			for (int x = 0; x < Display.getWidth();)// /check for
			{
				graphics.drawBitmap(x, 0, getPreferredWidth(), bg.getHeight(),
						bg, 0, 0);
				x += bg.getWidth();
			}
		} else {
			graphics.setColor(Color.BLACK);
			graphics.drawRect(0, 0, getPreferredWidth(), getPreferredHeight());
		}

		super.subpaint(graphics);
	}

	public boolean touchEvent(TouchEvent event) {
		int eventID = event.getEvent();
		if (eventID == TouchEvent.DOWN || eventID == TouchEvent.UP) {
			boolean hit = false;
			int x = event.getX(1);
			int y = event.getY(1);

			for (int f = 0; f < getFieldCount(); f++) {
				ToolBarButtonField field = (ToolBarButtonField) getField(f);
				XYRect ext = field.getExtent();
				if (ext.contains(x, y)) {
					hit = true;
					if (eventID == TouchEvent.UP) {
						field.setActive(false);
						this.setFocus();
					} else {
						field.setFocus();
						field.setActive(true);
					}
					invalidate();
					break;
				}
			}

			if (!hit && eventID == TouchEvent.UP) {
				this.setFocus();
				invalidate();
			}
		}
		return false;
	}

	protected void sublayout(int maxWidth, int maxHeight) {
		this.setExtent(maxWidth, getPreferredHeight());

		Enumeration iter = leftJustifiedButtons.elements();

		if (maxHeight > Display.getHeight())
			maxHeight = Display.getHeight();

		int y = 0;
		int curX = sideMargin;

		//
		// Layout the left justified buttons
		//
		while (iter.hasMoreElements()) {
			ToolBarButtonField button = (ToolBarButtonField) iter.nextElement();
			this.layoutChild(button, button.getPreferredWidth(), button
					.getPreferredWidth());

			this.setPositionChild(button, curX, 0);
			curX = curX + button.getWidth();
		}

		int minX = curX + buttonSpacing;
		int totalButtonsWidth = 0;
		//
		// Layout the right justified buttons
		//
		iter = rightJustifiedButtons.elements();

		while (iter.hasMoreElements()) {
			ToolBarButtonField button = (ToolBarButtonField) iter.nextElement();
			this.layoutChild(button, button.getPreferredWidth(), button
					.getPreferredWidth());
			totalButtonsWidth += button.getWidth() + buttonSpacing;
		}
		totalButtonsWidth -= buttonSpacing;

		if ((totalButtonsWidth + minX) > maxWidth) {
			preferredWidth = totalButtonsWidth + minX;
		}
		curX = maxWidth;
		iter = rightJustifiedButtons.elements();
		while (iter.hasMoreElements()) {
			ToolBarButtonField button = (ToolBarButtonField) iter.nextElement();
			this.layoutChild(button, button.getPreferredWidth(), button
					.getPreferredWidth());

			curX = curX - button.getWidth();
			this.setPositionChild(button, curX, y);
			curX -= buttonSpacing;
		}
	}
}