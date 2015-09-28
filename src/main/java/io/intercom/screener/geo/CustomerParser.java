package io.intercom.screener.geo;

import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

public class CustomerParser {

    private static final Logger logger = LogManager.getLogger(CustomerParser.class);

    private final Gson gson = new Gson();

    public Stream<Customer> parseCustomers(Stream<String> linesStream) {
        if (linesStream == null) {
            return Stream.empty();
        }
        return linesStream.map(this::parseCustomer)
                .filter(customer -> customer != null);
    }

    public Customer parseCustomer(String s) {
        try {
            Customer customer = gson.fromJson(s, Customer.class);
            if (isCustomerValid(customer)) {
                return customer;
            } else {
                logger.error("Error parsing string: " + s);
            }
        } catch (Exception e) {
            logger.error("Error parsing string: " + s, e);
        }
        return null;
    }

    private boolean isCustomerValid(Customer customer) {
        if (customer == null) {
            return false;
        }
        if (customer.getUserId() == null) {
            return false;
        }
        if (customer.getUserId() < 0) {
            return false;
        }
        if (customer.getName() == null) {
            return false;
        }
        if (customer.getLatitude() == null) {
            return false;
        }
        if (!GeoUtils.isLatitudeValid(customer.getLatitude())) {
            return false;
        }
        if (customer.getLongitude() == null) {
            return false;
        }
        if (!GeoUtils.isLongitudeValid(customer.getLongitude())) {
            return false;
        }
        return true;
    }
}
