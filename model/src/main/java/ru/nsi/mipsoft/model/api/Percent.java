package ru.nsi.mipsoft.model.api;

public class Percent {

    public static class PercentageException extends RuntimeException{
        public PercentageException() {
            super();
        }

        public PercentageException(String message, Throwable cause) {
            super(message, cause);
        }

        public PercentageException(String message) {
            super(message);
        }

        protected PercentageException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }

        public PercentageException(Throwable cause) {
            super(cause);
        }
    }

    public Percent(double value) {
        if(value < 0 || value > 1){
            String message = "Ошибка при создании объекта типа Percent:" +
                    " параметр переданный в конструктор должен лежать в диапазоне [0.0; 1.0]";
            throw new PercentageException(message);
        }
        this.value = value;
    }

    public <T extends Number> double getPercentOfValue(T value){
        return this.value * value.doubleValue();
    }

    public double getValue() {
        return value;
    }

    private final double value;
}
