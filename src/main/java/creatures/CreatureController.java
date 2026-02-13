package creatures;
import java.util.HashMap;
import java.util.Vector;
import java.util.Random; 
import enums.*;
public class CreatureController {
    private static final Random randomGenerator = new Random();
    private HashMap<Integer,HashMap<enums.oppInfoEnum,Vector<Opp>>> oppMap;


    private void applyVetrancy(Opp opp){
        int number = randomGenerator.nextInt(100)+1;
        double modifier =0.0;
        String name = null;
        if(number > 75 && number <=85 ){
            modifier = 0.65;
            name = "Weaker";
        }else if(number >85 && number <=95 ){
            modifier = 1.50;
            name = "Enranged";
        }else if(number > 95){
            modifier = 2.0;
            name = "Mythic";
        }else{
            return;
        }

        opp.setName(name +" "+opp.getName());

        opp.setStrength((int) (opp.getStrength() * modifier));
        opp.setPerception((int) (opp.getPerception() * modifier));
        opp.setEndurance((int) (opp.getEndurance() * modifier));
        opp.setCharisma((int) (opp.getCharisma() * modifier));
        opp.setAgility((int) (opp.getAgility() * modifier));
        opp.setLuck((int) (opp.getLuck() * modifier));
        opp.setMaxHp((int) (opp.getMaxHp() * modifier));

        opp.setDeathXp((int)(opp.getDeathXp()*modifier));


        
        
    }
    private int getRank(){
        int number = randomGenerator.nextInt(100);
        int rank;
        if(number < 40){
            rank =1;
        }else if(number >40 && number <=65 ){
            rank =2;
        }else if(number >65 && number <= 80){
            rank =3;
        }else if(number >80 && number <= 92 ){
            rank = 4;
        }else{
            rank = 5;
        }

        return(rank);
    }
    public Opp getOpp(int rank,boolean dungeonType,boolean monster){
        Opp opp = new Opp(null, 0, 0, 0, 0, 0, 0, 0, null, 0, null);



        return(opp);
    }




}
