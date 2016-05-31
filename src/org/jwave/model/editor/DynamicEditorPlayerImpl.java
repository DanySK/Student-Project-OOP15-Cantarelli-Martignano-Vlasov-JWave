package org.jwave.model.editor;

import org.jwave.model.player.DynamicPlayer;

public class DynamicEditorPlayerImpl extends DynamicPlayerDecorator {
	public DynamicEditorPlayerImpl(final DynamicPlayer player) {
		super(player);
	}
}