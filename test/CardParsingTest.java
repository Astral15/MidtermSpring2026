import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Characterization tests for the card-parsing utility methods in Main:
 * color(), rank(), number(), and points().
 *
 * These tests describe the CURRENT behavior of the implementation.
 * If a test fails after refactoring, behavior has changed unintentionally.
 */
class CardParsingTest {

    // ── color() ──────────────────────────────────────────────────────────────

    @Test void color_of_red_number_card()    { assertEquals("R", Main.color("R5")); }
    @Test void color_of_yellow_zero()        { assertEquals("Y", Main.color("Y0")); }
    @Test void color_of_green_skip()         { assertEquals("G", Main.color("GS")); }
    @Test void color_of_blue_draw_two()      { assertEquals("B", Main.color("B+2")); }
    @Test void color_of_wild_is_empty()      { assertEquals("", Main.color("W")); }
    @Test void color_of_wild_draw_four_is_empty() { assertEquals("", Main.color("W4")); }

    // ── rank() ───────────────────────────────────────────────────────────────

    @Test void rank_of_wild()              { assertEquals("WILD",          Main.rank("W")); }
    @Test void rank_of_wild_draw_four()    { assertEquals("WILD_DRAW_FOUR", Main.rank("W4")); }
    @Test void rank_of_skip()              { assertEquals("SKIP",           Main.rank("RS")); }
    @Test void rank_of_reverse()           { assertEquals("REVERSE",        Main.rank("RR")); }
    @Test void rank_of_draw_two()          { assertEquals("DRAW_TWO",       Main.rank("R+2")); }
    @Test void rank_of_number_card()       { assertEquals("NUMBER",         Main.rank("R5")); }
    @Test void rank_of_zero_is_number()    { assertEquals("NUMBER",         Main.rank("G0")); }
    @Test void rank_of_nine_is_number()    { assertEquals("NUMBER",         Main.rank("B9")); }

    // ── number() ─────────────────────────────────────────────────────────────

    @Test void number_of_five()            { assertEquals(5,  Main.number("R5")); }
    @Test void number_of_zero()            { assertEquals(0,  Main.number("Y0")); }
    @Test void number_of_nine()            { assertEquals(9,  Main.number("B9")); }
    @Test void number_of_non_number_card() { assertEquals(-1, Main.number("RS")); }
    @Test void number_of_wild()            { assertEquals(-1, Main.number("W")); }

    // ── points() ─────────────────────────────────────────────────────────────

    @Test void points_of_number_card()     { assertEquals(7,  Main.points("R7")); }
    @Test void points_of_zero()            { assertEquals(0,  Main.points("G0")); }
    @Test void points_of_nine()            { assertEquals(9,  Main.points("Y9")); }
    @Test void points_of_skip()            { assertEquals(20, Main.points("RS")); }
    @Test void points_of_reverse()         { assertEquals(20, Main.points("YR")); }
    @Test void points_of_draw_two()        { assertEquals(20, Main.points("B+2")); }
    @Test void points_of_wild()            { assertEquals(50, Main.points("W")); }
    @Test void points_of_wild_draw_four()  { assertEquals(50, Main.points("W4")); }
}

