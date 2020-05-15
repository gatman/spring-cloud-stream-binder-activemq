package properties;

public class ActiveConsumerProperties {
    private String destination;
    private Boolean transaction=false;
    private String type;


    public String getDestination() {
        return destination;
    }
    public void setDestination(String destination) {
        this.destination = destination;
    }
    public Boolean isTransaction() {
        return transaction;
    }
    public void setTransaction(Boolean transaction) {
        this.transaction = transaction;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
}
