package org.jwave.controller.editor;

import java.io.File;
import java.util.Scanner;

import org.jwave.controller.player.ClockAgent;
import org.jwave.controller.player.Controller;
import org.jwave.model.editor.DynamicEditorPlayerImpl;
import org.jwave.model.editor.ModifiableSongImpl;
import org.jwave.model.player.DynamicPlayer;
import org.jwave.model.player.DynamicPlayerImpl;
import org.jwave.model.player.PlaylistImpl;
import org.jwave.model.player.PlaylistManagerImpl;
import org.jwave.model.player.SongImpl;

public class EditorDebug {
	public static void main(String[] args) {
		Editor songEditor = new EditorImpl();
		int menuSelection;
		Scanner reader = new Scanner(System.in);
		
		Controller ctrl = new Controller();		
//		DynamicPlayer player = new DynamicEditorPlayerImpl(new DynamicPlayerImpl());
//		ctrl.setDynamicPlayer(player);
		DynamicPlayer player = ctrl.getDynamicPlayer();
		
		try {
			songEditor.loadSongToEdit(new SongImpl(new File("/Users/alexvlasov/Downloads/hello.mp3")));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		songEditor.setSelectionFrom(50000);
//		songEditor.setSelectionTo(100000);
//		songEditor.copySelection();
//		songEditor.deselectSelection();
//		songEditor.setSelectionFrom(300000);
//		songEditor.pasteCopiedSelection();
		
//		songEditor.setSelectionTo(35000);
		
		do {
			System.out.println("1. Load song");
			System.out.println("2. Print all cuts");
			System.out.println("3. Remove selection");
			System.out.println("4. Set selection from");
			System.out.println("5. Set selection to");
			System.out.println("6. Copy selection");
			System.out.println("7. Paste selection");
			System.out.println("8. Export song");
			System.out.println("9. Cut selection");			
			System.out.println("10. Export song MP3");
			System.out.println("11. Waveform debug");
			System.out.println("12. Play song");
			System.out.println("0. Exit");				
						
			System.out.println("Select option: ");
			menuSelection = reader.nextInt();
			
			switch (menuSelection) {
				case 1:
					try {
						songEditor.loadSongToEdit(new SongImpl(new File("/Users/alexvlasov/Downloads/hello.mp3")));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case 2:
					songEditor.printSongDebug();
					break;
				case 3:
					songEditor.deselectSelection();
					break;
				case 4:
					System.out.println("Selection from (ms): ");
					songEditor.setSelectionFrom(reader.nextInt());
					break;
				case 5:
					System.out.println("Selection to (ms): ");
					songEditor.setSelectionTo(reader.nextInt());
					break;
				case 6:
					songEditor.copySelection();
					break;
				case 7:
					songEditor.pasteCopiedSelection();
					break;
				case 8:
					songEditor.exportSong("/Users/alexvlasov/Downloads/hello.wav");
					break;
				case 9:
					songEditor.cutSelection();
					break;
				case 10:
//					songEditor.exportSongMP3("/Users/alexvlasov/Downloads/hello.wav", "/Users/alexvlasov/Downloads/ciao.mp3");
					break;
				case 11:
					songEditor.printWaveform();
					break;
				case 12:
					player.setPlayer(songEditor.getSong());
					player.play();
					break;
				default:
					break;
			}			
		} while (menuSelection != 0);
		
		reader.close();
	}
}

