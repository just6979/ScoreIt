/*
 * Copyright (c) 2015 Justin White <jw@justinwhite.net>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */

package net.justinwhite.score_it;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewGameFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {
    private static final int SEEKBAR_OFFSET;
    private static int MAX_NUM_PLAYERS;

    static {
        SEEKBAR_OFFSET = 2;
    }

    @Bind(R.id.seekNumPlayers)
    SeekBar seekNumPlayers;
    @Bind(R.id.textNumPlayers)
    TextView labelNumPlayers;
    private GameSetup gameSetup;
    private int numPlayers = 4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_game, container, false);
        ButterKnife.bind(this, rootView);

        MAX_NUM_PLAYERS = this.getResources().getInteger(R.integer.max_num_players);

        seekNumPlayers.setProgress(numPlayers - SEEKBAR_OFFSET);
        seekNumPlayers.setMax(MAX_NUM_PLAYERS - SEEKBAR_OFFSET);
        seekNumPlayers.setOnSeekBarChangeListener(this);

        labelNumPlayers.setText(Integer.toString(numPlayers));
        labelNumPlayers.clearFocus();

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            gameSetup = (GameSetup) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
        if (fromTouch) {
            numPlayers = progress + SEEKBAR_OFFSET;
            labelNumPlayers.setText(Integer.toString(numPlayers));
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @OnClick(R.id.buttonStartGame)
    protected void StartNewGame(View view) {
        gameSetup.setNumPlayers(seekNumPlayers.getProgress() + SEEKBAR_OFFSET);
        Fragment newFragment = new GameFragment();
        gameSetup.setCurrentFragmentID(MainActivity.FRAG_ID_GAME);
        getFragmentManager().beginTransaction()
                .replace(R.id.container, newFragment)
                .commit()
        ;
    }

}
