import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author wafaa
 */
public class ClientController extends Thread {

    List<String> onlineUsers = new ArrayList<String>();
    public transient static String online = "online";
    public static boolean nextplay = true;
    public static String[] msg=new String[3];
    public static String getOnline() {
        return online;
    }
    
     public static String[] getMsg() {
        return msg;
    }
    
    Socket mySocket;
    public  DataInputStream in;
    public  PrintStream out;
    Boolean conFlag;
    //ShowPlayScrean sps;

    public ClientController() {
        System.out.println("conroller");
        conFlag = connect();
        start();
    }

    public void send(String data) {
        System.out.println("send");
        while (!conFlag) {
            //reconnect it connection lost
            conFlag = connect();
        }
        System.out.println(data);
        System.out.println("send try");
        this.out.println(data);
        System.out.println("done");
    }

    public String[] receive() throws IOException {
        System.out.println("recieve method");
        String[] msg = new String[3];
        try {
            System.out.println("****************************************");
            String data = this.in.readLine();

            System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$: " + data);
            msg = data.split(":");
            if(data.contains("x")){
                System.out.println("***********&&&&&&&&&&&&&&&&&&########:"+data);
            }

        } catch (Exception ex) {
            System.out.println("exception recieve");
            ex.printStackTrace();
        }
        return msg;

    }

    public void run() {
        while (true) {
            try {
                //handel receiving msgs

                String[] replyMsg = receive();
                System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%" + replyMsg[0]);
                //replyMsg.forEach((k) -> System.out.println(k));
                for (String s : replyMsg) {
                    System.out.println(s);
                }
                switch (replyMsg[0]) {
                    case "login":
                        System.out.println("userslist:");
                        onlineUsers.add(replyMsg[1]);
                        for (String s : replyMsg) {
                            System.out.println(s);
                        }
                        break;

                    case "x":
                        System.out.println("******&&&&&&&&&&&&&&&&&&&&&&&&x played");
                        int i=0;
                        for (String s : replyMsg) {
                            System.out.print(s);
                            msg[i]=replyMsg[i];
                            i++;
                        }
                        break;

                    case "o":
                        System.out.println("******&&&&&&&&&&&&&&&&&&&&&&&&x played");
                        int j=0;
                        for (String s : replyMsg) {
                            System.out.print(s);
                            System.out.print(s);
                            msg[j]=replyMsg[j];
                            j++;
                        }
                        break;

                        
                    case "online":
                        System.out.print("online client:");
                        for (int k = 1; k < replyMsg.length; k++) {
                            System.out.println(replyMsg[k]);
                            online += (":" + replyMsg[k]);
                        }
                        System.out.println(online);

                        break;

                    case "startonline":
                        String player2name = replyMsg[1];
                        String turn_flag = replyMsg[2];
                        break;

                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    private Boolean connect() {
        try {
            System.out.println("socked started");
            System.out.println("conrollerfun");
            mySocket = new Socket("127.0.0.1", 5008);
            out = new PrintStream(mySocket.getOutputStream());
            in = new DataInputStream(mySocket.getInputStream());
            return true;
        } catch (IOException ex) {
            //ex.printStackTrace();
            return false;
        }
    }

}
