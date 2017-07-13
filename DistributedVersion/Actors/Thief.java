package DistributedVersion.Actors;

import DistributedVersion.ComInfo.ClientCom;
import DistributedVersion.Messages.AssaultPartyMessage;
import DistributedVersion.Messages.CollectionSiteMessage;
import DistributedVersion.Messages.ConcentrationSiteMessage;
import DistributedVersion.Messages.GeneralRepositoryMessage;


import static DistributedVersion.Messages.Constants.*;
import static DistributedVersion.ComInfo.ComPorts.*;

/**
 * Ordinary Thief
 *
 * @author Pedro Matos and Tiago Bastos
 */
public class Thief extends Thread {

    /**
     * General Repository
     */
    private ClientCom generalRepository;
    /**
     * Assault Party's manager
     */
    private ClientCom grupo;
    /**
     * Concentration Site
     */
    private ClientCom collectionSite;
    /**
     * CollectionSite
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
     * Thief Group
     */
    private int myGroup;
    /**
     * Thief Agility
     */
    private int agility;
    /**
     * Thief position in the group
     */
    private int pos_group;






    /**
     * @param id Thief ID
     * @param name Thief name
     * @param generalRepository General Repository
     * @param grupo Assault Party manager
     * @param collectionSite Concentration Site
     * @param concentrationSite CollectionSite
     */
    public Thief(int id, String name, String generalRepository, String grupo, String collectionSite, String concentrationSite) {

        super(name);
        this.name = name;
        this.id = id;

        this.generalRepository = new ClientCom(generalRepository, portGeneralRepo);
        this.collectionSite = new ClientCom(collectionSite, portCollectionSite);
        this.grupo = new ClientCom(grupo, portAssaultGroup);
        this.concentrationSite = new ClientCom(concentrationSite, portConcentrationSite);

        this.agility = getAgility();

        this.myGroup = -1;
        setThiefDisplacement(this.id, this.agility);
        this.pos_group = -1;
    }

    /**
     * Set the Thief Displacement
     * @param id id of thief
     * @param agility agility of the thief
     */
    private void setThiefDisplacement(int id, int agility) {
        GeneralRepositoryMessage inMessage, outMessage;

        while(!generalRepository.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.SETTHIEFDISPLACEMENT, id, agility);
        generalRepository.writeObject(outMessage);
        inMessage = (GeneralRepositoryMessage) generalRepository.readObject();
        generalRepository.close();
    }

    /**
     * Get Agility of thief
     * @return int with the agility
     */
    public int getAgility(){
        ConcentrationSiteMessage inMessage, outMessage;

        while(!concentrationSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.GETAGILITY, this.id);
        concentrationSite.writeObject(outMessage);
        inMessage = (ConcentrationSiteMessage) concentrationSite.readObject();
        concentrationSite.close();

        return inMessage.getArg1();
    }

    /**
     * Thief is ready
     * @param id of thief
     */
    public void imReady(int id){
        ConcentrationSiteMessage inMessage, outMessage;

        while(!concentrationSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.IMREADY, id);
        concentrationSite.writeObject(outMessage);
        inMessage = (ConcentrationSiteMessage) concentrationSite.readObject();
        concentrationSite.close();
    }

    /**
     * Get the state of thief
     * @param id thief id
     * @return thief state
     */
    public int getStateThief(int id){
        ConcentrationSiteMessage inMessage, outMessage;

        while(!concentrationSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.GETSTATETHIEF, id);
        concentrationSite.writeObject(outMessage);
        inMessage = (ConcentrationSiteMessage) concentrationSite.readObject();
        concentrationSite.close();

        return inMessage.getArg1();
    }

    /**
     * Function to see if Thief is busy or nort
     * @param id id of thief
     * @return true if thief is busy
     */
    public boolean getBusyThief(int id){
        ConcentrationSiteMessage inMessage, outMessage;

        while(!concentrationSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.GETBUSYTHIEF, id);
        concentrationSite.writeObject(outMessage);
        inMessage = (ConcentrationSiteMessage) concentrationSite.readObject();
        concentrationSite.close();

        return inMessage.getArg_b1();
    }

    /**
     * Function to see if thief is needed
     * @param id id of thief
     */
    public void amINeeded(int id){
        ConcentrationSiteMessage inMessage, outMessage;

        while(!concentrationSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.AMINEEDED, id);
        concentrationSite.writeObject(outMessage);
        inMessage = (ConcentrationSiteMessage) concentrationSite.readObject();
        concentrationSite.close();
    }

    /**
     * Get group of the thief
     * @param id id of thief
     * @return id of the group
     */
    public int getGroupThief(int id){
        ConcentrationSiteMessage inMessage, outMessage;

        while(!concentrationSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.GETGROUPTHIEF, id);
        concentrationSite.writeObject(outMessage);
        inMessage = (ConcentrationSiteMessage) concentrationSite.readObject();
        concentrationSite.close();

        return inMessage.getArg1();
    }

    /**
     * Get position of thief
     * @param id id of thief
     * @param group_id id of group
     * @return position of the thief
     */
    public int getPos(int id, int group_id){
        AssaultPartyMessage inMessage, outMessage;

        while(!this.grupo.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new AssaultPartyMessage(AssaultPartyMessage.GETPOS, id,group_id);
        grupo.writeObject(outMessage);
        inMessage = (AssaultPartyMessage) grupo.readObject();
        grupo.close();
        return inMessage.getArg1();
    }

    /**
     * Preparing excursion
     * @param id thief id
     */
    public void prepareExcursion(int id){
        ConcentrationSiteMessage inMessage, outMessage;

        while(!concentrationSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.PREPAREEXCURSION, id);
        concentrationSite.writeObject(outMessage);
        inMessage = (ConcentrationSiteMessage) concentrationSite.readObject();
        concentrationSite.close();
    }

    public void waitMinhaVez(int id, int group_id){
        AssaultPartyMessage inMessage, outMessage;

        while(!this.grupo.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new AssaultPartyMessage(AssaultPartyMessage.WAITMYTURN, id,group_id);
        grupo.writeObject(outMessage);
        inMessage = (AssaultPartyMessage) grupo.readObject();
        grupo.close();
    }

    public int crawlIn(int id, int agilidade, int meuGrupo, int pos_grupo){
        AssaultPartyMessage inMessage, outMessage;

        while(!this.grupo.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new AssaultPartyMessage(AssaultPartyMessage.CRAWLIN, id,agilidade,meuGrupo,pos_grupo);
        grupo.writeObject(outMessage);
        inMessage = (AssaultPartyMessage) grupo.readObject();
        grupo.close();

        return inMessage.getArg1();
    }

    public int getDistanciaSala(int group_id){
        AssaultPartyMessage inMessage, outMessage;

        while(!this.grupo.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new AssaultPartyMessage(AssaultPartyMessage.GETROOMDISTANCE, group_id);
        grupo.writeObject(outMessage);
        inMessage = (AssaultPartyMessage) grupo.readObject();
        grupo.close();

        return inMessage.getArg1();
    }

    public void naSala(int id){
        ConcentrationSiteMessage inMessage, outMessage;

        while(!concentrationSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.NASALA, id);
        concentrationSite.writeObject(outMessage);
        inMessage = (ConcentrationSiteMessage) concentrationSite.readObject();
        concentrationSite.close();
    }

    public boolean rollACanvas(int meuGrupo){
        AssaultPartyMessage inMessage, outMessage;

        while(!this.grupo.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new AssaultPartyMessage(AssaultPartyMessage.STEALPAINTING, meuGrupo);
        grupo.writeObject(outMessage);
        inMessage = (AssaultPartyMessage) grupo.readObject();
        grupo.close();

        return inMessage.getArg_b1();
    }

    public int getSalaAssalto(int meuGrupo){
        CollectionSiteMessage inMessage, outMessage;

        while(!this.collectionSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new CollectionSiteMessage(CollectionSiteMessage.GETASSAULTINGROOM, meuGrupo);
        collectionSite.writeObject(outMessage);
        inMessage = (CollectionSiteMessage) collectionSite.readObject();
        collectionSite.close();

        return inMessage.getArg1();

    }

    public void reverseDirection(int id){
        ConcentrationSiteMessage inMessage, outMessage;

        while(!concentrationSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.REVERSEDIRECTION, id);
        concentrationSite.writeObject(outMessage);
        inMessage = (ConcentrationSiteMessage) concentrationSite.readObject();
        concentrationSite.close();
    }

    public int crawlOut(int id, int agilidade, int meuGrupo, int pos_grupo){
        AssaultPartyMessage inMessage, outMessage;

        while(!this.grupo.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new AssaultPartyMessage(AssaultPartyMessage.CRAWLOUT, id,agilidade,meuGrupo,pos_grupo);
        grupo.writeObject(outMessage);
        inMessage = (AssaultPartyMessage) grupo.readObject();
        grupo.close();

        return inMessage.getArg1();
    }

    public int getPosGrupo(int id, int meuGrupo){
        CollectionSiteMessage inMessage, outMessage;

        while(!this.collectionSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new CollectionSiteMessage(CollectionSiteMessage.GETGROUPPOSITION,id, meuGrupo);
        collectionSite.writeObject(outMessage);
        inMessage = (CollectionSiteMessage) collectionSite.readObject();
        collectionSite.close();

        return inMessage.getArg1();
    }

    public void handACanvas(int id, int tmp_salaassalto, int meuGrupo, int tmp_getPosGrupo){
        CollectionSiteMessage inMessage, outMessage;

        while(!this.collectionSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new CollectionSiteMessage(CollectionSiteMessage.HANDACANVAS, id,tmp_salaassalto,meuGrupo,tmp_getPosGrupo);
        collectionSite.writeObject(outMessage);
        inMessage = (CollectionSiteMessage) collectionSite.readObject();
        collectionSite.close();

    }

    public void indicarSalaVazia(int tmp_salaassalto, int meuGrupo, int tmp_getPosGrupo){
        CollectionSiteMessage inMessage, outMessage;

        while(!this.collectionSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new CollectionSiteMessage(CollectionSiteMessage.FLAGEMPTYROOM,tmp_salaassalto,meuGrupo,tmp_getPosGrupo);
        collectionSite.writeObject(outMessage);
        inMessage = (CollectionSiteMessage) collectionSite.readObject();
        collectionSite.close();

    }

    public void indicarChegada(int id){
        ConcentrationSiteMessage inMessage, outMessage;

        while(!concentrationSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.INDICARCHEGADA, id);
        concentrationSite.writeObject(outMessage);
        inMessage = (ConcentrationSiteMessage) concentrationSite.readObject();
        concentrationSite.close();
    }

    private void setThiefState(int id, int stat) {
        GeneralRepositoryMessage inMessage, outMessage;

        while(!generalRepository.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.SETTHIEFSTATE, id, stat);
        generalRepository.writeObject(outMessage);
        inMessage = (GeneralRepositoryMessage) generalRepository.readObject();
        generalRepository.close();

    }

    private void setAP1_canvas(int pos_grupo, boolean quadro, int room) {
        GeneralRepositoryMessage inMessage, outMessage;

        while(!generalRepository.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.SETAP1CANVAS, pos_grupo, quadro, room);
        generalRepository.writeObject(outMessage);
        inMessage = (GeneralRepositoryMessage) generalRepository.readObject();
        generalRepository.close();
    }

    private void setAP2_canvas(int pos_grupo, boolean quadro, int room) {
        GeneralRepositoryMessage inMessage, outMessage;

        while(!generalRepository.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.SETAP2CANVAS, pos_grupo, quadro, room);
        generalRepository.writeObject(outMessage);
        inMessage = (GeneralRepositoryMessage) generalRepository.readObject();
        generalRepository.close();
    }

    private void setAP1_pos(int pos_grupo, int posicao) {
        GeneralRepositoryMessage inMessage, outMessage;

        while(!generalRepository.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.SETAP1POS, pos_grupo, posicao);
        generalRepository.writeObject(outMessage);
        inMessage = (GeneralRepositoryMessage) generalRepository.readObject();
        generalRepository.close();
    }

    private void setAP2_pos(int pos_grupo, int posicao) {
        GeneralRepositoryMessage inMessage, outMessage;

        while(!generalRepository.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.SETAP2POS, pos_grupo, posicao);
        generalRepository.writeObject(outMessage);
        inMessage = (GeneralRepositoryMessage) generalRepository.readObject();
        generalRepository.close();
    }

    private void setAP1_reset(int pos_grupo, int id) {
        GeneralRepositoryMessage inMessage, outMessage;

        while(!generalRepository.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.SETAP1RESET, pos_grupo, id);
        generalRepository.writeObject(outMessage);
        inMessage = (GeneralRepositoryMessage) generalRepository.readObject();
        generalRepository.close();
    }

    private void setAP2_reset(int pos_grupo, int id) {
        GeneralRepositoryMessage inMessage, outMessage;

        while(!generalRepository.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.SETAP2RESET, pos_grupo, id);
        generalRepository.writeObject(outMessage);
        inMessage = (GeneralRepositoryMessage) generalRepository.readObject();
        generalRepository.close();
    }

    @Override
    public void run() {

        int posicao = 0;

        imReady(this.id);

        boolean quadro = false;
        boolean heistOver = false;

        while (!heistOver) {
            int stat = getStateThief(this.id);

            switch (stat) {

                case OUTSIDE:
                    setThiefState(this.id, stat);

                    boolean getBusyLadrao = getBusyThief(id);

                    if (!getBusyLadrao && this.myGroup == -1) {
                        amINeeded(this.id);
                    } else {
                        //obter o grupo
                        this.myGroup = getGroupThief(id);
                        //obter a posicao no grupo
                        this.pos_group = getPos(this.id, this.myGroup);

                        if(this.myGroup != -1 && this.pos_group != -1){
                            prepareExcursion(this.id);
                        }
                    }
                    break;


                case CRAWLING_INWARDS:

                    waitMinhaVez(id, this.myGroup);
                    posicao = crawlIn(this.id, agility, myGroup, this.pos_group);
                    int getDistanciaSala = getDistanciaSala(myGroup);

                    if (posicao == getDistanciaSala) {
                        naSala(id);
                    }
                    break;

                case AT_A_ROOM:

                    quadro = rollACanvas(myGroup);

                    int room = getSalaAssalto(this.myGroup);

                    if (this.myGroup == 0) {
                        setAP1_canvas(this.pos_group, quadro, room);
                    } else if (this.myGroup == 1) {
                        setAP2_canvas(this.pos_group, quadro, room);
                    }
                    reverseDirection(id);
                    break;

                case CRAWLING_OUTWARDS:

                    if (posicao != 0) {
                        waitMinhaVez(id, this.myGroup);
                        posicao = crawlOut(this.id, this.agility, this.myGroup, this.pos_group);
                    }

                    if (this.myGroup == 0) {
                        setAP1_pos(this.pos_group, posicao);
                    } else if (this.myGroup == 1) {
                        setAP2_pos(this.pos_group, posicao);
                    }

                    if (posicao == 0) {
                        if (quadro) {

                            int tmp_salaassalto = getSalaAssalto(myGroup);
                            int tmp_getPosGrupo = getPosGrupo(id, myGroup);
                            handACanvas(id, tmp_salaassalto, this.myGroup, tmp_getPosGrupo);

                        } else {

                            int tmp_salaassalto = getSalaAssalto(myGroup);
                            int tmp_getPosGrupo = getPosGrupo(id, myGroup);
                            indicarSalaVazia(tmp_salaassalto, this.myGroup, tmp_getPosGrupo);
                        }

                        if (this.myGroup == 0) setAP1_reset(this.pos_group, this.id);
                        else if (this.myGroup == 1) setAP2_reset(this.pos_group, this.id);

                        this.myGroup = -1;
                        indicarChegada(id);
                    }

                    break;

                case HEIST_END:

                    heistOver = true;
                    end();
                    break;
            }
        }
    }

    private void end(){
        AssaultPartyMessage inMessage, outMessage;

        while(!grupo.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new AssaultPartyMessage(AssaultPartyMessage.END);
        grupo.writeObject(outMessage);
        inMessage = (AssaultPartyMessage) grupo.readObject();
        grupo.close();
    }
}
