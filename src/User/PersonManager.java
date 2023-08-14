package User;

import java.util.ArrayList;

public class PersonManager {

    private static ArrayList<Person> personArrList = new ArrayList<>();

    public void addPerson(String name, String address, int index){
        Person p = new Person(name, address, index);
        personArrList.add(p);
        
    }

    public Person getPerson(int id){
        if(id == Integer.MIN_VALUE || personArrList.size() == 0){
            return null;
        }

        int i = 0;

        do{
            Person p = personArrList.get(i);
            if(p.getId() == id){
                return p;
            }
            i++;
        }while(i < personArrList.size());

        return null;
    }

    public boolean deletePerson(int id){
        if(id == Integer.MIN_VALUE|| personArrList.size() == 0){
            return false;
        }

        int i = 0;

        do{
            Person p = personArrList.get(i);
            if(p.getId() == id){
                personArrList.remove(p);
                return true;
            }
            i++;
        }while(i < personArrList.size());

        return false;
    }

    public boolean putPerson(int id, String name){
        if(id == Integer.MIN_VALUE || personArrList.size() == 0){
            return false;
        }

        int i = 0;

        do{
            Person p = personArrList.get(i);
            if(p.getId() == id){
                p.setName(name);
                return true;
            }
            i++;
        }while(i < personArrList.size());

        return false;
    }
    
    //Pet related methods

    public ArrayList<Pet> getPets(int id){
        if(id == Integer.MIN_VALUE || personArrList.size() == 0){
            return null;
        }

        for(int i = 0; i < personArrList.size(); i++){
            Person p = personArrList.get(i);
            if(p.getId() == id){
                return p.getPets();
            }
        }

        return null;
    }

    public boolean addPet(int id, String petName){
        if(id == Integer.MIN_VALUE || personArrList.size() <= 0){
            return false;
        }
        
        for(int i = 0; i < personArrList.size(); i++){
            Person p = personArrList.get(i);
            if(p.getId() == id){
                p.setPet(petName);
                return true;
            }
        }
        return false;
    }

}
