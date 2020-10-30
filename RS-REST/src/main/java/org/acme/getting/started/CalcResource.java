/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.acme.getting.started;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
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
    public List<List<Long>> calc(List<Long> data) {
        
        //под результаты
        List<Boolean> results = new ArrayList<>();
        
        //разделить данные между активными клиентами
        List<List<Long>> splitData = redirectData(data);   
        
        return splitData;
    }
    
    private List<List<Long>> redirectData(List<Long> data){
        //активные соединения
        Set<Client> activeClients = GreetingResource.getActiveClients().keySet();
        int activeClientsount = activeClients.size();
        
        //splitData
        List<List<Long>> splitData = splitData(activeClientsount, data);
        
        //
        List<Thread> threadPool = new LinkedList<>();
        for(Client c:activeClients){
            //threadPool.add(new Thread(target));
        }
        
        return splitData;
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
