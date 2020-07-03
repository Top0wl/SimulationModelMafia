package sample;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Time;
import java.util.*;

import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import sample.Players.Black.BlackPeople;
import sample.Players.Black.Mafia;
import sample.Players.Human;
import sample.Players.Red.Civilian;
import sample.Players.Red.Commissioner;
import sample.Players.Red.RedPeople;

import javax.imageio.ImageIO;

public class Controller implements Initializable {
    int animation = 0;
    boolean flag = false;
    boolean firstday = true;
    int Counter = 0;
    public ArrayList<Human> Players = new ArrayList<Human>();
    public ArrayList<sample.Players.Red.RedPeople> RedPeople  = new ArrayList<RedPeople>();
    public ArrayList<sample.Players.Black.BlackPeople> BlackPeople = new ArrayList<BlackPeople>();
    public boolean IsDay = true;
    public boolean IsNight = false;

    @FXML
    public ImageView BackGround1;

    @FXML
    public ImageView BackGround2;

    @FXML
    private TextArea Logs;

    @FXML
    public VBox v1;

    @FXML
    public VBox v2;

    @FXML
    public Button Next;

    @FXML
    void next(ActionEvent event) {
            //Проверка, может быть кто выйграл???
            if (!WhoIsWin()) {

                //Апдейтить списки всех игроков...(Идея расписана в методе)
                UpdateBlackAndRedLists();

                //Cмена дня ночи
                NightToDay();
                DayToNight();

                Animation();


                if (IsDay) {
                    //Если это не не начало игры
                    if (flag) {
                        //Жители выбирают кого исключить

                        //Если коммиссар нашёл мафию он вскрывается
                        for (var commissioner : Players) {
                            commissioner.Play(Players, Logs);
                        }

                        SearchSuspect();

                    } else {
                        int Total = Players.get(Counter).Say();
                        Logs.appendText("Высказывается игрок под номером " + (Players.get(Counter).NumHuman + 1) + "\n");

                        Logs.appendText("Игрок " + (Players.get(Counter).NumHuman + 1) + " высказался с подозрительностью :" + Total + "%\n");

                        //Все люди детектят данную персону
                        Human.DetectPerson(Players.get(Counter).NumHuman, Total, Players, Logs);
                        Counter++;

                    }

                }

                if (IsNight) {
                    //Просыпается Мафия
                    MafiaMakesChoice();


                    //Просыпается коммисар
                    CommissionerMakesChoice();
                }
            }
      //  }
     }

    public static void Role(String[] players, String s, int count) {
        Random random = new Random();
        int RandomNumber = random.nextInt(10);
        for (int i = 0; i < count; i++)
        {
            while (players[RandomNumber] != null)
            {
                RandomNumber = random.nextInt(10);
            }
            players[RandomNumber] = s;
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Image q = null;
        try {
            q = new Image(new FileInputStream("SbyiCgxMacw.jpg"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BackGround1.setImage(q);
        try {
            q = new Image(new FileInputStream("pMS-kEdzwPM.jpg"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BackGround2.setImage(q);


        int i = 1;
        boolean flag = true;
        String[] ArrayPlayers = new String[10];

        Role(ArrayPlayers ,"Mafia", 2);
        //Коммисар
        Role(ArrayPlayers ,"Commissioner", 1);
        //Мирный житель
        Role(ArrayPlayers ,"Civilian", 7);

        for (var item: ArrayPlayers) {
            Random random = new Random();
            int r1 = random.nextInt(9) + 1;
            int r2 = random.nextInt(9) + 1;
            int r3 = random.nextInt(9) + 1;
            int r4 = random.nextInt(9) + 1;
            switch (item)
            {
                case ("Mafia"):
                {
                    Mafia mafia = null;
                    if(flag) {
                        mafia = new Mafia(r1, r2, r3, r4, ("Игрок " + i + " Mafia"), "Mafia1");
                        flag = false;
                    }
                    else
                    {
                        mafia = new Mafia(r1, r2, r3, r4, ("Игрок " + i + " Mafia"), "Mafia2");
                    }
                    Players.add(mafia);
                    BlackPeople.add(mafia);
                    if(v1.getChildren().size() != 5)
                    {
                        v1.getChildren().add(mafia.controlHuman);
                    }
                    else {
                        v2.getChildren().add(mafia.controlHuman);
                    }
                    break;
                }
                case ("Civilian"):
                {
                    Civilian civilian = new Civilian(r1,r2,r3,r4, ("Игрок " + i + " Civilian"),"Civillian" );
                    Players.add(civilian);
                    RedPeople.add(civilian);
                    if(v1.getChildren().size() != 5)
                    {
                        v1.getChildren().add(civilian.controlHuman);
                    }
                    else {
                        v2.getChildren().add(civilian.controlHuman);
                    }
                    break;
                }
                case ("Commissioner"):
                {
                    Commissioner commissioner = new Commissioner(r1,r2,r3,r4, ("Игрок " + i + " Commissioner"), "Commissioner");
                    Players.add(commissioner);
                    RedPeople.add(commissioner);
                    if(v1.getChildren().size() != 5)
                    {
                        v1.getChildren().add(commissioner.controlHuman);
                    }
                    else {
                        v2.getChildren().add(commissioner.controlHuman);
                    }
                    break;
                }
            }
            i++;
        }

        /* Пронумеровать игроков */
        Human.NumHumans(Players);

    }

    public void Kill(int i, ArrayList<Human> players, ArrayList<RedPeople> redPeople, ArrayList<BlackPeople> blackPeople) {

        if(i < 5) {
            for (var item : Players) {
                if (item.NumHuman == i) {
                    v1.getChildren().remove(item.controlHuman);
                    players.remove(item);
                    break;
                }
            }
        }
        else {
            for (var item : Players) {
                if (item.NumHuman == i) {
                    v2.getChildren().remove(item.controlHuman);
                    players.remove(item);
                    break;
                }
            }
        }

        for (var item: players) {
            for (var item2: item.MafiaList) {
                if(item2 == i)
                {
                    item.MafiaList.remove(item2);
                }
            }
        }

        for (var item : redPeople) {
            if (item.NumHuman == i) {
                redPeople.remove(item);
                break;
            }
        }

        for (var item : blackPeople) {
            if (item.NumHuman == i) {
                blackPeople.remove(item);
                break;
            }
        }



        for (var item : players) {
            for (var iteminblacklist : item.BlackList) {
                if (iteminblacklist.numperson == i) {

                    item.BlackList.remove(iteminblacklist);

                    Label l = new Label((i + 1) + "(" + iteminblacklist.total + ")");

                    for (var rd : item.controlHuman.BlackList.getChildren()) {
                        String var1 = l.getText();
                        Label temp = (Label) rd;
                        String var2 = temp.getText();
                        if (var1.equals(var2)) {
                            item.controlHuman.BlackList.getChildren().remove(rd);
                            break;
                        }
                    }


                    break;
                }
            }
            for (var iteminredlist : item.RedList) {
                if (iteminredlist.numperson == i) {
                    item.RedList.remove(iteminredlist);

                    Label l = new Label((i + 1) + "(" + iteminredlist.total + ")");

                    for (var rd : item.controlHuman.RedList.getChildren()) {
                        String var1 = l.getText();
                        Label temp = (Label) rd;
                        String var2 = temp.getText();
                        if (var1.equals(var2)) {
                            item.controlHuman.RedList.getChildren().remove(rd);
                            break;
                        }
                    }
                    //item.controlHuman.RedList.getChildren().remove(l);
                    break;
                }
            }
        }
    }

    public boolean WhoIsWin() {
        if(sample.Players.Red.RedPeople.IsWin(BlackPeople,RedPeople)) {
            Logs.appendText("____________________________________\n");
            Logs.appendText("......Мирные жители победили!.......\n");
            Logs.appendText("____________________________________\n");
            Next.setVisible(false);
            return true;
        }
        if(sample.Players.Black.BlackPeople.IsWin(BlackPeople,RedPeople)) {
            Logs.appendText("____________________________________\n");
            Logs.appendText("...........Мафия победила!..........\n");
            Logs.appendText("____________________________________\n");
            Next.setVisible(false);
            return true;
        }
        return false;
    }

    public void UpdateBlackAndRedLists() {
        //В 1 день, люди только начинают заполнять свои списки и поэтому апдейтить их не логично
        //И свайп будет работать не коректно
        if(!firstday) {
            //Апдейтаем списки людей, по правилу блэклист не может быть пустым
            for(var item: Players)
            {
                //Идея такова, если человек всем поверил, то последним 2 людям в ред листе он будет доверять меньше всего
                //Поэтому мы просто закидываем их в блэк лист...
                if(item.BlackList.size() == 0 && item.RedList.size() != 0) {
                    item.SwipeRedToBlack();
                }
            }
        }
    }

    public void NightToDay() {
       if(IsNight) {
           Logs.appendText("____________________________________\n");
           Logs.appendText("...........Наступает день...........\n");
           IsDay = true;
           IsNight = false;
           flag = true;
           animation = 2;
       }

   }

    public void DayToNight() {
        //Тут счётчик - фигня полная, в будущем нужно избавиться от него
        //Оне временный, так быстрее, но менее читабельнее и удобнее.
        if(Counter == Players.size()) {
            Counter = 0;
            if(IsDay)
            {
                Logs.appendText("____________________________________\n");
                Logs.appendText("...........Наступает ночь...........\n");
                IsDay = false;
                IsNight = true;
                firstday = false;
                animation = 1;
            }
        }
    }

    public void SearchSuspect() {
        Integer array[] = new Integer[10];
        for (int i = 0; i < array.length; i++) {
            array[i] = 0;
        }

        //Заполняем массив доверием
        for (var player: Players) {
            player.ChoiceHuman(array);
        }
        int maximum = -100000;
        //Далее нужно найти человека которому максимально не доверяют
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null && array[i] != 0) {
                if (maximum < array[i]) {
                    maximum = array[i];
                }
            }
        }

        for (int i = 0; i < array.length; i++) {
            if (array[i] == maximum) {
                Kill(i, Players, RedPeople, BlackPeople);
                Logs.appendText("Жители проголосовали, чтобы посадили " + (i + 1) + "-ого человека\n");
                flag = false;
                break;
            }
        }
    }

    public void MafiaMakesChoice() {
        int i = 0;
        int j = 0;
        int choiceNumber[] = new int[2];
        int choiceTotal[] = new int[2];
        //Просыпается мафия и голосует
        for (var mafia : Players) {
            if(mafia.Role.equals("Mafia"))
            {
                choiceNumber[i] = mafia.СhoiceHuman(RedPeople);
                choiceTotal[j++] = mafia.ChoiceHumanRating(RedPeople);
                Logs.appendText((mafia.NumHuman + 1) + " Мафия предлагает убить " + (choiceNumber[i++] + 1) + " человека т.к. этот человек его подозревает" + "\n");
            }
        }
        //Мафия делает групповой выбор кого убить
        if(choiceTotal[0] > choiceTotal[1]) { //Мафия убивает всё-таки 1 выбор
            for (var mafia : Players) {
                if(mafia.Role.equals("Mafia"))
                {
                    //Если номер из первых 5 игроков, просто у нас 2 поля где находятся игроки v1,v2
                    if(choiceNumber[0] < 5) {
                        for (var item : Players) {
                            if (item.NumHuman == choiceNumber[0]) {
                                v1.getChildren().remove(item.controlHuman);
                                break;
                            }
                        }
                    }
                    else {
                        for (var item : Players) {
                            if (item.NumHuman == choiceNumber[0]) {
                                v2.getChildren().remove(item.controlHuman);
                                break;
                            }
                        }
                    }
                    mafia.Kill(choiceNumber[0], Players, RedPeople);

                    Logs.appendText("Мафия убивает " + (choiceNumber[0] + 1) + "человека" + "\n");
                    Logs.appendText("____________________________________\n");
                    break;
                }
            }
        } /*Мафия убивает всё-таки 1 выбор*/
        else {
            for (var mafia : Players) {
                if(mafia.Role.equals("Mafia"))
                {
                    //Аналогично выше
                    if(choiceNumber[1] < 5) {
                        for (var item : Players) {
                            if (item.NumHuman == choiceNumber[1]) {
                                v1.getChildren().remove(item.controlHuman);
                            }
                        }
                    }
                    else {
                        for (var item : Players) {
                            if (item.NumHuman == choiceNumber[1]) {
                                v2.getChildren().remove(item.controlHuman);
                            }
                        }
                    }

                    mafia.Kill(choiceNumber[1], Players, RedPeople);

                    Logs.appendText("Мафия убивает " + (choiceNumber[1] + 1) + " человека" + "\n");
                    Logs.appendText("____________________________________\n");
                    break;
                }
            }
        } /*Мафия убивает всё-таки 2 выбор*/
    }

    public void CommissionerMakesChoice() {
        for (var commissioner : Players) {
            if(commissioner.Role.equals("Commissioner"))
            {
                Logs.appendText("____________________________________\n");
                //Комиссар выбирает человека
                Logs.appendText("Коммиссар хочет проверить игрока " + (commissioner.ChoiceHuman() + 1) + "\n");

                for (var item: Players) {
                    if(item.NumHuman == commissioner.ChoiceHuman())
                        if(item.Role.equals("Mafia")) {
                            Logs.appendText("Коммиссар нашёл мафию, она под игроком " + (commissioner.ChoiceHuman() + 1)  + "\n");
                            commissioner.MafiaList.add(commissioner.ChoiceHuman());
                            break;
                        } else {
                            Logs.appendText("Коммиссар проверил игрока " + (commissioner.ChoiceHuman() + 1) + " и выяснил что он не мафия" + "\n");
                            commissioner.RedList.add(commissioner.BlackList.get(0));
                            commissioner.BlackList.remove(0);
                            break;
                        }
                }
                Logs.appendText("____________________________________\n");
            }
        }
    }

    public void Animation()
    {

        if(animation == 1) {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(2000), BackGround2);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);

            fadeOut.setAutoReverse(false);
            fadeOut.play();
            animation = 0;
        }
        if(animation == 2) {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(2000), BackGround2);
            fadeOut.setFromValue(0.0);
            fadeOut.setToValue(1.0);

            fadeOut.setAutoReverse(false);
            fadeOut.play();
            animation = 0;
        }

         /*

        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), BackGround2);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setCycleCount(Timeline.INDEFINITE);
        fadeOut.setAutoReverse(true);
        fadeOut.play();

         */

    }

    public Image createImage(String path)
    {
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Image img = new Image(String.valueOf(image));
        return img;
    }

}
