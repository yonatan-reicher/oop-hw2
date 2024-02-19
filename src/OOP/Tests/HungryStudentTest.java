package OOP.Tests;

import OOP.Provided.HungryStudent;
import OOP.Provided.Restaurant;
import OOP.Provided.Restaurant.RateRangeException;
import OOP.Solution.HungryStudentImpl;
import OOP.Solution.RestaurantImpl;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;


public class HungryStudentTest {
    public Restaurant NewRestaurant() {
        HashSet<String> set = new HashSet<String>();
        set.add("McBurger");
        set.add("Chips");
        set.add("Doritos");
        RestaurantImpl rest = new RestaurantImpl(123, "name", 10, set);
        return rest;
    }

    @Test
    public void TestFavorite() throws RateRangeException {
        Restaurant r = NewRestaurant();
        HungryStudent s = new HungryStudentImpl(1, "A");

        try {
            s.favorite(r);
            fail("Exception should have been thrown!");
        } catch (HungryStudent.UnratedFavoriteRestaurantException ignored) {
        }

        assertEquals(0, s.favorites().size());

        r.rate(s, 4);
        try {
            s.favorite(r);
        } catch (HungryStudent.UnratedFavoriteRestaurantException e) {
            throw new RuntimeException(e);
        }

        assertEquals(1, s.favorites().size());
        try {
            // Rate again
            s.favorite(r);
        } catch (HungryStudent.UnratedFavoriteRestaurantException e) {
            throw new RuntimeException(e);
        }

        assertEquals(1, s.favorites().size());

        Restaurant r2 = new RestaurantImpl(1, "2", 4, new HashSet<>());
        r2.rate(s, 2);
        try {
            s.favorite(r2);
        } catch (HungryStudent.UnratedFavoriteRestaurantException e) {
            throw new RuntimeException(e);
        }

        assertEquals(2, s.favorites().size());
    }

    @Test
    public void TestFavoritesByRatingOrDistance() throws RateRangeException, HungryStudent.UnratedFavoriteRestaurantException {
        HashSet<String> menu = new HashSet<>();
        menu.add("");
        Restaurant r1 = new RestaurantImpl(1, "1", 1, menu);
        Restaurant r2 = new RestaurantImpl(2, "2", 2, menu);
        Restaurant r3 = new RestaurantImpl(3, "3", 3, menu);

        HungryStudent s = new HungryStudentImpl(10, "Jogi");
        r1.rate(s, 4);
        r2.rate(s, 3);
        r3.rate(s, 5);
        s.favorite(r1);
        s.favorite(r2);
        s.favorite(r3);

        List<Restaurant> rs = s.favoritesByRating(3).stream().toList();
        assertEquals(3, rs.size());
        assertEquals(r3, rs.get(0));
        assertEquals(r1, rs.get(1));
        assertEquals(r2, rs.get(2));

        rs = s.favoritesByRating(4).stream().toList();
        assertEquals(2, rs.size());
        assertEquals(r3, rs.get(0));
        assertEquals(r1, rs.get(1));

        rs = s.favoritesByDist(2).stream().toList();
        assertEquals(2, rs.size());
        assertEquals(r1, rs.get(0));
        assertEquals(r2, rs.get(1));
    }

    @Test
    public void TestToString() throws RateRangeException, HungryStudent.UnratedFavoriteRestaurantException {
        HashSet<String> menu = new HashSet<>();
        menu.add("");
        Restaurant r1 = new RestaurantImpl(1, "BBB", 1, menu);
        Restaurant r2 = new RestaurantImpl(2, "Anat", 2, menu);
        Restaurant r3 = new RestaurantImpl(3, "McDonalds", 3, menu);

        HungryStudent s = new HungryStudentImpl(10, "Jogi");
        r1.rate(s, 4);
        r2.rate(s, 3);
        r3.rate(s, 5);
        s.favorite(r1);
        s.favorite(r2);
        s.favorite(r3);

        String result = s.toString();

        assertEquals("""
        Hungry student: Jogi.
        Id: 10.
        Favorites: Anat, BBB, McDonalds.
        """.trim(), result.trim());
    }
}