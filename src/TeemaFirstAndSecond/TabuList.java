/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TeemaFirstAndSecond;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingDeque;

/**
 *
 * @author Iiro
 */

class Tabu{
    Match MO;
    int round;
    
    public Match getMO() {
        return MO;
    }

    public int getRound() {
        return round;
    }

    public Tabu(Match mo, int round){
        this.MO = mo;
        this.round = round;
    }
            
    @Override
    public String toString () {
      return MO.toString() + " " + round;
    }
}



public class TabuList {

    private LinkedBlockingDeque TabuL = null;

    public TabuList () {
        TabuL = new LinkedBlockingDeque();
    }

    void addMatch(Tabu tb) {
        if(TabuL.size() == 9){
            TabuL.removeFirst();
        }
        else{
            TabuL.add(tb);
        }
        
    }
    
    public int size(){
        return TabuL.size();
    }
    
    public LinkedBlockingDeque getTabuL() {
        return TabuL;
    }
    
    public boolean isInList(Match MO, int round){
        ArrayList listTB = new ArrayList();        
        listTB.addAll(TabuL);
        boolean retval = false;
        
        Match needle = new Match(MO.getHome(),MO.getVisitor(),MO.getGameDate(),MO.getRound());
        
        if(listTB.size() > 0){
            for (int i = 0; i < listTB.size(); i++) {
                Tabu TO = (Tabu)listTB.get(i);
                Match oMO = (Match) TO.getMO();
                
                if(needle.getHome() == oMO.getHome() && needle.getVisitor() == oMO.getVisitor() && round == TO.getRound()){
                    retval = true;
                } 
            }  
        } else {
            retval = false;
        }
        

        return retval;
    }
    

    public ArrayList getAllMatcheRounds(Match MO){
        ArrayList listTB = new ArrayList();   
        ArrayList retval = new ArrayList();   
        listTB.addAll(TabuL);

        Match needle = new Match(MO.getHome(),MO.getVisitor(),MO.getGameDate(),MO.getRound());

        
        if(listTB.size() > 0){
            for (int i = 0; i < listTB.size(); i++) {
                Tabu TO = (Tabu)listTB.get(i);
                Match oMO = (Match) TO.getMO();
                
                if(needle.getHome() == oMO.getHome() && needle.getVisitor() == oMO.getVisitor()){
                    retval.add(TO.getRound());
                } 
            }  
        } else {
            return retval;
        }
        
        
        
        return retval;
    }
    
    
    
    
    
    
    
    
}
