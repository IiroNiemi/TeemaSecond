package TeemaFirstAndSecond;

import static TeemaFirstAndSecond.MM.getRoundPenaltyIfThisMatchIsSetHere;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Iiro
 */
public class TeemaFAS {
    public static final int TEAMS = 15; 
    public static final int ROUNDS = 36;
    public static ArrayList[] roundStack = new ArrayList[ROUNDS];
    public static int[][] TeamPenalty = new int[TEAMS][ROUNDS];
    
    
    public static void main(String[] args) {
        InitializeList.StartList(); //Initializes roundStack List for all rounds in random rounds
        PenaltyC.countTeamPenalty(); //Counts penalty for each team
        
        System.out.println("kokonaisvirheet: " + PenaltyC.getOverallPenalty());
        System.out.println("Hakee...");
        
        //while(PenaltyC.getOverallPenalty() > 100){
            //InitializeList.StartList(); //arpoo uuden listan
            
            //Koitetaan parantaa ohjelmaa tuhat kertaa ennenkuin arvotaan uusi
            for (int i = 0; i < 1000; i++) {        
        

                MC.BeginMoveChain(5);
                
                
                //System.out.println("kokonaisvirheet: " + PenaltyC.getOverallPenalty());
                
            }
            
            
            if(PenaltyC.getOverallPenalty() < 200) System.out.println("kokonaisvirheet: " + PenaltyC.getOverallPenalty());
        //}
        
        
            
       PenaltyC.countTeamPenalty();         
        //PenaltyC.PrintTeamErrors();
       
       PenaltyC.PrintMatchList(); //tulostaa otteluohjelman virheineen
       System.out.println("stop!");

    }
}
