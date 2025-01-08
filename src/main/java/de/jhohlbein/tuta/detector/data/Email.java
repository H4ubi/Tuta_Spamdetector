package de.jhohlbein.tuta.detector.data;

public class Email {

    private String id;
    private String body;
    private Double spamScoring;
    private boolean spam;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(final String body) {
        this.body = body;
    }

    public Double getSpamScoring() {
        return spamScoring;
    }

    public void setSpamScoring(final Double spamScoring) {
        this.spamScoring = spamScoring;
    }

    public boolean isSpam() {
        return spam;
    }

    public void setSpam(final boolean spam) {
        this.spam = spam;
    }
}