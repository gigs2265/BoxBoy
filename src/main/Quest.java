package main;

public class Quest {

    Gamepanel gp;

    public String name;

    // Main quest stages
    public boolean foundAllFriends = false;
    public boolean foundJohn = false;
    public boolean defeatedNick = false;
    public boolean defeatedBrian = false;

    // Friend-finding sub-quests (overworld)
    public boolean talkedToVic = false;
    public boolean foundMike = false;
    public boolean foundIan = false;
    public boolean foundLiam = false;
    public boolean foundMiles = false;

    public Quest(Gamepanel gp) {
        this.gp = gp;
        this.name = "Find all your friends";
    }

    public String getQuestName() {
        if(!foundAllFriends) {
            return "Find all your friends";
        } else if(!foundJohn) {
            return "Wait, Where's John?";
        } else if(!defeatedNick) {
            return "Kill Nick";
        } else if(!defeatedBrian) {
            return "Kill Brian";
        } else {
            return "Quest Complete!";
        }
    }

    public String getCurrentObjective() {
        if(!foundAllFriends) {
            return "Find your friends on the island";
        } else if(!foundJohn) {
            return "Find John";
        } else if(!defeatedNick) {
            return "Defeat Nick";
        } else if(!defeatedBrian) {
            return "Defeat Brian";
        } else {
            return "Escape the island!";
        }
    }
    
    public void completeTalkToVic() {
        if(!talkedToVic) {
            talkedToVic = true;
            gp.ui.addMessage("Found Vic!");
        }
    }

    public void completeFoundMike() {
        if(!foundMike) {
            foundMike = true;
            gp.ui.addMessage("Found Mike!");
        }
    }

    public void completeFoundIan() {
        if(!foundIan) {
            foundIan = true;
            gp.ui.addMessage("Found Ian!");
        }
    }

    public void completeFoundLiam() {
        if(!foundLiam) {
            foundLiam = true;
            gp.ui.addMessage("Found Liam!");
            gp.ui.addMessage("You can now unlock Miles' door!");
        }
    }

    public void completeFoundMiles() {
        if(!foundMiles) {
            foundMiles = true;
            gp.ui.addMessage("Found Miles!");
            // Check if all overworld friends are found
            if(talkedToVic && foundMike && foundIan && foundLiam && foundMiles) {
                completeFoundAllFriends();
            }
        }
    }

    public void completeFoundAllFriends() {
        if(!foundAllFriends) {
            foundAllFriends = true;
            gp.ui.addMessage("Quest Complete: Found all your friends!");
            gp.ui.addMessage("New Quest: Wait, Where's John?");
        }
    }

    public void completeFoundJohn() {
        if(!foundJohn) {
            foundJohn = true;
            gp.ui.addMessage("Quest Complete: Found John!");
            gp.ui.addMessage("New Quest: Kill Nick");
        }
    }

    public void completeDefeatedNick() {
        if(!defeatedNick) {
            defeatedNick = true;
            gp.ui.addMessage("Quest Complete: Defeated Nick!");
            gp.ui.addMessage("New Quest: Kill Brian");
        }
    }

    public void completeDefeatedBrian() {
        if(!defeatedBrian) {
            defeatedBrian = true;
            gp.ui.addMessage("Quest Complete: Defeated Brian!");
            gp.ui.addMessage("You can now escape the island!");
            // Trigger victory
            gp.gameState = gp.victoryState;
        }
    }

    public boolean isComplete() {
        return defeatedBrian;
    }

    public boolean canUnlockMilesDoor() {
        return foundLiam;
    }
}