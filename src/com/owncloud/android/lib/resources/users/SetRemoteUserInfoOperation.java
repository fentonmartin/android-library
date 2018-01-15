package com.owncloud.android.lib.resources.users;/*
 * Nextcloud Android client application
 *
 * @author Barotsz Przybylski
 * Copyright (C) 2018 Bartosz Przybylski
 * Copyright (C) 2018 Nextcloud GmbH.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

import com.owncloud.android.lib.common.OwnCloudClient;
import com.owncloud.android.lib.common.operations.RemoteOperation;
import com.owncloud.android.lib.common.operations.RemoteOperationResult;
import com.owncloud.android.lib.common.utils.Log_OC;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;


public class SetRemoteUserInfoOperation extends RemoteOperation {

    private static final String TAG = SetRemoteUserInfoOperation.class.getSimpleName();

    private static final String OCS_ROUTE_PATH = "/ocs/v1.php/users/";

    public static final String FIELD_EMAIL = "email";
    public static final String FIELD_PHONE = "phone";
    public static final String FIELD_ADDRESS = "address";
    public static final String FIELD_WEBPAGE = "webpage";
    public static final String FIELD_TWITTER = "twitter";

    private static final String ENTITY_CONTENT_TYPE = "application/x-www-form-urlencoded";
    private static final String ENTITY_CHARSET = "UTF-8";

    private String mUserId;
    private String mFieldName;
    private String mValue;

    public SetRemoteUserInfoOperation(String userId, String fieldName, String value) {
        mUserId = userId;
        mFieldName = fieldName;
        mValue = value;
    }

    @Override
    protected RemoteOperationResult run(OwnCloudClient client) {
        RemoteOperationResult result = null;
        PutMethod method = null;

        try {
            method = new PutMethod(client.getBaseUri() + OCS_ROUTE_PATH + mUserId);
            method.addRequestHeader(OCS_API_HEADER, OCS_API_HEADER_VALUE);
            method.setRequestEntity(new StringRequestEntity(
                    mFieldName + "=" + mValue,
                    ENTITY_CONTENT_TYPE,
                    ENTITY_CHARSET));

            int status = client.executeMethod(method);

            if (status == HttpStatus.SC_OK) {
                result = new RemoteOperationResult(true, method);

            } else {
                result = new RemoteOperationResult(false, method);
                String response = method.getResponseBodyAsString();
                Log_OC.e(TAG, "Failed response while setting user information");
                Log_OC.e(TAG, "*** status code: " + status + "; response: " + response);
            }
        } catch (Exception e) {
            result = new RemoteOperationResult(e);
            Log_OC.e(TAG, "Exception while setting OC user information", e);
        } finally {
            if (method != null)
                method.releaseConnection();
        }

        return result;
    }
}
