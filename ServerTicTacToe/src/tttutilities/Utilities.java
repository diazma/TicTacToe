package tttutilities;

import java.awt.Point;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Diaz
 */
public class Utilities {

    public static boolean stringMoveIsValid(String move, char[][] board) {

        if (move == null || move.length() != 3) {
            return false;
        }
        Point moveInPoint = convertToInt(move);

        if (moveInPoint.x < 0 || moveInPoint.x > 2) {
            return false;
        }
        if (moveInPoint.y < 0 || moveInPoint.y > 2) {
            return false;
        }

        char current = board[moveInPoint.x][moveInPoint.y];
        if (current != ' ') {
            return false;
        }

        return true;

    }

    public static Point convertToInt(String move) {
        char row = move.charAt(0);
        char column = move.charAt(2);
        int rowValue = row - 48;
        int columnValue = column - 48;
        Point movePoint = new Point(rowValue, columnValue);
        return movePoint;
    }

    public static char[][] addMove(Point inputMove, boolean isPlayerX, char[][] board) {
        // Assuming the string is valid

        if (isPlayerX) {
            board[inputMove.x][inputMove.y] = 'X';
        } else if (!isPlayerX) {
            board[inputMove.x][inputMove.y] = 'O';
        }
        
        return board;
    }

    private static char isThereWinner(char[][] board) {
        boolean winnerExists = false;
        boolean isRow = false;
        boolean isColumn = false;
        boolean isDiagonal = false;
        char winnerType = ' ';
        
        
        // Check diagonal
        boolean diagonalLeftToRight = (board[0][0] == board[1][1]) && (board[1][1] == board[2][2]);
        boolean diagonalRightToLeft = (board[2][0] == board[1][1]) && (board[1][1] == board[0][2]);
        if (diagonalLeftToRight || diagonalRightToLeft) {
            isDiagonal = true;
            winnerType = board[1][1];
        }

            // Check vertical lines & horizontal
        for (int i = 0, j = 0; i < 3 || j < 3; i++, j++) {
            boolean currentRowIsEqual = (board[i][0] == board[i][1]) && (board[i][1] == board[i][2]);
            boolean isNotEmpty = (board[i][0] != ' ');
            if ( currentRowIsEqual && isNotEmpty ) {
                isRow = true;
                winnerType = board[i][0];
                break;
            }

        }
        for (int j = 0; j < 3; j++) {
            boolean currentColumnIsEqual = (board[0][j] == board[1][j]) && (board[1][j] == board[2][j]);
            boolean isNotEmpty = board[0][j] != ' ';
            if ( currentColumnIsEqual && isNotEmpty ) {
                isColumn = true;
                winnerType = board[0][j];
                break;
            }
        }
        // Check any winning case
        if (isDiagonal || isRow || isColumn) {
            winnerExists = true;
            
        }
        
        // Check the tie scenario
        if(isThereTie(board)){
            return 'T';
            
        }
        if (winnerExists) {
            if (winnerType == 'X') {
                return 'X';
            } else if (winnerType == 'O') {
                return 'O';
            }

        }
        
        return ' ';
    }

    public static char[][] initialize() {
        char[][]board = new char[3][3];
        board = new char[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
            }
        }
        
        return board;

    }

    public final static void drawBoard(char[][] board) {
        /* First line */
        System.out.println(board[0][0] + " || " + board[0][1] + " || " + board[0][2]);
        System.out.println("--||---||---");
        System.out.println(board[1][0] + " || " + board[1][1] + " || " + board[1][2]);
        System.out.println("--||---||---");
        System.out.println(board[2][0] + " || " + board[2][1] + " || " + board[2][2]);
    }
    
    public static void main (String args[]){
        char[][] someBoard;
        someBoard = initialize();
        System.out.println("Checking Horizontal line");
        System.out.println("Creating a new board... ");
        drawBoard(someBoard);
        someBoard[0][0] = 'X'; 
        someBoard[0][1] = 'X'; 
        someBoard[0][2] = 'X';
        boolean isGameOver = false;
        System.out.println("... and adding sample values");
        drawBoard(someBoard);
        
        char winner = isThereWinner(someBoard);
        isGameOver = announceWinner(winner);
        System.out.println("***");
        
        System.out.println("Is game over? " + isGameOver);
        
        System.out.println("Checking Vertical line");
        someBoard = initialize();
        System.out.println("Creating a new board... ");
        drawBoard(someBoard);
        someBoard[0][0] = 'X'; 
        someBoard[1][0] = 'X'; 
        someBoard[2][0] = 'X';
        winner = isThereWinner(someBoard);
        System.out.println("... and adding sample values");
        drawBoard(someBoard);
        announceWinner(winner);
        System.out.println("Is game over? " + isGameOver);
        
        someBoard = initialize();
         System.out.println("Checking Diagonal line");
        System.out.println("Creating a new board... ");
        drawBoard(someBoard);
        someBoard[0][0] = 'O'; 
        someBoard[1][1] = 'O'; 
        someBoard[2][2] = 'O';
        winner = isThereWinner(someBoard);
        System.out.println("... and adding sample values");
        drawBoard(someBoard);
        announceWinner(winner);
        System.out.println("Is game over? " + isGameOver);
        
    }

    private static boolean announceWinner(char winner) {
        if(winner != ' '){
            System.out.println("Player " + winner+  " wins");
            return true;
        }
        
        return false;
        
    }
    
    public static boolean statusOfGame(char[][] board){
        char winner = isThereWinner(board);
        
        if(winner == 'T'){
            System.out.println("No winners. Tie has occurred");
            return true;
        }
        else if(winner != ' '){
            System.out.println("Player " + winner+  " wins");
            return true;
        }
         
        else{
            return false;
        }
    }
    
    public static void isGameOverAnnouncement(boolean statusOfGame){
        if(statusOfGame){
            System.out.println("Game is over");
            
        }
    }

    private static boolean isThereTie(char[][] board) {
        for (int i=0; i<3; i++){
            for(int j = 0; j<3; j++){
                if(board[i][j] == ' '){
                    return false;
                }
            }
        }
        
        return true;
    }
}
