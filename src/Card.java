/**
 * Utility methods for interpreting UNO card strings.
 *
 * Cards are encoded as strings:
 *   R5, Y0, GS, BR, B+2  — colored cards (color prefix + rank suffix)
 *   W, W4                 — wild cards (no color)
 *
 * Keeping card behavior here gives it a clear home separate from
 * game orchestration in Main.
 */
public class Card {

    private Card() {}  // utility class — no instances needed

    /** Returns the single-letter color ("R","Y","G","B") or "" for wild cards. */
    public static String color(String card) {
        if (card.startsWith("R")) return "R";
        if (card.startsWith("Y")) return "Y";
        if (card.startsWith("G")) return "G";
        if (card.startsWith("B")) return "B";
        return "";
    }

    /**
     * Returns the rank name for a card string.
     * Possible values: WILD, WILD_DRAW_FOUR, SKIP, REVERSE, DRAW_TWO, NUMBER
     */
    public static String rank(String card) {
        if (card.equals("W"))    return "WILD";
        if (card.equals("W4"))   return "WILD_DRAW_FOUR";
        if (card.endsWith("S"))  return "SKIP";
        if (card.endsWith("R"))  return "REVERSE";
        if (card.endsWith("+2")) return "DRAW_TWO";
        return "NUMBER";
    }

    /** Returns the face-value digit for NUMBER cards, or -1 for all others. */
    public static int number(String card) {
        if (rank(card).equals("NUMBER")) {
            return Integer.parseInt(card.substring(1));
        }
        return -1;
    }

    /** Returns the scoring point value of a card. */
    public static int points(String card) {
        String r = rank(card);
        if (r.equals("NUMBER")) return number(card);
        if (r.equals("SKIP") || r.equals("REVERSE") || r.equals("DRAW_TWO")) return 20;
        if (r.equals("WILD") || r.equals("WILD_DRAW_FOUR")) return 50;
        return 0;
    }

    /**
     * Returns true if {@code card} can legally be played on top of {@code upCard}
     * given the currently called color (empty string means no called color).
     */
    public static boolean isLegal(String card, String upCard, String calledColor) {
        if (card.startsWith("W")) return true;
        if (color(card).equals(color(upCard))) return true;
        if (!calledColor.equals("") && color(card).equals(calledColor)) return true;
        if (rank(card).equals(rank(upCard)) && !rank(card).equals("NUMBER")) return true;
        if (rank(card).equals("NUMBER") && rank(upCard).equals("NUMBER")
                && number(card) == number(upCard)) return true;
        return false;
    }
}

