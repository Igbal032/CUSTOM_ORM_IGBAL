package Helpers;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helpers {
    Scanner scanner = new Scanner(System.in);

    public int enterAndCheckSelectedOption(int optionCount){
        Matcher matcher=null;
        String selectOp = "";
        boolean wrong=true;
        do{
            selectOp=scanner.next();
            try{
                if (Integer.parseInt(selectOp)>optionCount){
                    System.out.println("Zəhmət olmasa düzgün seçim edin!!");
                    wrong = false;
                }
                else {
                    Pattern pattern = Pattern.compile("[0-9]");
                    matcher = pattern.matcher(selectOp.trim());
                    if (!matcher.matches()){
                        System.out.println("Zəhmət olmasa düzgün seçim edin!!");
                        wrong=false;
                    }
                    else {
                        wrong = true;
                    }
                }
            }catch(Exception e ){
                System.out.println("Zəhmət olmasa düzgün seçim edin!!");
                wrong = false;
            }
        }
        while (!wrong);
        return Integer.parseInt(selectOp);
    }

    public boolean checkedYesOrNot(String YesOrNot){
        Pattern pattern = Pattern.compile("[1,2]{1}");
        Matcher matcher = pattern.matcher(YesOrNot);
        if (!matcher.matches()) System.out.println("Zehmet olmasa seçimi düzgün edin!!");
        return matcher.matches();
    }

    public int askQuestion(){
        String selectedAnswer="";
        do{
            System.out.println("1. Bəli\n2.Xeyr");
            selectedAnswer = scanner.next();
        }
        while (selectedAnswer.trim().length()==0||!checkedYesOrNot(selectedAnswer.trim()));
        if (Integer.parseInt(selectedAnswer)==1){
            return 1;
        }
        else if (Integer.parseInt(selectedAnswer)==2) return 2;
        else {
            return 0;
        }
    }

    public boolean checkCorrectionForId(String text){
        Pattern pattern = Pattern.compile("[\\d]+");
        Matcher matcher = pattern.matcher(text);
        if (!matcher.matches()) System.out.println("Zehmet olmasa ancaq rəqəm daxil edin!!");
        return matcher.matches();
    }

}
