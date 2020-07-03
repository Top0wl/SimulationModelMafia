package sample.Players.Black;

import javafx.scene.control.Label;
import javafx.scene.text.Font;
import sample.Players.Human;
import sample.Players.Red.RedPeople;

import java.util.ArrayList;
import java.util.Random;

public class Mafia extends BlackPeople {
    public Mafia(int Eloq, int Exp, int Log, int Intuit, String name, String mafia1) {
        super(Eloq, Exp, Log, Intuit, name, mafia1);
    }

    public int ChoiceHumanRating(ArrayList<RedPeople> RedPeoples) {
        Integer array[] = new Integer[10];
        for (int i = 0; i < array.length; i++) {
            array[i] = 0;
        }


        for (var human: RedPeoples) {
            int q = 0;
            int flag = 0;
            for (var BL: human.BlackList) {
                if(BL.numperson == this.NumHuman) {
                    flag = BL.total;
                }
                else
                {
                    q++;
                }
            }
            array[human.NumHuman] = flag;
        }

        int maximum = array[0];

        for (var item: array) {
            if (maximum < item) {
                maximum = item;
            }
        }

        return maximum;
    }

    public int СhoiceHuman(ArrayList<RedPeople> RedPeoples) {
        int k = 0;
        //Мафия ищет цель
        //Для этого она смотрит блеклисты людей, и где она она находится в наибольшем приоритете
        //того она и выдвигает в кандидатуру на убийство
        //Массив флажков для людей
        Integer array[] = new Integer[10];


        for (var human: RedPeoples) {
            //int q = 0;
            int flag = 0;
            for (var BL: human.BlackList) {
                if(BL.numperson == this.NumHuman) {
                    flag = BL.total;
                }
               //  else { q++; }
            }
            array[human.NumHuman] = flag;
        }
        while(array[k] == null)
        {
            k++;
        }
        int maximum = array[k];
        //
        //
        //
        int sum = 0;
        for (var item: array) {
            if(item != null) {
                sum += item;
            }
        }
        //
        //
        //Если нет людей подозревающих мафию:
        if(sum == 0) {
            Random random = new Random();
            int i;
            do {
                i = random.nextInt(10);
            }
            while (array[i] == null);
            return i;
        } else {

            for (int i = 0; i < array.length; i++) {
                if (array[i] != null) {
                    if (maximum < array[i]) {
                        maximum = array[i];
                    }
                }
            }

            for (int i = 0; i < array.length; i++) {
                if (array[i] != null) {
                    if (array[i] == maximum) {
                        return i;
                    }
                }
            }
        }
        return 0;
    }

    public void Kill(int i, ArrayList<Human> players, ArrayList<RedPeople> redPeople) {
        //Перебираем массив людей и убиваем i-ую персону
        for (var item: players) {
            if (item.NumHuman == i)
            {
                players.remove(item);
                item.controlHuman = null;
                break;
            }
        }

        //Перебираем массив красных людей и убиваем i-ую персону
        for (var item: redPeople) {
            if (item.NumHuman == i)
            {
                redPeople.remove(item);
                item.controlHuman = null;
                break;
            }
        }
        //Перебираем массив людей и редактируем
        for(var item: players)
        {
            item.DeleteInBlackList(i);
            item.DeleteInRedList(i);
        }
    }
}
