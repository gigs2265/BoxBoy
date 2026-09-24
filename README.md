# Box Boy!

A 2D action RPG built with Java Swing/AWT. Theo wakes up on an island with his
friends captured — he has to find them, rescue one more from a dungeon, then
defeat a mini-boss and a final boss to escape.

**Tech:** Java Swing/AWT, no external engine/libraries. 60 FPS fixed-timestep
game loop. 16px tiles scaled 3x (48px), 960x576 screen (20x12 tiles), 50x50
tile maps, 3 maps (Overworld, Dungeon, Josh's Hut).

For the full technical breakdown of every system in the game (quest system,
save/load, cutscenes, boss AI, etc.), see [GAME_MODIFICATIONS.md](GAME_MODIFICATIONS.md).

## Running it

```bash
cd "C:\Users\Owner\Documents\Eclipse workspace\My2dgame"
javac -d bin -cp bin $(find src -name '*.java')
java -cp bin main.Main
```

Or open in Eclipse: Refresh (F5) → Clean (Project → Clean) → Run.

## Version History

### v4 (current)
**Boots equip slot** — a third equipment slot next to weapon and shield. Boots
add movement speed: everyone starts in Flats (no bonus), and **Fresh Timbs**
(+2 speed) can be bought from Josh or found in the walled north clearing. The
character screen now shows a Speed stat and has its own Equipment window
showing weapon, shield and boots.

Also in this version:
- **PC Mode sounds** — PC Mode now also swaps five sound effects (player hit,
  fart blast, Brian's hit and death sounds, victory theme) for sanitized versions.
- **New dungeon tiles** — `nickwall` (solid wall) and `rathead` (floor decoration)
  placed around the dungeon.
- **Fixed:** selling to Josh paid 0 Fartcoins for most items (keys, Box Cutter,
  Rat Spray, Bloat, Flats). Every item now has a price, and nothing sells for less than 1.
- **Fixed:** Bloat couldn't be re-equipped after switching to another shield.
- **Fixed:** choosing "Leave" at Josh's shop reopened the Buy/Sell menu instead
  of closing it.
- **Fixed:** turning on Full Screen gave a black screen.
- PC Mode name for Jimmy's Ass changed to "Jimmy's Large Backside".

See Modifications 12–17 in `GAME_MODIFICATIONS.md` for implementation detail.

### v3
**"PC Mode"** — an opt-in, ironic "family friendly" toggle in the Options menu
(ENTER to check/uncheck, no restart required) that swaps the game's raunchy
dialogue, item names, monster names, and system messages for a uniformly flat,
over-corrected corporate/HR-euphemism voice. Gameplay is unaffected — only
displayed text changes. Persisted in `config.txt`.

Covers:
- All 7 NPCs' quest dialogue and idle dialogue bubbles
- Item descriptions and the two profane item names (`$Fartcoin` → `$FlatuCoin`,
  Jimmy's Big Fat Ass → Jimmy's Generous Donation)
- The `Kunt Krab` monster's kill message (→ `Kitty Krab`)
- Both cutscenes (Nick intro, Brian death)
- Victory screen, Game Over screen, level-up message, door/chest messages,
  environmental (damage pit) messages, and menu labels

Also in this version:
- Fixed a pause-menu layout bug where the "Back" button's baseline sat exactly
  on the Options subwindow's bottom edge (zero margin), visually spilling past
  the frame border. Now uses the same row spacing as every other menu item.

See the "Modification 11: PC Mode" section of `GAME_MODIFICATIONS.md` for full
implementation detail.

### Earlier versions
See `git log` and `GAME_MODIFICATIONS.md` (Modifications 1–10) for the camera
clamping, weapon sounds, quest system, quest-locked doors, continuous dialogue
flow, cutscene system, boss AI, idle NPC bubbles, save/load system, and Josh's
Hut additions that shipped before PC Mode.
