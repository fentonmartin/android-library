package com.owncloud.android;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import com.owncloud.android.lib.common.OwnCloudBasicCredentials;
import com.owncloud.android.lib.common.OwnCloudClient;
import com.owncloud.android.lib.common.OwnCloudClientFactory;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

/**
 * Common base for all integration tests
 */

@RunWith(AndroidJUnit4.class)
public abstract class AbstractIT {
    static OwnCloudClient client;
    protected static Context context;

    @BeforeClass
    public static void beforeAll() {
        Bundle arguments = InstrumentationRegistry.getArguments();
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        Uri url = Uri.parse(arguments.getString("TEST_SERVER_URL"));
        String loginName = arguments.getString("TEST_SERVER_USERNAME");
        String password = arguments.getString("TEST_SERVER_PASSWORD");

        client = OwnCloudClientFactory.createOwnCloudClient(url, context, true);
        client.setCredentials(new OwnCloudBasicCredentials(loginName, password));
        client.setUserId(loginName); // for test same as userId
    }
}
