package com.madam.AndroidEchoClient;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.StrictMode;
import android.widget.Toast;


// An Android client for our multithreaded EchoServer. 
public class AndroidEchoClient
{ 
    String IP;
    int port;
    Socket socket = null; 
    BufferedReader in = null; 
    PrintWriter out = null; 
    AndroidEchoClientActivity AECA;
	

	@SuppressLint("NewApi")
	public AndroidEchoClient(AndroidEchoClientActivity androidEchoClientActivity) 
    {
    	AECA = androidEchoClientActivity;
   
    	//fix problème compatilité
    	if (android.os.Build.VERSION.SDK_INT > 9) {
    		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    		StrictMode.setThreadPolicy(policy);
    	}
	}

	public Boolean connect(String sIP, String sPort){
          
        // Create the socket connection to the EchoServer. 
        try
        { 
            //connection au socket du server
        	socket = new Socket();
            socket.connect(new InetSocketAddress(sIP, new Integer(sPort)), 1000); //pour pouvoir mettre un timeOut à la connection
            
            // Create the streams to send and receive information 
            in = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream())); 
            Toast.makeText(AECA, "Connected to"+sIP, Toast.LENGTH_LONG).show();
        }         
        catch(UnknownHostException uhe) 
        { 
            // Host unreachable 
        	Toast.makeText(AECA, "Unknown Host :" + sIP, Toast.LENGTH_LONG).show();
            return false;
        } 
        catch(IOException ioe) 
        { 
            // Cannot connect to port on given host 
            Toast.makeText(AECA, "Cant connect to server at " + sIP + ":" + sPort + ". Make sure it is running."+ioe.getMessage(), Toast.LENGTH_LONG).show();
            return false;
        } 
         
		return true; 
         
    }
    
    public void close()
    {
    	try 
    	{
    		// Close the streams 
            out.close(); 
            in.close(); 
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public String send(String trame)
    {
        String reponse=null;
        try 
        { 
            // Since this is the client, we will initiate the talking. 
            // Send a string. 
            out.println(trame); 
            out.flush(); 
            // receive the reply. 
            
            reponse=in.readLine();
            System.out.println("Server Says : " + reponse);
        } 
        catch(IOException ioe) 
        { 
//            System.out.println("Exception during communication. Server probably closed connection."); 
            Toast.makeText(AECA, "Exception during communication. Server probably closed connection.", Toast.LENGTH_LONG).show();
            
        }
		return reponse;         
    } 
} 
