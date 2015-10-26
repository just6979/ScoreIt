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

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;

import net.justinwhite.score_it.phase_10.CreatePhase10GameFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity
        extends AppCompatActivity
        implements FragmentManager.OnBackStackChangedListener
{
    private static final int FRAG_ID_DEFAULT = 0;
    private static final int FRAG_ID_GAME_SELECT = 1;
    private static final int FRAG_ID_CREATE_PHASE_10_GAME = 2;

    @SuppressWarnings({"WeakerAccess", "unused"})
    @Bind(R.id.toolbar)
    public Toolbar toolbar;
    @SuppressWarnings({"WeakerAccess", "unused"})
    @Bind(R.id.buttonBigButton)
    public Button buttonBigButton;

    private ActionBar actionbar;
    private FragmentManager fragmentManager;

    private int curFrag = FRAG_ID_DEFAULT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        // setup the fancy new material style toolbar
        setSupportActionBar(toolbar);
        actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setLogo(R.mipmap.ic_launcher);
        }
        fragmentManager = getFragmentManager();
        fragmentManager.addOnBackStackChangedListener(this);
        displayFragment();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            if (!backStackEmpty()) {
                fragmentManager.popBackStack();
            }
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    public void onBackPressed() {
        if (backStackEmpty()) {
            this.finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public void onBackStackChanged() {
        boolean hasBackStack = !backStackEmpty();
        actionbar.setDisplayHomeAsUpEnabled(hasBackStack);
        actionbar.setDisplayShowHomeEnabled(!hasBackStack);
        if (!hasBackStack) {
            toolbar.setSubtitle(null);
        }
    }

    private boolean backStackEmpty() {
        return fragmentManager.getBackStackEntryCount() == 0;
    }

    @OnClick(R.id.buttonBigButton)
    public void displayFragment() {
        Fragment frag;

        switch (curFrag) {
        case FRAG_ID_DEFAULT:
        default:
            frag = new GameSelectFragment();
            curFrag = FRAG_ID_GAME_SELECT;
            toolbar.setSubtitle(null);
            break;
        case FRAG_ID_GAME_SELECT:
            frag = new CreatePhase10GameFragment();
            curFrag = FRAG_ID_CREATE_PHASE_10_GAME;
            toolbar.setSubtitle(R.string.Score_Phase_10);
            break;

        }

        fragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer, frag)
                .addToBackStack(null)
                .commit()
        ;
        fragmentManager.executePendingTransactions();
    }


}
