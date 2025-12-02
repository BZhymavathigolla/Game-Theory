import java.util.*;

/**
 * Marvel Tic-Tac-Toe:
 * THANOS = X (MAX)
 * CAPTAIN = O (MIN)
 *
 * Thanos goes first and uses minimax (perfect play).
 * Captain tries to minimize Thanos's score.
 *
 * Minimax scores:
 *   +1 -> Thanos (X) will win from this state (good for Thanos)
 *    0 -> Draw with perfect play
 *   -1 -> Captain (O) will win from this state (good for Captain)
 *
 * Usage: run, follow prompts. Input move index when asked (robust parsing).
 */
public class Main {

    static final char THANOS = 'X';     // MAX 
    static final char CAPTAIN = 'O';    // MIN 
    static final char EMPTY = ' ';

    static char[][] board = {
            {EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY}
    };

    static Scanner sc = new Scanner(System.in);
    static Random rand = new Random();

    static class Move {
        int r, c;
        Move(int r, int c) { this.r = r; this.c = c; }
        public String toString() { return "(" + r + "," + c + ")"; }
    }

    public static void main(String[] args) {
        welcome();
        printBoard();

        // Thanos goes first
        while (true) {
            thanosMove();        
            printBoard();
            if (gameOver()) break;

            captainMove();      
            printBoard();
            if (gameOver()) break;
        }

        sc.close();
    }

    static void welcome() {
        System.out.println("⚡ Welcome to the Marvel Battle Arena! ⚡");
        System.out.println("THANOS  v/s  CAPTAIN AMERICA");
        System.out.println("Thanos goes first (he's powerful and thinks ahead!).\n");
    }

    // Thanos's turn: perfect minimax (MAX). Tie-broken randomly.
    static void thanosMove() {
        System.out.println("\n⚡ THANOS's turn...");

        List<Move> bestMoves = new ArrayList<>();
        int bestScore = Integer.MIN_VALUE;

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (board[r][c] == EMPTY) {
                    board[r][c] = THANOS;
                    int score = minimax(false);
                    board[r][c] = EMPTY;

                    if (score > bestScore) {
                        bestScore = score;
                        bestMoves.clear();
                        bestMoves.add(new Move(r, c));
                    } else if (score == bestScore) {
                        bestMoves.add(new Move(r, c));
                    }
                }
            }
        }

        Move pick = bestMoves.get(rand.nextInt(bestMoves.size()));
        board[pick.r][pick.c] = THANOS;
        System.out.println("⚡ THANOS strikes at: " + pick);
    }

    // Captain's turn
    static void captainMove() {
        System.out.println("\n🛡 CAPTAIN AMERICA! Your turn... Minimax is analyzing options...");

        List<Move> winningForCaptain = new ArrayList<>();
        List<Move> drawingMoves = new ArrayList<>();
        List<Move> allMoves = new ArrayList<>();

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (board[r][c] == EMPTY) {
                    board[r][c] = CAPTAIN;
                    int score = minimax(true);
                    board[r][c] = EMPTY;

                    Move m = new Move(r, c);
                    allMoves.add(m);
                    if (score == -1) winningForCaptain.add(m);
                    else if (score == 0) drawingMoves.add(m);
                }
            }
        }

        if (!winningForCaptain.isEmpty()) {
            System.out.println("Recommended moves (lead to CAPTAIN win if played perfectly):");
            printMoveList(winningForCaptain);
        } else if (!drawingMoves.isEmpty()) {
            System.out.println("No immediate winning move. Recommended draws (force at least a draw):");
            printMoveList(drawingMoves);
        } else {
            System.out.println("No guaranteed win/draw — choose any move (Thanos may force a win):");
            printMoveList(allMoves);
        }

        int choice = -1;
        List<Move> offered = !winningForCaptain.isEmpty() ? winningForCaptain
                         : !drawingMoves.isEmpty() ? drawingMoves
                         : allMoves;

        while (true) {
            System.out.print("Enter move index (0 - " + (offered.size()-1) + "): ");
            String line = sc.nextLine().trim();
            if (line.isEmpty()) { System.out.println("Please enter a number."); continue; }
            try {
                choice = Integer.parseInt(line);
                if (choice >= 0 && choice < offered.size()) break;
            } catch (NumberFormatException e) {}
            System.out.println("Invalid input. Try again.");
        }

        Move chosen = offered.get(choice);
        board[chosen.r][chosen.c] = CAPTAIN;
        System.out.println("🛡 Captain places at: " + chosen);
    }

    static void printMoveList(List<Move> moves) {
        for (int i = 0; i < moves.size(); i++) {
            System.out.println(i + ": " + moves.get(i));
        }
    }

    static int minimax(boolean isThanosTurn) {
        Character res = checkWinner();
        if (res != null) {
            if (res == THANOS) return +1;
            if (res == CAPTAIN) return -1;
            return 0;
        }

        if (isThanosTurn) {
            int best = Integer.MIN_VALUE;

            //Write your logic here

            return best;
        } else {
            int best = Integer.MAX_VALUE;

            //Write your logic here

            return best;
        }
    }

    static Character checkWinner() {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != EMPTY &&
                board[i][0] == board[i][1] &&
                board[i][1] == board[i][2]) return board[i][0];

            if (board[0][i] != EMPTY &&
                board[0][i] == board[1][i] &&
                board[1][i] == board[2][i]) return board[0][i];
        }

        if (board[0][0] != EMPTY &&
            board[0][0] == board[1][1] &&
            board[1][1] == board[2][2]) return board[0][0];

        if (board[0][2] != EMPTY &&
            board[0][2] == board[1][1] &&
            board[1][1] == board[2][0]) return board[0][2];

        for (int r = 0; r < 3; r++)
            for (int c = 0; c < 3; c++)
                if (board[r][c] == EMPTY) return null;

        return 'D';
    }

    static boolean gameOver() {
        Character res = checkWinner();
        if (res == null) return false;

        if (res == THANOS) {
            System.out.println("\n⚡ THANOS WINS! Perfectly balanced victory! ⚡");
        } else if (res == CAPTAIN) {
            System.out.println("\n🛡 CAPTAIN AMERICA WINS! Freedom prevails! 🛡");
        } else {
            System.out.println("\n🤝 It's a DRAW. Peace is restored.");
        }
        return true;
    }

    static void printBoard() {
        System.out.println("\nCurrent Battle Grid:");
        System.out.println("-------------");
        for (int r = 0; r < 3; r++) {
            System.out.print("| ");
            for (int c = 0; c < 3; c++) {
                System.out.print(board[r][c] + " | ");
            }
            System.out.println("\n-------------");
        }
    }
}
