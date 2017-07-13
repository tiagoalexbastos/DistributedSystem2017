package DistributedVersion.Monitors.AssaultParty;

import DistributedVersion.ComInfo.ServerCom;
import DistributedVersion.Messages.AssaultPartyMessage;
import DistributedVersion.Messages.AssaultPartyMessageException;
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
     * Interface to assault party
     *
     * @serialField grupoInter
     */
    private AssaultPartyInterface grupoInter;

    /**
     * Instantiation of Interface to assault party
     * @param sconi communication channel
     * @param grupoInter interface to collection site
     */
    public ClientProxy(ServerCom sconi, AssaultPartyInterface grupoInter) {
        super("Proxy_" + getProxyId());

        this.sconi = sconi;
        this.grupoInter = grupoInter;
    }

    /**
     * Life cycle
     */
    @Override
    public void run() {
        AssaultPartyMessage inMessage = null,
                outMessage = null;


        inMessage = (AssaultPartyMessage) sconi.readObject();
        try {
            outMessage = grupoInter.processAndReply(inMessage);
        } catch (AssaultPartyMessageException e) {
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
        Class<DistributedVersion.Monitors.AssaultParty.ClientProxy> cl = null;

        int proxyId;

        try {
            cl = (Class<DistributedVersion.Monitors.AssaultParty.ClientProxy>) Class.forName("DistributedVersion.Monitors.AssaultParty.ClientProxy");
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

