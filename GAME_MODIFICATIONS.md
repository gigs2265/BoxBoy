# Box Boy! - Complete Modification Documentation

## Project Overview
This is a 2D action RPG Java game called "Box Boy!" built with Java Swing/AWT. The player (Theo) wakes up on an island with friends who have been captured, must find them, rescue one more from a dungeon, then defeat a mini-boss and a final boss to escape.

**Technical Specs:**
- Built with Java Swing/AWT (no external libraries/engine)
- 60 FPS fixed-timestep game loop (logic always runs at 60 updates/sec; rendering drops frames on slow machines instead of slowing the game down)
- Tile size: 16px scaled 3x = 48px tiles
- Screen: 960x576 (20x12 tiles)
- World: 50x50 tiles per map
- 3 maps: Overworld (Map 0), Dungeon (Map 1), Josh's Hut interior (Map 2)

**Project Location:** `C:\Users\Owner\Documents\Eclipse workspace\My2dgame`

This file supersedes the older `GAME_MODIFICATIONS.md` history and the separate `BOXBOYNOTES.md` (both deleted 2026-09-21) — it reflects what's actually in the code now, in one place.

**Maintenance note:** this file gets updated alongside every code change from here on, not just in a batch afterward — new modifications get their own numbered section (or an existing one gets amended in place, e.g. when a later fix changes how an earlier mod's UI is laid out) as soon as the change is made, so this doc never drifts out of sync with the code.

---

## Codebase Map

```
src/
  main/     - engine: game loop, input, collision, events, UI, quest, save/load, sound
  entity/   - Entity base class, Player, Projectile, NPC_* (7 friends)
  monster/  - MON_Tuchi, MON_Armored_Tuchi, MON_Kunt_Krab, MON_Nick, MON_Brian
  object/   - weapons, shields, boots, consumables, doors, chest, keys, coins
  tile/     - Tile, TileManager (loads maps from res/maps/*.txt)
```

- **`main.Gamepanel`** owns the entity arrays (`obj`, `npc`, `monster`, `projectileList`), the game loop, and the camera-clamp helpers (`getCameraX/Y`).
- **`main.AssetSetter`** is the single place all objects/NPCs/monsters get spawned per map (`setObject()`, `setNPC()`, `setMonster()`), called both at game start and on retry/restart.
- **`main.UI`** draws every screen (HUD, dialogue, inventory, trade/chest, pause, options, title, game over, victory) and owns the cutscene player.
- **`main.Quest`** is a flat set of booleans plus two getter methods (`getQuestName()`, `getCurrentObjective()`) that the UI reads to render the tracker (press P).

---

## Modification 1: Camera Edge Clamping (Fixed Black Borders)

**Problem:** Near map edges the camera showed black areas outside the map.

**Solution:** `Gamepanel.getCameraX()` / `getCameraY()` clamp the camera to `[0, worldWidth - screenWidth]` / `[0, worldHeight - screenHeight]`. `TileManager.draw()`, `Entity.draw()`, and `Player.draw()` all compute `screenX/screenY` from these clamped values instead of the player's raw world position, so the player sprite visibly slides toward the screen edge (instead of staying dead-center) as the camera stops.

**Files:** `Gamepanel.java`, `TileManager.java`, `Entity.java`, `Player.java`

---

## Modification 2: Weapon-Specific Sound Effects

**Problem:** Every weapon played the same attack sound (spray sound, #6).

**Solution:** `Player.interactNPC()` checks `currentWepon.type` and plays sound 16 (`fartblast.wav`) for Jimmy's Ass (`type_ass`), sound 6 (`spraysound.wav`) for everything else.

**File:** `Player.java`

---

## Modification 3: Progressive Quest System

**Overview:** A linear quest chain enforced through boolean flags in `Quest.java`, checked by each NPC's `speak()` and by boss-kill logic in `Player.damageMonster()`.

**Chain:**
1. **Find all your friends** — talk to 5 overworld NPCs, each requiring their *previous* friend's questline to be done before their own dialogue completion counts:
   - Vic (11 dialogues, no prerequisite) → `talkedToVic`
   - Mike (1 dialogue, needs `talkedToVic`) → `foundMike`
   - Ian (12 dialogues, needs `foundMike`) → `foundIan`
   - Liam (14 dialogues, needs `foundIan`) → `foundLiam` (also unlocks Miles' door)
   - Miles (13 dialogues, needs `foundLiam`) → `foundMiles` → auto-completes `foundAllFriends`
2. **Wait, Where's John?** — find John in the dungeon (7 dialogues, needs `foundAllFriends`) → `foundJohn`
3. **Kill Nick** — defeat the mini-boss → `defeatedNick`
4. **Kill Brian** — defeat the final boss → `defeatedBrian` → victory screen

Each `NPC_*.speak()` override calls `super.speak()` (which advances the dialogue array and sets `dialogueComplete` once it runs out of lines) and then checks `dialogueComplete && <previous flag> && !<own flag>` before calling the matching `Quest.complete*()` method. Each `complete*()` method is idempotent (checks `!flag` before setting it) and pushes an on-screen message via `ui.addMessage()`.

`Quest.getQuestName()` / `getCurrentObjective()` derive the current stage purely from which flags are still false — there's no separate "current stage" field to get out of sync.

**Quest tracker (press P):** `UI.drawQuestTracker()` shows the quest name/objective, then a friend checklist while stage 1 is active, or a single "[ ] Find John" / "[ ] Defeat Nick" / "[ ] Defeat Brian" line for later stages.

**Files:** `Quest.java`, `UI.java`, all `NPC_*.java`, `Player.java` (boss-kill hooks)

---

## Modification 4: Quest-Locked Doors

Two doors block sequence-breaking, each a small `Entity` subclass with `interact()` (bump into it, no key) and `use()` (bump into it while holding a key):

- **`OBJ_VicDoor`** at (23, 32) — refuses to open until `quest.talkedToVic`, even with a key. Message: *"Vic: HEY ASSHOLE GET THE FUCK OVER HERE!!!"*
- **`OBJ_MilesDoor`** at (38, 12) — refuses to open until `quest.canUnlockMilesDoor()` (= `foundLiam`), even with a key. Message: *"HEY STUPID\nDID YOU READ THE QUEST LIST?!"*

Both: if the quest gate isn't met, `use()` returns early without consuming the key or removing the door. `Player.pickUpObject()` special-cases both door names (matched by `.name.equals(...)`) to route to `door.use()`/`door.interact()` instead of the generic pickup path, and explicitly sets `gp.obj[...] = null` to remove the door only when `use()` actually unlocked it.

Regular `OBJ_Door` has no quest gate — any key opens it, no dialogue prerequisite.

**Files:** `OBJ_VicDoor.java`, `OBJ_MilesDoor.java` (created), `AssetSetter.java` (placement), `Player.java` (pickUpObject routing)

---

## Modification 5: Continuous Dialogue Flow

**Problem:** Originally every dialogue line required closing and re-opening the dialogue box by re-walking into the NPC.

**Solution:** `Gamepanel.currentNPC` tracks which NPC slot is currently being talked to (`999` = none). `Player.interactNPC()` sets it when dialogue starts. While in `dialougeState`, `KeyHandler.dialogueState()` calls `speak()` again on that same NPC each time ENTER is pressed, instead of leaving dialogue state. `Entity.speak()` advances through the `dialouges[]` array and, once it runs past the last line, sets `dialogueComplete = true`, resets `currentNPC` to `999`, and flips `gameState` back to `playState` automatically.

**Files:** `Gamepanel.java`, `Player.java`, `KeyHandler.java`, `Entity.java`

---

## Modification 6: Cutscene System

A generalized cutscene player was added to `UI.java`, replacing what earlier notes called a Brian-specific "`startBrianCutscene`" — that method name never shipped; the actual implementation is type-generic from the start.

- `UI.startCutscene(String[] lines, int type, Entity monster)` — begins a cutscene: sets `cutsceneActive`, stores the line array + a `cutsceneType` tag + an optional related monster, shows line 0, switches to `dialougeState`.
- `UI.advanceCutscene()` — called from `KeyHandler.dialogueState()` on ENTER whenever `cutsceneActive` is true (checked *before* the normal NPC-dialogue branch). Steps to the next line, or on the last line runs type-specific end behavior:
  - `CUTSCENE_BRIAN_DEATH` (1): sets the boss `dying = true`, switches to `victoryState`, starts the looping victory theme (sound 19).
  - `CUTSCENE_NICK_INTRO` (2): just returns to `playState`.
  - Any other/default type: returns to `playState`.

**Two cutscenes exist today:**
- **Nick intro** (`EventHandler.nickIntro()`) — fires once, the first time the player crosses a 3-tile-wide trigger strip at the entrance to Nick's dungeon room (checked via `quest.metNick`, which is saved so it won't refire after a reload).
- **Brian death** (`Player.damageMonster()`) — when Brian's HP hits 0, instead of the normal death flow he gets a multi-line "confession" cutscene explaining the rat-monster backstory, and only *then* dies and completes the quest.

**Files:** `UI.java`, `KeyHandler.java`, `EventHandler.java`, `Player.java`, `Quest.java` (`metNick` flag)

---

## Modification 7: Boss AI & Combat Detail

Both bosses use tile-distance-based state machines in `setAction()` (approach / retreat / strafe / ranged-attack bands), distinct from the simple chase-or-wander AI on regular monsters (`MON_Tuchi`, `MON_Armored_Tuchi`, `MON_Kunt_Krab`).

- **`MON_Nick`** (mini-boss, 50 HP): shoots `OBJ_Milkshot` projectiles at medium range, retreats if the player gets within 3 tiles, approaches if too far. `damageReaction()` additionally makes him retreat once his HP drops below a third. Drops a key on death (`checkDrop()`), which opens the door to Brian's arena.
- **`MON_Brian`** (final boss, 40 HP, 4x sprite size): shoots `OBJ_Titmilk` projectiles (heavy-hitter) in a medium band, strafes when the player is adjacent, approaches when far. Every `POOP_INTERVAL` (600 update ticks, ~10s) while a fight is engaged (player within 12 tiles), `poopTuchi()` spawns a fresh `MON_Tuchi` behind him — capped at 3 nearby tuchis at once, and it won't spawn one inside a wall tile. Drops random loot on death.
- **`heavyHitter`** (flag on `Entity`, checked in `damagePlayer()`): a heavy-hitter's damage only gets halved (not fully absorbed) by the player's defense, and always deals at least 1 — armored/boss enemies punch through shields instead of being walled off by them entirely.
- Damage sounds are picked once per hit in `Player.damageMonster()`: Brian has his own hit sound (17) and death sound (18, replacing the normal hit sound on the killing blow, with music stopped so it rings out alone); everyone else uses the shared hit sound (7).

**Files:** `MON_Nick.java`, `MON_Brian.java`, `Entity.java` (`heavyHitter`, `damagePlayer`), `Player.java` (`damageMonster`)

---

## Modification 8: Idle NPC Dialogue Bubbles

Every named NPC (`NPC_Ian`, `NPC_John`, `NPC_Josh`, `NPC_Liam`, `NPC_Mike`, `NPC_Miles`, `NPC_Vic`) has a 12-line `idleDialogues[]` array of ambient one-liners, independent of their main quest dialogue. In `setAction()`, when not currently mid-conversation, each NPC runs a random timer (roughly every 4–5 seconds, per-NPC odds ~30–45%) that pops a random idle line into a speech bubble above their head for ~3 seconds. Drawn by `Entity.drawDialogueBubble()` — a rounded black box with a white border and a small triangle pointer, positioned above the sprite, drawn after the invincibility-flicker alpha is restored so it's never faded out.

**Files:** `Entity.java` (`drawDialogueBubble`, idle-dialogue fields), all `NPC_*.java`

---

## Modification 9: Save / Load System

Single-slot save to `save.dat` via Java object serialization.

- **`DataStorage`** (`Serializable`) is a flat bag of everything worth persisting: player stats, position, current map, inventory (by item name only — see below), and every `Quest` flag, plus per-map object state (name/position for every object slot on every map, plus opened/loot for chests).
- **`SaveLoad.save()`** walks `gp.player`, `gp.quest`, and every `gp.obj[map][i]` and fills a `DataStorage`, then serializes it.
- **`SaveLoad.load()`** deserializes, restores player stats/position/quest flags, rebuilds the inventory by looking up each saved item name through `getObject(String)` (a name→`new OBJ_*` switch — inventory items are **not** serialized directly, just re-constructed from their name, which is why equipping is restored by *slot index* (`currentWeaponSlot`/`currentShieldSlot`) rather than identity), then re-runs `AssetSetter.setNPC()`/`setMonster()` (NPCs/monsters always respawn at their default spots — only objects and quest state persist) before rebuilding every map's object layout from the saved names/positions. Chest `opened` state and remaining loot are restored the same way. Older saves with fewer object slots than the current build are handled by only reading `min(current slots, saved slots)`.
- Reachable from the title screen ("Load Game") and from the pause/options menu ("Save Game").

**Files:** `SaveLoad.java`, `DataStorage.java`, `KeyHandler.java` (title/options hooks), `UI.java` (options menu save trigger)

---

## Modification 10: Josh's Hut (Map 2)

A third map was added: Josh's hut interior, loaded from `res/maps/hutmap.txt`. Stepping onto the hut tile on the overworld (11, 30) teleports the player inside (25, 26); stepping on the door tile inside (25, 28) teleports back out to (11, 31). Josh appears here as a second placement (he's also in the dungeon, at (42, 34) on Map 1) acting as a merchant with the same trade inventory (Beer, Jimmy's Ass, Hot Burg, Ian's Sword, a purchasable Key).

**Files:** `TileManager.java` (loads `hutmap.txt` as map 2), `EventHandler.java` (teleport triggers), `AssetSetter.java` (Josh spawn in map 2), `res/maps/hutmap.txt`

---

## Modification 11: "PC Mode" (sanitized-dialogue toggle)

An opt-in setting that swaps the game's raunchy text for a uniformly flat, over-corrected corporate/HR-euphemism voice — an ironic "family friendly" mode. The joke is that every character starts sounding like the same nervous compliance memo, regardless of who they are; the mechanic never touches gameplay, only what's displayed.

### Toggle & persistence

- **`Gamepanel.pcMode`** (boolean, default `false`) is the single source of truth, checked live wherever text is drawn — no restart required, unlike Full Screen.
- New **"PC Mode"** row in the Options menu, directly under "Full Screen", toggled with ENTER exactly like a checkbox (fills in the same square-checkbox UI Full Screen uses, drawn just below it). All the other rows below it (Music, Sound EF, Controls, Save Game, Fuck Off, Back) shifted down one `commandNum` slot to make room, and the Options subwindow grew by one tile-row (`frameHeight` 10→11 tiles, `frameY` nudged up half a tile) since the original layout had zero pixels of spare margin before "Back".
- **Post-playtest fix:** the gap before "Back" was originally `tileSize*2` (double every other row's spacing), which put its baseline exactly on the subwindow's bottom edge (552px, with `frameY=24` + `frameHeight=528`) — visually spilling past the frame border. Changed to a single `tileSize` gap like every other row, landing "Back" at 504px, 48px clear of the bottom edge. Fixed and confirmed working in-game.
- Persisted as a 4th line in `config.txt` (`on`/`off`, same convention as Full Screen), read/written by `Config.java`. Old 3-line config files load fine — the missing line just leaves `pcMode` at its in-memory default (`false`), same fallback behavior the existing Music/SE fields already had for a totally missing file.

### The lookup mechanism

**`main.PcText.pick(Gamepanel gp, String original, String pc)`** — a single static helper used everywhere. Returns `pc` if `gp.pcMode` is true *and* `pc` is non-null/non-empty; otherwise falls back to `original`. This means a line that's already inoffensive never needs a PC-mode duplicate authored — its `pc` slot is simply left unset (`null`/`""`) and `pick()` quietly falls through. (A few lines were written as literal duplicates anyway rather than left null — harmless, just slightly more verbose than necessary.)

For content that's a whole array rather than one string (cutscene line lists, the victory-screen paragraph, the title-screen menu labels), the same idea is done manually with a `gp.pcMode ? pcArray : originalArray` ternary at the point of use, since `PcText.pick()` only handles single strings.

**One firm rule:** an `Entity`'s `name` field is *never* touched by PC mode. `name` is used throughout the codebase as a stable identity key — inventory lookups (`searchItemInInventory`), door/chest routing in `Player.pickUpObject()`, boss-kill checks (`monsterName.equals("Brian")`), and the whole `SaveLoad`/`DataStorage` save-file format all switch on exact `name` strings. Renaming an item or NPC's `name` under PC mode would silently break saves and pickup/quest logic the moment the toggle changed. Only *displayed* text — descriptions, dialogue, system messages — is swapped.

### What's covered

| Area | Mechanism | Coverage |
|---|---|---|
| Item descriptions | `Entity.pcDescription` field (parallel to `description`), read via `PcText.pick()` in `UI.drawInventory()`'s item-detail popup | Beer, Bloat, Box Cutter, Ian's Sword, Jimmy's Ass, Rat Spray, Hot Burg, Rat Tail (Key's description was already clean, left as-is) |
| Consumable-use flavor text | `PcText.pick()` inline at the `currentDialouge =` assignment in `use()` | Beer, Rat Tail |
| Door/chest messages | `PcText.pick()` inline | `OBJ_VicDoor`, `OBJ_MilesDoor` (the profane lines); `OBJ_Door`/`OBJ_Chest` needed no changes — their messages were already clean |
| Environmental messages | `PcText.pick()` inline | The damage-pit message in `EventHandler.damagePit()` |
| Cutscenes | Parallel `nickCutscenePc`/`brianCutscenePc` arrays on `UI`, same line count/order as the originals, selected via ternary at the two `startCutscene()` call sites (`EventHandler.nickIntro()`, `Player.damageMonster()`) | Both cutscenes, full rewrite, 6 and 10 lines respectively |
| System/menu text | `gp.pcMode ? ... : ...` ternary inline | Victory screen ("You beat the game!" body text + "Thanks for wasting your time" line), Game Over screen title ("You Suck!" → "Nice Try!"), the full-screen-restart notice, the title-screen and options-menu "Fuck Off" labels (→ "Exit Kindly", picked to be short enough it doesn't overflow the title screen's fixed-width menu columns) |
| NPC quest dialogue | `Entity.pcDialouges[]` (parallel to `dialouges[]`, same index), picked inside the shared `Entity.speak()` | All 7 NPCs, full line-for-line rewrite (Vic 11, Mike 1, Ian 12, Liam 14, Miles 13, John 7, Josh 1 — 59 lines total) |
| NPC idle bubbles | A per-NPC `idlePcDialogues[]` field (parallel to `idleDialogues[]`), picked at the point each NPC rolls a random idle line in `setAction()` | All 7 NPCs × 12 lines = 84 lines total |
| Item **names** (not just descriptions) | `Entity.pcName` field (parallel to `pcDescription`), plus `Entity.getDisplayName(gp)` helper; picked inline via `PcText.pick(gp, name, pcName)` at every draw site | Only the two names that are actually profane — `"$Fartcoin"` → `"$FlatuCoin"`, `"Jimmy's Big Fat Ass"` → `"Jimmy's Large Backside"` (was "Jimmy's Generous Donation" in v3, renamed in v4). Already-clean names (Beer, Rat Tail, Box Cutter, Hot Burg, Ian's Sword, Bloat, Rat Spray) were deliberately left alone — their crude humor lives only in descriptions, which was already covered above. |
| Monster **name** | Same `Entity.pcName`/`getDisplayName(gp)` mechanism | `MON_Kunt_Krab`: `"a Kunt Krab"` → `"a Kitty Krab"` in the "You killed ...!" kill message (`Player.damageMonster()`). The identity string used for boss-kill routing (`monsterName.equals("Brian")`/`"Nick"`) is untouched — only the display call at the kill-message line was swapped to `getDisplayName(gp)`. |
| Level-up message | `gp.pcMode ? ... : ...` ternary inline in `Player.checkLevelUp()` (a spot the original coverage sweep missed) | `"Your dumbass reached level X\nGood job idiot!"` → `"Great job! You reached level X!\nYou are so good at this game!"` |

Menu *labels* that aren't jokes (Options, Music, Controls, item names, etc.) were left untouched — only the parts that were actually part of the game's crude voice got a PC rewrite.

**`name` vs `pcName`:** exactly like `pcDialouges`/`pcDescription`, `pcName` is purely cosmetic — every identity path (`SaveLoad`'s name→object `switch`, the `item.name.equals("$Fartcoin")` currency check in `UI.pickUpObject`-adjacent chest logic, `Player.pickUpObject()` door/chest routing) still switches on the real `name` field, untouched. Display sites updated to go through `pcName`: `UI`'s "Obtained X!" message, the chest-currency pickup message, `OBJ_Fart_Coin.use()`'s pickup message, both trade-window `$FartCoin:`/`$FlatuCoin:` balance labels, the character-screen `$fartcoin`/`$flatucoin` stat label, and the "Not enough Fartcoins."/"Not enough FlatuCoins." shop error. `OBJ_Jimmysass`'s `pcDescription` now interpolates `pcName` instead of the raw profane `name` in its bracketed header.

### Verification approach

The game can't be driven headlessly in this environment (no display for the Swing window, no synthetic key events), so testing combined:
- A full-project `javac` compile after every batch of changes.
- A standalone reimplementation of `Config.save()`/`load()`'s exact logic, run against throwaway files, covering: full round-trip with PC Mode on/off, an old 3-line config file loading safely with PC Mode defaulting correctly, and a missing config file not crashing.
- A standalone mirror of the Options-menu `commandNum` navigation math, confirming all 8 rows wrap correctly in both directions and that every row index used elsewhere (the "Controls" and "Fuck Off" back-navigation resets) still lands on the right label.
- Pixel-math verification (via a small Python calculation, not a screenshot) that the taller Options subwindow still fits inside the 576px screen height and that the new "Exit Kindly" label doesn't run past the title screen's right edge.
- A regression script scanning every PC-branch string in the source (all `PcText.pick()` second arguments, every `pcDescription`/`pcDialouges`/`idlePcDialogues` value, every `pcMode ?` ternary branch) against a profanity/slur wordlist — confirmed clean across all ~185 PC-mode lines written.
- A second regression script confirming no original/PC pair was accidentally left identical (which would silently mean "PC mode do nothing" for that line) — the only matches found were lines that were legitimately already clean before PC mode existed, which is correct, not a bug.
- An index-alignment script confirming every `dialouges[i]`/`idleDialogues[i]` has a same-indexed `pcDialouges[i]`/`idlePcDialogues[i]` (and vice versa) for all 7 NPCs, and that the two cutscene arrays have matching line counts (6 and 10) — a silent off-by-one here would show the wrong PC line, or an array-index exception if the PC array were shorter.

**Files:** `main/PcText.java` (new), `main/Gamepanel.java` (`pcMode` field), `main/Config.java` (persistence), `main/UI.java` (Options menu row + layout resize, cutscene arrays, victory/game-over/title/fullscreen-notice text, item-name display sites), `main/EventHandler.java` (pit message, Nick cutscene selection), `entity/Entity.java` (`pcDescription`, `pcDialouges[]`, `pcName`, `getDisplayName()`, `speak()` lookup), `entity/Player.java` (Brian cutscene selection, monster kill-message display, level-up message), all 7 `NPC_*.java` (`idlePcDialogues[]` + content), `object/OBJ_Beer.java`, `object/OBJ_Rattail.java`, `object/OBJ_VicDoor.java`, `object/OBJ_MilesDoor.java`, `object/OBJ_Fart_Coin.java` (`pcName`, pickup message), `object/OBJ_Jimmysass.java` (`pcName`, `pcDescription` fix), `monster/MON_Kunt_Krab.java` (`pcName`), plus `pcDescription` content in `OBJ_Bloat.java`, `OBJ_Boxcutter.java`, `OBJ_Dildo_Sword.java`, `OBJ_Spicyburger.java`, `OBJ_Spray_Normal.java`

### Status: playtested, dialogue confirmed working

In-game playtest confirmed all dialogue (NPC quest lines, idle bubbles, item descriptions, etc.) works as expected under PC Mode. One layout bug was found and fixed (see the pause-menu "Back" button note above). Remaining next-session TODO items below are still open.

**One Eclipse gotcha hit while wrapping up this session, worth remembering:** a brand-new source file (`PcText.java`) created directly on disk didn't get picked up by Eclipse's workspace model — every other class recompiled fine on Clean/Build, but `PcText.class` was silently never produced, which surfaced as "main.PcText.pick cannot be resolved" everywhere it's called. Fix was a plain **Refresh (F5)** on the project *before* Clean/Build — Clean/Build alone isn't enough when a file appeared outside Eclipse's own editor.

### Next session TODO
- [x] **Playtest PC Mode in-game.** Dialogue confirmed working as expected.
- [x] Pause menu "Back" button spilling past the frame border — fixed, see above.
- [x] Item name PC rewrites (`$Fartcoin`/Jimmy's Ass) implemented — compiled clean, not yet playtested.
- [x] Monster name PC rewrite (`Kunt Krab` → `Kitty Krab`) implemented — compiled clean, not yet playtested.
- [ ] **Playtest the new item-name PC rewrites specifically:** pick up a Fartcoin (world + chest) and check the pickup message/HUD balance/character-screen label all show "FlatuCoin" under PC Mode; buy/sell at Josh's shop and check the price window + "Not enough FlatuCoins." error; view Jimmy's Ass's item-detail popup under PC Mode and confirm the bracketed header shows "Jimmy's Large Backside" not the original name.
- [ ] **Playtest the Kunt Krab rename:** kill one under PC Mode and confirm the kill message reads "You killed a Kitty Krab!" not the original name.
- [x] Level-up message PC rewrite implemented — compiled clean, not yet playtested.
- [ ] Confirm config.txt round-trips correctly from the real game (quit after toggling PC Mode on, relaunch, check it's still on) — logic was verified with a standalone harness but never through the actual `Main`/`Config` path.
- [ ] If anything reads awkwardly in-game (line wrapping in the dialogue box, a line running past the item-description subwindow, etc.), that's a real find the static checks couldn't catch — the dialogue/description boxes wrap on `\n` you place manually, so a rewritten line with different length than the original could sit differently even if it's under the same character-ish budget.
- [ ] Optional: extend PC Mode coverage to anything found in step 1 that got missed (see the Future Enhancement Ideas note on this).

---

## Modification 12: Boots Equip Slot & Fresh Timbs

**Status: shipped in v4 - playtested, confirmed working.**

A third equip slot alongside weapon and shield: boots, which affect movement speed instead of attack/defense.

- **`Entity.type_boots`** (type 10) is the new item type, and **`Entity.speedValue`** (parallel to `attackValue`/`defenseValue`) is the stat it carries.
- **`Entity.currentBoots`** is the new equip-slot field, parallel to `currentWepon`/`currentSheild`.
- **`Player.baseSpeed`** replaces the old hardcoded `speed = 4` in `setDefaultValues()` - `speed` itself is now always derived via **`Player.getSpeed()`** (`baseSpeed + currentBoots.speedValue`), the same pattern `getAttack()`/`getDefense()` already used for strength/dexterity. `getSpeed()` is called on equip (`selectItem()`), on `setDefaultValues()`, and after `SaveLoad.load()`.
- **`object.OBJ_Flats`** - the default boots (`speedValue = 0`), equivalent to Rat Spray/Bloat being the default weapon/shield. Player always starts with a pair equipped (`setDefaultValues()`/`setItems()`) so `currentBoots` is never null.
- **`object.OBJ_FreshTimbs`** - the new purchasable/found item (`speedValue = 2`, price 2). Sold at both of Josh's shop locations (dungeon + hut, same `NPC_Josh.setItems()`); one free copy also placed on the overworld at (40, 3), in the walled north clearing.

**UI:** `UI.drawCharacterScreen()` gained a "Speed" stat line (grouped with Attack/Defense) and a "Stats" title (same centered-title style as the Equipment window below). Equipment icons (weapon/shield/boots) no longer live in the stats window at all — adding the boots icon there originally overflowed the frame, so equipment now has its **own window**: `UI.drawEquipmentWindow(frameX, frameY)`, same `drawSubWindow()` style, titled "Equipment", positioned immediately to the right of the stats window (`frameX + frameWidth` of the stats frame, same `frameY`), sized 4×5 tiles. Each row is `Weapon:`/`Shield:`/`Boots:` with its icon drawn inline at half size (`gp.tileSize/2`, scaled down from the native 48px via the `drawImage(img,x,y,w,h,null)` overload) immediately after the label, rather than stacked label-above-full-size-icon — keeps each item on one line and leaves the window shorter. There's a clear 1-tile gap before the inventory grid window further right. `UI.drawInventory()`'s equipped-item highlight and the trade screen's "can't sell equipped" check both now also check `currentBoots`.

**Save/Load:** `DataStorage.currentBootsSlot` persists which inventory slot is equipped as boots, mirroring `currentWeaponSlot`/`currentShieldSlot`. Because `DataStorage`'s `serialVersionUID` is pinned at `1L`, older save files missing this field just deserialize it as `0` rather than failing to load - `SaveLoad.load()` guards against that by only trusting the restored slot if the item actually sitting there is `type_boots`, otherwise the constructor's default Flats stay equipped. `SaveLoad.getObject()` also gained `"Flats"`/`"Fresh Timbs"` name→object cases.

**Files:** `entity/Entity.java` (`type_boots`, `speedValue`, `currentBoots`), `entity/Player.java` (`baseSpeed`, `getSpeed()`, equip handling), `object/OBJ_Flats.java` (new), `object/OBJ_FreshTimbs.java` (new), `entity/NPC_Josh.java` (shop stock), `main/AssetSetter.java` (overworld pickup), `main/UI.java` (Speed row, new `drawEquipmentWindow()`, equip-highlight/sell checks), `main/DataStorage.java` + `main/SaveLoad.java` (persistence)

---

## Modification 13: New Decorative/Wall Tiles (nickwall, rathead)

**Status: shipped in v4 - playtested, confirmed working.**

Two new tile art assets registered in `TileManager`:

- **`tile[45]` "nickwall"** - a collidable wall variant (`collision = true`), loaded from `/tiles/nickwall.png.png` (the source file itself has a double `.png` extension, so the resource path spells it out literally rather than relying on any auto-append).
- **`tile[46]` "rathead"** - a decorative floor overlay (no collision), loaded from `/tiles/rathead.png`.

`tile[]` array size grew from 47 to 49 slots to fit both.

**Placements (dungeon map, `dundgonmap.txt`):**
- rathead at (col 10, row 32), (col 26, row 11), (col 36, row 6) - all previously plain floor.
- nickwall ×3 side-by-side at row 2, cols 28/29/30 - directly above the open room at row 3, replacing plain wall tiles.

**Files:** `tile/TileManager.java` (tile registration), `res/maps/dundgonmap.txt` (placements)

---

## Modification 14: PC Mode Sound Effects

**Status: shipped in v4 - playtested, confirmed working.**

Extends "PC Mode" (Modification 11) from text-only to also swap sound effects, via an indirection layer that only PC-affected clips pass through.

- **`Gamepanel.pcSoundIndex(int i)`** - private helper checked inside `playMusic()`/`playSE()`. If `pcMode` is on, remaps a small set of clip indices to their PC variants; everything else (the large majority of sounds) passes through unchanged.
- Remapped: 8 (hittaken)→20, 16 (fartblast)→21, 17 (brianhit)→22, 18 (briandies)→23, 19 (victorytheme)→24.
- **`Sound.java`**: `soundURL[]` grew from 30 to 35 slots to hold the 5 new PC clips (`pchittaken.wav`, `pcfartblast.wav`, `pcbrianhit.wav`, `pcbriandies.wav`, `pcvictorytheme.wav`), all under `res/sound/`.

**Files:** `main/Gamepanel.java` (`pcSoundIndex()`), `main/Sound.java` (new clip slots)

---

## Modification 15: Bug Fixes (Bloat re-equip, Josh shop "Leave" loop)

**Status: shipped in v4 - playtested, confirmed working.**

Two unrelated bugs found and fixed while testing the boots feature (Modification 12):

**Bloat could not be re-equipped after switching shields.** `OBJ_Bloat` (the starting/default shield) never set `type` in its constructor, so it defaulted to `0` (`type_player`) instead of `type_sheild`. `Player.selectItem()`'s equip logic branches on `selectedItem.type == type_sheild`, so selecting Bloat from the inventory silently did nothing once you'd switched to Hot Burg (or any other shield) - the only reason this went unnoticed earlier is that Bloat is pre-equipped at game start, so nobody had tried to re-select it from the inventory grid until now. Fixed by adding `type = type_sheild;` to `OBJ_Bloat`'s constructor.

**Talking to Josh, then picking "Leave," dropped you back into the Buy/Sell/Leave menu instead of closing it** - you had to select Leave twice. Root cause: `NPC_Josh.speak()` unconditionally set `gameState = tradeState` after every call to `super.speak()`, but `speak()` is also the same method `KeyHandler.dialogueState()` re-invokes on ENTER to dismiss Josh's farewell line ("See you later!") once `Entity.speak()`'s dialogue-exhausted branch has already routed back to `playState`. The unconditional trade-state assignment ran *after* that and stomped it back to `tradeState`. Fixed by only opening the shop when `dialougeIndex != 0` right after `super.speak()` - true exactly when a line was just shown (dialogue still in progress), false when `Entity.speak()` just wrapped `dialougeIndex` back to 0 on exhaustion (dialogue finished, already routed to `playState` - don't reopen the shop). `Entity.dialogueComplete` was considered as the guard instead but rejected: it's a one-way flag that never resets to `false`, so it would correctly skip the shop the first time but then never let it reopen on any later visit.

**Files:** `object/OBJ_Bloat.java` (added `type = type_sheild`), `entity/NPC_Josh.java` (`speak()` guard)

---

## Modification 16: Fullscreen Black Screen Fix

**Status: shipped in v4 - playtested, confirmed working.**

**Problem:** turning on Full Screen (Options menu -> restart) produced a black screen instead of the game.

**Root cause:** `Gamepanel.setFullScreen()` used `GraphicsDevice.setFullScreenWindow()` - real exclusive fullscreen mode. That conflicts with how this game actually renders: `drawToScreen()` calls plain `JPanel.getGraphics()` every frame and draws into whatever it returns, which is the pattern Swing/AWT explicitly warns against once a `GraphicsDevice` owns the screen exclusively - the display-mode handoff can leave that call drawing into a stale or invalid graphics context. On top of that, `screenWidth2`/`screenHeight2` were read from `Main.window.getWidth()/getHeight()` *immediately* after `setFullScreenWindow()` returned, before the window's resize to the new display mode was guaranteed to have actually happened - a race that could hand `drawToScreen()`'s final `drawImage()` a 0x0 (or otherwise wrong) target size.

**Fix:** `setFullScreen()` no longer touches `GraphicsDevice` at all. It maximizes the (already undecorated, per `Main.java`) window instead - `Main.window.setExtendedState(JFrame.MAXIMIZED_BOTH)` plus an explicit `setBounds()` to the screen's usable area (`GraphicsEnvironment.getMaximumWindowBounds()`, read synchronously, no race) - which fills the screen without ever taking exclusive ownership of the display, so the existing `getGraphics()`-based render path keeps working normally. `screenWidth2`/`screenHeight2` are set directly from those same bounds. The `JFrame`'s content pane already uses the default `BorderLayout` (`window.add(gamePanel)` with no constraint = `BorderLayout.CENTER`), so `gamePanel` automatically resizes to fill the maximized window and `getGraphics()` returns a correctly-sized context - no other changes needed.

Fullscreen still only takes effect on the next full restart (unchanged - toggling it in the Options menu still just flips `fullScreenOn` and shows the existing "restart required" notice; `setFullScreen()` is only ever called once, from `setupGame()`).

**Files:** `main/Gamepanel.java` (`setFullScreen()` rewritten, dropped the now-unused `GraphicsDevice` import)

---

## Modification 17: Selling to Josh Paid 0 Fartcoins

**Status: shipped in v4 - playtested, confirmed working.**

**Problem:** selling items to Josh (reported at the hut, but both Josh placements share the same code) removed the item but paid nothing.

**Root cause:** `UI.trade_sell()` pays `item.price / 2`, but most items never set `price`, so it defaulted to `0`: Key (every key found in the world or carried from the start - only the keys Josh sells had `price = 10`, set in `NPC_Josh.setItems()`), Box Cutter, Rat Spray, Bloat, Flats, and Burg. On top of that, integer division meant any price-1 item would also sell for 0.

**Fix:**
- Added base prices: Rat Spray 2, Bloat 2, Burg 2, Flats 2, Box Cutter 6, Key 10 (matching Josh's price, so free and bought keys now both sell for 5).
- `trade_sell()` now pays `Math.max(1, price / 2)`, so no item can ever sell for 0.

Selling an equipped item is still blocked, as before.

**Files:** `main/UI.java` (`trade_sell()`), `object/OBJ_Spray_Normal.java`, `object/OBJ_Boxcutter.java`, `object/OBJ_Bloat.java`, `object/OBJ_Burger.java`, `object/OBJ_Flats.java`, `object/OBJ_Key.java` (added `price`)

---

## NPC & Location Reference

**Overworld NPCs (Map 0):**
| NPC | Position | Dialogues | Gate |
|---|---|---|---|
| Vic | (22, 22) | 11 | — |
| Mike | (3, 34) | 1 | `talkedToVic` |
| Ian | (17, 6) | 12 | `foundMike` |
| Liam | (30, 45) | 14 | `foundIan` |
| Miles | (38, 4), behind locked door at (38, 12) | 13 | `foundLiam` |

**Dungeon NPCs (Map 1):**
| NPC | Position | Role |
|---|---|---|
| John | (5, 41) | Captive, first cell, 7 dialogues |
| Josh | (42, 34) | Merchant |

**Josh's Hut (Map 2):**
| NPC | Position | Role |
|---|---|---|
| Josh | (25, 22) | Merchant (same stock as dungeon Josh) |

**Bosses (Map 1):**
| Monster | Position | Notes |
|---|---|---|
| Nick | (9, 29) | Mini-boss, ranged, drops key, triggers intro cutscene on approach |
| Brian | (22, 8) | Final boss, 4x size, ranged + spawns tuchis, death cutscene → victory |

**Map transitions (`EventHandler`):**
- Overworld (48, 5) any direction → Dungeon entrance hallway (5, 47)
- Dungeon (2, 47) any direction → Overworld stairs (47, 5)
- Overworld (11, 30) → Josh's Hut (25, 26)
- Josh's Hut (25, 28) → Overworld (11, 31)
- Two damage-pit trigger tiles on the overworld: (28, 31) facing up, (13, 23) any direction — each deals 1 damage and shows a message
- Dungeon (14, 29)/(14, 30)/(14, 31), any direction — one-time Nick intro cutscene trigger

---

## Known Issues / Loose Ends

- **`OBJ_Door.use()` never sets `collision = false`** after unlocking, unlike `OBJ_VicDoor`, `OBJ_MilesDoor`, and `OBJ_Chest`, which all do. A regular door may stay solid after being "unlocked" even though `Player.pickUpObject()` nulls out the object reference on the same call (masking the bug in the common case, but the entity itself is still inconsistent with the others).
- **`src/main/window.java`** is an empty file — dead, unused.
- **`src/object/Pest.java`** and **`src/object/OBJ_Pest.java`** are near-duplicate unused classes, both incorrectly named `"Key"` internally; nothing in `AssetSetter` spawns either.
- **`res/maps/map.txt`** (12 lines) is an unused leftover — the three loaded maps are `world.txt`, `dundgonmap.txt`, and `hutmap.txt`.
- **`res/npc/New Piskel-1.png(3).png`** and **`res/player/Theo-1.png.png`** look like raw export/scratch files, not referenced by any `setup("/…")` call.

---

## Important Constants Cheat Sheet

- `type_ass = 9` (Jimmy's Ass weapon type); other weapon types: `type_spray=3`, `type_cutter=4`, `type_dildo=8`; `type_boots = 10` (Flats/Fresh Timbs)
- Sound effects: 4=pickup/use, 5=unlock, 6=spray/milkshot fire, 7=hit monster, 8=hit player, 9=level up, 10=cursor, 11=puke/shoot, 12=coin, 13/3=die, 14=transition, 15=dungeon music, 16=fart blast, 17=Brian hit, 18=Brian dies, 19=victory theme, 20-24=PC Mode variants of 8/16/17/18/19 (see Modification 14)
- Map 0 = Overworld, Map 1 = Dungeon, Map 2 = Josh's Hut
- `currentNPC = 999` means no NPC is being talked to; the same `999` sentinel is reused for "no index found" in `CollisionChecker` and inventory search
- `heavyHitter = true` on: Armored Tuchi, Kunt Krab, Brian, Brian's Titmilk projectile (Nick is NOT a heavy hitter)

---

## Compilation & Workflow

```bash
cd "C:\Users\Owner\Documents\Eclipse workspace\My2dgame"
javac -d bin -cp bin src/path/to/File.java
```

Eclipse workflow: modify → refresh project (F5) → Clean (Project → Clean) → Eclipse auto-builds. Fall back to manual `javac` if issues persist.

---

## Future Enhancement Ideas

- Optional side quests / quest rewards (items, XP bonuses)
- Quest journal with full history, quest markers on map
- Multiple save slots
- Fix the `OBJ_Door` collision bug noted above
- Clean up dead files (`window.java`, `Pest.java`/`OBJ_Pest.java`, `map.txt`, stray export PNGs)
- Dialogue skip/fast-forward option
- Achievement system tied to quests
- PC Mode: extend to any remaining flavor text found later (only the areas listed in Modification 11's coverage table were touched — a fresh grep for profanity across `src/` is the fastest way to check for stragglers before assuming coverage is total)
- More boots beyond Flats/Fresh Timbs (e.g. a negative-speed "heavy boots" tradeoff item, or a defense/speed hybrid) now that the `type_boots` slot exists (Modification 12)
