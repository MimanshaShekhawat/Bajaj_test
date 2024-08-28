package com.example;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ApiTests {

    @Test
    public void testValidUserCreation() {
        Response response = ApiTestUtils.createUser("1", "Aarav", "Sharma", 9876543210L, "aarav.sharma@example.com");
        assertThat(response.getStatusCode(), equalTo(201)); // Assume 201 Created is returned
    }

    @Test
    public void testDuplicatePhoneNumber() {
        ApiTestUtils.createUser("1", "Aarav", "Sharma", 9876543210L, "aarav.sharma@example.com");
        Response response = ApiTestUtils.createUser("2", "Vihaan", "Verma", 9876543210L, "vihaan.verma@example.com");
        assertThat(response.getStatusCode(), equalTo(400)); // Expecting 400 Bad Request for duplicate phone number
    }

    @Test
    public void testDuplicateEmailId() {
        ApiTestUtils.createUser("1", "Aarav", "Sharma", 9876543210L, "aarav.sharma@example.com");
        Response response = ApiTestUtils.createUser("2", "Vihaan", "Verma", 1234567890L, "aarav.sharma@example.com");
        assertThat(response.getStatusCode(), equalTo(400)); // Expecting 400 Bad Request for duplicate email
    }

    @Test
    public void testMissingRollNumber() {
        Response response = RestAssured.given()
            .header("Content-Type", "application/json")
            .body("{\"firstName\":\"Aarav\",\"lastName\":\"Sharma\",\"phoneNumber\":9876543210,\"emailId\":\"aarav.sharma@example.com\"}")
            .post(ApiTestUtils.BASE_URL);
        assertThat(response.getStatusCode(), equalTo(401)); // Expecting 401 Unauthorized due to missing roll-number
    }

    @Test
    public void testMissingRequiredFields() {
        Response response = ApiTestUtils.createUser("1", null, "Sharma", 9876543210L, "aarav.sharma@example.com");
        assertThat(response.getStatusCode(), equalTo(400)); // Expecting 400 Bad Request due to missing required fields
    }
}
