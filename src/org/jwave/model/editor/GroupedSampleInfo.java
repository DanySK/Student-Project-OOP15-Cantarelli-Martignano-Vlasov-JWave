package org.jwave.model.editor;

/**
 * This interface models the concept of the information pertaining to
 * a single group of sound samples. The grouped adjective refers to the fact
 * that this information contains values pertaining to a group of samples
 * and not a single sample. Therefore we do not have absolute values but
 * statistical values like the maximum positive value or the root mean square
 * (RMS) of all the negative values. The 4 values produced for each group of
 * samples for each channel (8 total values) are:
 * 
 * 		- maximum positive value
 * 		- minimum negative value
 * 		- root mean square of positive values
 * 		- root mean square of negative values
 *
 */
public interface GroupedSampleInfo {
    /**
     * Gets the maximum amplitude for this group in the left channel.
     *          
     * @return
     * 			the maximum amplitude (up to 1) for this group.
     * 
     */		
	float getLeftChannelMax();
	
    /**
     * Gets the minimum amplitude for this group in the left channel.
     *          
     * @return
     * 			the minimum amplitude (down to -1) for this group.
     * 
     */		
	float getLeftChannelMin();	
	
    /**
     * Gets the RMS of positive values for this group in the left channel.
     *          
     * @return
     * 			the RMS of the positive values (0 to 1) this group.
     * 
     */		
	float getLeftChannelPositiveRMS();	
	
    /**
     * Gets the RMS of negative values for this group in the left channel.
     *          
     * @return
     * 			the RMS of the positive values (-1 to 0) this group.
     * 
     */		
	float getLeftChannelNegativeRMS();	
	
    /**
     * Gets the maximum amplitude for this group in the right channel.
     *          
     * @return
     * 			the maximum amplitude (up to 1) for this group.
     * 
     */		
	float getRightChannelMax();
	
    /**
     * Gets the minimum amplitude for this group in the right channel.
     *          
     * @return
     * 			the minimum amplitude (down to -1) for this group.
     * 
     */		
	float getRightChannelMin();	
	
    /**
     * Gets the RMS of positive values for this group in the right channel.
     *          
     * @return
     * 			the RMS of the positive values (0 to 1) this group.
     * 
     */		
	float getRightChannelPositiveRMS();	
	
    /**
     * Gets the RMS of negative values for this group in the right channel.
     *          
     * @return
     * 			the RMS of the positive values (-1 to 0) this group.
     * 
     */		
	float getRightChannelNegativeRMS();
}
