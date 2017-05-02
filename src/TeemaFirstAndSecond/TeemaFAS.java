package TeemaFirstAndSecond;


import java.util.ArrayList;

/**
 * Random seeds(100) has been set for debuging in two classes; G and InitializeList, remember to clear those when going production mode!
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
        ArrayList KA = new ArrayList(); //Counts average
        
        System.out.println("Alkuvirheet: " + PenaltyC.getOverallPenalty());

        
        //while(PenaltyC.getOverallPenalty() > 0){
            
            //InitializeList.StartList(); //arpoo uuden listan
        
            int loops = 2000;
            for (int i = 0; i < loops; i++) {        
                //System.out.println("i: " + i);
                Jumper.BeginMoveChain(7);
                
                System.out.println("kokonaisvirheet: " + PenaltyC.getOverallPenalty());
                System.out.println("");
                KA.add(PenaltyC.getOverallPenalty());
                if(PenaltyC.getOverallPenalty() == 0) break;
            }
            
            
            
            
        //}
        //System.out.println("kokonaisvirheet: " + PenaltyC.getOverallPenalty());
        
       
       
       PenaltyC.countTeamPenalty();         
       //PenaltyC.PrintTeamErrors();
       PenaltyC.PrintMatchList();
       G.PrintAveragePenalty(loops, KA);
       
       //PenaltyC.PrintMatchList(); //tulostaa otteluohjelman virheineen

    }
}
