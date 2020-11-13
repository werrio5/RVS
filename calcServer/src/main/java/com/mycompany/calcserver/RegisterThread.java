/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.calcserver;

import io.quarkus.scheduler.Scheduled;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 *
 * @author pavel
 */
@ApplicationScoped
public class RegisterThread{

    @Scheduled(every="1s")
    public void register() {
        System.out.println("register thread started");
        while (true) {
            try {
                HttpClient httpClient = HttpClientBuilder.create().build();
                HttpPost request = new HttpPost("http://" + "192.168.1.13:8080" + "/register");
                StringBuilder json = new StringBuilder();
                json.append("{");
                json.append("\"ip\":\"" + "192.168.1.4" + "\",");
                json.append("\"port\":" + 8080);
                json.append("}");

                request.addHeader("accept", "*/*");
                request.addHeader("content-type", "application/json");
                request.setEntity(new StringEntity(json.toString()));

                HttpResponse response = httpClient.execute(request);
//        String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
//        System.out.println("Response body: " + responseBody);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(RegisterThread.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(RegisterThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
