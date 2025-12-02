import java.util.*;

/**
 * Marvel Tic-Tac-Toe:
 * THOR = X (MAX)
 * CAPTAIN = O (MIN)
 *
 * Thor  goes first and uses minimax (perfect play).
 * Captain tries to minimize Thor's score.
 *
 * Minimax scores:
 *   +1 -> Thor (X) will win from this state (good for Thor)
 *    0 -> Draw with perfect play
 *   -1 -> Captain (O) will win from this state (good for Captain)
 *
 * Usage: run, follow prompts. Input move index when asked (robust parsing).
 */
public class Main {

    static final char THOR = 'X';     // MAX 
    static final char CAPTAIN = 'O';  // MIN 
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

        // Thor goes first
        while (true) {
            thorMove();         // Thor (MAX) plays using minimax
            printBoard();
            if (gameOver()) break;

            captainMove();      // Captain (MIN) by recommendations
            printBoard();
            if (gameOver()) break;
        }

        sc.close();
    }

    static void welcome() {
        System.out.println("⚡ Welcome to the Marvel Battle Arena! ⚡");
        System.out.println("THOR  v/s  CAPTAIN AMERICA");
        System.out.println("Thor goes first (he's powerful and thinks ahead!).\n");
    }

    // Thor's turn: perfect minimax (MAX). Tie-broken randomly.
    static void thorMove() {
        System.out.println("\n⚡ THOR's turn...");

        List<Move> bestMoves = new ArrayList<>();
        int bestScore = Integer.MIN_VALUE;

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (board[r][c] == EMPTY) {
                    board[r][c] = THOR;
                    int score = minimax(false); // after Thor moves, it's MIN's turn
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

        // pick one best move (random tie-break)
        Move pick = bestMoves.get(rand.nextInt(bestMoves.size()));
        board[pick.r][pick.c] = THOR;
        System.out.println("⚡ THOR strikes at: " + pick);
    }

    // Captain's turn: show recommended moves (those that minimize Thor's score).
    static void captainMove() {
        System.out.println("\n🛡 CAPTAIN AMERICA! Your turn... Minimax is analyzing options...");

        List<Move> winningForCaptain = new ArrayList<>(); // moves with score == -1
        List<Move> drawingMoves = new ArrayList<>();      // moves with score == 0
        List<Move> allMoves = new ArrayList<>();

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (board[r][c] == EMPTY) {
                    board[r][c] = CAPTAIN;
                    int score = minimax(true); // after Captain moves, it's MAX's turn
                    board[r][c] = EMPTY;

                    Move m = new Move(r, c);
                    allMoves.add(m);
                    if (score == -1) winningForCaptain.add(m);
                    else if (score == 0) drawingMoves.add(m);
                }
            }
        }

        // Print recommendations (best-first: winning moves for Captain, then draws, then others)
        if (!winningForCaptain.isEmpty()) {
            System.out.println("Recommended moves (lead to CAPTAIN win if played perfectly):");
            printMoveList(winningForCaptain);
        } else if (!drawingMoves.isEmpty()) {
            System.out.println("No immediate winning move. Recommended draws (force at least a draw):");
            printMoveList(drawingMoves);
        } else {
            System.out.println("No guaranteed win/draw — choose any move (Thor may force a win):");
            printMoveList(allMoves);
        }

        // Accept user choice robustly
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
            } catch (NumberFormatException e) {
                // fallthrough
            }
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

    // Minimax function: returns +1 if THOR wins from this state, -1 if CAPTAIN wins, 0 draw.
    // isThorTurn tells whose turn it is in the simulated state.
    static int minimax(boolean isThorTurn) {
        Character res = checkWinner();
        if (res != null) {
            if (res == THOR) return +1;
            if (res == CAPTAIN) return -1;
            return 0; // draw
        }

        if (isThorTurn) { // MAX player
            int best = Integer.MIN_VALUE;




          
            //Write your logic here





          
            return best;
        } else { // CAPTAIN (MIN)
            int best = Integer.MAX_VALUE;




          
            //Write your logic here





          
            return best;
        }
    }

    // Check winner or draw:
    // returns 'X' or 'O' if a player wins, 'D' if draw, or null if game continues
    static Character checkWinner() {
        // rows & cols
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != EMPTY &&
                board[i][0] == board[i][1] &&
                board[i][1] == board[i][2]) return board[i][0];

            if (board[0][i] != EMPTY &&
                board[0][i] == board[1][i] &&
                board[1][i] == board[2][i]) return board[0][i];
        }

        // diagonals
        if (board[0][0] != EMPTY &&
            board[0][0] == board[1][1] &&
            board[1][1] == board[2][2]) return board[0][0];

        if (board[0][2] != EMPTY &&
            board[0][2] == board[1][1] &&
            board[1][1] == board[2][0]) return board[0][2];

        // any empty cell?
        for (int r = 0; r < 3; r++)
            for (int c = 0; c < 3; c++)
                if (board[r][c] == EMPTY) return null;

        return 'D'; // draw
    }

    static boolean gameOver() {
        Character res = checkWinner();
        if (res == null) return false;

        if (res == THOR) {
            System.out.println("\n⚡ THOR WINS! Heaped lightning victory! ⚡");
        } else if (res == CAPTAIN) {
            System.out.println("\n🛡 CAPTAIN AMERICA WINS! Freedom prevails! 🛡");
        } else {
            System.out.println("\n🤝 It's a DRAW. You stopped World War.");
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
