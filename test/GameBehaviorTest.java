import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Characterization tests for game-level behaviors:
 * turn advancement (next), bot card/color choice, and scoring.
 *
 * Global state in Main is set directly before each test.
 * These tests describe what the CURRENT implementation does.
 */
class GameBehaviorTest {

    @BeforeEach
    void resetState() {
        Main.playerNames = new ArrayList<>();
        Main.playerNames.add("P1");
        Main.playerNames.add("P2");
        Main.playerNames.add("P3");
        Main.currentPlayer = 0;
        Main.direction = 1;
        Main.upCard = "R5";
        Main.calledColor = "";
    }

    // ── next() — turn advancement ─────────────────────────────────────────────

    @Test void next_advances_forward_by_one() {
        Main.currentPlayer = 0;
        Main.next();
        assertEquals(1, Main.currentPlayer);
    }

    @Test void next_forward_wraps_past_last_player() {
        Main.currentPlayer = 2; // last of 3
        Main.next();
        assertEquals(0, Main.currentPlayer);
    }

    @Test void next_backward_wraps_before_first_player() {
        Main.direction = -1;
        Main.currentPlayer = 0;
        Main.next();
        assertEquals(2, Main.currentPlayer);
    }

    @Test void next_backward_decrements_normally() {
        Main.direction = -1;
        Main.currentPlayer = 2;
        Main.next();
        assertEquals(1, Main.currentPlayer);
    }

    // ── chooseBotCard() priority ──────────────────────────────────────────────

    @Test void bot_prefers_draw_two_over_number_card() {
        Main.upCard = "R5"; Main.calledColor = "";
        ArrayList<String> hand = new ArrayList<>();
        hand.add("R3");   // legal number — index 0
        hand.add("R+2");  // legal draw two — index 1 (preferred)
        assertEquals(1, Main.chooseBotCard(hand));
    }

    @Test void bot_prefers_skip_over_plain_number() {
        Main.upCard = "R5"; Main.calledColor = "";
        ArrayList<String> hand = new ArrayList<>();
        hand.add("R3");  // legal number — index 0
        hand.add("RS");  // legal skip  — index 1 (preferred)
        assertEquals(1, Main.chooseBotCard(hand));
    }

    @Test void bot_plays_wild_only_as_last_resort() {
        Main.upCard = "R5"; Main.calledColor = "";
        ArrayList<String> hand = new ArrayList<>();
        hand.add("B3"); // illegal
        hand.add("W");  // wild — index 1 (last resort)
        assertEquals(1, Main.chooseBotCard(hand));
    }

    @Test void bot_draws_when_nothing_is_legal() {
        Main.upCard = "R5"; Main.calledColor = "";
        ArrayList<String> hand = new ArrayList<>();
        hand.add("B3"); // not legal — only card
        assertEquals(-1, Main.chooseBotCard(hand));
    }

    @Test void bot_respects_called_color() {
        Main.upCard = "W"; Main.calledColor = "B";
        ArrayList<String> hand = new ArrayList<>();
        hand.add("G3"); // not legal (G != called B)
        hand.add("B7"); // legal via called color — index 1
        assertEquals(1, Main.chooseBotCard(hand));
    }

    @Test void bot_prefers_number_over_wild_when_number_is_legal() {
        Main.upCard = "R9"; Main.calledColor = "";
        ArrayList<String> hand = new ArrayList<>();
        hand.add("W");  // wild — index 0
        hand.add("R4"); // legal number — index 1 (preferred over wild)
        // Bot picks R4 (number priority pass) before falling back to W
        assertEquals(1, Main.chooseBotCard(hand));
    }

    // ── chooseBotColor() ─────────────────────────────────────────────────────

    @Test void bot_color_picks_majority_color() {
        ArrayList<String> hand = new ArrayList<>();
        hand.add("B1"); hand.add("B2"); hand.add("R3");
        assertEquals("B", Main.chooseBotColor(hand));
    }

    @Test void bot_color_picks_red_when_tied_at_top() {
        // r=1, y=0, g=0, b=1 → r >= b in the first condition
        ArrayList<String> hand = new ArrayList<>();
        hand.add("R1"); hand.add("B2");
        assertEquals("R", Main.chooseBotColor(hand));
    }

    @Test void bot_color_returns_red_when_hand_is_all_wilds() {
        // all counts = 0; r>=y && r>=g && r>=b (0>=0>=0>=0) → "R"
        ArrayList<String> hand = new ArrayList<>();
        hand.add("W"); hand.add("W4");
        assertEquals("R", Main.chooseBotColor(hand));
    }

    // ── Scoring (points on individual cards) ─────────────────────────────────

    @Test void hand_score_sums_correctly() {
        // R5=5, W=50, GS=20 → total 75
        assertEquals(75, Main.points("R5") + Main.points("W") + Main.points("GS"));
    }

    @Test void hand_with_only_numbers_scores_face_values() {
        assertEquals(15, Main.points("R5") + Main.points("Y3") + Main.points("G7"));
    }

    @Test void hand_with_only_wilds_scores_100() {
        assertEquals(100, Main.points("W") + Main.points("W4"));
    }

    // ── Edge case: draw() returns "W" when deck and discard are both empty ────

    @Test void draw_returns_wild_when_both_deck_and_discard_empty() {
        Main.deck = new ArrayList<>();
        Main.discard = new ArrayList<>();
        String drawn = Main.draw();
        assertEquals("W", drawn);
    }
}

