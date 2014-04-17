import java.net.*; 
import java.io.*; 

// A client for our multithreaded EchoServer. 
public class EchoClient 
{ 
    String serveur;
    int port;
    Socket s = null; 
    BufferedReader in = null; 
    PrintWriter out = null; 
	
	
	/*public static void main(String[] args) 
    { 
        // First parameter has to be machine name 
        if(args.length == 0) 
        { 
            System.out.println("Usage : EchoClient <serverName>"); 
            return; 
        } 
     */    
    public EchoClient()
    {
    	port=80;
    	serveur="127.0.0.1";
    }
    
    public void connect()
    {
        // Create the socket connection to the EchoServer. 
        try 
        { 
            s = new Socket(serveur, port); 
            // Create the streams to send and receive information 
            in = new BufferedReader(new InputStreamReader(s.getInputStream())); 
            out = new PrintWriter(new OutputStreamWriter(s.getOutputStream())); 
        }         
        catch(UnknownHostException uhe) 
        { 
            // Host unreachable 
            System.out.println("Unknown Host :" + serveur); 
            s = null; 
        } 
        catch(IOException ioe) 
        { 
            // Cannot connect to port on given host 
            System.out.println("Cant connect to server at " + port + ". Make sure it is running."); 
            s = null; 
        } 
         
        if(s == null) 
            System.exit(-1); 
         
    }
    
    public void close(){
    	try {
    		// Close the streams 
            out.close(); 
            in.close(); 
			s.close();
			System.exit(-1);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public String send(String trame) {       
        
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
            System.out.println("Exception during communication. Server probably closed connection."); 
        }
		return reponse;         
    } 
} 
