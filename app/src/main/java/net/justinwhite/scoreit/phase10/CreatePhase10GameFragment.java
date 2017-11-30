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

package net.justinwhite.scoreit.phase10;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import net.justinwhite.scoreit.R;
import net.justinwhite.scoremodel.Game;
import net.justinwhite.scoremodel.phase10.Phase10Game;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnItemSelected;

public class CreatePhase10GameFragment
        extends Fragment
        implements SeekBar.OnSeekBarChangeListener
{
    // intent extras for Phase10GameActivity
    public static final String EXTRA_NUM_PLAYERS = "EXTRA_NUM_PLAYERS";
    public static final String EXTRA_GAME_NAME = "EXTRA_GAME_NAME";
    public static final String EXTRA_PHASES = "EXTRA_PHASES";
    // constants for building layout and game setup
    public static final int DEFAULT_NUM_PLAYERS = 4;
    private static final int SEEKBAR_OFFSET = Phase10Game.MIN_PLAYERS;
    // important layout widgets
    @SuppressWarnings({"WeakerAccess", "unused"})
    @BindView(R.id.textNumPlayers) TextView labelNumPlayers;
    @SuppressWarnings({"WeakerAccess", "unused"})
    @BindView(R.id.labelMinPlayers) TextView labelMinPlayers;
    @SuppressWarnings({"WeakerAccess", "unused"})
    @BindView(R.id.labelMaxPlayers) TextView labelMaxPlayers;
    @SuppressWarnings({"WeakerAccess", "unused"})
    @BindView(R.id.seekNumPlayers) SeekBar seekNumPlayers;
    @SuppressWarnings({"WeakerAccess", "unused"})
    @BindView(R.id.checkNameIt) CheckBox checkNameIt;
    @SuppressWarnings({"WeakerAccess", "unused"})
    @BindView(R.id.editGameName) EditText editGameName;
    @SuppressWarnings({"WeakerAccess", "unused"})
    @BindView(R.id.spinnerPhases) Spinner spinnerPhases;
    @SuppressWarnings({"WeakerAccess", "unused"})
    @BindView(R.id.gridPhases) GridLayout gridPhases;
    // game setup stuff
    private int numPlayers;
    private int maxNumPlayers;
    private int minNumPLayers;
    private int[] checkBoxIDs;
    private boolean selectionFromCheckboxes;
    // others
    private InputMethodManager imm;
    private boolean customName;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_create_phase10_game, container, false);
        ButterKnife.bind(this, view);
        // setup the fancy new material style toolbar
        // read shared prefs
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        numPlayers = sharedPref.getInt(getString(R.string.pref_current_num_players),
                DEFAULT_NUM_PLAYERS
        );
        // grab inputmanager for soft keyboard handling
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        // get max & min players from the GameModel
        maxNumPlayers = Phase10Game.MAX_PLAYERS;
        minNumPLayers = Phase10Game.MIN_PLAYERS;
        // set up the seekbar and labels
        labelMinPlayers.setText(String.format(Locale.getDefault(),"%d", minNumPLayers));
        labelMaxPlayers.setText(String.format(Locale.getDefault(),"%d", maxNumPlayers));
        seekNumPlayers.setMax(maxNumPlayers - SEEKBAR_OFFSET);
        seekNumPlayers.setProgress(numPlayers - SEEKBAR_OFFSET);
        seekNumPlayers.setOnSeekBarChangeListener(this);
        labelNumPlayers.setText(String.format(Locale.getDefault(),"%d", numPlayers));
        // set up the game name checkbox and edittext
        checkNameIt.setChecked(false);
        customName = false;
        buildName();
        editGameName.clearFocus();
        editGameName.setVisibility(View.GONE);
        // set up the phase selection dropdown
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getActivity(),
                R.array.phase_selections,
                android.R.layout.simple_spinner_dropdown_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPhases.setAdapter(adapter);
        // set up the phase selection checkboxes
        checkBoxIDs = new int[Phase10Game.MAX_PHASE + 1];
        for (int i = 1; i <= Phase10Game.MAX_PHASE; i++) {
            CheckBox check = new CheckBox(getActivity());
            int id = View.generateViewId();
            checkBoxIDs[i] = id;
            check.setId(id);
            check.setText(String.valueOf(i));
            check.setChecked(true);
            check.setOnClickListener(v -> {
                spinnerPhases.setSelection(5);
                selectionFromCheckboxes = true;
            }
            );
            gridPhases.addView(check);
        }
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.pref_current_num_players), numPlayers);
        editor.apply();
    }

    @SuppressWarnings("unused")
    @OnClick({R.id.labelHowManyPlayers, R.id.textNumPlayers})
    protected void setDefaultPlayerCount() {
        seekNumPlayers.setProgress(DEFAULT_NUM_PLAYERS - SEEKBAR_OFFSET);
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

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
        numPlayers = progress + SEEKBAR_OFFSET;
        labelNumPlayers.setText(String.format(Locale.getDefault(),"%d", numPlayers));
        buildName();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @SuppressWarnings("unused")
    @OnCheckedChanged(R.id.checkNameIt)
    protected void onCheckNameItChanged(boolean isChecked) {
        if (isChecked) {
            customName = true;
            // show the edittext & set focus, which will show the keyboard
            editGameName.setVisibility(View.VISIBLE);
            editGameName.requestFocus();
        } else {
            customName = false;
            buildName();
            // remove the edittext from layout and clear focus, which will hide the keyboard
            editGameName.clearFocus();
            editGameName.setVisibility(View.GONE);
        }
    }

    private void buildName() {
        if (!customName) {
            String name = Game.Companion.buildName();
            editGameName.setText(name);
        }
    }

    @SuppressWarnings("unused")
    @OnFocusChange(R.id.editGameName)
    protected void onEditGameNameFocusChange(boolean hasFocus) {
        if (hasFocus) {
            imm.showSoftInput(editGameName, InputMethodManager.SHOW_IMPLICIT);
        } else {
            imm.hideSoftInputFromWindow(editGameName.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS
            );
        }
    }

    @SuppressWarnings("unused")
    @OnItemSelected(R.id.spinnerPhases)
    protected void onSpinnerPhasesSelected(AdapterView<?> parent, int position, long id) {
        // don't do anything if the spinner was changed programmatically
        if (selectionFromCheckboxes) {
            selectionFromCheckboxes = false;
            return;
        }
        selectionFromCheckboxes = false;

        CheckBox checkBox;
        String selected = String.valueOf(parent.getItemAtPosition(position));

        for (int i = 1; i <= Phase10Game.MAX_PHASE; i++) {
            checkBox = getActivity().findViewById(checkBoxIDs[i]);
            switch (selected) {
            case "All":
                checkBox.setChecked(true);
                break;
            case "Odd":
                if ((i % 2) != 0) {
                    checkBox.setChecked(true);
                } else {
                    checkBox.setChecked(false);
                }
                break;
            case "Even":
                if ((i % 2) == 0) {
                    checkBox.setChecked(true);
                } else {
                    checkBox.setChecked(false);
                }
                break;
            case "First 5":
                if (i <= (Phase10Game.MAX_PHASE + 1) / 2) {
                    checkBox.setChecked(true);
                } else {
                    checkBox.setChecked(false);
                }
                break;
            case "Last 5":
                if (i > (Phase10Game.MAX_PHASE + 1) / 2) {
                    checkBox.setChecked(true);
                } else {
                    checkBox.setChecked(false);
                }
                break;
            case "Custom":
                checkBox.setChecked(false);
                break;
            default:
                checkBox.setChecked(true);
                break;
            }
        }
    }

    public void CreateNewGame() {
        boolean[] phases = new boolean[Phase10Game.MAX_PHASE + 1];
        boolean atLeastOnePhase = false;
        CheckBox checkBox;

        for (int i = 1; i <= Phase10Game.MAX_PHASE; i++) {
            checkBox = getActivity().findViewById(checkBoxIDs[i]);
            phases[i] = checkBox.isChecked();
            if (!atLeastOnePhase) { atLeastOnePhase = phases[i]; }
        }
        if (!atLeastOnePhase) {
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setTitle("No Phases");
            alertDialog.setMessage("No phases selected to play!");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Go Back",
                    (dialog, which) -> dialog.dismiss()
            );
            alertDialog.show();
            return;
        }

        Intent intent = new Intent(getActivity(), Phase10GameActivity.class);
        // always has these extras
        intent.putExtra(EXTRA_NUM_PLAYERS, numPlayers);
        intent.putExtra(EXTRA_PHASES, phases);
        // maybe has these extras
        if (checkNameIt.isChecked()) {
            intent.putExtra(EXTRA_GAME_NAME, editGameName.getText().toString());
        }
        getActivity().startActivity(intent);
    }

}