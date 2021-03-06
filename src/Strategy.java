/**
 *
 * @author Team Gredona
 *
 */
public abstract class Strategy {

    public static final int BUY = -1;
    public static final int HOLD = 0;
    public static final int SELL = 1;
    public static final int FAST_PERIOD = 5;
    public static final int SLOW_PERIOD = 20;
    public static final int SIMPLE_MOVING_AVERAGE = 0;
    public static final int LINEAR_WEIGHTED_MOVING_AVERAGE = 1;
    public static final int EXPONENTIAL_MOVING_AVERAGE = 2;
    public static final int TRIANGULAR_MOVING_AVERAGE = 3;
    /**
     * The type of strategy
     */
    String type;
    int typeInt;
    /**
     * Instantiates a "first in first out" buffer of size SLOW_PERIOD = 20 or
     * FAST_PERIOD = 5 that contains floats. When the buffer is full (ie size()
     * = 20 or 5, respectively), the head is removed to create space
     */
    protected CircularFIFOBuffer<Float> slowDataBuffer = new CircularFIFOBuffer(SLOW_PERIOD);
    protected CircularFIFOBuffer<Float> fastDataBuffer = new CircularFIFOBuffer(FAST_PERIOD);
    protected float currentFastMovingAverage, currentSlowMovingAverage;
    protected float previousFastMovingAverage, previousSlowMovingAverage;
    protected float oldestFastDatapoint, oldestSlowDatapoint;

    /**
     * Constructor
     */
    public Strategy() {
    }

    /**
     * Updates the strategy with a new data point.
     *
     * @return the action recommended by the strategy ( BUY, HOLD, or SELL )
     */
    public int update(float newDataPoint) {
        updateSlowDataBuffer(newDataPoint);
        updateFastDataBuffer(newDataPoint);
        previousSlowMovingAverage = currentSlowMovingAverage;
        currentSlowMovingAverage = computeSlowMovingAverage();
        previousFastMovingAverage = currentFastMovingAverage;
        currentFastMovingAverage = computeFastMovingAverage();
        return decideTradingAction();
    }

    /**
     * @param newDataPoint
     */
    protected void updateFastDataBuffer(float newDataPoint) {
        if (fastDataBuffer.size() != 0) {
            oldestFastDatapoint = fastDataBuffer.peek();
        }

        fastDataBuffer.add(newDataPoint);
    }

    /**
     * @param newDataPoint
     */
    protected void updateSlowDataBuffer(float newDataPoint) {
        if (slowDataBuffer.size() != 0) {
            oldestSlowDatapoint = slowDataBuffer.peek();
        }
        slowDataBuffer.add(newDataPoint);
    }

    /**
     *
     * @return the average over the last 20
     */
    protected abstract float computeSlowMovingAverage();

    /**
     * @return the average over the last 5
     */
    protected abstract float computeFastMovingAverage();

    /**
     * Detects crossover. If there is a crossover, this method decides whether
     * or not to buy or sell. Otherwise it holds.
     *
     * @return the strategy's recommended course of action
     */
    protected int decideTradingAction() {
	/**
        if (currentFastMovingAverage == currentSlowMovingAverage) {
            if (currentFastMovingAverage > previousFastMovingAverage) {
                return BUY;
            }
            if (currentFastMovingAverage < previousFastMovingAverage) {
                return SELL;
            }
        }
**/
        if (previousFastMovingAverage > previousSlowMovingAverage && currentFastMovingAverage < currentSlowMovingAverage) {
            return BUY;
        }

        if (previousFastMovingAverage < previousSlowMovingAverage && currentFastMovingAverage > currentSlowMovingAverage) {
            return SELL;

        }
        return HOLD;
    }

    /**
     *
     * @return an int representation of the type 
     * 
     * SIMPLE_MOVING_AVERAGE = 0,
     * LINEAR_WEIGHTED_MOVING_AVERAGE = 1,
     * EXPONENTIAL_MOVING_AVERAGE = 2,
     * TRIANGULAR_MOVING_AVERAGE = 3
     *
     */
    public int getTypeInt() {
        return typeInt;
    }

    /**
     * @return the type of strategy as a String
     */
    @Override
    public String toString() {
        return type;
    }
}
