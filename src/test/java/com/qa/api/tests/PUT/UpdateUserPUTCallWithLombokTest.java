package com.qa.api.tests.PUT;

import com.api.data.Users_Lombok;
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

public class UpdateUserPUTCallWithLombokTest {
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
    public void tearDown () {
        playwright.close();
    }

    public static String getRandomEmail() {
        String email = "testingAutomation" + System.currentTimeMillis() + "@gmail.com";
        return email;
    }

    @Test
    public void updateUserPUTCallWithLombokTest() throws IOException {
        Users_Lombok users = Users_Lombok.builder()
                .name("kien trung")
                .email(getRandomEmail())
                .gender("male")
                .status("inactive").build();

        //Create User Post Call
        APIResponse apiResponse = requestContext.post("https://gorest.co.in/public/v2/users",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setHeader("Authorization", BearerToken)
                        .setData(users));

        System.out.println(apiResponse.text());
        Assert.assertEquals(apiResponse.status(), 201);

        //Convert response text/json to POJO - deserialization
        ObjectMapper objectMapper = new ObjectMapper();
        Users_Lombok responseUser = objectMapper.readValue(apiResponse.body(), Users_Lombok.class);
        Assert.assertEquals(users.getEmail(), responseUser.getEmail());

        String userID = responseUser.getId();
        users.setStatus("active");
        users.setName("UpdateName");

        //Update User Information PUT Call
        APIResponse apiPUTCall = requestContext.put("https://gorest.co.in/public/v2/users/" + userID,
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setHeader("Authorization", BearerToken)
                        .setData(users));

        System.out.println(apiPUTCall.text());
        Assert.assertEquals(apiPUTCall.status(), 200);

        //Get user Call
        APIResponse apiGetCall = requestContext.get("https://gorest.co.in/public/v2/users/" + userID,
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setHeader("Authorization", BearerToken));

        System.out.println(apiGetCall.text());

        Users_Lombok getUser = objectMapper.readValue(apiGetCall.body(), Users_Lombok.class);
        Assert.assertEquals(apiGetCall.status(), 200);

        Assert.assertEquals(getUser.getStatus(), "active");
        Assert.assertEquals(getUser.getName(), "UpdateName");
    }

}