/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.acme.getting.started;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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
public class TransmitData {
    
   
    public static List<Boolean> sendRequest(Client c, List<Long> data) {

        HttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpPost request = new HttpPost("http://"+c.getIp()+":"+c.getPort()+"/calc");
            StringBuilder json = new StringBuilder();
            json.append("[");
            for(int i=0;i<data.size();i++){
                json.append(data.get(i));
                if(i!=data.size()-1){
                    json.append(',');
                }
            }
            json.append("]");

            request.addHeader("accept", "*/*");
            request.addHeader("content-type", "application/json");
            request.setEntity(new StringEntity(json.toString()));
            
            HttpResponse response = httpClient.execute(request);
            String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            System.out.println("Response body: " + responseBody);
            List<Boolean> result = parseJson(responseBody);
            return result;
        } catch (Exception ex) {
        } finally {
            // @Deprecated httpClient.getConnectionManager().shutdown(); 
        }
        return null;
    }
    
    private static List<Boolean> parseJson(String json){
        String s = json.replaceAll("[", "");
        s = json.replaceAll("]", "");
        String[] bools = s.split(",");
        
        List<Boolean> result = new LinkedList<>();
        for(String b:bools){
            result.add(Boolean.valueOf(b));
        }
        return result;
    }
}
