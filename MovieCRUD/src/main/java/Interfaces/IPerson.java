package Interfaces;
import Models.Person;
import java.util.List;

public interface IPerson {
    void  createPerson();
    void updatePerson();
    void deletePerson();
    void getAll();
    void searchWithName();
}
