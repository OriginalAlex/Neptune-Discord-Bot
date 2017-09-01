package com.originalalex.github.ranking;

public class RankingCalculator {

    /*
    This ranking system uses the ELO ranking system. See: https://en.wikipedia.org/wiki/Elo_rating_system#Performance_rating for more information.
     */
    private static final int K_FACTOR = 32; // The K_FACTOR which Chess ranking systems use

    public int calculateNewRating(int current, boolean positive) {
        return (positive) ? current+1 : current-1;
    }

    // Unused ELO code - changed to a simple +/- rating

//    private int updated(int ratersRating, int currentRating, boolean positive) {
//
//        double r1 = Math.pow(10, ratersRating/400);
//        double r2 = Math.pow(10, currentRating / 400);
//
//        double expectedRating1 = r1 / (r1 + r2);
//        double expectedRating2 = r2 / (r1 + r2);
//
//        if (positive) {
//            return (int) (r2 + K_FACTOR * (1 - expectedRating2));
//        } else {
//            return  (int) (r2 + K_FACTOR * (0 - expectedRating1));
//        }
//    }
//
//    private int getTotalOfRaters(int currentRating, int positiveRatings, int negRatings) {
//        return currentRating * (positiveRatings + negRatings) - (400 * (positiveRatings - negRatings));
//    }

}
