package cc.eoma.clipboard.synchronizer.receiver;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import cc.eoma.clipboard.synchronizer.ReceiverHandler;

/**
 * @Description:
 * @Author goma
 * @Date 2021/8/25 下午12:29
 * @Version 1.0
 */
public class BroadcastReceiver {

    public void receive(ReceiverHandler handler) {
        try (DatagramSocket datagramSocket = new DatagramSocket(3001)) {
            byte[] buf = new byte[1024];
            DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length);
            while (true) {
                this.handler(datagramSocket, datagramPacket, handler);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void handler(DatagramSocket datagramSocket, DatagramPacket datagramPacket, ReceiverHandler handler) {
        try {
            datagramSocket.receive(datagramPacket);
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
