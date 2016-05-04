package org.jwave.model.editor;

import java.util.List;

public class EditorImpl implements Editor {
	@Override
	public void loadSongToEdit(String songPath) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isSongLoaded() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getSongLength() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setSelectionFrom(int ms) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSelectionTo(int ms) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getSelectionFrom() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSelectionTo() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void deselectSelection() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isCursorSet() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSomethingSelected() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean copySelection() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getCopiedFrom() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getCopiedTo() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void resetCopiedSelection() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isSomethingCopied() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pasteCopiedSelection() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean cutSelection() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void exportSong(String exportPath) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exportSongMP3(String sourcePath, String destinationPath) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void printAllCuts() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Integer> getWaveform(int from, int to) {
		// TODO Auto-generated method stub
		return null;
	}
}
