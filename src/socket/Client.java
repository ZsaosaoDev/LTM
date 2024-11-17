package socket;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.StringTokenizer;

public class Client {
    Socket socket;
    DataInputStream netIn;
    DataOutputStream netOut;

    public Client(Socket socket) throws IOException {
        this.socket = socket;
        netIn = new DataInputStream(socket.getInputStream());
        netOut = new DataOutputStream(socket.getOutputStream());

    }

    public void run() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line, com, des, source; // com: type of send to server (send, get, exit) des: destination file source:
                                       // source file

        try {
            while (true) {
                line = reader.readLine();
                if ("exit".equals(line)) {
                    netOut.writeUTF("exit");
                    break;
                }
                StringTokenizer st = new StringTokenizer(line);
                com = st.nextToken();
                source = st.nextToken();
                des = st.nextToken();

                Long size = new File(source).length();
                netOut.writeUTF(com + " " + des);
                netOut.flush();
                netOut.writeLong(size);
                netOut.flush();

                int count;
                byte[] buff = new byte[102400];
                FileInputStream fis = new FileInputStream(source);
                while ((count = fis.read(buff)) > 0) {
                    netOut.write(buff, 0, count);
                }
                netOut.flush();
                fis.close();
                System.out.println(netIn.readUTF());

            }

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 2000);
            Client client = new Client(socket);
            client.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
