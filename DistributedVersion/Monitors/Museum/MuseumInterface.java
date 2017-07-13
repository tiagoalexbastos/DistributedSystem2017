package DistributedVersion.Monitors.Museum;

import DistributedVersion.Messages.MuseumMessage;
import DistributedVersion.Messages.MuseumMessageException;

import static DistributedVersion.Messages.Constants.*;

/**
 * Created by pmatos9 on 17/04/17.
 */
public class MuseumInterface {

    private Museum museu;
    private boolean isALive = true;

    public MuseumInterface(Museum museu) {
        this.museu = museu;

    }

    /**
     * Function that executes the operation specified by each received message
     *
     * @param inMessage Message with the request
     * @return MuseumMessage reply
     * @throws MuseumMessageException if the message contains an invalid request
     */
    public MuseumMessage processAndReply(MuseumMessage inMessage) throws MuseumMessageException {

        MuseumMessage outMessage = null;


        switch (inMessage.getType()) {
            case MuseumMessage.STEALPAINTING:
                if (inMessage.getNrSala() > NUM_ROOMS || inMessage.getNrSala() < 0) {
                    throw new MuseumMessageException("Room invalid!", inMessage);
                }
                break;
            case MuseumMessage.GETNUMBEROFSTOLENGPAINTINGS:
                if (inMessage.getNrSala() > NUM_ROOMS || inMessage.getNrSala() < 0) {
                    throw new MuseumMessageException("Room invalid!", inMessage);
                }
                break;
            case MuseumMessage.GETDISTANCE:
                if (inMessage.getNrSala() > NUM_ROOMS || inMessage.getNrSala() < 0) {
                    throw new MuseumMessageException("Room invalid!", inMessage);
                }
                break;
            case MuseumMessage.END:
                break;
            default:
                throw new MuseumMessageException("Room invalid!", inMessage);
        }


        /*
         * processing
         */

        switch (inMessage.getType()) {
            case MuseumMessage.STEALPAINTING:
                boolean resp = museu.stealPainting(inMessage.getNrSala());
                outMessage = new MuseumMessage(MuseumMessage.SENDRESPROUBARQUADRO, resp);
                break;
            case MuseumMessage.GETNUMBEROFSTOLENGPAINTINGS:
                int nrQuadros = museu.getNumberofStolenPaintings(inMessage.getNrSala());
                outMessage = new MuseumMessage(MuseumMessage.SENDNUMEROQUADROS, nrQuadros);
                break;
            case MuseumMessage.GETDISTANCE:
                int dist = museu.getDistance(inMessage.getNrSala());
                outMessage = new MuseumMessage(MuseumMessage.SENDDISTANCIA, dist);
                break;
            case MuseumMessage.END:
                this.isALive = false;
                break;
            default:
                throw new MuseumMessageException("Type invalid!", inMessage);
        }


        return outMessage;
    }

    /**
     * Check if server can terminate
     * @return boolean to terminate server
     */
    public boolean isAlive() {
        return this.isALive;
    }
}

