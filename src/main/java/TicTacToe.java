import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

public class TicTacToe {
    public static final char DOT_O = 'O';
    public static final char DOT_X = 'X';
    public static final char DOT_EMPTY = ' ';
    public static char[][] fieldCell;
    public static int[] aiLogicBlock = {0, 0};
    public static int humanScore;
    public static int aiScore;
    public static char humanDot;
    public static char aiDot;
    public static int sizeX;
    public static int sizeY;
    public static Scanner scanner = new Scanner(System.in);
    public static Random random = new Random();
    public static int dotToWin;

    public static void main(String[] args) {
        startNewGame();
    }

    public static void startNewGame() {
        while (true) {
            roundStart();
            System.out.printf("Игра окончена со счетом %d (Ваш), %d (Компьютера)\n", humanScore, aiScore);
            System.out.println("Хотите сыграть еще раз? Y/N");
            if (!scanner.next().toLowerCase(Locale.ROOT).equals("y")){
                System.out.println("Хорошего дня!");
                break;
            }
        }
    }

    public static void roundStart(){
        createField();
        drawField();
        chooseYourDot();
        chooseAmountDotsToWin();
        boolean checkMove = chooseFirstTurn();
        drawField();
        if (checkMove) {
            humanFirst();
        } else {
            aiFirst();
        }
    }

    public static void humanFirst(){
        while(true){
            humanTurn();
            if (gameCheck(humanDot)) {
                break;
            }
            aiTurn();
            if (gameCheck(aiDot)){
                break;
            }
        }
    }

    public static void aiFirst(){
        while(true){
            aiTurn();
            if (gameCheck(aiDot)) {
                break;
            }
            humanTurn();
            if (gameCheck(humanDot)){
                break;
            }
        }
    }

    public static void createField(){
        System.out.print("Введите размер поля по у>>>>>");
        do{
            sizeY = scanner.nextInt();
            if (sizeY < 3 || sizeY > 9) {
                System.out.println("Минимальный размер поля 3 на 3, Максимальный 9 на 9");
                // ограничение максимального необходимо для правильных табуляций в консоли.
            }
        } while (!(sizeY >= 3 && sizeY <= 9));
        System.out.println();
        System.out.print("Введите размер поля по x>>>>>");
        do{
            sizeX = scanner.nextInt();
            if (sizeX < 3 || sizeX > 9) {
                System.out.println("Минимальный размер поля 3 на 3, Максимальный 9 на 9");
            }
        } while (!(sizeX >= 3 && sizeX <= 9));
        System.out.println();
        System.out.printf("Размер вашего поля %d на %d\n", sizeY, sizeX);
        fieldCell = new char[sizeY][sizeX];
        for (int i = 0; i < sizeY; i++) {
            for (int j = 0; j < sizeX; j++) {
                fieldCell[i][j] = DOT_EMPTY;
            }
        }
    }

    public static void chooseYourDot(){
        System.out.println("Введите X для игры крестиками, и любой другой символ для игры О");
        if (!scanner.next().toLowerCase(Locale.ROOT).equals("x")) {
            humanDot = DOT_O;
            aiDot = DOT_X;
        } else {
            humanDot = DOT_X;
            aiDot = DOT_O;
        }
    }

    public static void drawField(){
        System.out.print("X|");
        for (int i = 1; i <= sizeX; i++) {
            System.out.print(" " + i + "|");
        }
        System.out.println();
        for (int i = 0; i < sizeY; i++) {
            System.out.print((i + 1) + "| ");
            for (int j = 0; j < sizeX; j++) {
                System.out.print((fieldCell[i][j]) + "| ");
            }
            System.out.println();
        }
    }

    public static boolean chooseFirstTurn(){
        System.out.println("Если вы хотите ходить первым введите Y в противном случае первым будет ходить компьютер");
        if (scanner.next().toLowerCase(Locale.ROOT).equals("y")) {
            return true;
        } return false;
    }

    public static void chooseAmountDotsToWin(){
        System.out.println("Введите длину линии необходимой для победы");
        do {
            dotToWin = scanner.nextInt();
            if (dotToWin <= 2) {
                System.out.println("Минимальная длина 3");
                System.out.println("Повторите ввод");
            }
            if (!(dotToWin <= sizeY && dotToWin <= sizeX)) {
                System.out.printf("Длина превышает размер максимальной диагонали поля %d x %d\n", sizeY, sizeX);
                System.out.println("Повторите ввод");
            }
        } while (!(dotToWin <= sizeY && dotToWin <= sizeX && dotToWin > 2 )); /* для корректной работы диагоналей
        на неквадратном поле реализую так. Тут можно реализовать проверку через "или" и потом предупреждать,
        что диагональ в случае если dotToWin больше одного из размеров корректно работать не будет.
        */
    }

    public static void humanTurn(){
        int y;
        int x;
        do {
            System.out.printf("Введите координаты куда поставить %c\n", humanDot);
            do {
                System.out.println("Введите координату Y");
                y = scanner.nextInt() - 1;
            } while (!(y < sizeY && y >= 0));
            do {
                System.out.println("Введите координату X");
                x = scanner.nextInt() - 1;
            } while (!(x < sizeX && x >= 0));
        } while (fieldCell[y][x] != DOT_EMPTY);
        fieldCell[y][x] = humanDot;
        drawField();
    }

    public static void aiTurn(){
        int n,k;
        // сюда логику Ai
        if (aiLogicSelectBlockType(humanDot)) {
            aiLogicDo();
        } else{
        do {
            n = random.nextInt(sizeY);
            k = random.nextInt(sizeX);
        } while (fieldCell[n][k] != DOT_EMPTY);
        fieldCell[n][k] = aiDot;
        System.out.println("Ход компьютера");
        drawField();
        }
    }

    public static boolean checkDraw(){
        for (int i = 0; i < sizeY; i++) {
            for (int j = 0; j < sizeX; j++) {
                if (fieldCell[i][j] == DOT_EMPTY) {
                    return false;
                }
                }
            }
        System.out.println("Ничья!");
        return true;
        }

    public static boolean checkWin(char dotCheck){
        int winTemp = 0;
        for (int i = 0; i < sizeY; i++) {   // проверка горизонтальной линии
            for (int j = 0; j < sizeX; j++) {
                if (fieldCell[i][j] == dotCheck) {
                   ++winTemp;
                   if (winTemp == dotToWin){
                       return true;
                   }
                } else {
                    winTemp = 0;
                }
            }
            winTemp = 0;
        }
        for (int i = 0; i < sizeX; i++) {   // проверка вертикальной линии
            for (int j = 0; j < sizeY; j++) {
                if (fieldCell[j][i] == dotCheck) {
                    ++winTemp;
                    if (winTemp == dotToWin){
                        return true;
                    }
                } else {
                    winTemp = 0;
                }
            }
            winTemp = 0;
        }
        for (int i = 0; i < sizeY; i++) { // проверка прямой диагонали для любых полей
            for (int j = 0; j < sizeX; j++) {
                if (fieldCell[i][j] == dotCheck) {
                    if (i + dotToWin <= sizeY && j + dotToWin <= sizeX) {
                        ++winTemp;
                        for (int k = 1; k < dotToWin; k++) {
                            if (fieldCell[i + k][j + k] == dotCheck) {
                                ++winTemp;
                                if (winTemp == dotToWin) {
                                    return true;
                                }
                            } else {
                                winTemp = 0;
                            }
                        }
                    }
                }
            }
        }
        for (int i = sizeY - 1; i >= 0; i--) { // проверка обратной диагонали для любых полей
            for (int j = sizeX - 1; j >= 0; j--) {
                if (fieldCell[i][j] == dotCheck) {
                    if (i - dotToWin <= sizeY && j + dotToWin <= sizeX) {
                        ++winTemp;
                        for (int k = 1; k < dotToWin; k++) {
                            if (i - k >= 0 && j + k < sizeX){
                            if ( fieldCell[i - k][j + k] == dotCheck) {
                                ++winTemp;
                                if (winTemp == dotToWin) {
                                    return true;
                                }
                            } else {
                                winTemp = 0;
                            }
                        }
                        }
                    }
                }
            }
        }
         return false;
    }

    public static boolean gameCheck(char dotCheck){
        if (checkWin(dotCheck) && dotCheck == humanDot) {
            System.out.println("Вы выйграли!");
            humanScore++;
            return true;
        } else if (checkWin(dotCheck) && dotCheck == aiDot) {
            System.out.println("Компьютер выйграл!");
            aiScore++;
            return true;
        }
        return checkDraw();
    }

    public static boolean aiLogicSelectBlockType(char dotCheck) {
        int winTemp = 0;
        for (int i = 0; i < sizeY; i++) {   // проверка горизонтальной линии
            for (int j = 0; j < sizeX; j++) {
                if (fieldCell[i][j] == dotCheck) {
                    ++winTemp;
                    if (winTemp == 2) {
                        aiLogicBlock[0] = i;
                        aiLogicBlock[1] = j;
                        if (aiLogicBlock[1] + 1 < sizeX) {
                            aiLogicBlock[1] = aiLogicBlock[1] + 1;
                            if (fieldCell[aiLogicBlock[0]][aiLogicBlock[1]] == DOT_EMPTY) {
                                return true;
                            }
                        } else {
                            aiLogicBlock[1] = aiLogicBlock[1] - 2;
                            if (fieldCell[aiLogicBlock[0]][aiLogicBlock[1]] == DOT_EMPTY) {
                                return true;
                            }
                        }
                    }
                } else {
                    winTemp = 0;
                }
            }
            winTemp = 0;
        }
        for (int i = 0; i < sizeX; i++) {   // проверка вертикальной линии
            for (int j = 0; j < sizeY; j++) {
                if (fieldCell[j][i] == dotCheck) {
                    ++winTemp;
                    if (winTemp == 2) {
                        aiLogicBlock[0] = j;
                        aiLogicBlock[1] = i;
                        if (aiLogicBlock[0] + 1 < sizeY) {
                            aiLogicBlock[0] = aiLogicBlock[0] + 1;
                            if (fieldCell[aiLogicBlock[0]][aiLogicBlock[1]] == DOT_EMPTY) {
                                return true;
                            }
                        } else {
                            aiLogicBlock[0] = aiLogicBlock[0] - 2;
                            if (fieldCell[aiLogicBlock[0]][aiLogicBlock[1]] == DOT_EMPTY) {
                                return true;
                            }

                        }
                    }
                } else {
                        winTemp = 0;
                    }
                }
                winTemp = 0;
            }

            for (int i = 0; i < sizeY; i++) { // проверка прямой диагонали для любых полей
                for (int j = 0; j < sizeX; j++) {
                    if (fieldCell[i][j] == dotCheck) {
                        if (i + dotToWin <= sizeY && j + dotToWin <= sizeX) {
                            ++winTemp;
                            for (int k = 1; k < dotToWin; k++) {
                                if (fieldCell[i + k][j + k] == dotCheck) {
                                    ++winTemp;
                                    if (winTemp == 2) {
                                        aiLogicBlock[0] = i + k;
                                        aiLogicBlock[1] = j + k;
                                        if (aiLogicBlock[0] + 1 < sizeY && aiLogicBlock[1] + 1 < sizeX){
                                            aiLogicBlock[0]++;
                                            aiLogicBlock[1]++;
                                            if (fieldCell[aiLogicBlock[0]][aiLogicBlock[1]] == DOT_EMPTY) {
                                                return true;
                                            }
                                        } else {
                                            if (aiLogicBlock[0] - 2 >= 0 && aiLogicBlock[1] - 2 >= 0){
                                                aiLogicBlock[0] = aiLogicBlock[0] - 2;
                                                aiLogicBlock[1] = aiLogicBlock[1] - 2;
                                                if (fieldCell[aiLogicBlock[0]][aiLogicBlock[1]] == DOT_EMPTY) {
                                                    return true;
                                                }
                                            }

                                        }
                                    }
                                } else {
                                    winTemp = 0;
                                }
                            }
                        }
                    }
                }
            }
            for (int i = sizeY - 1; i >= 0; i--) { // проверка обратной диагонали для любых полей
                for (int j = sizeX - 1; j >= 0; j--) {
                    if (fieldCell[i][j] == dotCheck) {
                        if (i - dotToWin <= sizeY && j + dotToWin <= sizeX) {
                            ++winTemp;
                            for (int k = 1; k < dotToWin; k++) {
                                if (i - k >= 0 && j + k < sizeX) {
                                    if (fieldCell[i - k][j + k] == dotCheck) {
                                        ++winTemp;
                                        if (winTemp == 2) {
                                            aiLogicBlock[0] = i - k;
                                            aiLogicBlock[1] = j + k;
                                            if (aiLogicBlock[0] - 1 >= 0 && aiLogicBlock[1] + 1 < sizeX){
                                                aiLogicBlock[0]--;
                                                aiLogicBlock[1]++;
                                                if (fieldCell[aiLogicBlock[0]][aiLogicBlock[1]] == DOT_EMPTY) {
                                                    return true;
                                                }
                                            } else {
                                                if (aiLogicBlock[0] + 2 < sizeY && aiLogicBlock[1] - 2 > 0){
                                                    aiLogicBlock[0] = aiLogicBlock[0] + 2;
                                                    aiLogicBlock[1] = aiLogicBlock[1] - 2;
                                                    if (fieldCell[aiLogicBlock[0]][aiLogicBlock[1]] == DOT_EMPTY) {
                                                        return true;
                                                    }
                                                }

                                            }
                                        }
                                    } else {
                                        winTemp = 0;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return false;
        }

    public static void aiLogicDo(){
                fieldCell[aiLogicBlock[0]][aiLogicBlock[1]] = aiDot;
                drawField();
        }
    }


