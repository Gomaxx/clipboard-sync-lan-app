package cc.eoma.clipboard.synchronizer.receiver;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import cc.eoma.clipboard.synchronizer.ReceiverHandler;

public class MultiCastReceiver {
    public void receive(String multiCastAddr, Integer port, ReceiverHandler handler) {
        try (MulticastSocket multicastSocket = new MulticastSocket(port)) {
            multicastSocket.joinGroup(InetAddress.getByName(multiCastAddr));
            byte[] bytes = new byte[1024 * 10];
            DatagramPacket datagramPacket = new DatagramPacket(bytes, 0, bytes.length);

            while (true) {
                this.handler(multicastSocket, datagramPacket, handler);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void handler(MulticastSocket multicastSocket, DatagramPacket datagramPacket, ReceiverHandler handler) {
        try {
            multicastSocket.receive(datagramPacket);
            if (Objects.equals(InetAddress.getLocalHost().getHostAddress(), datagramPacket.getAddress().getHostAddress())) {
                return;
            }
            String message = new String(datagramPacket.getData(), 0, datagramPacket.getLength(), StandardCharsets.UTF_8);
            // byte[] xxx = new byte[datagramPacket.getLength()];
            // System.arraycopy(datagramPacket.getData(), 0, xxx, 0, datagramPacket.getLength());
            handler.process(message);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}