package ch.adrianelsener.train.pi.dto;

public class Command {
    private final Mode mode;
    private String data;

    private Command(Builder builder) {
        mode = builder.mode;
    }

    public Result execute() {
        System.out.printf("%s\n", data);
        return new Result(Result.Status.OK);
    }

    public void setData(final String data) {
        this.data = data;
    }

    public static class Builder {
        private Mode mode;

        public Builder setMode(Mode mode) {
            this.mode = mode;
            return this;
        }
    }
}
