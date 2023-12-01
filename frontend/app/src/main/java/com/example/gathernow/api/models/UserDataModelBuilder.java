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

    public UserDataModel build() {
        return userDataModel;
    }

}
