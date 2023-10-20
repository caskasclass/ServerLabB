package Finder;

import java.util.ArrayList;
import pkg.Emotion;

/**
 *
 * @author lorenzo
 */
public interface EmotionManagerInterface {

    ArrayList<Emotion> getEmotions();

    void insertEmotion(Emotion emotion);
    
    void setSearchCriteria(String searchCriteria);
    
}
