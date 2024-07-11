public enum StatusCode {
    OK(200, "OK"),
    NOT_FOUND(404, "Not Found");

    private final int code;
    private final String text;

    StatusCode(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public int getCode() {
        return code;
    }

    public String getText() {
        return text;
    }
}
