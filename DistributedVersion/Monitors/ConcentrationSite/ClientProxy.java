package DistributedVersion.Monitors.ConcentrationSite;

import DistributedVersion.ComInfo.ServerCom;
import DistributedVersion.Messages.ConcentrationSiteMessage;
import DistributedVersion.Messages.ConcentrationSiteMessageException;
import genclass.GenericIO;

/**
 * Created by pmatos9 on 18/04/17.
 */
public class ClientProxy extends Thread {

    /**
     * Number of threads
     *
     * @serialField nProxy
     */
    private static int nProxy;
    /**
     * Communication channel
     *
     * @serialField sconi
     */
    private ServerCom sconi;
    /**
     * Interface of CollectionSite
     *
     * @serialField concentrationInterf
     */
    private ConcentrationSiteInterface concentrationInterf;

    /**
     * Instantiation of interface
     * @param sconi communication channel
     * @param concentrationInterf interface concentration site
     */
    public ClientProxy(ServerCom sconi, ConcentrationSiteInterface concentrationInterf) {
        super("Proxy_" + getProxyId());

        this.sconi = sconi;
        this.concentrationInterf = concentrationInterf;
    }

    /**
     * Life cycle
     */
    @Override
    public void run() {
        ConcentrationSiteMessage inMessage = null,
                outMessage = null;

        inMessage = (ConcentrationSiteMessage) sconi.readObject();
        try {
            outMessage = concentrationInterf.processAndReply(inMessage);
        } catch (ConcentrationSiteMessageException e) {
            GenericIO.writelnString("Thread " + getName() + ": " + e.getMessage() + "!");
            GenericIO.writelnString(e.getMessageVal().toString());
            System.exit(1);
        }
        sconi.writeObject(outMessage);
        sconi.close();
    }

    /**
     * Generator of instantiation
     *
     * @return id of instantiation
     */
    private static int getProxyId() {
        Class<DistributedVersion.Monitors.ConcentrationSite.ClientProxy> cl = null;
        //   virtual de Java
        int proxyId;

        try {
            cl = (Class<DistributedVersion.Monitors.ConcentrationSite.ClientProxy>) Class.forName("DistributedVersion.Monitors.ConcentrationSite.ClientProxy");
        } catch (ClassNotFoundException e) {
            GenericIO.writelnString("Data type of ClientProxy not found!");
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

