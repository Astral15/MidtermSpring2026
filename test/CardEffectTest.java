import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Characterization tests for card-effect application methods.
 *
 * Covers the required behaviors: skip, reverse, draw two, wild draw four —
 * verifying what actually happens to turn order and hand sizes.
 */
class CardEffectTest {

    @BeforeEach
    void resetState() {
        Main.playerNames = new ArrayList<>();
        Main.playerNames.add("P1");
        Main.playerNames.add("P2");
        Main.playerNames.add("P3");
        Main.hands = new ArrayList<>();
        for (int i = 0; i < 3; i++) Main.hands.add(new ArrayList<>());
        Main.deck = new ArrayList<>();
        Main.deck.add("R1"); Main.deck.add("R2"); Main.deck.add("R3");
        Main.deck.add("R4"); Main.deck.add("R5"); Main.deck.add("R6");
        Main.discard = new ArrayList<>();
        Main.direction = 1;
        Main.currentPlayer = 0;
        Main.upCard = "R5";
        Main.calledColor = "";
        Main.quiet = true;
        Main.view = new ConsoleView(true);
    }

    // ── applySkip() ───────────────────────────────────────────────────────────

    @Test void skip_advances_past_next_player_to_one_after() {
        // Player 0 plays; player 1 is skipped; player 2 goes next
        Main.currentPlayer = 0;
        Main.applySkip();
        assertEquals(2, Main.currentPlayer);
    }

    @Test void skip_wraps_correctly_at_end_of_player_list() {
        // Player 2 plays; player 0 is skipped; player 1 goes next
        Main.currentPlayer = 2;
        Main.applySkip();
        assertEquals(1, Main.currentPlayer);
    }

    // ── applyReverse() ────────────────────────────────────────────────────────

    @Test void reverse_flips_direction_from_forward_to_backward() {
        Main.direction = 1;
        Main.applyReverse();
        assertEquals(-1, Main.direction);
    }

    @Test void reverse_flips_direction_from_backward_to_forward() {
        Main.direction = -1;
        Main.applyReverse();
        assertEquals(1, Main.direction);
    }

    @Test void reverse_3_players_goes_one_step_in_new_direction() {
        // Player 0, direction becomes -1, next() wraps to player 2
        Main.currentPlayer = 0;
        Main.direction = 1;
        Main.applyReverse();
        assertEquals(2, Main.currentPlayer);
    }

    @Test void reverse_2_players_acts_as_skip_same_player_goes_again() {
        // With 2 players, reverse = skip: player 0 gets another turn
        Main.playerNames.remove(2);
        Main.hands.remove(2);
        Main.currentPlayer = 0;
        Main.direction = 1;
        Main.applyReverse();
        assertEquals(0, Main.currentPlayer);
    }

    // ── applyDrawTwo() ────────────────────────────────────────────────────────

    @Test void draw_two_adds_exactly_two_cards_to_next_player() {
        Main.currentPlayer = 0;
        int before = Main.hands.get(1).size();
        Main.applyDrawTwo();
        assertEquals(before + 2, Main.hands.get(1).size());
    }

    @Test void draw_two_skips_turn_of_player_who_drew() {
        // Player 1 draws two and is skipped; player 2 goes next
        Main.currentPlayer = 0;
        Main.applyDrawTwo();
        assertEquals(2, Main.currentPlayer);
    }

    // ── applyWildDrawFour() ───────────────────────────────────────────────────

    @Test void wild_draw_four_adds_exactly_four_cards_to_next_player() {
        Main.currentPlayer = 0;
        int before = Main.hands.get(1).size();
        Main.applyWildDrawFour();
        assertEquals(before + 4, Main.hands.get(1).size());
    }

    @Test void wild_draw_four_skips_turn_of_player_who_drew() {
        // Player 1 draws four and is skipped; player 2 goes next
        Main.currentPlayer = 0;
        Main.applyWildDrawFour();
        assertEquals(2, Main.currentPlayer);
    }

    // ── applyCardEffect() dispatcher ─────────────────────────────────────────

    @Test void dispatcher_routes_skip_card() {
        Main.currentPlayer = 0;
        Main.applyCardEffect("RS");
        assertEquals(2, Main.currentPlayer);
    }

    @Test void dispatcher_routes_reverse_card() {
        Main.direction = 1;
        Main.applyCardEffect("RR");
        assertEquals(-1, Main.direction);
    }

    @Test void dispatcher_routes_draw_two_card() {
        Main.currentPlayer = 0;
        Main.applyCardEffect("R+2");
        assertEquals(2, Main.hands.get(1).size());
    }

    @Test void dispatcher_routes_wild_draw_four() {
        Main.currentPlayer = 0;
        Main.applyCardEffect("W4");
        assertEquals(4, Main.hands.get(1).size());
    }

    @Test void dispatcher_number_card_just_advances_one_turn() {
        Main.currentPlayer = 0;
        Main.applyCardEffect("R5");
        assertEquals(1, Main.currentPlayer);
    }
}

