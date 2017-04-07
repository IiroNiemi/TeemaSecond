package TeemaFirstAndSecond;


import java.util.ArrayList;

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
        
        //while(PenaltyC.getOverallPenalty() > 0){
        //for (int k  = 0; k < 1; k++) {
            
            //InitializeList.StartList(); //arpoo uuden listan
            
            for (int i = 0; i < 1000; i++) {        
                
                MC.BeginMoveChain(6);
                
                if(PenaltyC.getOverallPenalty() == 0) break;
            }
            
            
            
            
        //}
        System.out.println("kokonaisvirheet: " + PenaltyC.getOverallPenalty());
        
            
       PenaltyC.countTeamPenalty();         
       //PenaltyC.PrintTeamErrors();
       PenaltyC.PrintMatchList();

       
       //PenaltyC.PrintMatchList(); //tulostaa otteluohjelman virheineen
       System.out.println("stop!");

    }
}
