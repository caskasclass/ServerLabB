package Finder;

import java.util.ArrayList;

import jars.ChartData;
import jars.CommentSection;
import jars.EmotionEvaluation;

/**
 * Progetto laboratorio B: "Emotional Songs", anno 2022-2023
 * 
 * @author Beatrice Bastianello, matricola 751864, VA
 * @author Lorenzo Barbieri  , matricola 748695, VA
 * @author Filippo Storti , matricola 749195, VA
 * @author Nazar Viytyuk, matricola 748964, VA
 * @version 1.0
 */

public interface EmotionManagerInterface {

    EmotionEvaluation getMyEmotions(String userId);

    ArrayList<ChartData> getAllEmotions();

    void insertEmotions(EmotionEvaluation emotion);
    
    void setSearchCriteria(String searchCriteria);

    ArrayList<CommentSection> getAllComments();

    ArrayList<CommentSection> getMyComments(String userId);


    


    
}
