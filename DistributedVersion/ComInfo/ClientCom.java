package DistributedVersion.ComInfo;

import genclass.GenericIO;
import java.io.*;
import java.net.*;

/**
 *   This type of data implements communication channel based on sockets over TCP.
 */

public class ClientCom
{
    /**
     *  Communication socket
     *    @serialField commSocket
     */

    private Socket commSocket = null;

    /**
     *      Machine of the server
     *    @serialField serverHostName
     */

    private String serverHostName = null;

    /**
     *  Port listening
     *    @serialField serverPortNumb
     */

    private int serverPortNumb;

    /**
     *  Entry Stream
     *    @serialField in
     */

    private ObjectInputStream in = null;

    /**
     *  Exit Stream
     *    @serialField out
     */

    private ObjectOutputStream out = null;

    /**
     *  Instantiation of communication channel
     *
     *    @param hostName name where the server is located
     *    @param portNumb port number
     */

    public ClientCom (String hostName, int portNumb)
    {
        serverHostName = hostName;
        serverPortNumb = portNumb;
    }

    /**
     *  Opening socket communication
     *
     *    @return true, if channel has been open
     *            false, other case
     */

    public boolean open ()
    {
        boolean success = true;
        SocketAddress serverAddress = new InetSocketAddress (serverHostName, serverPortNumb);

        try
        { commSocket = new Socket();
            commSocket.connect (serverAddress);
        }
        catch (UnknownHostException e)
        { GenericIO.writelnString (Thread.currentThread ().getName () +
                " - name of the machine is not known: " +
                serverHostName + "!");
            e.printStackTrace ();
            System.exit (1);
        }
        catch (NoRouteToHostException e)
        { GenericIO.writelnString (Thread.currentThread ().getName () +
                " - the machine cannot be reached: " +
                serverHostName + "!");
            e.printStackTrace ();
            System.exit (1);
        }
        catch (ConnectException e)
        { GenericIO.writelnString (Thread.currentThread ().getName () +
                " - server does not respond in: " + serverHostName + "." + serverPortNumb + "!");
            if (e.getMessage ().equals ("Connection refused"))
                success = false;
            else { GenericIO.writelnString (e.getMessage () + "!");
                e.printStackTrace ();
                System.exit (1);
            }
        }
        catch (SocketTimeoutException e)
        { GenericIO.writelnString (Thread.currentThread ().getName () +
                " - Timeout on: " +
                serverHostName + "." + serverPortNumb + "!");
            success = false;
        }
        catch (IOException e)                           // erro fatal --- outras causas
        { GenericIO.writelnString (Thread.currentThread ().getName () +
                " - Unexpected error: " +
                serverHostName + "." + serverPortNumb + "!");
            e.printStackTrace ();
            System.exit (1);
        }

        if (!success) return (success);

        try
        { out = new ObjectOutputStream (commSocket.getOutputStream ());
        }
        catch (IOException e)
        { GenericIO.writelnString (Thread.currentThread ().getName () +
                " - not possible to open the exit way!");
            e.printStackTrace ();
            System.exit (1);
        }

        try
        { in = new ObjectInputStream (commSocket.getInputStream ());
        }
        catch (IOException e)
        { GenericIO.writelnString (Thread.currentThread ().getName () +
                " - not possible to open the entry way!");
            e.printStackTrace ();
            System.exit (1);
        }

        return (success);
    }

    /**
     *  Closing communication socket
     *  Closing streams.
     *  Closing socket
     */

    public void close ()
    {
        try
        { in.close();
        }
        catch (IOException e)
        { GenericIO.writelnString (Thread.currentThread ().getName () +
                " - not possible to close the channel!");
            e.printStackTrace ();
            System.exit (1);
        }

        try
        { out.close();
        }
        catch (IOException e)
        { GenericIO.writelnString (Thread.currentThread ().getName () +
                " - not possible to close the channel!");
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
     *  Reading object
     *
     *    @return objecto lido
     */

    public Object readObject ()
    {
        Object fromServer = null;                            // objecto

        try
        { fromServer = in.readObject ();
        }
        catch (InvalidClassException e)
        { GenericIO.writelnString (Thread.currentThread ().getName () +
                " - Deserialization not possible!");
            e.printStackTrace ();
            System.exit (1);
        }
        catch (IOException e)
        { GenericIO.writelnString (Thread.currentThread ().getName () +
                " - Error in reading from socket!");
            e.printStackTrace ();
            System.exit (1);
        }
        catch (ClassNotFoundException e)
        { GenericIO.writelnString (Thread.currentThread ().getName () +
                " - Type of object unknown!");
            e.printStackTrace ();
            System.exit (1);
        }

        return fromServer;
    }

    /**
     *  Writing object
     *
     *    @param toServer objecto a ser escrito
     */

    public void writeObject (Object toServer)
    {
        try
        { out.writeObject (toServer);
        }
        catch (InvalidClassException e)
        { GenericIO.writelnString (Thread.currentThread ().getName () +
                " - Object cannot be serializable!");
            e.printStackTrace ();
            System.exit (1);
        }
        catch (NotSerializableException e)
        { GenericIO.writelnString (Thread.currentThread ().getName () +
                " - Data type not serializable!");
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
