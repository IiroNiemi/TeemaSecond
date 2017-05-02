/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TeemaFirstAndSecond;

import static TeemaFirstAndSecond.InitializeList.AllGameDates;
import static TeemaFirstAndSecond.Jumper.TabuL;
import static TeemaFirstAndSecond.TeemaFAS.ROUNDS;
import static TeemaFirstAndSecond.TeemaFAS.TEAMS;
import static TeemaFirstAndSecond.TeemaFAS.TeamPenalty;
import static TeemaFirstAndSecond.TeemaFAS.roundStack;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Iiro
 */

public class G {
    
    public static Random r = new Random(100);
    
    public static Match getRandomMatch(){
        int randomRound = r.nextInt(ROUNDS);
        while(roundStack[randomRound].size() <= 0){
            randomRound = r.nextInt(ROUNDS);
        }
        int roundLength = roundStack[randomRound].size();
        Match RM = (Match)roundStack[randomRound].get(r.nextInt(roundLength));
        
        while(TabuL.isInList(RM, RM.getRound())){
            randomRound = r.nextInt(ROUNDS);
            while(roundStack[randomRound].size() <= 0){
                randomRound = r.nextInt(ROUNDS);
            }
            roundLength = roundStack[randomRound].size();
            RM = (Match)roundStack[randomRound].get(r.nextInt(roundLength));
        }
        
        
        return RM;
    }
    
    public static Match getRandomMatch(int round){
        ArrayList<Match> roundmatches = roundStack[round];
        ArrayList<Match> retval = new ArrayList();
        
        for (int i = 0; i < roundmatches.size(); i++) {
            Match MO = roundmatches.get(i);
            Match nMO = new Match(MO.getHome(),MO.getVisitor(),MO.getGameDate(),MO.getRound());
            retval.add(nMO);
        }
        
        int roundS = roundmatches.size();
        for (int i = 0; i < roundS; i++) {
            Match MO = roundmatches.get(i);
            if(TabuL.isInList(MO, MO.getRound())){
                retval.remove(MO);
            }
        }
        
        Match RM = null;
        roundS = retval.size();
        if(roundS > 0){
            RM = retval.get(r.nextInt(roundS));
        }
        return RM;

    }
    
    public static roundCand getRoundCandidate(Match Begin){
        ArrayList retval = new ArrayList();
        roundCand RC = new roundCand(Begin.getRound(),0,Begin.getGameDate());
        
        //Etsii pienimmän virhemääräisen kierroksen.
        int minValue = 1000;
        for (int i = 0; i < ROUNDS; i++) {
            int temp = getRoundPenaltyIfThisMatchIsSetHere(i,Begin);
            if (temp < minValue) {
                minValue = temp;
                RC = new roundCand(i, minValue,AllGameDates[i]);
            }
        }
        
        //Kerää kaikki tällä virhemäärällä olevat kierrokset.
        for (int i = 0; i < ROUNDS; i++) { 
            int newPenalty = getRoundPenaltyIfThisMatchIsSetHere(i,Begin); //otetaan ylös virhetilanne jokaiselta kierrokselta
            
            if(newPenalty <= minValue){
                minValue = newPenalty;
                RC = new roundCand(i, newPenalty,AllGameDates[i]);
                retval.add(RC);
            }
        }
        
        if(retval.isEmpty()){
            retval.add(RC);
        }
        
        //Poistaa kierroksen mistä lähdettiin
        for (int i = 0; i < retval.size(); i++) {
            roundCand FC = (roundCand)retval.get(i);
            if(Begin.getRound() == FC.getRoundcand()){
                retval.remove(FC);
            }
            
        }
        
        int numofcands = retval.size();
        roundCand RCc = null;

        if(numofcands == 1){
            RCc = (roundCand)retval.get(numofcands-1); //eka
        } else if (numofcands > 1){
            RCc = (roundCand)retval.get(r.nextInt(numofcands)); //arvotaan mikä otetaan, mutta ei kuitenkaan samaa kierrosta mistä lähdettiin.
            while(RCc.getRoundcand() == Begin.getRound()){
                RCc = (roundCand)retval.get(r.nextInt(numofcands));
            }
        } else { //Ei palauteta nullia jos ei löydy ehdokasta, palautetaan sama mistä lähdettiin.
            RCc = new roundCand(Begin.getRound(), 0,Begin.getGameDate());
            return RCc;
        }
        
        
        
       
        return RCc;
    }
    
    public static int getRoundPenaltyIfThisMatchIsSetHere(int round, Match findplace){
        List RoundMatches = new ArrayList();
        List RoundTeams = new ArrayList();
        RoundMatches.addAll(roundStack[round]);
        int newErrors = 0;
        boolean oddTeamNoPenalty = true;
        
        if(RoundMatches.isEmpty()){ //Kierros on tyhjä niin palautetaan nolla heti!
            return newErrors;
        } else {
            /*Puretaan kierroksesta kaikki joukkueet listaan */
            for (Object RoundM : RoundMatches) {
                Match MO = (Match) RoundM;
                RoundTeams.add(MO.getHome());
                RoundTeams.add(MO.getVisitor());
            }

            /* Lisätään kierrokselle sovitettavan pelin joukkeet */
            RoundTeams.add(findplace.getHome());
            RoundTeams.add(findplace.getVisitor());


            /*Lasketaan virhetilanne sovituksen jälkeen */
            for (int j = 0; j < TEAMS; j++) { //Käydään kaikki joukkueet läpi

                if(Collections.frequency(RoundTeams, j) == 1){ 
                    //Löytyi kerran, kaikki ok!
                } else {
                    //allowpenalty sallii yhden joukkueelle yhden poissaolon per kierros!                              
                    if(oddTeamNoPenalty == true){
                        if(Collections.frequency(RoundTeams, j) == 0){  
                            oddTeamNoPenalty = false; //Yksi poissaolo armahdettu
                        } else {
                            newErrors += Collections.frequency(RoundTeams, j) - 1;
                        }
                    } else {
                        if(Collections.frequency(RoundTeams, j) == 0){ 
                            newErrors++; 
                        } else {
                            newErrors += Collections.frequency(RoundTeams, j) - 1; 
                        }    
                    }

                }
            }
            return newErrors;    
        }
        
        
    }
    
    public static boolean setOnRound(Match MO, roundCand RC) { 
        Match JumpFinished = new Match(MO.getHome(), MO.getVisitor(), RC.getRoundDate(), RC.getRoundcand()); 
        
        int newerr = getRoundPenaltyIfThisMatchIsSetHere(RC.getRoundcand(),MO); //Sallitaanko siirto kierrokselle jos se aiheuttaa yhtä paljon virheitä?
        int olderr = PenaltyC.getRoundPenalty(JumpFinished.getRound()); //Vanha virhetilanne kierroksella (tyhjäkierros antaa virheitä koska sieltä puuttuu otteluita.) 
        int beforeMoveErrs = PenaltyC.getRoundPenalty(MO.getRound()); //Lähtötilanteen virheet siirrettävältä kierrokselta
        
        double grain = r.nextDouble();
        if(newerr <= olderr || grain < 0.0015){ //Sallitaan huonontava siirto hyvin pienellä todennäköisyydellä (SA)
            TabuL.addMatch(new Tabu(JumpFinished,RC.getRoundcand())); //Lisää nyt oikein Tabulistalle jos se on täynnä!
            roundStack[MO.getRound()].remove(MO);
            roundStack[RC.roundcand].add(JumpFinished);
            PenaltyC.countTeamPenalty();
            if(grain < 0.0015) System.out.println("alle 0.0015!");
            return true;
        } else {
            return false;
        }
        
    }
    
    public static Match getRoundMatchWhichCausesMostPenalty(int round){
        
        ArrayList retval = new ArrayList();
        ArrayList MatchesWithPenalty = new ArrayList();
        ArrayList RoundMatches = roundStack[round];
        matchCand mC = null;
            
        for (int j = 0; j < RoundMatches.size(); j++) {
            Match tempMO = (Match)RoundMatches.get(j);
            if(!TabuL.isInList(tempMO, tempMO.getRound())){
                for (int k = 0; k < RoundMatches.size(); k++) {
                    Match ndl = (Match)RoundMatches.get(k);
                    if(tempMO.getHome() == ndl.getHome() && tempMO.getVisitor() == ndl.getVisitor()){
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
        
        
        if(MatchesWithPenalty.isEmpty() && RoundMatches.size() > 0){
            for (int i = 0; i < RoundMatches.size(); i++) {
                Match MO = (Match) RoundMatches.get(i);
                if(!TabuL.isInList(MO,MO.getRound())){
                    mC = new matchCand(MO,0);
                    MatchesWithPenalty.add(mC);
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
                    MC = new matchCand(MO, 0);
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
        
        int numofWM = retval.size();
        if(numofWM == 0){
            //Match MO = new Match(true);
            //return null; //Pitäisikö tässä vaan arpoa satunnainen kierrokselta?
            return getRandomMatch(round);
        } else if(numofWM == 1){
            matchCand MC = (matchCand)retval.get(0);
            return MC.getMatch();
        } else{
            matchCand MC = (matchCand)retval.get(r.nextInt(numofWM));
            return MC.getMatch();
        }
        
        
    }
    
    public static void PrintMatchList(){ //Ohjelman tulostus
        int sum = 0;
        int lockCount = 0;
        //System.out.println("# " + "K - V" + " PVM " );
        System.out.println();
        System.out.println("Ottelut: ");
        /*
        for (Object singleRound : roundStack) { 
            ArrayList RList = (ArrayList)singleRound;
            for (Object RList1 : RList) {
                Match MO = (Match) RList1;
                System.out.println( MO.toString());
                //if(MO.isLockedToRow()== true) lockCount++;
                sum++;
            
            }
        }*/
        for (int i = 0; i < ROUNDS; i++) {
            ArrayList RList = (ArrayList)roundStack[i];
            if(RList.size() == 0){
                System.out.println(i + " -");
            } else {
                for (Object list : RList) {
                    Match MO = (Match) list;
                    System.out.print(MO.toString());
                    if(TabuL.isInList(MO, MO.getRound())) System.out.print(" Tabulla");
                    System.out.println();
                    sum++;
                }
  
            }
        }
                
        System.out.println("Virheet: " + PenaltyC.getOverallPenalty() + " otteluja: " + sum + " Lukittuja: " + lockCount); 
    }
}
