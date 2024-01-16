import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
/**
 * @author Ben Luo
 * bsl47
 * :)
 */

 public final class Canvas<T extends Comparable<? super T>>{
    //Initialize variables
    //private Navigable<T, Set<T>> pixels;
    private final NavigableMap<T, NavigableSet<T>> pixels;
    
    //private Comparator - new for assignment 8
    private Comparator<T> canvasComparator;

    /**
     * REFACTORED
     * private constructor that takes in a Map , map of sets
     * and copies the content into the private tree map
     * @param objMap - generic map of sets, assert not null
     */
    private Canvas(Map<T, Set<T>> objMap, Comparator<T> comparator) {
        canvasComparator = comparator; //refactor private constructor
        this.pixels = canvasComparator != null ? new TreeMap<>(canvasComparator) : new TreeMap<>(); // refactor
        assert objMap != null;
        for(var x: objMap.keySet()) {
            var entry = x;
            NavigableSet<T> toPutSet = canvasComparator != null ? new TreeSet<>(canvasComparator) : new TreeSet<>(); //refactor
            for(var y: objMap.get(x)) {
                toPutSet.add(y);
            }
            this.pixels.put(entry, toPutSet);
        }
        
    }

    /**
     * Instead of a public constructor, we have a helper method of()
     * that will return a Canvas Object
     * @param <T> - Generic Type Argument
     * @param pixMap - Map of T keys and Set<T> values
     * @param optCmp - Optional of Comparator
     * @return - Canvas<T> object
     * @throws Exception when argument is null
     */
    public static final <T extends Comparable<? super T>> Canvas<T> of(Map<T, Set<T>> pixMap, Optional<Comparator<T>> optCmp) throws Exception {
        return optCmp.isPresent() ? new Canvas<T>(Objects.requireNonNull(pixMap), optCmp.get()) : Canvas.of(pixMap); 
    }

    /**
     * REFACTORED
     * Instead of a public constructor, we have a helper method of()
     * that will return a Canvas Object
     * @param <T> - Generic Type Argument
     * @param objMap - Map of T keys and Set<T> values
     * @return - Canvas<T> object
     * @throws Exception when the argument is null
     */
    public static final <T extends Comparable<? super T>> Canvas<T> of(Map<T, Set<T>> objMap) throws Exception {
        return new Canvas<T>(Objects.requireNonNull(objMap), null); //comparator default to null
    }

    /**
     * NEW/REFACTORED
     * Overload for of() which ensures a non-null comparator is taken in
     * Returns a canvas Object with ordering based on this comparator
     * @param <T>- Generic Type Argument
     * @param objMap - Map of T keys and Set<T> values
     * @param comparator - Comparator object of type T
     * @return- Canvas<T> object
     * @throws Exception when either argument is null
     */
    public static final <T extends Comparable<? super T>> Canvas<T> of(Map<T, Set<T>> objMap, Comparator<T> comparator) throws Exception {
        return new Canvas<T>(Objects.requireNonNull(objMap), Objects.requireNonNull(comparator)); 
    }

    /**
     * REFACTORED TO USE STREAM: Helper method that takes in a sorted map and returns an unsorted map
     * @param <T>
     * @param sortedMap
     * @return toReturnMap - an unsorted map (Hash Map of Hash Sets)
     */
    public static final <T> Map<T, Set<T>> unsortedMap(NavigableMap<T, NavigableSet<T>> sortedMap){
        return sortedMap.keySet().stream()
            .collect(Collectors.toMap(Function.identity(), 
                k -> sortedMap.get(k).stream().collect(Collectors.toCollection(HashSet::new))));

        //OLD CODE
        // Map<T, Set<T>> toReturnMap = new HashMap<>(sortedMap);
        // for(var x: sortedMap.keySet()) {
        //     var entry = x;
        //     Set<T> toPutSet = new HashSet<>();
        //     for(var y: sortedMap.get(x)) {
        //         var key = y;
        //         toPutSet.add(key);
        //     }
        //     toReturnMap.put(entry, toPutSet);
        // }
        // return toReturnMap;
    }

    /**
     * REFACTORED WITH STREAMS
     * Method takes in a rectangle and uses it to slice the Canvas.
     * The result is a new canvas that contains only the point within or 
     * on the rectangle
     * @param rect
     * @return - new instance of canvas object
     * @throws Exception
     */
    public final Canvas<T> slice(Rectangle<T> rect) throws Exception {
            return Canvas.of(pixels.subMap(canvasComparator != null ? 
                    (canvasComparator.compare(rect.left(), rect.right()) < 0 ? rect.left() : rect.right()) : rect.left(), true,
                    canvasComparator != null ? 
                    (canvasComparator.compare(rect.left(), rect.right()) > 0 ? rect.left() : rect.right()) : rect.right(), true)
                .keySet()
                .stream()
                .collect(Collectors.toMap(Function.identity(), k -> pixels
                    .get(k).subSet(canvasComparator != null ? (canvasComparator.compare(rect.bot(), rect.top()) < 0 ? rect.bot() : rect.top()) : rect.bot(), true,
                    canvasComparator != null ? (canvasComparator.compare(rect.bot(), rect.top()) > 0 ? rect.bot() : rect.top()) : rect.top(), true))), 
                canvasComparator != null ? Optional.of(canvasComparator): Optional.empty());
                
        //OLD CODE
        // if(canvasComparator != null) then we need to find the new lefts and rights
        //     T fakeLeft = canvasComparator != null ? (canvasComparator.compare(rect.left(), rect.right()) < 0 ? rect.left() : rect.right()) : rect.left();
        //     T fakeRight = canvasComparator != null ? (canvasComparator.compare(rect.left(), rect.right()) > 0 ? rect.left() : rect.right()) : rect.right();
        //     T fakeBot = canvasComparator != null ? (canvasComparator.compare(rect.bot(), rect.top()) < 0 ? rect.bot() : rect.top()) : rect.bot();
        //     T fakeTop = canvasComparator != null ? (canvasComparator.compare(rect.bot(), rect.top()) > 0 ? rect.bot() : rect.top()) : rect.top();

        // //make an unsorted map from these new bounds (only sliced horizontally right now)
        // Map<T, Set<T>> xBoundMap = Canvas.unsortedMap(pixels.subMap(fakeLeft, true, fakeRight, true));
        // //Make a canvas object from these new bounds
        // Canvas<T> toReturnCanvas = canvasComparator != null ? Canvas.of(xBoundMap, canvasComparator) : Canvas.of(xBoundMap); //Refactored

        // //iteratore through this sets in the canvas' map to have vertical slices
        // Set<Map.Entry<T, NavigableSet<T>>> pairSets = toReturnCanvas.pixels.entrySet();
        // for(var x: pairSets) {
        //     NavigableSet<T> toPutSet = x.getValue().subSet(fakeBot, true, fakeTop, true);
        //     toReturnCanvas.pixels.put(x.getKey(), toPutSet);
        // }
        // //return map
        // return toReturnCanvas;
    }

    /**
     * Takes in a minimum and maximum range of x values and returns
     * a new canvas with the pixels between the minimum and maximum inclusive
     * @param xMin
     * @param xMax
     * @return - Canvas object with pixels within the range
     * @throws Exception
     */
    public final Canvas<T> subCanvas(T xMin, T xMax) throws Exception {
        //Refactor
        T fakeMin = canvasComparator != null ? (canvasComparator.compare(xMin, xMax) < 0 ? xMin : xMax) : xMin;
        T fakeMax = canvasComparator != null ? (canvasComparator.compare(xMin, xMax) > 0 ? xMin : xMax) : xMax;
        return canvasComparator != null ? Canvas.of(Canvas.unsortedMap(pixels.subMap(fakeMin, true, fakeMax, true)), canvasComparator) : Canvas.of(Canvas.unsortedMap(pixels.subMap(fakeMin, true, fakeMax, true))); //Refactored
    }

    /**
     * Method to add a point to our canvas
     * Makes sure the coordinate given is not null and does not already exist in the canvas
     * If key exists append to corresponding set, otherwise make key set pair, then recursively
     * append to corresponding set
     * @param xCoord - x coordinate value of the 2D point
     * @param yCoord - y coordinate value of teh 2D point
     * @return - return a boolean indicating whether or not the point was successfully added
     */
    public final boolean add(T xCoord, T yCoord) {
        //return (nonNull(xCoord, yCoord) && !hasPoint(xCoord, yCoord)) && pixels.containsKey(xCoord) ? pixels.get(xCoord).add(yCoord) : pixels.put(xCoord, new TreeSet<T>()) == null && this.add(xCoord, yCoord);
        return (nonNull(xCoord, yCoord) && !hasPoint(xCoord, yCoord)) && pixels.containsKey(xCoord) ? pixels.get(xCoord).add(yCoord) : (canvasComparator != null ? pixels.put(xCoord, new TreeSet<T>(canvasComparator)) == null && this.add(xCoord, yCoord) : pixels.put(xCoord, new TreeSet<T>()) == null && this.add(xCoord, yCoord));
    }

     /**
     * NEW: ASSGN 10 REFACTOR WITH STREAMS
     * Helper method that takes in 2 objects and verifies that neither is null
     * @param obj1
     * @param obj2
     * @return
     */
    private static final boolean nonNull(Object obj1, Object obj2) {
        return Stream.of(obj1, obj2).allMatch(r -> r != null);
        //return obj1 != null && obj2 != null;

    }

    /**
     * Takes in a Rectangle and returns how many points are within or on that rectangle
     * @param rect
     * @return - long value indicating the amount of points in the rectangle
     * @throws Exception
     */
    public final long sliceCount(Rectangle<T> rect) throws Exception {
        return this.slice(rect).pointCount();
    }

    /**
     * Method returns a view of the horizontal coordinates in the 
     * map as a set.
     * @return
     */
    public final Set<T> xSet() {
        return pixels.keySet();
    }

    /**
     * A method who takes in an horizontal coordinate and returns
     * it's corresponding set of vertical coordinates 
     * (if it exists, otherwise return unmodifiable empty set)
     * @param xCoord - generic type representing the x coordinate
     * @return - corresponding y set if exists, otherwise unmodifiable empty set
     */
    public final Set<T> ySet(T xCoord) {
        return pixels.get(xCoord) != null ? pixels.get(xCoord) : Collections.emptySet();
    }

    /**
     * Method to return a boolean indicating whether or not a given point 
     * exists in our canvas
     * @param xCoord - generic type representing the x coordinate
     * @param yCoord - generic type representing the y coordinate
     * @return - True if point exists in map, False otherwise
     */
    public final boolean hasPoint(T xCoord, T yCoord) {
        return pixels.get(xCoord) != null && pixels.get(xCoord).contains(yCoord);
    }

    /**
     * REFACTORED WITH STREAMS
     * Returns the total number of points in the map
     * @return - type long
     */
    public final long pointCount() {
        return pixels.values().stream().map(c -> c.stream().count()).reduce(0L, Long::sum);
        // Collection<NavigableSet<T>> pairs = pixels.values();
        // long count = 0;
        // for(var trav : pairs) {
        //     count += trav.size();
        // }
        // return count;
    }

    private record Coordinate<T>(T horizontal, T vertical) {}
    /**
     * REFACTORED WITH STREAMS
     * Perform transformations to the coordinates in a canvas.
     * Take in 2 arguments, a bifunction that transforms the coordinates
     * and returns a new x value, and another bifunction that returns a new y value
     * @param transformX - maps coordinate to new x value
     * @param transfromY - maps coordinate to new y value
     * @param transformY 
     * @return - new Canvas
     * @throws Exception
     */
    public final Canvas<T> transform(BiFunction<T, T, T> transformX, BiFunction<T, T, T> transformY) throws Exception{
        return this.pixels.keySet().stream()
            .flatMap(x -> pixels.get(x).stream()
                .map(y -> new Coordinate<>(transformX.apply(x,y), transformY.apply(x, y))))
            .collect(() -> new Canvas<>(new TreeMap<>(), this.canvasComparator),
                (c, pair) -> c.add(pair.horizontal(), pair.vertical()),
                (c1, c2) -> c1.pixels.forEach((key, set) -> set.forEach(yVal -> c2.add(key,yVal))));
    
        //Code Explanation
        //pixels.keySet().stream() gives stream of T's, the keys (x values)
        //.flatMap(x -> pixels.get(x).stream() means for each x we get a stream of its corresponding y's
        //.map(y -> new Coordinate<>(transformX.apply(x,y), transformY.apply(x, y)))) means we now have a stream of transformed coordinates
        //collect(R, T) performs a collection of a stream of T, transformed coordinates
        //() -> new Canvas<>(new TreeMap<>(), this.canvasComparator) provides 
        //  R, our results container, is supplied by new Canvas Objects with empty Maps but same comparator
        //(c, pair) -> c1.add(pair.horizontal(), pair.vertical()) takes a transformed coordinate
        //  and adds the pair to our c - results container
        //(c1, c2) -> c1.pixels.forEach((x, set) -> set.forEach(y -> c2.add(x,y)))) simply copies one result container to another

        //OLD CODE
        // Canvas<T> toReturnCanvas = Canvas.of(new TreeMap<>());
        // for(var entry: pixels.entrySet()) {
        //     for(var yValue: entry.getValue()) {
        //         toReturnCanvas.add(transformX.apply(entry.getKey(), yValue), transformY.apply(entry.getKey(), yValue));
        //     }
        // }
        // return toReturnCanvas;
    }
 }
