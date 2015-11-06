package pl.sobsoft.spring.boot.compression;

public class SomeDTO {
    private String something;

    public SomeDTO() {
    }

    public SomeDTO(String something) {
        this.something = something;
    }

    public String getSomething() {
        return something;
    }

    public void setSomething(String something) {
        this.something = something;
    }
}
