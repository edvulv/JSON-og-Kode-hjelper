package forms;

public class testClass {
    private int points;
    private String name;
    private float value;

    public testClass(int points, String name, float value) {
        this.points = points;
        this.name = name;
        this.value = value;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
