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

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import net.justinwhite.score_model.phase_10.Phase10GameModel;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateGameActivity
        extends AppCompatActivity
        implements SeekBar.OnSeekBarChangeListener {

    public static final String EXTRA_NUM_PLAYERS = "EXTRA_NUM_PLAYERS";

    public static final int DEFAULT_NUM_PLAYERS = 4;
    private static final int SEEKBAR_OFFSET = Phase10GameModel.MIN_PLAYERS;

    @SuppressWarnings({"WeakerAccess", "unused"})
    @Bind(R.id.seekNumPlayers) SeekBar seekNumPlayers;
    @SuppressWarnings({"WeakerAccess", "unused"})
    @Bind(R.id.textNumPlayers) TextView labelNumPlayers;
    @SuppressWarnings({"WeakerAccess", "unused"})
    @Bind(R.id.labelMinPlayers) TextView labelMinPlayers;
    @SuppressWarnings({"WeakerAccess", "unused"})
    @Bind(R.id.labelMaxPlayers) TextView labelMaxPlayers;
    private int numPlayers;
    private Toolbar toolbar;
    private int maxNumPlayers;
    private int minNumPLayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_game);
        ButterKnife.bind(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        numPlayers = sharedPref.getInt(getString(R.string.pref_current_num_players), DEFAULT_NUM_PLAYERS);

        // get max & min players from the GameModel
        maxNumPlayers = Phase10GameModel.MAX_PLAYERS;
        labelMaxPlayers.setText(Integer.toString(maxNumPlayers));
        minNumPLayers = Phase10GameModel.MIN_PLAYERS;
        labelMinPlayers.setText(Integer.toString(minNumPLayers));

        seekNumPlayers.setMax(maxNumPlayers - SEEKBAR_OFFSET);

        seekNumPlayers.setProgress(numPlayers - SEEKBAR_OFFSET);
        seekNumPlayers.setOnSeekBarChangeListener(this);

        labelNumPlayers.setText(Integer.toString(numPlayers));
        labelNumPlayers.clearFocus();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.pref_current_num_players), numPlayers);
        editor.apply();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
        numPlayers = progress + SEEKBAR_OFFSET;
        labelNumPlayers.setText(Integer.toString(numPlayers));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.buttonStartGame)
    protected void StartNewGame(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(EXTRA_NUM_PLAYERS, numPlayers);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in_1000, R.anim.fade_out_1000);
    }

    @OnClick(R.id.labelMinPlayers)
    protected void setMinPlayerCount(View view) {
        seekNumPlayers.setProgress(minNumPLayers - SEEKBAR_OFFSET);
    }

    @OnClick(R.id.labelMaxPlayers)
    protected void setMaxPlayerCount(View view) {
        seekNumPlayers.setProgress(maxNumPlayers - SEEKBAR_OFFSET);
    }

    @OnClick(R.id.labelHowManyPlayers)
    protected void setDefaultPlayerCount(View view) {
        seekNumPlayers.setProgress(DEFAULT_NUM_PLAYERS - SEEKBAR_OFFSET);
    }

}
