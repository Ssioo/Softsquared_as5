package com.softsqaured.softsquared_as5.Objects;

import org.json.JSONException;
import org.json.JSONObject;

public class UserInfo {
    private String email="";
    private String name="";
    private String profileUrl="";

    private static UserInfo userInfo = new UserInfo();

    private UserInfo() {
    }

    public static UserInfo getInstance() {
        return userInfo;
    }

    public String getEmail() {
        return email;
    }

    public void setJSON(String Naverjson) {
        try {
            JSONObject jsonObject = new JSONObject(Naverjson);
            JSONObject profileObject = jsonObject.getJSONObject("response");
            setName(profileObject.getString("name"));
            setEmail(profileObject.getString("email"));
            setProfileUrl(profileObject.getString("profile_image"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void clear() {
        email = "";
        name = "";
        profileUrl = "";
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }
}
