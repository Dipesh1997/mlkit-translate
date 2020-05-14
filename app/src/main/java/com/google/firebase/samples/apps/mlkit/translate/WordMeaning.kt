package com.google.firebase.samples.apps.mlkit.translate

class WordMeaning(
    var word: String,
    var speak: String,
    var hindi: String,
    var wordmeaning: String
) {
    var isExpanded = false

    override fun toString(): String {
        return "WordMeaning{" +
                "word='" + word + '\'' +
                ", speak='" + speak + '\'' +
                ", hindi='" + hindi + '\'' +
                ", wordmeaning='" + wordmeaning + '\'' +
                ", expanded=" + isExpanded +
                '}'
    }

}