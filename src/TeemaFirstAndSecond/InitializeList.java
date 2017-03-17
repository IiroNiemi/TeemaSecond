package TeemaFirstAndSecond;

import static TeemaFirstAndSecond.TeemaFAS.ROUNDS;
import static TeemaFirstAndSecond.TeemaFAS.TEAMS;
import static TeemaFirstAndSecond.TeemaFAS.roundStack;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Iiro
 */
public class InitializeList {
    
    public static Random r = new Random();
    public static Date d = new Date();
    public static Date[] AllGameDates = {fromString("16.09.2016"),fromString("17.09.2016"),fromString("20.09.2016"),fromString("23.09.2016"),fromString("24.09.2016"),fromString("27.09.2016"),fromString("29.09.2016"),fromString("01.10.2016"),fromString("05.10.2016"),fromString("06.10.2016"),fromString("08.10.2016"),fromString("12.10.2016"),fromString("13.10.2016"),fromString("15.10.2016"),fromString("18.10.2016"), fromString("20.10.2016"),fromString("22.10.2016"),fromString("25.10.2016"),fromString("27.10.2016"),fromString("29.10.2016"),fromString("09.11.2016"),fromString("10.11.2016"),fromString("12.11.2016"),fromString("15.11.2016"),fromString("17.11.2016"),fromString("19.11.2016"),fromString("22.11.2016"),fromString("24.11.2016"),fromString("26.11.2016"),fromString("29.11.2016"),fromString("1.12.2016"),fromString("3.12.2016"),fromString("7.12.2016"),fromString("8.12.2016"),fromString("10.12.2016"),fromString("14.12.2016")};
    public static int[][] extragames ={{2,8},{8,1},{1,2},{3,13},{12,3},
                                    {13,12},{4,6},{6,5},{5,4},{7,11},
                                    {10,7},{11,10},{9,14},{15,9},{14,15}};
    //Lukitut ottelut indeksi-korjataan, eli vähennetään yksi numero jokaisesta
    public static int[][] lockedGames = {{14,8,1},{10,12,1},{4,6,1},{2,8,2},{4,12,2},{10,11,5},{3 ,7 ,6},{1 ,13,8},{14,9,8},{12,13,10},{13,12,11},{8,1 ,11},{9,15,11},{14,1,14},{10,13,14},{15,1,16},{6,4,16},{4,6,17},{2,1 ,17},{14,15,17},{9,14,19},{1,15,22},{6,1,23},{9,13,23},{1,14,25},{14,13,29},{1,8,31},{10,1,32},{6,14,32},{9,12,32},{12,2,33},{1,9,34},{15,9,35},{3,13,36},{6,15,4},{8,15,5},{11,9,4},{4,9,5},{8,4,13},{12,4,14},{14,6,13},{9,6,14},{6,12,15},{11,12,16},{11,8,18},{7,8,19},{9,11,24},{15,11,25},{12,7,25},{8,7,26},{15,6,27},{12,6,28},{9,8,27},{15,8,28},{4,14,31},{8,11,34},{12,11,35}};
    
    public static void StartList(){
        List roundList = new ArrayList();
        Match singleMatch = null;
        List retval = new ArrayList();
        
        for (int i = 0; i <= TEAMS; i++) {
            for (int j = i+1; j <= TEAMS-1; j++) {
                singleMatch = new Match(j, i, null, r.nextInt(ROUNDS));
                retval.add(singleMatch);

                singleMatch = new Match(i, j, null, r.nextInt(ROUNDS) );
                retval.add(singleMatch);
            }
        }
        
        //Lisätään Lisäpelit listaan
        /*
        for (int i = 0; i < extragames.length; i++) {
            for (int j = 0; j < 1; j++) {
                Match Ematch = new Match(extragames[i][j], extragames[i][j+1], null, r.nextInt(ROUNDS));
                for (int k = 0; k < retval.size(); k++) { //katsotaan ettei peli osu kahteen kertaan samalle kierrokselle
                    Match MO = (Match)retval.get(k);
                    while(MO.equals(Ematch) && MO.getRound() == Ematch.getRound()){
                        Ematch = new Match(extragames[i][j], extragames[i][j+1], null, r.nextInt(ROUNDS));
                    }
                }
                retval.add(Ematch);
            }
        }
        */
        
        //lisätään peleille päivämäärät listalta
        for (int i = 0; i < roundStack.length; i++) {
            roundStack[i] = new ArrayList();
            d = AllGameDates[i];
            
            for (int j = 0; j < retval.size(); j++) {
                singleMatch = (Match) retval.get(j);
                if(singleMatch.getRound() == i){
                    singleMatch.setDate(d);
                    roundList.add(singleMatch);
                }
            }
            roundStack[i].addAll(roundList);
            roundList.clear();
        }
        
        //Lukitaan määritetyt pelit
        
        for (int i = 0; i < ROUNDS; i++) {
            List RoundMatches = new ArrayList();
            RoundMatches.addAll(roundStack[i]);
            for (int j = 0; j < RoundMatches.size(); j++) {
                Match MO = (Match)RoundMatches.get(j);
                for (int k = 0; k < lockedGames.length; k++) {
                    if(MO.getHome() == lockedGames[k][0]-1 && MO.getVisitor() == lockedGames[k][1]-1){
                        int LockInt = lockedGames[k][2]-1;
                        Match newMatch = new Match(MO.getHome(), MO.getVisitor(), AllGameDates[LockInt], LockInt); // -1 on indeksikorjaus
                        
                        newMatch.setLockedToRow(true); //Ottelu on lukittu tälle kierrokselle
                        roundStack[LockInt].add(newMatch);
                        roundStack[i].remove(MO);
                    }
                }
            }
        }
        
        
        
        
    }

    
    
    
    //----Helper for parsing date----
    private static Date fromString( String pvm ) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        Date dateStr = null;
        try{
            dateStr = formatter.parse(pvm);    
        } catch(ParseException e){
            System.out.println(e.getMessage());
        }
        return dateStr;
    }
}
