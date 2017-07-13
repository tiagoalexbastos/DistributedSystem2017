package DistributedVersion.ProblemInformation;

import DistributedVersion.Messages.AssaultPartyMessageException;
import DistributedVersion.Messages.GeneralRepositoryMessage;
import DistributedVersion.Messages.GeneralRepositoryMessageException;

/**
 * Created by pmatos9 on 24/04/17.
 */
public class GeneralRepositoryInterface {
    private GeneralRepository general;
    private boolean isALive = true;

    GeneralRepositoryInterface(GeneralRepository general) {
        this.general = general;
    }


    /**
     * Function that executes the operation specified by each received message
     *
     * @param inMessage Message with the request
     * @return GeneralRepositoryMessage reply
     * @throws GeneralRepositoryMessageException if the message contains an invalid request
     */
    public GeneralRepositoryMessage processAndReply(GeneralRepositoryMessage inMessage) throws GeneralRepositoryMessageException {

        GeneralRepositoryMessage outMessage = null;

        boolean check;
        int resp;
        int state;

        switch (inMessage.getMsgType()) {
            case GeneralRepositoryMessage.STARTLOG:
                general.iniciarLog();
                outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.ACK);
                break;

            case GeneralRepositoryMessage.ADDLOG:
                general.add_log();
                outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.ACK);
                break;

            case GeneralRepositoryMessage.ENDLOG:
                general.finalizarRelatorio(inMessage.getArg1());
                outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.ACK);
                break;

            case GeneralRepositoryMessage.SETROOMDISTANCE:
                general.setDistanciaSala(inMessage.getArg1(), inMessage.getArg2());
                outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.ACK);
                break;

            case GeneralRepositoryMessage.SETNUMBEROFPAINTINGS:
                general.setNrQuadrosSala(inMessage.getArg1(), inMessage.getArg2());
                outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.ACK);
                break;

            case GeneralRepositoryMessage.SETMASTERTHIEFSTATE:
                general.setMasterThiefState(inMessage.getArg1());
                outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.ACK);
                break;

            case GeneralRepositoryMessage.SETTHIEFSTATE:
                general.setThiefState(inMessage.getArg1(), inMessage.getArg2());
                outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.ACK);
                break;

            case GeneralRepositoryMessage.SETTHIEFSITUATION:
                general.setThiefSituation(inMessage.getArg1(), inMessage.getArg2());
                outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.ACK);
                break;

            case GeneralRepositoryMessage.SETTHIEFDISPLACEMENT:
                general.setThiefDisplacement(inMessage.getArg1(), inMessage.getArg2());
                outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.ACK);
                break;

            case GeneralRepositoryMessage.SETASSAULTPARTY1ROOM:
                general.setAssaultParty1_room(inMessage.getArg1());
                outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.ACK);
                break;

            case GeneralRepositoryMessage.SETASSAULTPARTY2ROOM:
                general.setAssaultParty2_room(inMessage.getArg1());
                outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.ACK);
                break;

            case GeneralRepositoryMessage.SETAP1POS:
                general.setAP1_pos(inMessage.getArg1(), inMessage.getArg2());
                outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.ACK);
                break;

            case GeneralRepositoryMessage.SETAP1CANVAS:
                general.setAP1_canvas(inMessage.getArg1(), inMessage.getB1(), inMessage.getArg2());
                outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.ACK);
                break;

            case GeneralRepositoryMessage.SETAP1POSIDCANVAS:
                general.setAP1_pos_id_canvas(inMessage.getArg1(), inMessage.getArg2(), inMessage.getArg3(), inMessage.getB1());
                outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.ACK);
                break;

            case GeneralRepositoryMessage.SETAP1RESET:
                general.setAP1_reset(inMessage.getArg1(), inMessage.getArg2());
                outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.ACK);
                break;

            case GeneralRepositoryMessage.SETAP2POS:
                general.setAP2_pos(inMessage.getArg1(), inMessage.getArg2());
                outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.ACK);
                break;

            case GeneralRepositoryMessage.SETAP2CANVAS:
                general.setAP2_canvas(inMessage.getArg1(), inMessage.getB1(), inMessage.getArg2());
                outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.ACK);
                break;

            case GeneralRepositoryMessage.SETAP2POSIDCANVAS:
                general.setAP2_pos_id_canvas(inMessage.getArg1(), inMessage.getArg2(), inMessage.getArg3(), inMessage.getB1());
                outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.ACK);
                break;

            case GeneralRepositoryMessage.SETAP2RESET:
                general.setAP2_reset(inMessage.getArg1(), inMessage.getArg2());
                outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.ACK);
                break;

            case GeneralRepositoryMessage.END:
                this.isALive = false;
                break;
        }
        return outMessage;
    }

    public boolean isAlive() {
        return this.isALive;
    }
}
