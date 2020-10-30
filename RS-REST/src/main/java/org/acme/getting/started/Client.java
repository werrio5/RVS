/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.acme.getting.started;

import static java.lang.System.currentTimeMillis;

/**
 *
 * @author pavel
 */
public class Client {
    
    private String ip;
    private int port;
    
    public Client(){
    }
    
    public Client(String ip, int port){
        this.port = port;
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    
}
