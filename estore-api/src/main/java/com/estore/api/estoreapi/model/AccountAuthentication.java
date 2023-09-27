package com.estore.api.estoreapi.model;

public class AccountAuthentication {
    public AccountAuthentication() {}

    /**
     * Validate that the user is in the right session
     * @param session the current session
     * @param account the account that requesting authentication
     * @return true if the account is admin or if the account is in the right session else return false
     */
    public boolean accountAuthentication(AccountSession session, Account account) {
        if (session.getLoggedAccount().getUsername().equals("admin")) {
            return true;
        }
        return session.getLoggedAccountID() == account.getId();
    }
}
