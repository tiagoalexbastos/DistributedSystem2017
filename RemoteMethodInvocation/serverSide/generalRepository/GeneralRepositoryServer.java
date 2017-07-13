package serverSide.generalRepository;

import genclass.GenericIO;
import interfaces.GeneralRepositoryInterface;
import interfaces.Register;
import registry.RegistryConfig;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by tiagoalexbastos on 20-05-2017.
 */
public class GeneralRepositoryServer {

    public static void main(String args[]) {
        /* obtenção da localização do serviço de registo RMI */
//        String rmiRegHostName = RegistryConfig.RMI_REGISTRY_HOSTNAME;
//        int rmiRegPortNumb = RegistryConfig.RMI_REGISTRY_PORT;
        String rmiRegHostName = args[0];
        int rmiRegPortNumb = Integer.parseInt(args[1]);


        /* instanciação e instalação do gestor de segurança */
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        GenericIO.writelnString("Security manager was installed!");

        // Instanciçãoo do RefereeSite
        GeneralRepository generalRepository = new GeneralRepository(rmiRegHostName, rmiRegPortNumb);
        GeneralRepositoryInterface generalRepositoryInterface = null;

        try {
            generalRepositoryInterface = (GeneralRepositoryInterface)
                    UnicastRemoteObject.exportObject(generalRepository, RegistryConfig.RMI_GENREPO_PORT);
        } catch (RemoteException e) {
            System.out.println("Excepção na geração do stub para o General Repository: " + e.getMessage());
            System.exit(1);
        }
        System.out.println("O stub para o General Repository foi gerado!");

        /* seu registo no serviço de registo RMI */
        String nameEntryBase = RegistryConfig.RMI_REGISTER_NAME;
        String nameEntryObject = RegistryConfig.RMI_REGISTRY_GENREPO_NAME;
        Registry registry = null;
        Register reg = null;

        try {
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException e) {
            System.out.println("Excepção na criação do registo RMI: " + e.getMessage());
            System.exit(1);
        }
        System.out.println("O registo RMI foi criado!");

        try {
            reg = (Register) registry.lookup(nameEntryBase);
        } catch (RemoteException e) {
            System.out.println("RegisterRemoteObject lookup exception: " + e.getMessage());
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("RegisterRemoteObject not bound exception: " + e.getMessage());
            System.exit(1);
        }

        try {
            reg.bind(nameEntryObject, generalRepositoryInterface);
        } catch (RemoteException e) {
            System.out.println("Excepção no registo do General Repository: " + e.getMessage());
            System.exit(1);
        } catch (AlreadyBoundException e) {
            System.out.println("O General Repository já está bound: " + e.getMessage());
            System.exit(1);
        }

        System.out.println("O General Repository foi registado!");
    }
}
