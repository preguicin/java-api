package User;

public class Pet {
    private int id;
    private String name;

    //Constructors
    public Pet(int id){
        this.id = id;
    }

    public Pet(int id, String name){
        this.id = id;
        this.name = name;
    }

    public Pet(){}

    //Getters and Setters
    public int getId(){
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
