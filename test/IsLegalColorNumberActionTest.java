import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Characterization tests for Main.isLegal() — matching by color, number,
 * and action type (SKIP / REVERSE / DRAW_TWO).
 *
 * These describe the CURRENT behavior, including any quirks.
 */
class IsLegalColorNumberActionTest {

    // ── Match by color ────────────────────────────────────────────────────────

    @Test void same_color_number_is_legal()       { assertTrue(Main.isLegal("R2", "R9", "")); }
    @Test void same_color_zero_is_legal()         { assertTrue(Main.isLegal("R0", "R7", "")); }
    @Test void same_color_action_on_number_legal() { assertTrue(Main.isLegal("RS", "R5", "")); }
    @Test void same_color_number_on_action_legal() { assertTrue(Main.isLegal("R3", "RS", "")); }

    // ── No match — should be illegal ─────────────────────────────────────────

    @Test void different_color_different_number_illegal() { assertFalse(Main.isLegal("B3", "R9", "")); }
    @Test void different_color_different_action_illegal() { assertFalse(Main.isLegal("GS", "RR", "")); }
    @Test void different_color_number_vs_action_illegal() { assertFalse(Main.isLegal("B5", "RS", "")); }

    // ── Match by number ───────────────────────────────────────────────────────

    @Test void same_number_different_color_legal()   { assertTrue(Main.isLegal("G9", "R9", "")); }
    @Test void same_zero_different_color_legal()     { assertTrue(Main.isLegal("B0", "R0", "")); }
    @Test void same_number_three_colors_legal()      { assertTrue(Main.isLegal("Y5", "G5", "")); }
    @Test void different_numbers_no_match_illegal()  { assertFalse(Main.isLegal("R3", "G7", "")); }

    // ── Match by action rank (non-NUMBER ranks match each other across colors) ─

    @Test void skip_on_skip_different_color_legal()       { assertTrue(Main.isLegal("GS", "RS", "")); }
    @Test void reverse_on_reverse_different_color_legal() { assertTrue(Main.isLegal("GR", "RR", "")); }
    @Test void draw_two_on_draw_two_different_color_legal(){ assertTrue(Main.isLegal("G+2", "R+2", "")); }

    // Action ranks do NOT match each other across types
    @Test void skip_on_reverse_illegal()       { assertFalse(Main.isLegal("GS", "RR", "")); }
    @Test void skip_on_draw_two_illegal()      { assertFalse(Main.isLegal("GS", "R+2", "")); }
    @Test void reverse_on_draw_two_illegal()   { assertFalse(Main.isLegal("GR", "R+2", "")); }
}

