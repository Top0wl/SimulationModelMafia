package sample.Players.Red;

import javafx.scene.control.TextArea;
import sample.Players.Human;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Commissioner extends RedPeople {


    public Commissioner(int Eloq, int Exp, int Log, int Intuit, String name, String commissioner) {
        super(Eloq, Exp, Log, Intuit, name, commissioner);
    }

    public void Play(TextArea Logs, ArrayList<Human> Players)
    {
        //ЕСЛИ У КОМИССАРА В ЛИСТЕ 1 МАФИЯ
        if (MafiaList.size() == 1) {
            Logs.appendText("Игрок " + (NumHuman + 1) + " вскрывается под ролью коммисара и говорит, что он нашёл мафию под номером: " + (MafiaList.get(0) + 1) + "\n");
            for (var item : Players) {
                if (item.NumHuman == MafiaList.get(0)) {
                    Logs.appendText("Игрок " + (MafiaList.get(0) + 1) + " начинает оправдываться \n");
                    Human.DetectPerson(MafiaList.get(0), item.Say(), Players, Logs);
                }
            }
        }
        //ЕСЛИ У КОМИССАРА В ЛИСТЕ 2 МАФИИ
        if (MafiaList.size() == 2) {
            Logs.appendText("Игрок " + (NumHuman + 1) + " вскрывается под ролью коммисара и говорит, что он нашёл мафию под номером: " + (MafiaList.get(0) + 1) + " и " + (MafiaList.get(1) + 1) + "\n");
            for (var item : Players) {
                if (item.NumHuman == MafiaList.get(0)) {
                    Logs.appendText("Игрок " + (MafiaList.get(0) + 1) + " начинает оправдываться \n");
                    Human.DetectPerson(MafiaList.get(0), item.Say(), Players, Logs);
                }
                if (item.NumHuman == MafiaList.get(1)) {
                    Logs.appendText("Игрок " + (MafiaList.get(1) + 1) + " начинает оправдываться \n");
                    Human.DetectPerson(MafiaList.get(1), item.Say(), Players, Logs);
                }
            }
        }

    }
}
