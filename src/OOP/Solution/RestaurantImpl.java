package OOP.Solution;

import OOP.Provided.HungryStudent;
import OOP.Provided.Restaurant;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RestaurantImpl implements Restaurant {
    private int id;
    private String name;
    private int distFromTech;
    private Set<String> menu;
    private Map<HungryStudent, Integer> ratings = new HashMap<HungryStudent, Integer>();

    public RestaurantImpl(int id, String name, int distFromTech, Set<String> menu) {
        this.id = id;
        this.name = name;
        this.distFromTech = distFromTech;
        this.menu = menu;
    }

    @Override
    public int distance() {
        return distFromTech;
    }

    @Override
    public Restaurant rate(HungryStudent s, int r) throws RateRangeException {
        if (!(0 <= r && r <= 5)) {
            throw new RateRangeException();
        }

        ratings.put(s, r);
        return this;
    }

    @Override
    public int numberOfRates() {
        return ratings.size();
    }

    @Override
    public double averageRating() {
        if (ratings.isEmpty()) return 0;
        return (double) ratings.values().stream().reduce(Integer::sum).orElse(0) / ratings.size();
    }

    @Override
    public int compareTo(Restaurant o) {
        throw new RuntimeException("Not implemented!");
    }

    @Override
    public String toString() {
        throw new RuntimeException("Not implemented!");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantImpl that = (RestaurantImpl) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
