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

package net.justinwhite.score_it.phase_10;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import net.justinwhite.score_it.R;
import net.justinwhite.score_model.phase_10.Phase10Player;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Phase10PlayerViewHolder
        extends RecyclerView.ViewHolder
        implements View.OnClickListener, View.OnLongClickListener
{
    private final LayoutInflater inflater;
    private final Phase10PlayerAdapter adapter;

    @SuppressWarnings("unused")
    @Bind(R.id.textPlayerName) TextView textPlayerName;
    @SuppressWarnings("unused")
    @Bind(R.id.textPlayerScore) TextView textPlayerScore;
    @SuppressWarnings("unused")
    @Bind(R.id.textPlayerPhase) TextView textPlayerPhase;

    private Phase10Player player;

    public Phase10PlayerViewHolder(
            View _itemView,
            LayoutInflater _inflater,
            Phase10PlayerAdapter _adapter) {
        super(_itemView);
        inflater = _inflater;
        adapter = _adapter;
        ButterKnife.bind(this, _itemView);
        _itemView.setOnClickListener(this);
        _itemView.setOnLongClickListener(this);
    }

    public void setPlayer(Phase10Player _player) {
        player = _player;
    }

    @Override public void onClick(View v) {
        @SuppressLint("InflateParams")
        final View dialogView = inflater.inflate(R.layout.dialog_phase10_score_update, null);
        final EditText editNewScore = dialogView.findViewById(R.id.editNewScore);
        final CheckBox checkNextPhase = dialogView.findViewById(R.id.checkNextPhase);
        // set data for the dialog and result actions
        final String playerName = player.getName();
        // build and show the dialog
        Dialog dialog = new AlertDialog.Builder(v.getContext())
                .setTitle(v.getResources().getString(R.string.Update_Score_colon) + playerName)
                .setView(dialogView)
                .setPositiveButton(R.string.Change, (dialog12, i) -> {
                    player.addScore(
                            Integer.valueOf(editNewScore.getText().toString())
                    );
                    if (checkNextPhase.isChecked()) { player.completeCurrentPhase(); }
                    adapter.notifyDataSetChanged();
                }
                )
                .setNegativeButton(R.string.Cancel, (dialog1, i) -> dialog1.cancel()
                )
                .create();
        // show the keyboard right away
        dialog.getWindow()
              .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();

    }

    @Override public boolean onLongClick(View v) {
        @SuppressLint("InflateParams")
        final View dialogView = inflater.inflate(R.layout.dialog_phase10_player_name_change, null);
        final EditText editPlayerName = dialogView.findViewById(R.id.editPlayerName);
        // set data for the dialog and result actions
        editPlayerName.setText(player.getName());
        // build and show the dialog
        Dialog dialog = new AlertDialog.Builder(v.getContext())
                .setTitle(R.string.Change_Player_Name)
                .setView(dialogView)
                .setPositiveButton(R.string.Change, (dialog12, i) -> {
                    player.setName(editPlayerName.getText().toString());
                    adapter.notifyDataSetChanged();
                }
                )
                .setNegativeButton(R.string.Cancel, (dialog1, i) -> dialog1.cancel()
                )
                .create();
        // show the keyboard right away
        dialog.getWindow()
              .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();
        return false;
    }
}
