package com.estore.api.estoreapi.model;

public class AccountSession {
    private static int idChanger = 1;
    private int sessionID;
    private Account loggedAccount;

    /**
     * Account Sessions 
     * @param Account an account 
     * @param ActiveAccountSessions the accounts that are all in sessions 
     * 
     */
    public AccountSession(Account loggedAccount, ActiveAccountSessions activeAccounts) throws Exception {
        for (AccountSession session : activeAccounts.getSessions()) {
            if (session.getLoggedAccountID() == loggedAccount.getId()) {
                throw new Exception();                
            }
        }
        this.sessionID = idChanger++;
        this.loggedAccount = loggedAccount;
    }

    /**
     * gets sessionID 
     * @return an int id 
     */
    public int getSessionID() {
        return sessionID;
    }

    /**
     * gets account
     * @return an account 
     */
    public Account getLoggedAccount() {
        return loggedAccount;
    }

    /**
     * gets account id 
     * @return an account int id 
     */
    public int getLoggedAccountID() {
        return loggedAccount.getId();
    }
}
