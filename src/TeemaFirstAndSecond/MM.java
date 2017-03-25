package TeemaFirstAndSecond;

import static TeemaFirstAndSecond.InitializeList.AllGameDates;
import static TeemaFirstAndSecond.TeemaFAS.ROUNDS;
import static TeemaFirstAndSecond.TeemaFAS.TEAMS;
import static TeemaFirstAndSecond.TeemaFAS.roundStack;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
    public static TabuList TabuL = new TabuList();
    
    
    
    public static void swapMatch(){
        LinkedBlockingDeque L = TabuL.getTabuL();
        /*
        Match dummy = new Match(1,2,null,10);
        Match wrong = new Match(5,6,null,10);
        
        TabuL.addMatch(new Tabu(dummy,5));
        TabuL.addMatch(new Tabu(dummy,6));
        TabuL.addMatch(new Tabu(dummy,7));

        
        ArrayList history = TabuL.getAllMatcheRounds(dummy);
        
        dummy.setRound(5); //Arvottiin historiassa oleva kierros
        
        while(history.contains(dummy.getRound())){
            dummy.setRound(r.nextInt(ROUNDS));
        }
        
        System.out.println("stop");
        */
        
        

        int randomRound = r.nextInt(ROUNDS);
        while(roundStack[randomRound].size() <= 1){
            randomRound = r.nextInt(ROUNDS);
        }
        int roundLength = roundStack[randomRound].size();
        
        Match RM = (Match)roundStack[randomRound].get(r.nextInt(roundLength));
        
        ArrayList history = TabuL.getAllMatcheRounds(RM); //hakee ottelun historian, eli mihin kierroksille sitä on jo sovitettu

        while(history.contains(RM.getRound()) || RM.isLockedToRow()){ //Jos peli löytyy tabulistalta, arvotaan kokonaan uusi niin kauan kunnes löytyy uusi peli
            randomRound = r.nextInt(ROUNDS);
            while(roundStack[randomRound].size() <= 1){
                randomRound = r.nextInt(ROUNDS);
            }
            roundLength = roundStack[randomRound].size();
            RM = (Match)roundStack[randomRound].get(r.nextInt(roundLength));
        }
        
        //otetaan kopio ottelusta, jotta voidaan poistaa tarvittaessa se listalta, jos sille löytyy parempi paikka.
        Match RMcopy = new Match(RM.getHome(),RM.getVisitor(),RM.getGameDate(),RM.getRound()); 
     
        
        
        /* Koitetaan siirtää peliä satunnaiselle kierrokselle ja tutkitaan 
        aiheutuuko ennemmän vai vähemmän virheitä kuin sieltä kierrokselta mistä lähdettiin*/
        if(Move(RM)){
            TabuL.addMatch(new Tabu(RM,RMcopy.getRound()));
            roundStack[randomRound].remove(RMcopy);

        } else {   
           TabuL.addMatch(new Tabu(RM,RMcopy.getRound()));
        }
        
        
    }
    
    
    public static void BeginMoveChain(int jumps){
        
        for (int i = 0; i < jumps; i++) {
            if(i == 0){
                int randomRound = r.nextInt(ROUNDS);
                while(roundStack[randomRound].size() <= 1){
                    randomRound = r.nextInt(ROUNDS);
                }
                int roundLength = roundStack[randomRound].size();

                Match RM = (Match)roundStack[randomRound].get(r.nextInt(roundLength));
                Jump(RM);
            } else {
                Jump();
            }
            
        }

    }
    
    public static Match goingMatch = null; //Jatkossa tutkittava ottelu ladataan tähän

    public static void Jump(Match Begin){
        ArrayList candidates = getRoundCandidates(Begin);//hakee kierrokset missä penalty määrä on pienin, jos löytyy useampi yhtä pieni, niin niistä arvotaan mihin mennään.
        int numofcands = candidates.size();
        roundCand RC = null;
        
        if(numofcands > 1){
            RC = (roundCand)candidates.get(r.nextInt(numofcands)); //arvotaan mikä otetaan
        } else if (numofcands == 1){
            RC = (roundCand)candidates.get(numofcands-1); //eka
        }
        
        Match JumpFinished = new Match(Begin.getHome(), Begin.getVisitor(), RC.getRoundDate(), RC.getRoundcand());
        TabuL.addMatch(new Tabu(Begin,RC.getRoundcand()));
        roundStack[Begin.getRound()].remove(Begin);
        roundStack[RC.roundcand].add(JumpFinished);
        
        
        //mikä kierroksen otteluista aiheuttaa eniten virheitä kierroksella?
        Match[] test = PenaltyC.getRoundMatchWhichCausesMostPenalty(0);
        
        
        
        int[] smallPenaltyCandMatch = getEqualpenaltyFromCand(candidates);
        
        ArrayList RoundMatchList = getMatchesWithEqualCausePenalty(candidates); //Arpoo ehdokkaista kierroksen ja palauttaa kierroksen ottelut.
        
        //tähän väliin ottelun asettaminen kierrokselle?
        
        /*Tässä välissä pitää laskea mikä kierroksen otteluista aiheuttaa eniten virheitä kierroksella ja palauttaa lista jos löytyy useampi ottelu mikä aiheuttaa samanverran. 
        Jos palauttaa listan niin sillon arvotaan taas ottelu mitä lähdetään sovittamaan.*/
        

        goingMatch = (Match)RoundMatchList.get(r.nextInt(RoundMatchList.size()));
        
        //Nyt tämän jälkeen käydään taas kaikki ottelut läpi uudella matsilla (rM)
        
    }
    public static void Jump(){
        
    }
    
    
    /*Ongelmana on nyt se että jos sovitettava ottelu aiheuttaa nolla virhettä kierroksella, 
    niin sen huomioiminen on vaikeaa int[] tietorakenteessa.
    Tarkoitus on toteuttaa tietorakenne, josta käy myös ilmi nollavirhetilanteet. */
    public static ArrayList getRoundCandidates(Match Begin){
        ArrayList retval = new ArrayList();
        roundCand RC = null;
        //int lastWinner = 0;
        
        //Etsii pienimmän virhemääräisen kierroksen.
        int minValue = getRoundPenaltyIfThisMatchIsSetHere(r.nextInt(ROUNDS),Begin);
        for (int i = 1; i < ROUNDS; i++) {
            int temp = getRoundPenaltyIfThisMatchIsSetHere(i,Begin);
            if (temp < minValue) {
                minValue = temp;
            }
        }

        //Kerää kaikki tällä virhemäärällä olevat kierrokset.
        for (int i = 0; i < ROUNDS; i++) { 
            int newPenalty = getRoundPenaltyIfThisMatchIsSetHere(i,Begin); //otetaan ylös virhetilanne jokaiselta kierrokselta
            int oldPenalty = PenaltyC.getAllTeamsRoundPenalty(i); //Hakee kierroksen virheet ilman sovitettavaa ottelua

            if(newPenalty <= minValue && newPenalty <= oldPenalty){
                minValue = newPenalty;
                RC = new roundCand(i, newPenalty,AllGameDates[i]);
                retval.add(RC);
            }
        }
        //Testitulostus jos löytyy nollavirheeksi muuttuvia kierroksia
        for (Object cand : retval) {
            roundCand tRC = (roundCand)cand;
            if(tRC.causedpenalty == 0) System.out.println(+ tRC.roundcand + " " + tRC.causedpenalty);
        }
        return retval;
    }


    

    
    
    public static ArrayList getMatchesWithEqualCausePenalty(int[] candidates){
        ArrayList retval = new ArrayList();
        
        return retval;
    }
    
    
    
    
    
    
    /* Seuraava toteutus ottaa arvotusta ohjelmasta yhden ottelun ja tarkastelee aiheuttaako se lisävirheitä vai ei */
    public static boolean Move(Match findplace){
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
    
    public static void ArrangeHomeAndVisitLocks(){
        List RoundMatches = new ArrayList();
        List ListOfHomeLocks = new ArrayList();
        List ListOfVisitLocks = new ArrayList();
        
        
        ArrayList[] BothLocks = PenaltyC.GetHomeAndVisitPrevents(); //hakee ottelut joissa on koti- ja vierasestoja
        ListOfHomeLocks.addAll(BothLocks[0]); //Asetetaan listoihin
        ListOfVisitLocks.addAll(BothLocks[1]); 

        
        
        
        while(BothLocks[0].size() > 0 || BothLocks[1].size() > 0){
            
            ListOfHomeLocks.addAll(BothLocks[0]); //Päivitetään tilanne
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
                        newMO.setRound(r.nextInt(ROUNDS));
                        while(newMO.getHome() == LM.getHome() && newMO.getRound() == LM.getRound() ){ //tähän joku viritys ettei ota kaikkia?
                            newMO.setRound(r.nextInt(ROUNDS));
                        }
                    }
                    
                    if(MO.getRound() != newMO.getRound() /*&& roundStack[i].size() >= 1*/){ //???????
                        roundStack[newMO.getRound()].add(newMO);
                        roundStack[i].remove(MO);    
                    } else {
                        //System.out.println("tuleeko tänne koskaan?");
                    }
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

/* tietorakenne kierrosehdokkaiden säilömiseen. 
Sisältää kierrosehdokkaan ja ottelun aiheuttamat virheet tällä kierroksella. */
class roundCand{
    int roundcand;
    Date roundDate;
    int causedpenalty;
    
    public roundCand(int RC, int CP, Date RD){
        roundcand = RC;
        causedpenalty = CP;
        roundDate = RD;
    }
    
    public int getRoundcand() {
        return roundcand;
    }
    public int getCausedpenalty() {
        return causedpenalty;
    }
    public Date getRoundDate() {
        return roundDate;
    }
    
    public void setRoundcand(int roundcand) {
        this.roundcand = roundcand;
    }
    public void setCausedpenalty(int causedpenalty) {
        this.causedpenalty = causedpenalty;
    }
    public void setRoundDate(Date roundDate) {
        this.roundDate = roundDate;
    }

}


