package main;

public class Quest {
    
    Gamepanel gp;
    
    public String name;
    public boolean objective1;
    public boolean objective2;
    public boolean objective3;
    
    public Quest(Gamepanel gp) {
        this.gp = gp;
        this.name = "Escape the Island";
        this.objective1 = false;
        this.objective2 = false;
        this.objective3 = false;
    }
    
    public String getCurrentObjective() {
        if(!objective1) {
            return "Find all your friends";
        } else if(!objective2) {
            return "Defeat Nick";
        } else if(!objective3) {
            return "Escape the island";
        } else {
            return "Quest Complete!";
        }
    }
    
    public void completeObjective1() {
        objective1 = true;
        gp.ui.addMessage("Objective Complete: Found all friends!");
    }
    
    public void completeObjective2() {
        objective2 = true;
        gp.ui.addMessage("Objective Complete: Defeated Nick!");
    }
    
    public void completeObjective3() {
        objective3 = true;
        gp.ui.addMessage("Quest Complete: Escaped the island!");
        // Trigger victory
        gp.gameState = gp.victoryState;
    }
    
    public boolean isComplete() {
        return objective1 && objective2 && objective3;
    }
}