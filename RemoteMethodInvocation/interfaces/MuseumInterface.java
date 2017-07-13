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
public interface MuseumInterface extends Remote {
    /**
     * Method to steal the paiting
     *
     * @param nr_sala         room id
     * @param vectorTimestamp clock
     * @return clock and true if success
     * @throws RemoteException may throw during a execution of a remote method call
     */
    public Tuple<VectorTimestamp, Boolean> rollACanvas(int nr_sala, VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * Distance of the room
     *
     * @param nr_sala room id
     * @param clone clock
     * @return clock and distance
     * @throws RemoteException may throw during a execution of a remote method call
     */
    public Tuple<VectorTimestamp, Integer> getMuseumRoomDistance(int nr_sala, VectorTimestamp clone) throws RemoteException;

    /**
     * This function is used for the log to signal the AssaultPartyManager to shutdown.
     *
     * @throws RemoteException may throw during a execution of a remote method call
     */
    public void signalShutdown() throws RemoteException;
}
