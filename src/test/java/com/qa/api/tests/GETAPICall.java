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
import java.util.Map;

public class GETAPICall {
    Playwright playwright;
    APIRequest request;
    APIRequestContext requestContext;
    APIResponse apiResponse;

    @BeforeTest
    public void setup() {
        playwright = Playwright.create();
        request = playwright.request();
        requestContext = request.newContext();
        apiResponse = requestContext.get("https://gorest.co.in/public/v2/users");
    }

    @Test
    public void getUsersAPITest() throws IOException {
        int status = apiResponse.status();
        Assert.assertEquals(status, 200);
        Assert.assertEquals(apiResponse.ok(), true);

        //Show response body json
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonResponse = objectMapper.readTree(apiResponse.body());
//        System.out.println(jsonResponse.toPrettyString());
        System.out.println(apiResponse.text());
//        jsonResponse.toPrettyString();
        System.out.println("------Print response headers---------");
        Map<String, String> headerMap = apiResponse.headers();
        System.out.println(headerMap);
        Assert.assertEquals(headerMap.get("content-type"), "application/json; charset=utf-8");
    }

    @Test
    public void getSpecificUserApiTest() {
        apiResponse = requestContext.get("https://gorest.co.in/public/v2/users", RequestOptions.create()
                .setQueryParam("status", "active")
                .setQueryParam("gender", "male"));
        int statusCode = apiResponse.status();
        Assert.assertEquals(statusCode, 200);
    }

    @AfterTest
    public void TearDown() {
        playwright.close();
    }

}
