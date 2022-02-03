package com.alp.chatuygulamasi;

public class UserList {
    private String name;

    private String userId;
    private String profilImageUrl;

    public UserList() {
    }

    public UserList(String name , String userId, String profilImageUrl) {
        this.name = name;

        this.userId = userId;
        this.profilImageUrl = profilImageUrl;
    }

    public String getProfilImageUrl() {
        return profilImageUrl;
    }

    public void setProfilImageUrl(String profilImageUrl) {
        this.profilImageUrl = profilImageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
