/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.testapp;

import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author pavel
 */
public class MainClass {

    private static final long[] nums = {1130713291, 764586887, 870675287, 4, 820778753, // НЕ простые - 4, 8, 123456789
        263316799, 438152887, 861690899, 8, 123456789};

    public static void main(String[] args) {

        //local
        //localCalc();

        //request
        calcRequest();
    }


    private static void localCalc() {
        System.err.println("вычисление на этом компьютере");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        for (long num : nums) {
            calc(num);
        }
        Timestamp timestamp2 = new Timestamp(System.currentTimeMillis());
        long diff = timestamp2.getTime() - timestamp.getTime();
        System.err.println("время выполнение (мс) = " + diff);
    }

    private static void calc(long num) {
        System.out.println(isPrime(num));
    }

    private static boolean isPrime(long num) {
        if (num % 2 == 0) {
            return false;
        }

        boolean isPrime = true;
        for (int i = 3; i < num; i += 2) {
            if (num % i == 0) {
                isPrime = false;
                break;
            }
        }
        return isPrime;
    }

    private static void calcRequest() {
        System.err.println("запрос вычисления на удаленных компьютерах");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        sendRequest();
        Timestamp timestamp2 = new Timestamp(System.currentTimeMillis());
        long diff = timestamp2.getTime() - timestamp.getTime();
        System.err.println("время выполнение (мс) = " + diff);
    }

    private static void sendRequest() {
        Map<Integer, Long> arguments = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            arguments.put(i, nums[i]);
        }
        HttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpPost request = new HttpPost("http://192.168.1.7:8080/register");
            StringBuilder json = new StringBuilder();
            json.append("{");
            json.append("\"ip\":\"ipaddr\",");
            json.append("\"port\":123");
            json.append("}");

            request.addHeader("accept", "*/*");
            request.addHeader("content-type", "application/json");
            request.setEntity(new StringEntity(json.toString()));
            
            HttpResponse response = httpClient.execute(request);
            String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            System.out.println("Response body: " + responseBody);
        } catch (Exception ex) {
        } finally {
            // @Deprecated httpClient.getConnectionManager().shutdown(); 
        }
    }
}
