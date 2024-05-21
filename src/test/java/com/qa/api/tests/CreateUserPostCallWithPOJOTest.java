package com.qa.api.tests;

import com.api.data.Users;
import com.beust.ah.A;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;

public class CreateUserPostCallWithPOJOTest {
    Playwright playwright;
    APIRequest request;
    APIRequestContext requestContext;
    String BearerToken = "Bearer 6d4236fba5f7eef8e054b55e59d28c104a8fc77e516ef80c86a039465522dcd5";

    @BeforeTest
    public void setup() {
        playwright = Playwright.create();
        request = playwright.request();
        requestContext = request.newContext();
    }

    @AfterTest
    public void tearDown() {
        playwright.close();
    }

    public static String getRandomEmail() {
        String email = "testingAuto" + System.currentTimeMillis() + "@gmail.com";
        return email;
    }

    @Test
    public void createUserPostCallWithPOJOTest() throws IOException {
        Users user = new Users("kiennge", getRandomEmail(), "male", "active");

        APIResponse apiResponse = requestContext.post("https://gorest.co.in/public/v2/users",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setHeader("Authorization", BearerToken)
                        .setData(user));

        System.out.println(apiResponse.status());
        Assert.assertEquals(apiResponse.status(), 201);
        System.out.println(apiResponse.text());

        //Convert response text/json to POJO -- deserialization
        ObjectMapper objectMapper = new ObjectMapper();
        Users actualUser = objectMapper.readValue(apiResponse.body(), Users.class);
        System.out.println("----------Actual user response----------");
        System.out.println(actualUser.toString());

        Assert.assertEquals(user.getName(), actualUser.getName());
        Assert.assertEquals(user.getEmail(), actualUser.getEmail());
        Assert.assertEquals(user.getGender(), actualUser.getGender());
        Assert.assertEquals(user.getStatus(), actualUser.getStatus());
        Assert.assertNotNull(actualUser.getId());
    }
}
