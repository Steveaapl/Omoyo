package com.google.muditi.deligoo;

import android.content.Intent;

import com.example.muditi.deligoo.*;
import com.example.muditi.deligoo.Registrationid;
import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by muditi on 25-12-2015.
 */
public class TokenRefress extends InstanceIDListenerService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        Intent intent = new Intent(this, Registrationid.class);
        startService(intent);
    }
}
