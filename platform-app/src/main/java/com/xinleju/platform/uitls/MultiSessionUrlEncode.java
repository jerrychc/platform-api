package com.xinleju.platform.uitls;

import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.session.web.http.HttpSessionManager;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by admin on 2017/9/28.
 */
public class MultiSessionUrlEncode {
    public static String encodeUrl(HttpServletRequest httpRequest, String url){
        HttpSessionManager sessionManager = (HttpSessionManager) httpRequest
                .getAttribute(HttpSessionManager.class.getName());
        SessionRepository<Session> repo = (SessionRepository<Session>) httpRequest
                .getAttribute(SessionRepository.class.getName());

        String currentSessionAlias = sessionManager.getCurrentSessionAlias(httpRequest);
        Map<String, String> sessionIds = sessionManager.getSessionIds(httpRequest);
        String unauthenticatedAlias = null;

        String contextPath = httpRequest.getContextPath();

        // tag::addAccountUrl[]
        String addAlias = unauthenticatedAlias == null ? // <1>
                sessionManager.getNewSessionAlias(httpRequest)
                : // <2>
                unauthenticatedAlias; // <3>
        String addAccountUrl = sessionManager.encodeURL(contextPath + "/" +url, currentSessionAlias);
        addAccountUrl = addAccountUrl.indexOf("?")!=-1?(addAccountUrl+"&_t="+System.currentTimeMillis()):(addAccountUrl+"?_t="+System.currentTimeMillis());

        return  addAccountUrl;
    }

    public static Session getSessionIdByAlias(HttpServletRequest httpRequest, String sessionAlias){
        HttpSessionManager sessionManager = (HttpSessionManager) httpRequest
                .getAttribute(HttpSessionManager.class.getName());
        SessionRepository<Session> repo = (SessionRepository<Session>) httpRequest
                .getAttribute(SessionRepository.class.getName());

        String currentSessionAlias = sessionManager.getCurrentSessionAlias(httpRequest);
        Map<String, String> sessionIds = sessionManager.getSessionIds(httpRequest);
        String unauthenticatedAlias = null;

        String contextPath = httpRequest.getContextPath();
        Session session =  null;
        for (Map.Entry<String, String> entry : sessionIds.entrySet()) {
            String alias = entry.getKey();
            String sessionId = entry.getValue();
            if(alias.equals(sessionAlias)){
                session = repo.getSession(sessionId);
                break;
            }
        }
        if(session == null){
            session = repo.createSession();
        }
        return session;
    }
}
