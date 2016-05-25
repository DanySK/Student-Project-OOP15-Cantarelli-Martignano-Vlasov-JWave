package org.jwave.model.editor;

/**
 * This interface models the concept of the information pertaining to
 * a single, simple, sound sample. The simple adjective refers to the fact
 * that this information contains a single value for the left channel and
 * a single value for the right channel. Both values refer to the amplitude
 * of the soundwave in the sample, for their respective channels.
 *
 */
public interface SimpleSampleInfo {
    /**
     * Gets the amplitude for this sample in the left channel.
     *          
     * @return
     * 			the amplitude (normalized -1 to 1) for this sample
     * 			in the left channel.
     */		
	int getLeftChannel();
	
    /**
     * Gets the amplitude for this sample in the right channel.
     *          
     * @return
     * 			the amplitude (normalized -1 to 1) for this sample
     * 			in the right channel.
     */			
	int getRightChannel();
}
