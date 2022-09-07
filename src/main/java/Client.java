import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client{

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String userName;

    public Client(Socket socket, String userName) {
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.userName = userName;
        } catch (IOException e){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    private void sendMessage(){
String msg;
        try {
            bufferedWriter.write(userName);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()) {
                msg = scanner.nextLine();
                bufferedWriter.write(userName + " : " + msg);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        }catch (IOException e){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }

    }
    private void receiveMessage(){
        String msg;
        try {
           msg =  bufferedReader.readLine();
            System.out.println(userName + " : " + msg);

        }catch (IOException e){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }

    }

    private void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (socket != null) {
                socket.close();
            }
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please, write your username ");
        String username = scanner.nextLine();
        Socket socket1 = new Socket("localhost", 1234);
        Client client = new Client(socket1, username);
        client.sendMessage();
        client.receiveMessage();

    }
}