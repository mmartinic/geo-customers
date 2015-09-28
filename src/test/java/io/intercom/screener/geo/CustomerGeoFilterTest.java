package io.intercom.screener.geo;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;

public class CustomerGeoFilterTest {

    @Test
    public void testParseFileAndFilterCustomersValidFile() {
        CustomerGeoFilter customerGeoFilter =
                new CustomerGeoFilter(GeoUtils.OFFICE_LATITUDE, GeoUtils.OFFICE_LONGITUDE, GeoUtils.CUTOFF_RADIUS);

        List<Customer> customers = customerGeoFilter.parseFileAndFilterCustomers("./target/test-classes/validInput.txt");
        Assert.assertEquals(Arrays.asList(4, 5, 6, 8, 11, 12, 13, 15, 17, 23, 24, 26, 29, 30, 31, 39),
                customers.stream().map(Customer::getUserId).collect(
                        Collectors.toList()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFileAndFilterCustomersNoFile() {
        CustomerGeoFilter customerGeoFilter =
                new CustomerGeoFilter(GeoUtils.OFFICE_LATITUDE, GeoUtils.OFFICE_LONGITUDE, GeoUtils.CUTOFF_RADIUS);

        customerGeoFilter.parseFileAndFilterCustomers("nofile");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFileAndFilterCustomerNullInput() {
        CustomerGeoFilter customerGeoFilter =
                new CustomerGeoFilter(GeoUtils.OFFICE_LATITUDE, GeoUtils.OFFICE_LONGITUDE, GeoUtils.CUTOFF_RADIUS);

        customerGeoFilter.parseFileAndFilterCustomers(null);
    }

    @Test
    public void testParseFileAndFilterCustomerEmptyFile() {
        CustomerGeoFilter customerGeoFilter =
                new CustomerGeoFilter(GeoUtils.OFFICE_LATITUDE, GeoUtils.OFFICE_LONGITUDE, GeoUtils.CUTOFF_RADIUS);

        List<Customer> customers = customerGeoFilter.parseFileAndFilterCustomers("./target/test-classes/empty.txt");
        Assert.assertEquals(Collections.emptyList(), customers);
    }

    @Test
    public void testParseFileAndFilterCustomerInputWithDuplicates() {
        CustomerGeoFilter customerGeoFilter =
                new CustomerGeoFilter(GeoUtils.OFFICE_LATITUDE, GeoUtils.OFFICE_LONGITUDE, GeoUtils.CUTOFF_RADIUS);

        List<Customer> customers = customerGeoFilter.parseFileAndFilterCustomers("./target/test-classes/duplicates.txt");
        Assert.assertEquals(Arrays.asList(new Customer(12, "Christina McArdle", 52.986375, -6.043701),
                new Customer(23, "Eoin Gallagher", 54.080556, -6.361944)), customers);
    }

    @Test
    public void testParseFileAndFilterCustomersFileWithInvalidValues() {
        CustomerGeoFilter customerGeoFilter =
                new CustomerGeoFilter(GeoUtils.OFFICE_LATITUDE, GeoUtils.OFFICE_LONGITUDE, GeoUtils.CUTOFF_RADIUS);

        List<Customer> customers = customerGeoFilter.parseFileAndFilterCustomers("./target/test-classes/invalidValues.txt");
        List<Customer> expectedCustomers = Arrays.asList(
                new Customer(15, "Michael Ahearn", 52.966, -6.463),
                new Customer(17, "Patricia Cahill", 54.180238, -5.920898),
                new Customer(23, "Eoin Gallagher", 54.080556, -6.361944),
                new Customer(24, "Rose Enright", 54.133333, -6.433333),
                new Customer(26, "Stephen McArdle", 53.038056, -7.653889));
        Assert.assertEquals(expectedCustomers, customers);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFileAndFilterCustomerInvalidCenterLatitude() {
        new CustomerGeoFilter(123456, GeoUtils.OFFICE_LONGITUDE, GeoUtils.CUTOFF_RADIUS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFileAndFilterCustomerInvalidCenterLongitude() {
        new CustomerGeoFilter(GeoUtils.OFFICE_LATITUDE, 123456, GeoUtils.CUTOFF_RADIUS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFileAndFilterCustomerInvalidCutoffRadius() {
        new CustomerGeoFilter(GeoUtils.OFFICE_LATITUDE, GeoUtils.OFFICE_LONGITUDE, -12);
    }

    @Test
    public void testIsCustomerInRadiusTrue() {
        CustomerGeoFilter customerGeoFilter =
                new CustomerGeoFilter(GeoUtils.OFFICE_LATITUDE, GeoUtils.OFFICE_LONGITUDE, GeoUtils.CUTOFF_RADIUS);

        Customer customer = new Customer(1, "test", 52.966, -6.463);
        Assert.assertEquals(true, customerGeoFilter.isCustomerInRadius(customer));
    }

    @Test
    public void testIsCustomerInRadiusFalse() {
        CustomerGeoFilter customerGeoFilter =
                new CustomerGeoFilter(GeoUtils.OFFICE_LATITUDE, GeoUtils.OFFICE_LONGITUDE, GeoUtils.CUTOFF_RADIUS);

        Customer customer = new Customer(1, "test", 57.966, -6.463);
        Assert.assertEquals(false, customerGeoFilter.isCustomerInRadius(customer));
    }

    @Test
    public void testOpenFileStream() {
        CustomerGeoFilter customerGeoFilter =
                new CustomerGeoFilter(GeoUtils.OFFICE_LATITUDE, GeoUtils.OFFICE_LONGITUDE, GeoUtils.CUTOFF_RADIUS);

        Stream<String> linesStream = customerGeoFilter.openFileStream("./target/test-classes/duplicates.txt");
        Assert.assertEquals(4, linesStream.count());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpenFileStreamNoFile() {
        CustomerGeoFilter customerGeoFilter =
                new CustomerGeoFilter(GeoUtils.OFFICE_LATITUDE, GeoUtils.OFFICE_LONGITUDE, GeoUtils.CUTOFF_RADIUS);

        customerGeoFilter.openFileStream("nofile");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpenFileStreamNullInput() {
        CustomerGeoFilter customerGeoFilter =
                new CustomerGeoFilter(GeoUtils.OFFICE_LATITUDE, GeoUtils.OFFICE_LONGITUDE, GeoUtils.CUTOFF_RADIUS);

        customerGeoFilter.openFileStream(null);
    }

    @Test
    public void testFilterCustomersByDistanceSortById() {
        CustomerGeoFilter customerGeoFilter =
                new CustomerGeoFilter(GeoUtils.OFFICE_LATITUDE, GeoUtils.OFFICE_LONGITUDE, GeoUtils.CUTOFF_RADIUS);

        List<Customer> unfilteredCustomers = Arrays.asList(
                new Customer(17, "Patricia Cahill", 54.180238, -5.920898),
                new Customer(15, "Michael Ahearn", 52.966, -6.463),
                new Customer(178, "test", 74.180238, -5.920898),
                new Customer(26, "Stephen McArdle", 53.038056, -7.653889),
                new Customer(24, "Rose Enright", 54.133333, -6.433333),
                new Customer(23, "Eoin Gallagher", 54.080556, -6.361944),
                new Customer(175, "test", 0., 1.)
        );

        List<Customer> expectedCustomers = Arrays.asList(
                new Customer(15, "Michael Ahearn", 52.966, -6.463),
                new Customer(17, "Patricia Cahill", 54.180238, -5.920898),
                new Customer(23, "Eoin Gallagher", 54.080556, -6.361944),
                new Customer(24, "Rose Enright", 54.133333, -6.433333),
                new Customer(26, "Stephen McArdle", 53.038056, -7.653889));
        
        List<Customer> customers =
                customerGeoFilter.filterCustomersByDistanceSortById(unfilteredCustomers.stream()).collect(Collectors.toList());

        Assert.assertEquals(expectedCustomers, customers);
    }
}
