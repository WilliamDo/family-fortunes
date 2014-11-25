package uk.co.ultimaspin.familyfortunes.data;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by william on 25/11/2014.
 */
public class PrizeAnswerUtil {

    private static final long INTERVAL_PRIZE_ANSWER = 60L * 1000L;

    private AtomicLong lastPrizeAnswerTime;
    // Set current time interval;

    private static PrizeAnswerUtil prizeAnswerUtil;

    private PrizeAnswerUtil() {
        lastPrizeAnswerTime = new AtomicLong(System.currentTimeMillis());
    }

    public boolean isPrizeAnswerTime() {

        long currentTime = System.currentTimeMillis();

        boolean isPrizeAnswerTime = false;

        if (currentTime - lastPrizeAnswerTime.get() > INTERVAL_PRIZE_ANSWER) {
            isPrizeAnswerTime = true;
            lastPrizeAnswerTime.set(currentTime);
        }

        return isPrizeAnswerTime;

    }

    public static PrizeAnswerUtil getInstance() {

        if (prizeAnswerUtil == null) {
            prizeAnswerUtil = new PrizeAnswerUtil();
        }

        return prizeAnswerUtil;
    }

}
