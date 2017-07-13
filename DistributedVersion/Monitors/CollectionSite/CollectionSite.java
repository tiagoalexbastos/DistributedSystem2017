package DistributedVersion.Monitors.CollectionSite;

import DistributedVersion.Messages.AssaultPartyMessage;
import DistributedVersion.Messages.ConcentrationSiteMessage;
import DistributedVersion.Messages.GeneralRepositoryMessage;
import DistributedVersion.ComInfo.ClientCom;
import DistributedVersion.Messages.MuseumMessage;

import static DistributedVersion.Messages.Constants.*;

/**
 * Monitor Collection Site
 *
 * @author Pedro Matos and Tiago Bastos
 */
public class CollectionSite {
    /**
     * CollectionSite
     */
    private final ClientCom concentrationSite;
    /**
     * Museum
     */
    private final ClientCom museum;
    /**
     * Assault partys manager
     */
    private final ClientCom assaultParty;
    /**
     * General Repository
     */
    private ClientCom general;
    /**
     * Master Thief state
     */
    private int masterThiefState;
    /**
     * Empty rooms
     */
    private boolean[] emptyRoom = new boolean[NUM_ROOMS];
    /**
     * Assault Partys
     */
    private int[][] parties = new int[2][NUM_GROUP];
    /**
     * Nr of elements in the groups
     */
    private int[] number_of_group_elements = new int[2];
    /**
     * Total of stollen paintings
     */
    private int stolenPaintings;
    /**
     * Rooms occupied
     */
    private int[] assaultRooms = new int[NUM_ROOMS];

    /**
     * Groups occupied
     */
    private boolean grupoOcup[] = new boolean[2];


    /**
     * Number of groups
     */
    int countGrupos = 0;

    /**
     *  @param museum Museum
     * @param concentrationSite CollectionSite
     * @param assaultParty Manager of assault partys
     * @param generalRepository General Repository
     */
    public CollectionSite(String museum, String concentrationSite, String assaultParty, String generalRepository) {
        this.general = new ClientCom(generalRepository,22460);
        this.museum = new ClientCom(museum,22461);
        this.concentrationSite = new ClientCom(concentrationSite,22462);
        this.assaultParty = new ClientCom(assaultParty,22463);

        // Estado inicial do chefe
        masterThiefState = PLANNING_THE_HEIST;


        number_of_group_elements[0] = 0;
        number_of_group_elements[1] = 0;
        countGrupos = 0;
        grupoOcup[0] = false;
        grupoOcup[1] = false;



        for (int i = 0; i < NUM_GROUP; i++) {
            parties[0][i] = -1;
            parties[1][i] = -1;
        }

        for (int i = 0; i < NUM_ROOMS; i++) {
            this.emptyRoom[i] = false;
            this.assaultRooms[i] = -1;
        }

        this.stolenPaintings = 0;

    }

    /**
     * Checks if the groups are done
     *
     * @return -1 if they are or 0/1 if they aren't.
     */
    public synchronized int checkGroups() {

        if (!this.grupoOcup[0]) {

            return 0;
        } else if (!this.grupoOcup[1]) {

            return 1;
        } else {

            return -1;
        }
    }

    /**
     * Adds thief to an assault party
     *
     * @param ladraoID thief id
     * @param grupo group id
     * @return Position in the group. -1 if the group is full
     */
    public synchronized int joinGroup(int ladraoID, int grupo) {


        boolean cond = this.isGroupFull(grupo);

        if (!cond) {
            for (int i = 0; i < NUM_GROUP; i++) {
                if (this.parties[grupo][i] == -1) {
                    this.number_of_group_elements[grupo]++;
                    this.parties[grupo][i] = ladraoID;
                    //break;



                    AssaultPartyMessage inMessage, outMessage;


                    while(!assaultParty.open()){
                        try{
                            Thread.sleep((long)(1000));
                        }
                        catch (InterruptedException e){
                        }
                    }
                    outMessage = new AssaultPartyMessage(AssaultPartyMessage.JOINPARTY, ladraoID,grupo,i);
                    assaultParty.writeObject(outMessage);
                    inMessage = (AssaultPartyMessage) assaultParty.readObject();
                    assaultParty.close();

                    return i;
                }
            }
        }

        return -1;
    }



    /**
     * Verifies if the group is full
     *
     * @param grupoID group id
     * @return false if it isn't full; true if is
     */
    public synchronized boolean isGroupFull(int grupoID) {

        if (this.number_of_group_elements[grupoID] != NUM_GROUP) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Verifies master thief state
     *
     * @return master thief state
     */
    public synchronized int getMasterThiefState() {
        return this.masterThiefState;
    }

    /**
     * Starts the assault
     */
    public synchronized void startOperations() {

        this.masterThiefState = DECIDING_WHAT_TO_DO;
        setMasterThiefState(this.masterThiefState);
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
     * Creates assault party
     * @param idGrupo if of party
     * @return true if formed, false if not.
     */
    public synchronized boolean prepareAssaultParty(int idGrupo){


        this.grupoOcup[idGrupo] = true;
        this.countGrupos++;
        this.masterThiefState = ASSEMBLING_A_GROUP;
        //general.setMasterThiefState(this.masterThiefState);

        int j;
        for (j = 0; j < NUM_ROOMS; j++) {

            if (this.assaultRooms[j] == -1 && !this.emptyRoom[j]) {
                this.assaultRooms[j] = idGrupo;
                break;
            }
        }

        if(idGrupo == 0) setAssaultParty1_room(j);
        if(idGrupo == 1) setAssaultParty2_room(j);


        AssaultPartyMessage inMessage, outMessage;


        while(!assaultParty.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new AssaultPartyMessage(AssaultPartyMessage.CREATEASSAULTPARTY,idGrupo,j);
        assaultParty.writeObject(outMessage);
        inMessage = (AssaultPartyMessage) assaultParty.readObject();
        assaultParty.close();

        int tmp = inMessage.getArg1();

        if (tmp == -1){
            return false;
        }
        return true;
    }

    /**
     * Function that set's the room ID to the first assault party
     * @param j room ID
     */
    private void setAssaultParty1_room(int j){
        GeneralRepositoryMessage inMessage, outMessage;

        while(!general.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.SETASSAULTPARTY1ROOM, j);
        general.writeObject(outMessage);
        inMessage = (GeneralRepositoryMessage) general.readObject();
        general.close();
    }

    /**
     * Function that set's the room ID to the first assault party
     * @param j room ID
     */
    private void setAssaultParty2_room(int j){
        GeneralRepositoryMessage inMessage, outMessage;

        while(!general.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.SETASSAULTPARTY2ROOM, j);
        general.writeObject(outMessage);
        inMessage = (GeneralRepositoryMessage) general.readObject();
        general.close();
    }


    /**
     * Master is resting
     */
    public synchronized void takeARest(){


        ConcentrationSiteMessage inMessage, outMessage;


        while(!concentrationSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.GETTHIEFNUMBER);
        concentrationSite.writeObject(outMessage);
        inMessage = (ConcentrationSiteMessage) concentrationSite.readObject();
        concentrationSite.close();



        int nrLadroes = inMessage.getArg1();

        int checkGrupos = this.checkGroups();

        if (checkGrupos == -1 || nrLadroes < NUM_GROUP) {
            if (this.masterThiefState == ASSEMBLING_A_GROUP) {
                this.masterThiefState = WAITING_FOR_GROUP_ARRIVAL;
                setMasterThiefState(this.masterThiefState);
                return;
            }
            this.masterThiefState = WAITING_FOR_GROUP_ARRIVAL;
            setMasterThiefState(this.masterThiefState);

            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        } else {
            this.masterThiefState = DECIDING_WHAT_TO_DO;
            setMasterThiefState(this.masterThiefState);

        }
    }

    /**
     * Returns the number of elements in the group
     * @param grupoID id of group
     * @return number of elements
     */
    public synchronized int getGroupNumberOfElements(int grupoID) {
        return this.number_of_group_elements[grupoID];
    }



    /**
     * Hand canvas to the thief and leave the group
     *
     * @param ladraoID thief id
     * @param sala room id
     * @param grupo group id
     * @param pos position in group
     */
    public synchronized void handACanvas(int ladraoID, int sala, int grupo, int pos){


        this.stolenPaintings++;
        this.parties[grupo][pos] = -1;
        if (--this.number_of_group_elements[grupo] == 0) {
            this.assaultRooms[sala] = -1;


            AssaultPartyMessage inMessage, outMessage;
            while(!assaultParty.open()){
                try{
                    Thread.sleep((long)(1000));
                }
                catch (InterruptedException e){
                }
            }
            outMessage = new AssaultPartyMessage(AssaultPartyMessage.DESTROYGRPOUP, grupo);
            assaultParty.writeObject(outMessage);
            inMessage = (AssaultPartyMessage) assaultParty.readObject();
            assaultParty.close();


            this.grupoOcup[grupo] = false;
            if (this.masterThiefState == WAITING_FOR_GROUP_ARRIVAL) {
                this.masterThiefState = DECIDING_WHAT_TO_DO;
                setMasterThiefState(this.masterThiefState);
            }
            notifyAll();
        }

        if(grupo == 0){
            setAP1_reset(pos,ladraoID);
        }
        else if (grupo == 1){
            setAP2_reset(pos,ladraoID);
        }

    }

    /**
     * Function that reset's info about Assault Party 1
     * @param pos_grupo
     * @param id
     */
    private void setAP1_reset(int pos_grupo, int id) {
        GeneralRepositoryMessage inMessage, outMessage;

        while(!general.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.SETAP1RESET, pos_grupo, id);
        general.writeObject(outMessage);
        inMessage = (GeneralRepositoryMessage) general.readObject();
        general.close();
    }

    /**
     * Function that reset's info about Assault Party 2
     * @param pos_grupo
     * @param id
     */
    private void setAP2_reset(int pos_grupo, int id) {
        GeneralRepositoryMessage inMessage, outMessage;

        while(!general.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.SETAP2RESET, pos_grupo, id);
        general.writeObject(outMessage);
        inMessage = (GeneralRepositoryMessage) general.readObject();
        general.close();
    }

    /**
     * Room is empty
     *
     * @param sala room id
     * @param grupo group id
     * @param pos position in the group
     */
    public synchronized void flagEmptyRoom(int sala, int grupo, int pos){


        this.emptyRoom[sala] = true;
        this.parties[grupo][pos] = -1;
        if (--this.number_of_group_elements[grupo] == 0) {
            this.assaultRooms[sala] = -1;

            AssaultPartyMessage inMessage, outMessage;
            while(!assaultParty.open()){
                try{
                    Thread.sleep((long)(1000));
                }
                catch (InterruptedException e){
                }
            }
            outMessage = new AssaultPartyMessage(AssaultPartyMessage.DESTROYGRPOUP, grupo);
            assaultParty.writeObject(outMessage);
            inMessage = (AssaultPartyMessage) assaultParty.readObject();
            assaultParty.close();


            this.grupoOcup[grupo] = false;
            if (this.masterThiefState == WAITING_FOR_GROUP_ARRIVAL) {
                this.masterThiefState = DECIDING_WHAT_TO_DO;
                setMasterThiefState(this.masterThiefState);
            }
            notifyAll();
        }


    }

    /**
     * Verifies the position in the group
     * @param id thief id
     * @param grupo group id
     * @return position in the group
     */
    public int getGroupPosition(int id, int grupo) {

        for (int i = 0; i < NUM_GROUP; i++) {
            if (parties[grupo][i] == id) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Verifies if there are paitings in the museum
     * @return true if empty
     */
    public boolean checkEmptyMuseum() {
        for (int i = 0; i < emptyRoom.length; i++) {
            if (!emptyRoom[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Show the results of the assault
     */
    public void sumUpResults() {
        this.masterThiefState = PRESENTING_THE_REPORT;
        setMasterThiefState(this.masterThiefState);
    }

    /**
     * Number of stollen paitings
     *
     * @return Number of stollen paitings
     */

    public int getStolenPaintings() {

        return this.stolenPaintings;
    }

    /**
     * Checks if there are rooms with paitings
     *
     * @return true if there are rooms with paitings
     */
    public boolean checkEmptyRooms() {

        for (int i = 0; i < NUM_ROOMS; i++) {
            if (!emptyRoom[i] && assaultRooms[i] == -1) {
                //salasLivres = true;
                return true;
            }
        }
        return false;
    }


    /**
     * Returns the room the group is assaulting
     *
     * @param grupoID group id
     * @return room id
     */
    public int getAssaultingRoom(int grupoID) {
        for (int i = 0; i < NUM_ROOMS; i++) {
            if (this.assaultRooms[i] == grupoID) {
                return i;
            }
        }
        return -1;
    }

    public void end() {
        MuseumMessage inMessage, outMessage;
        while(!museum.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new MuseumMessage(MuseumMessage.END);
        museum.writeObject(outMessage);
        inMessage = (MuseumMessage) museum.readObject();
        museum.close();
    }
}
