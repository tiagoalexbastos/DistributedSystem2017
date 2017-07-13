package interfaces;

import support.Tuple;
import support.VectorTimestamp;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface that defines the operations available over the objects that
 * represent the Collection Site
 *
 * @author Pedro Matos
 * @author Tiago Bastos
 */
public interface ConcentrationSiteInterface extends Remote {
    /**
     * Get for the thief's group
     *
     * @param id              thief's id
     * @param vectorTimestamp clock
     * @return clock and thief's group
     * @throws RemoteException may throw during a execution of a remote method call
     */
    public Tuple<VectorTimestamp, Integer> getThiefGroup(int id, VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * The thief uses this method when he is ready.
     * The thief is added to the FIFO
     *
     * @param ladraoID        thief's id
     * @param vectorTimestamp clock
     * @return clock
     * @throws RemoteException may throw during a execution of a remote method call
     */
    public VectorTimestamp imReady(int ladraoID, VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * Get for the number of thiefs in the CollectionSite
     *
     * @param vectorTimestamp clock
     * @return clock and number of thieves
     * @throws RemoteException may throw during a execution of a remote method call
     */
    public Tuple<VectorTimestamp, Integer> getNumberOfThieves(VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * Get for the thief state
     *
     * @param ladraoID        thief id
     * @param vectorTimestamp clock
     * @return clock and thief state
     * @throws RemoteException may throw during a execution of a remote method call
     */
    public Tuple<VectorTimestamp, Integer> getThiefState(int ladraoID, VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * Thief uses this when he is waiting for orders
     *
     * @param ladraoID        thief id
     * @param vectorTimestamp clock
     * @return clock
     * @throws RemoteException may throw during a execution of a remote method call
     */
    public VectorTimestamp amINeeded(int ladraoID, VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * Calls a thief from the FIFO and adds to the group
     *
     * @param grupo           group id
     * @param vectorTimestamp clock
     * @return clock and id of thief, or -1 if FIFO is empty
     * @throws RemoteException may throw during a execution of a remote method call
     */
    public Tuple<VectorTimestamp, Integer> callThief(int grupo, VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * Verifies if the thief is busy
     *
     * @param ladraoID        thief id
     * @param vectorTimestamp clock
     * @return clock and  true if busy, false is free
     * @throws RemoteException may throw during a execution of a remote method call
     */
    public Tuple<VectorTimestamp, Boolean> getBusyThief(int ladraoID, VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * State changes to Crawl IN
     *
     * @param ladraoID        thief id
     * @param vectorTimestamp clock
     * @return clock
     * @throws RemoteException may throw during a execution of a remote method call
     */
    public VectorTimestamp prepareExcursion(int ladraoID, VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * State changes to Crawl OUT
     *
     * @param ladraoID        thief id
     * @param vectorTimestamp clock
     * @return clock
     * @throws RemoteException may throw during a execution of a remote method call
     */
    public VectorTimestamp reverseDirection(int ladraoID, VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * State changes to At a Room
     *
     * @param ladraoID        thief id
     * @param vectorTimestamp clock
     * @return clock
     * @throws RemoteException may throw during a execution of a remote method call
     */
    public VectorTimestamp atARoom(int ladraoID, VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * Thief arrives at the CollectionSite
     *
     * @param ladraoID        thief id
     * @param vectorTimestamp clock
     * @return clock
     * @throws RemoteException may throw during a execution of a remote method call
     */
    public VectorTimestamp flagArrival(int ladraoID, VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * GET for the agility
     *
     * @param ladraoID        thief id
     * @param vectorTimestamp clock
     * @return clock and thief agility
     * @throws RemoteException may throw during a execution of a remote method call
     */
    public Tuple<VectorTimestamp, Integer> getAgility(int ladraoID, VectorTimestamp vectorTimestamp) throws RemoteException;

    ;

    /**
     * Waiting for the correct number of thiefs in order to create an assault party
     *
     * @param vectorTimestamp clock
     * @return clock
     * @throws RemoteException may throw during a execution of a remote method call
     */
    public VectorTimestamp waitForThieves(VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * Waits for all the thiefs in order to finish operation
     *
     * @param vectorTimestamp clock
     * @return clock
     * @throws RemoteException may throw during a execution of a remote method call
     */
    public VectorTimestamp waitForThievesEnd(VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * This function is used for the log to signal the AssaultPartyManager to shutdown.
     *
     * @throws RemoteException may throw during a execution of a remote method call
     */
    public void signalShutdown() throws RemoteException;
}
