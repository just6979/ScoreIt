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

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.justinwhite.score_model.phase_10.Phase10Player;

import java.util.List;

class Phase10PlayerAdapter
        extends RecyclerView.Adapter<Phase10PlayerViewHolder>
{

    private final List<Phase10Player> players;
    private final LayoutInflater inflater;

    public Phase10PlayerAdapter(List<Phase10Player> _players, LayoutInflater _inflater) {
        super();
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
    public Phase10PlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        Phase10PlayerViewHolder viewHolder;

        view = inflater.inflate(R.layout.item_phase10_player, parent, false);
        viewHolder = new Phase10PlayerViewHolder(view, inflater, this);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(Phase10PlayerViewHolder holder, int position) {
        Phase10Player player = players.get(position);
        holder.setPlayer(player);
        holder.textPlayerName.setText(player.getName());
        holder.textPlayerScore.setText(String.valueOf(player.getScore()));
        holder.textPlayerPhase.setText(String.valueOf(player.getPhase()));
    }

}
