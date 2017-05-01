/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TeemaFirstAndSecond;

import static TeemaFirstAndSecond.InitializeList.AllGameDates;
import static TeemaFirstAndSecond.TeemaFAS.ROUNDS;
import static TeemaFirstAndSecond.TeemaFAS.roundStack;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Iiro
 */
public class Jumper {

    public static TabuList TabuL = new TabuList();
    
    
    public static void BeginMoveChain(int jumps){
        boolean succesJ = Jump(G.getRandomMatch(),jumps);           
    }
    
    public static Match JumpF = null; //tätä arvoa päivitetään ekan hypyn jälkeen.
    
    public static boolean Jump(Match Begin, int jumps){
        JumpF = new Match(Begin.getHome(), Begin.getVisitor(), Begin.getGameDate(), Begin.getRound());
        boolean succes = true;
        
        
        
        int i;
        for (i = 0; i < jumps && succes == true; i++) {
                G.PrintMatchList();
                System.out.println("hypättiin: " + JumpF.toString());
                
            roundCand RC = G.getRoundCandidate(JumpF);
            
                
                System.out.println("tämä peli aiheuttaa seuraavat virheet: " + JumpF.toString());
                for (int j = 0; j < ROUNDS; j++) { 
                    System.out.print(j + "# VK: " + G.getRoundPenaltyIfThisMatchIsSetHere(j,JumpF));
                    if(roundStack[j].isEmpty()) System.out.print(" Tyhjä");
                    System.out.println();
                }
                System.out.println("Round Cand: " + RC.getRoundcand());
            
            succes = G.setOnRound(JumpF, RC); //jatketaanko hyppyä
            
                System.out.println("Kokonais virh. " + PenaltyC.getOverallPenalty());
            
            
            if(succes == true){
                JumpF = G.getRoundMatchWhichCausesMostPenalty(RC.getRoundcand()); //Palauttaa kierrokselta yhden ottelun tai nullin jos kaikki on tabulla.
                if(JumpF == null) succes = false;
                
            } else {
                break;
            }
            
            if(succes == false){
                System.out.println("Hyppy loppui, arvotaan uusi: ");
                break;
            }
            
        }
        
        if(i == jumps){
             System.out.println("--- i == jumps ---");
            return true;
        }else{
            return false;
        }
        
        
        
        
    }
    
    
    
    
    
    
    
    
    
    

    
    
    
    
    
    
    
    
    
    
    

    
}

class Flag{
    boolean Flg;
    public Flag(){}
    public Flag(boolean UpOrDown){
        this.Flg = UpOrDown;
    }
    public boolean isFlg() {
        return Flg;
    }
    public void setFlg(boolean Flg) {
        this.Flg = Flg;
    }
}