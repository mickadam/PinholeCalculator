import java.net.*; 
import java.io.*; 

public class Echoserver extends Thread
{         
    ServerSocket m_ServerSocket; 
    int port;
	public boolean askEnd; 
    
    public Echoserver()  
    {
    port=80;
    askEnd=false;
    }
    
    public void run(){
    	try 
        { 
            // Create the server socket. 
            m_ServerSocket = new ServerSocket(port); 
        } 
        catch(IOException ioe) 
        { 
            System.out.println("Could not create server socket at "+port+". Quitting."); 
            System.exit(-1); 
        } 
         
        System.out.println("Listening for clients on "+port+"..."); 
         
        // Successfully created Server Socket. Now wait for connections. 
        int id = 0; 
        while(true) 
        {                         
            try 
            { 
                // Accept incoming connections. 
                Socket clientSocket = m_ServerSocket.accept(); 
                if(askEnd) break;
                // accept() will block until a client connects to the server. 
                // If execution reaches this point, then it means that a client 
                // socket has been accepted. 
                 
                // For each client, we will start a service thread to 
                // service the client requests. This is to demonstrate a 
                // multithreaded server, although not required for such a 
                // trivial application. Starting a thread also lets our 
                // EchoServer accept multiple connections simultaneously. 
               
                // Start a service thread 

                ClientServiceThread cliThread = new ClientServiceThread(clientSocket, id++); 
                cliThread.start(); 
            } 
            catch(IOException ioe) 
            { 
                System.out.println("Exception encountered on accept. Ignoring. Stack Trace :"); 
                ioe.printStackTrace(); 
            } 
        } 
    } 
     
     
    class ClientServiceThread extends Thread 
    { 
        Socket m_clientSocket;         
        int m_clientID = -1; 
        boolean m_bRunThread = true; 
         
        ClientServiceThread(Socket s, int clientID) 
        { 
            m_clientSocket = s; 
            m_clientID = clientID; 
        } 
 
        public void run() 
        {             
            // Obtain the input stream and the output stream for the socket 
            // A good practice is to encapsulate them with a BufferedReader 
            // and a PrintWriter as shown below. 
            BufferedReader in = null;  
            PrintWriter out = null; 
             
            // Print out details of this connection 
            System.out.println("Accepted Client : ID - " + m_clientID + " : Address - " +  
                             m_clientSocket.getInetAddress().getHostName()); 
                 
            try 
            {                                 
                in = new BufferedReader(new InputStreamReader(m_clientSocket.getInputStream())); 
                out = new PrintWriter(new OutputStreamWriter(m_clientSocket.getOutputStream())); 
                 
                // At this point, we can read for input and reply with appropriate output. 
                 
                // Run in a loop until m_bRunThread is set to false 
                while(m_bRunThread) 
                {                     
                    // read incoming stream 
                    String clientCommand = "" + in.readLine(); 
                 
                    if(!clientCommand.equals("null")){
                    	
	                     System.out.println("Client Says :" + clientCommand);  
	                    if(clientCommand.equalsIgnoreCase("quit")) 
	                    { 
	                        // Special command. Quit this thread 
	                        m_bRunThread = false;    
	                        System.out.print("Stopping client thread for client : " + m_clientID); 
	                    } 
	                    else 
	                    { 
	                        // Echo it back to the client. 
	                        out.println("OK "+clientCommand); 
	                        out.flush(); 
	                    } 
                    }
                } 
            } 
            catch(Exception e) 
            { 
                e.printStackTrace(); 
            } 
            finally 
            { 
                // Clean up 
                try 
                {                     
                    in.close(); 
                    out.close(); 
                    m_clientSocket.close(); 
                    System.out.println("...Stopped"); 
                } 
                catch(IOException ioe) 
                { 
                    ioe.printStackTrace(); 
                } 
            } 
        } 
    } 
} 