import java.util.Scanner;
import static java.lang.Math.abs;
import static java.lang.Math.max;

public class Game {

    Board Board = new Board();
    Player Player1 = new Player();
    Player Player2 = new Player();
    Player Player3 = new Player();
    Scanner SCANNER = new Scanner(System.in);
    boolean isPlayerMove = true;
    boolean player_1_move = true;
    public char[][] game_board = new char[8][8];
    int max_offset_x = 0;
    int offset_x = -1;
    int offset_y = -1;

    public void startGame() {
        resetBoard();
        System.out.print("Choose gamemode\nTo play against computer: Enter 0\nTo play against other player: Enter 1\n");
        String mode = SCANNER.next();
        if (mode.charAt(0) == '0') {
            System.out.print("\nPlayer # 1 plays with black pieces, Computer plays with white pieces\n");
            playAgainstComputer();
        } else if (mode.charAt(0) == '1'){
            System.out.print("\nPlayer # 1 plays with black pieces, Player # 2 plays with white pieces\n");
            playAgainstPlayer();
        } else {
            System.out.print("Wrong gamemode!\n");
            System.exit(-1);
        }
    }
    public void getTheScoreWithComputer() {
        int score1 = Player1.getScore();
        int score2 = Player2.getScore();
        System.out.print(" \n");
        System.out.print("Player score: ");
        System.out.print(score1);
        System.out.print(" \n");
        System.out.print("Computer score: ");
        System.out.print(score2);
        System.out.print(" \n");
    }

    public void getTheScoreWithPlayer() {
        int score1 = Player1.getScore();
        int score2 = Player3.getScore();
        System.out.print(" \n");
        System.out.print("Player #1 score: ");
        System.out.print(score1);
        System.out.print(" \n");
        System.out.print("Player #2 score: ");
        System.out.print(score2);
        System.out.print(" \n");
    }
    private void resetBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                game_board[i][j] = Board.initial_board[i][j];
            }
        }
        Player1.setScore(2);
        Player2.setScore(2);
    }

    public void updateTheScore() {
        int count_black_pieces = 0;
        int count_white_pieces = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (game_board[i][j] == Board.white_piece) {
                    count_white_pieces++;
                }
                if (game_board[i][j] == Board.black_piece) {
                    count_black_pieces++;
                }
            }
        }
        Player1.setScore(count_black_pieces);
        Player2.setScore(count_white_pieces);
    }

    public void updateTheScorePlayer() {
        int count_black_pieces = 0;
        int count_white_pieces = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (game_board[i][j] == Board.white_piece) {
                    count_white_pieces++;
                }
                if (game_board[i][j] == Board.black_piece) {
                    count_black_pieces++;
                }
            }
        }
        Player1.setScore(count_black_pieces);
        Player3.setScore(count_white_pieces);
    }
    public void playAgainstComputer() {
        while (endOfTheGame().equals("Game is still going")) {
            Board.printTheBoard(game_board);
            updateTheScore();
            getTheScoreWithComputer();
            nextMoveComputer();
        }
        Board.printTheBoard(game_board);
        updateTheScore();
        getTheScoreWithComputer();
        System.out.print("\nGame is ended!\n");
        System.exit(0);
    }

    public void playAgainstPlayer() {
        while (endOfTheGame().equals("Game is still going")) {
            Board.printTheBoard(game_board);
            updateTheScorePlayer();
            getTheScoreWithPlayer();
            nextMovePlayer();
        }
        Board.printTheBoard(game_board);
        updateTheScorePlayer();
        getTheScoreWithPlayer();
        System.out.print("\nGame is ended!\n");
        System.exit(0);
    }

    public void refreshEnemyCoordiantes(int[][] enemy_coordinates) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                enemy_coordinates[i][j] = -1;
            }
        }
    }
    public boolean checkEnemyPieceAround(int offset_y, int offset_x, char enemy_color) {
        if (offset_y > 0 && game_board[offset_y - 1][offset_x] == enemy_color) {
            return true;
        }
        if (offset_y < 7 && game_board[offset_y + 1][offset_x] == enemy_color) {
            return true;
        }
        if (offset_x > 0 && game_board[offset_y][offset_x - 1] == enemy_color) {
            return true;
        }
        if (offset_x < 7 && game_board[offset_y][offset_x + 1] == enemy_color) {
            return true;
        }
        if (offset_y < 7 && offset_x < 7 && game_board[offset_y + 1][offset_x + 1] == enemy_color) {
            return true;
        }
        if (offset_y > 0 && offset_x < 7 && game_board[offset_y - 1][offset_x + 1] == enemy_color) {
            return true;
        }
        if (offset_y < 7 && offset_x > 0 && game_board[offset_y + 1][offset_x - 1] == enemy_color) {
            return true;
        }
        if (offset_y > 0 && offset_x > 0 && game_board[offset_y - 1][offset_x - 1] == enemy_color) {
            return true;
        }
        return false;

    }
    public boolean setNewPiece(int offset_x, int offset_y, char enemy_color) {
        char my_color = enemy_color == 'w'? 'b' : 'w';
        boolean some_piece_captured = false;
        if (game_board[offset_y][offset_x] != Board.empty_cell) {
            System.out.print("\nThe cell is busy!");
            System.exit(-1);
        }
        if (!checkEnemyPieceAround(offset_y, offset_x, enemy_color)) {
            System.out.print("\nIllegal move!");
            System.exit(-1);
        }
        int off_from_enemy_piece = 2;
        game_board[offset_y][offset_x] = my_color;
        if (offset_y > 0 && game_board[offset_y - 1][offset_x] == enemy_color) {
            off_from_enemy_piece = 2;
            while (offset_y - off_from_enemy_piece >= 0) {
                if (game_board[offset_y - off_from_enemy_piece][offset_x] == enemy_color) {
                    off_from_enemy_piece++;
                } else if (game_board[offset_y - off_from_enemy_piece][offset_x] == my_color) {
                    while (off_from_enemy_piece >= 2) {
                        some_piece_captured = true;
                        off_from_enemy_piece--;
                        game_board[offset_y - off_from_enemy_piece][offset_x] = my_color;
                    }
                    break;
                } else if (game_board[offset_y - off_from_enemy_piece][offset_x] == Board.empty_cell) {
                    break;
                }
            }
        }

        if (offset_y < 7 && game_board[offset_y + 1][offset_x] == enemy_color) {
            off_from_enemy_piece = 2;
            while (offset_y + off_from_enemy_piece <= 7) {
                if (game_board[offset_y + off_from_enemy_piece][offset_x] == enemy_color) {
                    off_from_enemy_piece++;
                } else if (game_board[offset_y + off_from_enemy_piece][offset_x] == my_color) {
                    while (off_from_enemy_piece >= 2) {
                        some_piece_captured = true;
                        off_from_enemy_piece--;
                        game_board[offset_y + off_from_enemy_piece][offset_x] = my_color;
                    }
                    break;
                } else if (game_board[offset_y + off_from_enemy_piece][offset_x] == Board.empty_cell) {
                    break;
                }
            }
        }

        if (offset_x > 0 && game_board[offset_y][offset_x - 1] == enemy_color) {
            off_from_enemy_piece = 2;
            while (offset_x - off_from_enemy_piece >= 0) {
                if (game_board[offset_y][offset_x - off_from_enemy_piece] == enemy_color) {
                    off_from_enemy_piece++;
                } else if (game_board[offset_y][offset_x - off_from_enemy_piece] == my_color) {
                    while (off_from_enemy_piece >= 2) {
                        some_piece_captured = true;
                        off_from_enemy_piece--;
                        game_board[offset_y][offset_x - off_from_enemy_piece] = my_color;
                    }
                    break;
                } else if (game_board[offset_y][offset_x - off_from_enemy_piece] == Board.empty_cell) {
                    break;
                }
            }
        }

        if (offset_x < 7 && game_board[offset_y][offset_x + 1] == enemy_color) {
            off_from_enemy_piece = 2;
            while (offset_x + off_from_enemy_piece <= 7) {
                if (game_board[offset_y][offset_x + off_from_enemy_piece] == enemy_color) {
                    off_from_enemy_piece++;
                } else if (game_board[offset_y][offset_x + off_from_enemy_piece] == my_color) {
                    while (off_from_enemy_piece >= 2) {
                        some_piece_captured = true;
                        off_from_enemy_piece--;
                        game_board[offset_y][offset_x + off_from_enemy_piece] = my_color;
                    }
                    break;
                } else if (game_board[offset_y][offset_x + off_from_enemy_piece] == Board.empty_cell) {
                    break;
                }
            }
        }

        if (offset_x > 0 && offset_y > 0 && game_board[offset_y - 1][offset_x - 1] == enemy_color) {
            off_from_enemy_piece = 2;
            while (offset_x - off_from_enemy_piece >= 0 && offset_y - off_from_enemy_piece >= 0) {
                if (game_board[offset_y - off_from_enemy_piece][offset_x - off_from_enemy_piece] == enemy_color) {
                    off_from_enemy_piece++;
                } else if (game_board[offset_y - off_from_enemy_piece][offset_x - off_from_enemy_piece] == my_color) {
                    while (off_from_enemy_piece >= 2) {
                        some_piece_captured = true;
                        off_from_enemy_piece--;
                        game_board[offset_y - off_from_enemy_piece][offset_x - off_from_enemy_piece] = my_color;
                    }
                    break;
                } else if (game_board[offset_y - off_from_enemy_piece][offset_x - off_from_enemy_piece] == Board.empty_cell) {
                    break;
                }
            }
        }

        if (offset_x < 7 && offset_y < 7 && game_board[offset_y + 1][offset_x + 1] == enemy_color) {
            off_from_enemy_piece = 2;
            while (offset_x + off_from_enemy_piece <= 7 && offset_y + off_from_enemy_piece <= 7) {
                if (game_board[offset_y + off_from_enemy_piece][offset_x + off_from_enemy_piece] == enemy_color) {
                    off_from_enemy_piece++;
                } else if (game_board[offset_y + off_from_enemy_piece][offset_x + off_from_enemy_piece] == my_color) {
                    while (off_from_enemy_piece >= 2) {
                        some_piece_captured = true;
                        off_from_enemy_piece--;
                        game_board[offset_y + off_from_enemy_piece][offset_x + off_from_enemy_piece] = my_color;
                    }
                    break;
                } else if (game_board[offset_y + off_from_enemy_piece][offset_x + off_from_enemy_piece] == Board.empty_cell) {
                    break;
                }
            }
        }


        if (offset_x < 7 && offset_y > 0 && game_board[offset_y - 1][offset_x + 1] == enemy_color) {
            off_from_enemy_piece = 2;
            while (offset_x + off_from_enemy_piece <= 7 && offset_y - off_from_enemy_piece >= 0) {
                if (game_board[offset_y - off_from_enemy_piece][offset_x + off_from_enemy_piece] == enemy_color) {
                    off_from_enemy_piece++;
                } else if (game_board[offset_y - off_from_enemy_piece][offset_x + off_from_enemy_piece] == my_color) {
                    while (off_from_enemy_piece >= 2) {
                        some_piece_captured = true;
                        off_from_enemy_piece--;
                        game_board[offset_y - off_from_enemy_piece][offset_x + off_from_enemy_piece] = my_color;
                    }
                    break;
                } else if (game_board[offset_y - off_from_enemy_piece][offset_x + off_from_enemy_piece] == Board.empty_cell) {
                    break;
                }
            }
        }

        if (offset_x > 0 && offset_y < 7 && game_board[offset_y + 1][offset_x - 1] == enemy_color) {
            off_from_enemy_piece = 2;
            while (offset_x - off_from_enemy_piece >= 0 && offset_y + off_from_enemy_piece <= 7) {
                if (game_board[offset_y + off_from_enemy_piece][offset_x - off_from_enemy_piece] == enemy_color) {
                    off_from_enemy_piece++;
                } else if (game_board[offset_y + off_from_enemy_piece][offset_x - off_from_enemy_piece] == my_color) {
                    while (off_from_enemy_piece >= 2) {
                        some_piece_captured = true;
                        off_from_enemy_piece--;
                        game_board[offset_y + off_from_enemy_piece][offset_x - off_from_enemy_piece] = my_color;
                    }
                    break;
                } else if (game_board[offset_y + off_from_enemy_piece][offset_x - off_from_enemy_piece] == Board.empty_cell) {
                    break;
                }
            }
        }
        if (!some_piece_captured) {
            System.out.print("\nIllegal move!");
            System.exit(-1);
        }
        return true;
    }

    public void refreshMaxMove(double[][] max_move) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                max_move[i][j] = -1;
            }
        }
    }
    public void findMaxMove(char enemy_color) {
        offset_x = -1;
        offset_y = -1;
        double mx = -1;
        char my_color = enemy_color == 'w' ? 'b' : 'w';
        double[][] max_move = new double[8][8];
        refreshMaxMove(max_move);
        int off = 1;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (game_board[i][j] == enemy_color) {
                    if (i > 0 && game_board[i - 1][j] == my_color) {
                        off = 1;
                        while (i + off <= 7) {
                            if (game_board[i + off][j] == enemy_color) {
                                off++;
                            } else if (game_board[i + off][j] == my_color) {
                                break;
                            } else if (game_board[i + off][j] == Board.empty_cell) {
                                if (max_move[i + off][j] == -1) {
                                    if (i + off == 7 && (j == 0 || j == 7)) {
                                        max_move[i + off][j] = 0.8;
                                    } else if (i + off == 7) {
                                        max_move[i + off][j] = 0.4;
                                    } else {
                                        max_move[i + off][j] = 0;
                                    }
                                }
                                int pos = i + off;
                                while (off >= 1) {
                                    off--;
                                    if (i + off == 7 || j == 0 || j == 7) {
                                        max_move[pos][j] += 2;
                                    } else {
                                        max_move[pos][j] += 1;
                                    }
                                }
                                break;
                            }
                        }
                    }

                    if (i < 7 && game_board[i + 1][j] == my_color) {
                        off = 1;
                        while (i - off >= 0) {
                            if (game_board[i - off][j] == enemy_color) {
                                off++;
                            } else if (game_board[i - off][j] == my_color) {
                                break;
                            } else if (game_board[i - off][j] == Board.empty_cell) {
                                if (max_move[i - off][j] == -1) {
                                    if (i - off == 0 && (j == 0 || j == 7)) {
                                        max_move[i - off][j] = 0.8;
                                    } else if (i - off == 0) {
                                        max_move[i - off][j] = 0.4;
                                    } else {
                                        max_move[i - off][j] = 0;
                                    }
                                }
                                int pos = i - off;
                                while (off >= 1) {
                                    off--;
                                    if (i - off == 0 || j == 0 || j == 7) {
                                        max_move[pos][j] += 2;
                                    } else {
                                        max_move[pos][j] += 1;
                                    }
                                }
                                break;
                            }
                        }
                    }

                    if (j > 0 && game_board[i][j - 1] == my_color) {
                        off = 1;
                        while (j + off <= 7) {
                            if (game_board[i][j + off] == enemy_color) {
                                off++;
                            } else if (game_board[i][j + off] == my_color) {
                                break;
                            } else if (game_board[i][j + off] == Board.empty_cell) {
                                if (max_move[i][j + off] == -1) {
                                    if (j + off == 7 && (i == 0 || i == 7)) {
                                        max_move[i][j + off] = 0.8;
                                    } else if (j + off == 7) {
                                        max_move[i][j + off] = 0.4;
                                    } else {
                                        max_move[i][j + off] = 0;
                                    }
                                }
                                int pos = j + off;
                                while (off >= 1) {
                                    off--;
                                    if (j + off == 7 || i == 0 || i == 7) {
                                        max_move[i][pos] += 2;
                                    } else {
                                        max_move[i][pos] += 1;
                                    }
                                }
                                break;
                            }
                        }
                    }

                    if (j < 7 && game_board[i][j + 1] == my_color) {
                        off = 1;
                        while (j - off >= 0) {
                            if (game_board[i][j - off] == enemy_color) {
                                off++;
                            } else if (game_board[i][j - off] == my_color) {
                                break;
                            } else if (game_board[i][j - off] == Board.empty_cell) {
                                if (max_move[i][j - off] == -1) {
                                    if (j - off == 0 && (i == 0 || i == 7)) {
                                        max_move[i][j - off] = 0.8;
                                    } else if (j - off == 0) {
                                        max_move[i][j - off] = 0.4;
                                    } else {
                                        max_move[i][j - off] = 0;
                                    }
                                }
                                int pos = j - off;
                                while (off >= 1) {
                                    off--;
                                    if (j - off == 0 || i == 0 || i == 7) {
                                        max_move[i][pos] += 2;
                                    } else {
                                        max_move[i][pos] += 1;
                                    }
                                }
                                break;
                            }
                        }
                    }

                    if (i > 0 && j > 0 && game_board[i - 1][j - 1] == my_color) {
                        off = 1;
                        while (i + off <= 7 && j + off <= 7) {
                            if (game_board[i + off][j + off] == enemy_color) {
                                off++;
                            } else if (game_board[i + off][j + off] == my_color) {
                                break;
                            } else if (game_board[i + off][j + off] == Board.empty_cell) {
                                if (max_move[i + off][j + off] == -1) {
                                    if (i + off == 7 && j + off == 7) {
                                        max_move[i + off][j + off] = 0.8;
                                    } else if (i + off == 7 || j + off == 7) {
                                        max_move[i + off][j + off] = 0.4;
                                    } else {
                                        max_move[i + off][j + off] = 0;
                                    }
                                }
                                int pos_i = i + off;
                                int pos_j = j + off;
                                while (off >= 1) {
                                    off--;
                                    if (i + off == 7 || j + off == 7) {
                                        max_move[pos_i][pos_j] += 2;
                                    } else {
                                        max_move[pos_i][pos_j] += 1;
                                    }
                                }
                                break;
                            }
                        }
                    }
                    if (i > 0 && j < 7 && game_board[i - 1][j + 1] == my_color) {
                        off = 1;
                        while (i + off <= 7 && j - off >= 0) {
                            if (game_board[i + off][j - off] == enemy_color) {
                                off++;
                            } else if (game_board[i + off][j - off] == my_color) {
                                break;
                            } else if (game_board[i + off][j - off] == Board.empty_cell) {
                                if (max_move[i + off][j - off] == -1) {
                                    if (i + off == 7 && j - off == 0) {
                                        max_move[i + off][j - off] = 0.8;
                                    } else if (i + off == 7 || j - off == 0) {
                                        max_move[i + off][j - off] = 0.4;
                                    } else {
                                        max_move[i + off][j - off] = 0;
                                    }
                                }
                                int pos_i = i + off;
                                int pos_j = j - off;
                                while (off >= 1) {
                                    off--;
                                    if (i + off == 7 || j - off == 0) {
                                        max_move[pos_i][pos_j] += 2;
                                    } else {
                                        max_move[pos_i][pos_j] += 1;
                                    }
                                }
                                break;
                            }
                        }
                    }

                    if (j > 0 && i < 7 && game_board[i + 1][j - 1] == my_color) {
                        off = 1;
                        while (i - off >= 0 && j + off <= 7) {
                            if (game_board[i - off][j + off] == enemy_color) {
                                off++;
                            } else if (game_board[i - off][j + off] == my_color) {
                                break;
                            } else if (game_board[i - off][j + off] == Board.empty_cell) {
                                if (max_move[i - off][j + off] == -1) {
                                    if (i - off == 0 && j + off == 0) {
                                        max_move[i - off][j + off] = 0.8;
                                    } else if (i - off == 0 || j + off == 0) {
                                        max_move[i - off][j + off] = 0.4;
                                    } else {
                                        max_move[i - off][j + off] = 0;
                                    }
                                }
                                int pos_i = i - off;
                                int pos_j = j + off;
                                while (off >= 1) {
                                    off--;
                                    if (i - off == 0 || j + off == 7) {
                                        max_move[pos_i][pos_j] += 2;
                                    } else {
                                        max_move[pos_i][pos_j] += 1;
                                    }
                                }
                                break;
                            }
                        }
                    }

                    if (i < 7 && j < 7 && game_board[i + 1][j + 1] == my_color) {
                        off = 1;
                        while (i - off >= 0 && j - off <= 7) {
                            if (game_board[i - off][j - off] == enemy_color) {
                                off++;
                            } else if (game_board[i - off][j - off] == my_color) {
                                break;
                            } else if (game_board[i - off][j - off] == Board.empty_cell) {
                                if (max_move[i - off][j - off] == -1) {
                                    if (i - off == 0 && j + off == 7) {
                                        max_move[i - off][j - off] = 0.8;
                                    } else if (i - off == 0 || j - off == 7) {
                                        max_move[i - off][j - off] = 0.4;
                                    } else {
                                        max_move[i - off][j - off] = 0;
                                    }
                                }
                                int pos_i = i - off;
                                int pos_j = j - off;
                                while (off >= 1) {
                                    off--;
                                    if (i - off == 0 || j - off == 0) {
                                        max_move[pos_i][pos_j] += 2;
                                    } else {
                                        max_move[pos_i][pos_j] += 1;
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (max_move[i][j] > mx) {
                    mx = max_move[i][j];
                    offset_x = i;
                    offset_y = j;
                }
            }
        }

    }
    public boolean setNewPieceByComputer(boolean need_to_change_color, char enemy_color) {
        int [][] enemy_coordinates = new int[8][8];
        refreshEnemyCoordiantes(enemy_coordinates);
        findMaxMove('b');
        setNewPiece(offset_y, offset_x,'b');
        return false;
    }


    private Integer nextMovePlayer() {
        String move;
        if (player_1_move) {
            System.out.print("Player # 1 have to enter move in format <small_letter><number>\n");
            move = SCANNER.next();
            int offset_y = abs(move.charAt(1) - '8');
            int offset_x = abs(move.charAt(0) - 'a');
            if (checkOnBoundaries(move) == -1) {
                System.out.print("Your move is out of the board!");
                System.exit(-1);
            }
            setNewPiece(offset_x, offset_y,'w');
            player_1_move = false;
        } else {
            System.out.print("Player # 2 have to enter move in format <small_letter><number>\n");
            move = SCANNER.next();
            int offset_y = abs(move.charAt(1) - '8');
            int offset_x = abs(move.charAt(0) - 'a');
            if (checkOnBoundaries(move) == -1) {
                System.out.print("Your move is out of the board!");
                System.exit(-1);
            }
            setNewPiece(offset_x, offset_y, 'b');
            player_1_move = true;
        }
        return 0;
    }
    private Integer nextMoveComputer() {
        String move;
        if (isPlayerMove) {
            System.out.print("Enter your move in format <small_letter><number>\n");
            move = SCANNER.next();
            int offset_y = abs(move.charAt(1) - '8');
            int offset_x = abs(move.charAt(0) - 'a');
            if (checkOnBoundaries(move) == -1) {
                System.out.print("Your move is out of the board!");
                System.exit(-1);
            }
            setNewPiece(offset_x, offset_y,'w');
            isPlayerMove = false;
        } else {
            setNewPieceByComputer(false, 'b');
            isPlayerMove = true;
        }
        return 0;
    }

    private Integer checkOnBoundaries(String move) {
        if (move.charAt(0) >= 'a' && move.charAt(0) <= 'h' && move.charAt(1) >= '0' && move.charAt(1) <= '8') {
            return 0;
        }
        return -1;
    }
    public boolean onlyOneColorOnTheBoard() {
        boolean white_piece = false;
        boolean black_piece = false;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (game_board[i][j] == 'b') {
                    black_piece = true;
                }
                if (game_board[i][j] == 'w') {
                    white_piece = true;
                }
            }
        }
        if ((white_piece && !black_piece ) || (!white_piece && black_piece)) {
            return true;
        }
        return false;
    }
    private String endOfTheGame() {
        if (onlyOneColorOnTheBoard()) {
            return "Game is ended";
        }
        if (Board.isAnyEmptyCells(game_board).equals("Board doesn't have empty cell")) {
            return "Game is ended";

        }
        return "Game is still going";
    }
}
