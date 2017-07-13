package DistributedVersion.Monitors.Museum;

import DistributedVersion.ComInfo.ServerCom;
import DistributedVersion.Messages.MuseumMessage;
import DistributedVersion.Messages.MuseumMessageException;
import genclass.GenericIO;

/**
 * Created by pmatos9 on 17/04/17.
 */
public class ClientProxy extends Thread {

    /**
     *
     * Thread Counter
     *
     * @serialField nProxy
     */
    private static int nProxy;
    /**
     * Comunication channel
     * @serialField sconi
     */
    private ServerCom sconi;
    /**
     * Interface to Museum
     *
     * @serialField museuInter
     */
    private MuseumInterface museuInter;

    /**
     * Instantiation of Interface to museum
     *
     * @param sconi communication channel
     * @param museumInter interface to museum
     */
    public ClientProxy(ServerCom sconi, MuseumInterface museumInter) {
        super("Proxy_" + getProxyId());

        this.sconi = sconi;
        this.museuInter = museumInter;
    }

    /**
     * Life cycle
     */
    @Override
    public void run() {
        MuseumMessage inMessage = null,
                outMessage = null;

        inMessage = (MuseumMessage) sconi.readObject();
        try {
            outMessage = museuInter.processAndReply(inMessage);
        } catch (MuseumMessageException e) {
            GenericIO.writelnString("Thread " + getName() + ": " + e.getMessage() + "!");
            GenericIO.writelnString(e.getMessageVal().toString());
            System.exit(1);
        }
        sconi.writeObject(outMessage);
        sconi.close();
    }

    /**
     *
     * Generation of Id of instantiation
     *
     * @return Id of instantiation
     */
    private static int getProxyId() {
        Class<DistributedVersion.Monitors.Museum.ClientProxy> cl = null;
        //   virtual de Java
        int proxyId;

        try {
            cl = (Class<DistributedVersion.Monitors.Museum.ClientProxy>) Class.forName("DistributedVersion.Monitors.Museum.ClientProxy");
        } catch (ClassNotFoundException e) {
            GenericIO.writelnString("Data type hasn't been found!");
            e.printStackTrace();
            System.exit(1);
        }

        synchronized (cl) {
            proxyId = nProxy;
            nProxy += 1;
        }

        return proxyId;
    }
}

