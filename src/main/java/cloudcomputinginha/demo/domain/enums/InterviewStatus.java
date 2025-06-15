package cloudcomputinginha.demo.domain.enums;

public enum InterviewStatus {
    SCHEDULED, NO_SHOW,
    IN_PROGRESS, DONE;

    public static boolean isValid(String status) {
        try {
            valueOf(status.toUpperCase());
            return true;
        } catch (IllegalArgumentException | NullPointerException e) {
            return false;
        }
    }
}