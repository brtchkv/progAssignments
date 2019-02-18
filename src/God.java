import java.util.Objects;

public abstract class God implements Comparable<Human>{
    private String name;
    private int age;
    private String type;

    public God (String name, int age, String type){
        this.name = name;
        this.age = age;
        this.type = type;
    }

    public God (String name, String type){
        this.name = name;
        this.type = type;
    }

    public God(String type){
        this.type = type;
    }

    public int getAge() {
        return this.age;
    }

    public String getName(){
        return this.name;
    }

    public String getType(){
        return this.type;
    }

    @Override
    public String toString() {
        return (getName() + " -- " + getType() + " -- " + getAge());
    }

//    @Override

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        God god = (God) o;
        return age == god.age &&
                Objects.equals(name, god.name) &&
                Objects.equals(type, god.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, type);
    }

    @Override
    public int compareTo(Human human) {
        return this.getName().compareTo(human.getName());
    }
}
