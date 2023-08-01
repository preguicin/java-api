package User;

import java.util.ArrayList;

public class PersonManager {
    ArrayList<Person> personArrList = new ArrayList<>();

    public void addPerson(String name){
        Person p = new Person(name);
        personArrList.add(p);
    }

    public boolean deletePerson(String name){
        int index = 0;

        do{
            Person p = personArrList.get(index);
            if(p.getName().equals(name)){
                personArrList.remove(p);
                return true;
            }
            index++;
        }while(index < personArrList.size());

        return false;
    }

    public boolean putPerson(String name){
        int index = 0;

        do{
            Person p = personArrList.get(index);
            if(p.getName().equals(name)){
                p.setName(name);
                return true;
            }
            index++;
        }while(index < personArrList.size());

        return false;
    }

}
