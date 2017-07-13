package DistributedVersion.Monitors.CollectionSite;

import DistributedVersion.ComInfo.ServerCom;
import DistributedVersion.Messages.CollectionSiteMessage;
import DistributedVersion.Messages.CollectionSiteMessageException;
import genclass.GenericIO;

/**
 * Created by pmatos9 on 18/04/17.
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
     * Interface to collection site
     *
     * @serialField escriInter
     */
    private CollectionSiteInterface escriInter;

    /**
     * Instantiation of Interface to collection site
     *
     * @param sconi communication channel
     * @param escriInter interface to collection site
     */
    public ClientProxy(ServerCom sconi, CollectionSiteInterface escriInter) {
        super("Proxy_" + getProxyId());

        this.sconi = sconi;
        this.escriInter = escriInter;
    }

    /**
     * Life Cycle
     */
    @Override
    public void run() {
        CollectionSiteMessage inMessage = null,
                outMessage = null;

        inMessage = (CollectionSiteMessage) sconi.readObject();
        try {
            outMessage = escriInter.processAndReply(inMessage);
        } catch (CollectionSiteMessageException e) {
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
        Class<DistributedVersion.Monitors.CollectionSite.ClientProxy> cl = null;
        int proxyId;

        try {
            cl = (Class<DistributedVersion.Monitors.CollectionSite.ClientProxy>) Class.forName("DistributedVersion.Monitors.CollectionSite.ClientProxy");
        } catch (ClassNotFoundException e) {
            GenericIO.writelnString("Type of data hasn't been found.");
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


