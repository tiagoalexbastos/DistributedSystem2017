package DistributedVersion.Messages;

import java.io.Serializable;

/**
 * Created by pmatos9 on 17/04/17.
 */
public class MuseumMessage implements Serializable {

    /**
     * Serialization key
     */
    private static final long serialVersionUID = 1000L;
    /**
     * Message to steal painting
     */
    public static final int STEALPAINTING = 0;
    /**
     * Answer with stolen painting
     */
    public static final int SENDRESPROUBARQUADRO = 1;
    /**
     * Message to get distance
     */
    public static final int GETDISTANCE = 2;
    /**
     * Answer to send distance
     */
    public static final int SENDDISTANCIA = 3;
    /**
     * Message to get number of stolen paintings
     */
    public static final int GETNUMBEROFSTOLENGPAINTINGS = 4;
    /**
     * Answer with the number of paintings
     */
    public static final int SENDNUMEROQUADROS = 5;
    /**
     * Message to end server
     */
    public static final int END = 99;
    /**
     * Message type
     */
    private int msgType = -1;
    /**
     * Room id
     */
    private int nrSala = -1;
    /**
     * Room distance
     */
    private int distanciaSala = -1;
    /**
     * Nr paintings
     */
    private int nrQuadros = -1;
    /**
     * Painting stolen
     */
    private boolean quadroRoubado = false;

    /**
     * To String
     * @return string with content
     */
    @Override
    public String toString() {
        return ("Type "+msgType+"\n nrSala "+nrSala+"\n distancia Sala "+distanciaSala+"\n nrQuadros "+ nrQuadros+"\n quadro "+quadroRoubado);
    }



    /**
     * Possible message
     * @param type message type
     */
    public MuseumMessage(int type) {
        this.msgType = type;
    }

    /**
     * Possible message
     * @param type message type
     * @param info information
     */
    public MuseumMessage(int type, int info) {
        this.msgType = type;

        switch (type) {
            case MuseumMessage.GETDISTANCE:
                this.nrSala = info;
                break;
            case MuseumMessage.GETNUMBEROFSTOLENGPAINTINGS:
                this.nrSala = info;
                break;
            case MuseumMessage.STEALPAINTING:
                this.nrSala = info;
                break;
            case MuseumMessage.SENDDISTANCIA:
                this.distanciaSala = info;
                break;
            case MuseumMessage.SENDNUMEROQUADROS:
                this.nrQuadros = info;
                break;
        }

    }

    /**
     * Possible message
     * @param type message type
     * @param info boolean information
     */
    public MuseumMessage(int type, boolean info) {
        this.msgType = type;
        this.quadroRoubado = info;
    }

    /**
     * Getter if painting has been stoled
     * @return true if stolen painting
     */
    public boolean getQuadroRoubado() {
        return this.quadroRoubado;
    }

    /**
     * Getter to number of paintings in a room
     * @return number of paintings
     */
    public int getNrQuadros() {
        return this.nrQuadros;
    }

    /**
     * Getter to distance of room
     * @return distance of room
     */
    public int getDistanciaSala() {
        return this.distanciaSala;
    }

    /**
     * Getter to the id of the room
     * @return id of the room
     */
    public int getNrSala() {
        return this.nrSala;
    }

    /**
     * Getter to the type of message
     * @return type of message
     */
    public int getType() {
        return this.msgType;
    }
}

