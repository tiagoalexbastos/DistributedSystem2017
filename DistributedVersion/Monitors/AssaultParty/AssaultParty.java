package DistributedVersion.Monitors.AssaultParty;

import static DistributedVersion.Messages.Constants.*;
import static DistributedVersion.Messages.Constants.DIST_THIEVES;

import DistributedVersion.Messages.GeneralRepositoryMessage;
import DistributedVersion.ComInfo.ClientCom;
import DistributedVersion.Messages.MuseumMessage;

import java.util.Arrays;

/**
 * Assault Party
 * @author Pedro Matos and Tiago Bastos
 */
public class AssaultParty {

    /**
     * Museum
     */
    ClientCom museum;

    /**
     * General Repository
     */
    ClientCom gen;
    /**
     * Thief's positions
     */
    private int[][] position = new int[2][NUM_GROUP];  // 1- linha position 2- linha IDLadrao
    /**
     * Next thief to crawl
     */
    private boolean[] myTurn = new boolean[NUM_GROUP];
    /**
     * Number of thiefs at the room
     */
    private int atARoom;
    /**
     * Number of thiefs in the CollectionSite
     */
    private int atConcentrationSite;
    /**
     * Number of thiefs in the assault party
     */
    private int num_of_elements;
    /**
     * Thiefs in the room
     */
    private boolean[] at_a_room = new boolean[NUM_GROUP];
    /**
     * Thiefs in the CollectionSite
     */
    private boolean[] im_back = new boolean[NUM_GROUP];
    /**
     * Room id
     */
    private int room_number;
    /**
     * Room distance
     */
    private int roomDistance = -1;

    /**
     * Group id
     */
    private int id = -1;

    /**
     *
     * @param museum  Museum
     * @param room_number room id
     * @param id thief id
     * @param general General Repository
     */
    public AssaultParty(String museum, int room_number, int id, String general) {

        this.gen = new ClientCom(general,22460);
        this.museum = new ClientCom(museum, 22461);
        this.id = id;
        this.num_of_elements = 0;
        this.atARoom = 0;
        this.atConcentrationSite = 0;
        this.room_number = room_number;

        for (int i = 0; i < NUM_GROUP; i++) {
            this.position[0][i] = 0;
            this.position[1][i] = -1;
            this.myTurn[i] = false;
            this.at_a_room[i] = false;
            this.im_back[i] = false;
        }
    }

    /**
     * Enter the assault party
     * @param ladraoID thief id
     * @param pos_grupo group position
     */
    public synchronized void joinParty(int ladraoID, int pos_grupo) {

        this.position[1][num_of_elements] = ladraoID;
        int realID = ladraoID+1;
        if (num_of_elements == NUM_GROUP - 1) {
            this.myTurn[num_of_elements] = true;
        }

        if (id == 0) {
            setAP1_pos_id_canvas(pos_grupo, ladraoID, 0, false);

        } else if (id == 1) {
            setAP2_pos_id_canvas(pos_grupo, ladraoID, 0, false);
        }

        this.num_of_elements++;
    }


    /**
     * Function that updates the Thief current situation (ID, position & Canvas)
     * @param pos_grupo Thief position in group
     * @param ladraoID thief ID
     * @param pos thief new position
     * @param cv canvas flag
     */
    private void setAP1_pos_id_canvas(int pos_grupo, int ladraoID, int pos, boolean cv) {
        GeneralRepositoryMessage inMessage, outMessage;

        while(!gen.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.SETAP1POSIDCANVAS, pos_grupo, ladraoID, pos, cv);
        gen.writeObject(outMessage);
        inMessage = (GeneralRepositoryMessage) gen.readObject();
        gen.close();
    }

    /**
     * Function that updates the Thief current situation (ID, position & Canvas)
     * @param pos_grupo Thief position in group
     * @param ladraoID thief ID
     * @param pos thief new position
     * @param cv canvas flag
     */
    private void setAP2_pos_id_canvas(int pos_grupo, int ladraoID, int pos, boolean cv) {
        GeneralRepositoryMessage inMessage, outMessage;

        while(!gen.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.SETAP2POSIDCANVAS, pos_grupo, ladraoID, pos, cv);
        gen.writeObject(outMessage);
        inMessage = (GeneralRepositoryMessage) gen.readObject();
        gen.close();
    }

    /**
     * Function that returns the Thief position in the party
     * @param ladraoID thief ID
     * @return thief position
     */
    public synchronized int getPos(int ladraoID){


        for(int i = 0; i< num_of_elements; i++){
            if(this.position[1][i] == ladraoID){
                return i;
            }
        }
        return -1;
    }

    /**
     * Function that set's the Ordinary Thief State
     * @param ladraoID Ordinary Thief ID
     * @param state Ordinary Thief State
     */
    private void setThiefState(int ladraoID, int state){
        GeneralRepositoryMessage inMessage, outMessage;

        while(!gen.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.SETTHIEFSTATE, ladraoID, state);
        gen.writeObject(outMessage);
        inMessage = (GeneralRepositoryMessage) gen.readObject();
        gen.close();
    }

    /**
     * Function that set's the thief position
     * @param pos_grupo thief position in the group
     * @param distanciaSala room distance
     */
    private void setAP1_pos(int pos_grupo, int distanciaSala) {
        GeneralRepositoryMessage inMessage, outMessage;

        while(!gen.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.SETAP1POS, pos_grupo, distanciaSala);
        gen.writeObject(outMessage);
        inMessage = (GeneralRepositoryMessage) gen.readObject();
        gen.close();
    }

    /**
     * Function that set's the thief position
     * @param pos_grupo thief position in the group
     * @param distanciaSala room distance
     */
    private void setAP2_pos(int pos_grupo, int distanciaSala) {
        GeneralRepositoryMessage inMessage, outMessage;

        while(!gen.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.SETAP2POS, pos_grupo, distanciaSala);
        gen.writeObject(outMessage);
        inMessage = (GeneralRepositoryMessage) gen.readObject();
        gen.close();
    }

    /**
     * crawl in
     * @param ladraoID thief id
     * @param agilidade thief agility
     * @param idGrupo group id
     * @param posgrupo position in the group
     * @return final position
     */
    public synchronized int crawlIn(int ladraoID, int agilidade, int idGrupo, int posgrupo) {


        int indiceNoGrupo = this.getPosition(ladraoID);

        int getDistanciaSala = this.getRoomDistance();


        if (this.atARoom != num_of_elements) {

            if (this.myTurn[indiceNoGrupo]) {

                setThiefState(ladraoID,CRAWLING_INWARDS);

                boolean repetir = true;
                int[] partnersPos = new int[NUM_GROUP - 1];
                do {
                    int myPosition = 0;

                    int i = 0;
                    for (int k = 0; k < NUM_GROUP; k++) {

                        if (position[1][k] != ladraoID) {
                            partnersPos[i++] = position[0][k];
                        } else {
                            myPosition = position[0][k];
                        }

                    }
                    Arrays.sort(partnersPos);
                    for (i = agilidade; i > 0; i--) {
                        boolean tooFarOrOcupada = false;

                        int minha = myPosition + i;
                        int first = partnersPos[1];
                        int second = partnersPos[0];




                        if ((minha - first > DIST_THIEVES) || (minha - first == 0 && (minha != 0) && minha != getDistanciaSala)) {
                            tooFarOrOcupada = true;
                        }

                        if (minha == second) {
                            tooFarOrOcupada = true;
                        }

                        if (!tooFarOrOcupada) {
                            if (myPosition + i >= getDistanciaSala) {
                                position[0][indiceNoGrupo] = getDistanciaSala;
                                atARoom++;
                                at_a_room[indiceNoGrupo] = true;
                                repetir = false;
                                if(idGrupo == 0){
                                    setAP1_pos(posgrupo,getDistanciaSala);
                                    setThiefState(ladraoID,AT_A_ROOM );
                                }
                                else if(idGrupo== 1){
                                    setAP2_pos(posgrupo,getDistanciaSala);
                                    setThiefState(ladraoID,AT_A_ROOM );
                                }
                            } else {
                                position[0][indiceNoGrupo] = myPosition + i;
                                if(idGrupo == 0){
                                    setAP1_pos(posgrupo,myPosition + i);
                                }
                                else if(idGrupo== 1){
                                    setAP2_pos(posgrupo,myPosition + i);
                                }
                            }

                            break;
                        } else {
                            if (i == 1) repetir = false;
                        }
                    }
                } while (repetir);

                this.myTurn[indiceNoGrupo] = false;
                int j = 0;
                for (j = 0; j < partnersPos.length; j++) {
                    if (partnersPos[j] > position[0][indiceNoGrupo]) ;
                    {
                        break;
                    }
                }
                for (int k = 0; k < num_of_elements; k++) {
                    if (position[0][k] == partnersPos[j]) {
                        this.myTurn[k] = true;
                    }
                }


            }
        }
        notifyAll();

        return this.position[0][indiceNoGrupo];

    }

    /**
     * Crawl out
     * @param ladraoID thief id
     * @param agilidade thief agility
     * @param idGrupo group id
     * @param posgrupo position in the group
     * @return final position
     */
    public synchronized int crawlOut(int ladraoID, int agilidade, int idGrupo, int posgrupo ){

        int indiceNoGrupo = this.getPosition(ladraoID);

        int getDistanciaSala = this.getRoomDistance();

        if(atARoom == num_of_elements){
            if (this.myTurn[indiceNoGrupo]) {

                boolean repetir = true;
                int[] partnersPos = new int[NUM_GROUP - 1];
                do{
                    int myPosition = -1;

                    int i = 0;
                    for (int k = 0; k < NUM_GROUP; k++) {

                        if (position[1][k] != ladraoID) {
                            partnersPos[i++] = position[0][k];
                        } else {
                            myPosition = position[0][k];
                        }

                    }

                    if(myPosition == getDistanciaSala) setThiefState(ladraoID,CRAWLING_OUTWARDS );

                    Arrays.sort(partnersPos);
                    for (i = agilidade; i > 0; i--) {
                        boolean tooFarOrOcupada = false;

                        int minha = myPosition - i;
                        int first = partnersPos[0];
                        int second = partnersPos[1];


                        if ((first - minha   > DIST_THIEVES) || (first-minha == 0 && (minha != getDistanciaSala))) {
                            tooFarOrOcupada = true;
                        }

                        if (minha == second) {
                            tooFarOrOcupada = true;
                        }


                        if ((!tooFarOrOcupada)) {
                            if (myPosition - i <= 0) {
                                position[0][indiceNoGrupo] = 0;
                                atConcentrationSite++;
                                im_back[indiceNoGrupo] = true;
                                repetir = false;
                                if(idGrupo == 0){
                                    setAP1_pos(posgrupo,0);
                                }
                                else if(idGrupo== 1){
                                    setAP2_pos(posgrupo,0);
                                }
                                setThiefState(ladraoID, OUTSIDE);
                            } else {
                                position[0][indiceNoGrupo] = myPosition - i;
                                if(idGrupo == 0){
                                    setAP1_pos(posgrupo,myPosition - i);
                                }
                                else if(idGrupo== 1){
                                    setAP2_pos(posgrupo,myPosition - i);
                                }
                            }
                            break;
                        }
                        else {
                            if (i == 1) repetir = false;
                        }
                    }
                }while(repetir);

                this.myTurn[indiceNoGrupo] = false;
                int j = 0;
                for (j = partnersPos.length - 1; j >= 0; j--) {
                    if (partnersPos[j] < position[0][indiceNoGrupo]);
                    {
                        break;
                    }

                }
                for (int k = 0; k < num_of_elements; k++) {
                    if (position[0][k] == partnersPos[j]) {
                        this.myTurn[k] = true;
                    }
                }
            }
        }
        notifyAll();
        return this.position[0][indiceNoGrupo];
    }

    /**
     * Get position
     * @param ladraoID thief id
     * @return position
     */
    public synchronized int getPosition(int ladraoID) {
        int j = -1;

        for (int i = 0; i < NUM_GROUP; i++) {
            if (this.position[1][i] == ladraoID) {
                j = i;
                break;
            }
        }
        return j;
    }

    /**
     * Get distance
     * @return distance
     */
    public int getRoomDistance(){

        if (roomDistance == -1) {
            MuseumMessage inMessage, outMessage;


            while(!museum.open()){
                try{
                    Thread.sleep((long)(1000));
                }
                catch (InterruptedException e){
                }
            }
            outMessage = new MuseumMessage(MuseumMessage.GETDISTANCE, room_number);
            museum.writeObject(outMessage);
            inMessage = (MuseumMessage) museum.readObject();
            museum.close();

            roomDistance = inMessage.getDistanciaSala();
        }
        return roomDistance;
    }

    /**
     * Steals paiting
     *
     * @return true if success
     */
    public synchronized boolean stealPainting(){
        MuseumMessage inMessage, outMessage;

        while(!museum.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new MuseumMessage(MuseumMessage.STEALPAINTING, room_number);
        museum.writeObject(outMessage);
        inMessage = (MuseumMessage) museum.readObject();
        museum.close();


        return inMessage.getQuadroRoubado();

    }

    /**
     * Waiting for it's turn
     * @param id thief id
     */
    public synchronized void waitMyTurn(int id) {
        notifyAll();
        int pos = this.getPosition(id);
        while (!myTurn[pos]) {

            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
