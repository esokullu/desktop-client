/////////////////////////////////////////////////////////////////////
//TeenyWeb.java -- a brain-dead web server written in Java
//This is a severely limited web server; it can only handle requests for
//HTML documents (no GIFS, no CGIs, etc -- though that could be built
//in later on). The purpose of this is to give you a very simple example
//of how a web server works.
/////////////////////////////////////////////////////////////////////

package mygroup;


import java.io.*;
import java.net.*;
import java.util.*;

public class TeenyWeb extends Thread
{

	ServerSocket listen_socket;
        String httpRootDir;

        public TeenyWeb(int servPort, String httpRoot) {
            //set instance variables from constructor args
            //int servPort = Integer.parseInt(port);
            httpRootDir = httpRoot;
            try{
                        //create new ServerSocket
                        listen_socket = new ServerSocket (servPort);

		}
		catch(IOException e) {System.err.println(e);}
            
        }


        
	public void run()
	{
		try{
			while(true)
			{
                                //listen for a request. When a request comes in,
                                //accept it, then create a Connection object to
                                //service the request and go back to listening on
                                //the port.

				Socket client_socket = listen_socket.accept();
                                System.out.println("connection request received");
                                Connection c = new Connection (client_socket, httpRootDir);
                                
			}
		}
		catch(IOException e) {System.err.println(e);}
	}

        
}

//The Connection class -- this is where HTTP requests are serviced

class Connection extends Thread
{
	protected Socket client;
	protected DataInputStream in;
	protected BufferedOutputStream out;
        String httpRootDir;
        String requestedFile;

        public Connection (Socket client_socket, String httpRoot)
	{
                //set instance variables from args
                httpRootDir = httpRoot;
		client = client_socket;

                //create input and output streams for conversation with client

		try{
			in = new DataInputStream(client.getInputStream());
			out = new BufferedOutputStream (client.getOutputStream());
		}
		catch(IOException e)
		{
		 	System.err.println(e);
			try {client.close();}
			catch (IOException e2) {};
			return;
		}
                //start this object's thread -- the rest of the action
                //takes place in the run() method, which is called by
                //the thread.
		this.start();

                

	}

        public void run()
	{
                String line = null;     //read buffer
                String req = null;      //first line of request
                //OutputStream os;

		try{
                                //read HTTP request -- the request comes in
                                //on the first line, and is of the form:
                                //      GET <filename> HTTP/1.x

                                req = in.readLine();

                                //loop through and discard rest of request
                                line = req;
                                while (line.length() > 0)
                                {
                                line = in.readLine();
                                }

                                //parse request -- get filename
                                StringTokenizer st = new StringTokenizer(req);
                                //discard first token ("GET")
                                st.nextToken();
                                requestedFile = st.nextToken();

                                /*if(requestedFile.equals("empty")) {
                                    // hack
                                    // something that's never gonna happen
                                    // purpose: not to get java.io.FileNotFoundException
                                    // also make sure there are no dirs
                                    requestedFile = "dljsndslnfasdlfnasd3fnasdf";
                                }*/


                                System.out.println("requested file is "+requestedFile);

                                if(requestedFile.equals("/crossdomain.xml")) {
                                    System.out.println("evet");
                                    sendResponseHeader("text/xml");
                                    String crossdomain = "<?xml version=\"1.0\"?>\n<!DOCTYPE cross-domain-policy SYSTEM \"http://www.macromedia.com/xml/dtds/cross-domain-policy.dtd\">\n<cross-domain-policy>\n\t<site-control permitted-cross-domain-policies=\"master-only\"/>\t<allow-access-from domain=\"*\" secure=\"false\" />\n</cross-domain-policy>";
                                    this.sendString(crossdomain);
                                    return;
                                }

                                //read in file

                                //create File object
                                File f = new File(httpRootDir + requestedFile);


                                //send response
                                sendResponseHeader("video/x-flv");
                                //sendResponseHeader("text/plain");

                                //check to see if file exists
                                if (!f.canRead()||f.isDirectory()) {
                                    // check if torrent exists
                                    // if it does, download it and start the bittorrent
                                    // in the meanwhile, display "loading"
                                    
                                    System.err.println("in can't read");

                                    p2p downloadvideo = new p2p(System.getProperty("java.io.tmpdir")+"/funvideo.torrent");
                                    downloadvideo.start();

                                    try {
                                            synchronized(this) {
                                    boolean videodownloaded = false;

                                    while(!videodownloaded) {
                                        
                                                //wait();
                                                sleep(1000);
                                                videodownloaded = downloadvideo.isvideodownloaded();
                                    }
                                            }
                                        } catch(Exception e) {}
                                        
                                    
                                    

                                    System.err.println("out of loop");

                                    f = new File(System.getProperty("java.io.tmpdir")+"/deneme.flv");

                                    if (!f.canRead()) {
                                        System.err.println("can't read the downloaded file");
                                        sendString("");
                                        return;
                                    }

                                }

                                //read in file
                                BufferedInputStream fis = new BufferedInputStream (new FileInputStream(f));

                                byte[] buffer = new byte[4096];
                                int bytesRead;
                                while ((bytesRead = fis.read(buffer)) != -1) {
                                    out.write(buffer, 0, bytesRead);
                                }
                                fis.close();




		}
                catch (IOException e) {System.out.println(e);}
		finally
		{
			try {client.close();}
			catch (IOException e) {};

		}


	}

        //send a HTTP header to the client
        //The first line is a status message from the server to the client.
        //The second line holds the mime type of the document
        void sendResponseHeader(String type)
        {
                String res ="HTTP/1.0 200 OK" + "\n" + "Content-type: " +type+ "\n\n";
                InputStream resb = new ByteArrayInputStream(res.getBytes());
                byte[] buffer = new byte[4096];
                int bytesRead;
                try {
                    while ((bytesRead = resb.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                } catch (IOException e) {System.out.println(e);}
        }

        //write a string to the client.
        void sendString(String str)
        {
                InputStream resb = new ByteArrayInputStream(str.getBytes());
                byte[] buffer = new byte[4096];
                int bytesRead;
                try {
                    while ((bytesRead = resb.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                } catch (IOException e) {System.out.println(e);}
        }


}