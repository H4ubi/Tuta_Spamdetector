package de.jhohlbein.tuta.detector.impl;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefaultSpamDetectorTest {

    private final DefaultSpamDetector testMe = new DefaultSpamDetector();

    @Test
    void findDocumentClustersBySimilarity_whenTwoOfThreeDocumentsAreSimilar_thenReturnOneCluster() {
        // GIVEN
        final String firstDocument = """
                Can curiosity may end shameless explained. True high on said mr on come. An do \
                mr design at little myself wholly entire though. Attended of on stronger or mr pleasure. Rich \
                four like real yet west get. Felicity in dwelling to drawings. His pleasure new steepest for \
                reserved formerly disposed jennings.""";
        final String secondDocument = """
                Had strictly mrs handsome mistaken cheerful. We it so if resolution invitation \
                remarkably unpleasant conviction. As into ye then form. To easy five less if rose were. Now set \
                offended own out required entirely. Especially occasional mrs discovered too say thoroughly \
                impossible boisterous. My head when real no he high rich at with. After so power of young as. Bore \
                year does has get long fat cold saw neat. Put boy carried chiefly shy general.""";
        final String thirdDocument = """
                Can curiosity may end shameless explained. True high on said mr on come. An do \
                mr entire though. Attended of on stronger or mr pleasure. Rich four like real yet west get.
                Felicity in dwelling to drawings. His pleasure new steepest for reserved formerly disposed jennings.""";
        // WHEN
        final List<Set<Integer>> clusters = testMe.findDocumentClustersBySimilarity(of(firstDocument, secondDocument,
                thirdDocument));
        // THEN
        assertEquals(1, clusters.size(), "One cluster should be found");
        assertTrue(clusters.get(0).contains(0), "Document with index 0 should be in the cluster.");
        assertTrue(clusters.get(0).contains(2), "Document with index 2 should be in the cluster.");
    }

    @Test
    void findDocumentClustersBySimilarity_whenThreeOfFourDocumentsAreSimilar_thenReturnOneCluster() {
        // GIVEN
        final String firstDocument = """
                Can curiosity may end shameless explained. True high on said mr on come. An do \
                mr design at little myself wholly entire though. Attended of on stronger or mr pleasure. Rich \
                four like real yet west get. Felicity in dwelling to drawings. His pleasure new steepest for \
                reserved formerly disposed jennings.""";
        final String secondDocument = """
                Had strictly mrs handsome mistaken cheerful. We it so if resolution invitation \
                remarkably unpleasant conviction. As into ye then form. To easy five less if rose were. Now set \
                offended own out required entirely. Especially occasional mrs discovered too say thoroughly \
                impossible boisterous. My head when real no he high rich at with. After so power of young as. Bore \
                year does has get long fat cold saw neat. Put boy carried chiefly shy general.""";
        final String thirdDocument = """
                Can curiosity may end shameless explained. True high on said mr on come. An do \
                mr entire though. Attended of on stronger or mr pleasure. Rich four like real yet west get.
                Felicity in dwelling to drawings. His pleasure new steepest for reserved formerly disposed jennings.""";
        final String fourthDocument = """
                Can curiosity may end shameless explained. True high on said mr on come. An do \
                mr entire though. Attended of on stronger or mr pleasure. Rich four like real yet west get.
                Felicity in to drawings. His pleasure new steepest for reserved formerly disposed jennings.""";
        // WHEN
        final List<Set<Integer>> clusters = testMe.findDocumentClustersBySimilarity(of(firstDocument, secondDocument,
                thirdDocument, fourthDocument));
        // THEN
        assertEquals(1, clusters.size(), "One cluster should be found");
        final Set<Integer> clusterFound = clusters.get(0);
        assertTrue(clusterFound.contains(0), "Document with index 0 should be in the cluster.");
        assertTrue(clusterFound.contains(2), "Document with index 2 should be in the cluster.");
        assertTrue(clusterFound.contains(3), "Document with index 3 should be in the cluster.");
    }

    @Test
    void calculateSimilarity_whenStringsIdentical_thenSimilarityBetweenTwoStringsIs1() {
        // GIVEN
        final String document = """
                Had strictly mrs handsome mistaken cheerful. We it so if resolution invitation \
                remarkably unpleasant conviction. As into ye then form. To easy five less if rose were. Now set \
                offended own out required entirely. Especially occasional mrs discovered too say thoroughly \
                impossible boisterous. My head when real no he high rich at with. After so power of young as. Bore \
                year does has get long fat cold saw neat. Put boy carried chiefly shy general.""";
        // WHEN
        final Double similarity = testMe.calculateSimilarityBetweenTwoStrings(document, document);
        // THEN
        assertEquals(1.0, similarity, 0.000000000000001d, "The similarity should be 1 as the strings are identical");
    }

    @Test
    void calculateSimilarity_whenTwoDifferentStrings_thenSimilarityBetweenTwoStringsIsBetween0And1() {
        // GIVEN
        final String firstDocument = """
                Can curiosity may end shameless explained. True high on said mr on come. An do \
                mr design at little myself wholly entire though. Attended of on stronger or mr pleasure. Rich \
                four like real yet west get. Felicity in dwelling to drawings. His pleasure new steepest for \
                reserved formerly disposed jennings.""";
        final String secondDocument = """
                Had strictly mrs handsome mistaken cheerful. We it so if resolution invitation \
                remarkably unpleasant conviction. As into ye then form. To easy five less if rose were. Now set \
                offended own out required entirely. Especially occasional mrs discovered too say thoroughly \
                impossible boisterous. My head when real no he high rich at with. After so power of young as. Bore \
                year does has get long fat cold saw neat. Put boy carried chiefly shy general.""";
        // WHEN
        final Double similarity = testMe.calculateSimilarityBetweenTwoStrings(firstDocument, secondDocument);
        // THEN
        assertTrue(similarity > 0, "The similarity should be between 0 and 1");
        assertTrue(similarity < 1, "The similarity should be between 0 and 1");
    }

    @Test
    void transformStringToVector_whenStringIsGiven_thenTransformItToVector() {
        // GIVEN
        final String document = "This is a document. It is the best document ever.";
        // WHEN
        final Map<CharSequence, Integer> vector = testMe.transformStringToVector(document);
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
}