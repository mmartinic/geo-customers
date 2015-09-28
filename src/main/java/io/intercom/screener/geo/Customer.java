package io.intercom.screener.geo;

import com.google.gson.annotations.SerializedName;

public class Customer {

    @SerializedName("user_id")
    private final Integer userId;
    private final String name;
    private final Double latitude;
    private final Double longitude;

    public Customer(Integer userId, String name, Double latitude, Double longitude) {
        this.userId = userId;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Customer customer = (Customer) o;

        if (userId != null ? !userId.equals(customer.userId) : customer.userId != null) {
            return false;
        }
        if (name != null ? !name.equals(customer.name) : customer.name != null) {
            return false;
        }
        if (latitude != null ? !latitude.equals(customer.latitude) : customer.latitude != null) {
            return false;
        }
        return !(longitude != null ? !longitude.equals(customer.longitude) : customer.longitude != null);

    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (latitude != null ? latitude.hashCode() : 0);
        result = 31 * result + (longitude != null ? longitude.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
