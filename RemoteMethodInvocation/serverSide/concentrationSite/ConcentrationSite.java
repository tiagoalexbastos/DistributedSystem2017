package serverSide.concentrationSite;

import interfaces.ConcentrationSiteInterface;
import interfaces.GeneralRepositoryInterface;
import interfaces.Register;
import registry.RegistryConfig;
import support.Constantes;
import support.MemFIFO;
import support.Tuple;
import support.VectorTimestamp;

import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import static support.Constantes.*;

/**
 * Class that descibes the Collection Site
 * Monitor Concentration Site.
 *
 * @author Pedro Matos and Tiago Bastos
 */
public class ConcentrationSite implements ConcentrationSiteInterface {

    /**
     * FIFO used when the Thiefs arrive to the CollectionSite.
     */
    private MemFIFO fifo;
    /**
     * State of thief's
     */
    private int[] thiefState = new int[NUM_THIEVES];
    /**
     * Flag if the thief is busy
     */
    private boolean[] busyThief = new boolean[NUM_THIEVES];
    /**
     * Number of thiefs in the CollectionSite
     */
    private int numberofThieves;
    /**
     * Agility of each thief
     */
    private int thievesAgility[] = new int[NUM_THIEVES];
    /**
     * Group of each thief
     */
    private int thiefGroup[] = new int[NUM_THIEVES];
    /**
     * General Repository
     */
    private GeneralRepositoryInterface general;
    /**
     * Thief's situation
     */
    private int thiefSituation[] = new int[NUM_THIEVES];

    /**
     * Local Vector Timestamp
     */
    private VectorTimestamp local;

    /**
     * RMI Register host name
     */
    private String rmiRegHostName;

    /**
     * RMI Register host name
     */
    private int rmiRegPortNumb;


    /**
     * @param generalRepository General Repository Interface
     * @param rmiRegHostName RMI Register host name
     * @param rmiRegPortNumb RMI Register port number
     */
    public ConcentrationSite(GeneralRepositoryInterface generalRepository, String rmiRegHostName, int rmiRegPortNumb) {

        this.rmiRegHostName = rmiRegHostName;
        this.rmiRegPortNumb = rmiRegPortNumb;

        this.numberofThieves = 0;

        for (int i = 0; i < NUM_THIEVES; i++) {
            thiefState[i] = OUTSIDE;
            this.busyThief[i] = false;
            thiefGroup[i] = -1;
            thievesAgility[i] = ((int) (Math.random() * (MAX_AGIL - 1))) + MIN_AGIL;
            this.thiefSituation[i] = WAITING;
        }

        fifo = new MemFIFO(NUM_THIEVES);

        this.general = generalRepository;

        local = new VectorTimestamp(Constantes.VECTOR_TIMESTAMP_SIZE, 0);

    }

    /**
     * Get for the thief's group
     *
     * @param id              thief's id
     * @param vectorTimestamp clock
     * @return clock and thief's group
     */
    @Override
    public synchronized Tuple<VectorTimestamp, Integer> getThiefGroup(int id, VectorTimestamp vectorTimestamp) {

        local.update(vectorTimestamp);

        return new Tuple<>(local.clone(), thiefGroup[id]);

    }

    /**
     * The thief uses this method when he is ready.
     * The thief is added to the FIFO
     *
     * @param ladraoID        thief's id
     * @param vectorTimestamp clock
     * @return clock
     */
    @Override
    public synchronized VectorTimestamp imReady(int ladraoID, VectorTimestamp vectorTimestamp) {
        local.update(vectorTimestamp);

        if (!fifo.full()) {
            this.busyThief[ladraoID] = false;
            fifo.write(ladraoID);
            this.numberofThieves++;
            this.thiefGroup[ladraoID] = -1;
        } else {
            System.out.println("ERRO!!");
        }

        if (this.numberofThieves >= NUM_GROUP) {

            notifyAll();
        } else {
            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }

        return local.clone();
    }

    /**
     * Get for the number of thiefs in the CollectionSite
     *
     * @param vectorTimestamp clock
     * @return clock and number of thieves
     */
    @Override
    public synchronized Tuple<VectorTimestamp, Integer> getNumberOfThieves(VectorTimestamp vectorTimestamp) {
        local.update(vectorTimestamp);

        return new Tuple<>(local.clone(), this.numberofThieves);
    }

    /**
     * Get for the thief state
     *
     * @param ladraoID        thief id
     * @param vectorTimestamp clock
     * @return clock and thief state
     */
    @Override
    public synchronized Tuple<VectorTimestamp, Integer> getThiefState(int ladraoID, VectorTimestamp vectorTimestamp) {
        local.update(vectorTimestamp);

        return new Tuple<>(local.clone(), this.thiefState[ladraoID]);

    }

    /**
     * Thief uses this when he is waiting for orders
     *
     * @param ladraoID        thief id
     * @param vectorTimestamp clock
     * @return clock
     */
    @Override
    public synchronized VectorTimestamp amINeeded(int ladraoID, VectorTimestamp vectorTimestamp) {

        local.update(vectorTimestamp);

        notifyAll();
        while (!this.busyThief[ladraoID] && this.thiefState[ladraoID] != HEIST_END) {
            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }

        return local.clone();

    }

    /**
     * Calls a thief from the FIFO and adds to the group
     *
     * @param grupo           group id
     * @param vectorTimestamp clock
     * @return clock and id of thief, or -1 if FIFO is empty
     */
    @Override
    public synchronized Tuple<VectorTimestamp, Integer> callThief(int grupo, VectorTimestamp vectorTimestamp) {

        local.update(vectorTimestamp);

        int id = -1;

        if (!fifo.empty()) {
            id = (Integer) fifo.read();
            this.numberofThieves--;
            this.busyThief[id] = true;
            this.thiefGroup[id] = grupo;
            setMasterThiefState(ASSEMBLING_A_GROUP);
            setThiefSituation(id, IN_PARTY);
            notifyAll();
        }

        return new Tuple<>(local.clone(), id);
    }


    /**
     * Verifies if the thief is busy
     *
     * @param ladraoID        thief id
     * @param vectorTimestamp clock
     * @return clock and  true if busy, false is free
     */
    @Override
    public synchronized Tuple<VectorTimestamp, Boolean> getBusyThief(int ladraoID, VectorTimestamp vectorTimestamp) {
        local.update(vectorTimestamp);
        return new Tuple<>(local.clone(), this.busyThief[ladraoID]);
    }

    /**
     * State changes to Crawl IN
     *
     * @param ladraoID        thief id
     * @param vectorTimestamp clock
     * @return clock
     */
    @Override
    public synchronized VectorTimestamp prepareExcursion(int ladraoID, VectorTimestamp vectorTimestamp) {

        local.update(vectorTimestamp);

        this.thiefState[ladraoID] = CRAWLING_INWARDS;
        return local.clone();
    }

    /**
     * State changes to Crawl OUT
     *
     * @param ladraoID        thief id
     * @param vectorTimestamp clock
     * @return clock
     */
    @Override
    public synchronized VectorTimestamp reverseDirection(int ladraoID, VectorTimestamp vectorTimestamp) {
        local.update(vectorTimestamp);

        this.thiefState[ladraoID] = CRAWLING_OUTWARDS;
        return local.clone();
    }

    /**
     * State changes to At a Room
     *
     * @param ladraoID        thief id
     * @param vectorTimestamp clock
     * @return clock
     */
    @Override
    public synchronized VectorTimestamp atARoom(int ladraoID, VectorTimestamp vectorTimestamp) {
        local.update(vectorTimestamp);

        this.thiefState[ladraoID] = AT_A_ROOM;
        return local.clone();
    }

    /**
     * Thief arrives at the CollectionSite
     *
     * @param ladraoID        thief id
     * @param vectorTimestamp clock
     * @return clock
     */
    @Override
    public synchronized VectorTimestamp flagArrival(int ladraoID, VectorTimestamp vectorTimestamp) {

        local.update(vectorTimestamp);

        this.busyThief[ladraoID] = false;
        this.thiefGroup[ladraoID] = -1;
        this.thiefState[ladraoID] = OUTSIDE;
        setThiefState(ladraoID, this.thiefState[ladraoID]);
        setThiefSituation(ladraoID, WAITING);
        this.imReady(ladraoID, local.clone());


        return local.clone();
    }


    /**
     * GET for the agility
     *
     * @param ladraoID        thief id
     * @param vectorTimestamp clock
     * @return clock and thief agility
     */
    @Override
    public synchronized Tuple<VectorTimestamp, Integer> getAgility(int ladraoID, VectorTimestamp vectorTimestamp) {

        local.update(vectorTimestamp);

        return new Tuple<>(local.clone(), thievesAgility[ladraoID]);
    }

    /**
     * Waiting for the correct number of thiefs in order to create an assault party
     *
     * @param vectorTimestamp clock
     * @return clock
     */
    @Override
    public synchronized VectorTimestamp waitForThieves(VectorTimestamp vectorTimestamp) {

        local.update(vectorTimestamp);

        while (this.numberofThieves < NUM_GROUP) {
            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }
        notifyAll();

        return local.clone();
    }

    /**
     * Waits for all the thiefs in order to finish operation
     *
     * @param vectorTimestamp clock
     * @return clock
     */
    @Override
    public synchronized VectorTimestamp waitForThievesEnd(VectorTimestamp vectorTimestamp) {

        local.update(vectorTimestamp);

        while (this.numberofThieves < NUM_THIEVES) {
            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }
        for (int i = 0; i < NUM_THIEVES; i++) {
            thiefState[i] = HEIST_END;
        }
        notifyAll();
        return local.clone();
    }

    /**
     * This function is used for the log to signal the AssaultPartyManager to shutdown.
     *
     * @throws RemoteException may throw during a execution of a remote method call
     */
    @Override
    public void signalShutdown() throws RemoteException {
        Register reg = null;
        Registry registry = null;

        String rmiRegHostName;
        int rmiRegPortNumb;

        rmiRegHostName = this.rmiRegHostName;
        rmiRegPortNumb = this.rmiRegPortNumb;

        try {
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException ex) {
            System.out.println("Erro ao localizar o registo");
            ex.printStackTrace();
            System.exit(1);
        }

        String nameEntryBase = RegistryConfig.RMI_REGISTER_NAME;
        String nameEntryObject = RegistryConfig.RMI_REGISTRY_CONSITE_NAME;


        try {
            reg = (Register) registry.lookup(nameEntryBase);
        } catch (RemoteException e) {
            System.out.println("RegisterRemoteObject lookup exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("RegisterRemoteObject not bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        try {
            // Unregister ourself
            reg.unbind(nameEntryObject);
        } catch (RemoteException e) {
            System.out.println("Concentration registration exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("Concentration not bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        try {
            // Unexport; this will also remove us from the RMI runtime
            UnicastRemoteObject.unexportObject(this, true);
        } catch (NoSuchObjectException ex) {
            ex.printStackTrace();
            System.exit(1);
        }

        System.out.println("Concentration Site closed.");
    }

    /**
     * Function that set's the Ordinary Thief Situation
     * @param id Thief ID
     * @param inParty Thief State
     */
    private void setThiefSituation(int id, int inParty) {
        try {
            local.update(this.general.setThiefSituation(id, inParty, local.clone()));
        } catch (RemoteException e) {
            System.err.println("Excepção na invocação remota de método" + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Function that set's the Master Thief state
     * @param stat Master Thief state
     */
    private void setMasterThiefState(int stat) {
        try {
            local.update(this.general.setMasterThiefState(stat, local.clone()));
        } catch (RemoteException e) {
            System.err.println("Excepção na invocação remota de método" + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }


    /**
     * Function that set's the Ordinary Thief State
     * @param ladraoID Ordinary Thief ID
     * @param i Ordinary Thief State
     */
    private void setThiefState(int ladraoID, int i) {
        try {
            local.update(this.general.setThiefState(ladraoID, i, local.clone()));
        } catch (RemoteException e) {
            System.err.println("Excepção na invocação remota de método" + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
