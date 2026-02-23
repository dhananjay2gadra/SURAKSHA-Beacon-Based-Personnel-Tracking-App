/*
 * Decompiled with CFR 0.145.
 */
package tata.aut.tatasurksha;

public class Response {
    private boolean status;
    private int messageNumber;
    private byte[] packet;

    public Response(boolean status, int messageNumber, byte[] packet) {
        this.status = status;
        this.messageNumber = messageNumber;
        this.packet = packet;
    }

    public boolean isStatus() {
        return this.status;
    }

    public int getMessageNumber() {
        return this.messageNumber;
    }

    public byte[] getPacket() {
        return this.packet;
    }
}

