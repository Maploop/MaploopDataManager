package me.leon.intel;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Job {
    private final SIM_RUNNABLE _SIMULATOR;
    private final String _JOB_ID;
    private boolean _SEPRATE_THREAD = false;
    private boolean _DISPLAY_PROGRESS = false;

    private final int _MAX_PRO_SIZE;

    String jT;

    public Job(String jT, SIM_RUNNABLE r, int maxSize) {
        this._JOB_ID = UUID.randomUUID().toString().split("-")[0];
        this._SIMULATOR = r;
        this._MAX_PRO_SIZE = maxSize;
        this.jT = jT;
    }

    public Job TOGGLE_seprateThread() {
        this._SEPRATE_THREAD = !this._SEPRATE_THREAD;
        System.out.println("Job@" + _JOB_ID + "$> Turned " + (_SEPRATE_THREAD ? "ON" : "OFF") + " seprate threads.");
        return this;
    }

    public Job TOGGLE_displayProgress() {
        this._DISPLAY_PROGRESS = !this._DISPLAY_PROGRESS;
        System.out.println("Job@" + _JOB_ID + "$> Turned " + (_SEPRATE_THREAD ? "ON" : "OFF") + " display progress.");
        return this;
    }

    public void start() {
        System.out.println("Job@" + _JOB_ID + "$> Starting job...");
        if (_SEPRATE_THREAD) {
            AtomicBoolean running = new AtomicBoolean(true);
            String[] lastLog = {""};
            AtomicInteger prog = new AtomicInteger(0);
            new Thread(() -> {
                _SIMULATOR.SET_onSysOutCall((data) ->
                {
                    lastLog[0] = data;
                });

                _SIMULATOR.SET_onSysProgCall((i) ->
                {
                    prog.set(i);
                });

                _SIMULATOR.SET_onSysEndCall((e) ->
                {
                    running.set(false);
                });

                int turn = 0;
                while (running.get()) {
                    _SIMULATOR.run(turn);
                    turn++;
                    if (_DISPLAY_PROGRESS) {
                        CUtil.buildProgressBar(jT, (float) (prog.get() / _MAX_PRO_SIZE), _MAX_PRO_SIZE, lastLog[0]);
                    }
                }

            }).start();
        }
    }
}
