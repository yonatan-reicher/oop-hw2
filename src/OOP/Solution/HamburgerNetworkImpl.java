package OOP.Solution;

import OOP.Provided.HungryStudent;
import OOP.Provided.HamburgerNetwork;
import OOP.Provided.Restaurant;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;


public class HamburgerNetworkImpl implements HamburgerNetwork {
    private Collection<HungryStudent> students;
    private Collection<Restaurant> resturants;
    public HamburgerNetworkImpl() {
        students = new LinkedList<HungryStudent>();
        resturants = new LinkedList<Restaurant>();
    }

    @Override
    public HungryStudent joinNetwork(int id, String name) throws HungryStudent.StudentAlreadyInSystemException {
        //check if student already exists
        for (HungryStudent student : students) {
            if (student instanceof HungryStudentImpl && ((HungryStudentImpl) student).id() == id){
                throw new HungryStudent.StudentAlreadyInSystemException();
            }
        }
        HungryStudent newStudent = new HungryStudentImpl(id, name);
        students.add(newStudent);
        return newStudent;
    }

    @Override
    public Restaurant addRestaurant(int id, String name, int dist, Set<String> menu) throws Restaurant.RestaurantAlreadyInSystemException {
        //check if restaurant already exists
        for (Restaurant restaurant : resturants) {
            //check if the restaurant is an instance of RestaurantImpl and if it is, check if the id is the same
            if (restaurant instanceof RestaurantImpl && ((RestaurantImpl) restaurant).id() == id) {
                throw new Restaurant.RestaurantAlreadyInSystemException();
            }
        }
        Restaurant newRestaurant = new RestaurantImpl(id, name, dist, menu);
        resturants.add(newRestaurant);
        return newRestaurant;
    }

    @Override
    public Collection<HungryStudent> registeredStudents() {
        return students;
    }

    @Override
    public Collection<Restaurant> registeredRestaurants() {
        return resturants;
    }

    @Override
    public HungryStudent getStudent(int id) throws HungryStudent.StudentNotInSystemException {
        for (HungryStudent student : students) {
            if (student instanceof HungryStudentImpl && ((HungryStudentImpl) student).id() == id){
                return student;
            }
        }
        throw new HungryStudent.StudentNotInSystemException();
    }

    @Override
    public Restaurant getRestaurant(int id) throws Restaurant.RestaurantNotInSystemException {
        for (Restaurant restaurant : resturants) {
            //check if the restaurant is an instance of RestaurantImpl and if it is, check if the id is the same
            if (restaurant instanceof RestaurantImpl && ((RestaurantImpl) restaurant).id() == id) {
                return restaurant;
            }
        }
        throw new Restaurant.RestaurantNotInSystemException();
    }
    public HamburgerNetwork addConnection(HungryStudent s1, HungryStudent s2)
            throws HungryStudent.StudentNotInSystemException, HungryStudent.ConnectionAlreadyExistsException, HungryStudent.SameStudentException {
        // check if either one of them is not in the system
        if (!students.contains(s1) || !students.contains(s2)) {
            throw new HungryStudent.StudentNotInSystemException();
        }
        // check if they are the same student
        //check if they're both instances of HungryStudentImpl then check if their id's are the same
        if (s1 instanceof HungryStudentImpl && s2 instanceof HungryStudentImpl && ((HungryStudentImpl) s1).id() == ((HungryStudentImpl) s2).id()) {
            throw new HungryStudent.SameStudentException();
        }
        // check if they are already friends
        if (s1.getFriends().contains(s2)) {
            throw new HungryStudent.ConnectionAlreadyExistsException();
        }
        s1.addFriend(s2);
        //s2.addFriend(s1);
        return this;
    }

    @Override
    public Collection<Restaurant> favoritesByRating(HungryStudent s) throws HungryStudent.StudentNotInSystemException {
        //check if student is in the system
        if (!students.contains(s)) {
            throw new HungryStudent.StudentNotInSystemException();
        }
        //create a new body for friends of s in the system and sort it by id of student in an increasing order
        Collection<HungryStudent> friends = s.getFriends();
        //remove the friends who are not in the system
        friends.removeIf(friend -> !students.contains(friend));
        //sort the friends by id in an increasing order
        Collection<HungryStudent> sortedFriends = friends.stream().sorted((s1, s2) -> {
            if (s1 instanceof HungryStudentImpl && s2 instanceof HungryStudentImpl) {
                return ((HungryStudentImpl) s1).id() - ((HungryStudentImpl) s2).id();
            }
            return 0;
        }).toList();
        //go through every friend in order and add their favorites to the collection using the method favoritesByrating and then add the resturants to the collection
        Collection<Restaurant> favorites = new LinkedList<Restaurant>();
        for (HungryStudent friend : sortedFriends) {
            favorites.addAll(friend.favoritesByRating(0));
        }
        //create a new collection for the resutrants only it is of type resturant_with_remove
        Collection<resturant_with_remove> resturants_with_removed_list = new LinkedList<resturant_with_remove>();
        for (Restaurant resturant : favorites) {
            resturants_with_removed_list.add(new resturant_with_remove(resturant));
        }
        //go through the collection favorites and drop resturants that arent in the collection of resturants and also drop every resturant that exists before with a different friend
        int first_counter = 0;
        for (resturant_with_remove resturant1 : resturants_with_removed_list) {
            int second_counter = 0;
            if (!resturants.contains(resturant1.new_Restaurant) || resturant1.remove) {
                resturant1.remove=true;
                first_counter++;
                continue;
            }
            for (resturant_with_remove resturant2 : resturants_with_removed_list) {
                //check if both resturants are of type RestaurantImpl and then check if their id's are the same
                if (resturant1.new_Restaurant instanceof RestaurantImpl && resturant2.new_Restaurant instanceof RestaurantImpl) {
                    if (((RestaurantImpl) resturant1.new_Restaurant).id() == ((RestaurantImpl) resturant2.new_Restaurant).id() && first_counter != second_counter) {
                        resturant2.remove=true;
                    }
                }
                second_counter++;
            }
            first_counter++;
        }
        Collection<Restaurant> return_favorites = new LinkedList<Restaurant>();
        for (resturant_with_remove resturant : resturants_with_removed_list) {
            if (!resturant.remove) {
                return_favorites.add(resturant.new_Restaurant);
            }
        }
        return return_favorites;
    }


//        //check if student is in the system
//        if (!students.contains(s)) {
//            throw new HungryStudent.StudentNotInSystemException();
//        }
//        //create a new body for friends of s in the system and sort it by id of student in an increasing order
//        Collection<HungryStudent> friends = s.getFriends();
//        //remove the friends who are not in the system
//        friends.removeIf(friend -> !students.contains(friend));
//        //sort the friends by id in an increasing order after checking if they are instances of HungryStudentImpl
//        Collection<HungryStudent> sortedFriends = friends.stream().sorted((s1, s2) -> {
//            if (s1 instanceof HungryStudentImpl && s2 instanceof HungryStudentImpl) {
//                return ((HungryStudentImpl) s1).id() - ((HungryStudentImpl) s2).id();
//            }
//            return 0;
//    }).toList();
//         //does this sort in friends? does collection saves the sort?
//        //create a new collection for the restaurants go through the friends and add their favorites to the collection, in a decreasing order of the resturant rating, if its equal then distance from the technion
//        //i want the linked list to have another integer in them indicating which friends it came from, starting from 0 up to the number of friends
//        Collection <resturant_with_counter> favorites = new LinkedList<resturant_with_counter>();
//        int counter = 0;
//        for (HungryStudent friend : sortedFriends) {
//            //create a new resturant with counter for each collection of favorite resturants of a friend in friends and increase it for every different element in friends
//            for (Restaurant resturant : friend.favorites()) {
//                //check if the resturant is in the system and add it, otherwise dont add it
//                if (resturants.contains(resturant)) {
//                    favorites.add(new resturant_with_counter(resturant, counter));
//                }
//            }
//            counter++;
//        }
//        //sort the restaurants by rating in a decreasing order and then by distance from the technion in a decreasing order then by id in an increasing order
//        Collection<resturant_with_counter> sortedFavorites = favorites.stream().sorted((r1, r2) -> {
//            //check if the counter is lower, first counters at the start always
//            if (r1.new_counter != r2.new_counter) {
//                return r1.new_counter - r2.new_counter;
//            }
//            int ratingComparison = Double.compare(r1.new_Restaurant.averageRating(), r2.new_Restaurant.averageRating());
//            if (ratingComparison != 0) {
//                return ratingComparison;
//            }
//           if (r1.new_Restaurant.distance() != r2.new_Restaurant.distance()) {
//                return r1.new_Restaurant.distance() - r2.new_Restaurant.distance();
//            }
//           //check if they're both of type RestaurantImpl and then check if their id's are the same
//            if (r1.new_Restaurant instanceof RestaurantImpl && r2.new_Restaurant instanceof RestaurantImpl) {
//                return ((RestaurantImpl) r1.new_Restaurant).id() - ((RestaurantImpl) r2.new_Restaurant).id();
//            }
//            return ratingComparison;
//        }).toList();
//        // go through favorites and drop every resturant that exists before with a differen counter
//        for (resturant_with_counter resturant1 : sortedFavorites) {
//            for (resturant_with_counter resturant2: sortedFavorites)
//            {
//                //check if both resturants are of type RestaurantImpl and then check if their id's are the same
//                if (resturant1.new_Restaurant instanceof RestaurantImpl && resturant2.new_Restaurant instanceof RestaurantImpl) {
//                    if (((RestaurantImpl) resturant1.new_Restaurant).id() == ((RestaurantImpl) resturant2.new_Restaurant).id() && resturant1.new_counter != resturant2.new_counter)
//                    {
//                        sortedFavorites.remove(resturant2);
//                    }
//                }
//            }
//        }
//        //create a new collection for the restaurants and add the resturants from the favorites collection to it in the same order but without the counter
//        Collection<Restaurant> favorites_resturants = new LinkedList<Restaurant>();
//        for (resturant_with_counter resturant : sortedFavorites) {
//            favorites_resturants.add(resturant.new_Restaurant);
//        }
//        return favorites_resturants;


    @Override
    public Collection<Restaurant> favoritesByDist(HungryStudent s) throws HungryStudent.StudentNotInSystemException {
        //check if student is in the system
        if (!students.contains(s)) {
            throw new HungryStudent.StudentNotInSystemException();
        }
        //create a new body for friends of s in the system and sort it by id of student in an increasing order
        Collection<HungryStudent> friends = s.getFriends();
        //remove the friends who are not in the system
        friends.removeIf(friend -> !students.contains(friend));
        //sort the friends by id in an increasing order
        Collection<HungryStudent> sortedFriends = friends.stream().sorted((s1, s2) -> {
            if (s1 instanceof HungryStudentImpl && s2 instanceof HungryStudentImpl) {
                return ((HungryStudentImpl) s1).id() - ((HungryStudentImpl) s2).id();
            }
            return 0;
        }).toList();
        //go through every friend in order and add their favorites to the collection using the method favoritesByrating and then add the resturants to the collection
        Collection<Restaurant> favorites = new LinkedList<Restaurant>();
        for (HungryStudent friend : sortedFriends) {
            favorites.addAll(friend.favoritesByDist((Integer.MAX_VALUE)));
        }
        //create a new collection for the resutrants only it is of type resturant_with_remove
        Collection<resturant_with_remove> resturants_with_removed_list = new LinkedList<resturant_with_remove>();
        for (Restaurant resturant : favorites) {
            resturants_with_removed_list.add(new resturant_with_remove(resturant));
        }
        //go through the collection favorites and drop resturants that arent in the collection of resturants and also drop every resturant that exists before with a different friend
        int first_counter = 0;
        for (resturant_with_remove resturant1 : resturants_with_removed_list) {
            int second_counter = 0;
            if (!resturants.contains(resturant1.new_Restaurant) || resturant1.remove) {
                resturant1.remove=true;
                first_counter++;
                continue;
            }
            for (resturant_with_remove resturant2 : resturants_with_removed_list) {
                //check if both resturants are of type RestaurantImpl and then check if their id's are the same
                if (resturant1.new_Restaurant instanceof RestaurantImpl && resturant2.new_Restaurant instanceof RestaurantImpl) {
                    if (((RestaurantImpl) resturant1.new_Restaurant).id() == ((RestaurantImpl) resturant2.new_Restaurant).id() && first_counter != second_counter) {
                        resturant2.remove=true;
                    }
                }
                second_counter++;
            }
            first_counter++;
        }
        Collection<Restaurant> return_favorites = new LinkedList<Restaurant>();
        for (resturant_with_remove resturant : resturants_with_removed_list) {
            if (!resturant.remove) {
                return_favorites.add(resturant.new_Restaurant);
            }
        }
        return return_favorites;
    }
    //create function string tostring
    public String toString(){
        /**
         * @return the network's description as a string in the following format:
         * <format>
         * Registered students: <studentId1, studentId2, studentId3...>.
         * Registered restaurants: <resId1, resId2, resId3...>.
         * Students:
         * <student1Id> -> [<friend1Id, friend2Id, friend3Id...>].
         * <student2Id> -> [<friend1Id, friend2Id, friend3Id...>].
         * ...
         * End students.
         * </format>
         * Note: students, restaurants and friends' ids are ordered by natural integer order, asc.*
         * Example:
         *
         * Registered students: 1, 236703, 555555.
         * Registered restaurants: 12, 13.
         * Students:
         * 1 -> [236703, 555555555].
         * 236703 -> [1].
         * 555555 -> [1].
         * End students.
         * */
        //create a string for the registered students
        StringBuilder registered_students = new StringBuilder("Registered students: ");
        for (HungryStudent student : students) {
            //check if student is an instance of HungryStudentImpl and then add the id to the string
            if (student instanceof HungryStudentImpl) {
                registered_students.append(((HungryStudentImpl) student).id()).append(", ");
            }
        }
        //remove the last comma and space
        registered_students = new StringBuilder(registered_students.substring(0, registered_students.length() - 2));
        //create a string for the registered restaurants
        StringBuilder registered_resturants = new StringBuilder("Registered restaurants: ");
        for (Restaurant resturant : resturants) {
            //check if resturant is an instance of RestaurantImpl and then add the id to the string
            if (resturant instanceof RestaurantImpl) {
                registered_resturants.append(((RestaurantImpl) resturant).id()).append(", ");
            }
        }
        //remove the last comma and space
        registered_resturants = new StringBuilder(registered_resturants.substring(0, registered_resturants.length() - 2));
        //create a string for the students and their friends
        StringBuilder students_and_friends = new StringBuilder("Students:\n");
        for (HungryStudent student : students) {
            //check if student is an instance of HungryStudentImpl and then add the id and the friends to the string
            if (student instanceof HungryStudentImpl) {
                students_and_friends.append(((HungryStudentImpl) student).id()).append(" -> [");
            }
            for (HungryStudent friend : student.getFriends()) {
                //check if friend is an instance of HungryStudentImpl and then add the id to the string
                if (friend instanceof HungryStudentImpl) {
                    students_and_friends.append(((HungryStudentImpl) friend).id()).append(", ");
                }
            }
            //remove the last comma and space
            students_and_friends = new StringBuilder(students_and_friends.substring(0, students_and_friends.length() - 2));
            students_and_friends.append("].\n");
        }
        //students_and_friends += "End students.";
        //return the string
        return registered_students + "\n" + registered_resturants + "\n" + students_and_friends;
    }
    private boolean canGetAlongAux(HungryStudent s, Restaurant r, int t, Collection<HungryStudent> seen_friends) {
        //check if the student has the restaurant as a favorite
        if (s.favorites().contains(r)) {
            return true;
        }
        //check if the counter is bigger than t
        if (t == 0) {
            return false;
        }
        //go through the friends of the student and check if they have the restaurant as a favorite and if they do, return true, if not, go through their friends and so on
        for (HungryStudent friend : s.getFriends()) {
            if (!seen_friends.contains(friend)) {
                seen_friends.add(friend);
                if (canGetAlongAux(friend, r, t - 1, seen_friends)) {
                    seen_friends.remove(friend);
                    return true;
                }
                seen_friends.remove(friend);
            }
        }
        return false;

    }
    @Override
    public boolean getRecommendation(HungryStudent s, Restaurant r, int t) throws HungryStudent.StudentNotInSystemException, Restaurant.RestaurantNotInSystemException, HamburgerNetwork.ImpossibleConnectionException {
        //check if student is in the system
        if (!students.contains(s)) {
            throw new HungryStudent.StudentNotInSystemException();
        }
        //check if restaurant is in the system
        if (!resturants.contains(r)) {
            throw new Restaurant.RestaurantNotInSystemException();
        }
        //check if t is negative
        if (t < 0) {
            throw new HamburgerNetwork.ImpossibleConnectionException();
        }
        //start a recursive counter and a collection of students we've been through, then recursively call increasing the counter to check if a friend of a friend of a friend... has the restaurant as a favorite and if the counter is smaller than t, if it hits t you can go back, also before checking on someone you need to check if he's in the "we've been through collection" and if he is you can go back
        Collection<HungryStudent> seen_friends = new LinkedList<HungryStudent>();
        seen_friends.add(s);
        return canGetAlongAux(s, r, t, seen_friends);
    }
}
