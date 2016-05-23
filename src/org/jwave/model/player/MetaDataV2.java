package org.jwave.model.player;

public interface MetaDataV2 {
    
    /**
     * 
     * @param metaDataValue
     *          the type of meta data you want to retrieve.
     * 
     * @return
     *          a String specifing the meta data value.
     */
    String retrieve(MetaData metaDataValue);
    
    /**
     * Sets a value for a meta data.
     * 
     * @param metaDataValue
     *          the new MetaDataValue
     *          
     * @param newValue
     *          the new value to be set.         
     */
    void setData(MetaData metaDataValue, String newValue);
    
}
