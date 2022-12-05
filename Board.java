public class Board {
    private final String[] number_coordinates = {"8", "7", "6", "5", "4", "3", "2", "1"};
    private final String[] letter_coordinates = {"a", "b", "c", "d", "e", "f", "g", "h"};
    private final String boundary = "-------";

    public final char black_piece = 'b';
    public final char white_piece = 'w';

    public final char empty_cell = ' ';
    public final char[][] initial_board = {	{empty_cell, empty_cell, empty_cell, empty_cell, empty_cell, empty_cell, empty_cell, empty_cell},
            {empty_cell, empty_cell, empty_cell, empty_cell, empty_cell, empty_cell, empty_cell, empty_cell},
            {empty_cell, empty_cell, empty_cell, empty_cell, empty_cell, empty_cell, empty_cell, empty_cell},
            {empty_cell, empty_cell, empty_cell, white_piece, black_piece, empty_cell, empty_cell, empty_cell},
            {empty_cell, empty_cell, empty_cell, black_piece, white_piece, empty_cell, empty_cell, empty_cell},
            {empty_cell, empty_cell, empty_cell, empty_cell, empty_cell, empty_cell, empty_cell, empty_cell},
            {empty_cell, empty_cell, empty_cell, empty_cell, empty_cell, empty_cell, empty_cell, empty_cell},
            {empty_cell, empty_cell, empty_cell, empty_cell, empty_cell, empty_cell, empty_cell, empty_cell}};


    private void printBoundary() {
        System.out.print("  ");
        for (int j = 0; j < 7; j++) {
            System.out.print(boundary);
        }
    }
    private void printLetters() {
        for (int j = 0; j < 8; j++) {
            System.out.print("     ");
            System.out.print(letter_coordinates[j]);
        }
    }

    public String isAnyEmptyCells(char[][] game_board) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (game_board[i][j] == empty_cell) {
                    return "Board does have empty cell";
                }
            }
        }
        return "Board doesn't have empty cell";
    }
    public void printTheBoard(char[][] game_board) {
        for (int i = 0; i < 8; i++) {
            printBoundary();
            System.out.print('\n');
            System.out.print(number_coordinates[i]);
            for (int j = 0; j < 8; j++) {
                System.out.print("|   ");
                System.out.print(game_board[i][j]);
                System.out.print(" ");
            }
            System.out.print("|");
            System.out.print('\n');
        }
        printBoundary();
        System.out.print('\n');
        printLetters();
    }
}
