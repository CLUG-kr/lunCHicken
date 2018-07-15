package com.lunCHicken;

import org.json.JSONObject;

public interface LoginSocketListener {

    public void onMessage(String action, JSONObject data);

}
