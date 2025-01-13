package board;

import java.util.Objects;

public class Edge {

    protected final Vertex u;
    protected final Vertex v;

    public Edge(Vertex u, Vertex v) {
        this.u = u;
        this.v = v;
    }

    public Vertex getU() {
        return u;
    }

    public Vertex getV() {
        return v;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return Objects.equals(u, edge.u) && Objects.equals(v, edge.v);
    }

    @Override
    public int hashCode() {
        return Objects.hash(u, v);
    }
}
