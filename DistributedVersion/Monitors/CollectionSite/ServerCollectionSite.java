package DistributedVersion.Monitors.CollectionSite;

import DistributedVersion.ComInfo.ServerCom;
import genclass.GenericIO;

import java.net.SocketTimeoutException;

import static DistributedVersion.ComInfo.ComPorts.*;

/**
 * Created by pmatos9 on 18/04/17.
 */
public class ServerCollectionSite {

        public static void main(String[] args) {

            CollectionSite escritorio = null;
            CollectionSiteInterface escritorioInterface;
            ServerCom scon, sconi;
            ClientProxy cliProxy;


            String museu = machine_museum;
            String concentrationSite = machine_concentration;
            String assaultGroup =machine_party;
            String generalRepository =machine_log;

            scon = new ServerCom(portCollectionSite);
            scon.start();
            escritorio = new CollectionSite(museu,concentrationSite, assaultGroup, generalRepository);
            escritorioInterface = new CollectionSiteInterface(escritorio);
            GenericIO.writelnString("Service ConcentrationSite has been established!");
            GenericIO.writelnString("Server listening.");


        /*
         * processamento de pedidos
         */

            boolean keepAlive = true;

            while (keepAlive) {
                try{
                    sconi = scon.accept();
                    cliProxy = new ClientProxy(sconi, escritorioInterface);
                    cliProxy.start();
                }catch(SocketTimeoutException e){
                    if(!escritorioInterface.isAlive()){
                        keepAlive = false;
                    }
                }

            }

            GenericIO.writelnString("ConcentrationSite has been terminated!");

        }
}

