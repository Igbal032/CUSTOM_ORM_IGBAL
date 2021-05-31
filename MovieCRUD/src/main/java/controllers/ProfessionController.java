package controllers;

import Helpers.Helpers;
import Interfaces.IProfession;
import Models.Person;
import Models.Profession;
import Helpers.ProfessionHelper;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ProfessionController implements IProfession {
    Helpers helpers = new Helpers();
    ProfessionHelper professionHelper;
    Scanner scanner = new Scanner(System.in);
    EntityManager em ;
    public ProfessionController(EntityManager entityManager) {
        this.em=entityManager;
        this.professionHelper = new ProfessionHelper(entityManager);
    }
    @Override
    public void createProfession() {
    String name = professionHelper.enterInfo();
        EntityTransaction et = em.getTransaction();
        et.begin();
        Profession newProfession = Profession.builder()
                .name(name)
                .createdDate(LocalDate.now()).build();
        em.merge(newProfession);
        et.commit();
        System.out.println("Vəzifə əlavə edildi");
        return;
    }

    @Override
    public void updateProfession() {
        Profession findProfession =  null;
        EntityTransaction et = em.getTransaction();
        do{
            findProfession = professionHelper.askIdAndGetProfession();
            if (findProfession==null||findProfession.getDeletedDate()!=null){
                System.out.println("Bu İD də heç bir vəzifə tapılmadı!!");
            }
        }
        while (findProfession==null||findProfession.getDeletedDate()!=null);
        boolean wrongSelect = true;
        et.begin();
        do{
            System.out.println("Ad deyişmək istəyirsiniz?");
            int yesOrNot = helpers.askQuestion();
            if (yesOrNot==1){
                findProfession.setName(professionHelper.enterInfo());
                wrongSelect = true;
            }else if(yesOrNot==2){
                wrongSelect=true;
            }
            else {
                wrongSelect=false;
            }
        }
        while (!wrongSelect);
        em.merge(findProfession);
        et.commit();
    }

    @Override
    public void deleteProfession() {
        Profession profession=null;
        do{
            profession = professionHelper.askIdAndGetProfession();
            if (profession==null||profession.getDeletedDate()!=null){
                System.out.println("Bu İD də heç bir vəzifə tapılmadı tapılmadı!!");
            }
        }
        while (profession==null||profession.getDeletedDate()!=null);
        EntityTransaction et = em.getTransaction();
        et.begin();
        profession.setDeletedDate(LocalDate.now());
        em.merge(profession);
        System.out.println("Vəzifə müvəffəqiyyətlə silindi");
        et.commit();
    }

    @Override
    public void searchWithName() {
        String text = "";
        do{
            System.out.println("Zəhmət olmasa ad daxil edin!!");
            text = scanner.nextLine();
        }
        while (text.length()==0||!professionHelper.checkedName(text));
        List<Profession> professions = em.createNativeQuery(
                "select * from professions p where p.deleted_date is null order by p.name",Profession.class).getResultList();
        System.out.println("--------------Vəzifələr");
        String finalText = text;
        professions.stream().filter(w->w.getName().toLowerCase().startsWith(finalText.trim().toLowerCase())).forEach(p->{
            System.out.println("Vəzifə ID: "+p.getId()+
                    "|| Vəzifənin adı: "+p.getName()+"\n");
        });
        if (professions.size()!=0)
            professionMenu2();
        else {
            System.out.println("Bu adda vəzifə mövcud deyil");
            return;
        }
    }

    public  void  professionMenu(){
        while (true){
            System.out.println("1. Vəzifə əlavə edin");
            System.out.println("2. Ada görə axtarış");
            System.out.println("3. Şəxsiyyətə və vəzifə əməlyatları");
            System.out.println("0. Geriyə qayit");
            int select =  selectOptionInProfessionMenu();
            if (select==0){
                return;
            }
        }
    }

    private int selectOptionInProfessionMenu() {
        System.out.println("Zəhmət olmasa seçim edin!!");
        int whichOperation =  helpers.enterAndCheckSelectedOption(4);
        switch (whichOperation){
            case 1:{
                createProfession();
                return 1;
            }
            case 2:{
                searchWithName();
                return 2;
            }
            case 3:{
                PersonAndProfession personAndProfession = new PersonAndProfession(em);
                personAndProfession.PersonAndProfessionMenu();
                return 3;
            }
            case 0:
                return 0;
            default:
                return 5;
        }
    }

    public  void  professionMenu2(){
        while (true){
            System.out.println("1. Vəzifə Redaktə etmık");
            System.out.println("2. Vəzifəni silmək");
            System.out.println("0. Geriyə qayit");
            int select =  selectOptionInProfessionMenu2();
            if (select==0){
                return;
            }
        }
    }

    private int selectOptionInProfessionMenu2() {
        System.out.println("Zəhmət olmasa seçim edin!!");
        int whichOperation =  helpers.enterAndCheckSelectedOption(3);
        switch (whichOperation){
            case 1:{
                updateProfession();
                return 1;
            }
            case 2:{
                deleteProfession();
                return 2;
            }
            case 0:
                return 0;
            default:
                return 5;
        }
    }



}
