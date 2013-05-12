package com.mw.cameraCapture;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import net.rim.device.api.io.file.FileSystemJournal;
import net.rim.device.api.io.file.FileSystemJournalEntry;
import net.rim.device.api.io.file.FileSystemJournalListener;
import net.rim.device.api.ui.UiApplication;

public class FileSystemJournalListenerImpl implements FileSystemJournalListener {

	public static FileSystemJournalListenerImpl INSTANCE = new FileSystemJournalListenerImpl();
	private long _lastUSN;

	private Vector fileNames = new Vector();
	
	public static Vector byteArrVector = new Vector();

	private FileSystemJournalListenerImpl() {

	}

	public void fileJournalChanged() {
		long nextUSN = FileSystemJournal.getNextUSN();
		String path = null;
		for (long lookUSN = nextUSN - 1; lookUSN >= _lastUSN; --lookUSN) {
			FileSystemJournalEntry entry = FileSystemJournal.getEntry(lookUSN);

			if (entry == null) {
				break;
			}

			path = entry.getPath();
			System.out.println("path is----------------" + path);

			if (entry.getEvent() == FileSystemJournalEntry.FILE_ADDED) {
				switch (entry.getEvent()) {
				case FileSystemJournalEntry.FILE_ADDED:
					String capturedVideoPath = path;
					if (capturedVideoPath != null) {
						fileNames.addElement(path);
					}
					break;
				}
			}
		}
		_lastUSN = nextUSN;
	}

	public void startListening() {
		fileNames.removeAllElements();
		byteArrVector.removeAllElements();
		UiApplication.getUiApplication().addFileSystemJournalListener(this);
		_lastUSN = FileSystemJournal.getNextUSN();

	}

	public void stopListening() {
		UiApplication.getUiApplication().removeFileSystemJournalListener(this);
	}

	public Vector getFileNames() {
		return fileNames;
	}

	public void setFileNames(Vector fileNames) {
		this.fileNames = fileNames;
	}

	public Vector getRecordedFilePath() {
		
		for (int i = 0; i < fileNames.size(); i++) {
			
			String file = ((String) fileNames.elementAt(i));
			System.out.println("fileName when added into Vector inside for loop------------------------"+ file);

			if(file != null){
				InputStream input = null;
				FileConnection fconn = null;
				try {
					fconn = (FileConnection) Connector.open("file://" + file);
					if (fconn.exists()) {
						input = fconn.openInputStream();
						byte[] b = new byte[2048];
						ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
						while (true) {
							int read = input.read(b);
							if (read == -1) {
								break;
							}
							arrayOutputStream.write(b, 0, read);
						}
						byte[] data = arrayOutputStream.toByteArray();
						byteArrVector.addElement(data);
						System.out.println("size of vector is-------------------------"+byteArrVector.size());
					}
					fconn.delete();
					fconn.close();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}			
		}
		return byteArrVector;
	}
}
