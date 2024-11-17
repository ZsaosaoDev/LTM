package socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;

public class Server extends Thread {
    Socket socket;
    DataInputStream netIn;
    DataOutputStream netOut;

    public Server(Socket socket) throws IOException {
        this.socket = socket;
        netIn = new DataInputStream(socket.getInputStream());
        netOut = new DataOutputStream(socket.getOutputStream());

    }

    @Override
    public void run() {
        String line;
        String com, des;
        try {
            while (true) {
                line = netIn.readUTF();
                if ("exit".equals(line)) {
                    break;
                }
                StringTokenizer st = new StringTokenizer(line);
                com = st.nextToken();
                des = st.nextToken();

                long size = netIn.readLong();

                FileOutputStream fos = new FileOutputStream(des);
                dataCopy(netIn, fos, size);
                fos.close();
                netOut.writeUTF("Done");
                netOut.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    private static boolean dataCopy(DataInputStream fis, FileOutputStream fos, long pSize) throws IOException {
        byte[] buff = new byte[102400];
        long remain = pSize;

        while (remain > 0) {
            int byteMustRead = (int) (remain > buff.length ? buff.length : remain);
            int byteRead = fis.read(buff, 0, byteMustRead);
            if (byteRead == -1)
                return false;
            fos.write(buff, 0, byteRead);
            remain -= byteRead;
        }

        return true;
    }

}
