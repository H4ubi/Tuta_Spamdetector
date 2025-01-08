package de.jhohlbein.tuta.detector.impl;

import de.jhohlbein.tuta.detector.data.Email;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

class DefaultSpamDetectorTest {

    private final DefaultSpamDetector testMe = new DefaultSpamDetector();

    @Test
    void calculateSimilarityScoringAndCategorizeSpamForEmails_whenTwoOfThreeEmailBodiesAreSimilar_thenCategorizeTwoAsSpamAndSetCalculatedValues() {
        // GIVEN
        final String firstBody = """
                Can curiosity may end shameless explained. True high on said mr on come. An do \
                mr design at little myself wholly entire though. Attended of on stronger or mr pleasure. Rich \
                four like real yet west get. Felicity in dwelling to drawings. His pleasure new steepest for \
                reserved formerly disposed jennings.""";
        final String secondBody = """
                Had strictly mrs handsome mistaken cheerful. We it so if resolution invitation \
                remarkably unpleasant conviction. As into ye then form. To easy five less if rose were. Now set \
                offended own out required entirely. Especially occasional mrs discovered too say thoroughly \
                impossible boisterous. My head when real no he high rich at with. After so power of young as. Bore \
                year does has get long fat cold saw neat. Put boy carried chiefly shy general.""";
        final String thirdBody = """
                Can curiosity may end shameless explained. True high on said mr on come. An do \
                mr entire though. Attended of on stronger or mr pleasure. Rich four like real yet west get.
                Felicity in dwelling to drawings. His pleasure new steepest for reserved formerly disposed jennings.""";
        // WHEN
        final List<Email> categorizedEmails =
                testMe.calculateSimilarityScoringAndCategorizeSpamForEmails(asList(createEmailForString(firstBody),
                        createEmailForString(secondBody), createEmailForString(thirdBody)));
        // THEN
        assertTrue(categorizedEmails.get(0).isSpam(), "Email with index 0 should be categorized as spam.");
        assertEquals(categorizedEmails.get(0).getSpamScoring(), categorizedEmails.get(1).getSpamScoring(),
                "Emails " + "with indexes 1 and 2 should have the same spam scoring");
        assertTrue(categorizedEmails.get(1).isSpam(), "Email with index 1 should be categorized as spam.");
        assertFalse(categorizedEmails.get(2).isSpam(), "Email with index 2 should not be categorized as spam.");
    }

    @Test
    void calculateSimilarityScoringAndCategorizeSpamForEmails_whenThreeOfFourEmailBodiesAreSimilar_thenCategorizeThreeAsSpamAndSetCalculatedValues() {
        // GIVEN
        final String firstBody = """
                Can curiosity may end shameless explained. True high on said mr on come. An do \
                mr design at little myself wholly entire though. Attended of on stronger or mr pleasure. Rich \
                four like real yet west get. Felicity in dwelling to drawings. His pleasure new steepest for \
                reserved formerly disposed jennings.""";
        final String secondBody = """
                Had strictly mrs handsome mistaken cheerful. We it so if resolution invitation \
                remarkably unpleasant conviction. As into ye then form. To easy five less if rose were. Now set \
                offended own out required entirely. Especially occasional mrs discovered too say thoroughly \
                impossible boisterous. My head when real no he high rich at with. After so power of young as. Bore \
                year does has get long fat cold saw neat. Put boy carried chiefly shy general.""";
        final String thirdBody = """
                Can curiosity may end shameless explained. True high on said mr on come. An do \
                mr entire though. Attended of on stronger or mr pleasure. Rich four like real yet west get.
                Felicity in dwelling to drawings. His pleasure new steepest for reserved formerly disposed jennings.""";
        final String fourthBody = """
                Can curiosity may end shameless explained. True high on said mr on come. An do \
                mr entire though. Attended of on stronger or mr pleasure. Rich four like real yet west get.
                Felicity in to drawings. His pleasure new steepest for reserved formerly disposed jennings.""";
        // WHEN
        final List<Email> categorizedEmails =
                testMe.calculateSimilarityScoringAndCategorizeSpamForEmails(asList(createEmailForString(firstBody),
                        createEmailForString(secondBody), createEmailForString(thirdBody),
                        createEmailForString(fourthBody)));
//        // THEN
        assertTrue(categorizedEmails.get(0).isSpam(), "Email with index 0 should be categorized as spam.");
        assertEquals(categorizedEmails.get(0).getSpamScoring(), categorizedEmails.get(1).getSpamScoring(),
                "Emails " + "with indexes 1 and 2 should have the same spam scoring");
        assertTrue(categorizedEmails.get(1).isSpam(), "Email with index 1 should be categorized as spam.");
        assertTrue(categorizedEmails.get(2).isSpam(), "Email with index 1 should be categorized as spam.");
        assertTrue(categorizedEmails.get(2).getSpamScoring() < categorizedEmails.get(1).getSpamScoring(), "Email " +
                "with" + " index 1 should have a lower spam scoring than 1 and 2.");
        assertFalse(categorizedEmails.get(3).isSpam(), "Email with index 2 should not be categorized as spam.");
    }

    @Test
    void calculateSimilarity_whenStringsIdentical_thenSimilarityBetweenTwoStringsIs1() {
        // GIVEN
        final String input = """
                Had strictly mrs handsome mistaken cheerful. We it so if resolution invitation \
                remarkably unpleasant conviction. As into ye then form. To easy five less if rose were. Now set \
                offended own out required entirely. Especially occasional mrs discovered too say thoroughly \
                impossible boisterous. My head when real no he high rich at with. After so power of young as. Bore \
                year does has get long fat cold saw neat. Put boy carried chiefly shy general.""";
        // WHEN
        final Double similarity = testMe.calculateSimilarityBetweenTwoStrings(input, input);
        // THEN
        assertEquals(1.0, similarity, 0.000000000000001d, "The similarity should be 1 as the strings are identical");
    }

    @Test
    void calculateSimilarity_whenTwoDifferentStrings_thenSimilarityBetweenTwoStringsIsBetween0And1() {
        // GIVEN
        final String firstInput = """
                Can curiosity may end shameless explained. True high on said mr on come. An do \
                mr design at little myself wholly entire though. Attended of on stronger or mr pleasure. Rich \
                four like real yet west get. Felicity in dwelling to drawings. His pleasure new steepest for \
                reserved formerly disposed jennings.""";
        final String secondInput = """
                Had strictly mrs handsome mistaken cheerful. We it so if resolution invitation \
                remarkably unpleasant conviction. As into ye then form. To easy five less if rose were. Now set \
                offended own out required entirely. Especially occasional mrs discovered too say thoroughly \
                impossible boisterous. My head when real no he high rich at with. After so power of young as. Bore \
                year does has get long fat cold saw neat. Put boy carried chiefly shy general.""";
        // WHEN
        final Double similarity = testMe.calculateSimilarityBetweenTwoStrings(firstInput, secondInput);
        // THEN
        assertTrue(similarity > 0, "The similarity should be between 0 and 1");
        assertTrue(similarity < 1, "The similarity should be between 0 and 1");
    }

    @Test
    void calculateSimilarity_whenTwoDifferentStrings_thenSimilarityCalculationIsCommutative() {
        // GIVEN
        final String firstInput = """
                Can curiosity may end shameless explained. True high on said mr on come. An do \
                mr design at little myself wholly entire though. Attended of on stronger or mr pleasure. Rich \
                four like real yet west get. Felicity in dwelling to drawings. His pleasure new steepest for \
                reserved formerly disposed jennings.""";
        final String secondInput = """
                Had strictly mrs handsome mistaken cheerful. We it so if resolution invitation \
                remarkably unpleasant conviction. As into ye then form. To easy five less if rose were. Now set \
                offended own out required entirely. Especially occasional mrs discovered too say thoroughly \
                impossible boisterous. My head when real no he high rich at with. After so power of young as. Bore \
                year does has get long fat cold saw neat. Put boy carried chiefly shy general.""";
        // WHEN
        final Double firstSimilarity = testMe.calculateSimilarityBetweenTwoStrings(firstInput, secondInput);
        final Double secondSimilarity = testMe.calculateSimilarityBetweenTwoStrings(secondInput, firstInput);
        // THEN
        assertEquals(firstSimilarity, secondSimilarity, "The similarities should be the same.");
    }

    @Test
    void transformStringToVector_whenStringIsGiven_thenTransformItToVector() {
        // GIVEN
        final String input = "This is a document. It is the best document ever.";
        // WHEN
        final Map<CharSequence, Integer> vector = testMe.transformStringToVector(input);
        // THEN
        assertEquals(3, vector.size(), "The size of vector should be 3.");
        assertTrue(vector.containsKey("document"), "The word 'document' was not properly indexed.");
        assertEquals(2, vector.get("document"), "The value for 'document' should be 2.");
        assertTrue(vector.containsKey("best"), "The word 'best' was not properly indexed.");
        assertEquals(1, vector.get("best"), "The value for 'best' should be 1.");
        assertTrue(vector.containsKey("ever"), "The word 'ever' was not properly indexed.");
        assertEquals(1, vector.get("ever"), "The value for 'ever' should be 1.");
    }

    @Test
    void removeStopWords_whenStopwordsAreInTheList_thenRemoveStopwords() {
        // GIVEN
        final List<String> tokens = new ArrayList<>(asList("this", "is", "a", "document"));
        // WHEN
        final List<String> clearedTokens = testMe.removeStopWords(tokens);
        // THEN
        assertEquals(1, clearedTokens.size(), "The size of tokens should be 1. No stopwords have been removed.");
        assertEquals("document", clearedTokens.get(0), "The term document should be in the list of tokens.");
    }

    private Email createEmailForString(final String emailBody) {
        final Email email = new Email();
        email.setBody(emailBody);
        email.setId(UUID.randomUUID().toString());
        return email;
    }
}