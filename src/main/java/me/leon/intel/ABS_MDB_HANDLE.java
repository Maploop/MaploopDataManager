package me.leon.intel;

public class ABS_MDB_HANDLE {
    private final MDB_HANDLE selfContainedHandle;

    public ABS_MDB_HANDLE() {
        selfContainedHandle = getClass().getAnnotation(MDB_HANDLE.class);
        if (selfContainedHandle == null) {
            System.out.println("Absolution of ABS_MDB_HANDLE.J66 has failed. Please try again");
            System.out.println("[ERROR] SELF CONTAINED HANDLE IS NULLIFIED OR DOESN'T EXIST (?)");
            // throw new RuntimeException("END OF EXECUTION >> 0");
        }
    }

    public void initiateConnection() {
        System.out.println("Starting JOBS...");
        new Thread(() ->
        {
            int prog = 0;
            for (int i = 0; i < 30; i++) {
                prog++;
                float poZ = (float) prog / 30;
                CUtil.buildProgressBar("MongoConnection ", poZ, 30, "Hello");

                try {
                    Thread.sleep(50);
                } catch (Exception e) {

                }
            }
        }).start();
    }


}
