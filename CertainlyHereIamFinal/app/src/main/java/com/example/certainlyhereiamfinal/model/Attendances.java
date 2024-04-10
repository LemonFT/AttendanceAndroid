package com.example.certainlyhereiamfinal.model;

public class Attendances {
    private String email;
    private String name;
    private String identifier;
    private boolean checked;

    public Attendances(String email, String name, String identifier, boolean checked) {
        this.email = email;
        this.name = name;
        this.identifier = identifier;
        this.checked = checked;
    }

    public String getEmail() {
        return email;
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

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}