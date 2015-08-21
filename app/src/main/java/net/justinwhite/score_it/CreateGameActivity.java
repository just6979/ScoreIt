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

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import net.justinwhite.score_model.phase_10.Phase10Game;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateGameActivity
        extends AppCompatActivity
        implements SeekBar.OnSeekBarChangeListener,
        AdapterView.OnItemSelectedListener {

    public static final String EXTRA_NUM_PLAYERS = "EXTRA_NUM_PLAYERS";

    public static final int DEFAULT_NUM_PLAYERS = 4;
    private static final int SEEKBAR_OFFSET = Phase10Game.MIN_PLAYERS;

    @SuppressWarnings({"WeakerAccess", "unused"})
    @Bind(R.id.seekNumPlayers) SeekBar seekNumPlayers;
    @SuppressWarnings({"WeakerAccess", "unused"})
    @Bind(R.id.textNumPlayers) TextView labelNumPlayers;
    @SuppressWarnings({"WeakerAccess", "unused"})
    @Bind(R.id.labelMinPlayers) TextView labelMinPlayers;
    @SuppressWarnings({"WeakerAccess", "unused"})
    @Bind(R.id.labelMaxPlayers) TextView labelMaxPlayers;
    @SuppressWarnings({"WeakerAccess", "unused"})
    @Bind(R.id.gridPhases) GridLayout gridPhases;
    @SuppressWarnings({"WeakerAccess", "unused"})
    @Bind(R.id.spinnerPhases) Spinner spinnerPhases;

    private int numPlayers;
    private int maxNumPlayers;
    private int minNumPLayers;
    private int[] checkIDs;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_game);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        numPlayers = sharedPref.getInt(getString(R.string.pref_current_num_players), DEFAULT_NUM_PLAYERS);

        checkIDs = new int[Phase10Game.MAX_PHASE];
        for (int i = 0; i < Phase10Game.MAX_PHASE; i++) {
            CheckBox check = new CheckBox(this);
            int id = View.generateViewId();
            checkIDs[i] = id;
            check.setId(id);
            check.setText(String.valueOf(i + 1));
            check.setChecked(true);
            check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    spinnerPhases.setSelection(5);
                }
            });
            gridPhases.addView(check);
        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.phase_selections,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPhases.setAdapter(adapter);
        spinnerPhases.setOnItemSelectedListener(this);

        // get max & min players from the GameModel
        maxNumPlayers = Phase10Game.MAX_PLAYERS;
        labelMaxPlayers.setText(Integer.toString(maxNumPlayers));
        minNumPLayers = Phase10Game.MIN_PLAYERS;
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
    protected void StartNewGame() {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(EXTRA_NUM_PLAYERS, numPlayers);
        startActivity(intent);
        // Check if we're running on Android 5.0 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            overridePendingTransition(R.anim.fade_in_1000, R.anim.fade_out_1000);
        }
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.labelMinPlayers)
    protected void setMinPlayerCount() {
        seekNumPlayers.setProgress(minNumPLayers - SEEKBAR_OFFSET);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.labelMaxPlayers)
    protected void setMaxPlayerCount() {
        seekNumPlayers.setProgress(maxNumPlayers - SEEKBAR_OFFSET);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.labelHowManyPlayers)
    protected void setDefaultPlayerCount() {
        seekNumPlayers.setProgress(DEFAULT_NUM_PLAYERS - SEEKBAR_OFFSET);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        CheckBox check;

        String selected = String.valueOf(parent.getItemAtPosition(position));

        for (int i = 0; i < Phase10Game.MAX_PHASE; i++) {
            check = (CheckBox) findViewById(checkIDs[i]);
            switch (selected) {
                case "All":
                    check.setChecked(true);
                    break;
                case "Even":
                    if ((i % 2) != 0) {
                        check.setChecked(true);
                    } else {
                        check.setChecked(false);
                    }
                    break;
                case "Odd":
                    if ((i % 2) == 0) {
                        check.setChecked(true);
                    } else {
                        check.setChecked(false);
                    }
                    break;
                case "First 5":
                    if (i < Phase10Game.MAX_PHASE / 2) {
                        check.setChecked(true);
                    } else {
                        check.setChecked(false);
                    }
                    break;
                case "Last 5":
                    if (i >= Phase10Game.MAX_PHASE / 2) {
                        check.setChecked(true);
                    } else {
                        check.setChecked(false);
                    }
                    break;
                case "Custom":
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

}