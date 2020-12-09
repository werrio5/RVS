/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.acme.getting.started;

import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;

/**
 *
 * @author pavel
 */
public abstract class AsyncResponse implements FutureCallback<HttpResponse>{


    final int i;
    final DataStorage dataStorage;
    
    public AsyncResponse(int i,DataStorage dataStorage){
        this.i = i;
        this.dataStorage=dataStorage;
    }
    
}
