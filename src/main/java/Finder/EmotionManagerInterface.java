package Finder;

import java.util.ArrayList;

import jars.ChartData;
import jars.EmotionEvaluation;

/**
 *
 * @author lorenzo
 */
public interface EmotionManagerInterface {

    EmotionEvaluation getMyEmotions(String userId);

    ArrayList<ChartData> getAllEmotions();

    void insertEmotions(EmotionEvaluation emotion);
    
    void setSearchCriteria(String searchCriteria);
    
}
