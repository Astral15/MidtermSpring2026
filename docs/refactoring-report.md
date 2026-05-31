# Refactoring Report

## What behavior did you characterize before refactoring?

Before touching any production code, 76 JUnit 5 characterization tests were added across four test classes:

| Test class | What it covers |
|---|---|
| `CardParsingTest` | `color()`, `rank()`, `number()`, `points()` — all branches |
| `IsLegalColorNumberActionTest` | `isLegal()` matching by color, number, action rank |
| `IsLegalWildCalledColorTest` | `isLegal()` with wild cards and called-color override |
| `GameBehaviorTest` | `next()` wraparound, bot card/color priority, scoring, empty-deck edge case |

The existing `--self-test` (9 built-in checks) was also verified before any change.

## What were the worst design problems you found?

1. **Duplicated legality logic** — the five-condition legality check existed in three places: `isLegal()`, the main turn loop in `playGame()`, and copied four times inside `chooseBotCard()`.
2. **One 190-line `playGame()` method** — turn flow, card validation, effect application, scoring, and console output were all tangled together.
3. **Card behavior spread across Main** — `color()`, `rank()`, `number()`, `points()`, `isLegal()` were static methods on `Main` with no logical grouping.
4. **Console output mixed with game rules** — every game event printed directly inside the same block that computed the rule outcome, making rules untestable without capturing stdout.
5. **Global mutable state** — all game state is static fields on `Main`, making tests order-dependent and harder to isolate.

## Which refactorings did you perform?

| Commit | Change |
|---|---|
| 7 | Replaced four copies of the inline legality check in `chooseBotCard()` with calls to `isLegal()` |
| 8 | Extracted `Card.java` — all card-parsing and legality logic now lives there; `Main`'s methods delegate; last inline legality copy in `playGame()` also removed |
| 9 | Extracted `applySkip()`, `applyReverse()`, `applyDrawTwo()`, `applyWildDrawFour()`, and `applyCardEffect()` from the `playGame()` if/else block |
| 10 | Extracted `ConsoleView.java` — all `System.out` print calls in game logic replaced with named view methods |

## What behavior did you intentionally preserve?

All behavior documented in `docs/rules.html` was preserved:

- All hands are visible in the terminal during play.
- Humans are allowed to type `draw` even when holding a legal card.
- An illegal index selection causes a penalty card and turn loss.
- Bot players automatically play drawn cards when legal.
- Wild draw four forces the next player to draw four and skip.
- Draw two forces the next player to draw two and skip.
- Reverse with two players acts as a skip.
- The starting up card is re-drawn if it is a wild.

## What risks remain?

- **Global state** — all game fields are still `static` on `Main`; running two games in the same JVM could interfere (the existing multi-game loop resets state in `playGame()`, but tests must also reset manually).
- **`playGame()` is still long** — the turn loop could be further decomposed into `handleTurn()` and `resolvePlay()` methods.
- **Input parsing is still tangled** — `askHuman()` mixes prompt printing with index parsing and legality validation; this was not separated in this refactoring.
- **Scoring is still inline** — the win-detection and point-summation block inside `playGame()` could be extracted to a `scoreWin()` method.
- **No randomness seeding in tests** — `GameBehaviorTest` sets global state directly but does not reset `random`; tests that rely on deck order would be fragile.

