package DistributedVersion.Monitors.ConcentrationSite;

import DistributedVersion.Messages.ConcentrationSiteMessage;
import DistributedVersion.Messages.ConcentrationSiteMessageException;

import static DistributedVersion.Messages.Constants.MAX_AGIL;

/**
 * Created by pmatos9 on 18/04/17.
 */
public class ConcentrationSiteInterface {
    private ConcentrationSite base;
    private boolean isAlive = true;
    ConcentrationSiteInterface(ConcentrationSite base) {
        this.base = base;
    }

    /**
     * Function that executes the operation specified by each received message
     *
     * @param inMessage Message with the request
     * @return ConcentrationSiteMessage reply
     * @throws ConcentrationSiteMessageException if the message contains an invalid request
     */
    public ConcentrationSiteMessage processAndReply(ConcentrationSiteMessage inMessage) throws ConcentrationSiteMessageException {

        ConcentrationSiteMessage outMessage = null;

        switch (inMessage.getType()) {
            case ConcentrationSiteMessage.CALLTHIEF:
                if (inMessage.getArg1() > 1 || inMessage.getArg1() < 0) {
                    throw new ConcentrationSiteMessageException("Invalid Group!", inMessage);
                }
                break;
            case ConcentrationSiteMessage.WAITTHIVES:
                break;
            case ConcentrationSiteMessage.WAITTHIEVESEND:
                break;
            case ConcentrationSiteMessage.GETTHIEFNUMBER:
                break;
            case ConcentrationSiteMessage.AMINEEDED:
                if (inMessage.getArg1() < 0) {
                    throw new ConcentrationSiteMessageException("Invalid thief id!", inMessage);
                }
                break;
            case ConcentrationSiteMessage.IMREADY:
                if (inMessage.getArg1() < 0) {
                    throw new ConcentrationSiteMessageException("Invalid thief id!", inMessage);
                }
                break;
            case ConcentrationSiteMessage.GETBUSYTHIEF:
                if (inMessage.getArg1() < 0) {
                    throw new ConcentrationSiteMessageException("Invalid thief id!", inMessage);
                }
                break;
            case ConcentrationSiteMessage.GETGROUPTHIEF:
                if (inMessage.getArg1() < 0) {
                    throw new ConcentrationSiteMessageException("Invalid thief id!", inMessage);
                }
                break;
            case ConcentrationSiteMessage.GETSTATETHIEF:
                if (inMessage.getArg1() < 0) {
                    throw new ConcentrationSiteMessageException("Invalid thief id!", inMessage);
                }
                break;
            case ConcentrationSiteMessage.INDICARCHEGADA:
                if (inMessage.getArg1() < 0) {
                    throw new ConcentrationSiteMessageException("Invalid thief id!", inMessage);
                }
                break;
            case ConcentrationSiteMessage.PREPAREEXCURSION:
                if (inMessage.getArg1() < 0) {
                    throw new ConcentrationSiteMessageException("Invalid thief id!", inMessage);
                }
                break;
            case ConcentrationSiteMessage.NASALA:
                if (inMessage.getArg1() < 0) {
                    throw new ConcentrationSiteMessageException("Invalid thief id!", inMessage);
                }
                break;
            case ConcentrationSiteMessage.REVERSEDIRECTION:
                if (inMessage.getArg1() < 0) {
                    throw new ConcentrationSiteMessageException("Invalid thief id!", inMessage);
                }
                break;
            case ConcentrationSiteMessage.GETAGILITY:
                if (inMessage.getArg1() > MAX_AGIL || inMessage.getArg1() < 0) {
                    throw new ConcentrationSiteMessageException("Invalid agility!", inMessage);
                }
                break;
            case ConcentrationSiteMessage.END:
                break;
            default:
                throw new ConcentrationSiteMessageException("Invalid type!", inMessage);
        }

        /*
         * processing msg received
         */
        boolean check;
        int resp;
        int state;

        switch (inMessage.getType()) {
            case ConcentrationSiteMessage.CALLTHIEF:
                resp = base.callThief(inMessage.getArg1());
                outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.RESPCHAMALADRAO, resp);
                break;
            case ConcentrationSiteMessage.WAITTHIVES:
                base.waitForThieves();
                outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.ACK);
                break;
            case ConcentrationSiteMessage.WAITTHIEVESEND:
                base.waitForThievesEnd();
                outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.ACK);
                break;
            case ConcentrationSiteMessage.AMINEEDED:
                base.amINeeded(inMessage.getArg1());
                outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.ACK);
                break;
            case ConcentrationSiteMessage.IMREADY:
                base.iAmReady(inMessage.getArg1());
                outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.ACK);
                break;
            case ConcentrationSiteMessage.GETBUSYTHIEF:
                check = base.getBusyThief(inMessage.getArg1());
                outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.RESPGETBUSYLADRAO, check);
                break;
            case ConcentrationSiteMessage.GETGROUPTHIEF:
                resp = base.getThiefGroup(inMessage.getArg1());
                outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.RESPGETGRUPOLADRAO, resp);
                break;
            case ConcentrationSiteMessage.GETTHIEFNUMBER:
                resp = base.getNumber_of_thieves();
                outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.RESPGETNRLADROES, resp);
                break;
            case ConcentrationSiteMessage.GETSTATETHIEF:
                state = base.getThiefState(inMessage.getArg1());
                outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.RESPGETSTATELADRAO, state);
                break;
            case ConcentrationSiteMessage.INDICARCHEGADA:
                base.thiefArrived(inMessage.getArg1());
                outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.ACK);
                break;
            case ConcentrationSiteMessage.PREPAREEXCURSION:
                base.prepareExcursion(inMessage.getArg1());
                outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.ACK);
                break;
            case ConcentrationSiteMessage.NASALA:
                base.atARoom(inMessage.getArg1());
                outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.ACK);
                break;
            case ConcentrationSiteMessage.REVERSEDIRECTION:
                base.reverseDirection(inMessage.getArg1());
                outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.ACK);
                break;
            case ConcentrationSiteMessage.GETAGILITY:
                resp = base.getAgility(inMessage.getArg1());
                outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.RESPGETAGILITY,resp);
                break;
            case ConcentrationSiteMessage.END:
                isAlive = false;
                break;
            default:
                throw new ConcentrationSiteMessageException("Invalid type!", inMessage);

        }
        return outMessage;
    }

    public boolean isAlive(){
        return this.isAlive;
    }
}

