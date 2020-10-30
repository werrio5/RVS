/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.acme.getting.started;

import java.util.List;

/**
 *
 * @author pavel
 */
public class CalcThread implements Runnable{
    
    private final String ip;
    private final int port;
    private final List<Long> data;

    public CalcThread(String ip, int port, List<Long> data) {
        this.ip = ip;
        this.port = port;
        this.data = data;
    }

    @Override
    public void run() {

    }
    
    private void sendData(){
         try {
                HttpClient httpClient = HttpClientBuilder.create().build();
                HttpPost request = new HttpPost("http://" + serverIp + "/register");
                StringBuilder json = new StringBuilder();
                json.append("{");
                json.append("\"ip\":\"" + ip + "\",");
                json.append("\"port\":" + port);
                json.append("}");

                request.addHeader("accept", "*/*");
                request.addHeader("content-type", "application/json");
                request.setEntity(new StringEntity(json.toString()));

                HttpResponse response = httpClient.execute(request);
//        String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
//        System.out.println("Response body: " + responseBody);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(RegisterThread.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(RegisterThread.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
}
