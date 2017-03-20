package TeemaFirstAndSecond;

import java.util.Comparator;
import java.util.Date;

/**
 *
 * @author Iiro
 */

public class Match implements Comparator<Match>, Comparable<Match>, Cloneable{
    private int Home;
    private int Visitor;
    private int Round;
    private Date GameDate;
    private boolean lockedToRow = false;
    
    public Match(int home, int visitor, Date GameDate, int Round){
        this.Home = home;
        this.Visitor = visitor;
        this.GameDate = GameDate;
        this.Round = Round;
      
    }
    public Match(){ 
    }
    
    public boolean isLockedToRow() {
        return lockedToRow;
    }
    public void setLockedToRow(boolean lockedToRow) {
        this.lockedToRow = lockedToRow;
    }
    public int getHome() {
        return Home;
    }    
    public int getVisitor() {
        return Visitor;
    }
    public int getRound() {
        return Round;
    }
    public Date getGameDate() {
        return GameDate;
    }
    
    public void setRound(int round){
        this.Round = round;
    }
    public void setDate(Date d){
        this.GameDate = d;
    }
    
    @Override
    public boolean equals(Object object){
        boolean isEqual = false;
        if(object != null && object instanceof TeemaFirstAndSecond.Match){
            
            if((this.Home == ((Match)object).Home) && 
                (this.Visitor == ((Match)object).Visitor) //&&
                //(this.Round == ((Match)object).Round)
                //(this.GameDate == ((Match)object).GameDate)
                    ){
                isEqual = true;
            }
            
        }
        return isEqual;
    }
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 13 * hash + this.Home;
        hash = 13 * hash + this.Visitor;
        //hash = 13 * hash + this.Round;
        return hash;
    }
    @Override
    public int compare(Match t, Match t1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Override
    public int compareTo(Match t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Override
    public String toString(){
        String locked = "";
        if(lockedToRow) locked = "LOCKED";
        String s = Round + "# " + Home + " " + Visitor  + " " + locked  /*+ GameDate*/;
        return s;
    }
}
