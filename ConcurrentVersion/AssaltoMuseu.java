package ConcorrentVersion;

import static ConcorrentVersion.ProblemInformation.Constantes.*;

import ConcorrentVersion.ProblemInformation.GeneralRepository;
import ConcorrentVersion.Monitors.ConcentrationSite.ConcentrationSite;
import ConcorrentVersion.Monitors.CollectionSite.CollectionSite;
import ConcorrentVersion.Monitors.AssaultParty.AssaultPartyManager;
import ConcorrentVersion.Monitors.Museum.Museum;
import ConcorrentVersion.Actors.MasterThief;
import ConcorrentVersion.Actors.Thief;

/**
 * Main Program
 *
 * @author Pedro Matos and Tiago Bastos
 */
public class AssaltoMuseu {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {


        for(int z = 0; z <10; z++){
            System.out.println("Tentativa: "+z);



            GeneralRepository generalRepository = new GeneralRepository();

            Museum museum = new Museum(generalRepository);
            ConcentrationSite concentrationSite = new ConcentrationSite(generalRepository);
            AssaultPartyManager gestorGrupos = new AssaultPartyManager(museum, generalRepository);
            CollectionSite escritorio = new CollectionSite(museum, concentrationSite, gestorGrupos, generalRepository);


            MasterThief chefe;

            Thief[] ladrao = new Thief[NUM_THIEVES];
            for (int i = 0; i < NUM_THIEVES; i++) {
                String nomeLadrao = "Thief-" + i;
                ladrao[i] = new Thief(i, nomeLadrao, generalRepository, gestorGrupos, escritorio, concentrationSite);
                ladrao[i].start();
            }


            chefe = new MasterThief("LadraoChefe", generalRepository, escritorio, concentrationSite);
            chefe.start();

            try {
                chefe.join();
            } catch (InterruptedException ex) {
                System.out.println("Master Thief is over!");
            }


            for (int i = 0; i < NUM_THIEVES; i++) {
                try {
                    ladrao[i].join();
                } catch (InterruptedException ex) {
                    System.out.println("Thief " + i + " is over!");
                }
            }
        }
    }
}
