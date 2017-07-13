package interfaces;

import support.Tuple;
import support.VectorTimestamp;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface that defines the operations available over the objects that
 * represent the AssaultPartyManager
 *
 * @author Pedro Matos
 * @author Tiago Bastos
 */
public interface AssaultPartyManagerInterface extends Remote {
    /**
     * Group Formation
     *
     * @param idGrupo         group id
     * @param nrSala          room id
     * @param vectorTimestamp clock
     * @return clock and boolean if creation is successful
     * @throws RemoteException may throw during a execution of a remote method call
     */
    public Tuple<VectorTimestamp, Boolean> createAssaultParty(int idGrupo, int nrSala, VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * Method that returns the thief position inside the Assault Party
     *
     * @param ladraoID        thief id
     * @param idGrupo         group id
     * @param vectorTimestamp clock
     * @return clock and int with the position of the thief in the assault party
     * @throws RemoteException may throw during a execution of a remote method call
     */
    public Tuple<VectorTimestamp, Integer> getGroupPosition(int ladraoID, int idGrupo, VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * Destroys the group
     *
     * @param idGrupo         group id
     * @param vectorTimestamp clock
     * @return clock
     * @throws RemoteException may throw during a execution of a remote method call
     */
    public VectorTimestamp destroyAssaultParty(int idGrupo, VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * Places thief in the group
     *
     * @param ladraoID        thief id
     * @param idGrupo         group id
     * @param pos_grupo       group position
     * @param vectorTimestamp clock
     * @return clock
     * @throws RemoteException may throw during a execution of a remote method call
     */
    public VectorTimestamp joinAssaultParty(int ladraoID, int idGrupo, int pos_grupo, VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * Calls crawl in
     *
     * @param ladraoID        thief id
     * @param agilidade       thief agility
     * @param idGrupo         group id
     * @param posgrupo        position in the group
     * @param vectorTimestamp clock
     * @return clock and final position
     * @throws RemoteException may throw during a execution of a remote method call
     */
    public Tuple<VectorTimestamp, Integer> crawlIn(int ladraoID, int agilidade, int idGrupo, int posgrupo, VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * Calls crawl out
     *
     * @param ladraoID        thief id
     * @param agilidade       thief agility
     * @param idGrupo         group id
     * @param posgrupo        position in the group
     * @param vectorTimestamp clock
     * @return clock final position
     * @throws RemoteException may throw during a execution of a remote method call
     */
    public Tuple<VectorTimestamp, Integer> crawlOut(int ladraoID, int agilidade, int idGrupo, int posgrupo, VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * Get distance of room
     *
     * @param idGrupo         group id
     * @param vectorTimestamp clock
     * @param id
     * @return clock and respective room distance
     * @throws RemoteException may throw during a execution of a remote method call
     */
    public Tuple<VectorTimestamp, Integer> getRoomDistance(int idGrupo, VectorTimestamp vectorTimestamp, int id) throws RemoteException;

    /**
     * Steal paiting
     *
     * @param idGrupo         group id
     * @param vectorTimestamp clock
     * @param id
     * @return clock and true if paiting was stolen
     * @throws RemoteException may throw during a execution of a remote method call
     */
    public Tuple<VectorTimestamp, Boolean> rollACanvas(int idGrupo, VectorTimestamp vectorTimestamp, int id) throws RemoteException;

    /**
     * Thief is waiting for it's turn
     *
     * @param id              Thief id
     * @param idGrupo         Group id
     * @param vectorTimestamp clock
     * @return clock
     * @throws RemoteException may throw during a execution of a remote method call
     */
    public VectorTimestamp waitMyTurn(int id, int idGrupo, VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * This function is used for the log to signal the AssaultPartyManager to shutdown.
     *
     * @throws RemoteException may throw during a execution of a remote method call
     */
    public void signalShutdown() throws RemoteException;
}
