package edu.cooper.ece465;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Worker {
    public static void main(String[] args) {
        try(Socket s = new Socket("localhost", 420)){
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            Gson g = new Gson();

            boolean initDone = false;
            String initData;

            while(!initDone) {
                if ((initData = br.readLine()) != null) {

                    initDone = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
