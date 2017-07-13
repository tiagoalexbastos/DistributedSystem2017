package registry;

import genclass.GenericIO;
import interfaces.Register;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by tiagoalexbastos on 19-05-2017.
 */
public class ServerRegisterRemoteObject {
    /**
     * Main task.
     */

    /**
     *
     * @param args arguments
     */
    public static void main(String[] args) {
    /* get location of the registry service */
//        String rmiRegHostName = RegistryConfig.RMI_REGISTRY_HOSTNAME;
//        int rmiRegPortNumb = RegistryConfig.RMI_REGISTRY_PORT;
        String rmiRegHostName = args[0];
        int rmiRegPortNumb = Integer.parseInt(args[1]);
    /* create and install the security manager */

        if (System.getSecurityManager() == null)
            System.setSecurityManager(new SecurityManager());
        GenericIO.writelnString("Security manager was installed!");

    /* instantiate a registration remote object and generate a stub for it */

        RegisterRemoteObject regEngine = new RegisterRemoteObject(rmiRegHostName, rmiRegPortNumb);
        Register regEngineStub = null;

        try {
            regEngineStub = (Register) UnicastRemoteObject.exportObject(regEngine, RegistryConfig.RMI_REGISTER_PORT);
        } catch (RemoteException e) {
            GenericIO.writelnString("RegisterRemoteObject stub generation exception: " + e.getMessage());
            System.exit(1);
        }
        GenericIO.writelnString("Stub was generated!");

    /* register it with the local registry service */

        String nameEntry = "RegisterHandler";
        Registry registry = null;

        try {
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException e) {
            GenericIO.writelnString("RMI registry creation exception: " + e.getMessage());
            System.exit(1);
        }
        GenericIO.writelnString("RMI registry was created!");

        try {
            registry.rebind(nameEntry, regEngineStub);
        } catch (RemoteException e) {
            GenericIO.writelnString("RegisterRemoteObject remote exception on registration: " + e.getMessage());
            System.exit(1);
        }
        GenericIO.writelnString("RegisterRemoteObject object was registered!");
    }
}
