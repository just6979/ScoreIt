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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.justinwhite.score_model.phase_10.Phase10PlayerModel;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

class Phase10PlayerAdapter extends ArrayAdapter<Phase10PlayerModel> {

    private final List<Phase10PlayerModel> players;
    private final int layoutID;
    private final LayoutInflater layoutInflater;

    public Phase10PlayerAdapter(Context _context, @SuppressWarnings("SameParameterValue") int _layoutID, List<Phase10PlayerModel> _players) {
        super(_context, _layoutID, _players);
        layoutID = _layoutID;
        players = _players;
        layoutInflater = LayoutInflater.from(_context);
    }

    @Override
    public int getCount() {
        return players.size();
    }

    @Override
    public Phase10PlayerModel getItem(int position) {
        return players.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;

        if (view == null) {
            view = layoutInflater.inflate(layoutID, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Phase10PlayerModel p = players.get(position);

        holder.textPlayerName.setText(p.getName());
//        textPlayerScore.setText(p.getScore());
//        textPlayerPhase.setText(p.getPhase());

        return view;
    }

    @SuppressWarnings("unused")
    static class ViewHolder {
        @Bind(R.id.textPlayerName)
        TextView textPlayerName;
        @Bind(R.id.textPlayerScore)
        TextView textPlayerScore;
        @Bind(R.id.textPlayerPhase)
        TextView textPlayerPhase;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
