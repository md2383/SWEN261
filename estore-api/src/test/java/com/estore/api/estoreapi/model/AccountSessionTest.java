package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model-tier")
public class AccountSessionTest {
    private final Account account = new Account("admin","email", "password", "firstName", "lastName", 6, "");
    private ActiveAccountSessions activeAccountSessions = new ActiveAccountSessions();

    @Test
    public void testGetSessionID() throws Exception {
        AccountSession session = new AccountSession(account, activeAccountSessions);
        // Setup
        int expected = session.getSessionID();

        // Invoke
        int result = session.getSessionID();

        // Analysis
        assertEquals(expected, result);
    }

    @Test
    public void testGetLoggedAccount() throws Exception{
        AccountSession session = new AccountSession(account, activeAccountSessions);
        // Setup
        Account expected = account;

        // Invoke
        Account result = session.getLoggedAccount();

        // Analysis
        assertEquals(expected, result);
    }

    @Test
    public void testGetLoggedAccountID() throws Exception{
        AccountSession session = new AccountSession(account, activeAccountSessions);
        // Setup
        int expected = 6;

        // Invoke
        int result = session.getLoggedAccountID();

        // Analysis
        assertEquals(expected, result);
    }
    
}