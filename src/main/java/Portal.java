public class Portal {
    int id;
    int destinationId;
    int costs;

    public Portal(int id, int destinationId, int costs) {
        this.id = id;
        this.destinationId = destinationId;
        this.costs = costs;
    }

    @Override
    public String toString() {
        return "Portal{" +
                "id=" + id +
                ", destinationId=" + destinationId +
                ", costs=" + costs +
                '}';
    }
}
