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
        if (o.getClass() != RestaurantImpl.class) return 0;
        RestaurantImpl r = (RestaurantImpl) o;
        return Integer.compare(id, r.id);
    }

    @Override
    public String toString() {
    /*
     * @return the restaurant's description as a string in the following format:
                * <format>
     * Restaurant: <name>.
     * Id: <id>.
     * Distance: <dist>.
     * Menu: <menuItem1, menuItem2, menuItem3...>.
     * </format>
     * Note: Menu items are ordered by lexicographical order, asc.
                *
     * Example:
     *
     * Restaurant: BBB.
     * Id: 1.
     * Distance: 5.
     * Menu: Cola, French Fries, Steak.
     */
        String ret = "";
        ret += "Restaurant: " + name + ".\n";
        ret += "Id: " + id + ".\n";
        ret += "Distance: " + distFromTech + ".\n";
        ret += "Menu: ";
        ret += menu.stream().sorted().reduce((x, y) -> x + ", " + y).orElse("");
        ret += ".";
        return ret;
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

    public boolean wasRatedBy(HungryStudent student) {
        return this.ratings.containsKey(student);
    }

    public int id() {
        return this.id;
    }

    public String name() {
        return this.name;
    }
}
