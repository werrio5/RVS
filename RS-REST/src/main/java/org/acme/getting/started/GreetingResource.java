package org.acme.getting.started;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/register")
public class GreetingResource {

    //private static Map<Client,Long> clients = Collections.synchronizedMap(new HashMap<Client,Long>());    
    private static Map<Client,Long> clients = new HashMap<Client,Long>();    

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(Client client) {
        if(isNewClient(client))        
            clients.put(client,System.currentTimeMillis()); 
        else{
            resetTime(client);
        }
        return Response.ok().build();
    }
    
    private boolean isNewClient(Client client){
        for(Client c:clients.keySet()){
            if(c.getIp().equals(client.getIp()) & c.getPort() == client.getPort()){
                return false;
            }
        }
        return true;
    } 
    
    private void resetTime(Client client){
        for(Client c:clients.keySet()){
            if(c.getIp().equals(client.getIp()) & c.getPort() == client.getPort()){
                clients.put(c,System.currentTimeMillis()); 
            }
        }
    }

    
    @Path("/list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<Client,Long> list(){
        return clients;
    }
    
    public static Map<Client,Long> getActiveClients(){
        return clients;
    }
    
}