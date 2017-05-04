/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TeemaFirstAndSecond;


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
            if(PenaltyC.getOverallPenalty() == 0) break;
                
            roundCand RC = G.getRoundCandidate(JumpF);
                if(RC.getRoundcand() == JumpF.getRound()) {
                    break;
                } 

            succes = G.setOnRound(JumpF, RC); //jatketaanko hyppyä

            
            
            if(succes == true){
                JumpF = G.getRoundMatchWhichCausesMostPenalty(RC.getRoundcand()); //Palauttaa kierrokselta yhden ottelun tai nullin jos kaikki on tabulla.
                if(JumpF == null) succes = false;
                
            } else {
                break;
            }
            
            if(succes == false){
                break;
            }
        }
        
        if(i == jumps){
            //System.out.println("--- OK ---");
            return true;
        }else{
            //System.out.println("Keskeytys hypyllä: " + i);
            return false;
        }
        
        
        
        
    }

}
