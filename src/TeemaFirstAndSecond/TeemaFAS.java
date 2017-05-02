package TeemaFirstAndSecond;


import java.util.ArrayList;

/**
 * Random seeds(100) has been set for debuging in two classes; G and InitializeList, remember to clear those when going production mode!
 * @author Iiro
 */
public class TeemaFAS {
    public static final int TEAMS = 6; 
    public static final int ROUNDS = 12;
    public static ArrayList[] roundStack = new ArrayList[ROUNDS];
    public static int[][] TeamPenalty = new int[TEAMS][ROUNDS];
    
    
    public static void main(String[] args) {
        InitializeList.StartList(); //Initializes roundStack List for all rounds in random rounds
        PenaltyC.countTeamPenalty(); //Counts penalty for each team
        
        System.out.println("kokonaisvirheet: " + PenaltyC.getOverallPenalty());
        
        //while(PenaltyC.getOverallPenalty() > 0){
        //for (int k  = 0; k < 1; k++) {
            
            //InitializeList.StartList(); //arpoo uuden listan
            
            for (int i = 0; i < 100; i++) {        
                System.out.println("i: " + i);
                //MC.BeginMoveChain(6);
                Jumper.BeginMoveChain(8);
                
                if(PenaltyC.getOverallPenalty() == 0) break;
            }
            
            
            
            
        //}
        //System.out.println("kokonaisvirheet: " + PenaltyC.getOverallPenalty());
        
            
       PenaltyC.countTeamPenalty();         
       //PenaltyC.PrintTeamErrors();
       PenaltyC.PrintMatchList();

       
       //PenaltyC.PrintMatchList(); //tulostaa otteluohjelman virheineen
       System.out.println("stop!");

    }
}
