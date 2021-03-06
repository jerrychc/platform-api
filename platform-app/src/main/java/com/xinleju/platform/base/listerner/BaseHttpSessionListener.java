package com.xinleju.platform.base.listerner;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class BaseHttpSessionListener implements HttpSessionListener{

	@Override
	public void sessionCreated(HttpSessionEvent event) {
		// TODO Auto-generated method stub
		HttpSession session = event.getSession();
        System.out.println("----------------------------------sessionCreated");
        System.out.println("sessionId:                      " + session.getId());

        System.out.println("sessionCreationTime:            " + session.getCreationTime());

        System.out.println("sessionLastAccessedTime:        "+session.getLastAccessedTime());

        int maxInterval = session.getMaxInactiveInterval();
        System.out.println("sessionMaxInactiveInterval(s):  " + session.getMaxInactiveInterval());

        System.out.println("sessionExpirtion:               " + (session.getCreationTime() + maxInterval*1000)) ;       


        System.out.println("----------------------------------sessionCreated");
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		// TODO Auto-generated method stub
		HttpSession session = se.getSession();
        System.out.println("----------------------------------sessionDestroyed");
        System.out.println("sessionId:              " + session.getId());

        System.out.println("sessionCreationTime:    " + session.getCreationTime());

        System.out.println("sessionLastAccessedTime:"+session.getLastAccessedTime());

        System.out.println("hsName:                 "+session.getAttribute("hsName"));

        System.out.println("-----------------------------------sessionDestroyed");
	}

}
