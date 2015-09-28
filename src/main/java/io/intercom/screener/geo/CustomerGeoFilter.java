package io.intercom.screener.geo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomerGeoFilter {

    private final CustomerParser customerParser = new CustomerParser();
    private final double centerLatitude;
    private final double centerLongitude;
    private final double cutoffRadius;

    public CustomerGeoFilter(double centerLatitude, double centerLongitude, double cutoffRadius) {
        if (!GeoUtils.isLatitudeValid(centerLatitude)) {
            throw new IllegalArgumentException("centerLatitude is not valid: " + centerLatitude);
        }

        if (!GeoUtils.isLongitudeValid(centerLongitude)) {
            throw new IllegalArgumentException("centerLongitude is not valid: " + centerLongitude);
        }

        if (cutoffRadius <= 0) {
            throw new IllegalArgumentException("cutoffRadius is not valid: " + cutoffRadius);
        }

        this.centerLatitude = centerLatitude;
        this.centerLongitude = centerLongitude;
        this.cutoffRadius = cutoffRadius;
    }

    public List<Customer> parseFileAndFilterCustomers(String filePath) {
        Stream<String> unparsedCustomers = openFileStream(filePath);
        Stream<Customer> customerStream = customerParser.parseCustomers(unparsedCustomers);
        List<Customer> filteredCustomers = filterCustomersByDistanceSortById(customerStream)
                .collect(Collectors.toList());
        return filteredCustomers;
    }

    Stream<String> openFileStream(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("filePath is null or empty");
        }

        File inputFile = new File(filePath);
        if (!inputFile.exists() || !inputFile.isFile()) {
            throw new IllegalArgumentException("filePath is not a file: " + filePath);
        }

        Stream<String> linesStream;
        try {
            linesStream = Files.lines(inputFile.toPath());
        } catch (IOException e) {
            throw new RuntimeException("Error opening file: " + inputFile, e);
        }
        return linesStream;
    }

    Stream<Customer> filterCustomersByDistanceSortById(Stream<Customer> customerStream) {
        return customerStream
                .filter(this::isCustomerInRadius)
                .sorted(Comparator.comparing(Customer::getUserId))
                .distinct();
    }

    boolean isCustomerInRadius(Customer customer) {
        double distance = GeoUtils.calculateDistance(
                customer.getLatitude(), customer.getLongitude(),
                centerLatitude, centerLongitude);
        return distance <= cutoffRadius;
    }

    public static void main(String[] args) {
        CustomerGeoFilter customerGeoFilter =
                new CustomerGeoFilter(GeoUtils.OFFICE_LATITUDE, GeoUtils.OFFICE_LONGITUDE, GeoUtils.CUTOFF_RADIUS);

        if (args.length < 1) {
            System.out.println("Please pass file path as an argument.");
            return;
        }
        try {
            List<Customer> customers = customerGeoFilter.parseFileAndFilterCustomers(args[0]);
            for (Customer customer : customers) {
                System.out.println("Customer id: " + customer.getUserId() + ", name: " + customer.getName());
            }
        } catch (Exception e) {
            System.out.println("Error while filtering customers file: " + args[0] + ", error - " + e.getMessage());
        }
    }
}
