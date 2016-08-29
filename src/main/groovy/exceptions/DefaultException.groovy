package exceptions

class DefaultException extends Exception {

    @Override
    public Throwable fillInStackTrace() {
        // do nothing
        return this
    }
}
