package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model-tier")
public class AccountAuthenticationTest {
    private final AccountAuthentication accountAuthentication = new AccountAuthentication();

    @Test
    public void testAccountAuthentication() throws Exception {
        // Setup
        Account account = new Account("admin","email", "password", "firstName", "lastName", 0, "");
        ActiveAccountSessions activeAccountSessions = new ActiveAccountSessions();
        AccountSession session = new AccountSession(account, activeAccountSessions);

        // Invoke
        boolean result = accountAuthentication.accountAuthentication(session, account);

        // Analysis
        assertTrue(result);
    }

    @Test
    public void testAccountAuthenticationFails() throws Exception {
        // Setup
        Account account2 = new Account("bhkb","email", "password", "firstName", "lastName", 999, "");
        ActiveAccountSessions activeAccountSessions = new ActiveAccountSessions();
        AccountSession session = new AccountSession(account2, activeAccountSessions);

        // Invoke
        boolean result = accountAuthentication.accountAuthentication(session, new Account("pfdsgnsfo","email", "password", "firstName", "lastName", 0, ""));

        // Analysis
        assertFalse(result);
    }

    @Test
    public void testAccountAuthenticationSuccess() throws Exception {
        // Setup
        Account account2 = new Account("aa","email", "password", "firstName", "lastName", 999, "");
        ActiveAccountSessions activeAccountSessions = new ActiveAccountSessions();
        AccountSession session = new AccountSession(account2, activeAccountSessions);

        // Invoke
        boolean result = accountAuthentication.accountAuthentication(session, new Account("bb","email", "password", "firstName", "lastName", 999, ""));

        // Analysis
        assertTrue(result);
    }

}