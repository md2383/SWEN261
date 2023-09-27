package com.estore.api.estoreapi.model;

import java.util.ArrayList;
import java.util.List;

public class ActiveAccountSessions {
    public List<AccountSession> activeSessions;

    /**
     * Create a list of active accounts as sessions
     */
    public ActiveAccountSessions() {
        activeSessions = new ArrayList<AccountSession>();
    }

    /**
     * Get a list of all online/active sessions
     * @return list of AccountSession
     */
    public List<AccountSession> getSessions() {
        return activeSessions;
    }

    /**
     * Add a session (account is logged in as session)
     * @param session new session to be added
     */
    public void addSession(AccountSession session) {
        activeSessions.add(session);
    }

    /**
     * Remove a session (account logged out as session)
     * @param session exisiting session to be removed
     */
    public void removeSession(AccountSession session) {
        activeSessions.remove(session);
    }
}
