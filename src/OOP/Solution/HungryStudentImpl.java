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
        ((HungryStudentImpl)s).friends.add(this);
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
                .sorted((x, y) -> {
                    if (x.averageRating() > y.averageRating()) return 1;
                    if (x.averageRating() < y.averageRating()) return -1;
                    if (x.distance() < y.distance()) return 1;
                    if (x.distance() > y.distance()) return -1;
                    if (((RestaurantImpl)x).id() < ((RestaurantImpl)y).id()) return 1;
                    if (((RestaurantImpl)x).id() > ((RestaurantImpl)y).id()) return -1;
                    return 0;
                })
                .toList();
    }

    @Override
    public Collection<Restaurant> favoritesByDist(int dLimit) {
        return this.favorites.stream()
                .filter(x -> x.distance() < dLimit)
                .sorted((x, y) -> {
                    if (x.distance() < y.distance()) return 1;
                    if (x.distance() > y.distance()) return -1;
                    if (x.averageRating() > y.averageRating()) return 1;
                    if (x.averageRating() < y.averageRating()) return -1;
                    if (((RestaurantImpl)x).id() < ((RestaurantImpl)y).id()) return 1;
                    if (((RestaurantImpl)x).id() > ((RestaurantImpl)y).id()) return -1;
                    return 0;
                })
                .toList();
    }

    @Override
    public int compareTo(HungryStudent o) {
        throw new RuntimeException("Not implemented!");
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
        throw new RuntimeException("Not implemented!");
    }
}
