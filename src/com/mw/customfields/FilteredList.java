package com.mw.customfields;

import java.util.Vector;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.ObjectListField;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class FilteredList extends Manager implements FieldChangeListener {
	
	private String[] dummayArray = new String[0];
	private String[] array;
	private EditField editField;
	private ObjectListField listField;
	private VerticalFieldManager listManager;
	private int searchIndex;

	public void setText(String text) {
		editField.setText(text);
	}

	public String getText() {
		return editField.getText();
	}

	public int getSearchIndex() {
		return searchIndex;
	}

	public void setSearchIndex(int searchIndex) {
		this.searchIndex = searchIndex;
	}

	public FilteredList(String[] listArray, long style) {

		super(NO_VERTICAL_SCROLL | NO_VERTICAL_SCROLLBAR);

		this.array = listArray;

		editField = new EditField("", "", 50, style) {
			protected void paintBackground(Graphics g) {
				g.clear();
				if (isFocus()) {
					g.setColor(0x000072BC);
					invalidate();
				} else {
					g.setColor(Color.BLACK);
				}
				g.drawRect(0, 0, getWidth(), getHeight());
			}

			protected void paint(Graphics graphics) {
				graphics.setColor(Color.BLACK);
				super.paint(graphics);
			}

			protected boolean navigationClick(int status, int time) {							
				fieldChangeNotify(0);
				return true;
			}
		};
		editField.setChangeListener(this);

		listField = new ObjectListField() {
			protected boolean navigationClick(int status, int time) {
				int index = listField.getSelectedIndex();
				String string = listField.get(listField, index).toString();
				editField.setText(string);

				UiApplication.getUiApplication().invokeLater(new Runnable() {

					public void run() {
						dummayArray = new String[0];
						listField.set(dummayArray);
						listField.setEmptyString("", 0);
						invalidate();
					}
				});
				return true;
			}
		};
		listField.set(dummayArray);
		listField.setEmptyString("Type for suggestions", 0);

		listManager = new VerticalFieldManager(VERTICAL_SCROLL
				| VERTICAL_SCROLLBAR);
		listManager.add(listField);

		add(editField);
		add(listManager);// R
	}

	protected void onUnfocus() {
		super.onUnfocus();
		listField.set(dummayArray);
		listField.setEmptyString("Type for suggession", 0);
	}

	private void initManager(boolean expended) {
		if (expended) {
			add(listManager);
			setFieldWithFocus(editField);
		} else {
			delete(listManager);
		}
		invalidate();
	}

	protected void sublayout(int width, int height) {
		int editFieldHeight = 0;
		int listManagerHeight = 0;

		for (int i = 0; i < getFieldCount(); i++) {
			Field field = getField(i);
			if (field == editField) {
				layoutChild(editField, width, height);
				editFieldHeight = editField.getHeight();
				setPositionChild(editField, 0, 0);
			} else if (field == listManager) {
				layoutChild(listManager, width, 100);
				listManagerHeight = listManager.getHeight();
				setPositionChild(listManager, 0, editFieldHeight);
			}
		}
		setExtent(width, editFieldHeight + listManagerHeight);
	}

	public void fieldChanged(Field field, int context) {
		String text = editField.getText();
		reInitializeList(text);
		reInitializeList(text);
	}

	private boolean compare(String string, String string2) {

		if (string.length() > string2.length()) {
			return false;
		}

		string = string.toLowerCase();
		string2 = string2.toLowerCase();

		/*
		 * Rahul exception may come:array out of bound exception when length of
		 * iccode ayyay element is less than 4 since getsearchindex is 4 .so
		 * handle later
		 */
		try {
			for (int i = 0; i < string.length(); i++) {
				if (string.charAt(i) != string2.charAt(i)) {
					return false;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return true;
	}

	private void reInitializeList(String string) {
		Vector vector = new Vector();

		for (int i = 0; i < array.length; i++) {
			boolean match = compare(string, array[i]);
			if (match) {
				vector.addElement(array[i]);
			}
		}

		String[] newdummayArray = new String[vector.size()];
		for (int i = 0; i < vector.size(); i++) {
			newdummayArray[i] = (String) vector.elementAt(i);
		}
		listField.set(newdummayArray);

		if (newdummayArray.length == 0) {
			listField.set(dummayArray);
			listField.setEmptyString("No Suggestions found", 0);
		}
		invalidate();
	}
}
