package uk.co.ultimaspin.familyfortunes.data;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by william on 25/11/2014.
 */
public class PrizeAnswerUtil {

    private static final long INTERVAL_PRIZE_ANSWER = 30L * 60L * 1000L;

    private AtomicLong timeInterval;

    private AtomicBoolean prizeAnswerEnabled;

    private AtomicLong lastPrizeAnswerTime;
    // Set current time interval;

    private static PrizeAnswerUtil prizeAnswerUtil;

    private PrizeAnswerUtil() {
        lastPrizeAnswerTime = new AtomicLong(System.currentTimeMillis());
        prizeAnswerEnabled = new AtomicBoolean(false);
        timeInterval = new AtomicLong(INTERVAL_PRIZE_ANSWER);
    }

    public boolean isPrizeAnswerTime() {

        long currentTime = System.currentTimeMillis();

        boolean isPrizeAnswerTime = false;

        if (currentTime - lastPrizeAnswerTime.get() > timeInterval.get()) {
            isPrizeAnswerTime = true;
            lastPrizeAnswerTime.set(currentTime);
        }

        return isPrizeAnswerTime && prizeAnswerEnabled.get();

    }

    public void setEnabled(Boolean enabled) {
        prizeAnswerEnabled.set(enabled);
        if (enabled) {
            // Reset the last prize answer time to start from now
            lastPrizeAnswerTime.set(System.currentTimeMillis());
        }
    }

    public void setTimerInterval(long intervalInMinutes) {
        timeInterval.set(intervalInMinutes * 60L * 1000L);
        lastPrizeAnswerTime.set(System.currentTimeMillis());
    }

    public static PrizeAnswerUtil getInstance() {

        if (prizeAnswerUtil == null) {
            prizeAnswerUtil = new PrizeAnswerUtil();
        }

        return prizeAnswerUtil;
    }

}
