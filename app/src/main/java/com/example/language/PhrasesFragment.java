package com.example.language;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhrasesFragment extends Fragment {

    private MediaPlayer mediaPlayer;

    private AudioManager mAudioManager;

    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer();
        }
    };

    AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListner = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {

            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                mediaPlayer.pause();
                mediaPlayer.seekTo(0);

            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                mediaPlayer.start();

            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                releaseMediaPlayer();
            }


        }
    };


    public PhrasesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_list, container, false);


        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Word> phrases = new ArrayList<Word>();

        phrases.add(new Word("What is your name?", "Mi peru yenti?", R.raw.phrase_one));
        phrases.add(new Word("How are you?", "Yela unnaru?", R.raw.phrase_two));
        phrases.add(new Word("What are you doing?", "Yem chestunnaru?", R.raw.phrase_three));
        phrases.add(new Word("My name is Bittu", "Na peru Bittu", R.raw.phrase_four));
        phrases.add(new Word("Are you hungry?", "Aakali vestunda?", R.raw.phrase_five));
        phrases.add(new Word("Are you coming", "Miru vostunnara?", R.raw.phrase_six));
        phrases.add(new Word("Are you going outside", "Bayataki veltunnara?", R.raw.phrase_seven));
        phrases.add(new Word("Shall we go for a movie?", "Manam movie ki veldama?", R.raw.phrase_eight));
        phrases.add(new Word("I am not feeling well", "Naku ontlo baledu", R.raw.phrase_nine));
        phrases.add(new Word("What are you eating?", "Yem tintunnaru?", R.raw.phrase_ten));
        phrases.add(new Word("I am eating rice", "Nenu annam tintunnanu", R.raw.phrase_eleven));


        WordAdapter adapter = new WordAdapter(getActivity(), phrases, R.color.category_phrases);
        ListView phraseList = (ListView) rootView.findViewById(R.id.list);
        phraseList.setAdapter(adapter);

        phraseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Word phrases1 = phrases.get(position);

                releaseMediaPlayer();

                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListner, AudioManager.STREAM_MUSIC,
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {


                    mediaPlayer = MediaPlayer.create(getActivity(), phrases1.getVoiceId());
                    mediaPlayer.start();

                    mediaPlayer.setOnCompletionListener(mCompletionListener);

                }

            }
        });


        return rootView;

    }


    @Override
    public void onStop() {
        super.onStop();

        releaseMediaPlayer();
    }

    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();

            mediaPlayer = null;

            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListner);
        }
    }


}
