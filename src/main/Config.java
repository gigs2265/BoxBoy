package main;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Config {
	
	Gamepanel gp;
	
	public Config(Gamepanel gp) {
		this.gp = gp;
	}
	
	public void saveConfig() {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("config.txt"));
			
			// FULLSCREEN
			if(gp.fullScreenOn == true) {
				bw.write("on");
			} else {
				bw.write("off");
			}
			bw.newLine();
			
			// MUSIC VOL
			bw.write(String.valueOf(gp.music.volumeScale));
			bw.newLine();
			
			// SE VOL
			bw.write(String.valueOf(gp.se.volumeScale));
			bw.newLine();
			
			bw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadConfig() {
		try {
			BufferedReader br = new BufferedReader(new FileReader("config.txt"));
			String s = br.readLine();
			
			// FULLSCREEN - Add null check
			if(s != null && s.equals("on")) {
				gp.fullScreenOn = true;
			}
			if(s != null && s.equals("off")) {
				gp.fullScreenOn = false;
			}
			
			// MUSIC VOL
			s = br.readLine();
			if(s != null) {
				gp.music.volumeScale = Integer.parseInt(s);
			}
			
			// SE VOL
			s = br.readLine();
			if(s != null) {
				gp.se.volumeScale = Integer.parseInt(s);
			}
			
			br.close();
					 
		} catch (Exception e) {
			// If config file doesn't exist or is corrupted, use defaults
			System.out.println("Config file not found or corrupted. Using default settings.");
		}
	}
}