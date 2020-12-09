/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.acme.getting.started;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.security.acl.Owner;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.nio.IOControl;
import org.apache.http.nio.client.methods.AsyncCharConsumer;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import org.apache.http.nio.protocol.HttpAsyncRequestProducer;
import org.apache.http.protocol.HttpContext;
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
//        try {
//            //boolean dataSent = 
//                    sendData();
////            if(dataSent){
////                dataStorage.storeData(this);
////            }
//        } catch (IOException ex) {
//            Logger.getLogger(CalcThread.class.getName()).log(Level.SEVERE, null, ex);
//        }
        
    }

//    private boolean sendData() throws IOException {
//        boolean resultReceived = false;
//        
//            CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
//            httpclient.start();
//        try {
//            System.out.println("sending data to "+ip+":"+port);
//            //HttpClient httpClient = HttpClientBuilder.create().build();
//            final CountDownLatch latch2 = new CountDownLatch(1);
//            
//            
//            HttpPost request = new HttpPost("http://" + ip + ":" + String.valueOf(port) + "/calc");
//            StringBuilder json = new StringBuilder();
////            json.append("[");
////            for (Long value : data) {
////                json.append(value);
////                if (data.indexOf(value) != data.size() - 1) {
////                    json.append(",");
////                }
////            }
////            json.append("]");
//            json.append(data);
//            System.out.println(json.toString());
//
//            request.addHeader("accept", "*/*");
//            request.addHeader("content-type", "application/json");
//            request.setEntity(new StringEntity(json.toString()));
//
//            HttpAsyncRequestProducer producer3 = HttpAsyncMethods.create(request);
//            AsyncCharConsumer<HttpResponse> consumer3 = new AsyncCharConsumer<HttpResponse>() {
//
//        HttpResponse response;
//
//        @Override
//        protected void onResponseReceived(final HttpResponse response) {
//            this.response = response;
//        }
//
//        @Override
//        protected void onCharReceived(final CharBuffer buf, final IOControl ioctrl) throws IOException {
//            // Do something useful
//        }
//
//        @Override
//        protected void releaseResources() {
//        }
//
//        @Override
//        protected HttpResponse buildResult(final HttpContext context) {
//            return this.response;
//        }
//
//    };
//    httpclient.execute(producer3, consumer3, new AsyncResponse(this) {
//  
//            
//        @Override
//        public void completed(final HttpResponse response3) {
//            
//                System.out.println("123test");
//            try {
//                latch2.countDown();
//                System.out.println(request.getRequestLine() + "->" + response3.getStatusLine());
//                String responseBody = EntityUtils.toString(response3.getEntity(), StandardCharsets.UTF_8);
//                System.out.println(responseBody);
//
//                List<Boolean> results = getDataFromJson(responseBody);
//                boolean resultReceived = checkReceivedData(results,data.size());
//                if(resultReceived){
//                    receivedData = results;
//                }
//                dataStorage.storeData(owner);
//                threadStoped = true;
//                httpclient.close();
//                
//            } catch (IOException | ParseException ex) {
//                Logger.getLogger(CalcThread.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//
//        @Override
//        public void failed(final Exception ex) {
//            latch2.countDown();
//            System.out.println(request.getRequestLine() + "->" + ex);
//        }
//
//        @Override
//        public void cancelled() {
//            latch2.countDown();
//            System.out.println(request.getRequestLine() + " cancelled");
//        }
//
//    });
//
////            try {
////                latch2.await();
//                
////            HttpResponse response = httpClient.execute(request);
////            String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
//            
////            System.out.println(responseBody);
////            
////            List<Boolean> results = getDataFromJson(responseBody);
////            resultReceived = checkReceivedData(results,data.size());
////            if(resultReceived){
////                receivedData = results;
////            }
////            } catch (InterruptedException ex) {
////                Logger.getLogger(CalcThread.class.getName()).log(Level.SEVERE, null, ex);
////            }
//
//            
//        } catch (IOException e) {
//        }
//        finally{
//        }
//        
//        return resultReceived;
//    }
    
    
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
        System.out.println("json="+json);
        String result = json.substring(1, json.length()-1);
        System.out.println(result);
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
