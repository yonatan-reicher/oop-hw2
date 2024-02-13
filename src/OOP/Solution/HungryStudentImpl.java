package OOP.Solution;

import OOP.Provided.HungryStudent;
import OOP.Provided.Restaurant;

import java.util.Collection;
import java.util.Set;

public class HungryStudentImpl implements HungryStudent {
    public HungryStudentImpl
    @Override
    public HungryStudent favorite(Restaurant r) throws UnratedFavoriteRestaurantException {
        return null;
    }

    @Override
    public Collection<Restaurant> favorites() {
        return null;
    }

    @Override
    public HungryStudent addFriend(HungryStudent s) throws SameStudentException, ConnectionAlreadyExistsException {
        return null;
    }

    @Override
    public Set<HungryStudent> getFriends() {
        return null;
    }

    @Override
    public Collection<Restaurant> favoritesByRating(int rLimit) {
        return null;
    }

    @Override
    public Collection<Restaurant> favoritesByDist(int dLimit) {
        return null;
    }

    @Override
    public int compareTo(HungryStudent o) {
        return 0;
    }
}
