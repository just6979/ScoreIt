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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.justinwhite.score_model.phase_10.Phase10Player;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

class Phase10PlayerAdapter extends RecyclerView.Adapter<net.justinwhite.score_it.Phase10PlayerAdapter.ViewHolder> {

    private final List<Phase10Player> players;
    private final int layoutID;
    private final LayoutInflater layoutInflater;

    public Phase10PlayerAdapter(Context _context, @SuppressWarnings("SameParameterValue") int _layoutID, List<Phase10Player> _players) {
        super();
        layoutID = _layoutID;
        players = _players;
        layoutInflater = LayoutInflater.from(_context);
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

        view = layoutInflater.inflate(layoutID, parent, false);
        viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return viewHolder;
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        Phase10Player p = players.get(position);

        holder.textPlayerName.setText(p.getName());
        holder.textPlayerScore.setText(String.valueOf(p.getScore()));
        holder.textPlayerPhase.setText(String.valueOf(p.getPhase()));
    }

    @SuppressWarnings("unused")
    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.textPlayerName) TextView textPlayerName;
        @Bind(R.id.textPlayerScore) TextView textPlayerScore;
        @Bind(R.id.textPlayerPhase) TextView textPlayerPhase;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

}
