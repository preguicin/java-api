package User;

import java.util.ArrayList;

public class Person {
    //Person variables
    private int id;
    private String address;
    private String name;
    //Pet variables
    private ArrayList<Pet> pets = new ArrayList<>();
    private int petIndex = 0;

    public Person(String name,String address, int id){
        this.id = id;
        this.name = name;
        this.address = address;
    }
    
    public ArrayList<Pet> getPets() {
        ArrayList<Pet> petReturn = new ArrayList<>();
        for(int i = 0; i < pets.size(); i++){
            petReturn.add(pets.get(i));
        }
        return petReturn;
    }

    public boolean deletePet(int petId){
        if(petId == Integer.MIN_VALUE){
            return false;
        }
         for(int i = 0; i < pets.size(); i++){
            Pet p = pets.get(i);
            if(p.getId() == petId){
                pets.remove(i);
                return true;
            }
        }
        return false;
    }

    public Pet getPet(int i) {
        return pets.get(i);
    }

    public Pet getLatestPet() {
        if(petIndex == 0){
         return null;
        }
        return pets.get(petIndex - 1);
    }

    public void setPet(String name) {
        Pet pet = new Pet(petIndex, name);
        pets.add(pet);
        petIndex++;
    }

    public int getId() {
        return this.id;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }
}
