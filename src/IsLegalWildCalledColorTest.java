import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Characterization tests for Main.isLegal() — wild cards and the
 * "called color" override that applies after a wild is played.
 *
 * These describe the CURRENT behavior of this implementation.
 */
class IsLegalWildCalledColorTest {

    // ── Wild (W) is always legal ──────────────────────────────────────────────

    @Test void wild_on_number_card_is_legal()  { assertTrue(Main.isLegal("W", "R5", "")); }
    @Test void wild_on_skip_is_legal()         { assertTrue(Main.isLegal("W", "GS", "")); }
    @Test void wild_on_wild_is_legal()         { assertTrue(Main.isLegal("W", "W", "")); }
    @Test void wild_with_called_color_is_legal(){ assertTrue(Main.isLegal("W", "W", "B")); }

    // ── Wild Draw Four (W4) is always legal ──────────────────────────────────

    @Test void w4_on_number_card_is_legal()    { assertTrue(Main.isLegal("W4", "G9", "")); }
    @Test void w4_on_action_card_is_legal()    { assertTrue(Main.isLegal("W4", "RS", "")); }
    @Test void w4_with_called_color_is_legal() { assertTrue(Main.isLegal("W4", "W", "B")); }
    @Test void w4_on_w4_is_legal()             { assertTrue(Main.isLegal("W4", "W4", "")); }

    // ── Called color overrides the up-card color ──────────────────────────────

    @Test void card_matching_called_color_is_legal()  { assertTrue(Main.isLegal("B3", "W", "B")); }
    @Test void card_matching_called_color_action()    { assertTrue(Main.isLegal("BS", "W", "B")); }
    @Test void non_matching_called_color_is_illegal() { assertFalse(Main.isLegal("R3", "W", "B")); }
    @Test void green_card_when_blue_called_is_illegal(){ assertFalse(Main.isLegal("G3", "W", "B")); }

    // ── Edge case: up card is W, color() returns "" ───────────────────────────
    // color("G3") = "G", color("W") = "" — they do NOT match,
    // so without a called color, a colored card cannot be played on a bare W.
    @Test void colored_card_on_bare_wild_no_call_is_illegal() {
        assertFalse(Main.isLegal("G3", "W", ""));
    }

    // ── Called color has no effect when it is empty string ───────────────────

    @Test void empty_called_color_does_not_trigger_color_branch() {
        // R3 vs G5: different color, different number — must be illegal
        assertFalse(Main.isLegal("R3", "G5", ""));
    }
}

