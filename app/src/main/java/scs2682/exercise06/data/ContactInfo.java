package scs2682.exercise06.data;

import android.os.Bundle;

public class ContactInfo {
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String PHONE = "phone";
    private static final String EMAIL = "email";

    private final Bundle bundle;

    public ContactInfo(String firstName, String lastName, String phone, String email){
        bundle = new Bundle();
        bundle.putString(FIRST_NAME, firstName);
        bundle.putString(LAST_NAME, lastName);
        bundle.putString(PHONE, phone);
        bundle.putString(EMAIL, email);
    }

    public ContactInfo(Bundle bundle){
        this.bundle = bundle;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ContactInfo){
            ContactInfo other = (ContactInfo) obj;
            return getFirstName().equals(other.getFirstName())
                    && getEmail().equals(other.getEmail());
        }
        else {
            return false;
        }
    }

    public Bundle getBundle() {
        return bundle;
    }

    public String getFirstName() {
        return bundle.getString(FIRST_NAME, "");
    }

    public String getLastName() {
        return bundle.getString(LAST_NAME, "");
    }

    public String getPhone() {
        return bundle.getString(PHONE, "");
    }

    public String getEmail() {
        return bundle.getString(EMAIL, "");
    }
}
