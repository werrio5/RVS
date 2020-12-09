/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.acme.getting.started;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author werrio5
 */
public class DataStorage {
    
  
    private Boolean[] results;
    private int activeClients;

    DataStorage(int activeClients, int dataLength) {
        this.activeClients = activeClients;
        results = new Boolean[dataLength];
        for(Boolean b:results){
            b=null;
        }
    }
    
    public void storeData(List<Boolean> res, int clientId){
        for(int i=0;i<res.size();i++){
            results[i*activeClients + clientId] = res.get(i);
        }
    }
    
//    private int getClientIndex(CalcThread thread){
//        Client[] clients = new Client[activeClients.size()];
//        for(int i=0;i<activeClients.size();i++){
//            clients[i]=(Client) activeClients.toArray()[i];
//        }
//            
//        for(int i=0;i<activeClients.size();i++){
//            if(clients[i].getIp().equals(thread.getIp()) & clients[i].getPort() == thread.getPort()){
//                return i;
//            }
//        }
//        return -1;
//    }
    
    public boolean dataOk(){
        for(Boolean result:results){
            if(result==null) return false;
        }
        return true;
    }
    
    public List<Boolean> getData(){
        List<Boolean> data = new LinkedList<>();
        for(Boolean b:results){
            data.add(b);
        }
        return data;
    }
    
    public void print(){
        for(Boolean b:results){
            System.out.println(b);
        }
    }
    
//    public List<Long> getUnsentData(){
//        List<Long> allData = new LinkedList<>();
//        for(List<Long> data:splitData){
//            allData.addAll(data);
//        }
//        
//        List<Long> unsentData = new LinkedList<>();
//        for(int i=0;i<allData.size();i++){
//            if(results[i]==null){
//                unsentData.add(allData.get(i));
//            }
//        }
//        
//        return unsentData;
//    }
}
