package me.leon.intel;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class SIM_RUNNABLE {
    private Consumer<String> ON_SYSOUT_CALL;
    private Consumer<Integer> ON_SYSPROG_CALL;
    private Consumer<Integer> ON_SYSEND_CALL;
    private int _LAST_PROG = 0;

    public abstract void run(int turn);

    public void SET_onSysOutCall(Consumer<String> r)
    {
        ON_SYSOUT_CALL = r;
    }

    public void SET_onSysProgCall(Consumer<Integer> r)
    {
        ON_SYSPROG_CALL = r;
    }

    public void SET_onSysEndCall(Consumer<Integer> r)
    {
        ON_SYSEND_CALL = r;
    }

    void SYSPROG(int P)
    {
        if (ON_SYSPROG_CALL == null) return;

        ON_SYSPROG_CALL.accept(P); 
        _LAST_PROG = P;
    }

    void SYSEND(int exitCode)
    {
        if (ON_SYSEND_CALL == null) return;

        ON_SYSEND_CALL.accept(exitCode);
    }

    void INCREMENT_SYSPROG(int p)
    {
        _LAST_PROG += p;
        SYSPROG(_LAST_PROG);
    }

    void SYSOUT(String data)
    {
        if (ON_SYSOUT_CALL == null) return;

        ON_SYSOUT_CALL.accept(data);
    }
}
