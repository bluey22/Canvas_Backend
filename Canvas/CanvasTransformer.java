import java.math.BigDecimal;
import java.math.RoundingMode;
/**
 * @author Ben Luo
 * bsl47
 * :)
 */
public class CanvasTransformer {
    static Canvas<Double> translate(Canvas<Double> canvas, Double xCoord, Double yCoord) throws Exception {
        return canvas.transform((x,y) -> x + xCoord, (x,y) -> y + yCoord);
    }

    //Ax = b
    //x is our matrix of (x,y)
    //A, our transformation matrix, is 
    // | cos -sin|
    // | sin  cos|
    // b is our resulting coordinate
    static Canvas<Double> rotate(Canvas<Double> canvas, Double angle) throws Exception {
        return canvas.transform((x,y) -> new BigDecimal(x * Math.cos(Math.toRadians(angle)) - y * Math.sin(Math.toRadians(angle))).setScale(5, RoundingMode.HALF_UP).doubleValue(), 
            (x,y) -> new BigDecimal(x * Math.sin(Math.toRadians(angle)) + y * Math.cos(Math.toRadians(angle))).setScale(5, RoundingMode.HALF_UP).doubleValue());
    }

    static Canvas<Double> magnify(Canvas<Double> canvas, Double factor) throws Exception {
        return canvas.transform((x,y) -> x*factor, (x,y) -> y*factor);
    }
}