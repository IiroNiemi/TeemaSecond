package TeemaFirstAndSecond;


import static TeemaFirstAndSecond.InitializeList.AllGameDates;
import static TeemaFirstAndSecond.MM.getRoundPenaltyIfThisMatchIsSetHere;
import static TeemaFirstAndSecond.TeemaFAS.ROUNDS;
import static TeemaFirstAndSecond.TeemaFAS.roundStack;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 *
 * @author Iiro
 */
public class MC {
    
    public static Random r = new Random();
    public static TabuList TabuL = new TabuList();
    
    public static void BeginMoveChain(int jumps){
        Jump(getRandomMatch(),jumps);
    }
    
    public static Match jumpBegin = null; //tätä arvoa päivitetään ekan hypyn jälkeen.
    public static void Jump(Match Begin, int jumps){
        
        jumpBegin = new Match(Begin.getHome(), Begin.getVisitor(), Begin.getGameDate(), Begin.getRound());
        
        for (int i = 0; i < jumps; i++) {
            
            PenaltyC.countTeamPenalty(); //päivitetään virhetilanne joka hypyn jälkeen
            
            
            
            /*kertoo Paljonko on kierrosvirheet jos tämä asetetaan sinne*/
            ArrayList candidates = getRoundCandidates(jumpBegin);//hakee kierrokset missä penalty määrä on pienin, jos löytyy useampi yhtä pieni, niin niistä arvotaan mihin mennään.
            int numofcands = candidates.size();
            roundCand RC = null;

            if(numofcands == 1){
                RC = (roundCand)candidates.get(numofcands-1); //eka
            } else if (numofcands > 1){
                RC = (roundCand)candidates.get(r.nextInt(numofcands)); //arvotaan mikä otetaan
            }
            
            if(numofcands != 0){
                
                Match JumpFinished = new Match(jumpBegin.getHome(), jumpBegin.getVisitor(), RC.getRoundDate(), RC.getRoundcand());
                TabuL.addMatch(new Tabu(JumpFinished,RC.getRoundcand()));
                
                roundStack[jumpBegin.getRound()].remove(jumpBegin);
                roundStack[RC.roundcand].add(JumpFinished);
                    
               
                //mikä kierroksen otteluista aiheuttaa eniten virheitä kierroksella (samaa mikä laitettiin ei voi valita)
                ArrayList worstMatches = PenaltyC.getRoundMatchWhichCausesMostPenalty(JumpFinished.getRound()); 
                int numofWM = worstMatches.size();
                if(numofWM == 0){
                    //System.out.println("kaikki kierroksen ottelut olivat tabulla, arvotaan satunnainen matsi kaikilta kierroksilta");
                    jumpBegin = getRandomMatch();
                } else if(numofWM == 1){
                    matchCand MC = (matchCand)worstMatches.get(0);
                    jumpBegin = (Match) MC.getMatch();
                } else {
                    matchCand MC = (matchCand)worstMatches.get(r.nextInt(numofWM));
                    jumpBegin = (Match) MC.getMatch();
                }
                
            } else {
                System.out.println("Kandidaatteja tasan nolla");
                //jumpBegin = getRandomMatch();
            }
        }
    }
    
    /*Palauttaa kierroksia jotka aiheuttavat vähiten virheitä sovitettavalla matsilla (voi olla miinusmerkkinen myös)*/
    public static ArrayList getRoundCandidates(Match Begin){
        ArrayList retval = new ArrayList();
        roundCand RC = new roundCand(Begin.getRound(),0,Begin.getGameDate());
        
        //Etsii pienimmän virhemääräisen kierroksen.
        int minValue = getRoundPenaltyIfThisMatchIsSetHere(r.nextInt(ROUNDS),Begin);
        for (int i = 1; i < ROUNDS; i++) {
            int temp = getRoundPenaltyIfThisMatchIsSetHere(i,Begin);
            if (temp < minValue) {
                minValue = temp;
                RC = new roundCand(i, minValue,AllGameDates[i]);
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
        
        if(retval.isEmpty()){
            retval.add(RC);
        }
        
        //int minIndex = dirtyList.indexOf(Collections.min(dirtyList)); //otetaan ehdokas millä pienin penalty
        return retval;
    }
    
    public static Match getRandomMatch(){
        int randomRound = r.nextInt(ROUNDS);
        while(roundStack[randomRound].size() <= 1){
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
