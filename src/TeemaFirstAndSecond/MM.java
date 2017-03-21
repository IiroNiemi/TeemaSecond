package TeemaFirstAndSecond;

import static TeemaFirstAndSecond.TeemaFAS.ROUNDS;
import static TeemaFirstAndSecond.TeemaFAS.TEAMS;
import static TeemaFirstAndSecond.TeemaFAS.roundStack;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.LinkedBlockingDeque;

/**
 *
 * @author Iiro
 */
public class MM {
    
    public static int[][] homeTeamLock = {{0,2},{0,13},{0,14},{0,15},{0,16},{5,34},{8,27},{9,25},{11,1},{11,19},{11,28},{11,30},{13,1},{13,9},{13,24},{13,25},{13,30},{14,7},{14,9},{14,25},{0,8},{4,8},{5,8},{7,8},{8,8},{10,8},{12,8},{13,8},{0,11},{4,11},{5,11},{7,11},{8,11},{10,11},{12,11},{13,11},{0,20},{4,20},{5,20},{7,20},{8,20},{10,20},{12,20},{13,20},{0,32},{4,32},{5,32},{7,32},{8,32},{10,32},{12,32},{13,32},{0,35},{4,35},{5,35},{7,35},{8,35},{10,35},{12,35},{13,35}};
    public static int[][] visitTeamLock = {{0,8},{4,8},{5,8},{7,8},{8,8},{10,8},{12,8},{13,8},{0,11},{4,11},{5,11},{7,11},{8,11},{10,11},{12,11},{13,11},{0,20},{4,20},{5,20},{7,20},{8,20},{10,20},{12,20},{13,20}};
    
    
    public static Random r = new Random();
    //Tabulista, näitä otteluita ei siirretä hetkeen, koska niitä sovitettiin juuri!
    public static LinkedBlockingDeque TabuL = new LinkedBlockingDeque(2); 
    public static int[][] oddTeams = new int[36][1];
    
    public static void swapMatch(){
        int randomRound = r.nextInt(ROUNDS);
        while(roundStack[randomRound].size() <= 1){
            randomRound = r.nextInt(ROUNDS);
        }
        int roundLength = roundStack[randomRound].size();
        
        Match RM = (Match)roundStack[randomRound].get(r.nextInt(roundLength));
        
        while(TabuL.contains(RM) || RM.isLockedToRow()){ //Jos peli löytyy tabulistalta, arvotaan kokonaan uusi niin kauan kunnes löytyy uusi peli
            randomRound = r.nextInt(ROUNDS);
            while(roundStack[randomRound].size() <= 1){
                randomRound = r.nextInt(ROUNDS);
            }
            roundLength = roundStack[randomRound].size();
            RM = (Match)roundStack[randomRound].get(r.nextInt(roundLength));
        }
        
        //otetaan kopio ottelusta, jotta voidaan poistaa tarvittaessa se listalta, jos sille löytyy parempi paikka.
        Match RMcopy = new Match(RM.getHome(),RM.getVisitor(),RM.getGameDate(),RM.getRound()); 
     
        /* Aiheutuuko pelistä enemmän virheitä kierroksella vai ei */
        if(compareMatchPenaltyToOtherRoundsAndTryToSet(RM)){
            roundStack[randomRound].remove(RMcopy);
        } else {   
            if(TabuL.size() == 1){
                TabuL.removeFirst();
            }
            TabuL.addLast(RMcopy);   
        }
    }
    
    /* Seuraava toteutus ottaa arvotusta ohjelmasta yhden ottelun ja tarkastelee aiheuttaako se lisävirheitä vai ei */
    public static boolean compareMatchPenaltyToOtherRoundsAndTryToSet(Match findplace){
        boolean succed = false;
        int roundCandidate = 0;
        int lastWinner = 0;
        
        //Koitetaan sovittaa ottelua jokaiselle kierrokselle
        for (int i = 0; i < ROUNDS; i++) { 
            int newPenalty = getRoundPenaltyIfThisMatchIsSetHere(i,findplace); //otetaan ylös virhetilanne jokaiselta kierrokselta
            int oldPenalty = PenaltyC.getAllTeamsRoundPenalty(i);
            
            if(lastWinner == 0){
                if(newPenalty < oldPenalty){
                    roundCandidate = i; //Arvottu ottelu aiheuttaa vähemmän rankkareita täällä, otetaan kierros ylös mihin ottelu sopisi
                    lastWinner = newPenalty; //jatkossa koitetaan etsiä vielä parempaa kierrosta vertaamalla uuteen rankkariin
                }
            } else if(newPenalty < oldPenalty && newPenalty < lastWinner){
                roundCandidate = i;
                lastWinner = newPenalty;
            }
        }

        /* Tärkeä kohta; ennenkuin vastaus sovituksesta palautetaan, 
        tarkastetaan muuttuiko virhetilanne, jos muuttui niin oliko se parempaan suuntaan */
        if(findplace.getRound() == roundCandidate){
            succed = false;
        } else if(getRoundPenaltyIfThisMatchIsSetHere(roundCandidate,findplace) < PenaltyC.getAllTeamsRoundPenalty(roundCandidate)) {
            
            //Haetaan kierroksen päivämäärä ja vaihdetaan se uudelle ottelulle
            for (int i = 0; i < roundStack[roundCandidate].size(); i++) { 
                Match DateM = (Match)roundStack[roundCandidate].get(i);
                findplace.setDate(DateM.getGameDate());
            }
            
            findplace.setRound(roundCandidate);
            roundStack[roundCandidate].add(findplace);
            succed = true;
        }     

        return succed;
    }
    
    /* Laskee virheet jos ottelu laitetaan tälle kierrokselle */    
    public static int getRoundPenaltyIfThisMatchIsSetHere(int round, Match findplace){
        List RoundMatches = new ArrayList();
        List RoundTeams = new ArrayList();
        RoundMatches.addAll(roundStack[round]);
        int newErrors = 0;
        boolean oddTeamNoPenalty = true;
        
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
            
            if(Collections.frequency(RoundTeams, j) == 1){ //Löytyi kerran, kaikki ok!
            } else {
                //allowpenalty sallii yhden joukkueelle yhden poissaolon per kierros!                              
                if(oddTeamNoPenalty == true){
                    if(Collections.frequency(RoundTeams, j) == 0){  
                        oddTeamNoPenalty = false; //Yksi poissaolo armahdettu
                        oddTeams[round][0] = j;
                       
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
    
    public static void ArrangeHomeAndVisitLocks(){
        List RoundMatches = new ArrayList();
        List ListOfHomeLocks = new ArrayList();
        List ListOfVisitLocks = new ArrayList();
        
        ArrayList[] BothLocks = PenaltyC.GetHomeAndVisitPrevents(); //hakee ottelut joissa on koti- ja vierasestoja
        ListOfHomeLocks.addAll(BothLocks[0]); //Asetetaan listoihin
        ListOfVisitLocks.addAll(BothLocks[1]); 

        
        
        
        while(BothLocks[0].size() > 0 || BothLocks[1].size() > 0){
            
            ListOfHomeLocks.addAll(BothLocks[0]); //Päivitetään tilanne, kutsu löytyy lopusta
            ListOfVisitLocks.addAll(BothLocks[1]);
            
            //ensin koitetaan löytää paikka kotiestopeleille
            for (int i = 0; i < roundStack.length; i++) {
                RoundMatches.addAll(roundStack[i]);
                for (int j = 0; j < RoundMatches.size(); j++) {
                    Match MO = (Match) RoundMatches.get(j);
                    Match newMO = new Match(MO.getHome(), MO.getVisitor(), MO.getGameDate(),MO.getRound()); //Kopio kierroksen ottelusta, ettei viittaa samaan muistialueeseen        
                    for (int k = 0; k < ListOfHomeLocks.size(); k++) {
                        Match LM = (Match)ListOfHomeLocks.get(k);
                        while(newMO.getHome() == LM.getHome() && newMO.getRound() == LM.getRound()){
                            newMO.setRound(r.nextInt(ROUNDS));
                        }
                    }
                    if(MO.getRound() != newMO.getRound() ){
                        roundStack[newMO.getRound()].add(newMO);
                        roundStack[i].remove(MO);    
                    } else { }
                }
                RoundMatches.clear();
            }
            
            //Sitten sama vieraspeleille
            for (int i = 0; i < roundStack.length; i++) {
                RoundMatches.addAll(roundStack[i]);
                for (int j = 0; j < RoundMatches.size(); j++) {
                    Match MO = (Match) RoundMatches.get(j);
                    Match newMO = new Match(MO.getHome(), MO.getVisitor(), MO.getGameDate(),MO.getRound());      
                    for (int k = 0; k < ListOfVisitLocks.size(); k++) {
                        Match LM = (Match)ListOfVisitLocks.get(k);
                        while(newMO.getHome() == LM.getHome() && newMO.getRound() == LM.getRound()){
                            newMO.setRound(r.nextInt(ROUNDS));
                        }
                    }
                    if(MO.getRound() != newMO.getRound() /*&& roundStack[i].size() > 1*/){ //<-------------tähän joku viritys ettei ota kaikkia?
                        roundStack[newMO.getRound()].add(newMO);
                        roundStack[i].remove(MO);    
                    } else { }
                }
                RoundMatches.clear();
            }
            
        
            BothLocks = PenaltyC.GetHomeAndVisitPrevents(); //Katsotaan tilanne
            ListOfHomeLocks.clear();
            ListOfVisitLocks.clear();
            //System.out.println("kotiestoja: " + BothLocks[0].size());
            //System.out.println("vierasestoja: " + BothLocks[1].size());
        }
        
        PenaltyC.PrintHomePrevents();
        System.out.println("stop" );
        

        
        
    }
    
    //Nää vois olla periaatteessa sama objekti, mutta toistaiseksi pidän erillään selkeyden vuoksi.
    public static class homeLock{
        int team;
        int round;
        
        public int getTeam() {
            return team;
        }

        public int getRound() {
            return round;
        }

        public homeLock(int team, int round){
            this.team = team;
            this.round = round;
        }
    }
    public static class visitLock{
        int team;
        int round;
        
        public int getTeam() {
            return team;
        }

        public int getRound() {
            return round;
        }

        public visitLock(int team, int round){
            this.team = team;
            this.round = round;
        }
    }
    

}


