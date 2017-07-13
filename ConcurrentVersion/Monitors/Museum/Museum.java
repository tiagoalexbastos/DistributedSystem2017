package ConcorrentVersion.Monitors.Museum;

import static ConcorrentVersion.ProblemInformation.Constantes.*;
import ConcorrentVersion.ProblemInformation.GeneralRepository;
import java.util.Random;

/**
 * Monitor Museum
 *
 * @author Pedro Matos and Tiago Bastos
 */
public class Museum {

    /**
     * Rooms of the museum
     */
    int[][] salas = new int[NUM_ROOMS][2];
    /**
     * General Repository
     */
    GeneralRepository generalRepository;

    /**
     * @param generalRepository General Repository
     */
    public Museum(GeneralRepository generalRepository){

        this.generalRepository = generalRepository;

        for (int i = 0; i < salas.length; i++) {
            Random r = new Random();
            salas[i][0] = r.nextInt((MAX_PAINTS+1)-MIN_PAINTS) + MIN_PAINTS;
            this.generalRepository.setNrQuadrosSala(i, salas[i][0]);
            Random r2 = new Random();

            salas[i][1] = r2.nextInt((MAX_DIST+1)-MIN_DIST) + MIN_DIST;
            this.generalRepository.setDistanciaSala(i, salas[i][1]);
        }



    }

    /**
     *
     * Method to steal the paiting
     *
     * @param nr_sala room id
     * @return true if success
     */
    public synchronized boolean roubarQuadro(int nr_sala) {
        if (salas[nr_sala][0] > 0) {
            salas[nr_sala][0]--;
            //generalRepository.setNrQuadrosSala(nr_sala, salas[nr_sala][0]);
            return true;
        } else {
            return false;
        }
    }


    /**
     * Distance of the room
     * @param nr_sala room id
     * @return distance
     */
    public synchronized int getDistancia(int nr_sala) {
        return salas[nr_sala][1];
    }

    /**
     * Number of paitings
     * @param nr_sala room id
     * @return nr of paitings
     */
    public synchronized int getNumeroQuadros(int nr_sala) {
        return salas[nr_sala][0];
    }
}
