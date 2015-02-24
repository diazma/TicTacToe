/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clienttictactoe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import tttutilities.Utilities;
import static tttutilities.Utilities.initialize;

/**
 *
 * @author Diaz
 */
public final class ClientTicTacToe {

    private final Socket clientSocket;
    private final BufferedReader in;
    private final PrintWriter out;
    private char board[][];
    private static final int PORT = 6189;
    private static final String ADDRESS = "localhost";
    boolean isGameOver = false;

    public ClientTicTacToe() throws Exception {

        clientSocket = new Socket(ADDRESS, PORT);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream(), true);

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            System.out.println("Connecting to server...");
            ClientTicTacToe client = new ClientTicTacToe();
            client.play();

        } catch (Exception ex) {
            Logger.getLogger(ClientTicTacToe.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void play() throws Exception {

        boolean isValidMoveFromServer = false;
        String input = " ";
        System.out.println("Connected to server. Game starts!");
        board = initialize();
        Utilities.drawBoard(board);
        System.out.println("You are player X!");

        try {
            while (!isGameOver) {
                // ask player on client side and update the board
                askPlayerX();
                
                if(isGameOver){
                    break;
                }
                System.out.println("Waiting for Player O");

                isValidMoveFromServer = false;
                while (!isValidMoveFromServer) {
                    input = in.readLine();
                    if (input != null && input.length() == 3) {
                        isValidMoveFromServer = true;
                    }

                }

            // add move to the board   
                board = Utilities.addMove(Utilities.convertToInt(input), false, board);
                Utilities.drawBoard(board);
                isGameOver = Utilities.statusOfGame(board);
                

            }
        } finally {
            System.out.println("Game is over.");
            clientSocket.close();
        }

    }

    public final void drawBoard() {
        /* First line */
        System.out.println(board[0][0] + " || " + board[0][1] + " || " + board[0][2]);
        System.out.println("--||---||---");
        System.out.println(board[1][0] + " || " + board[1][1] + " || " + board[1][2]);
        System.out.println("--||---||---");
        System.out.println(board[2][0] + " || " + board[2][1] + " || " + board[2][2]);
    }

    private void askPlayerX() throws IOException {
        // ask user in this side, get string and send it to client
        // register move in the board

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String userResponse = "";
        boolean isValidResponse = false;
        while (!isValidResponse) {
            
            System.out.print("type your move (eg., \'1 1\'):  ");
            userResponse = br.readLine();
            isValidResponse = Utilities.stringMoveIsValid(userResponse, board);
            if(!isValidResponse){
                System.out.println("Wrong format for move or cell was already played. Try again.");
            }           
        }

        // once we are sure the response has correct format
        // send to Server
        out.println(userResponse);

        // add move to the board in client side
        board = Utilities.addMove(Utilities.convertToInt(userResponse), true, board);
        Utilities.drawBoard(board);
        isGameOver = Utilities.statusOfGame(board);
    }

}
