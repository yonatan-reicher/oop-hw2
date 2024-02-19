package OOP.Tests;

import OOP.Provided.HamburgerNetwork.ImpossibleConnectionException;
import OOP.Provided.HungryStudent;
import OOP.Provided.HungryStudent.*;
import OOP.Provided.Restaurant;
import OOP.Provided.Restaurant.RateRangeException;
import OOP.Provided.Restaurant.RestaurantAlreadyInSystemException;
import OOP.Provided.Restaurant.RestaurantNotInSystemException;
import OOP.Solution.HungryStudentImpl;
import OOP.Solution.RestaurantImpl;
import org.junit.Test;

import java.util.*;
import java.util.function.Predicate;

import static org.junit.Assert.*;


public class RestaurantTest {
    public Restaurant NewRestaurant() {
        HashSet<String> set = new HashSet<String>();
        set.add("McBurger");
        set.add("Chips");
        set.add("Doritos");
        RestaurantImpl rest = new RestaurantImpl(123, "name", 10, set);
        return rest;
    }

    @Test
    public void TestRateAndAverageAndRates() {
        Restaurant r = NewRestaurant();
        HungryStudent s1 = new HungryStudentImpl(10, "Yoav");
        HungryStudent s2 = new HungryStudentImpl(20, "Jogev");

        assertEquals(0, r.numberOfRates());
        assertEquals(0.0, r.averageRating(), 0.);
        try {
            r.rate(s1, 10);
            fail("Exception should have been thrown");
        }  catch (RateRangeException ignored) {}
        assertEquals(0.0, r.averageRating(), 0.);
        assertEquals(0, r.numberOfRates());
        try {
            r.rate(s1, 5);
            assertEquals(5.0, r.averageRating(), 0.0);
            assertEquals(1, r.numberOfRates());
            r.rate(s1, 3);
            assertEquals(3.0, r.averageRating(), 0.0);
            assertEquals(1, r.numberOfRates());
            r.rate(s2, 4);
            assertEquals(3.5, r.averageRating(), 0.1);
            assertEquals(2, r.numberOfRates());
        } catch (RateRangeException e) {
            fail();
        }
    }

    @Test
    public void TestCompare() {
        Restaurant r1 = new RestaurantImpl(100, "xello", 15, new HashSet<>());
        Restaurant r2 = new RestaurantImpl(200, "hello", 15, new HashSet<>());

        assertEquals(0, r1.compareTo(r1));
        assertEquals(0, r2.compareTo(r2));
        assertEquals(Integer.compare(1, 2), r1.compareTo(r2));
        assertEquals(Integer.compare(2, 1), r2.compareTo(r1));
    }

    @Test
    public void TestToString() {
        HashSet<String> menu = new HashSet<>();
        menu.add("Name of");
        menu.add("Dish");
        menu.add("Hell Yeah");
        Restaurant r = new RestaurantImpl(42, "Jo", 5, menu);
        String result = r.toString();
        assertEquals("""
        Restaurant: Jo.
        Id: 42.
        Distance: 5.
        Menu: Dish, Hell Yeah, Name of.
        """.trim(), result.trim());
    }

    @Test
    public void TestWasRatedBy() {
        RestaurantImpl r = (RestaurantImpl) NewRestaurant();
        HungryStudent s = new HungryStudentImpl(1, "Jogev");

        assertFalse(r.wasRatedBy(s));
        try {
            r.rate(s, 10);
            fail("Excepiton Should have been thrown!");
        } catch (RateRangeException ignored) { }
        assertFalse(r.wasRatedBy(s));
        try {
            r.rate(s, -1);
            fail("Excepiton Should have been thrown!");
        } catch (RateRangeException ignored) { }
        assertFalse(r.wasRatedBy(s));
        try {
            r.rate(s, 2);
        } catch (RateRangeException ignored) {
            fail("Exception should not have been thrown!");
        }
        assertTrue(r.wasRatedBy(s));
    }
}