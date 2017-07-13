package DistributedVersion.ProblemInformation;

import DistributedVersion.ComInfo.ServerCom;
import DistributedVersion.Messages.GeneralRepositoryMessage;
import DistributedVersion.Messages.GeneralRepositoryMessageException;
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
     * Interface to General Repository
     *
     * @serialField generalRepositoryInterface
     */
    private GeneralRepositoryInterface generalRepositoryInterface;

    /**
     * Instantiation of interface
     *
     * @param sconi communication channel
     * @param generalRepositoryInterface interface
     */
    public ClientProxy(ServerCom sconi, GeneralRepositoryInterface generalRepositoryInterface) {
        super("Proxy_" + getProxyId());

        this.sconi = sconi;
        this.generalRepositoryInterface = generalRepositoryInterface;
    }

    /**
     * Life cycle
     */
    @Override
    public void run() {
        GeneralRepositoryMessage inMessage = null,
                outMessage = null;

        inMessage = (GeneralRepositoryMessage) sconi.readObject();
        try {
            outMessage = generalRepositoryInterface.processAndReply(inMessage);
        } catch (GeneralRepositoryMessageException e) {
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
        Class<DistributedVersion.ProblemInformation.ClientProxy> cl = null;
        //   virtual de Java
        int proxyId;

        try {
            cl = (Class<DistributedVersion.ProblemInformation.ClientProxy>) Class.forName("DistributedVersion.ProblemInformation.ClientProxy");
        } catch (ClassNotFoundException e) {
            GenericIO.writelnString("Data type not found!");
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

