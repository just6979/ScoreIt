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
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import net.justinwhite.score_model.phase_10.Phase10GameModel;
import net.justinwhite.score_model.phase_10.Phase10PlayerModel;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GameFragment
        extends Fragment
        implements
        YesNoDialog.DialogListener,
        LineEditDialog.DialogListener,
        AdapterView.OnItemClickListener {

    @SuppressWarnings({"WeakerAccess", "unused"})
    @Bind(R.id.textGameName)
    TextView textGameName;
    @SuppressWarnings({"WeakerAccess", "unused"})
    @Bind(R.id.listPlayers)
    ListView listView;
    private GameSetupListener gameSetupListener;
    private Phase10GameModel game;
    private Phase10PlayerAdapter adapter;
    private int chosenPlayer;

    public static GameFragment newInstance() {
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public GameFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            @SuppressWarnings("UnusedAssignment") Bundle args = getArguments();
        }

        game = new Phase10GameModel();
        adapter = new Phase10PlayerAdapter(this.getActivity().getBaseContext(), R.layout.item_phase10_player, game.getPlayerList());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_game, container, false);
        ButterKnife.bind(this, rootView);

        int numPlayers = ((MainActivity) getActivity()).getNumPlayers();
        game.setNumPlayers(numPlayers);
        textGameName.setText(game.getName());

        listView.setAdapter(adapter);

        // Set OnItemClickListener so we can be notified on item clicks
        listView.setOnItemClickListener(this);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            gameSetupListener = (GameSetupListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement GameSetupListener");
        }
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.buttonEndGame)
    protected void EndGame(View view) {
        FragmentManager fm = getActivity().getFragmentManager();
        YesNoDialog endGameDialog = YesNoDialog.newInstance(
                getString(R.string.End_the_game_question)
        );
        endGameDialog.setTargetFragment(this, 0);
        endGameDialog.show(fm, "end_game_dialog");
    }

    @Override
    public void onSubmit() {
        Fragment newFragment = CreateGameFragment.newInstance();
        gameSetupListener.setCurrentFragmentID(MainActivity.FRAG_ID_CREATE_GAME);
        getFragmentManager().beginTransaction()
                .replace(R.id.container_main, newFragment)
                .commit()
        ;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        chosenPlayer = position;
        FragmentManager fragmentManager = getActivity().getFragmentManager();
        LineEditDialog changeNameDialog = LineEditDialog.newInstance(
                game.getPlayer(position).getName()
        );
        changeNameDialog.setTargetFragment(this, 0);
        changeNameDialog.show(fragmentManager, "change_name_dialog");

    }

    @Override
    public void onSubmit(String newName) {
        Phase10PlayerModel player = game.getPlayer(chosenPlayer);
        player.setName(newName);
        game.buildName();
        textGameName.setText(game.getName());
    }

    @SuppressWarnings("unused")
    public interface GameSetupListener {
        int getNumPlayers();

        void setNumPlayers(int _numPlayers);

        int getCurrentFragmentID();

        void setCurrentFragmentID(int newFragmentID);
    }

    @SuppressWarnings({"unused", "EmptyMethod"})
    public interface OnPlayerItemInteractionListener {
        void onPlayerItemInteraction(int player);
    }
}
