package TeemaFirstAndSecond;

import static TeemaFirstAndSecond.TeemaFAS.ROUNDS;
import static TeemaFirstAndSecond.TeemaFAS.TEAMS;
import static TeemaFirstAndSecond.TeemaFAS.TeamPenalty;
import static TeemaFirstAndSecond.TeemaFAS.roundStack;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Iiro
 */
public class PenaltyC {
    
    public static void countTeamPenalty(){
        List RoundMatches = new ArrayList();
        List RoundTeams = new ArrayList();
        int teamP = 0;
        
        TeamPenalty = new int[TEAMS][ROUNDS]; //Virheet nollataan ettei pinoudu.
        
        for (int i = 0; i < roundStack.length; i++) {
            RoundMatches.addAll(roundStack[i]);
            boolean oddTeamNoPenalty = true;
            
            for (Object RoundM : RoundMatches) {
                Match MO = (Match) RoundM;
                RoundTeams.add(MO.getHome());
                RoundTeams.add(MO.getVisitor());
            }
            
            for (int j = 0; j < TEAMS; j++) {
                teamP = 0;
                if(Collections.frequency(RoundTeams, j) == 1){  
                    //<--Löytyi kierrokselta kerran, kaikki ok!
                } else {
                    //kerran kierroksessa ei saa antaa virhettä joukkueen poissaolosta
                    if(oddTeamNoPenalty == true){
                        if(Collections.frequency(RoundTeams, j) == 0){ //Sallitaan yksi virhe(poissaolo) kierrokselle
                            oddTeamNoPenalty = false;
                        }
                        else {
                            //miinus yksi ettei laske ensimmäistä esiintymistä virheeksi
                            teamP += Collections.frequency(RoundTeams, j) - 1; 
                        }
                    } else {
                        if(Collections.frequency(RoundTeams, j) == 0){ 
                            teamP += 1;//Yksi virhe sallittu, joten jatkossa rangaistaan poissaoloista
                        } else {
                            //miinus yksi ettei laske ensimmäistä esiintymistä virheeksi
                            teamP += Collections.frequency(RoundTeams, j) - 1; 
                        }    
                    }
                    TeamPenalty[j][i] = teamP;
                }
            }
            
            RoundTeams.clear();
            RoundMatches.clear();
        }   
    }
    
    public static int getTeamTotalPenalty(int team){
        int sum = 0;
        
        for (int j = 0; j < TeamPenalty[team].length; j++) {
            sum += TeamPenalty[team][j];
        }

        return sum;
    }
    
    public static int getOverallPenalty(){
        int sum = 0;
        for (int i = 0; i < TeamPenalty.length; i++) {
            for (int j = 0; j < TeamPenalty[i].length; j++) {
                sum += TeamPenalty[i][j];
            }
            
        }
        return sum;
    }
    
    public static int getAllTeamsRoundPenalty(int round){
        int sum = 0;

        for (int i = 0; i < TEAMS; i++) {
            for (int j = 0; j < ROUNDS; j++) {
                if(j == round){
                    sum += TeamPenalty[i][j];
                }
            }
        }
        return sum;
    }
    
    public static int getMostMatchesRound(){
        int tempSize = 0;
        int winner = 0;
        
        for (int i = 0; i < roundStack.length; i++) {
            
            if(roundStack[i].size() > tempSize){
                tempSize = roundStack[i].size();
                winner = i;
            }
            
        }
        
        return winner;
    }
    
    public static int getLeastMatchesRound(){
        int tempSize = 91;
        int loser = 0;
        
        for (int i = 0; i < roundStack.length; i++) {
            
            if(roundStack[i].size() <= tempSize){
                tempSize = roundStack[i].size();
                loser = i;
            }
        }
        return loser;
    }

    static int getRoundPenalty(int round) {
        int sum = 0;
        
        for (int i = 0; i < TEAMS; i++) {
            for (int j = 0; j < ROUNDS; j++) {
                if(j == round){
                    sum += TeamPenalty[i][j];    
                }
            }
        }

        return sum;
    }
    
//---- HELPERS ---
    public static void PrintTeamErrors(){
        System.out.println("TeamPenalty:"); //Listataan joukkueiden virheet
        int team = 0;
        int round = 0;
        for (int[] TeamRound : TeamPenalty) {
            System.out.println("Team: "+team);
            for (int U : TeamRound) {
                System.out.print(round + "# ");
                System.out.println(U);
                round++;
            }
            System.out.println();
            team++;
            round = 0;
        }
    }
    public static void PrintMatchList(){ //Ohjelman tulostus
        int sum = 0;
        int lockCount = 0;
        System.out.println("# " + "K - V" + " PVM " );
        for (Object singleRound : roundStack) { 
            ArrayList RList = (ArrayList)singleRound;
            for (Object RList1 : RList) {
                Match MO = (Match) RList1;
                System.out.println( MO.toString());
                if(MO.isLockedToRow()== true) lockCount++;
                sum++;
            
            }
        }
                
        System.out.println("Virheet: " + PenaltyC.getOverallPenalty() + " otteluja: " + sum + " Lukittuja: " + lockCount); 
    }
    public static void PrintRoundStackMatches(){
        //Tulostaa kaikki joukkueet, 540 kappaletta 15 ottelijalla
        int sum = 0;
        for (Object singleRound : roundStack) { 
            ArrayList RList = (ArrayList)singleRound;
            for (Object SingleMatch : RList) {
                    sum++;
            }
        }
        System.out.println(sum);
    }
}
    
        


    

