package sample.Players.Black;

import sample.Players.Human;
import sample.Players.Red.RedPeople;

import java.util.ArrayList;

public class BlackPeople extends Human {
    public BlackPeople(int Eloq, int Exp, int Log, int Intuit, String name, String s) {
        super(Eloq, Exp, Log, Intuit, name, s);
    }



    public static boolean IsWin(ArrayList<BlackPeople> BlackPeople, ArrayList<RedPeople> RedPeople)
    {
        if(BlackPeople.size() == RedPeople.size()) {
            return true;
        }
        return false;
    }

}
