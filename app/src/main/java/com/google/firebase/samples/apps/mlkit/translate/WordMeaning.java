package com.google.firebase.samples.apps.mlkit.translate;

public class WordMeaning {

    private String word;
    private String speak;
    private String hindi;
    private String wordmeaning;
    private boolean expanded;

    public WordMeaning(String word, String speak, String hindi, String wordmeaning) {
        this.word = word;
        this.speak = speak;
        this.hindi = hindi;
        this.wordmeaning = wordmeaning;
        this.expanded = false;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getSpeak() {
        return speak;
    }

    public void setSpeak(String speak) {
        this.speak = speak;
    }

    public String getHindi() {
        return hindi;
    }

    public void setHindi(String hindi) {
        this.hindi = hindi;
    }

    public String getWordmeaning() {
        return wordmeaning;
    }

    public void setWordmeaning(String wordmeaning) {
        this.wordmeaning = wordmeaning;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    @Override
    public String toString() {
        return "WordMeaning{" +
                "word='" + word + '\'' +
                ", speak='" + speak + '\'' +
                ", hindi='" + hindi + '\'' +
                ", wordmeaning='" + wordmeaning + '\'' +
                ", expanded=" + expanded +
                '}';
    }
}
