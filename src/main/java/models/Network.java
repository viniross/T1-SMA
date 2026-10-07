package src.main.java.models;

public class Network {

    private String target;
    private double probability;

    public Network(String target, double probability) {
        this.target = target;
        this.probability = probability;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }
}
