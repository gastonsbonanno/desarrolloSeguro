package com.hermosaprogramacion.blog.saludmock.data.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Objeto plano Java para representar el cuerpo de la petición POST /affiliates/login
 */
public class RegisterBody {
    @SerializedName("id")
    private String userId;
    private String name;
    private String address;
    private String gender;
    private String password;


    public RegisterBody(String userId, String name, String address, String gender, String password) {
        this.userId = userId;
        this.name = name;
        this.address = address;
        this.gender = gender;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
