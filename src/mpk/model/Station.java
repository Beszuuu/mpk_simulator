package mpk.model;

public class Station {
    private String name;
    private double popularity; // Prawdopodobieństwo generowania pasażera

    public Station(String name, double popularity) {
        this.name = name;
        this.popularity = popularity;
    }

    public String getName() {
        return name;
    }

    public double getPopularity() {
        return popularity;
    }

    // Do wyświetlania nazw przystnków
    @Override
    public String toString() {
        return name;
    }
}
