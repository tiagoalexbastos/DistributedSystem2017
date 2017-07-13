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
public interface CollectionSiteInterface extends Remote {
    /**
     * Checks if the groups are done
     *
     * @param vectorTimestamp clock
     * @return clock and int with the id of the group
     * @throws RemoteException may throw during a execution of a remote method call
     */
    public Tuple<VectorTimestamp, Integer> checkGroups(VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * Adds thief to an assault party
     *
     * @param ladraoID        thief id
     * @param grupo           group id
     * @param vectorTimestamp clock
     * @return clock and the Position in the group. -1 if the group is full
     * @throws RemoteException may throw during a execution of a remote method call
     */
    public Tuple<VectorTimestamp, Integer> joinAssaultParty(int ladraoID, int grupo, VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * Verifies master thief state
     *
     * @param vectorTimestamp clock
     * @return clock and the master thief state
     * @throws RemoteException may throw during a execution of a remote method call
     */
    public Tuple<VectorTimestamp, Integer> getMasterThiefState(VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * Starts the assault
     *
     * @param vectorTimestamp clock
     * @return clock
     * @throws RemoteException may throw during a execution of a remote method call
     */
    public VectorTimestamp startOperations(VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * Creates assault party
     *
     * @param idGrupo if of party
     * @param vectorTimestamp clock
     * @return clock and true if formed, false if not.
     * @throws RemoteException may throw during a execution of a remote method call
     */
    public VectorTimestamp prepareAssaultParty(int idGrupo, VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * Master is resting
     *
     * @param vectorTimestamp clock
     * @return clock
     * @throws RemoteException may throw during a execution of a remote method call
     */
    public VectorTimestamp takeARest(VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * Returns the number of elements in the group
     *
     * @param grupoID         id of group
     * @param vectorTimestamp clock
     * @return clock number of elements
     * @throws RemoteException may throw during a execution of a remote method call
     */
    public Tuple<VectorTimestamp, Integer> getNumberElemGroup(int grupoID, VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * Hand canvas to the thief and leave the group
     *
     * @param ladraoID        thief id
     * @param sala            room id
     * @param grupo           group id
     * @param pos             position in group
     * @param vectorTimestamp clock
     * @return clock
     * @throws RemoteException may throw during a execution of a remote method call
     */
    public VectorTimestamp handACanvas(int ladraoID, int sala, int grupo, int pos, VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * Room is empty
     *
     * @param sala            room id
     * @param grupo           group id
     * @param pos             position in the group
     * @param vectorTimestamp clock
     * @return clock
     * @throws RemoteException may throw during a execution of a remote method call
     */
    public VectorTimestamp flagEmptyRoom(int sala, int grupo, int pos, VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * Verifies if there are paitings in the museum
     *
     * @param vectorTimestamp clock
     * @return clock and true if museum is empty
     * @throws RemoteException may throw during a execution of a remote method call
     */
    public Tuple<VectorTimestamp, Boolean> checkEmptyMuseum(VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * Show the results of the assault
     *
     * @param vectorTimestamp clock
     * @return clock
     * @throws RemoteException may throw during a execution of a remote method call
     */
    public VectorTimestamp sumUpResults(VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * Number of stollen paitings
     *
     * @param vectorTimestamp clock
     * @return clock and number of paintings
     * @throws RemoteException may throw during a execution of a remote method call
     */
    public Tuple<VectorTimestamp, Integer> getNumberofStolenPaints(VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * Checks if there are rooms with paitings
     *
     * @param vectorTimestamp clock
     * @return clock and true if the rooms aren't empty
     * @throws RemoteException may throw during a execution of a remote method call
     */
    public Tuple<VectorTimestamp, Boolean> checkEmptyRooms(VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * Returns the room the group is assaulting
     *
     * @param grupoID         group id
     * @param vectorTimestamp clock
     * @return clock and room id
     * @throws RemoteException may throw during a execution of a remote method call
     */
    public Tuple<VectorTimestamp, Integer> getAssaultRoom(int grupoID, VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * This function is used for the log to signal the AssaultPartyManager to shutdown.
     *
     * @throws RemoteException may throw during a execution of a remote method call
     */
    public void signalShutdown() throws RemoteException;
}
