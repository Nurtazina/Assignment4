package com.company;
import java.util.*;


class Vertex<V>{
    private V data;
    private Map<Vertex<V>, Double> adjacentVertices = new HashMap<>();

    public Vertex(V data) {
        this.data = data;
    }

    public void addAdjacentVertex(Vertex<V> destination, double weight){
        adjacentVertices.put(destination, weight);
    }

    public Map<Vertex<V>, Double> getAdjacentVertices() {
        return adjacentVertices;
    }

    public V getData() {
        return data;
    }
}
class BreadthFirstSearch<T> extends Search<T>{

    public BreadthFirstSearch(WeightedGraph<T> graph, T source) {
        super(new Vertex<T>(source));
        bfs(graph, source);
    }

    private void bfs(WeightedGraph<T> graph, T current) {
        marked.add(new Vertex<T>(current));
        Queue<Vertex<T>> queue = new LinkedList<>();
        queue.add(new Vertex<T>(current));
        while (!queue.isEmpty()) {
            Vertex<T> v = queue.remove();
            for(Map.Entry<Vertex<T>, Double> entry : graph.map.get(graph.map.indexOf(v)).getAdjacentVertices().entrySet()){
                Vertex<T> vertex = entry.getKey();
                if (!marked.contains(vertex)) {
                    marked.add(vertex);
                    edgeTo.put(vertex, v);
                    queue.add(vertex);
                }
            }
        }
    }
}

class DepthFirstSearch<T> extends Search<T> {

    public DepthFirstSearch(WeightedGraph<T> graph, T source) {
        super(new com.company.Vertex<T>(source));
        dfs(graph, source);
    }

    private void dfs(WeightedGraph<T> graph, T current) {
        marked.add(new Vertex<T>(current));
        count++;

        Vertex<T>cur = new Vertex<T>(current);
        for(Map.Entry<Vertex<T>, Double> entry : graph.map.get(graph.map.indexOf(cur)).getAdjacentVertices().entrySet()){
            Vertex<T> vertex = entry.getKey();
            if (!marked.contains(vertex)) {
                edgeTo.put(vertex, new Vertex<T>(current));
                dfs(graph, vertex.getData());
            }
        }
    }

}

class Search<T> {
    protected int count;
    protected Set<Vertex<T>> marked;
    protected Map<Vertex<T>, Vertex<T>> edgeTo;
    protected final Vertex<T> source;

    public Search(Vertex<T> source) {
        this.source = source;
        marked = new HashSet<>();
        edgeTo = new HashMap<>();
    }

    public boolean hasPathTo(Vertex<T> v) {
        return marked.contains(v);
    }

    public Iterable<Vertex<T>> pathTo(T v) {
        if (!hasPathTo(new Vertex<T>(v))) return null;
        LinkedList<Vertex<T>> ls = new LinkedList<>();
        for (Vertex<T> i = new Vertex<T>(v); i != source; i = edgeTo.get(i)) {
            ls.push(i);
        }
        ls.push(source);

        return ls;
    }

    public int getCount() {
        return count;
    }
}

class WeightedGraph<T> {
    private final boolean undirected;
    public ArrayList<Vertex<T>> map = new ArrayList<>();
    public WeightedGraph() {
        this.undirected = true;
    }

    public WeightedGraph(boolean undirected) {
        this.undirected = undirected;
    }

    public void addVertex(T v) {
        map.add(new Vertex<T>(v));
    }

    public void addEdge(T source, T dest, double weight) {
        if (!hasVertex(source))
            addVertex(source);

        if (!hasVertex(dest))
            addVertex(dest);

        if (hasEdge(source, dest)
                || source.equals(dest))
            return; // reject parallels & self-loops

        if(map.indexOf(new Vertex<T>(source)) == -1){
            return;
        }

        map.get(map.indexOf(new Vertex<T>(source))).addAdjacentVertex(new Vertex<T>(dest), weight);


        if (undirected)
            map.get(map.indexOf(new Vertex<T>(dest))).addAdjacentVertex(new Vertex<T>(source), weight);
    }

    public int getVerticesCount() {
        return map.size();
    }

    public int getEdgesCount() {
        int count = 0;

        for(Vertex<T> to : map){
            count += to.getAdjacentVertices().size();
        }

        if (undirected)
            count /= 2;

        return count;
    }


    public boolean hasVertex(T v) {
        return map.contains(new Vertex<T>(v));
    }

    public boolean hasEdge(T source, T dest) {
        if (!hasVertex(source)) return false;
        return map.get(map.indexOf(new Vertex<T>(source))).getAdjacentVertices().containsKey(new Vertex<T>(dest));
    }
    /*
    public Iterable<T> adjacencyList(T v) {
        if (!hasVertex(v)) return null;
        List<T> vertices = new LinkedList<>();
        for (Edge<T> e : map.get(v)) {
            vertices.add(e.getDest());
        }
        return vertices;
    }

    public Iterable<Edge<T>> getEdges(T v) {
        if (!hasVertex(v)) return null;
        return map.get(v);
    }
     */
}

