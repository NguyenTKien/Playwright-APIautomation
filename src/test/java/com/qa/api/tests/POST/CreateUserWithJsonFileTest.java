package com.qa.api.tests.POST;

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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class CreateUserWithJsonFileTest {
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

    @Test
    public void createUserWithJsonFileTest() throws IOException {
        //get json file
        byte[] fileBytes = null;
        File jsonFile = new File("./src/test/data/user.json");
        fileBytes = Files.readAllBytes(jsonFile.toPath());

        //POST call: create user
        APIResponse apiPostResponse = requestContext.post("https://gorest.co.in/public/v2/users",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setHeader("Authorization", BearerToken)
                        .setData(fileBytes));

        System.out.println(apiPostResponse.status());
        Assert.assertEquals(apiPostResponse.status(), 201);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonResponseBody = objectMapper.readTree(apiPostResponse.body());
        System.out.println(jsonResponseBody.toPrettyString());

    }
}
