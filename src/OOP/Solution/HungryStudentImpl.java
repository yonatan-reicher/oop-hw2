package OOP.Solution;

import OOP.Provided.HungryStudent;
import OOP.Provided.Restaurant;

import java.util.*;

public class HungryStudentImpl implements HungryStudent {
    private final int id;
    private final String name;
    private final HashSet<HungryStudent> friends = new HashSet<>();
    private final HashSet<Restaurant> favorites = new HashSet<>();

    public HungryStudentImpl(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int id() {
        return this.id;
    }

    @Override
    public HungryStudent favorite(Restaurant r) throws UnratedFavoriteRestaurantException {
        RestaurantImpl ri = (RestaurantImpl)r;
        if (!ri.wasRatedBy(this)) {
            throw new UnratedFavoriteRestaurantException();
        }
        favorites.add(r);
        return this;
    }

    @Override
    public Collection<Restaurant> favorites() {
        return new HashSet<>(this.favorites);
    }

    @Override
    public HungryStudent addFriend(HungryStudent s) throws SameStudentException, ConnectionAlreadyExistsException {
        if (this == s) throw new SameStudentException();
        if (this.friends.contains(s)) throw new ConnectionAlreadyExistsException();
        this.friends.add(s);
        return s;
    }

    @Override
    public Set<HungryStudent> getFriends() {
        return new HashSet<>(this.friends);
    }

    @Override
    public Collection<Restaurant> favoritesByRating(int rLimit) {
        return this.favorites.stream()
                .filter(x -> x.averageRating() >= rLimit)
                .sorted(Comparator
                        .comparing(Restaurant::averageRating).reversed()
                        .thenComparing(Restaurant::distance)
                        .thenComparing(x -> ((RestaurantImpl)x).id())
                )
                .toList();
    }

    @Override
    public Collection<Restaurant> favoritesByDist(int dLimit) {
        return this.favorites.stream()
                .filter(x -> x.distance() <= dLimit)
                .sorted(Comparator
                        .comparing(Restaurant::distance).reversed()
                        .thenComparing(Restaurant::averageRating).reversed()
                        .thenComparing(x -> ((RestaurantImpl)x).id())
                )
                .toList();
    }

    @Override
    public int compareTo(HungryStudent o) {
        if (o.getClass() != HungryStudentImpl.class) return 0;
        HungryStudentImpl s = (HungryStudentImpl) o;
        return Integer.compare(id, s.id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HungryStudentImpl that = (HungryStudentImpl) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        String ret = "Hungry student: " + name + ".\n";
        ret += "Id: " + id + ".\n";
        ret += "Favorites: ";
        ret += String.join(", ", favorites.stream().map(r -> ((RestaurantImpl)r).name()).sorted().toList());
        ret += ".";
        return  ret;
    }
}
