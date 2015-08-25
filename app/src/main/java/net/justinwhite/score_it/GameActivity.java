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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import net.justinwhite.score_model.phase_10.Phase10Game;
import net.justinwhite.score_model.phase_10.Phase10Player;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GameActivity
        extends AppCompatActivity
        implements
        LineEditDialog.DialogListener,
        ScoreUpdateDialog.DialogListener,
        RecyclerItemClickListener.OnItemClickListener
{

    @SuppressWarnings({"WeakerAccess", "unused"})
    @Bind(R.id.toolbar) Toolbar toolbar;
    @SuppressWarnings({"WeakerAccess", "unused"})
    @Bind(R.id.textGameName) TextView textGameName;
    @SuppressWarnings({"WeakerAccess", "unused"})
    @Bind(R.id.listPlayers) RecyclerView recyclerView;
    private Phase10Game game;
    private int chosenPlayer;
    private Phase10PlayerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        // always has these extras
        int numPlayers = intent.getIntExtra(
                CreateGameActivity.EXTRA_NUM_PLAYERS,
                CreateGameActivity.DEFAULT_NUM_PLAYERS
        );
        boolean[] phases = intent.getBooleanArrayExtra(CreateGameActivity.EXTRA_PHASES);
        // maybe has these extras
        String gameName = "";
        if (intent.hasExtra(CreateGameActivity.EXTRA_GAME_NAME)) {
            gameName = intent.getStringExtra(CreateGameActivity.EXTRA_GAME_NAME);
        }

        game = new Phase10Game(numPlayers);
        game.setName(gameName);
        game.setActivePhases(phases);
        textGameName.setText(game.getName());

        recyclerView.setHasFixedSize(true);

        int orientation = getResources().getConfiguration().orientation;
        RecyclerView.LayoutManager layoutManager;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager = new GridLayoutManager(this, 2);
        } else {
            layoutManager = new LinearLayoutManager(this);
        }
        recyclerView.setLayoutManager(layoutManager);

        adapter = new Phase10PlayerAdapter(
                getLayoutInflater(),
                game.getPlayerList()
        );
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, this));
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            showExitDialog();
            return (true);
        }
        return (super.onOptionsItemSelected(item));
    }

    private void showExitDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.End_the_game_question))
                .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                finish();
                            }
                        }
                )
                .setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.cancel();
                            }
                        }
                )
                .create()
                .show();
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.buttonEndGame)
    protected void onClickEndGame(View view) {
        showExitDialog();
    }

    @Override
    public void onItemClick(View childView, int position) {
        chosenPlayer = position;
        ScoreUpdateDialog.newInstance(game.getPlayer(position).getName())
                         .show(getFragmentManager(), "update_score_dialog");
    }

    @Override
    public void onItemLongPress(View childView, int position) {
        chosenPlayer = position;
        LineEditDialog.newInstance(game.getPlayer(chosenPlayer).getName())
                      .show(getFragmentManager(), "change_name_dialog");
    }

    @Override
    public void onLineEditSubmit(String newName) {
        game.renamePlayer(chosenPlayer, newName);
        textGameName.setText(game.getName());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onScoreUpdateSubmit(int newScore, boolean completedPhase) {
        Phase10Player player = game.getPlayer(chosenPlayer);
        player.addScore(newScore);
        if (completedPhase) { player.completePhase(); }
        adapter.notifyDataSetChanged();
    }
}
