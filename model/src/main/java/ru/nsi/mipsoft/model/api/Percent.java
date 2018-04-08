package ru.nsi.mipsoft.model.api;

public class Percent {

    public static class PersantageException extends RuntimeException{
        public PersantageException() {
            super();
        }

        public PersantageException(String message, Throwable cause) {
            super(message, cause);
        }

        public PersantageException(String message) {
            super(message);
        }

        protected PersantageException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }

        public PersantageException(Throwable cause) {
            super(cause);
        }
    }

    public Percent(float value) {
        if(value < 0 || value > 1.F){
            String message = "Ошибка при создании объекта типа Percent:" +
                    " параметр переданный в конструктор должен лежать в диапазоне [0.0; 1.0]";
            throw new PersantageException(message);
        }
        this.value = value;
    }

    public <T extends Number> float getPercentOfValue(T value){
        return this.value * value.floatValue();
    }

    public float getValue() {
        return value;
    }

    private final float value;
}
