package com.rsavto.categories.util;

/**
 * @author mfedechko
 */
public class RsAvtoUtils {

    public static void countDown(final int seconds) {

        System.out.println("Starting countdown...");

        for (int i = seconds; i >= 0; i--) {
            // Create the countdown string
            String countdownString = "Time remaining: " + i + " seconds";
            System.out.println(countdownString);

            if (i > 0) {
                try {
                    // Pause for 1 second (1000 milliseconds)
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("Countdown finished!");
    }

}
