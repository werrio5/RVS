/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.calcserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author pavel
 */
public class MainClass {
        
    //args[0] - server ip:port
    //args[1] this pc ip
//public static void main(String[] args) throws Throwable {
//        register(args);
//        ServerSocket ss = new ServerSocket(8080);
//        while (true) {
//            Socket s = ss.accept();
//            System.err.println("Client accepted");
//            new Thread(new SocketProcessor(s)).start();
//        }
//    }
//
//private static void register(String[] args) throws IOException{
//        new Thread(new RegisterThread(args[0], args[1], Integer.valueOf(args[2]))).start();
//}

public static List<Boolean> localCalc(List<Long> nums) {
        List<Boolean> result = new LinkedList<>();
        System.err.println("вычисление на этом компьютере");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        for (long num : nums) {
            boolean v = calc(num);
            System.out.println(num+" - "+v);
            result.add(v);
        }
        Timestamp timestamp2 = new Timestamp(System.currentTimeMillis());
        long diff = timestamp2.getTime() - timestamp.getTime();
        System.err.println("время выполнение (мс) = " + diff);
        
        return result;
    }

    private static boolean calc(long num) {
        return isPrime(num);
    }

    private static boolean isPrime(long num) {
        if (num % 2 == 0) {
            return false;
        }

        boolean isPrime = true;
        for (int i = 3; i < num; i += 2) {
            if (num % i == 0) {
                isPrime = false;
                break;
            }
        }
        return isPrime;
    }

    private static class SocketProcessor implements Runnable {

        private Socket s;
        private InputStream is;
        private OutputStream os;
        private List<String> jsonData;

        private SocketProcessor(Socket s) throws Throwable {
            this.s = s;
            this.is = s.getInputStream();
            this.os = s.getOutputStream();
        }

        public void run() {
            try {
                //receive data
                readInputHeaders();      
                
                //get values
                List<Long> data = getDataFromJson();
                
                //calc
                List<Boolean> result = localCalc(data);
                
                
                writeResponse(result);
            } catch (Throwable t) {
                /*do nothing*/
            } finally {
                try {
                    s.close();
                } catch (Throwable t) {
                    /*do nothing*/
                }
            }
            System.err.println("Client processing finished");
        }
        
        private List<Long> getDataFromJson(){
            List<Long> data = new LinkedList<>();
            for(String s:jsonData){
                
            }
            
            return data;
        }

        private void writeResponse(List<Boolean> data) throws Throwable {
            String response = "HTTP/1.1 200 OK\r\n"
                    + "Server: server\r\n"
                    + "Content-Type: text/html\r\n"
                    + "Content-Length: " + 0 + "\r\n"
                    + "Connection: close\r\n\r\n";
            String result = response;
            os.write(result.getBytes());
            os.flush();
        }

        private void readInputHeaders() throws Throwable {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            while (true) {
                String s = br.readLine();
                System.out.println(s);
                if (s.length() == 0 | s.trim().length() == 0) {
                    readContent(br);                    
                }
            }
        }
        
        private void readContent(BufferedReader br) throws IOException{
            jsonData = new LinkedList<>();
             while (true) {
                String s = br.readLine();
                jsonData.add(s);
                System.out.println(s);
                if (s.length() == 0 | s.trim().length() == 0) {
                    break;
                }
            }
        }
    }
}