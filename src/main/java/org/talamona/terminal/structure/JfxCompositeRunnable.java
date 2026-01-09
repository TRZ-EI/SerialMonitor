package org.talamona.terminal.structure;

import java.util.ArrayList;
import java.util.Collection;

public class JfxCompositeRunnable implements Runnable {
    private Collection<RunnableFragment> fragments;

    public JfxCompositeRunnable(){
        this.fragments = new ArrayList<RunnableFragment>();
    }

    @Override
    public void run() {
        if (this.fragments != null) {
            for (RunnableFragment rf : this.fragments) {
                if (rf != null) {
                    rf.executeFragment();
                }
            }
        }
    }
    public void add(RunnableFragment fragment){
        if (this.fragments != null) {
            this.fragments.add(fragment);
        }
    }
}
