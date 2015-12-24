import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Fuzhong on 2015/12/23.
 */
public class NIOServer {

    public static void main (String [] args) throws IOException {
        int port = 9999;
        if (args != null && args.length > 0) {
            try{
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {

            }
        }
        ServerSocket server = null;
        try{
            server = new ServerSocket(port);
            System.out.println("The NIO server is start in port:" + port);
            Socket socket = null;
            while (true) {
                socket = server.accept();
                new Thread(new NIOSocketHandler(socket)).start();
            }
        }finally {
            if (server != null) {
                System.out.println("The NIO server close!");
                server.close();
                server = null;
            }
        }

    }

}
