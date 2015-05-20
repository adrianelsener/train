package ch.adrianelsener.train.pi.dto;

public class Command {
    private String data;

    public Result execute() {
        System.out.printf("%s\n", data);
        return new Result(Result.Status.OK);
    }

    public void setData(final String data) {
        this.data = data;
    }
}
