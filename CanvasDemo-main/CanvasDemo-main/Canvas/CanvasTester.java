/**
 * PROJECT PORTION AND HOMEWORK AND PROGRAMMING TESTER:
 * JUNIT Testing class for Canvas
 * @author Ben Luo
 * bsl47
 * :)
 */
import static org.junit.Assert.*;

// import java.math.BigDecimal;
// import java.util.ArrayList;
//import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
//import java.util.Set;
import java.util.*;
//import java.util.function.*;

import org.junit.*;

//PROJECT PORTION TESTING
public class CanvasTester {
    @Test
    public void generalTest() throws Exception{//Sorry for messy testing! this was just something scrapped together to quickly check
        //Make a Map of these mappings
        // (-1, 0), (0, -3), (0, 4), (2, 0)
        Map<Integer, Set<Integer>> objMap = Map.of(-1, Set.of(0), 0, Set.of(-3, 4), 2, Set.of(0));

        //Make a Rectangle of top: y = 2 | bot: y = -3 | left: x = -5 | right: x = 2
        Rectangle<Integer> rectangle = Rectangle.of(2, -3, -5, 2);
        
        //Make a Canvas Object WITH EMPTY OPTIONAL 
        Canvas<Integer> example = Canvas.of(objMap, Optional.empty());
        
        //OLD testers but show that even though we changed to navigable maps and sets we maintained full functionality
        //OLD Test add method and pointcount method. 
        assertEquals(4, example.pointCount());
        example.add(10,25);
        assertEquals(5, example.pointCount());

        //OLD Make sure point exists OLD
        assertEquals(true, example.hasPoint(10, 25));

        //OLD Make sure hasPoint() works
        assertEquals(false, example.hasPoint(10, 20));
        assertEquals(false, example.hasPoint(9, 25));
        
        //SLICE and SUBCANVAS
        //OLD assertEquals(true, example.hasPoint(10, 25));
        Canvas<Integer> exampleTwo = example.slice(rectangle);
        assertEquals(3, exampleTwo.pointCount());

        //OLD testing subCanvas method. Let x min = -1 and x max = 25, should get 5 points in new canvas
        Canvas<Integer> exampleThree = example.subCanvas(-1, 25);
        assertEquals(5, exampleThree.pointCount());

        //OLD, TESTING TRANSFORMATIONS
        Map<Double, Set<Double>> objMap2 = Map.of(-1.0, Set.of(0.0), 0.0, Set.of(-3.0, 4.0), 2.0, Set.of(0.0));
        Canvas<Double> newExampleCanvas = Canvas.of(objMap2);

        //OLD, Test translate method
        Canvas<Double> translateExampleCanvas = CanvasTransformer.translate(newExampleCanvas, 5.0, 6.0);
        assertEquals(true, translateExampleCanvas.hasPoint(4.0, 6.0));

        //Test Rotation method
        Canvas<Double> rotationExampleCanvas = CanvasTransformer.rotate(newExampleCanvas, 90.0);
        System.out.println(rotationExampleCanvas.xSet());
        assertEquals(true, rotationExampleCanvas.hasPoint(0.0, -1.0));

        //Test Magnify method
        Canvas<Double> magnifyExampleCanvas = CanvasTransformer.magnify(newExampleCanvas, 5.0);
        assertEquals(true, magnifyExampleCanvas.hasPoint(-5.0, 0.0));

        //Canvas ordered by descending
        //Make a Map of these mappings
        // (-1, 0), (0, -3), (0, 4), (2, 0)
        Map<Integer, Set<Integer>> newObjMap = Map.of(-1, Set.of(0), 0, Set.of(-3, 4), 2, Set.of(0));

        //Make a Rectangle of top: y = 2 | bot: y = -3 | left: x = -5 | right: x = 2
        Rectangle<Integer> newRectangle = Rectangle.of(2, -3, -5, 2);
        
        //Make a Canvas Object, x's in reverse order and y's in reverse order
        Optional<Comparator<Integer>> demoOptional = Optional.of(Comparator.reverseOrder());
        Canvas<Integer> newExample = Canvas.of(newObjMap, demoOptional);
        
        //Refactored Slice
        Canvas<Integer> sliceComparatorTest = newExample.slice(newRectangle);
        assertEquals(3, sliceComparatorTest.pointCount());

        //Refactored subcanvas
        Canvas<Integer> testSub = newExample.subCanvas(-1, 1);
        assertEquals(3, testSub.pointCount());
        
    }

    //Test that our rectangle throws exceptions
    @Test(expected = Exception.class)
    public void testForBotTop() throws Exception {
        //testing that that if bottom value is higher/greater than top then correctly throws exception
        Rectangle.of(50,100,2,5);
    }

    @Test(expected = Exception.class)
    public void testForLefRightMisMatch() throws Exception {
        //verify our null methods
        Rectangle.of(100,50,30,15);
    }

    @Test(expected = Exception.class)
    public void testNullRectangle() throws Exception {
        //testing that that if left value is higher/greater than right then correctly throws exception
        Rectangle.of(100,50,30,null);
    }
    
}
