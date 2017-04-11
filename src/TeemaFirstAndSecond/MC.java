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

/**
 *
 * @author Iiro
 */
public class MC {
    
    public static Random r = new Random(100);
    public static TabuList TabuL = new TabuList();
    
    public static void BeginMoveChain(int jumps){
        Jump(getRandomMatch(),jumps);
    }
    
    public static Match jumpBegin = null; //tätä arvoa päivitetään ekan hypyn jälkeen.
    
    public static void Jump(Match Begin, int jumps){
        
        jumpBegin = new Match(Begin.getHome(), Begin.getVisitor(), Begin.getGameDate(), Begin.getRound());
        
        for (int i = 0; i < jumps; i++) {
            
            PenaltyC.countTeamPenalty(); //päivitetään virhetilanne joka hypyn jälkeen
            
            System.out.println("tämä kierros aiheuttaa seuraavat virheet: " + jumpBegin.toString());
            for (int j = 0; j < ROUNDS; j++) { 
                System.out.print(j + "# VK: " + getRoundPenaltyIfThisMatchIsSetHere(j,jumpBegin));
                if(roundStack[j].isEmpty()) System.out.print(" Tyhjä");
                System.out.println();
            }
            
            
            /*kertoo Paljonko on kierrosvirheet jos tämä asetetaan sinne*/
            ArrayList candidates = getRoundCandidates(jumpBegin);//hakee kierrokset missä penalty määrä on pienin, jos löytyy useampi yhtä pieni, niin niistä arvotaan mihin mennään.
            int numofcands = candidates.size();
            roundCand RC = null;
            
            if(numofcands == 1){
                RC = (roundCand)candidates.get(numofcands-1); //eka
            } else if (numofcands > 1){
                RC = (roundCand)candidates.get(r.nextInt(numofcands)); //arvotaan mikä otetaan, mutta ei kuitenkaan samaa kierrosta mistä lähdettiin.
                while(RC.getRoundcand() == jumpBegin.getRound()){
                    RC = (roundCand)candidates.get(r.nextInt(numofcands));
                }
            }
            
            if(numofcands != 0){
                
                Match JumpFinished = new Match(jumpBegin.getHome(), jumpBegin.getVisitor(), RC.getRoundDate(), RC.getRoundcand());
                
                System.out.println("Kierrosehdokas: " + JumpFinished.getRound());
                
                int newerr = getRoundPenaltyIfThisMatchIsSetHere(RC.getRoundcand(),JumpFinished);
                int olderr = PenaltyC.getRoundPenalty(JumpFinished.getRound()); //tyhjäkierros antaa virheitä koska sieltä puuttuu otteluita
                
                if(newerr < olderr || r.nextDouble() < 0.0015){ //Sallitaan huonontava siirto hyvin pienellä todennäköisyydellä (SA)
                
                    TabuL.addMatch(new Tabu(JumpFinished,RC.getRoundcand()));
                    roundStack[jumpBegin.getRound()].remove(jumpBegin);
                    roundStack[RC.roundcand].add(JumpFinished);
                    System.out.println("LAITETTIIN KIERROKSELLE: " + JumpFinished.toString());
                    
                    PenaltyC.countTeamPenalty();
                
                    System.out.println("kierroksen #" + JumpFinished.getRound() + " pelit");
                    PenaltyC.printRoundTeams(JumpFinished.getRound());


                    //mikä kierroksen otteluista aiheuttaa eniten virheitä kierroksella (samaa mikä laitettiin ei voi valita)
                    ArrayList worstMatches = PenaltyC.getRoundMatchWhichCausesMostPenalty(JumpFinished.getRound()); 
                    int numofWM = worstMatches.size();

                    System.out.println("ehdokkaat pelille: " + JumpFinished.toString());
                    for (int j = 0; j < worstMatches.size(); j++) {
                        matchCand MC = (matchCand)worstMatches.get(j);
                        Match MO = MC.getMatch();
                        System.out.print("E: " + MO.toString()+ "Penalty: " + MC.getmPenalty());
                        if(TabuL.isInList(MO, MO.getRound())) System.out.println(" Tabulla");
                        System.out.println();

                    }

                    if(numofWM == 0){

                        TabuL.addMatch(new Tabu(jumpBegin,jumpBegin.getRound()));
                        jumpBegin = getRandomMatch();
                        while(TabuL.isInList(jumpBegin, jumpBegin.getRound())) jumpBegin = getRandomMatch();
                        System.out.println("numofWM == 0, arvotaan matsi");
                    } else if(numofWM == 1){
                        TabuL.addMatch(new Tabu(jumpBegin,jumpBegin.getRound()));
                        matchCand MC = (matchCand)worstMatches.get(0);
                        jumpBegin = (Match) MC.getMatch();
                        System.out.println("numofWM == 1, otetaan ensimmäinen");

                    } else {
                        TabuL.addMatch(new Tabu(jumpBegin,jumpBegin.getRound()));
                        matchCand MC = (matchCand)worstMatches.get(r.nextInt(numofWM));
                        while(TabuL.isInList(MC.getMatch(), MC.getMatch().getRound())) MC = (matchCand)worstMatches.get(r.nextInt(numofWM));
                        jumpBegin = (Match) MC.getMatch();
                        System.out.println("numofWM, ehdokkaita on useampi, arvotaan ehdokkaiden kesken");
                    }

                    } else{
                        //Siirto aiheuttaisi enemmän virheitä kuin mitä kierroksella oli ennen --> ei aseteta
                        System.out.println("ei laitettu peliä: " + JumpFinished.toString() + " koska newerr " + newerr + " oli suurempi kuin olderr: " + olderr);
                        System.out.println("laitetaan yritetty peli tabulistalle: " + JumpFinished.toString());
                        //TabuL.addMatch(new Tabu(JumpFinished,RC.getRoundcand()));


                    }
                

                
            } else {
                TabuL.addMatch(new Tabu(jumpBegin,jumpBegin.getRound()));
                System.out.println("ARVOTAAN UUSI");
                jumpBegin = getRandomMatch();
                while(TabuL.isInList(jumpBegin, jumpBegin.getRound())) jumpBegin = getRandomMatch();
            }
            
            
            PenaltyC.PrintMatchList();
            
        }
    }
    
    /*Palauttaa kierroksia jotka aiheuttavat vähiten virheitä sovitettavalla matsilla*/
    public static ArrayList getRoundCandidates(Match Begin){
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
        
        for (int i = 0; i < retval.size(); i++) {
            roundCand FC = (roundCand)retval.get(i);
            if(Begin.getRound() == FC.getRoundcand()){
                retval.remove(FC);
            }
            
        }
        
        //int minIndex = dirtyList.indexOf(Collections.min(dirtyList)); //otetaan ehdokas millä pienin penalty
        return retval;
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
    
    public static Match getRandomMatch(){
        int randomRound = r.nextInt(ROUNDS);
        while(roundStack[randomRound].size() <= 0){
            randomRound = r.nextInt(ROUNDS);
        }
        int roundLength = roundStack[randomRound].size();
        Match RM = (Match)roundStack[randomRound].get(r.nextInt(roundLength));
        return RM;
    }
}

/* tietorakenne kierrosehdokkaiden säilömiseen. 
Sisältää kierrosehdokkaan ja ottelun aiheuttamat virheet tällä kierroksella. */
class roundCand implements Comparable<roundCand>{
    int roundcand;
    Date roundDate;
    int penalty;
    
    public roundCand(int RC, int CP, Date RD){
        roundcand = RC;
        penalty = CP;
        roundDate = RD;
    }
    
    public int getRoundcand() {
        return roundcand;
    }
    public int getCausedpenalty() {
        return penalty;
    }
    public Date getRoundDate() {
        return roundDate;
    }
    
    public void setRoundcand(int roundcand) {
        this.roundcand = roundcand;
    }
    public void setCausedpenalty(int causedpenalty) {
        this.penalty = causedpenalty;
    }
    public void setRoundDate(Date roundDate) {
        this.roundDate = roundDate;
    }

    @Override
    public int compareTo(roundCand t) {
        return this.penalty - t.penalty;
    }

}
class matchCand{

    Match match;
    int mPenalty;
    
    public matchCand(Match MC, int mP){
        match = MC;
        mPenalty = mP;
    }
    
    public Match getMatch() {
        return match;
    }

    public int getmPenalty() {
        return mPenalty;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public void setmPenalty(int mPenalty) {
        this.mPenalty = mPenalty;
    }

}
