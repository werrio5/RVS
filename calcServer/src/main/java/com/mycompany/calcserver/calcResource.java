package com.mycompany.calcserver;

import java.util.LinkedList;
import java.util.List;
import javax.ws.rs.Produces;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;


@Path("/calc")
public class calcResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<Boolean> calc(List<Long> data) {
        List<Boolean> result = MainClass.localCalc(data);
        return result;
    }
    
}