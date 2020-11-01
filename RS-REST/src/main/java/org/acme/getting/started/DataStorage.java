/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.acme.getting.started;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author werrio5
 */
public class DataStorage {
    
    private final List<List<Long>> splitData;
    private final Set<Client> activeClients;
    
    private Boolean[] results;

    DataStorage(List<List<Long>> splitData, Set<Client> activeClients, int dataLength) {
        this.splitData = splitData;
        this.activeClients = activeClients;
        results = new Boolean[dataLength];
        for(Boolean b:results){
            b=null;
        }
    }
    
    public void storeData(CalcThread thread){
        int clientIndex = getClientIndex(thread);
        int startPos = 0;
        for(int i=0;i<clientIndex;i++){
            startPos+=splitData.get(i).size();
        }
        List<Boolean> receivedData = thread.getReceivedData();
        int dataLength = receivedData.size();
        for(int i=startPos; i<startPos+dataLength; i++){
            results[i] = receivedData.get(i);
        }
    }
    
    private int getClientIndex(CalcThread thread){
        Client[] clients = (Client[]) activeClients.toArray();
        for(int i=0;i<activeClients.size();i++){
            if(clients[i].getIp().equals(thread.getIp()) & clients[i].getPort() == thread.getPort()){
                return i;
            }
        }
        return -1;
    }
    
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
    
    public List<Long> getUnsentData(){
        List<Long> allData = new LinkedList<>();
        for(List<Long> data:splitData){
            allData.addAll(data);
        }
        
        List<Long> unsentData = new LinkedList<>();
        for(int i=0;i<allData.size();i++){
            if(results[i]==null){
                unsentData.add(allData.get(i));
            }
        }
        
        return unsentData;
    }
}
