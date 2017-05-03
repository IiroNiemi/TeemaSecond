package TeemaFirstAndSecond;


import java.util.ArrayList;

/**
 * Random seeds(100) has been set for debuging in two classes; G and InitializeList, remember to clear those when going production mode!
 * @author Iiro
 */
public class TeemaFAS {
    public static final int TEAMS = 9; 
    public static final int ROUNDS = 18;
    public static ArrayList[] roundStack = new ArrayList[ROUNDS];
    public static int[][] TeamPenalty = new int[TEAMS][ROUNDS];
    
    
    public static void main(String[] args) {
        InitializeList.StartList(); //Initializes roundStack List for all rounds in random rounds
        PenaltyC.countTeamPenalty(); //Counts penalty for each team
        ArrayList KA = new ArrayList(); //Counts average
        
        System.out.println("Alkuvirheet: " + PenaltyC.getOverallPenalty());

        int i = 0;
        int loops = 1000000;
        
        //while(PenaltyC.getOverallPenalty() > 0){
            
            //InitializeList.StartList(); //arpoo uuden listan
        
            for (i = 0; i < loops; i++) {        
                //System.out.println("i: " + i);
                
                Jumper.BeginMoveChain(10);

                PenaltyC.countTeamPenalty();
                if(PenaltyC.getOverallPenalty() < 10) System.out.println("Virheitä: " + PenaltyC.getOverallPenalty());
                KA.add(PenaltyC.getOverallPenalty());
                if(PenaltyC.getOverallPenalty() == 0) break;
            }
            
            
        //}
        
        
       
       
       PenaltyC.countTeamPenalty();         
       PenaltyC.PrintMatchList();
       G.PrintAveragePenalty(loops, KA);
       if(i < loops)System.out.println("Vastaus löytyi kierroksella: " + i);
       

    }
}
