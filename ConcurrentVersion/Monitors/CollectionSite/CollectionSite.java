package ConcorrentVersion.Monitors.CollectionSite;

import ConcorrentVersion.ProblemInformation.GeneralRepository;
import ConcorrentVersion.Monitors.ConcentrationSite.ConcentrationSite;
import ConcorrentVersion.Monitors.AssaultParty.AssaultPartyManager;
import ConcorrentVersion.Monitors.Museum.Museum;
import static ConcorrentVersion.ProblemInformation.Constantes.*;

/**
 * Monitor Collection Site
 *
 * @author Pedro Matos and Tiago Bastos
 */
public class CollectionSite {
    /**
     * CollectionSite
     */
    private final ConcentrationSite concentrationSite;
    /**
     * Museum
     */
    private final Museum museum;
    /**
     * Assault partys manager
     */
    private final AssaultPartyManager gestorGrupos;
    /**
     * Master Thief state
     */
    private int estadoChefe;
    /**
     * Empty rooms
     */
    private boolean[] salaVazia = new boolean[NUM_ROOMS];
    /**
     * Assault Partys
     */
    private int[][] grupos = new int[2][NUM_GROUP];
    /**
     * Nr of elements in the groups
     */
    private int[] nrElemGrupo = new int[2];
    /**
     * Total of stollen paintings
     */
    private int quadrosRoubados;
    /**
     * Rooms occupied
     */
    private int[] salaAssalto = new int[NUM_ROOMS];

    /**
     * Groups occupied
     */
    private boolean grupoOcup[] = new boolean[2];

    /**
     * General Repository
     */
    private GeneralRepository general;

    /**
     * Number of groups
     */
    int countGrupos = 0;

    /**
     *  @param museum Museum
     * @param concentrationSite CollectionSite
     * @param gestorGrupos Manager of assault partys
     * @param generalRepository General Repository
     */
    public CollectionSite(Museum museum, ConcentrationSite concentrationSite, AssaultPartyManager gestorGrupos, GeneralRepository generalRepository) {
        this.concentrationSite = concentrationSite;
        this.museum = museum;
        this.gestorGrupos = gestorGrupos;

        // Estado inicial do chefe
        estadoChefe = PLANNING_THE_HEIST;


        nrElemGrupo[0] = 0;
        nrElemGrupo[1] = 0;
        countGrupos = 0;
        grupoOcup[0] = false;
        grupoOcup[1] = false;

        this.general = generalRepository;

        for (int i = 0; i < NUM_GROUP; i++) {
            grupos[0][i] = -1;
            grupos[1][i] = -1;
        }

        for (int i = 0; i < NUM_ROOMS; i++) {
            this.salaVazia[i] = false;
            this.salaAssalto[i] = -1;
        }

        this.quadrosRoubados = 0;

    }

    /**
     * Checks if the groups are done
     *
     * @return -1 if they are or 0/1 if they aren't.
     */
    public synchronized int checkGrupos() {

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
    public synchronized int entrarGrupo(int ladraoID, int grupo) {


        boolean cond = this.grupoCheio(grupo);

        if (!cond) {
            for (int i = 0; i < NUM_GROUP; i++) {
                if (this.grupos[grupo][i] == -1) {
                    this.nrElemGrupo[grupo]++;
                    this.grupos[grupo][i] = ladraoID;
                    //break;
                    this.gestorGrupos.entrar(ladraoID,grupo,i);
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
    public synchronized boolean grupoCheio(int grupoID) {

        if (this.nrElemGrupo[grupoID] != NUM_GROUP) {
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
    public synchronized int getEstadoChefe() {
        return this.estadoChefe;
    }

    /**
     * Starts the assault
     */
    public synchronized void startOperations() {

        this.estadoChefe = DECIDING_WHAT_TO_DO;
        general.setMasterThiefState(this.estadoChefe);
    }

    /**
     * Creates assault party
     * @param idGrupo if of party
     * @return true if formed, false if not.
     */
    public synchronized boolean prepareAssaultParty(int idGrupo){


        this.grupoOcup[idGrupo] = true;
        this.countGrupos++;
        this.estadoChefe = ASSEMBLING_A_GROUP;
        //general.setMasterThiefState(this.estadoChefe);

        int j;
        for (j = 0; j < NUM_ROOMS; j++) {

            if (this.salaAssalto[j] == -1 && !this.salaVazia[j]) {
                this.salaAssalto[j] = idGrupo;
                break;
            }
        }

        if(idGrupo == 0) general.setAssaultParty1_room(j);
        if(idGrupo == 1) general.setAssaultParty2_room(j);


        boolean aux = gestorGrupos.formarGrupo(idGrupo, j);


        return aux;
    }


    /**
     * Master is resting
     */
    public synchronized void takeARest(){



        int nrLadroes = concentrationSite.getNrLadroes();

        int checkGrupos = this.checkGrupos();

        if (checkGrupos == -1 || nrLadroes < NUM_GROUP) {
            if (this.estadoChefe == ASSEMBLING_A_GROUP) {
                this.estadoChefe = WAITING_FOR_GROUP_ARRIVAL;
                general.setMasterThiefState(this.estadoChefe);
                return;
            }
            this.estadoChefe = WAITING_FOR_GROUP_ARRIVAL;
            general.setMasterThiefState(this.estadoChefe);

            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        } else {
            this.estadoChefe = DECIDING_WHAT_TO_DO;
            general.setMasterThiefState(this.estadoChefe);

        }
    }

    /**
     * Returns the number of elements in the group
     * @param grupoID id of group
     * @return number of elements
     */
    public synchronized int getNrElemGrupo(int grupoID) {
        return this.nrElemGrupo[grupoID];
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


        this.quadrosRoubados++;
        this.grupos[grupo][pos] = -1;
        if (--this.nrElemGrupo[grupo] == 0) {
            this.salaAssalto[sala] = -1;
            this.gestorGrupos.desfazerGrupo(grupo);
            this.grupoOcup[grupo] = false;
            if (this.estadoChefe == WAITING_FOR_GROUP_ARRIVAL) {
                this.estadoChefe = DECIDING_WHAT_TO_DO;
                general.setMasterThiefState(this.estadoChefe);
            }
            notifyAll();
        }

        if(grupo == 0){
            general.setAP1_reset(pos,ladraoID);
        }
        else if (grupo == 1){
            general.setAP2_reset(pos,ladraoID);
        }

    }

    /**
     * Room is empty
     *
     * @param sala room id
     * @param grupo group id
     * @param pos position in the group
     */
    public synchronized void indicarSalaVazia(int sala, int grupo, int pos){


        this.salaVazia[sala] = true;
        this.grupos[grupo][pos] = -1;
        if (--this.nrElemGrupo[grupo] == 0) {
            this.salaAssalto[sala] = -1;
            this.gestorGrupos.desfazerGrupo(grupo);
            this.grupoOcup[grupo] = false;
            if (this.estadoChefe == WAITING_FOR_GROUP_ARRIVAL) {
                this.estadoChefe = DECIDING_WHAT_TO_DO;
                general.setMasterThiefState(this.estadoChefe);
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
    public int getPosGrupo(int id, int grupo) {

        for (int i = 0; i < NUM_GROUP; i++) {
            if (grupos[grupo][i] == id) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Verifies if there are paitings in the museum
     * @return true if empty
     */
    public boolean checkEmptyMuseu() {
        for (int i = 0; i < salaVazia.length; i++) {
            if (!salaVazia[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Show the results of the assault
     */
    public void sumUpResults() {
        this.estadoChefe = PRESENTING_THE_REPORT;
        general.setMasterThiefState(this.estadoChefe);
    }

    /**
     * Number of stollen paitings
     *
     * @return Number of stollen paitings
     */

    public int getQuadrosRoubados() {

        return this.quadrosRoubados;
    }

    /**
     * Checks if there are rooms with paitings
     *
     * @return true if there are rooms with paitings
     */
    public boolean checkSalasLivres() {

        for (int i = 0; i < NUM_ROOMS; i++) {
            if (!salaVazia[i] && salaAssalto[i] == -1) {
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
    public int getSalaAssalto(int grupoID) {
        for (int i = 0; i < NUM_ROOMS; i++) {
            if (this.salaAssalto[i] == grupoID) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Get the number of paitings in the room
     * @param sala room id
     * @return number of paitings
     */
    public int get_paitings(int sala){
       return museum.getNumeroQuadros(sala);
    }

}
