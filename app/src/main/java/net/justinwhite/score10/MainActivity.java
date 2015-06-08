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

package net.justinwhite.score10;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MainActivity extends Activity implements SeekBar.OnSeekBarChangeListener {
    public static final String EXTRA_NUM_PLAYERS;
    private static final int INITIAL_NUM_PLAYERS;

    static {
        EXTRA_NUM_PLAYERS = "net.justinwhite.score10.NUM_PLAYERS_MESSAGE";
        INITIAL_NUM_PLAYERS = 4;
    }

    private int numPlayers;

    @InjectView(R.id.seekNumPlayers)
    protected SeekBar seekNumPlayers;
    @InjectView(R.id.textNumPlayers)
    protected TextView textNumPlayers;

    public MainActivity() {
        numPlayers = INITIAL_NUM_PLAYERS;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        seekNumPlayers.setOnSeekBarChangeListener(this);
        seekNumPlayers.setProgress(numPlayers - 2);
        textNumPlayers.setText(Integer.toString(numPlayers));
        textNumPlayers.clearFocus();

    }

    private void updateNumPlayers(int _numPlayers) {
        numPlayers = _numPlayers;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
        if (fromTouch) {
            updateNumPlayers(progress + 2);
            textNumPlayers.setText(Integer.toString(numPlayers));
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
        Intent intent = new Intent(this, NewGameActivity.class);
        intent.putExtra(EXTRA_NUM_PLAYERS, numPlayers);
        startActivity(intent);
    }

}
