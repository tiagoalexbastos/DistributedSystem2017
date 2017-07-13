package DistributedVersion.ComInfo;

import genclass.GenericIO;
import java.io.*;
import java.net.*;

/**
 *   Server side communication sockets.
 */

public class ServerCom
{
    /**
     *   Listening socket
     *    @serialField listeningSocket
     */

    private ServerSocket listeningSocket = null;

    /**
     *  Communication socket
     *    @serialField commSocket
     */

    private Socket commSocket = null;

    /**
     *  Port number for server
     *    @serialField serverPortNumb
     */

    private int serverPortNumb;

    /**
     *  Entry stream for communication
     *    @serialField in
     */

    private ObjectInputStream in = null;

    /**
     *  Exit stream for communication
     *    @serialField out
     */

    private ObjectOutputStream out = null;

    /**
     *  Instantiation of communication channel(form 1)
     *
     *    @param portNumb port number of the server
     */

    public ServerCom (int portNumb)
    {
        serverPortNumb = portNumb;
    }

    /**
     *  Instantiation of communication channel(form 2)
     *
     *    @param portNumb port number of the server
     *    @param lSocket listening socket
     */

    public ServerCom (int portNumb, ServerSocket lSocket)
    {
        serverPortNumb = portNumb;
        listeningSocket = lSocket;
    }

    /**
     *  Instantiation of the socket
     */

    public void start ()
    {
        try
        { listeningSocket = new ServerSocket (serverPortNumb);
        }
        catch (BindException e)
        { GenericIO.writelnString (Thread.currentThread ().getName () +
                " - not possible to associate the port to the socket " +
                serverPortNumb + "!");
            e.printStackTrace ();
            System.exit (1);
        }
        catch (IOException e)
        { GenericIO.writelnString (Thread.currentThread ().getName () +
                " - error in the association" +
                serverPortNumb + "!");
            e.printStackTrace ();
            System.exit (1);
        }
    }

    /**
     *  Closing socket
     */

    public void end ()
    {
        try
        { listeningSocket.close ();
        }
        catch (IOException e)
        { GenericIO.writelnString (Thread.currentThread ().getName () +
                " - Not possible to close the socket!");
            e.printStackTrace ();
            System.exit (1);
        }
    }

    /**
     *  Listening process
     *
     *    @return communication channel
     */

    public ServerCom accept () throws SocketTimeoutException {
        ServerCom scon;

        scon = new ServerCom(serverPortNumb, listeningSocket);
        try
        { scon.commSocket = listeningSocket.accept();
            listeningSocket.setSoTimeout(5000);
        }
        catch (SocketTimeoutException e){
            throw e;
        }
        catch (SocketException e)
        { GenericIO.writelnString (Thread.currentThread ().getName () +
                " - socket closed!");
            e.printStackTrace ();
            System.exit (1);
        }
        catch (IOException e)
        { GenericIO.writelnString (Thread.currentThread ().getName () +
                " - not possible to open the channel!");
            e.printStackTrace ();
            System.exit (1);
        }

        try
        { scon.in = new ObjectInputStream (scon.commSocket.getInputStream ());
        }
        catch (IOException e)
        { GenericIO.writelnString (Thread.currentThread ().getName () +
                " - not possible to open the channel!");
            e.printStackTrace ();
            System.exit (1);
        }

        try
        { scon.out = new ObjectOutputStream (scon.commSocket.getOutputStream ());
        }
        catch (IOException e)
        { GenericIO.writelnString (Thread.currentThread ().getName () +
                " - not possible to open the channel!");
            e.printStackTrace ();
            System.exit (1);
        }

        return scon;
    }

    /**
     *  Closing of the socket
     */

    public void close ()
    {
        try
        { in.close();
        }
        catch (IOException e)
        { GenericIO.writelnString (Thread.currentThread ().getName () +
                " - not possible to close the socket!");
            e.printStackTrace ();
            System.exit (1);
        }

        try
        { out.close();
        }
        catch (IOException e)
        { GenericIO.writelnString (Thread.currentThread ().getName () +
                " - not possible to close the socket!");
            e.printStackTrace ();
            System.exit (1);
        }

        try
        { commSocket.close();
        }
        catch (IOException e)
        { GenericIO.writelnString (Thread.currentThread ().getName () +
                " - not possible to close the socket!");
            e.printStackTrace ();
            System.exit (1);
        }
    }

    /**
     *  Reading object from socket
     *
     *    @return read object
     */

    public Object readObject ()
    {
        Object fromClient = null;

        try
        { fromClient = in.readObject ();
        }
        catch (InvalidClassException e)
        { GenericIO.writelnString (Thread.currentThread ().getName () +
                " - Deserialization not possible!");
            e.printStackTrace ();
            System.exit (1);
        }
        catch (IOException e)
        { GenericIO.writelnString (Thread.currentThread ().getName () +
                " - Error in reading!");
            e.printStackTrace ();
            System.exit (1);
        }
        catch (ClassNotFoundException e)
        { GenericIO.writelnString (Thread.currentThread ().getName () +
                " - Data type from object unknown!");
            e.printStackTrace ();
            System.exit (1);
        }

        return fromClient;
    }

    /**
     *  Writing object to socket
     *
     *    @param toClient object to be written
     */

    public void writeObject (Object toClient)
    {
        try
        { out.writeObject (toClient);
        }
        catch (InvalidClassException e)
        { GenericIO.writelnString (Thread.currentThread ().getName () +
                " - Serialization not possible!");
            e.printStackTrace ();
            System.exit (1);
        }
        catch (NotSerializableException e)
        { GenericIO.writelnString (Thread.currentThread ().getName () +
                " - Data type cannot be serialized!");
            e.printStackTrace ();
            System.exit (1);
        }
        catch (IOException e)
        { GenericIO.writelnString (Thread.currentThread ().getName () +
                " - Error in writing object!");
            e.printStackTrace ();
            System.exit (1);
        }
    }
}
