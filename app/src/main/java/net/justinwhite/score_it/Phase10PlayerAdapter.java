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

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import net.justinwhite.score_model.phase_10.Phase10Game;
import net.justinwhite.score_model.phase_10.Phase10Player;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

class Phase10PlayerAdapter
        extends RecyclerView.Adapter<Phase10PlayerAdapter.ViewHolder>
{

    private final List<Phase10Player> players;
    private final LayoutInflater inflater;
    private final Phase10Game game;

    public Phase10PlayerAdapter(Phase10Game _game, LayoutInflater _inflater,
                                List<Phase10Player> _players) {
        super();
        game = _game;
        players = _players;
        inflater = _inflater;
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        ViewHolder viewHolder;

        view = inflater.inflate(R.layout.item_phase10_player, parent, false);
        viewHolder = new ViewHolder(view, this);
        view.setTag(viewHolder);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Phase10Player p = players.get(position);
        holder.setPlayerIndex(position);
        holder.textPlayerName.setText(p.getName());
        holder.textPlayerScore.setText(String.valueOf(p.getScore()));
        holder.textPlayerPhase.setText(String.valueOf(p.getPhase()));
    }

    @SuppressWarnings("unused")
    public class ViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener
    {
        private final Phase10PlayerAdapter adapter;
        private int playerIndex;
        @Bind(R.id.textPlayerName) TextView textPlayerName;
        @Bind(R.id.textPlayerScore) TextView textPlayerScore;
        @Bind(R.id.textPlayerPhase) TextView textPlayerPhase;

        public ViewHolder(View view, Phase10PlayerAdapter _adapter) {
            super(view);
            ButterKnife.bind(this, view);
            adapter = _adapter;
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        public void setPlayerIndex(int position) {
            playerIndex = position;
        }

        @Override public void onClick(View v) {
            @SuppressLint("InflateParams")
            final View dialogView = inflater.inflate(R.layout.dialog_score_update, null);
            final EditText editNewScore = (EditText) dialogView.findViewById(R.id.editNewScore);
            final CheckBox checkNextPhase = (CheckBox) dialogView.findViewById(R.id.checkNextPhase);
            // set data for the dialog and result actions
            final String playerName = game.getPlayer(playerIndex).getName();
            // build and show the dialog
            Dialog dialog = new AlertDialog.Builder(v.getContext())
                    .setTitle(v.getResources().getString(R.string.Update_Score_colon) + playerName)
                    .setView(dialogView)
                    .setPositiveButton(R.string.Change, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {
                                    Phase10Player player = game.getPlayer(playerIndex);
                                    player.addScore(
                                            Integer.valueOf(editNewScore.getText().toString())
                                    );
                                    if (checkNextPhase.isChecked()) { player.completePhase(); }
                                    adapter.notifyDataSetChanged();
                                }
                            }
                    )
                    .setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {
                                    dialog.cancel();
                                }
                            }
                    )
                    .create();
            // show the keyboard right away
            dialog.getWindow()
                  .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            dialog.show();

        }

        @Override public boolean onLongClick(View v) {
            @SuppressLint("InflateParams")
            final View dialogView = inflater.inflate(R.layout.dialog_player_name_change, null);
            final EditText editPlayerName = (EditText) dialogView.findViewById(R.id.editPlayerName);
            // set data for the dialog and result actions
            editPlayerName.setText(game.getPlayer(playerIndex).getName());
            // build and show the dialog
            Dialog dialog = new AlertDialog.Builder(v.getContext())
                    .setTitle(R.string.Change_Player_Name)
                    .setView(dialogView)
                    .setPositiveButton(R.string.Change, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {
                                    game.renamePlayer(playerIndex,
                                            editPlayerName.getText().toString()
                                    );
                                    adapter.notifyDataSetChanged();
                                }
                            }
                    )
                    .setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {
                                    dialog.cancel();
                                }
                            }
                    )
                    .create();
            // show the keyboard right away
            dialog.getWindow()
                  .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            dialog.show();
            return false;
        }
    }

}
