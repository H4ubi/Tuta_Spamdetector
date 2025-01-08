package de.jhohlbein.tuta.detector;

import de.jhohlbein.tuta.detector.data.Email;

import java.util.List;

public interface SpamDetector {

    /**
     * Takes a list of {@link Email}s and calculates the scoring for each based on similarity to the other emails.
     * The input list will be returned as result with the spamScoring set on each email.
     *
     * @param emails The email list, that should be calculated
     * @return the resulting list of {@link Email}s, with the scores set
     */
    List<Email> calculateSimilarityScoringAndCategorizeSpamForEmails(List<Email> emails);

    /**
     * Calculates the similarity between two
     *
     * @param doc1 The first {@link String}
     * @param doc2 The second {@link String}
     * @return The similarity as {@link Double}
     */
    Double calculateSimilarityBetweenTwoStrings(String doc1, String doc2);
}
