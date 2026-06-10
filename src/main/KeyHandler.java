package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    Gamepanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, shotKeyPressed;
    
    boolean showDebugText = false;
    public boolean escapePressed;
    
    public KeyHandler(Gamepanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_ESCAPE) {
            if (gp.gameState == gp.optionState) {
                gp.gameState = gp.playState; 
            } else if (gp.gameState == gp.playState) {
                gp.gameState = gp.optionState; 
            }
            return; 
        }

        if (code == KeyEvent.VK_C) {
            if (gp.gameState == gp.characterState) {
                gp.gameState = gp.playState; 
            } else if (gp.gameState == gp.playState) {
                gp.gameState = gp.characterState; 
            }
            return; 
        }

        if (gp.gameState == gp.titleState) {
            titleState(code);
        } else if (gp.gameState == gp.playState) {
            playState(code);
        } else if (gp.gameState == gp.pauseState) {
            pauseState(code);
        } else if (gp.gameState == gp.dialougeState) {
            dialogueState(code);
        } else if (gp.gameState == gp.characterState) {
            characterState(code);
        } else if (gp.gameState == gp.optionState) {
            optionState(code);
        } else if (gp.gameState == gp.gameOverState) {
            gameOverState(code);
        } else if (gp.gameState == gp.tradeState) {
            tradeState(code);
        } else if (gp.gameState == gp.victoryState) {
            victoryState(code);
        }
    }

    public void titleState(int code) {
        if (code == KeyEvent.VK_W) {
            gp.ui.commandNum--;
            if(gp.ui.commandNum < 0) {
                gp.ui.commandNum = 2;
            }
        }
        if (code == KeyEvent.VK_S) {
            downPressed = true;
            gp.ui.commandNum++;
            if(gp.ui.commandNum > 2) {
                gp.ui.commandNum = 0;
            }
        }
        
        if(code == KeyEvent.VK_ENTER) {
            if(gp.ui.commandNum == 0) {
                gp.gameState = gp.playState;
                gp.playMusic(0);
                enterPressed = false;
            }
            
            if (gp.ui.commandNum == 1) {
                // Load game functionality
            }
            
            if(gp.ui.commandNum == 2) {
                System.exit(0);
            }
        }
    }
    
    public void playState(int code) {
        if (code == KeyEvent.VK_W) {
            upPressed = true;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = true;
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = true;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = true;
        }
        if(code == KeyEvent.VK_P) {
            gp.gameState = gp.pauseState;
        }
        if(code == KeyEvent.VK_C) {
            gp.gameState = gp.characterState;
        }
        if (code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
        if (code == KeyEvent.VK_E) {
            shotKeyPressed = true;
        }
        if (code == KeyEvent.VK_ESCAPE) {
            gp.gameState = gp.optionState;
        }
        
        if(code == KeyEvent.VK_T) {
            if(showDebugText == false) {
                showDebugText = true;
            } else if (showDebugText == true) {
                showDebugText = false;
            }
        }
        
        if (code == KeyEvent.VK_R) {
            switch(gp.currentMap) {
                case 0: gp.tileM.loadMap("/maps/world.txt", 0); break;
                case 1: gp.tileM.loadMap("/maps/dundgonmap.txt", 1); break;
            }
        }
    }
    
    public void pauseState(int code) {
        if(code == KeyEvent.VK_P) {
            gp.gameState = gp.playState;
        }
    }
    
    public void dialogueState(int code) {
        if (code == KeyEvent.VK_ENTER) {
            gp.gameState = gp.playState;
        }
    }
    
    public void characterState(int code) {
        if(code == KeyEvent.VK_ESCAPE) {
            gp.gameState = gp.playState;
        }
        
        if(code == KeyEvent.VK_ENTER) {
            gp.player.selectItem();
        }
        playerInventory(code);
    }
    
    public void optionState(int code) {
        if(code == KeyEvent.VK_ESCAPE) {
            gp.gameState = gp.playState;
        }
        
        if(code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
        
        int maxCommandNum = 0;
        switch(gp.ui.subState) {
            case 0: maxCommandNum = 5; break;
            case 3: maxCommandNum = 1; break;
        }
        
        if(code == KeyEvent.VK_W) {
            gp.ui.commandNum--;
            gp.playSE(10);
            if(gp.ui.commandNum < 0) {
                gp.ui.commandNum = maxCommandNum;
            }
        }
        
        if(code == KeyEvent.VK_S) {
            gp.ui.commandNum++;
            gp.playSE(10);
            if(gp.ui.commandNum > maxCommandNum) {
                gp.ui.commandNum = 0;
            }
        }
        
        if(code == KeyEvent.VK_A) {
            if(gp.ui.subState == 0) {
                if (gp.ui.commandNum == 1 && gp.music.volumeScale > 0) {
                    gp.music.volumeScale--;
                    gp.music.checkVolume();
                    gp.playSE(10);
                }
                if (gp.ui.commandNum == 2 && gp.se.volumeScale > 0) {
                    gp.se.volumeScale--;                
                    gp.playSE(10);
                }
            }
        }
        
        if(code == KeyEvent.VK_D) {
            if(gp.ui.subState == 0) {
                if (gp.ui.commandNum == 1 && gp.music.volumeScale < 5) {
                    gp.music.volumeScale++;
                    gp.music.checkVolume();
                    gp.playSE(10);
                }
                if (gp.ui.commandNum == 2 && gp.se.volumeScale < 5) {
                    gp.se.volumeScale++;                
                    gp.playSE(10);
                }
            }
        }
    }
    
    public void gameOverState(int code) {
        if(code == KeyEvent.VK_W) {
            gp.ui.commandNum--;
            if(gp.ui.commandNum < 0) {
                gp.ui.commandNum = 1;
            }
            gp.playSE(10);
        }
        if(code == KeyEvent.VK_S) {
            gp.ui.commandNum++;
            if(gp.ui.commandNum > 1) {
                gp.ui.commandNum = 0;
            }
            gp.playSE(10);
        }
        if(code == KeyEvent.VK_ENTER) {
            if(gp.ui.commandNum == 0) {
                gp.gameState = gp.playState;
                gp.retry();
            } else if (gp.ui.commandNum == 1) {
                gp.gameState = gp.titleState;
                gp.restart();
            }
        }
    }
    
    public void victoryState(int code) {
        if(code == KeyEvent.VK_ENTER) {
            gp.gameState = gp.titleState;
            gp.restart();
        }
    }
    
    public void tradeState(int code) {
        if (code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }

        if (gp.ui.subState == 0) {
            if (code == KeyEvent.VK_W) {
                gp.ui.commandNum--;
                if (gp.ui.commandNum < 0) {
                    gp.ui.commandNum = 2;
                }
                gp.playSE(10);
            }
            if (code == KeyEvent.VK_S) {
                gp.ui.commandNum++;
                if (gp.ui.commandNum > 2) {
                    gp.ui.commandNum = 0;
                }
                gp.playSE(10);
            }
        }
        if (gp.ui.subState == 1) {
            npcInventory(code);
            if (code == KeyEvent.VK_Q) {
                gp.ui.subState = 0;
            }
        }
        if (gp.ui.subState == 2) {
            playerInventory(code);
            if (code == KeyEvent.VK_Q) {
                gp.ui.subState = 0;
            }
        }
    }

    public void playerInventory(int code) {
        if(code == KeyEvent.VK_W) {    
            if(gp.ui.playerSlotRow != 0) {
                gp.ui.playerSlotRow--;
                gp.playSE(10);
            }
        }
        if(code == KeyEvent.VK_A) {    
            if(gp.ui.playerSlotCol != 0) {
                gp.ui.playerSlotCol--;
                gp.playSE(10);
            }
        }
        if(code == KeyEvent.VK_S) {    
            if(gp.ui.playerSlotRow != 3) {
                gp.ui.playerSlotRow++;
                gp.playSE(10);
            }
        }
        if(code == KeyEvent.VK_D) {    
            if(gp.ui.playerSlotCol != 4) {
                gp.ui.playerSlotCol++;
                gp.playSE(10);
            }
        }
    }
    
    public void npcInventory(int code) {
        if(code == KeyEvent.VK_W) {    
            if(gp.ui.npcSlotRow != 0) {
                gp.ui.npcSlotRow--;
                gp.playSE(10);
            }
        }
        if(code == KeyEvent.VK_A) {    
            if(gp.ui.npcSlotCol != 0) {
                gp.ui.npcSlotCol--;
                gp.playSE(10);
            }
        }
        if(code == KeyEvent.VK_S) {    
            if(gp.ui.npcSlotRow != 3) {
                gp.ui.npcSlotRow++;
                gp.playSE(10);
            }
        }
        if(code == KeyEvent.VK_D) {    
            if(gp.ui.npcSlotCol != 4) {
                gp.ui.npcSlotCol++;
                gp.playSE(10);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();    
        if(code == KeyEvent.VK_W) {
            upPressed = false;
        }
        
        if (code == KeyEvent.VK_E) {
            shotKeyPressed = false;
        }
        
        if(code == KeyEvent.VK_S) {
            downPressed = false;
        }

        if(code == KeyEvent.VK_A) {
            leftPressed = false;
        }

        if(code == KeyEvent.VK_D) {
            rightPressed = false;
        }

        if (code == KeyEvent.VK_ENTER) {
            enterPressed = false;
        }
    }
}