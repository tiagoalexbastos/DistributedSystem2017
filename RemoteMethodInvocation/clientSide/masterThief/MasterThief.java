package clientSide.masterThief;

import interfaces.CollectionSiteInterface;
import interfaces.ConcentrationSiteInterface;
import interfaces.GeneralRepositoryInterface;
import support.Tuple;
import support.VectorTimestamp;

import java.rmi.RemoteException;

import static support.Constantes.*;


/**
 * Represents the Master Thief
 *
 * @author Pedro Matos
 * @author Tiago Bastos
 */
public class MasterThief extends Thread {


    /**
     * Log that contains the thief's values
     */
    private GeneralRepositoryInterface generalRepository;
    /**
     * Concentration Site
     */
    private CollectionSiteInterface collectionSiteInterface;
    /**
     * Thief's concentrationSite
     */
    private ConcentrationSiteInterface concentrationSite;
    /**
     * Thief ID
     */
    private int id;
    /**
     * Thief name
     */
    private String name;

    /**
     * Thief local VectorTimestamp
     */
    private VectorTimestamp vt;


    /**
     * Creates a Master Thief instance for runnning in a distributed enviroment
     *
     * @param name                    Masther Thief's name
     * @param generalRepository       General Repository Interface
     * @param collectionSiteInterface Concentration Site Interface
     * @param concentrationSite       CollectionSite Interface
     */
    public MasterThief(String name, GeneralRepositoryInterface generalRepository, CollectionSiteInterface collectionSiteInterface,
                       ConcentrationSiteInterface concentrationSite) {

        super(name);
        this.name = name;
        this.id = NUM_THIEVES;

        this.generalRepository = generalRepository;

        this.concentrationSite = concentrationSite;
        this.collectionSiteInterface = collectionSiteInterface;

        vt = new VectorTimestamp(VECTOR_TIMESTAMP_SIZE, 0);
    }

    /**
     * Master Thief Lifecycle
     */
    @Override
    public void run() {

        int numero_grupos = -1;
        boolean heistOver = false;

        while (!heistOver) {

            int stat = getMasterThiefState();


            switch (stat) {
                case PLANNING_THE_HEIST:
                    startLog();
                    setMasterThiefState(stat);

                    if (getNumberOfThieves() == NUM_THIEVES) {
                        startOperations();
                    }

                    break;
                case DECIDING_WHAT_TO_DO:

                    numero_grupos = checkGroups();
                    boolean emptyMuseu = checkEmptyMuseum();
                    boolean checkSalasLivres = checkEmptyRooms();
                    if (numero_grupos != -1 && !emptyMuseu && checkSalasLivres){
                        prepareAssaultParty(numero_grupos);
                    }
                    else {
                        emptyMuseu = checkEmptyMuseum();
                        if (emptyMuseu)
                            sumUpResults();
                        else
                            takeARest();

                    }
                    break;
                case ASSEMBLING_A_GROUP:

                    waitForThieves();
                    int nrElemGrupo = getNumberElemGroup(numero_grupos);

                    if (nrElemGrupo == 0) {

                        for (int i = 0; i < NUM_GROUP; i++) {
                            int ladrao = callThief(numero_grupos);
                            joinAssaultParty(ladrao, numero_grupos);
                        }
                        takeARest();
                    }
                    break;
                case WAITING_FOR_GROUP_ARRIVAL:
                    takeARest();
                    break;
                case PRESENTING_THE_REPORT:
                    waitForThievesEnd();

                    int nrQuadrosRoubados = getNumberOfStolenPaints();
                    endReport(nrQuadrosRoubados);
                    heistOver = true;
                    break;
            }
        }


    }

    /**
     * End the log
     * @param nrQuadrosRoubados total of stolen paintings
     */
    private void endReport(int nrQuadrosRoubados) {
        try {
            this.generalRepository.endReport(nrQuadrosRoubados, vt.clone());
        } catch (RemoteException e) {
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Get all the stolen paintings
     * @return int with the value of stolen painting or no painting
     */
    private int getNumberOfStolenPaints() {
        int ret = -1;
        try {
            vt.increment();
            Tuple<VectorTimestamp, Integer> tuple = collectionSiteInterface.getNumberofStolenPaints(vt.clone());
            ret = tuple.getSecond();
            vt.update(tuple.getClock());
        } catch (RemoteException e) {
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        return ret;
    }

    /**
     * Function used when waiting for the thieves to finish
     */
    private void waitForThievesEnd() {
        try {
            VectorTimestamp clock = concentrationSite.waitForThievesEnd(vt.clone());
            vt.update(clock);
        } catch (RemoteException e) {
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Function used to place a thief inside a party
     * @param ladrao id of the thief
     * @param numero_grupos if of the group
     * @return assault party number
     */
    private int joinAssaultParty(int ladrao, int numero_grupos) {
        int ret = -1;
        try {
            vt.increment();
            Tuple<VectorTimestamp, Integer> tuple = collectionSiteInterface.joinAssaultParty(ladrao, numero_grupos, vt.clone());
            ret = tuple.getSecond();
            vt.update(tuple.getClock());
        } catch (RemoteException e) {
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        return ret;
    }

    /**
     * Function that calls a Thief to join a party
     * @param numero_grupos id of the group
     * @return the id of the thief
     */
    private int callThief(int numero_grupos) {
        int ret = -1;
        try {
            vt.increment();
            Tuple<VectorTimestamp, Integer> tuple = concentrationSite.callThief(numero_grupos, vt.clone());
            vt.update(tuple.getClock());
            ret = tuple.getSecond();
        } catch (RemoteException e) {
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        return ret;
    }

    /**
     * Get number of elements in the specified group
     * @param numero_grupos id of the group
     * @return int with the number of elements
     */
    private int getNumberElemGroup(int numero_grupos) {
        int ret = -1;
        try {
            Tuple<VectorTimestamp, Integer> tuple = collectionSiteInterface.getNumberElemGroup(numero_grupos, vt.clone());
            vt.update(tuple.getClock());
            ret = tuple.getSecond();
        } catch (RemoteException e) {
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        return ret;
    }

    /**
     * Function used when the Master Thief needs to be waiting for the thieves
     */
    private void waitForThieves() {
        try {
//            vt.increment();
            VectorTimestamp clock = concentrationSite.waitForThieves(vt.clone());
            vt.update(clock);
        } catch (RemoteException e) {
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Function that the Master Thief uses to take a rest
     */
    private void takeARest() {
        try {
            vt.increment();
            VectorTimestamp clock = collectionSiteInterface.takeARest(vt.clone());
            vt.update(clock);
        } catch (RemoteException e) {
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Function that shows the results of the heist: the number of stolen paintings.
     */
    private void sumUpResults() {
        try {
            vt.increment();
            VectorTimestamp clock = collectionSiteInterface.sumUpResults(vt.clone());
            vt.update(clock);
        } catch (RemoteException e) {
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Function that prepares one assault party
     * @param numero_grupos is the number of the assault party
     */
    private void prepareAssaultParty(int numero_grupos) {
        try {
            vt.increment();
            VectorTimestamp clock = collectionSiteInterface.prepareAssaultParty(numero_grupos, vt.clone());
            vt.update(clock);
        } catch (RemoteException e) {
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Function that checks if the rooms are empty
     * @return true if the rooms are empty
     */
    private boolean checkEmptyRooms() {
        boolean ret = false;
        try {
            Tuple<VectorTimestamp, Boolean> tuple = collectionSiteInterface.checkEmptyRooms(vt.clone());
            vt.update(tuple.getClock());
            ret = tuple.getSecond();
        } catch (RemoteException e) {
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        return ret;
    }

    /**
     * Function that checks if the Museum is Empty
     * @return boolean true if museum is empty; false if there are still paitings
     */
    private boolean checkEmptyMuseum() {
        boolean ret = false;
        try {
            Tuple<VectorTimestamp, Boolean> tuple = collectionSiteInterface.checkEmptyMuseum(vt.clone());
            vt.update(tuple.getClock());
            ret = tuple.getSecond();
        } catch (RemoteException e) {
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        return ret;
    }

    /**
     * Funcion that checks the number of groups
     * @return number of groups
     */
    private int checkGroups() {
        int ret = -1;
        try {
            Tuple<VectorTimestamp, Integer> tuple = collectionSiteInterface.checkGroups(vt.clone());
            vt.update(tuple.getClock());
            ret = tuple.getSecond();
        } catch (RemoteException e) {
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        return ret;
    }

    /**
     * Funcion that indicates the start of operations
     */
    private void startOperations() {
        try {
            vt.increment();
            VectorTimestamp clock = collectionSiteInterface.startOperations(vt.clone());
            vt.update(clock);
        } catch (RemoteException e) {
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     *  Function that returns the number of thives in the concentration site
     * @return Number of Thieves
     */
    private int getNumberOfThieves() {
        int ret = -1;
        try {
            Tuple<VectorTimestamp, Integer> tuple = concentrationSite.getNumberOfThieves(vt.clone());
            vt.update(tuple.getClock());
            ret = tuple.getSecond();
        } catch (RemoteException e) {
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        return ret;
    }

    /**
     * Start the log file
     */
    private void startLog() {
        try {
            this.generalRepository.startLog(vt.clone());
        } catch (RemoteException e) {
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     *  Function that gets the Master Thief state
     * @return Master Thief State
     */
    private int getMasterThiefState() {
        int state = -1;
        try {
            Tuple<VectorTimestamp, Integer> tuple = collectionSiteInterface.getMasterThiefState(vt.clone());
            vt.update(tuple.getClock());
            state = tuple.getSecond();
        } catch (RemoteException e) {
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        return state;
    }

    /**
     * Set Master Thief State
     * @param stat state of the master thief
     */
    private void setMasterThiefState(int stat) {
        try {
            vt.update(this.generalRepository.setMasterThiefState(stat, vt.clone()));
        } catch (RemoteException e) {
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
