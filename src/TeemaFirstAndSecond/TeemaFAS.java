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
        

        System.out.println("Hakee...");
        //while(PenaltyC.getOverallPenalty() > 150){
            //InitializeList.StartList(); //arpoo uuden listan
            
            //Koitetaan parantaa ohjelmaa tuhat kertaa ennenkuin arvotaan uusi
            for (int i = 0; i < 1000; i++) {        
        
                PenaltyC.countTeamPenalty();
                /*ei kunnioita lukittuja pelejä, imaisee vierasestojen kierrokset tyhjäksi, tarvii hiomista vielä.*/
                MM.ArrangeHomeAndVisitLocks(); 
                MM.swapMatch();  
                
                
                System.out.println("kokonaisvirheet: " + PenaltyC.getOverallPenalty());
                
            }
            
            
            if(PenaltyC.getOverallPenalty() < 150) System.out.println("kokonaisvirheet: " + PenaltyC.getOverallPenalty());
        //}
        
            
       PenaltyC.countTeamPenalty();         
        //PenaltyC.PrintTeamErrors();
       //int[][] i = MM.oddTeams; //Parittomat joukkueet, koitan keksiä vielä käyttöä näille.
       
       PenaltyC.PrintMatchList(); //tulostaa otteluohjelman virheineen
       System.out.println("stop!");

    }
}
