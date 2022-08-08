package db;

public class User {
    private String registrationNumber;
    private String fullName;
    private String nic;
    private String address;
    private String phoneNumber;

    public User() {
    }

    public User(String registrationNumber, String fullName, String nic, String address, String phoneNumber) {
        this.registrationNumber = registrationNumber;
        this.fullName = fullName;
        this.nic = nic;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String firstName) {
        this.fullName = firstName;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
