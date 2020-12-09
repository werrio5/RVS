/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.acme.getting.started;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author pavel
 */

@Path("/calc")
public class CalcResource {
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<Boolean> calc(List<Long> data) {
        
        //под результаты
        List<Boolean> results = new ArrayList<>();
        
        //разделить данные между активными клиентами
        List<Boolean> splitData = redirectData(data);  
        
        return splitData;
    }
    
    private List<Boolean> redirectData(List<Long> data){
        
        List<Long> notSentData = data;
        boolean allDataSent = false;
        List<Boolean> results = new LinkedList<>();
        
        //while(!allDataSent){
            //активные соединения
            Map<Client,Long> map = GreetingResource.getActiveClients();
            Set<Client> activeClients = new HashSet<>((Set<Client>)map.keySet());
            Client[] clients = new Client[activeClients.size()];
            for(int i=0;i<activeClients.size();i++){
                clients[i]=(Client) activeClients.toArray()[i];
            }
            
            int activeClientsount = activeClients.size();
            if(activeClientsount == 0) return results;
            //int activeClientsount = 2;

            //splitData
            List<List<Long>> splitData = splitData(activeClientsount, notSentData);
            for(int i=0;i<splitData.size();i++){
                for(int j=0;j<splitData.get(i).size();j++){
                    System.out.print(splitData.get(i).get(j));
                }
                System.out.println("");
            }

            //dataStorage
            DataStorage dataStorage = new DataStorage(activeClients.size(), data.size());

            sendAll(clients,splitData,dataStorage);
            while(!dataStorage.dataOk()){
            try {
                //dataStorage.print();
                System.out.println("data ok="+dataStorage.dataOk());
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(CalcResource.class.getName()).log(Level.SEVERE, null, ex);
            }            
            }
            results = dataStorage.getData();
            //start threads
//            List<CalcThread> threadPool = new LinkedList<>();
//            for(int i=0; i<activeClientsount; i++){
//                System.out.println("thread "+i);
//                threadPool.add(new CalcThread(clients[i].getIp(), clients[i].getPort(), splitData.get(i), dataStorage));
//                threadPool.get(i).run();
//                System.out.println(i+" started");
//            }
//            System.out.println("exit thread starting");
//
//
//            //ожидание завершения потоков
//            while(true){
//                //wait
//                System.out.println("waiting for all threads to stop");
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(CalcResource.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                
//                boolean allThreadsDown = true;
//                for(CalcThread thread:threadPool){
//                    if(!thread.isThreadStoped()){
//                        System.out.println("thread"+threadPool.indexOf(thread)+" is running");
//                        allThreadsDown = false;
//                    }
//                }
//                
//                if(allThreadsDown) {
//                    System.out.println("all threads stopped");
//                    break;
//                }
//            }
//            
//            System.out.println("data check");
//            if(dataStorage.dataOk()) {
//                System.out.println("data ok");
//                allDataSent = true;
//                results = dataStorage.getData();
//            }
//            else{
//                notSentData = dataStorage.getUnsentData();
//            }
       // }
        
        return results;
    }
    
    private List<List<Long>> splitData(int activeClientsount, List<Long> data){
        
        List<List<Long>> splitData = new LinkedList<>();
        for(int i=0; i< Math.ceil((double)data.size() / (double)activeClientsount); i++){
            for(int j=0; j<activeClientsount; j++){
                if(i==0) splitData.add(new LinkedList<>());
                if(i * activeClientsount + j <data.size() ){
                    splitData.get(j).add(data.get(i * activeClientsount + j));
                }
            }
        }
        return splitData;
    }
    
    
    private void sendAll(Client[] clients, List<List<Long>> data, DataStorage dataStorage){
        CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
        httpclient.start();
        for(int i=0;i<clients.length;i++){
            String url = "http://" + clients[i].getIp() + ":" + clients[i].getPort()+ "/calc";
            HttpPost request = new HttpPost(url);
            attachHeaders(request, data.get(i));
            Future<HttpResponse> future = httpclient.execute(request, new AsyncResponse(i,dataStorage){
                @Override
                public void completed(HttpResponse t) {

                        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                        System.out.println("completed");
                        System.out.println(t.getEntity().toString());
                        try {
                            String responseBody = EntityUtils.toString(t.getEntity(), StandardCharsets.UTF_8);
                            System.out.println(responseBody);                            
                            List<Boolean> results = getDataFromJson(responseBody);
                            System.out.println("i="+i);
                            dataStorage.storeData(results, i);
                    } catch (IOException | ParseException ex) {
                        Logger.getLogger(CalcResource.class.getName()).log(Level.SEVERE, null, ex);
                    }
                        
                }

                @Override
                public void failed(Exception excptn) {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    System.out.println("failed");
                }

                @Override
                public void cancelled() {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    System.out.println("cancelled");
                }                
            });
        }
    }
    
    private void attachHeaders(HttpPost request, List<Long> data){
        request.addHeader("accept", "*/*");
        request.addHeader("content-type", "application/json");
        StringBuilder json = new StringBuilder();
        json.append(data);
        try {
            request.setEntity(new StringEntity(json.toString()));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(CalcResource.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        System.out.println("json="+json);
        String result = json.substring(1, json.length()-1);
        System.out.println(result);
        return result;
    }
}
