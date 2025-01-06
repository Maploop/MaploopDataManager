package me.leon.intel;

public class ToastNotif {
    public static boolean toastBeingShown = false;
    public static ToastNotif currentToast;
    public static long toastDuration = 0;

    public static void showToast(String title, String message, int duration) {
        toastBeingShown = true;
        currentToast = new ToastNotif(title, message);
        toastDuration = duration;
    }

    private final String title;
    private final String message;

    public ToastNotif(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }
}
