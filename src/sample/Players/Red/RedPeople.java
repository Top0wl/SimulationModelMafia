package sample.Players.Red;

import sample.Players.Black.BlackPeople;
import sample.Players.Human;

import java.util.ArrayList;

public class RedPeople extends Human {
    public RedPeople(int Eloq, int Exp, int Log, int Intuit, String name, String s) {
        super(Eloq, Exp, Log, Intuit, name, s);
    }


    public static boolean IsWin(ArrayList<BlackPeople> BlackPeople, ArrayList<RedPeople> RedPeople)
    {
        if(BlackPeople.size() == 0) {
            return true;
        }
        return false;
    }
}
