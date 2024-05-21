package com.qa.api.tests;

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
import java.util.HashMap;
import java.util.Map;

public class CreateUserPostCallTest {
    Playwright playwright;
    APIRequest apiRequest;
    APIRequestContext requestContext;
    String BearerToken = "Bearer 6d4236fba5f7eef8e054b55e59d28c104a8fc77e516ef80c86a039465522dcd5";

    @BeforeTest
    public void setup() {
        playwright = Playwright.create();
        apiRequest = playwright.request();
        requestContext = apiRequest.newContext();
    }

    @AfterTest
    public void tearDown() {
        playwright.close();
    }

    public static String getRandomEmail() {
        String emailId = "testautomation" + System.currentTimeMillis() + "@gmail.com";
        System.out.println(emailId);
        return emailId;
    }

    @Test
    public void createUserTest() throws IOException {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("name", "KienNguyen");
        data.put("email", getRandomEmail());
        data.put("gender", "male");
        data.put("status", "active");

        //Post Create User
        APIResponse postAPIResponse = requestContext.post("https://gorest.co.in/public/v2/users",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setHeader("Authorization", BearerToken)
                        .setData(data));

        Assert.assertEquals(postAPIResponse.status(), 201);
        Assert.assertEquals(postAPIResponse.statusText(), "Created");

        System.out.println(postAPIResponse.text());
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode postJsonResponse = objectMapper.readTree(postAPIResponse.body());
        System.out.println(postJsonResponse.toPrettyString());

        //Get call: Fetch the same user by id
        String userID = postJsonResponse.get("id").asText();
                APIResponse getAPIResponse = apiRequest.newContext().get("https://gorest.co.in/public/v2/users/" +userID,
                RequestOptions.create()
                        .setHeader("Authorization", BearerToken));
        System.out.println(getAPIResponse.text());
        Assert.assertEquals(getAPIResponse.status(), 200);
        Assert.assertTrue(getAPIResponse.text().contains(userID));
        Assert.assertTrue(getAPIResponse.text().contains("KienNguyen"));
    }
}
