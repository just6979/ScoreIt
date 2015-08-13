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
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity
        extends Activity
        implements GameSetupListener {

    static final int FRAG_ID_CREATE_GAME = 1;
    static final int FRAG_ID_GAME = 2;
    private static final int DEFAULT_NUM_PLAYERS = 4;

    private int currentFragmentID;
    private int numPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        numPlayers = sharedPref.getInt(getString(R.string.current_num_players_pref), DEFAULT_NUM_PLAYERS);
        currentFragmentID = sharedPref.getInt(getString(R.string.current_fragment_id_pref), FRAG_ID_CREATE_GAME);

        Fragment nextFragment;
        switch (currentFragmentID) {
            case FRAG_ID_CREATE_GAME:
                nextFragment = new CreateGameFragment();
                break;
            case FRAG_ID_GAME:
                nextFragment = new GameFragment();
                break;
            default:
                // use CreateGame if invalid/unknown ID
                nextFragment = new CreateGameFragment();
                break;
        }
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, nextFragment)
                    .commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.current_num_players_pref), numPlayers);
        editor.putInt(getString(R.string.current_fragment_id_pref), currentFragmentID);
        editor.apply();
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public void setNumPlayers(int _numPlayers) {
        numPlayers = _numPlayers;
    }

    public int getCurrentFragmentID() {
        return currentFragmentID;
    }

    public void setCurrentFragmentID(int newFragmentID) {
        currentFragmentID = newFragmentID;
    }

}
