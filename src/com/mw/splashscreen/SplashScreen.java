package com.mw.splashscreen;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.ContextMenu;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.decor.BackgroundFactory;

import com.mw.utils.Utils;

public class SplashScreen extends MainScreen {

	private Bitmap bitmap;
	private Bitmap resizebitmap;
	private BitmapField bitmapField;

	public SplashScreen() {
		show();
	}

	private void show() {
		this.getMainManager().setBackground(
				BackgroundFactory.createLinearGradientBackground(0x00FFFFFF,
						0x00FFFFFF, 0x0064AFF2, 0x0064AFF2));

		bitmap = Bitmap.getBitmapResource("320X240splashscreenmis.png");

		resizebitmap = Utils.resizeBitmap(bitmap, Display.getWidth(), Display
				.getHeight(), false);

		bitmapField = new BitmapField(resizebitmap);
		add(bitmapField);
	}

	protected void makeContextMenu(ContextMenu contextMenu) {
		super.makeContextMenu(contextMenu);
	}

	public boolean onClose() {
		System.exit(0);
		return super.onClose();
	}
}
