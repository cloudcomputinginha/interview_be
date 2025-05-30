package cloudcomputinginha.demo.domain.enums;

public enum InterviewStatus {
    NO_SHOW, SCHEDULED, IN_PROGRESS, DONE;

    public static boolean isValid(String status) {
        try {
            valueOf(status.toUpperCase());
            return true;
        } catch (IllegalArgumentException | NullPointerException e) {
            return false;
        }
    }
}