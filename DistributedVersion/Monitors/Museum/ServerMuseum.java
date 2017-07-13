package DistributedVersion.Monitors.Museum;

import DistributedVersion.ComInfo.ServerCom;
import genclass.GenericIO;

import java.net.SocketTimeoutException;

import static DistributedVersion.ComInfo.ComPorts.*;

/**
 * Created by pmatos9 on 17/04/17.
 */
public class ServerMuseum {


    public static void main(String[] args)  {

        Museum museu;
        MuseumInterface museuInter;
        ServerCom scon, sconi;
        ClientProxy cliProxy;


        String generalRepository = machine_log;

        scon = new ServerCom(portMuseum);
        scon.start();
        museu = new Museum(generalRepository);
        museuInter = new MuseumInterface(museu);
        GenericIO.writelnString("Service Museum has been established!");
        GenericIO.writelnString("Server listening.");


        /*
         * processamento de pedidos
         */

        boolean keepAlive = true;

        while (keepAlive) {
            try{
                sconi = scon.accept();
                cliProxy = new ClientProxy(sconi, museuInter);
                cliProxy.start();
            }catch(SocketTimeoutException e){
                if(!museuInter.isAlive()){
                    keepAlive = false;
                }
            }

        }

        GenericIO.writelnString("Service Museum has been terminated!");
    }
}
