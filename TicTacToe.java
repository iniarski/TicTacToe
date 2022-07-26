import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class TicTacToe {
    protected int board[][] = new int[3][3];
    protected char playerSymbol;
    protected char opponentSymbol;
    protected int move = 0;

    protected static final String RESET = "\033[0m";  // Text Reset
    protected static final String RED = "\033[0;31m";     // RED
    protected static final String BLUE = "\033[0;34m";    // BLUE

    public void printBoard(){

        for (int i = 0; i < 3; i++) {
            System.out.print("\n\t");
            for (int j = 0; j < 3; j++) {
                switch (board[i][j]){
                    case 0:
                        System.out.print("[ ]");
                        break;
                    case 1:
                        System.out.print("[" + BLUE + playerSymbol + RESET + "]");
                        break;
                    case 2:
                        System.out.print("[" + RED + opponentSymbol + RESET + "]");
                        break;
                }
            }
        }
        System.out.println();
    }

    private boolean getGameType(){
        System.out.println("Press 1 for Player vs Player game");
        System.out.println("Press 2 for Player vs AI game");
        Scanner scanner = new Scanner(System.in);
        int input = 0;
        while (!(input == 1 || input == 2)){
            System.out.print("Insert value of 1 or 2 : ");

            try {
                input = scanner.nextInt();
            }
            catch (InputMismatchException ime){
                System.out.println("Enter a proper number");
                scanner.next();
            }


        }
        if (input == 1){
            System.out.println("A PvP game");
            return true;
        }
        else {
            System.out.println("A PvC game");
            return false;
        }

    }

    protected void setPlayerSymbol(){
        char input;
        Scanner scanner = new Scanner(System.in);
        System.out.println("To play as X enter x");
        System.out.println("To play as O enter o");
        System.out.println("To play as random enter any other value");
        System.out.print("Enter symbol : ");
        input = scanner.next().charAt(0);

        switch (input){
            case 'x':
                playerSymbol = 'X';
                break;
            case 'o':
                playerSymbol = 'O';
                break;
            default:
                int rand = new Random().nextInt() % 2;
                if (rand == 1)
                    playerSymbol = 'X';
                else
                    playerSymbol = 'O';
                break;
        }

        if (playerSymbol == 'X')
            opponentSymbol = 'O';
        else
            opponentSymbol = 'X';
    }

    protected int getFirstMove(){
        return new Random().nextInt(2) + 1;
    }

    protected boolean checkWin(int playerId){
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == playerId && board[i][1] == playerId
            && board[i][2] == playerId)
                return true;
            if (board[0][i] == playerId && board[1][i] == playerId
                    && board[2][i] == playerId)
                return true;
        }
        if (board[0][0] == playerId && board[1][1] == playerId
                && board[2][2] == playerId)
            return true;
        if (board[0][2] == playerId && board[1][1] == playerId
                && board[2][0] == playerId)
            return true;

        return false;
    }

    protected void playerMove (int onMove){

        Scanner scanner = new Scanner(System.in);

        System.out.println( "\n\t[1][2][3]\n\t[4][5][6]\n\t[7][8][9]");

        boolean moveNotConfirmed = true;
            while (moveNotConfirmed){
                System.out.print("\n Insert number of field : ");
                try {
                    int square = scanner.nextInt();
                    if (square < 1 || square > 9){
                        System.out.println("Field out of scope, please enter a number from 1 to 9");
                    }
                    else {
                        if (board[(square - 1) / 3][(square - 1) % 3] != 0) {
                            System.out.println("Field occupied, please select an empty field");
                        } else {
                            board[(square - 1) / 3][(square - 1) % 3] = onMove;
                            moveNotConfirmed = false;
                        }
                    }

                }
                catch (InputMismatchException ime){
                    System.out.println("Enter a proper number");
                    scanner.next();
                }
            }
        }
        
    private boolean oneMoreRound (){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Do you want to play again? [Y/n] ");
        while (true){
            char answer = scanner.next().charAt(0);
            if (answer == 'y' || answer == 'Y')
                return true;
            else if (answer =='n' || answer == 'N')
                return false;
        }
    }
        
    public void game() {

        boolean gameType = getGameType();
        // true = PvP game
        // false = PvC game
        boolean again;
        do {
            if (gameType) {
                PvP playersGame = new PvP();
            } else {
                PvC pvc = new PvC();
            }
            again = oneMoreRound();
        }while (again);

    }
}

class PvP extends TicTacToe{
    public PvP(){
        setPlayerSymbol();
        play();
    }
    void play(){
        int onMove = getFirstMove();
        boolean noWinner = true;

        System.out.println("Player 1 plays as " + playerSymbol);
        System.out.println("Player 2 plays as " + opponentSymbol + (char)'\n' );

        while (move < 9 && noWinner){
            System.out.println("Move " + (move + 1));
            System.out.println("Player " + onMove + " turn");
            playerMove(onMove);
            printBoard();
            noWinner = !checkWin(onMove);
            move++;
            onMove = 3 - onMove;
        }
        onMove = 3 - onMove;

        if (noWinner)
            System.out.println("The game ended in a draw");
        else
            System.out.println("Player " + onMove + " wins!");

        printBoard();
    }
}

class PvC extends TicTacToe{
    PvC (){
        setPlayerSymbol();
        play();
    }

    private void computerMove() {
        switch (move) {
            case 0:
                board[0][0] = 2;
                break;
            case 1:
                if (board[1][1] == 0)
                    board[1][1] = 2;
                else
                    board[0][0] = 2;
                break;
            default:
                // Tic Tac Toe algorithm
                // 1. If can win - win
                boolean hasNotMoved = true;
                for (int i = 0; i < 9; i++) {
                    if (isWinningMove(i, 2)) {
                        hasNotMoved = false;
                        board[i/3][i%3] = 2;
                        break;
                    }
                }
                // 2. If opponent has a winning move - block
                if (hasNotMoved) {
                    for (int i = 0; i < 9; i++) {
                        if (isWinningMove(i, 1)) {
                            hasNotMoved = false;
                            board[i/3][i%3] = 2;
                            break;
                            }
                        }
                    }

                // 3. If can fork - fork
                // Forking means playing a move creating two possibilities of winning on the next move
                if (hasNotMoved) {
                    for (int i = 0; i < 9; i++) {
                        if (isForking(i, 2)) {
                            hasNotMoved = false;
                            board[i/3][i%3] = 2;
                            break;
                        }
                    }
                }

                // 4. If enemy can fork - block

                if (hasNotMoved) {
                    for (int i = 0; i < 9; i++) {
                        if (isForking(i, 1)) {
                            hasNotMoved = false;
                            board[i/3][i%3] = 2;
                            break;
                        }
                    }
                }

                // 5. Play center

                if (hasNotMoved && board[1][1] == 0){
                    board[1][1] = 2;
                    hasNotMoved = false;
                }

                // 6. Play opposing corner

                if(hasNotMoved){
                    int corners[] = {0, 2, 6, 8};
                    int oppCorners[] = {8, 6, 2, 0};

                    for (int i = 0; i < 4; i++) {
                        int j = corners[i];
                        int k = oppCorners[i];
                        if(board[j/3][j%3] == 1 && board[k/3][k%3] == 0){
                            board[k/3][k%3] = 2;
                            hasNotMoved = false;
                            break;
                        }
                    }
                }

                // 7. Play a corner

                if (hasNotMoved){
                    int corners[] = {0, 2, 6, 8};

                    for(int i : corners){
                        if (board[i/3][i%3] == 0){
                            board[i/3][i%3] = 2;
                            hasNotMoved = false;
                            break;
                        }
                    }
                }

                // 8. Play a side

                if (hasNotMoved){
                    int sides[] = {1, 3, 5, 7};
                    for (int i : sides){
                        board[i/3][i%3] = 2;
                        break;
                    }
                }

                }
        }

    public void play(){
        int onMove = getFirstMove();
        boolean noWinner = true;

        System.out.println("Player plays as " + playerSymbol);
        System.out.println("Computer plays as " + opponentSymbol + (char)'\n' );
        printBoard();

        while (move < 9 && noWinner){
            System.out.println("Move " + (move + 1));
            if (onMove == 1){
                System.out.println("Player move");
                playerMove(1);
            }
            else {
                System.out.println("Computer move");
                computerMove();
                printBoard();
            }
            noWinner = !checkWin(onMove);
            move++;
            onMove = 3 - onMove;
        }
        onMove = 3 - onMove;

        if (noWinner)
            System.out.println("The game ended in a draw");
        else{
            if (onMove == 1)
                System.out.println("Player wins!");
            else
                System.out.println("Computer wins!");
        }

        printBoard();

    }

    private boolean isWinningMove(int field, int playerId){
        if (board[field/3][field%3] != 0)
            return false;
        else {
            board[field/3][field%3] = playerId;
            boolean isWinning = checkWin(playerId);
            board[field/3][field%3] = 0;
            return isWinning;
        }
    }

    private boolean isForking(int field, int playerId){
        if(board[field/3][field%3] != 0)
            return false;
        else {
            byte winningMovesCount = 0;
            board[field/3][field%3] = playerId;

            for (int i = 0; i < 9; i++) {
                if (board[i/3][i%3] == 0){
                    board[i/3][i%3] = playerId;
                    if (checkWin(playerId))
                        winningMovesCount++;
                    board[i/3][i%3] = 0;
                }
            }
            board[field/3][field%3] = 0;
            if (winningMovesCount >= 2)
                return true;
            else
                return false;
        }
    }


}