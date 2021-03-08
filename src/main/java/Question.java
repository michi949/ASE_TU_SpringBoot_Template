public class Question {
    int id;
    String type;
    int originId;
    int destinationId;

    public Question(int id, String type, int originId, int destinationId) {
        this.id = id;
        this.type = type;
        this.originId = originId;
        this.destinationId = destinationId;
    }
}
