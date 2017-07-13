package DistributedVersion.Monitors.ConcentrationSite;

import static DistributedVersion.ComInfo.ComPorts.portGeneralRepo;
import static DistributedVersion.Messages.Constants.*;
import DistributedVersion.Support.MemFIFO;
import DistributedVersion.ComInfo.ClientCom;
import DistributedVersion.Messages.GeneralRepositoryMessage;

/**
 * Monitor Concentration Site.
 * @author Pedro Matos and Tiago Bastos
 */
public class ConcentrationSite {

    /**
     * FIFO used when the Thiefs arrive to the CollectionSite.
     */
    private MemFIFO fifo;
    /**
     * State of thief's
     */
    private int[] thief_state = new int[NUM_THIEVES];
    /**
     * Flag if the thief is busy
     */
    private boolean[] busyThief = new boolean[NUM_THIEVES];
    /**
     * Number of thiefs in the CollectionSite
     */
    private int number_of_thieves;
    /**
     * Agility of each thief
     */
    private int thiefAgility[] = new int[NUM_THIEVES];
    /**
     * Group of each thief
     */
    private int thievesGroups[] = new int[NUM_THIEVES];
    /**
     * General Repository
     */
    private ClientCom general;
    /**
     * Thief's situation
     */
    private int thiefSituation[] = new int[NUM_THIEVES];


    /**
     * @param generalRepository General Repository
     */
    public ConcentrationSite(String generalRepository) {

        this.number_of_thieves = 0;

        for (int i = 0; i < NUM_THIEVES; i++) {
            thief_state[i] = OUTSIDE;
            this.busyThief[i] = false;
            thievesGroups[i] = -1;
            thiefAgility[i] = ((int) (Math.random() * (MAX_AGIL - 1))) + MIN_AGIL;
            this.thiefSituation[i] = WAITING;
        }

        fifo = new MemFIFO(NUM_THIEVES);

        this.general = new ClientCom(generalRepository, portGeneralRepo);
    }

    /**
     * Get for the thief's group
     * @param id thief's id
     * @return thief's group
     */
    public synchronized int getThiefGroup(int id) {


        return thievesGroups[id];

    }

    /**
     * The thief uses this method when he is ready.
     * The thief is added to the FIFO
     *
     * @param ladraoID thief's id
     */
    public synchronized void iAmReady(int ladraoID) {


        if (!fifo.full()) {
            this.busyThief[ladraoID] = false;
            fifo.write(ladraoID);
            this.number_of_thieves++;
            this.thievesGroups[ladraoID] = -1;
        } else {
            System.out.println("ERRO!!");
        }

        if (this.number_of_thieves >= NUM_GROUP) {

            notifyAll();
        } else {
            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    /**
     * Get for the number of thiefs in the CollectionSite
     * @return number of thiefs
     */
    public synchronized int getNumber_of_thieves() {


        return this.number_of_thieves;
    }

    /**
     * Get for the thief state
     * @param ladraoID thief id
     * @return thief state
     */

    public synchronized int getThiefState(int ladraoID) {

        return this.thief_state[ladraoID];

    }

    /**
     * Thief uses this when he is waiting for orders
     * @param ladraoID thief id
     */

    public synchronized void amINeeded(int ladraoID) {

        notifyAll();
        while (!this.busyThief[ladraoID] && this.thief_state[ladraoID] != HEIST_END) {
            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }

    }

    /**
     * Function that set's the Master Thief state
     * @param state Master Thief state
     */
    private void setMasterThiefState(int state){
        GeneralRepositoryMessage inMessage, outMessage;

        while(!general.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }

        outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.SETMASTERTHIEFSTATE, state);
        general.writeObject(outMessage);
        inMessage = (GeneralRepositoryMessage) general.readObject();
        general.close();
    }


    /**
     * Function that set's the Ordinary Thief Situation
     * @param id Thief ID
     * @param state Thief State
     */
    private void setThiefSituation(int id, int state){
        GeneralRepositoryMessage inMessage, outMessage;

        while(!general.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }

        outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.SETTHIEFSITUATION, id, state);
        general.writeObject(outMessage);
        inMessage = (GeneralRepositoryMessage) general.readObject();
        general.close();
    }


    /**
     *  Calls a thief from the FIFO and adds to the group
     * @param grupo group id
     * @return id of thief, or -1 if FIFO is empty
     */
    public synchronized int callThief(int grupo) {


        int id = -1;

        if (!fifo.empty()) {
            id = (Integer) fifo.read();
            this.number_of_thieves--;
            this.busyThief[id] = true;
            this.thievesGroups[id] = grupo;
            setMasterThiefState(ASSEMBLING_A_GROUP);
            setThiefSituation(id,IN_PARTY);
            notifyAll();
        }

        return id;
    }

    /**
     * Verifies if the thief is busy
     *
     * @param ladraoID thief id
     * @return true if busy, false is free
     */
    public synchronized boolean getBusyThief(int ladraoID) {
        return this.busyThief[ladraoID];
    }

    /**
     * State changes to Crawl IN
     * @param ladraoID thief id
     */
    public synchronized void prepareExcursion(int ladraoID) {
        this.thief_state[ladraoID] = CRAWLING_INWARDS;
    }

    /**
     * State changes to Crawl OUT
     * @param ladraoID thief id
     */
    public synchronized void reverseDirection(int ladraoID) {
        this.thief_state[ladraoID] = CRAWLING_OUTWARDS;
    }

    /**
     * State changes to At a Room
     * @param ladraoID thief id
     */
    public synchronized void atARoom(int ladraoID) {
        this.thief_state[ladraoID] = AT_A_ROOM;
    }

    /**
     * Function that set's the Ordinary Thief State
     * @param ladraoID Ordinary Thief ID
     * @param estado Ordinary Thief State
     */
    private void setThiefState(int ladraoID, int estado){
        GeneralRepositoryMessage inMessage, outMessage;

        while(!general.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }

        outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.SETTHIEFSTATE,ladraoID,estado);
        general.writeObject(outMessage);
        inMessage = (GeneralRepositoryMessage) general.readObject();
        general.close();

    }


    /**
     * Thief arrives at the CollectionSite
     * @param ladraoID thief id
     */
    public synchronized void thiefArrived(int ladraoID) {

        this.busyThief[ladraoID] = false;
        this.thievesGroups[ladraoID] = -1;
        this.thief_state[ladraoID] = OUTSIDE;
        setThiefState(ladraoID,this.thief_state[ladraoID]);
        setThiefSituation(ladraoID,WAITING);
        this.iAmReady(ladraoID);
    }

    /**
     * GET for the agility
     * @param ladraoID thief id
     * @return thief agility
     */
    public synchronized int getAgility(int ladraoID) {
        return thiefAgility[ladraoID];
    }

    /**
     * Waiting for the correct number of thiefs in order to create an assault party
     */
    public synchronized void waitForThieves() {
        while (this.number_of_thieves < NUM_GROUP) {
            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }
        notifyAll();
    }

    /**
     * Waits for all the thiefs in order to finish operation
     */
    public synchronized void waitForThievesEnd() {
        while (this.number_of_thieves < NUM_THIEVES) {
            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }
        for (int i = 0; i < NUM_THIEVES; i++) {
            thief_state[i] = HEIST_END;
        }
        notifyAll();
    }
}
