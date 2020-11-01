/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.acme.getting.started;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class CalcThread implements Runnable {
    
    private final String ip;
    private final int port;
    private final List<Long> data;
    private List<Boolean> receivedData;
    private final DataStorage dataStorage;
    private boolean threadStoped;

    public CalcThread(String ip, int port, List<Long> data, DataStorage dataStorage) {
        this.ip = ip;
        this.port = port;
        this.data = data;
        this.dataStorage = dataStorage;
        threadStoped = false;
    }

    @Override
    public void run() {
        try {
            boolean dataSent = sendData();
            if(dataSent){
                dataStorage.storeData(this);
            }
        } catch (IOException ex) {
            Logger.getLogger(CalcThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    private boolean sendData() throws IOException {
        boolean resultReceived = false;
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost request = new HttpPost("http://" + ip + ":" + String.valueOf(port) + "/calc");
            StringBuilder json = new StringBuilder();
            json.append("{[");
            for (Long value : data) {
                json.append(data);
                if (data.indexOf(value) != data.size() - 1) {
                    json.append(",");
                }
            }
            json.append("]}");

            request.addHeader("accept", "*/*");
            request.addHeader("content-type", "application/json");
            request.setEntity(new StringEntity(json.toString()));

            HttpResponse response = httpClient.execute(request);
            String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            
            List<Boolean> results = getDataFromJson(responseBody);
            resultReceived = checkReceivedData(results,data.size());
            if(resultReceived){
                receivedData = results;
            }
            
        } catch (IOException e) {
        }
        finally{
            threadStoped = true;
        }
        
        return resultReceived;
    }
    
    
    private List<Boolean> getDataFromJson(String json){
        List<Boolean> data = new LinkedList<>();
        String removedBrackets = removeBrackets(json);
        String[] values = removedBrackets.split(",");
        for(String s:values){
            data.add(Boolean.valueOf(s));
        }

        return data;
    }
    
    private String removeBrackets(String json){
        String result = json.replaceAll("{", "");
        result = result.replaceAll("}", "");
        result = result.replaceAll("[", "");
        result = result.replaceAll("]", "");
        return result;
    }
    
    private boolean checkReceivedData(List<Boolean> data, int targetLength){
        boolean dataOk = true;
        
        if(data.size() != targetLength){
            return false;
        }
        
        for(Boolean b:data){
            if(b == null){
                dataOk = false;
                break;
            }
        }
        
        return dataOk;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public List<Boolean> getReceivedData() {
        return receivedData;
    }

    public boolean isThreadStoped() {
        return threadStoped;
    }
    
    

}
