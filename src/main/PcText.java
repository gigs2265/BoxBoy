package main;

// Tiny helper for "PC Mode" (see Gamepanel.pcMode). Every place in the game
// that shows text can have a sanitized version alongside the normal one;
// this just picks which one to actually display.
//
// Convention: if a line doesn't need a PC rewrite (it's already clean),
// leave its pc-array slot / pc-field null or empty - pick() falls back to
// the original automatically, so nobody has to author a pointless near-duplicate.
public class PcText {

	// Returns pc if PC Mode is on AND pc is non-null/non-empty, otherwise
	// falls back to original. Never returns null if original isn't null.
	public static String pick(Gamepanel gp, String original, String pc) {
		if(gp.pcMode == true && pc != null && pc.length() > 0) {
			return pc;
		}
		return original;
	}
}
