package TeemaFirstAndSecond;

import static TeemaFirstAndSecond.MM.TabuL;
import static TeemaFirstAndSecond.MM.homeTeamLock;
import static TeemaFirstAndSecond.MM.visitTeamLock;
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
import java.util.Random;

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
    
    public static ArrayList getRoundMatchWhichCausesMostPenalty(int round){
        Random r = new Random();
        ArrayList retval = new ArrayList();
        ArrayList MatchesWithPenalty = new ArrayList();
        ArrayList RoundMatches = roundStack[round];
        matchCand mC = null;
        
        for (int i = 0; i < roundStack.length; i++) {
            ArrayList roundlist = roundStack[i]; 
            for (int j = 0; j < roundlist.size(); j++) {
                Match tempMO = (Match)roundlist.get(j);
                for (int k = 0; k < RoundMatches.size(); k++) {
                    Match ndl = (Match)RoundMatches.get(k);
                    if(i == round && tempMO.getHome() == ndl.getHome() && tempMO.getVisitor() == ndl.getVisitor()){
                        int firstP = TeamPenalty[tempMO.getHome()][round];
                        int secondP = TeamPenalty[tempMO.getVisitor()][round];
                        int mTotalP = firstP + secondP;
                        if(mTotalP > 0){
                            mC = new matchCand(ndl,mTotalP);
                            MatchesWithPenalty.add(mC);
                        }
                        
                    }
                }    
            }
            
        }
        
        //Ei palauteta jos ottelu on tabulistalla
        for (int i = 0; i < MatchesWithPenalty.size(); i++) {
            matchCand BmC = (matchCand)MatchesWithPenalty.get(i);
            Match MO = (Match)BmC.getMatch();
            for (int j = 0; j < TabuL.size(); j++) {
                if(TabuL.isInList(MO, MO.getRound())){
                    MatchesWithPenalty.remove(BmC);
                }
            }    
        }
        
        /*Palautetaan tyhjä jos kaikki kierroksen ottelut on tabulistalla*/
        if(MatchesWithPenalty.isEmpty()){
            boolean allroundMatchesAreTabu = true; 
            matchCand MC = null;
                    
                    
            for (int i = 0; i < RoundMatches.size(); i++) {
                Match MO = (Match)RoundMatches.get(i);
                if(!TabuL.isInList(MO, round)){
                    MC = new matchCand(MO, round);
                    allroundMatchesAreTabu = false;
                }
            }
            
            if(allroundMatchesAreTabu == false){ //palautetaan tyhjä lista jos kaikki on tabulla
                retval.add(MC);
            }
            

        } else{
            //Valikoi suurimman
            int biggest = 0;
            for (int i = 0; i < MatchesWithPenalty.size(); i++) {
                matchCand BmC = (matchCand)MatchesWithPenalty.get(i);
                if(BmC.mPenalty > biggest){
                    biggest = BmC.getmPenalty();
                }
            }
            //valitaan kaikki yhtäsuuret
            for (int i = 0; i < MatchesWithPenalty.size(); i++) {
                matchCand BmC = (matchCand)MatchesWithPenalty.get(i);
                if(BmC.mPenalty == biggest){
                    retval.add(BmC);
                }
            }
        }
        



        
        
        
        return retval;
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
        int tempSize = 226;
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
    
    public static int getRoundSize(int round){
        int roundSize = 0;
        for (int i = 0; i < roundStack.length; i++) {
            
            if(i == round){
                roundSize = roundStack[i].size();
            }
        }
        return roundSize;
    }
    
    public static int getAllMatchesCount(){
        int sum = 0;
        for (Object singleRound : roundStack) { 
            ArrayList RList = (ArrayList)singleRound;
            for (Object RList1 : RList) {
                Match MO = (Match) RList1;
                sum++;
            
            }
        }
        return sum;
    }
    

    
    public static ArrayList[] GetHomeAndVisitPrevents(){
        /*Tämä toteutus toimii virheenlaskentana, eli täällä ei muuteta mitään, palautetaan ainoastaan lista virheellisistä otteluista */
        List RoundMatches = new ArrayList();
        List ListOfHomeLocks = new ArrayList();
        List ListOfVisitLocks = new ArrayList();
        
        ArrayList[] retval = new ArrayList[2];
        retval[0] = new ArrayList();
        retval[1] = new ArrayList();
        
        //Olioistetaan koti ja vieras listoihin, homeTeamLock ja visitTeamLock ovat globaaleja jotka löytyvät MM.java:sta
        for (int[] homeTeamLock1 : homeTeamLock) {
            MM.homeLock HL = new MM.homeLock(homeTeamLock1[0], homeTeamLock1[1]);
            ListOfHomeLocks.add(HL);
        }
        for (int[] visitTeamLock1 : visitTeamLock) {
            MM.visitLock HL = new MM.visitLock(visitTeamLock1[0], visitTeamLock1[1]);
            ListOfVisitLocks.add(HL);
        }
        
        //hakee/tulostaa kaikki kotiestot
        for (int i = 0; i < roundStack.length; i++) {
            RoundMatches.addAll(roundStack[i]);
            for (int j = 0; j < RoundMatches.size(); j++) {
                Match MO = (Match) RoundMatches.get(j);
                for (int k = 0; k < ListOfHomeLocks.size(); k++) {
                    MM.homeLock HL = (MM.homeLock)ListOfHomeLocks.get(k);
                    if(MO.getHome() == HL.getTeam() && MO.getRound() == HL.getRound()){
                        //System.out.println("kotiesto: " + MO.getHome() + " Kierroksella: " + MO.getRound());
                        retval[0].add(MO);
                    }   
                }
            }
            RoundMatches.clear();
        }
        
        //Vierasestot
        for (int i = 0; i < roundStack.length; i++) {
            RoundMatches.addAll(roundStack[i]);
            for (int j = 0; j < RoundMatches.size(); j++) {
                Match MO = (Match) RoundMatches.get(j);
                for (int k = 0; k < ListOfVisitLocks.size(); k++) {
                    MM.visitLock VL = (MM.visitLock)ListOfVisitLocks.get(k);
                    if(MO.getVisitor()== VL.getTeam() && MO.getRound() == VL.getRound()){
                        //System.out.println("Vierasesto: " + MO.getVisitor() + " Kierroksella: " + MO.getRound());
                        retval[1].add(MO);
                    }
                }
            }
            RoundMatches.clear();
        }

        return retval;
    }
    //---- PRINTERS ---
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
                //System.out.println( MO.toString());
                if(MO.isLockedToRow()== true) lockCount++;
                sum++;
            
            }
        }
                
        System.out.println("Virheet: " + PenaltyC.getOverallPenalty() + " otteluja: " + sum + " Lukittuja: " + lockCount); 
    }
    
    public static void PrintHomePrevents(){ //Tulostaa kaikki kotiestot
        List RoundMatches = new ArrayList();
        List ListOfHomeLocks = new ArrayList();
        for (int[] homeTeamLock1 : homeTeamLock) {
            MM.homeLock HL = new MM.homeLock(homeTeamLock1[0], homeTeamLock1[1]);
            ListOfHomeLocks.add(HL);
        }
        
        for (int i = 0; i < roundStack.length; i++) {
            RoundMatches.addAll(roundStack[i]);
            for (int j = 0; j < RoundMatches.size(); j++) {
                Match MO = (Match) RoundMatches.get(j);
                for (int k = 0; k < ListOfHomeLocks.size(); k++) {
                    MM.homeLock HL = (MM.homeLock)ListOfHomeLocks.get(k);
                    if(MO.getHome() == HL.getTeam() && MO.getRound() == HL.getRound()){
                        System.out.println("kotiesto: " + MO.getHome() + " Kierroksella: " + MO.getRound());
                        
                    }   
                }
            }
            RoundMatches.clear();
        }
    }
}
    
        


    

