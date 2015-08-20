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

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import net.justinwhite.score_model.phase_10.Phase10Game;
import net.justinwhite.score_model.phase_10.Phase10Player;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GameActivity
        extends AppCompatActivity
        implements
        YesNoDialog.DialogListener,
        LineEditDialog.DialogListener,
        AdapterView.OnItemClickListener {

    @SuppressWarnings({"WeakerAccess", "unused"})
    @Bind(R.id.textGameName) TextView textGameName;
    @SuppressWarnings({"WeakerAccess", "unused"})
    @Bind(R.id.listPlayers) RecyclerView recyclerView;
    private Phase10Game game;
    private int chosenPlayer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.fade_in_1000, R.anim.fade_out_1000);

        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        int numPlayers = intent.getIntExtra(
                CreateGameActivity.EXTRA_NUM_PLAYERS,
                CreateGameActivity.DEFAULT_NUM_PLAYERS
        );

        game = new Phase10Game();
        game.setNumPlayers(numPlayers);
        textGameName.setText(game.getName());

        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Phase10PlayerAdapter adapter = new Phase10PlayerAdapter(
                this.getBaseContext(),
                R.layout.item_phase10_player,
                game.getPlayerList()
        );
        recyclerView.setAdapter(adapter);

        // Set OnItemClickListener so we can be notified on item clicks
//        recyclerView.setOnClickListener(this);
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
        YesNoDialog endGameDialog = YesNoDialog.newInstance(
                getString(R.string.End_the_game_question)
        );
        endGameDialog.show(getFragmentManager(), "end_game_dialog");
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.buttonEndGame)
    protected void EndGame(View view) {
        showExitDialog();
    }

    @Override
    public void onYesNoSubmit() {
        finish();
        // Check if we're running on Android 5.0 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            overridePendingTransition(R.anim.fade_in_500, R.anim.fade_out_500);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        chosenPlayer = position;
        LineEditDialog changeNameDialog = LineEditDialog.newInstance(
                game.getPlayer(position).getName()
        );
        changeNameDialog.show(getFragmentManager(), "change_name_dialog");
    }

    @Override
    public void onLineEditSubmit(String newName) {
        Phase10Player player = game.getPlayer(chosenPlayer);
        player.setName(newName);
        game.buildName();
        textGameName.setText(game.getName());
    }

}
