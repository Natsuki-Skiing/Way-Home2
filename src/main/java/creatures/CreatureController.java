package creatures;
import java.util.HashMap;
import java.util.Vector;
import java.util.Random; 
import enums.*;
public class CreatureController {
    private static final Random randomGenerator = new Random();
    private HashMap<Integer,HashMap<enums.oppInfoEnum,Vector<Opp>>> aboveOppMap;
    private HashMap<Integer,HashMap<enums.oppInfoEnum,Vector<Opp>>> dunOppMap;

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

    private boolean isMonster(){
        if(randomGenerator.nextInt(1) == 1){
            return(true);
        }

        return(false);
    }

    private boolean isDungeonType(){
        if(randomGenerator.nextInt(1) == 1){
            return(true);
        }

        return(false);
    }
    public Opp getOpp(boolean dungeonType,double modifier){
        Opp opp = getOpp(getRank(), dungeonType, isMonster(),modifier);

        return(opp);
    }
    public Opp getOpp(int rank,boolean dungeonType,boolean monster,double modifier){
        Opp opp = null;
        HashMap<Integer,HashMap<enums.oppInfoEnum,Vector<Opp>>> oppMap =null;

        if(dungeonType){
            oppMap = this.dunOppMap;
        }else{
            oppMap = this.aboveOppMap;
        }
        if(oppMap.containsKey(rank)){
            HashMap<oppInfoEnum,Vector<Opp>> rankMap = oppMap.get(rank);
            oppInfoEnum type = oppInfoEnum.HUMANOID;
            if(monster){
                type = oppInfoEnum.MONSTER;
            }

            Vector<Opp> oppVector = rankMap.get(type);

            Opp baseOpp = oppVector.get(this.randomGenerator.nextInt((oppVector.size()-1)));

            opp = new Opp(baseOpp,modifier);

        }



        return(opp);
    }




}
