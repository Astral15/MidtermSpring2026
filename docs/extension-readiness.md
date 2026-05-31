# Extension Readiness Note
## Which extension would your design support best?
**Adding a new card effect** (e.g. a "Draw Three" card, a "Swap Hands" card, or a "Reverse and Skip" variant).
## Where would that change be implemented?
The refactoring created a clear path:
1. **`Card.java`** — add the new rank string to `rank()` and its point value to `points()`.
2. **`Main.applyCardEffect()`** — add one `else if` branch that calls a new `applyXxx()` method.
3. **`Main.applyXxx()`** — implement the effect in a single, focused method (same pattern as `applyDrawTwo()`, `applySkip()`, etc.).
4. **`ConsoleView.java`** — add one `showXxx()` method for the new effect message.
5. **Tests** — add a characterization test for the new card in `CardParsingTest` and a behavior test in `GameBehaviorTest`.
No other code needs to change. Before this refactoring, a new effect would have required editing the 30-line if/else block inside `playGame()`, adding a print statement in the middle of game logic, and updating `chooseBotCard()`'s four duplicated legality checks.
## What part of your design still makes change difficult?
- **Bot strategy is hard-coded in `chooseBotCard()`** — adding a smarter bot requires editing that method directly. There is no strategy interface to implement.
- **Global state** — a rule variant that changes scoring or win conditions would need to change static fields on `Main`, shared across the whole session.
- **`askHuman()` mixes concerns** — replacing the CLI prompt with a different UI requires changing the same method that parses the index and validates legality.
