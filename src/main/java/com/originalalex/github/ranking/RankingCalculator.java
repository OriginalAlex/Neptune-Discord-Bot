package com.originalalex.github.ranking;

public class RankingCalculator {

    /*
    This ranking system uses the ELO ranking system. See: https://en.wikipedia.org/wiki/Elo_rating_system#Performance_rating for more information.
     */

    public int calculateNewRating(int ratersRating, int currentRating, int positiveRatings, int negativeRatings, boolean positiveIncrememnted) {
        int total = positiveIncrememnted ? getTotalOfRaters(currentRating, positiveRatings-1, negativeRatings) : getTotalOfRaters(currentRating, positiveRatings, negativeRatings-1); // Get the total from the old rep values
        return (total + ratersRating + 400 * (positiveRatings - negativeRatings)) / (positiveRatings + negativeRatings);
    }

    private int getTotalOfRaters(int currentRating, int positiveRatings, int negRatings) {
        return currentRating * (positiveRatings + negRatings) - (400 * (positiveRatings - negRatings));
    }

}
