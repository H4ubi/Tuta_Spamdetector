package de.jhohlbein.tuta.detector;

import java.util.List;
import java.util.Set;

public interface SpamDetector {

    /**
     * Takes a list of documents as {{@link String}} and finds clusters of similar documents. The results will contain a
     * list of all indexes of documents in the original list, that are part of a cluster
     *
     * @param documents The document list of clusters to find
     * @return the resulting list of clusters, which are represented by {{@link Set}} of indexes
     */
    List<Set<Integer>> findDocumentClustersBySimilarity(List<String> documents);

    /**
     * Calculates the similarity between two
     *
     * @param doc1 The first document as {@link String}
     * @param doc2 The second document as {@link String}
     * @return The similarity as {@link Double}
     */
    Double calculateSimilarityBetweenTwoStrings(String doc1, String doc2);
}
