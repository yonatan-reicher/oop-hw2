package OOP.Solution;

import OOP.Provided.HungryStudent;
import OOP.Provided.Restaurant;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class HungryStudentImpl implements HungryStudent {
    private int id;
    private String name;
    private HashSet<HungryStudent> friends = new HashSet<>();
    private HashSet<Restaurant> favorites = new HashSet<>();

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
        return null;
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
        return new HashSet<HungryStudent>(this.friends);
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
        return 0;
    }
}
