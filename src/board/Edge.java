package board;

public class Edge {

    protected final Vertex u;
    protected final Vertex v;
    protected boolean occupied;

    public Edge(Vertex u, Vertex v) {
        this.u = u;
        this.v = v;
        occupied = false;
    }

    public Vertex getU() {
        return u;
    }

    public Vertex getV() {
        return v;
    }
}
