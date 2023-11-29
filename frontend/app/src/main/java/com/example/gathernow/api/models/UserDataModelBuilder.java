package com.example.gathernow.api.models;

public class UserDataModelBuilder {
    // builder for UserDataModel
    private UserDataModel userDataModel;
    public UserDataModelBuilder() {
        userDataModel = new UserDataModel();
    }

    public UserDataModelBuilder setEmail(String email) {
        userDataModel.setEmail(email);
        return this;
    }

    public UserDataModelBuilder setPassword(String password) {
        userDataModel.setPassword(password);
        return this;
    }

    public UserDataModelBuilder setName(String name) {
        userDataModel.setName(name);
        return this;
    }

    public UserDataModelBuilder setAvatar(String avatar) {
        userDataModel.setAvatar(avatar);
        return this;
    }

    public UserDataModelBuilder setUserId(Integer user_id) {
        userDataModel.setUserId(user_id);
        return this;
    }

    public UserDataModelBuilder setCreatedAt(String created_at) {
        userDataModel.setCreatedAt(created_at);
        return this;
    }

    public UserDataModel build() {
        return userDataModel;
    }

}
