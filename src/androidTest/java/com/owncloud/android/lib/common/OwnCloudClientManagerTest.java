package com.owncloud.android.lib.common;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.net.Uri;
import android.os.Bundle;

import com.owncloud.android.AbstractIT;
import com.owncloud.android.lib.common.accounts.AccountUtils;

import org.junit.Test;

import java.io.IOException;

import androidx.test.platform.app.InstrumentationRegistry;

import static org.junit.Assert.assertEquals;

public class OwnCloudClientManagerTest extends AbstractIT {

    /**
     * Like on files app we create & store an account in Android's account manager.
     */
    @Test
    public void testUserId() throws OperationCanceledException, AuthenticatorException, IOException,
            AccountUtils.AccountNotFoundException {
        Bundle arguments = InstrumentationRegistry.getArguments();

        Uri url = Uri.parse(arguments.getString("TEST_SERVER_URL"));
        String loginName = arguments.getString("TEST_SERVER_USERNAME");
        String password = arguments.getString("TEST_SERVER_PASSWORD");

        AccountManager accountManager = AccountManager.get(context);
        String accountName = com.owncloud.android.lib.common.accounts.AccountUtils.buildAccountName(url, loginName);
        Account newAccount = new Account(accountName, "nextcloud");

        accountManager.addAccountExplicitly(newAccount, password, null);
        accountManager.setUserData(newAccount, AccountUtils.Constants.KEY_OC_BASE_URL, url.toString());
        accountManager.setUserData(newAccount, AccountUtils.Constants.KEY_USER_ID, loginName);

        OwnCloudClientManager manager = new OwnCloudClientManager();
        OwnCloudAccount account = new OwnCloudAccount(newAccount, context);

        OwnCloudClient client = manager.getClientFor(account, context);

        assertEquals(loginName, client.getUserId());
    }
}
