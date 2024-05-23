package com.qa.api.tests.GET;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.HttpHeader;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class APIResponseHeaderTest {
    Playwright playwright;
    APIRequest request;
    APIRequestContext requestContext;


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
    public void getHeadersTest() {
        APIResponse apiResponse = requestContext.get("https://gorest.co.in/public/v2/users");
        int statusCode = apiResponse.status();
        System.out.println("Status code: " + statusCode);
        Assert.assertEquals(statusCode, 200);

        //using map
        Map<String, String> headersMap = apiResponse.headers();
        headersMap.forEach((k,v) -> System.out.println(k + " : " + v));
        System.out.println("total headers response: " + headersMap.size());
        Assert.assertEquals(headersMap.get("server"), "cloudflare");

        //using list
        List<HttpHeader> headerList = apiResponse.headersArray();
        for (HttpHeader e: headerList) {
            System.out.println(e.name + " : " + e.value);
        }
    }
}