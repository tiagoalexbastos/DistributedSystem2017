package ConcorrentVersion.Monitors.AssaultParty;

import static ConcorrentVersion.ProblemInformation.Constantes.*;
import static ConcorrentVersion.ProblemInformation.Constantes.DIST_THIEVES;
import ConcorrentVersion.ProblemInformation.GeneralRepository;
import ConcorrentVersion.Monitors.Museum.Museum;

import java.util.Arrays;

/**
 * Assault Party
 * @author Pedro Matos and Tiago Bastos
 */
public class AssaultParty {

    /**
     * Museum
     */
    Museum museum;

    /**
     * General Repository
     */
    GeneralRepository gen;
    /**
     * Thief's positions
     */
    private int[][] posicao = new int[2][NUM_GROUP];  // 1- linha posicao 2- linha IDLadrao
    /**
     * Next thief to crawl
     */
    private boolean[] minhaVez = new boolean[NUM_GROUP];
    /**
     * Number of thiefs at the room
     */
    private int naSala;
    /**
     * Number of thiefs in the CollectionSite
     */
    private int noTerreiro;
    /**
     * Number of thiefs in the assault party
     */
    private int nrElementos;
    /**
     * Thiefs in the room
     */
    private boolean[] cheguei = new boolean[NUM_GROUP];
    /**
     * Thiefs in the CollectionSite
     */
    private boolean[] voltei = new boolean[NUM_GROUP];
    /**
     * Room id
     */
    private int nrSala;
    /**
     * Room distance
     */
    private int distanciaSala = -1;

    /**
     * Group id
     */
    private int id = -1;

    /**
     *
     * @param museum  Museum
     * @param nrSala room id
     * @param id thief id
     * @param general General Repository
     */
    public AssaultParty(Museum museum, int nrSala, int id, GeneralRepository general) {

        this.gen = general;
        this.museum = museum;
        this.id = id;
        this.nrElementos = 0;
        this.naSala = 0;
        this.noTerreiro = 0;
        this.nrSala = nrSala;

        for (int i = 0; i < NUM_GROUP; i++) {
            this.posicao[0][i] = 0;
            this.posicao[1][i] = -1;
            this.minhaVez[i] = false;
            this.cheguei[i] = false;
            this.voltei[i] = false;
        }
    }

    /**
     * Enter the assault party
     * @param ladraoID thief id
     * @param pos_grupo group position
     */
    public synchronized void entrar(int ladraoID, int pos_grupo) {
        this.posicao[1][nrElementos] = ladraoID;
        int realID = ladraoID+1;
        if (nrElementos == NUM_GROUP - 1) {
            this.minhaVez[nrElementos] = true;
        }

        if (id == 0) {
            gen.setAP1_pos_id_canvas(pos_grupo, ladraoID, 0, false);

        } else if (id == 1) {
            gen.setAP2_pos_id_canvas(pos_grupo, ladraoID, 0, false);
        }

        this.nrElementos++;
    }

    public synchronized int getPos(int ladraoID){


        for(int i = 0; i<nrElementos; i++){
            if(this.posicao[1][i] == ladraoID){
                return i;
            }
        }
        return -1;
    }

    /**
     * crawl in
     * @param ladraoID thief id
     * @param agilidade thief agility
     * @param idGrupo group id
     * @param posgrupo position in the group
     * @return final position
     */
    public synchronized int rastejarIn(int ladraoID, int agilidade, int idGrupo, int posgrupo) {


        int indiceNoGrupo = this.getPosicao(ladraoID);

        int getDistanciaSala = this.getDistanciaSala();


        if (this.naSala != nrElementos) {

            if (this.minhaVez[indiceNoGrupo]) {

                gen.setThiefState(ladraoID,CRAWLING_INWARDS);

                boolean repetir = true;
                int[] partnersPos = new int[NUM_GROUP - 1];
                do {
                    int myPosition = 0;

                    int i = 0;
                    for (int k = 0; k < NUM_GROUP; k++) {

                        if (posicao[1][k] != ladraoID) {
                            partnersPos[i++] = posicao[0][k];
                        } else {
                            myPosition = posicao[0][k];
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
                                posicao[0][indiceNoGrupo] = getDistanciaSala;
                                naSala++;
                                cheguei[indiceNoGrupo] = true;
                                repetir = false;
                                if(idGrupo == 0){
                                    gen.setAP1_pos(posgrupo,getDistanciaSala);
                                    gen.setThiefState(ladraoID,AT_A_ROOM );
                                }
                                else if(idGrupo== 1){
                                    gen.setAP2_pos(posgrupo,getDistanciaSala);
                                    gen.setThiefState(ladraoID,AT_A_ROOM );
                                }
                            } else {
                                posicao[0][indiceNoGrupo] = myPosition + i;
                                if(idGrupo == 0){
                                    gen.setAP1_pos(posgrupo,myPosition + i);
                                }
                                else if(idGrupo== 1){
                                    gen.setAP2_pos(posgrupo,myPosition + i);
                                }
                            }

                            break;
                        } else {
                            if (i == 1) repetir = false;
                        }
                    }
                } while (repetir);

                this.minhaVez[indiceNoGrupo] = false;
                int j = 0;
                for (j = 0; j < partnersPos.length; j++) {
                    if (partnersPos[j] > posicao[0][indiceNoGrupo]) ;
                    {
                        break;
                    }
                }
                for (int k = 0; k < nrElementos; k++) {
                    if (posicao[0][k] == partnersPos[j]) {
                        this.minhaVez[k] = true;
                    }
                }


            }
        }
        notifyAll();

        return this.posicao[0][indiceNoGrupo];

    }

    /**
     * Crawl out
     * @param ladraoID thief id
     * @param agilidade thief agility
     * @param idGrupo group id
     * @param posgrupo position in the group
     * @return final position
     */
    public synchronized int rastejarOut(int ladraoID, int agilidade, int idGrupo, int posgrupo ){

        int indiceNoGrupo = this.getPosicao(ladraoID);

        int getDistanciaSala = this.getDistanciaSala();

        if(naSala ==nrElementos){
            if (this.minhaVez[indiceNoGrupo]) {

                boolean repetir = true;
                int[] partnersPos = new int[NUM_GROUP - 1];
                do{
                    int myPosition = -1;

                    int i = 0;
                    for (int k = 0; k < NUM_GROUP; k++) {

                        if (posicao[1][k] != ladraoID) {
                            partnersPos[i++] = posicao[0][k];
                        } else {
                            myPosition = posicao[0][k];
                        }

                    }

                    if(myPosition == getDistanciaSala) gen.setThiefState(ladraoID,CRAWLING_OUTWARDS );

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
                                posicao[0][indiceNoGrupo] = 0;
                                noTerreiro++;
                                voltei[indiceNoGrupo] = true;
                                repetir = false;
                                if(idGrupo == 0){
                                    gen.setAP1_pos(posgrupo,0);
                                }
                                else if(idGrupo== 1){
                                    gen.setAP2_pos(posgrupo,0);
                                }
                                gen.setThiefState(ladraoID, OUTSIDE);
                            } else {
                                posicao[0][indiceNoGrupo] = myPosition - i;
                                if(idGrupo == 0){
                                    gen.setAP1_pos(posgrupo,myPosition - i);
                                }
                                else if(idGrupo== 1){
                                    gen.setAP2_pos(posgrupo,myPosition - i);
                                }
                            }
                            break;
                        }
                        else {
                            if (i == 1) repetir = false;
                        }
                    }
                }while(repetir);

                this.minhaVez[indiceNoGrupo] = false;
                int j = 0;
                for (j = partnersPos.length - 1; j >= 0; j--) {
                    if (partnersPos[j] < posicao[0][indiceNoGrupo]);
                    {
                        break;
                    }

                }
                for (int k = 0; k < nrElementos; k++) {
                    if (posicao[0][k] == partnersPos[j]) {
                        this.minhaVez[k] = true;
                    }
                }
            }
        }
        notifyAll();
        return this.posicao[0][indiceNoGrupo];
    }

    /**
     * Get position
     * @param ladraoID thief id
     * @return position
     */
    public synchronized int getPosicao(int ladraoID) {
        int j = -1;

        for (int i = 0; i < NUM_GROUP; i++) {
            if (this.posicao[1][i] == ladraoID) {
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
    public int getDistanciaSala(){

        if (distanciaSala == -1) {
            distanciaSala = museum.getDistancia(nrSala);
        }
        return distanciaSala;
    }

    /**
     * Steals paiting
     *
     * @return true if success
     */
    public synchronized boolean roubarQuadro(){


        return museum.roubarQuadro(nrSala);

    }

    /**
     * Waiting for it's turn
     * @param id thief id
     */
    public synchronized void waitMinhaVez(int id) {
        notifyAll();
        int pos = this.getPosicao(id);
        while (!minhaVez[pos]) {

            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
