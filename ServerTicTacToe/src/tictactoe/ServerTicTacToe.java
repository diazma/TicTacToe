/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import tttutilities.Utilities;

/**
 *
 * @author Diaz
 */
public class ServerTicTacToe {

    private static int PORT = 6189;

    ServerSocket serverSocket = null;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            ServerTicTacToe game = new ServerTicTacToe();

        } catch (IOException ex) {
            Logger.getLogger(ServerTicTacToe.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static class ServerGame extends Thread {

        private Socket socket;
        private char board[][];
        String response;
        PrintWriter out;
        BufferedReader in;
        boolean isGameOver = false;

        public ServerGame(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                System.out.println("You are player O");
                System.out.println("Waiting for client move");
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                boolean isValidMoveFromClient = false;
                String input = " ";
                board = Utilities.initialize();
                Utilities.drawBoard(board);

                while (!isGameOver) {
                    isValidMoveFromClient = false;
                    while (!isValidMoveFromClient) {
                        System.out.println("Waiting for Player X");
                        input = in.readLine();
                        if (input != null && input.length() == 3) {
                            isValidMoveFromClient = true;

                        }
                    }
                    if (Utilities.stringMoveIsValid(input, board)) {
                        board = Utilities.addMove(Utilities.convertToInt(input), true, board);
                        Utilities.drawBoard(board);
                        isGameOver = Utilities.statusOfGame(board);
                        if(isGameOver){
                            break;
                        }
                    }

                    askPlayerO();
                }
            } catch (IOException e) {

            } finally {
                try {
                    System.out.println("Game is over");
                    socket.close();
                    System.exit(0);
                } catch (IOException e) {

                } 
            }
        }

        private void askPlayerO() throws IOException {
            // ask user in this side, get string and send it to client
            // register move in the board

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String userResponse = "";
            boolean isValidResponse = false;
            while (!isValidResponse) {

                System.out.print("type your move (eg., \'1 1\'):  ");
                userResponse = br.readLine();
                isValidResponse = Utilities.stringMoveIsValid(userResponse, board);
                if (!isValidResponse) {
                    System.out.println("Wrong format for move or cell was already played. Try again.");
                }

            }

            // once we are sure the response has correct format
            // send to client
            out.println(userResponse);

            // add move to the board in server side
            board = Utilities.addMove(Utilities.convertToInt(userResponse), false, board);
            Utilities.drawBoard(board);
            isGameOver = Utilities.statusOfGame(board);

        }
    }

    public ServerTicTacToe() throws IOException {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Game about to start. Waiting for opponent player to connect...");

            while (true) {
                new ServerGame(serverSocket.accept()).start();
                System.out.println("Opponent connected. Game starts!");
            }

        } finally {
            serverSocket.close();
        }

    }

}
