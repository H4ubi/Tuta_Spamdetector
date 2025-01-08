package de.jhohlbein.tuta.detector.impl;

import de.jhohlbein.tuta.detector.SpamDetector;
import de.jhohlbein.tuta.detector.data.Email;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.text.similarity.CosineSimilarity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.join;
import static java.util.Arrays.stream;
import static java.util.function.Function.identity;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.toMap;

/**
 * The default implementation of {@link SpamDetector}, which uses the cosine similarity calculation.
 */
public class DefaultSpamDetector implements SpamDetector {

    private static final Logger LOG = LogManager.getLogger(DefaultSpamDetector.class);

    final private Configuration config;

    {
        try {
            config = new Configurations().properties(new File("config.properties"));
        } catch (final ConfigurationException e) {
            LOG.error("Could not load configuration file", e);
            throw new RuntimeException("Could not find configuration file config.properties", e);
        }
    }

    @Override
    public List<Email> calculateSimilarityScoringAndCategorizeSpamForEmails(final List<Email> emails) {

        // Compare all the emails with all the others.
        for (final Email email : emails) {
            emails.stream() //
                    .filter(toCompare -> emails.indexOf(toCompare) > emails.indexOf(email)) //
                    .forEach(toCompare -> {
                        LOG.debug("Comparing emails {} and {}", email.getId(), toCompare.getId());
                        // Calculate the similarity
                        final Double similarity = calculateSimilarityBetweenTwoStrings(email.getBody(),
                                toCompare.getBody());
                        // Check if it exceeds the threshold
                        setSpamScoringIfGreaterThanExistingSpamScoring(email, similarity);
                        setSpamScoringIfGreaterThanExistingSpamScoring(toCompare, similarity);
                    });
        }
        emails.sort(Comparator.comparing(Email::getSpamScoring).reversed());
        return emails;
    }

    private void setSpamScoringIfGreaterThanExistingSpamScoring(final Email email, final Double similarity) {
        if (email.getSpamScoring() == null || similarity > email.getSpamScoring()) {
            email.setSpamScoring(similarity);
            final double spamThreshold = config.getDouble("spamdetector.similarity.threshold");
            final boolean spam = similarity > spamThreshold;
            if (spam) {
                LOG.info("Spam detected: Email {} exceeds the threshold of {}", email.getId(), spamThreshold);
            }
            email.setSpam(spam);
        }
    }

    @Override
    public Double calculateSimilarityBetweenTwoStrings(final String firstString, final String secondString) {
        final Map<CharSequence, Integer> firstVector = transformStringToVector(firstString);
        final Map<CharSequence, Integer> secondVector = transformStringToVector(secondString);

        final CosineSimilarity cosineSimilarity = new CosineSimilarity();
        return cosineSimilarity.cosineSimilarity(firstVector, secondVector);

    }

    /**
     * Transforms a String in to a vector, that is represented by a map. The vector has the set of words in the
     * string as keySet and the frequency of each word as corresponding value.
     *
     * @param input The {@link String} to transform
     * @return The {@link Map} which represents the term frequency (TF) vector
     */
    protected Map<CharSequence, Integer> transformStringToVector(final String input) {
        final Pattern regexPattern = compile("(\\w)+");
        final Matcher matcher = regexPattern.matcher(input);
        // Split the string into words by using a regex that matches words
        final List<String> tokens = new ArrayList<>();
        while (matcher.find()) {
            // Add the words to the list of tokens
            tokens.add(matcher.group(0).toLowerCase());
        }
        // Remove stopwords
        removeStopWords(tokens);
        // Transform to vector, by mapping the stream
        return tokens.stream().collect(toMap(identity(), counter -> 1, Integer::sum));
    }

    /**
     * Filters a set of configured stopwords from the term list.
     *
     * @param tokens The {@link List} of tokens to be removed.
     * @return The filtered {@link List} of tokens.
     */
    protected List<String> removeStopWords(final List<String> tokens) {
        // Read the stopwords from the configuration file
        final List<String> stopWords =
                stream(config.getString("spamdetector.transformator.stopwords").split(",")).toList();
        tokens.removeAll(stopWords);
        LOG.debug("Tokens after removing stopwords: [{}]", join(",", tokens));
        return tokens;
    }
}
