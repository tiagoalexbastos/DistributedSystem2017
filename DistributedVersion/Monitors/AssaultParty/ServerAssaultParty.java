package DistributedVersion.Monitors.AssaultParty;

import DistributedVersion.ComInfo.ServerCom;
import genclass.GenericIO;

import java.net.SocketTimeoutException;

import static DistributedVersion.ComInfo.ComPorts.*;

/**
 * Created by pmatos9 on 18/04/17.
 */
public class ServerAssaultParty {

    public static void main(String[] args) {


        String museu = machine_museum;


        String generalRepository = machine_log;

        AssaultPartyInterface grupoAssaltoInter;
        ServerCom scon, sconi;

        ClientProxy cliProxy;

        scon = new ServerCom(portAssaultGroup);
        scon.start();
        AssaultParty[] grupo = new AssaultParty[2];
        grupo[0] = null;
        grupo[1] = null;
        grupoAssaltoInter = new AssaultPartyInterface(grupo, museu, generalRepository);
        GenericIO.writelnString("Assault party server has been established!");
        GenericIO.writelnString("Server is listening!");




        boolean keepAlive = true;

        while (keepAlive) {
            try{
                sconi = scon.accept();
                cliProxy = new ClientProxy(sconi, grupoAssaltoInter);
                cliProxy.start();
            }catch(SocketTimeoutException e){
                if(!grupoAssaltoInter.isAlive()){
                    keepAlive = false;
                }
            }

        }

        GenericIO.writelnString("Assault Party server has been terminated");
    }
}

