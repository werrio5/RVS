/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.acme.getting.started;

import java.util.HashSet;
import java.util.Set;
import javax.enterprise.context.ApplicationScoped;
import io.quarkus.scheduler.Scheduled;
import java.util.Map;
/**
 *
 * @author pavel
 */
@ApplicationScoped
public class Deactivation{

    @Scheduled(every="1s")
    public void run() {
            
            Map<Client,Long> activeClients = GreetingResource.getActiveClients();
            Set<Client> disconnectedClients = new HashSet<>();

            for (Client client : activeClients.keySet()) {
                long diff = System.currentTimeMillis() - activeClients.get(client);
                if (diff > 5000) {
                    disconnectedClients.add(client);
                }
            }
            for (Client client : disconnectedClients) {
                activeClients.remove(client);
            }
            System.out.println("clients = "+activeClients.size());
            System.out.println("clients disconencted = "+disconnectedClients.size());
        }    

}
