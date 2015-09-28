package io.intercom.screener.geo;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;

public class CustomerParserTest {

    @Test
    public void testParseCustomer() throws Exception {
        CustomerParser customerParser = new CustomerParser();
        Customer customer = customerParser.parseCustomer(
                "{\"latitude\": \"52.986375\", \"user_id\": 12, \"name\": \"Christina McArdle\", \"longitude\": \"-6.043701\"}");
        Customer expectedCustomer = new Customer(12, "Christina McArdle", 52.986375, -6.043701);
        Assert.assertEquals(expectedCustomer, customer);

    }

    @Test
    public void testParseCustomerInvalidInputMissingLongitude() throws Exception {
        CustomerParser customerParser = new CustomerParser();
        Customer customer = customerParser.parseCustomer(
                "{\"latitude\": \"52.986375\", \"user_id\": 12, \"name\": \"Christina McArdle\"}");
        Assert.assertEquals(null, customer);

    }

    @Test
    public void testParseCustomerInvalidInputMissingLatitude() throws Exception {
        CustomerParser customerParser = new CustomerParser();
        Customer customer = customerParser.parseCustomer(
                "{\"user_id\": 12, \"name\": \"Christina McArdle\", \"longitude\": \"-6.043701\"}");
        Assert.assertEquals(null, customer);

    }

    @Test
    public void testParseCustomerInvalidInputMissingUserId() throws Exception {
        CustomerParser customerParser = new CustomerParser();
        Customer customer = customerParser.parseCustomer(
                "{\"latitude\": \"52.986375\", \"name\": \"Christina McArdle\", \"longitude\": \"-6.043701\"}");
        Assert.assertEquals(null, customer);

    }

    @Test
    public void testParseCustomerInvalidInputMissingName() throws Exception {
        CustomerParser customerParser = new CustomerParser();
        Customer customer = customerParser.parseCustomer(
                "{\"latitude\": \"52.986375\", \"user_id\": 12, \"longitude\": \"-6.043701\"}");
        Assert.assertEquals(null, customer);

    }

    @Test
    public void testParseCustomerNullInput() throws Exception {
        CustomerParser customerParser = new CustomerParser();
        Customer customer = customerParser.parseCustomer(null);
        Assert.assertEquals(null, customer);
    }

    @Test
    public void testParseCustomerEmptyInput() throws Exception {
        CustomerParser customerParser = new CustomerParser();
        Customer customer = customerParser.parseCustomer("");
        Assert.assertEquals(null, customer);
    }

    @Test
    public void testParseCustomerEmptyJSON() throws Exception {
        CustomerParser customerParser = new CustomerParser();
        Customer customer = customerParser.parseCustomer("{}");
        Assert.assertEquals(null, customer);
    }

    @Test
    public void testParseCustomerInvalidString() throws Exception {
        CustomerParser customerParser = new CustomerParser();
        Customer customer = customerParser.parseCustomer("dsalklskldksl");
        Assert.assertEquals(null, customer);
    }

    @Test
    public void testParseCustomerInvalidUserID() throws Exception {
        CustomerParser customerParser = new CustomerParser();
        Customer customer = customerParser.parseCustomer(
                "{\"latitude\": \"52.986375\", \"user_id\": -5, \"name\": \"Christina McArdle\", \"longitude\": \"-6.043701\"}");
        Assert.assertEquals(null, customer);

        customer = customerParser.parseCustomer(
                "{\"latitude\": \"52.986375\", \"user_id\": \"string\", \"name\": \"Christina McArdle\", \"longitude\": \"-6.043701\"}");
        Assert.assertEquals(null, customer);

    }

    @Test
    public void testParseCustomerInvalidLatitude() throws Exception {
        CustomerParser customerParser = new CustomerParser();
        Customer customer = customerParser.parseCustomer(
                "{\"latitude\": \"1523565\", \"user_id\": 12, \"name\": \"Christina McArdle\", \"longitude\": \"-6.043701\"}");
        Assert.assertEquals(null, customer);

        customer = customerParser.parseCustomer(
                "{\"latitude\": \"-1523565\", \"user_id\": 12, \"name\": \"Christina McArdle\", \"longitude\": \"-6.043701\"}");
        Assert.assertEquals(null, customer);

        customer = customerParser.parseCustomer(
                "{\"latitude\": \"string\", \"user_id\": 12, \"name\": \"Christina McArdle\", \"longitude\": \"-6.043701\"}");
        Assert.assertEquals(null, customer);

    }

    @Test
    public void testParseCustomerInvalidLongitude() throws Exception {
        CustomerParser customerParser = new CustomerParser();
        Customer customer = customerParser.parseCustomer(
                "{\"latitude\": \"55\", \"user_id\": 12, \"name\": \"Christina McArdle\", \"longitude\": \"12545121332\"}");
        Assert.assertEquals(null, customer);

        customer = customerParser.parseCustomer(
                "{\"latitude\": \"55\", \"user_id\": 12, \"name\": \"Christina McArdle\", \"longitude\": \"-12545121332\"}");
        Assert.assertEquals(null, customer);

        customer = customerParser.parseCustomer(
                "{\"latitude\": \"55\", \"user_id\": 12, \"name\": \"Christina McArdle\", \"longitude\": \"string\"}");
        Assert.assertEquals(null, customer);

    }

    @Test
    public void testParseCustomers() {
        CustomerParser customerParser = new CustomerParser();
        List<String> unparsedCustomers = Arrays.asList(
                "{\"latitude\": \"52.966\", \"user_id\": 15, \"name\": \"Michael Ahearn\", \"longitude\": \"-6.463\"}",
                "{\"latitude\": \"54.180238\", \"user_id\": 17, \"name\": \"Patricia Cahill\", \"longitude\": \"-5.920898\"}",
                "{\"latitude\": \"54.080556\", \"user_id\": 23, \"name\": \"Eoin Gallagher\", \"longitude\": \"-6.361944\"}",
                "{\"latitude\": \"54.133333\", \"user_id\": 24, \"name\": \"Rose Enright\", \"longitude\": \"-6.433333\"}",
                "{\"latitude\": \"53.038056\", \"user_id\": 26, \"name\": \"Stephen McArdle\", \"longitude\": \"-7.653889\"}");
        List<Customer> customers = customerParser.parseCustomers(unparsedCustomers.stream()).collect(Collectors.toList());
        List<Customer> expectedCustomers = Arrays.asList(
                new Customer(15, "Michael Ahearn", 52.966, -6.463),
                new Customer(17, "Patricia Cahill", 54.180238, -5.920898),
                new Customer(23, "Eoin Gallagher", 54.080556, -6.361944),
                new Customer(24, "Rose Enright", 54.133333, -6.433333),
                new Customer(26, "Stephen McArdle", 53.038056, -7.653889));
        Assert.assertEquals(expectedCustomers, customers);
    }

    @Test
    public void testParseCustomersNull() {
        CustomerParser customerParser = new CustomerParser();
        Stream<Customer> customerStream = customerParser.parseCustomers(null);
        Assert.assertEquals(0, customerStream.count());
    }
}
