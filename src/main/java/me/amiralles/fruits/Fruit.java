package me.amiralles.fruits;

public class Fruit {

    public Long id;
    public String name;
    public Long itemId;

    public Fruit() {
    }

    public Fruit(Long id, String name, Long itemId) {
        this.id = id;
        this.name = name;
        this.itemId = itemId;
    }

    @Override
    public String toString() {
        return "Fruit{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", itemId='" + itemId + '\'' +
                '}';
    }
}
