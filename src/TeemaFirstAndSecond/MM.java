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
    
    public static Random r = new Random();
    //Tabulista, näitä otteluita ei siirretä hetkeen, koska niitä sovitettiin juuri!
    public static LinkedBlockingDeque TabuL = new LinkedBlockingDeque(1); 

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
        
        //otetaan kopio ottelusta, jotta voidaan poistaa tarvittaessa se listalta jos sille löytyy parempi paikka.
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
        tarkastetaan muuttuiko virhetilanne jos muuttui niin oliko se parempaan suuntaan */
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
