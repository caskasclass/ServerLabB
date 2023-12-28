package Finder;

import java.util.ArrayList;
import jars.Emotion;

/**
 *
 * @author lorenzo
 */
public interface EmotionManagerInterface {

    ArrayList<Emotion> getEmotions();

    void insertEmotion(Emotion emotion);
    
    void setSearchCriteria(String searchCriteria);
    
}
