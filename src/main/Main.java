package main;

import java.awt.Graphics;

import javax.swing.JFrame;

public class Main {
	
	public static JFrame window;

	public static void main(String[] args) {
		// Hardware-accelerated OpenGL pipeline for 2D rendering - big FPS
		// boost on older machines. If graphics ever glitch, delete this line.
		System.setProperty("sun.java2d.opengl", "true");
		
		window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setTitle("Box Boy!");
		//window.setUndecorated(true);
		
		Gamepanel gamePanel = new Gamepanel();
		window.add(gamePanel);
		
		gamePanel.config.loadConfig();
		if(gamePanel.fullScreenOn == true) {
			window.setUndecorated(true);
		}
		
		window.pack();
		
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		gamePanel.setupGame();
		gamePanel.startGameThread();
		
	

	}

}
