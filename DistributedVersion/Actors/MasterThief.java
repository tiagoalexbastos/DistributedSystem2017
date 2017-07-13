package DistributedVersion.Actors;

import static DistributedVersion.ComInfo.ComPorts.*;
import static DistributedVersion.Messages.Constants.*;

import DistributedVersion.ComInfo.ClientCom;
import DistributedVersion.Messages.CollectionSiteMessage;
import DistributedVersion.Messages.ConcentrationSiteMessage;
import DistributedVersion.Messages.GeneralRepositoryMessage;


/**
 * Represents the Master Thief
 * @author Pedro Matos and Tiago Bastos
 */
public class MasterThief extends Thread {


    /**
     * Log that contains the thief's values
     */
    private ClientCom generalRepository;
    /**
     * Concentration Site
     */
    private ClientCom collectionSite;
    /**
     * Thief's concentrationSite
     */
    private ClientCom concentrationSite;
    /**
     * Thief ID
     */
    private int id;
    /**
     * Thief name
     */
    private String name;



    /**
     *
     * @param name Masther Thief's name
     * @param generalRepository General Repository/log
     * @param collectionSite Concentration Site
     * @param concentrationSite CollectionSite
     */
    public MasterThief(String name, String generalRepository, String collectionSite, String concentrationSite) {

        super(name);
        this.name = name;
        this.id = NUM_THIEVES;

        this.generalRepository = new ClientCom(generalRepository, portGeneralRepo);
        this.concentrationSite = new ClientCom(concentrationSite, portConcentrationSite);
        this.collectionSite = new ClientCom(collectionSite, portCollectionSite);
    }

    /**
     *  Function that gets the Master Thief state
     * @return Master Thief State
     */
    private int getMasterThiefState(){
        CollectionSiteMessage inMessage, outMessage;

        while(!collectionSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new CollectionSiteMessage(CollectionSiteMessage.GETMASTERTHIEFSTATE);
        collectionSite.writeObject(outMessage);
        inMessage = (CollectionSiteMessage) collectionSite.readObject();
        collectionSite.close();

        return inMessage.getArg1();
    }

    /**
     *  Function that returns the number of thives in the concentration site
     * @return Number of Thieves
     */
    private int getThiefNumber(){
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

        return inMessage.getArg1();
    }

    /**
     * Funcion that indicates the start of operations
     */
    private void startOperations(){
        CollectionSiteMessage inMessage, outMessage;

        while(!collectionSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new CollectionSiteMessage(CollectionSiteMessage.STARTOPERATIONS);
        collectionSite.writeObject(outMessage);
        inMessage = (CollectionSiteMessage) collectionSite.readObject();
        collectionSite.close();
    }


    /**
     * Funcion that checks the number of groups
     * @return number of groups
     */
    private int checkGroups(){
        CollectionSiteMessage inMessage, outMessage;

        while(!collectionSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new CollectionSiteMessage(CollectionSiteMessage.CHECKGROUPS);
        collectionSite.writeObject(outMessage);
        inMessage = (CollectionSiteMessage) collectionSite.readObject();
        collectionSite.close();
        return inMessage.getArg1();
    }

    /**
     * Function that checks if the Museum is Empty
     * @return boolean true if museum is empty; false if there are still paitings
     */
    private boolean checkEmptyMuseum(){
        CollectionSiteMessage inMessage, outMessage;

        while(!collectionSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new CollectionSiteMessage(CollectionSiteMessage.CHECKEMPTYMUSEUM);
        collectionSite.writeObject(outMessage);
        inMessage = (CollectionSiteMessage) collectionSite.readObject();
        collectionSite.close();
        return inMessage.getBool();
    }

    /**
     * Function that checks if the rooms are empty
     * @return true if the rooms are empty
     */
    private boolean checkEmptyRooms(){
        CollectionSiteMessage inMessage, outMessage;

        while(!collectionSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new CollectionSiteMessage(CollectionSiteMessage.CHECKEMPTYROOMS);
        collectionSite.writeObject(outMessage);
        inMessage = (CollectionSiteMessage) collectionSite.readObject();
        collectionSite.close();
        return inMessage.getBool();
    }

    /**
     * Function that prepares one assault party
     * @param numero_grupos is the number of the assault party
     */
    private void prepareAssaultParty(int numero_grupos){
        CollectionSiteMessage inMessage, outMessage;

        while(!collectionSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new CollectionSiteMessage(CollectionSiteMessage.PREPAREASSAULTPARTY, numero_grupos);
        collectionSite.writeObject(outMessage);
        inMessage = (CollectionSiteMessage) collectionSite.readObject();
        generalRepository.close();

    }

    /**
     * Function that shows the results of the heist: the number of stolen paintings.
     */
    private void sumUpResults(){
        CollectionSiteMessage inMessage, outMessage;

        while(!collectionSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new CollectionSiteMessage(CollectionSiteMessage.SUMUPRESULTS);
        collectionSite.writeObject(outMessage);
        inMessage = (CollectionSiteMessage) collectionSite.readObject();
        collectionSite.close();
    }

    /**
     * Function that the Master Thief uses to take a rest
     */
    private void takeARest(){
        CollectionSiteMessage inMessage, outMessage;

        while(!collectionSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new CollectionSiteMessage(CollectionSiteMessage.TAKEAREST);
        collectionSite.writeObject(outMessage);
        inMessage = (CollectionSiteMessage) collectionSite.readObject();
        collectionSite.close();
    }

    /**
     * Function used when the Master Thief needs to be waiting for the thieves
     */
    private void waitThieves(){
        ConcentrationSiteMessage inMessage, outMessage;

        while(!concentrationSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.WAITTHIVES);
        concentrationSite.writeObject(outMessage);
        inMessage = (ConcentrationSiteMessage) concentrationSite.readObject();
        concentrationSite.close();
    }

    /**
     * Get number of elements in the specified group
     * @param numero_grupos id of the group
     * @return int with the number of elements
     */
    private int getNrElemGroup(int numero_grupos){
        CollectionSiteMessage inMessage, outMessage;

        while(!collectionSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new CollectionSiteMessage(CollectionSiteMessage.GETNRELEMENTSGROUP,numero_grupos);
        collectionSite.writeObject(outMessage);
        inMessage = (CollectionSiteMessage) collectionSite.readObject();
        collectionSite.close();
        return inMessage.getArg1();
    }

    /**
     * Function that calls a Thief to join a party
     * @param numero_grupos id of the group
     * @return the id of the thief
     */
    private int callThief(int numero_grupos){
        ConcentrationSiteMessage inMessage, outMessage;

        while(!concentrationSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.CALLTHIEF,numero_grupos);
        concentrationSite.writeObject(outMessage);
        inMessage = (ConcentrationSiteMessage) concentrationSite.readObject();
        concentrationSite.close();
        return inMessage.getArg1();
    }

    /**
     * Function used to place a thief inside a party
     * @param ladrao id of the thief
     * @param numero_grupos if of the group
     */
    private void joinGroup(int ladrao, int numero_grupos){
        CollectionSiteMessage inMessage, outMessage;

        while(!collectionSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new CollectionSiteMessage(CollectionSiteMessage.JOINGROUP,ladrao,numero_grupos);
        collectionSite.writeObject(outMessage);
        inMessage = (CollectionSiteMessage) collectionSite.readObject();
        collectionSite.close();
    }

    /**
     * Function used when waiting for the thieves to finish
     */
    private void waitThievesEnd(){
        ConcentrationSiteMessage inMessage, outMessage;

        while(!concentrationSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.WAITTHIEVESEND);
        concentrationSite.writeObject(outMessage);
        inMessage = (ConcentrationSiteMessage) concentrationSite.readObject();
        concentrationSite.close();
    }

    /**
     * Get all the stolen paintings
     * @return int with the value of stolen painting or no painting
     */
    private int getStolenPaintings(){
        CollectionSiteMessage inMessage, outMessage;

        while(!collectionSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new CollectionSiteMessage(CollectionSiteMessage.GETSTOLENPAINTINGS);
        collectionSite.writeObject(outMessage);
        inMessage = (CollectionSiteMessage) collectionSite.readObject();
        collectionSite.close();
        return inMessage.getArg1();
    }

    /**
     * Start the log file
     */
    private void startLog(){
        GeneralRepositoryMessage inMessage, outMessage;

        while(!generalRepository.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }

        outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.STARTLOG);
        generalRepository.writeObject(outMessage);
        inMessage = (GeneralRepositoryMessage) generalRepository.readObject();
        generalRepository.close();
    }

    /**
     * Set Master Thief State
     * @param stat state of the master thief
     */
    private void setMasterThiefState(int stat){
        GeneralRepositoryMessage inMessage, outMessage;

        while(!generalRepository.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }

        outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.SETMASTERTHIEFSTATE, stat);
        generalRepository.writeObject(outMessage);
        inMessage = (GeneralRepositoryMessage) generalRepository.readObject();
        generalRepository.close();
    }

    /**
     * End the log
     * @param quadros total of stolen paintings
     */
    private void endLog(int quadros){
        GeneralRepositoryMessage inMessage, outMessage;

        while(!generalRepository.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }

        outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.ENDLOG, quadros);
        generalRepository.writeObject(outMessage);
        inMessage = (GeneralRepositoryMessage) generalRepository.readObject();
        generalRepository.close();
    }

    /**
     * Report End of Heist
     */
    private void reportEnd(){
        GeneralRepositoryMessage inMessage, outMessage;

        while(!generalRepository.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }

        outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.END);
        generalRepository.writeObject(outMessage);
        inMessage = (GeneralRepositoryMessage) generalRepository.readObject();
        generalRepository.close();


        CollectionSiteMessage inMessage1, outMessage1;

        while(!collectionSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }

        outMessage1 = new CollectionSiteMessage(CollectionSiteMessage.END);
        collectionSite.writeObject(outMessage1);
        inMessage1 = (CollectionSiteMessage) collectionSite.readObject();
        collectionSite.close();


        ConcentrationSiteMessage inMessage2, outMessage2;

        while(!concentrationSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }

        outMessage2 = new ConcentrationSiteMessage(ConcentrationSiteMessage.END);
        concentrationSite.writeObject(outMessage2);
        inMessage2 = (ConcentrationSiteMessage) concentrationSite.readObject();
        concentrationSite.close();



    }

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

                        if (getThiefNumber() == NUM_THIEVES) {
                            startOperations();
                        }

                        break;


                    case DECIDING_WHAT_TO_DO:


                        numero_grupos = checkGroups();


                        boolean emptyMuseu = checkEmptyMuseum();

                        boolean checkSalasLivres = checkEmptyRooms();
                        if (numero_grupos != -1 && !emptyMuseu && checkSalasLivres) {
                            prepareAssaultParty(numero_grupos);
                        }
                        else {
                            emptyMuseu = checkEmptyMuseum();
                            if (emptyMuseu) {
                                sumUpResults();
                            } else {
                                takeARest();
                            }
                        }

                        break;


                    case ASSEMBLING_A_GROUP:


                        waitThieves();


                        int nrElemGrupo = getNrElemGroup(numero_grupos);

                        if (nrElemGrupo == 0) {

                            for (int i = 0; i < NUM_GROUP; i++) {

                                int ladrao = callThief(numero_grupos);
                                joinGroup(ladrao,numero_grupos);
                            }
                            takeARest();
                        }
                        break;


                    case WAITING_FOR_GROUP_ARRIVAL:
                        takeARest();
                        break;

                    case PRESENTING_THE_REPORT:
                        waitThievesEnd();

                        int nrQuadrosRoubados = getStolenPaintings();
                        endLog(nrQuadrosRoubados);
                        heistOver = true;
                        reportEnd();
                        break;
                }
            }


    }
}
