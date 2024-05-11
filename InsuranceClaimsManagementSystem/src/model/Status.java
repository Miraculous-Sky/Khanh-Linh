package model;

public enum Status {
    NEW("N"),
    PROCESSING("P"),
    DONE("D");

    private final String code;

    Status(String code) {
        this.code = code;
    }

    public static Status parse(String code) {
        if (code != null) {
            for (Status status : Status.values()) {
                if (status.code.equals(code)) {
                    return status;
                }
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }
}
