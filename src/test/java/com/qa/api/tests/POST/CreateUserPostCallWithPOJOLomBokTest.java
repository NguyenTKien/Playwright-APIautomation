package com.qa.api.tests.POST;

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

public class CreateUserPostCallWithPOJOLomBokTest {
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
        String email = "testingEmail" + System.currentTimeMillis() + "@gmail.com";
        return email;
    }

    @Test
    public void createUserPostCallWithPOJOLombokTest() throws IOException {
        //Create user by lombok
        Users_Lombok users = Users_Lombok.builder()
                .name("kienguyentrung")
                .email(getRandomEmail())
                .gender("male")
                .status("inactive").build();

        //Create user post call
        APIResponse apiResponse = requestContext.post("https://gorest.co.in/public/v2/users",
                RequestOptions.create()
                        .setHeader("Authorization", BearerToken)
                        .setHeader("Content-Type", "application/json")
                        .setData(users));

        System.out.println(apiResponse.text());
        Assert.assertEquals(apiResponse.status(), 201);

        //Convert response text/json to POJO - deserialization
        ObjectMapper objectMapper = new ObjectMapper();
        Users_Lombok actualUser = objectMapper.readValue(apiResponse.body(), Users_Lombok.class);

        System.out.println(actualUser.toString());
        Assert.assertEquals(users.getName(), actualUser.getName());
        Assert.assertEquals(users.getEmail(), actualUser.getEmail());
        Assert.assertEquals(users.getGender(), actualUser.getGender());
        Assert.assertEquals(users.getStatus(), actualUser.getStatus());
        Assert.assertNotNull(actualUser.getId());
    }

}
