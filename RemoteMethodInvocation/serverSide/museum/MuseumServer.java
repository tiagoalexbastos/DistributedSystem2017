package serverSide.museum;

import genclass.GenericIO;
import interfaces.GeneralRepositoryInterface;
import interfaces.MuseumInterface;
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
public class MuseumServer {

    public static void main(String[] args) {

        /* obtenção da localização do serviço de registo RMI */
//        String rmiRegHostName = RegistryConfig.RMI_REGISTRY_HOSTNAME;
//        int rmiRegPortNumb = RegistryConfig.RMI_REGISTRY_PORT;
        String rmiRegHostName = args[0];
        int rmiRegPortNumb = Integer.parseInt(args[1]);

        /* localização por nome do objecto remoto no serviço de registos RMI */
        GeneralRepositoryInterface generalRepositoryInterface = null;

        try {
            Registry registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
            generalRepositoryInterface = (GeneralRepositoryInterface)
                    registry.lookup(RegistryConfig.RMI_REGISTRY_GENREPO_NAME);
        } catch (RemoteException e) {
            System.out.println("Excepção na localização do General Repository: " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("O General Repository não está registado: " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        /* instanciação e instalação do gestor de segurança */
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        GenericIO.writelnString("Security manager was installed!");


        /* instanciação do objecto remoto que representa o Museu e geração de um stub para ele */
        Museum museum = new Museum(generalRepositoryInterface, rmiRegHostName, rmiRegPortNumb);
        MuseumInterface museumInterface = null;

        try {
            museumInterface = (MuseumInterface)
                    UnicastRemoteObject.exportObject(museum, RegistryConfig.RMI_MUSEUM_PORT);
        } catch (RemoteException e) {
            System.out.println("Excepção na geração do stub para o Museum: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("O stub para o Museum foi gerado!");


              /* seu registo no serviço de registo RMI */
        String nameEntryBase = RegistryConfig.RMI_REGISTER_NAME;
        String nameEntryObject = RegistryConfig.RMI_REGISTRY_MUSEUM_NAME;
        Registry registry = null;
        Register reg = null;

        try {
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException e) {
            System.out.println("Excepção na criação do registo RMI: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("O registo RMI foi criado!");

        try {
            reg = (Register) registry.lookup(nameEntryBase);
        } catch (RemoteException e) {
            System.out.println("RegisterRemoteObject lookup exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("RegisterRemoteObject not bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        try {
            reg.bind(nameEntryObject, museumInterface);
        } catch (RemoteException e) {
            System.out.println("Excepção no registo do Museum: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (AlreadyBoundException e) {
            System.out.println("O Museum já está registado: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        System.out.println("O museu foi registado!!");
    }
}
