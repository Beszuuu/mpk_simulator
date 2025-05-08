package mpk.model;

public class Stop {
    private String name;
    private double popularity; // Prawdopodobieństwo generowania pasażera

    public Stop(String name, double popularity) {
        this.name = name;
        this.popularity = popularity;
    }

    public String getName() {
        return name;
    }

    public double getPopularity() {
        return popularity;
    }
}