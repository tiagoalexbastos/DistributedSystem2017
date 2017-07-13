package ConcorrentVersion.Actors;

import static ConcorrentVersion.ProblemInformation.Constantes.*;

import ConcorrentVersion.ProblemInformation.GeneralRepository;
import ConcorrentVersion.Monitors.ConcentrationSite.ConcentrationSite;
import ConcorrentVersion.Monitors.CollectionSite.CollectionSite;


/**
 * Represents the Master Thief
 * @author Pedro Matos and Tiago Bastos
 */
public class MasterThief extends Thread {


    /**
     * Log that contains the thief's values
     */
    private GeneralRepository generalRepository;
    /**
     * Concentration Site
     */
    private CollectionSite escritorio;
    /**
     * Thief's concentrationSite
     */
    private ConcentrationSite concentrationSite;
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
     * @param escritorio Concentration Site
     * @param concentrationSite CollectionSite
     */
    public MasterThief(String name, GeneralRepository generalRepository, CollectionSite escritorio, ConcentrationSite concentrationSite) {

        super(name);
        this.name = name;
        this.id = NUM_THIEVES;

        this.generalRepository = generalRepository;

        this.concentrationSite = concentrationSite;
        this.escritorio = escritorio;
    }

    @Override
    public void run() {

        int numero_grupos = -1;
        boolean heistOver = false;

            while (!heistOver) {

                int stat = escritorio.getEstadoChefe();

                switch (stat) {
                    case PLANNING_THE_HEIST:

                        generalRepository.iniciarLog();
                        generalRepository.setMasterThiefState(stat);

                        if (concentrationSite.getNrLadroes() == NUM_THIEVES) {
                            escritorio.startOperations();

                        }

                        break;


                    case DECIDING_WHAT_TO_DO:


                        numero_grupos = escritorio.checkGrupos();


                        boolean emptyMuseu = escritorio.checkEmptyMuseu();

                        boolean checkSalasLivres = escritorio.checkSalasLivres();
                        if (numero_grupos != -1 && !emptyMuseu && checkSalasLivres) {
                            escritorio.prepareAssaultParty(numero_grupos);
                        }
                        else {
                            emptyMuseu =escritorio.checkEmptyMuseu();
                            if (emptyMuseu) {
                                escritorio.sumUpResults();
                            } else {
                                escritorio.takeARest();
                            }
                        }

                        break;


                    case ASSEMBLING_A_GROUP:


                        concentrationSite.esperaLadroes();


                        int nrElemGrupo = escritorio.getNrElemGrupo(numero_grupos);

                        if (nrElemGrupo == 0) {

                            for (int i = 0; i < NUM_GROUP; i++) {

                                int ladrao = concentrationSite.chamaLadrao(numero_grupos);
                                escritorio.entrarGrupo(ladrao,numero_grupos);
                            }
                            escritorio.takeARest();
                        }


                        break;


                    case WAITING_FOR_GROUP_ARRIVAL:

                        escritorio.takeARest();
                        break;


                    case PRESENTING_THE_REPORT:


                        concentrationSite.esperaLadroesFim();


                        int nrQuadrosRoubados = escritorio.getQuadrosRoubados();
                        generalRepository.finalizarRelatorio(nrQuadrosRoubados);
                        heistOver = true;
                        break;
                }
            }


    }
}
