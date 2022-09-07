import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable{

    private static List<ClientHandler> clientHandlerList = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String userName;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            userName = bufferedReader.readLine();
            clientHandlerList.add(this);
            printMessage("Server : " + userName + " has joined the chat!");
        } catch (IOException e){
            closeEverything(socket, bufferedWriter, bufferedReader);
        }
    }

    private void closeEverything(Socket socket, BufferedWriter bufferedWriter, BufferedReader bufferedReader) {
        removeClientHandler();
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

    private void removeClientHandler() {
        clientHandlerList.remove(this);
        System.out.println("SERVER : " + userName + " has left the chat!");
    }

    @Override
    public void run() {
        String msg;
       while (socket.isConnected()){
           try {
               msg = bufferedReader.readLine();
               printMessage(msg);
               System.out.println(msg);
           }catch (IOException e){
               closeEverything(socket, bufferedWriter, bufferedReader);
               break;
           }


        }

    }

    private void printMessage(String message){
        for(ClientHandler handler: clientHandlerList){
            try {
                if (handler.userName.equals(userName)){
                    handler.bufferedWriter.write(message);
                    handler.bufferedWriter.newLine();
                    handler.bufferedWriter.flush();
                }
            } catch (IOException e){
                closeEverything(socket, bufferedWriter, bufferedReader);
            }
        }

    }
}