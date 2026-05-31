import java.util.ArrayList;

/**
 * Handles all console output for the UNO game.
 *
 * Separating print statements from game logic means:
 * - Game rules can be tested without any console output.
 * - The CLI presentation can be changed without touching rule code.
 *
 * Pass quiet=true to suppress non-essential output (used in bot-only runs).
 */
public class ConsoleView {

    private final boolean quiet;

    public ConsoleView(boolean quiet) {
        this.quiet = quiet;
    }

    public void showGameHeader(int gameNumber) {
        if (!quiet) System.out.println("\n=== Game " + gameNumber + " ===");
    }

    public void showUpCard(String upCard, String calledColor) {
        if (!quiet) {
            String suffix = calledColor.equals("") ? "" : " called " + calledColor;
            System.out.println("\nUp card: " + upCard + suffix);
        }
    }

    public void showHand(String playerName, ArrayList<String> hand) {
        if (!quiet) System.out.println(playerName + " hand: " + join(hand));
    }

    public void showDrawn(String playerName, String card) {
        if (!quiet) System.out.println(playerName + " draws " + card);
    }

    public void showPlayed(String playerName, String card) {
        if (!quiet) System.out.println(playerName + " plays " + card);
    }

    public void showColorCalled(String playerName, String color) {
        if (!quiet) System.out.println(playerName + " calls " + color);
    }

    public void showUno(String playerName) {
        if (!quiet) System.out.println(playerName + " says UNO!");
    }

    public void showWin(String playerName, int points) {
        if (!quiet) System.out.println(playerName + " wins and scores " + points);
    }

    public void showPenaltyInvalidIndex(String playerName) {
        if (!quiet) System.out.println(playerName + " selected an invalid index and draws a penalty card.");
    }

    public void showPenaltyIllegalCard(String playerName, String card) {
        if (!quiet) System.out.println(playerName + " tried illegal card " + card + " and draws a penalty card.");
    }

    public void showDrawsTwo(String playerName) {
        if (!quiet) System.out.println(playerName + " draws two.");
    }

    public void showDrawsFour(String playerName) {
        if (!quiet) System.out.println(playerName + " draws four.");
    }

    public void showSafetyLimit() {
        if (!quiet) System.out.println("Game stopped at safety limit.");
    }

    public void showFinalScores(ArrayList<String> names, int[] scores) {
        System.out.println("\nFinal scores:");
        for (int i = 0; i < names.size(); i++) {
            System.out.println(names.get(i) + ": " + scores[i]);
        }
    }

    public void showPlayerCountError() {
        System.out.println("UNO needs 2 to 4 players.");
    }

    // ── helpers ───────────────────────────────────────────────────────────────

    private static String join(ArrayList<String> cards) {
        String out = "";
        for (int i = 0; i < cards.size(); i++) {
            out += i + ":" + cards.get(i);
            if (i < cards.size() - 1) out += " ";
        }
        return out;
    }
}

