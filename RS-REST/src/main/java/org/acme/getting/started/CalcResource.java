/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.acme.getting.started;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
            DataStorage dataStorage = new DataStorage(splitData, activeClients, data.size());

            //start threads
            List<CalcThread> threadPool = new LinkedList<>();
            for(int i=0; i<activeClientsount; i++){
                System.out.println("thread "+i);
                threadPool.add(new CalcThread(clients[i].getIp(), clients[i].getPort(), splitData.get(i), dataStorage));
                threadPool.get(i).run();
            }
            System.out.println("exit threads");
//              //send data
//              for(int i=0; i<activeClientsount; i++){
//                  List<Boolean> res = TransmitData.sendRequest(clients[i], splitData.get(i));
//              }

//            //ожидание завершения потоков
//            while(true){
//                //wait
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(CalcResource.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                
//                boolean allThreadsDown = true;
//                for(CalcThread thread:threadPool){
//                    if(!thread.isThreadStoped()){
//                        allThreadsDown = false;
//                        break;
//                    }
//                }
//                
//                if(allThreadsDown) {
//                    System.out.println("all threads stopped");
//                    break;
//                }
//                else{
//                    System.out.println("error");
//                }
//            }
            
            if(dataStorage.dataOk()) {
                allDataSent = true;
                results = dataStorage.getData();
            }
            else{
                notSentData = dataStorage.getUnsentData();
            }
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
    
}
