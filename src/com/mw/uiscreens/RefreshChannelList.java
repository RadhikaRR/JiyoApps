package com.mw.uiscreens;

import java.util.TimerTask;

import com.mw.webservice.CallService;

public class RefreshChannelList extends TimerTask {

	public void run() {			
			CallService.INSTANCE.publishCeoTrackerChannel(true);		
	}
}
