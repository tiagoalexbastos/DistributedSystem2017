package DistributedVersion.Monitors.AssaultParty;

import DistributedVersion.Messages.AssaultPartyMessage;
import DistributedVersion.Messages.AssaultPartyMessageException;
import DistributedVersion.Messages.CollectionSiteMessage;

/**
 * Created by pmatos9 on 18/04/17.
 */
public class AssaultPartyInterface {

    private AssaultParty[] assaultParty;
    private String museum, repo;
    private boolean isAlive = true;

    AssaultPartyInterface(AssaultParty[] assaultParty, String museum, String repo) {
        this.assaultParty = assaultParty;
        this.museum = museum;
        this.repo = repo;
    }

    /**
     * Function that executes the operation specified by each received message
     *
     * @param inMessage Message with the request
     * @return AssaultPartyMessage reply
     * @throws AssaultPartyMessageException if the message contains an invalid request
     */

    AssaultPartyMessage processAndReply(AssaultPartyMessage inMessage) throws AssaultPartyMessageException {
        AssaultPartyMessage outMessage = null;

        switch (inMessage.getMsgType()) {
            case AssaultPartyMessage.JOINPARTY:
                if (inMessage.getArg2() > 1 || inMessage.getArg2() < 0) {
                    throw new AssaultPartyMessageException("Invalid assault party ID!", inMessage);
                }
                if (inMessage.getArg1() < 0) {
                    throw new AssaultPartyMessageException("Invalid thief id!", inMessage);
                }
                break;
            case AssaultPartyMessage.DESTROYGRPOUP:
                if (inMessage.getArg1() > 1 || inMessage.getArg1() < 0) {
                    throw new AssaultPartyMessageException("Invalid assault party id!", inMessage);
                }
                break;
            case AssaultPartyMessage.CREATEASSAULTPARTY:
                if (inMessage.getArg1() > 1 || inMessage.getArg1() < 0) {
                    throw new AssaultPartyMessageException("Invalid assault party id!", inMessage);
                }
                if (inMessage.getArg2() < 0) {
                    throw new AssaultPartyMessageException("Invalid room id!", inMessage);
                }
                break;
            case AssaultPartyMessage.GETROOMDISTANCE:
                if (inMessage.getArg1() > 1 || inMessage.getArg1() < 0) {
                    throw new AssaultPartyMessageException("Invalid assault party id!", inMessage);
                }
                break;
            case AssaultPartyMessage.GETPOS:
                if (inMessage.getArg2() > 1 || inMessage.getArg2() < 0) {
                    throw new AssaultPartyMessageException("Invalid assault party id!", inMessage);
                }
                if (inMessage.getArg1() < 0) {
                    throw new AssaultPartyMessageException("Invalid thief id", inMessage);
                }

                break;
            case AssaultPartyMessage.CRAWLIN:
                if (inMessage.getArg3() > 1 || inMessage.getArg3() < 0) {
                    throw new AssaultPartyMessageException("Invalid assault party id!", inMessage);
                }
                if (inMessage.getArg1() < 0) {
                    throw new AssaultPartyMessageException("Invalid thief id!", inMessage);
                }
                if (inMessage.getArg2() < 0) {
                    throw new AssaultPartyMessageException("Invalid agility!", inMessage);
                }
                if (inMessage.getArg4() < 0) {
                    throw new AssaultPartyMessageException("Invalid position!", inMessage);
                }
                break;
            case AssaultPartyMessage.CRAWLOUT:
                if (inMessage.getArg3() > 1 || inMessage.getArg3() < 0) {
                    throw new AssaultPartyMessageException("Invalid assault party id!", inMessage);
                }
                if (inMessage.getArg1() < 0) {
                    throw new AssaultPartyMessageException("Invalid thief id!", inMessage);
                }
                if (inMessage.getArg2() < 0) {
                    throw new AssaultPartyMessageException("Invalid agility!", inMessage);
                }
                if (inMessage.getArg4() < 0) {
                    throw new AssaultPartyMessageException("Invalid position!", inMessage);
                }
                break;
            case AssaultPartyMessage.STEALPAINTING:
                if (inMessage.getArg1() > 1 || inMessage.getArg1() < 0) {
                    throw new AssaultPartyMessageException("Invalid assault party id!", inMessage);
                }
                break;
            case AssaultPartyMessage.WAITMYTURN:
                if (inMessage.getArg2() > 1 || inMessage.getArg2() < 0) {
                    throw new AssaultPartyMessageException("Invalid assault party id!", inMessage);
                }
                if (inMessage.getArg1() < 0) {
                    throw new AssaultPartyMessageException("Invalid thief id!", inMessage);
                }
                break;
            case AssaultPartyMessage.END:
                break;
            default:
                throw new AssaultPartyMessageException("Invalid type!", inMessage);
        }


        int resp;
        /*
         * processing
         */

        switch (inMessage.getMsgType()) {
            case AssaultPartyMessage.JOINPARTY:
                assaultParty[inMessage.getArg2()].joinParty(inMessage.getArg1(), inMessage.getArg3());
                outMessage = new AssaultPartyMessage(AssaultPartyMessage.ACK);
                break;
            case AssaultPartyMessage.DESTROYGRPOUP:
                destroyGroup(inMessage.getArg1());
                outMessage = new AssaultPartyMessage(AssaultPartyMessage.ACK);
                break;
            case AssaultPartyMessage.CREATEASSAULTPARTY:
                resp = createAssaultParty(inMessage.getArg1(), inMessage.getArg2());
                outMessage = new AssaultPartyMessage(AssaultPartyMessage.RESPFORMARGRUPO, resp);
                break;
            case AssaultPartyMessage.GETROOMDISTANCE:
                resp = assaultParty[inMessage.getArg1()].getRoomDistance();
                outMessage = new AssaultPartyMessage(AssaultPartyMessage.RESPGETDISTANCIASALA, resp);
                break;
            case AssaultPartyMessage.GETPOS:
                if (assaultParty[inMessage.getArg2()] != null) {
                    resp = assaultParty[inMessage.getArg2()].getPos(inMessage.getArg1());
                } else {
                    resp = -1;
                }
                outMessage = new AssaultPartyMessage(AssaultPartyMessage.RESPGETPOS, resp);
                break;
            case AssaultPartyMessage.CRAWLIN:
                resp = assaultParty[inMessage.getArg3()].crawlIn(inMessage.getArg1(), inMessage.getArg2(), inMessage.getArg3(), inMessage.getArg4());
                outMessage = new AssaultPartyMessage(AssaultPartyMessage.RESPCRAWLIN, resp);
                break;
            case AssaultPartyMessage.CRAWLOUT:
                resp = assaultParty[inMessage.getArg3()].crawlOut(inMessage.getArg1(), inMessage.getArg2(), inMessage.getArg3(), inMessage.getArg4());
                outMessage = new AssaultPartyMessage(AssaultPartyMessage.RESPCRAWLOUT, resp);
                break;
            case AssaultPartyMessage.STEALPAINTING:
                boolean quadro = assaultParty[inMessage.getArg1()].stealPainting();
                outMessage = new AssaultPartyMessage(AssaultPartyMessage.RESPROUBARQUADRO, quadro);
                break;
            case AssaultPartyMessage.WAITMYTURN:
                assaultParty[inMessage.getArg2()].waitMyTurn(inMessage.getArg1());
                outMessage = new AssaultPartyMessage(AssaultPartyMessage.ACK);
                break;
            case AssaultPartyMessage.END:
                isAlive = false;
                break;
            default:
                throw new AssaultPartyMessageException("Invalid type!", inMessage);
        }

        return outMessage;
    }


    /**
     * Function where the assault party is created
     *
     * @param id     assault party ID
     * @param nrSala room to steal
     * @return id of party
     */
    public int createAssaultParty(int id, int nrSala) {
        if (assaultParty[id] == null) {
            assaultParty[id] = new AssaultParty(museum, nrSala, id, repo);
            return id;
        } else {
            return -1;
        }
    }


    /**
     * Function that returns the state of the communication socket
     * @return boolean to terminate
     */
    public boolean isAlive() {
        return this.isAlive;
    }

    /**
     * Function where the group is destroyed
     * @param id group ID
     */
    public void destroyGroup(int id) {
        assaultParty[id] = null;
    }
}

