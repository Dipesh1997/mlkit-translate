package com.google.firebase.samples.apps.mlkit.translate;

import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

import java.util.List;
import java.util.Locale;

public class MeaningAdapter extends RecyclerView.Adapter<MeaningAdapter.WordMeaningVH> {

    private static final String TAG = "MeaningAdapter";
    List<WordMeaning> wordMeaningList;


    public MeaningAdapter(List<WordMeaning> wordMeaningList) {
        this.wordMeaningList = wordMeaningList;
    }

    @NonNull
    @Override
    public WordMeaningVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meaning_row, parent, false);
        return new WordMeaningVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordMeaningVH holder, int position) {

        WordMeaning wordMeaning = wordMeaningList.get(position);
        holder.wordTextView.setText(wordMeaning.getWord());
        holder.hindiTextView.setText(wordMeaning.getHindi());
        holder.speekTextView.setText(wordMeaning.getSpeak());
        holder.meaningTextView.setText(wordMeaning.getWordmeaning());

        boolean isExpanded = wordMeaningList.get(position).isExpanded();
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

    }

    @Override
    public int getItemCount() {
        return wordMeaningList.size();
    }

    class WordMeaningVH extends RecyclerView.ViewHolder {
        ConstraintLayout expandableLayout;
        TextView wordTextView, hindiTextView, speekTextView, meaningTextView;
        String url;
        private String sourceText;
        TextToSpeech t1,t2;

        public WordMeaningVH(@NonNull final View itemView) {
            super(itemView);

            wordTextView = itemView.findViewById(R.id.wordTextView);
            hindiTextView = itemView.findViewById(R.id.hindiTextView);
            speekTextView = itemView.findViewById(R.id.speekTextView);
            meaningTextView = itemView.findViewById(R.id.meaningTextView);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);
            t1=new TextToSpeech(itemView.getContext(), status -> {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.US);
                }
            });
            t2=new TextToSpeech(itemView.getContext(), status -> {
                if(status != TextToSpeech.ERROR) {
                    t2.setLanguage(Locale.US);
                }
            });

            wordTextView.setOnClickListener(view -> {
                WordMeaning wordMeaning = wordMeaningList.get(getAdapterPosition());
                wordMeaning.setExpanded(!wordMeaning.isExpanded());
                notifyItemChanged(getAdapterPosition());

            });

            hindiTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    translate();
                }
                private void translate() {
                    sourceText = meaningTextView.getText().toString();
                    FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                            //from language
                            .setSourceLanguage(FirebaseTranslateLanguage.EN)
                            // to language
                            .setTargetLanguage(FirebaseTranslateLanguage.HI)
                            .build();

                    final FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                    FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                    translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            translator.translate(sourceText).addOnSuccessListener(new OnSuccessListener<String>() {
                                @Override
                                public void onSuccess(String s) {
                                    meaningTextView.setText(s);
                                }
                            });
                        }
                    });
                }


            });

            speekTextView.setOnClickListener(v -> {
                String toSpeak = wordTextView.getText().toString();
                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                String toSpeakMeaning = meaningTextView.getText().toString();
                t2.speak(toSpeakMeaning, TextToSpeech.QUEUE_FLUSH, null);
            });

            meaningTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toast();
                }

                private void toast() {
                    String word = wordTextView.getText().toString();
                    String noSpaceStr = word.replaceAll("\\s", "");
                    Toast.makeText(itemView.getContext(),noSpaceStr,Toast.LENGTH_SHORT).show();
                    DictionaryRequest dictionaryRequest = new DictionaryRequest(itemView.getContext(), meaningTextView);
                    url = dictionaryEntries();
                    dictionaryRequest.execute(url);
                    translate();
                }

                private String dictionaryEntries() {
                    final String language = "en-gb";
                    final String word = wordTextView.getText().toString();
                    final String noSpaceStr = word.replaceAll("\\s", "");
                    final String fields = "definitions";
                    final String strictMatch = "false";
                    final String word_id = noSpaceStr.toLowerCase();
                    return "https://od-api.oxforddictionaries.com:443/api/v2/entries/" + language + "/" + word_id + "?" + "fields=" + fields + "&strictMatch=" + strictMatch;
                }
                private void translate() {
                    sourceText = wordTextView.getText().toString();
                    FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                            //from language
                            .setSourceLanguage(FirebaseTranslateLanguage.EN)
                            // to language
                            .setTargetLanguage(FirebaseTranslateLanguage.HI)
                            .build();

                    final FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                    FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                    translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            translator.translate(sourceText).addOnSuccessListener(new OnSuccessListener<String>() {
                                @Override
                                public void onSuccess(String s) {
                                    hindiTextView.setText(s);
                                }
                            });
                        }
                    });
                }


            });

        }

    }
}