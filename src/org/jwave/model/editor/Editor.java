package org.jwave.model.editor;

import java.util.List;

public interface Editor {
	// Carica una canzone (dal percorso passato) e la prepara per essere modificata
	void loadSongToEdit(String songPath);
	
	// Restituisce vero se una canzone e' stata caricata, falso altrimenti
	boolean isSongLoaded();
	
	// Restituisce la lunghezza della canzone in ms, -1 se non e' stata caricata nessuna canzone
	int getSongLength();
	
	// Posizione il cursore principale (in ms)
	void setSelectionFrom(int ms);
	
	// Posiziona il cursore secondario (in ms)
	void setSelectionTo(int ms);	
	
	// Restituisce la posizione del cursore principale (in ms)
	int getSelectionFrom();
	
	// Restituisce la posizione del cursore secondario (in ms)
	int getSelectionTo();	
	
	// Toglie la selezione attuale (entrambi i cursori)
	void deselectSelection();
	
	// Restituisce vero se il cursore principale e' posizionato
	boolean isCursorSet();
	
	// Restituisce vero se entrambi i cursori (principale e secondario) sono posizionati
	boolean isSomethingSelected();
	
	// Copia la selezione, se questa c'e' (isSomethingSelected())
	boolean copySelection();
	
	// Restituisce da quale ms parte il pezzo copiato (-1 se non e' stato copiato niente)
	int getCopiedFrom();
	
	// Restituisce fino a che ms arriva il pezzo copiato (-1 se non e' stato copiato niente)
	int getCopiedTo();
	
	// Dimentica il pezzo copiato
	void resetCopiedSelection();
	
	// Restituisce vero se e' stata copiata una selezione
	boolean isSomethingCopied();
	
	// Incolla il pezzo copiato nella posizione del cursore principale
	// shiftando in giu' il resto della canzone. Tutto questo possibile se e solo se
	// ci sta una selezione copiata e il cursore principale risulta posizionato
	boolean pasteCopiedSelection();
	
	// Taglia la selezione, se c'e' (togliendola), altrimenti restituisce falso
	boolean cutSelection(); 
	
	// Esporta la canzone con tutte le eventuali modifiche nel .wav specificato con exportPath
	void exportSong(String exportPath);
	
	// Esporta la canzone in .wav in sourcePath in una canzone in .mp3 in destinationPath
	public void exportSongMP3(String sourcePath, String destinationPath);
	
	// debug method to see what's going on in console
	void printAllCuts();
	
	// Returns the waveform from ms to ms, a constant maximum amount of values will be returned,
	// the minimum depending on the level of detail requested (number of samples), all the value
	// will be normalized within a 0 to maximumValue range
	List<Float> getWaveform(int from, int to, int samples, int maxValue);
}
