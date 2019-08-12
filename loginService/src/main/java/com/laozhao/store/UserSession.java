package com.laozhao.store;

/*
   用户会话实体
 */
 class UserSession {
    private String   token;
    private String   account;
    private String   username;

    public String getToken() {
        return token;
    }

    public String getAccount() {
        return account;
    }

    public String getUsername() {
        return username;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
