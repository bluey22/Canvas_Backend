import java.util.stream.Stream;

/**
 * This generic class represents the data type Rectangle in a graphics system
 * Rectangle's coordinate type extends comparable to help with error handling
 * @author Ben Luo
 * bsl47
 * :)
 */

public final class Rectangle<T extends Comparable<? super T>> {
    //initialize variables
    private final T top;
    private final T bot;
    private final T left;
    private final T right;

    /**
     * private constructor to create a Rectangle 
     * @param top top vertical coordinate
     * @param bot bottom vertical coordinate
     * @param left left horizontal coordinate
     * @param right right horizontal coordinate
     */
    private Rectangle(T top, T bot, T left, T right) {
        this.top = top;
        this.bot = bot;
        this.left = left;
        this.right = right;
    }

    /**
     * Instead of a public constructor for Rectangle, we have
     * the method of() which creates and returns a new Rectangle
     * object with the following arguments
     * @param <T> Generic type argument - top, bot, left, right, can be any type
     * @param top top vertical coordinate
     * @param bot bottom vertical coordinate
     * @param left left horizontal coordinate
     * @param right right horizontal coordinate
     * @return a new Rectangle or throw NullPointerException
     * @throws Exception when any of the arguments are null and throws exception when possible coordinate mismatch/flip
     */
    public static final <T extends Comparable<T>> Rectangle<T> of(T top, T bot, T left, T right) throws Exception {
        if(!((verifyNonNull(top)) && (verifyNonNull(bot)) && (verifyNonNull(left)) && (verifyNonNull(right)))) {//makes sure none of the arguments (top,bot,left,right) are null
            throw new NullPointerException("Provide us with non-null coordinates");
        }
        verifyBounds(left, right);
        verifyBounds(bot, top);
        return new Rectangle<T>(top, bot, left, right);
    }

    /**
     * Helper method that throws exception when the first argument is greater than the second
     * @param <T>
     * @param obj1
     * @param obj2
     */
    public static final <T extends Comparable<T>> void verifyBounds(T obj1, T obj2) {
        if (obj1.compareTo(obj2) > 0) {
            throw new IllegalArgumentException("Please provide valid rectangular bounds");
        }
    }         

    /**
     * REFACTORED WITH STREAMS FOR ASSIGNMENT 10
     * Helper method for of(), checks if an object is null
     * @param obj any object
     * @return if the object is not null (TRUE) || if the object is null (FALSE)
     */
    private static final boolean verifyNonNull (Object obj) {
        return Stream.of(obj).anyMatch(x -> x != null);
        //return obj!=null;
    }

    //getters for private final variables
    public T top() {
        return top;
    }

    public T bot() {
        return bot;
    }

    public T left() {
        return left;
    }

    public T right() {
        return right;
    }
} 
 