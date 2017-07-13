package DistributedVersion.Monitors.CollectionSite;

import DistributedVersion.Messages.CollectionSiteMessage;
import DistributedVersion.Messages.CollectionSiteMessageException;

/**
 * Created by pmatos9 on 18/04/17.
 */
public class CollectionSiteInterface {
    private CollectionSite collectionSite;
    private boolean isAlive = true;

    public CollectionSiteInterface(CollectionSite collectionSite) {
        this.collectionSite = collectionSite;
    }

    /**
     * Function that executes the operation specified by each received message
     *
     * @param inMessage Message with the request
     * @return CollectionSiteMessage reply
     * @throws CollectionSiteMessageException if the message contains an invalid request
     */

    public CollectionSiteMessage processAndReply(CollectionSiteMessage inMessage) throws CollectionSiteMessageException {

        CollectionSiteMessage outMessage = null;


        switch (inMessage.getMsgType()) {
            case CollectionSiteMessage.CHECKEMPTYMUSEUM:
                break;
            case CollectionSiteMessage.CHECKGROUPS:
                break;
            case CollectionSiteMessage.CHECKEMPTYROOMS:
                break;
            case CollectionSiteMessage.TAKEAREST:
                break;
            case CollectionSiteMessage.JOINGROUP:
                if (inMessage.getArg1() < 0) {
                    throw new CollectionSiteMessageException(" Id Thief invalid!", inMessage);
                } else if (inMessage.getArg2() < 0) {
                    throw new CollectionSiteMessageException(" Id grupo invalid!", inMessage);
                }
                break;
            case CollectionSiteMessage.PREPAREASSAULTPARTY:
                break;
            case CollectionSiteMessage.HANDACANVAS:
                if (inMessage.getArg1() < 0) {
                    throw new CollectionSiteMessageException(" Id Thief invalid!", inMessage);
                } else if (inMessage.getArg2() < 0) {
                    throw new CollectionSiteMessageException(" Sala assalto invalid!", inMessage);
                } else if (inMessage.getArg3() < 0) {
                    throw new CollectionSiteMessageException(" Id grupo invalid!", inMessage);
                } else if (inMessage.getArg4() < 0) {
                    throw new CollectionSiteMessageException(" Id Pos Grupo invalid!", inMessage);
                }
                break;
            case CollectionSiteMessage.GETMASTERTHIEFSTATE:
                break;
            case CollectionSiteMessage.GETNRELEMENTSGROUP:
                if (inMessage.getArg1() < 0) {
                    throw new CollectionSiteMessageException(" Nr elements invalid!", inMessage);
                }
                break;
            case CollectionSiteMessage.GETGROUPPOSITION:
                if (inMessage.getArg1() < 0) {
                    throw new CollectionSiteMessageException(" Group Position invalid!", inMessage);
                } else if (inMessage.getArg2() < 0) {
                    throw new CollectionSiteMessageException(" Group Position invalid!", inMessage);
                }
                break;
            case CollectionSiteMessage.GETSTOLENPAINTINGS:
                break;
            case CollectionSiteMessage.GETASSAULTINGROOM:
                if (inMessage.getArg1() < 0) {
                    throw new CollectionSiteMessageException(" Assault room invalid!", inMessage);
                }
                break;
            case CollectionSiteMessage.ISGROUPDFULL:
                if (inMessage.getArg1() < 0) {
                    throw new CollectionSiteMessageException(" Group ID invalid!", inMessage);
                }
                break;
            case CollectionSiteMessage.FLAGEMPTYROOM:
                if (inMessage.getArg1() < 0) {
                    throw new CollectionSiteMessageException(" Room invalid!", inMessage);
                } else if (inMessage.getArg3() < 0) {
                    throw new CollectionSiteMessageException(" Position invalid!", inMessage);
                }
                break;
            case CollectionSiteMessage.STARTOPERATIONS:
                break;
            case CollectionSiteMessage.SUMUPRESULTS:
                break;
            case CollectionSiteMessage.END:
                break;
            default:
                break;
        }


        /*
         * processamento das mensagens
         */

        boolean bResp;
        int resp;
        int stat;


        switch (inMessage.getMsgType()) {
            case CollectionSiteMessage.CHECKEMPTYMUSEUM:
                bResp = collectionSite.checkEmptyMuseum();
                outMessage = new CollectionSiteMessage(CollectionSiteMessage.RESPCHECKEMPTYMUSEU, bResp);
                break;
            case CollectionSiteMessage.CHECKGROUPS:
                resp = collectionSite.checkGroups();
                outMessage = new CollectionSiteMessage(CollectionSiteMessage.RESPCHECKGRUPOS, resp);
                break;
            case CollectionSiteMessage.CHECKEMPTYROOMS:
                bResp = collectionSite.checkEmptyRooms();
                outMessage = new CollectionSiteMessage(CollectionSiteMessage.RESPEMPTYROOMS, bResp);
                break;
            case CollectionSiteMessage.TAKEAREST:
                collectionSite.takeARest();
                outMessage = new CollectionSiteMessage(CollectionSiteMessage.RESPTAKEAREST);
                break;
            case CollectionSiteMessage.JOINGROUP:
                collectionSite.joinGroup(inMessage.getArg1(), inMessage.getArg2());
                outMessage = new CollectionSiteMessage(CollectionSiteMessage.RESPENTRARGRUPO);
                break;
            case CollectionSiteMessage.PREPAREASSAULTPARTY:
                bResp = collectionSite.prepareAssaultParty(inMessage.getArg1());
                outMessage = new CollectionSiteMessage(CollectionSiteMessage.RESPPREPAREASSAULTPARTY, bResp);
                break;
            case CollectionSiteMessage.HANDACANVAS:
                collectionSite.handACanvas(inMessage.getArg1(), inMessage.getArg2(), inMessage.getArg3(), inMessage.getArg4());
                outMessage = new CollectionSiteMessage(CollectionSiteMessage.RESPHANDACANVAS);
                break;
            case CollectionSiteMessage.GETMASTERTHIEFSTATE:
                stat = collectionSite.getMasterThiefState();
                outMessage = new CollectionSiteMessage(CollectionSiteMessage.RESPGETESTADOCHEFE, stat);
                break;
            case CollectionSiteMessage.GETNRELEMENTSGROUP:
                resp = collectionSite.getGroupNumberOfElements(inMessage.getArg1());
                outMessage = new CollectionSiteMessage(CollectionSiteMessage.RESPGETNRELEMENTOSGRUPO, resp);
                break;
            case CollectionSiteMessage.GETGROUPPOSITION:
                resp = collectionSite.getGroupPosition(inMessage.getArg1(), inMessage.getArg2());
                outMessage = new CollectionSiteMessage(CollectionSiteMessage.RESPGETPOSGRUPO, resp);
                break;
            case CollectionSiteMessage.GETSTOLENPAINTINGS:
                resp = collectionSite.getStolenPaintings();
                outMessage = new CollectionSiteMessage(CollectionSiteMessage.RESPGETQUADROSROUBADOS, resp);
                break;
            case CollectionSiteMessage.GETASSAULTINGROOM:
                resp = collectionSite.getAssaultingRoom(inMessage.getArg1());
                outMessage = new CollectionSiteMessage(CollectionSiteMessage.RESPGETSALAASSALTO, resp);
                break;
            case CollectionSiteMessage.ISGROUPDFULL:
                bResp = collectionSite.isGroupFull(inMessage.getArg1());
                outMessage = new CollectionSiteMessage(CollectionSiteMessage.RESPGRUPOCHEIO, bResp);
                break;
            case CollectionSiteMessage.FLAGEMPTYROOM:
                collectionSite.flagEmptyRoom(inMessage.getArg1(), inMessage.getArg2(), inMessage.getArg3());
                outMessage = new CollectionSiteMessage(CollectionSiteMessage.RESPINDICARSALAVAZIA);
                break;
            case CollectionSiteMessage.STARTOPERATIONS:
                collectionSite.startOperations();
                outMessage = new CollectionSiteMessage(CollectionSiteMessage.RESPSTARTOPERATIONS);
                break;
            case CollectionSiteMessage.SUMUPRESULTS:
                collectionSite.sumUpResults();
                outMessage = new CollectionSiteMessage(CollectionSiteMessage.RESPSUMUPRESULTS);
                break;
            case CollectionSiteMessage.END:
                isAlive = false;
                collectionSite.end();
                break;
            default:
                throw new CollectionSiteMessageException("Type invalid!", inMessage);
        }

        return outMessage;
    }

    public boolean isAlive() {
        return this.isAlive;
    }
}

