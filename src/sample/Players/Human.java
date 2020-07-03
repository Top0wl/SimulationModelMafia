package sample.Players;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import sample.MyCustomController.ElementControlHuman;
import sample.Players.Red.RedPeople;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class Human {


    public class NodeForSort
    {
        public int total;
        public int numperson;

        public NodeForSort(int t, int n) {total = t; numperson = n;}
        public int compareTo(NodeForSort node) { return node.total - this.total; }
    };

    /* Характеристики персонажей */

    public String Role;
    public int NumHuman;

    //Характеристики которые играют роль при разговоре выступлении

    public int Eloquence;   /* Красноречие --- 0.5*/
    public int Experience;  /*  Опыт игры  --- 0.5 */

    //Характеристики которые играют роль при вычислении мафии

    public int Logic;       /*  Логика  --- 0.4*/
    public int Intuition;   /* Интуиция --- 0.2*/
                            /*  Опыт игры  --- 0.4 */

    //Листы

    public List<Integer> MafiaList = new ArrayList<Integer>();
    public List<NodeForSort> BlackList = new ArrayList<NodeForSort>();
    public List<NodeForSort> RedList = new ArrayList<NodeForSort>();

    //Елемент контроля на гуи, самописаная штука...

    public ElementControlHuman controlHuman = new ElementControlHuman();

    /**** Конструкторы ****//**********************//**********************//**********************/
    public Human(int Eloq, int Exp, int Log, int Intuit, String name) {
        this.Eloquence = Eloq;
        controlHuman.PlayerEloquence.setText(controlHuman.PlayerEloquence.getText() + " " + Eloq);
        this.Experience = Exp;
        controlHuman.PlayerExperience.setText(controlHuman.PlayerExperience.getText() + " " + Exp);
        this.Logic = Log;
        controlHuman.PlayerLogic.setText(controlHuman.PlayerLogic.getText() + " " + Log);
        this.Intuition = Intuit;
        controlHuman.PlayerIntuition.setText(controlHuman.PlayerIntuition.getText() + " " + Intuit);

        controlHuman.PlayerName.setText(name);

        String[] words = name.split(" ");

        Role = words[2];

        Image q = null;
        try {
            q = new Image(new FileInputStream("C:\\Users\\semen\\Videos\\PicsArt_04-05-07.36.12.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        controlHuman.PlayerImage.setImage(q);
    }
    public Human(int Eloq, int Exp, int Log, int Intuit, String name, String imagename) {
        this.Eloquence = Eloq;
        controlHuman.PlayerEloquence.setText(controlHuman.PlayerEloquence.getText() + " " + Eloq);
        this.Experience = Exp;
        controlHuman.PlayerExperience.setText(controlHuman.PlayerExperience.getText() + " " + Exp);
        this.Logic = Log;
        controlHuman.PlayerLogic.setText(controlHuman.PlayerLogic.getText() + " " + Log);
        this.Intuition = Intuit;
        controlHuman.PlayerIntuition.setText(controlHuman.PlayerIntuition.getText() + " " + Intuit);

        controlHuman.PlayerName.setText(name);

        String[] words = name.split(" ");

        Role = words[2];

        Image q = null;
        try {
            q = new Image(new FileInputStream(imagename + ".jpg"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        controlHuman.PlayerImage.setImage(q);
    }
    /**********************//**********************//**********************//**********************/


    /** Методы действий человека **/

    public void ChoiceHuman(Integer[] array) {
        for (var item: BlackList) {
            array[item.numperson] += item.total;
        }
        for (var item: RedList) {
            array[item.numperson] -= item.total;
        }
    }

    public int ChoiceHuman() {
        return BlackList.get(0).numperson;
    }

    public int Say() {
        Random random = new Random();

        //Подсчёт рейтинга высказывания

        //Формула 1 - Берём рандомное число и уможаем на красноречие( 58 * 0.8)
        int l = random.nextInt(100);

        float ball1 = l * Eloquence / 10;
        l = random.nextInt(100);
        float ball2 = l * Experience / 10;

        //Итог высказывания

        float Total = (ball1 + ball2) / 200;

        //Тоесть Total будет показывать насколько человек не мафия,
        //Переделаем обратное выражение
        Total = 1 - Total;
        Total *= 100;

        return (int)Total;


    }

    public void CheckPerson(int Total, int NumPerson, TextArea Logs) {
        Random random = new Random();
        float koef = (float) ((Logic * 0.4) + (Experience * 0.4) + (Intuition * 0.2)) / 10;
        float detect = Total * koef;

        //Тоесть игрок item не поверил игроку который высказался
        //Своеобразный рандом, тоесть если человек высказался с подозрительностью 77% ему можно поверить а можно не поверить
        //Если мы ему не поверили тогда нужно узнать насколько не поверили, тоесть прочитать человека на его подозрительность исходя из
        int ball = random.nextInt(100);

        //Если игрок высказался подозрительно то
            //Проверить если он в блек листе, то повысить ему приоритет
            //Если же он в ред листе, тогда понизить ему приоритет
                //Если приоритет <0 то оправить его в блек лист

        //Если игрок высказался не подорительно, то
            //Если же он в ред листе то повысить ему приоритет
            //Проверить если он в блек листе, то понизить ему приоритет
                //Если приоритет <0 то оправить его в ред лист


        //Если игрок высказался подозрительно то
        if(ball <= detect)
        {
            NodeForSort q = new NodeForSort((int)detect, NumPerson);

            //Проверить если он в блек листе, то повысить ему приоритет
            if(CheckList(q, BlackList))
            {
                int index = CheckListIndex(q, BlackList);
                BlackList.get(index).total += detect;
                Logs.appendText("Игрок " + (NumHuman + 1) + " повысил приоритет подозрительности на игрока " + (NumPerson + 1) + " с подозрительностью " + BlackList.get(index).total + " балов\n");

                //Если его подозрительность меньше 0, то это превращается в доверие
                if(BlackList.get(index).total <= 0) {
                    BlackList.get(index).total *= (-1);
                    RedList.add(BlackList.get(index));
                    BlackList.remove(index);
                }
            }
            else {
                //Если же он в ред листе, тогда понизить ему приоритет
                if(CheckList(q, RedList))
                {
                    int index = CheckListIndex(q,RedList);
                    RedList.get(index).total -= detect;

                    //Если его доверие меньше 0, то это превращается в подозрительность
                    if(RedList.get(index).total <= 0)  {
                        RedList.get(index).total *= (-1);
                        Logs.appendText("Игрок " + (NumHuman + 1) + " повысил приоритет подозрительности на игрока " + (NumPerson + 1) + " с подозрительностью " + RedList.get(index).total + " балов\n");
                        BlackList.add(RedList.get(index));
                        RedList.remove(index);
                    }
                    else {
                        Logs.appendText("Игрок " + (NumHuman + 1) + " повысил приоритет подозрительности на игрока " + (NumPerson + 1) + " с подозрительностью " + RedList.get(index).total + " балов\n");
                    }
                }
                else {
                    //Добавить этого человека в коллекция блэклиста
                    BlackList.add(q);
                    controlHuman.AddInBlackList(NumPerson);
                    Logs.appendText("Игрок " + (NumHuman + 1) + " распознал игрока " + (NumPerson + 1) + " с подозрительностью " + (int) detect + "%\n");
                }
            }
        }
        //Если игрок высказался не подорительно, то
        else {
            NodeForSort q = new NodeForSort((int)detect, NumPerson);
            //Проверить если он в ред листе то повысить ему приоритет
            if(CheckList(q, RedList))
            {
                int index = CheckListIndex(q,RedList);
                RedList.get(index).total += detect;
                Logs.appendText("Игрок " + (NumHuman + 1) + " повысил приоритет доверия на игрока " + (NumPerson + 1) + " с доверием " + RedList.get(index).total + " балов\n");
            }
            else {
                //Проверить если он в блек листе, то понизить ему приоритет
                if (CheckList(q, BlackList)) {
                    int index = CheckListIndex(q, BlackList);
                    BlackList.get(index).total -= detect;

                    //Если приоритет <0 то оправить его в ред лист
                    if (BlackList.get(index).total <= 0) {
                        BlackList.get(index).total *= (-1);
                        Logs.appendText("Игрок " + (NumHuman + 1) + " повысил приоритет доверия на игрока " + (NumPerson + 1) + " с доверием " + BlackList.get(index).total + " балов\n");
                        RedList.add(BlackList.get(index));
                        BlackList.remove(index);
                    }
                    else {
                        Logs.appendText("Игрок " + (NumHuman + 1) + " повысил приоритет доверия на игрока " + (NumPerson + 1) + " с доверием " + BlackList.get(index).total + " балов\n");
                    }
                } else {
                    RedList.add(q);

                    controlHuman.AddInRedList(NumPerson);

                    Logs.appendText("Игрок " + (NumHuman + 1) + " доверяет игроку " + (NumPerson + 1) + " с доверием " + (int) detect + "%\n");
                }
            }
        }
        UpdateLists();
    }


    /**** Методы для работы с листами ****/

    public void DeleteInBlackList(int NumPerson) {
        for (var iteminblacklist : BlackList) {

            if (iteminblacklist.numperson == NumPerson) {

                BlackList.remove(iteminblacklist);

                controlHuman.DeleteInBlackList(NumPerson, iteminblacklist.total);

                break;
            }
        }
    }

    public void DeleteInRedList(int NumPerson) {
        for (var iteminredlist : RedList) {

            if (iteminredlist.numperson == NumPerson) {

                RedList.remove(iteminredlist);

                controlHuman.DeleteInRedList(NumPerson, iteminredlist.total);

                break;
            }
        }
    }

    public void SwipeRedToBlack() {
        BlackList.add(RedList.get(RedList.size() - 1));

        controlHuman.SwipeRedToBlack((RedList.get(RedList.size() - 1).numperson), (RedList.get(RedList.size() - 1).total));

        RedList.remove(RedList.size() - 1);

    }

    public boolean CheckList(NodeForSort node, List<NodeForSort> list) {
        for (var item: list) {
            if(item.numperson == node.numperson) return true;
        }
        return false;
    }

    public int CheckListIndex(NodeForSort node, List<NodeForSort> list) {
        int index = 0;
        for (var item: list) {
            if(item.numperson == node.numperson) return index;
            index++;
        }
        return -1;
    }

    public void UpdateLists() {
        Collections.sort(BlackList, NodeForSort::compareTo);
        Collections.sort(RedList, NodeForSort::compareTo);

        controlHuman.BlackList.getChildren().clear();
        for (var item : BlackList) {
            Label l2 = new Label(Integer.toString(item.numperson + 1) + "(" + item.total + ")");
            l2.setStyle("-fx-border-color: #000000;" + "-fx-border-width: 2;");
            l2.setFont(new Font("System Bold", 13));
            controlHuman.BlackList.getChildren().add(l2);
        }

        controlHuman.RedList.getChildren().clear();
        for (var item : RedList) {
            Label l2 = new Label(Integer.toString(item.numperson + 1) + "(" + item.total + ")");
            l2.setStyle("-fx-border-color: DarkRed;" + "-fx-border-width: 2;" + "-fx-text-fill: #8b0000;");
            l2.setFont(new Font("System Bold", 13));
            controlHuman.RedList.getChildren().add(l2);
        }

    }

    /**************************************/


    /*** Статические методы для увелечения читаемости кода... ***/

    public static void DetectPerson(int NumPerson, int Total, ArrayList<Human> Players, TextArea Logs) {
        for (var item: Players) {

            if(item.NumHuman != NumPerson)
            {
                for (var item2 : Players) {
                    if(item2.NumHuman == NumPerson)
                    {
                        item.CheckPerson(Total, item2.NumHuman, Logs);
                    }
                }
            }
        }
    }
    public static void NumHumans(ArrayList<Human> Players) {
        int i = 0;
        for (var item: Players) {
            item.NumHuman = i;
            i++;
        }
    }

    /************************************************************/


    /**     Методы для полиморфизма      **/

    public int СhoiceHuman(ArrayList<RedPeople> redPeople) { return 0; }
    public int ChoiceHumanRating(ArrayList<sample.Players.Red.RedPeople> redPeople) { return 0; }
    public void Kill(int i, ArrayList<Human> players, ArrayList<RedPeople> redPeople) { };
    public int Play(ArrayList<RedPeople> RedPeoples) {
        return 0;
    };
    public int Play(ArrayList<Human> Players, TextArea Logs) {
        return 0;
    }

    /**************************************/
}
