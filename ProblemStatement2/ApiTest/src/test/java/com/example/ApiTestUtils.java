package com.example;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class ApiTestUtils {

    static final String BASE_URL = "https://bfhldevapigw.healthrx.co.in/automation-campus/create/user";

    public static Response createUser(String rollNumber, String firstName, String lastName, long phoneNumber, String emailId) {
        return RestAssured.given()
            .header("roll-number", rollNumber)
            .header("Content-Type", "application/json")
            .body(String.format("{\"firstName\":\"%s\",\"lastName\":\"%s\",\"phoneNumber\":%d,\"emailId\":\"%s\"}", firstName, lastName, phoneNumber, emailId))
            .post(BASE_URL);
    }
}
