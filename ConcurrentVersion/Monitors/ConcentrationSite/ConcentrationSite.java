package ConcorrentVersion.Monitors.ConcentrationSite;

import static ConcorrentVersion.ProblemInformation.Constantes.*;
import ConcorrentVersion.Support.MemFIFO;
import ConcorrentVersion.ProblemInformation.GeneralRepository;

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
    private int[] estadoLadrao = new int[NUM_THIEVES];
    /**
     * Flag if the thief is busy
     */
    private boolean[] busyLadrao = new boolean[NUM_THIEVES];
    /**
     * Number of thiefs in the CollectionSite
     */
    private int nrLadroes;
    /**
     * Agility of each thief
     */
    private int agilidadeLadroes[] = new int[NUM_THIEVES];
    /**
     * Group of each thief
     */
    private int grupoLadrao[] = new int[NUM_THIEVES];
    /**
     * General Repository
     */
    private GeneralRepository general;
    /**
     * Thief's situation
     */
    private int situacaoLadrao[] = new int[NUM_THIEVES];

    /**
     * @param generalRepository General Repository
     */
    public ConcentrationSite(GeneralRepository generalRepository) {

        this.nrLadroes = 0;

        for (int i = 0; i < NUM_THIEVES; i++) {
            estadoLadrao[i] = OUTSIDE;
            this.busyLadrao[i] = false;
            grupoLadrao[i] = -1;
            agilidadeLadroes[i] = ((int) (Math.random() * (MAX_AGIL - 1))) + MIN_AGIL;
            this.situacaoLadrao[i] = WAITING;
        }

        fifo = new MemFIFO(NUM_THIEVES);

        this.general = generalRepository;
    }

    /**
     * Get for the thief's group
     * @param id thief's id
     * @return thief's group
     */
    public synchronized int getGrupoLadrao(int id) {


        return grupoLadrao[id];

    }

    /**
     * The thief uses this method when he is ready.
     * The thief is added to the FIFO
     *
     * @param ladraoID thief's id
     */
    public synchronized void estouPronto(int ladraoID) {


        if (!fifo.full()) {
            this.busyLadrao[ladraoID] = false;
            fifo.write(ladraoID);
            this.nrLadroes++;
            this.grupoLadrao[ladraoID] = -1;
        } else {
            System.out.println("ERRO!!");
        }

        if (this.nrLadroes >= NUM_GROUP) {

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
    public synchronized int getNrLadroes() {


        return this.nrLadroes;
    }

    /**
     * Get for the thief state
     * @param ladraoID thief id
     * @return thief state
     */

    public synchronized int getStateLadrao(int ladraoID) {

        return this.estadoLadrao[ladraoID];

    }

    /**
     * Thief uses this when he is waiting for orders
     * @param ladraoID thief id
     */

    public synchronized void amINeeded(int ladraoID) {

        notifyAll();
        while (!this.busyLadrao[ladraoID] && this.estadoLadrao[ladraoID] != HEIST_END) {
            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }

    }

    /**
     *  Calls a thief from the FIFO and adds to the group
     * @param grupo group id
     * @return id of thief, or -1 if FIFO is empty
     */
    public synchronized int chamaLadrao(int grupo) {


        int id = -1;

        if (!fifo.empty()) {
            id = (Integer) fifo.read();
            this.nrLadroes--;
            this.busyLadrao[id] = true;
            this.grupoLadrao[id] = grupo;
            general.setMasterThiefState(ASSEMBLING_A_GROUP);
            general.setThiefSituation(id,IN_PARTY);
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
    public synchronized boolean getBusyLadrao(int ladraoID) {
        return this.busyLadrao[ladraoID];
    }

    /**
     * State changes to Crawl IN
     * @param ladraoID thief id
     */
    public synchronized void prepareExcursion(int ladraoID) {
        this.estadoLadrao[ladraoID] = CRAWLING_INWARDS;
    }

    /**
     * State changes to Crawl OUT
     * @param ladraoID thief id
     */
    public synchronized void reverseDirection(int ladraoID) {
        this.estadoLadrao[ladraoID] = CRAWLING_OUTWARDS;
    }

    /**
     * State changes to At a Room
     * @param ladraoID thief id
     */
    public synchronized void naSala(int ladraoID) {
        this.estadoLadrao[ladraoID] = AT_A_ROOM;
    }

    /**
     * Thief arrives at the CollectionSite
     * @param ladraoID thief id
     */
    public synchronized void indicarChegada(int ladraoID) {


        this.busyLadrao[ladraoID] = false;
        this.grupoLadrao[ladraoID] = -1;
        this.estadoLadrao[ladraoID] = OUTSIDE;
        general.setThiefState(ladraoID,this.estadoLadrao[ladraoID] );
        general.setThiefSituation(ladraoID,WAITING);
        this.estouPronto(ladraoID);



    }

    /**
     * GET for the agility
     * @param ladraoID thief id
     * @return thief agility
     */
    public synchronized int getAgilidade(int ladraoID) {
        return agilidadeLadroes[ladraoID];
    }

    /**
     * Waiting for the correct number of thiefs in order to create an assault party
     */
    public synchronized void esperaLadroes() {
        while (this.nrLadroes < NUM_GROUP) {
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
    public synchronized void esperaLadroesFim() {
        while (this.nrLadroes < NUM_THIEVES) {
            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }
        for (int i = 0; i < NUM_THIEVES; i++) {
            estadoLadrao[i] = HEIST_END;
        }
        notifyAll();
    }
}
