package com.notorious.visualization.graphing.algorithm.tree.spanning.kruskal; /******************************************************************************
 *  Compilation:  javac KruskalMST.java
 *  Execution:    java  KruskalMST filename.txt
 *  Dependencies: EdgeWeightedGraph.java Edge.java Queue.java
 *                UF.java In.java StdOut.java
 *  Data files:   http://algs4.cs.princeton.edu/43mst/tinyEWG.txt
 *                http://algs4.cs.princeton.edu/43mst/mediumEWG.txt
 *                http://algs4.cs.princeton.edu/43mst/largeEWG.txt
 *
 *  Compute a minimum spanning forest using Kruskal's algorithm.
 *
 *  %  java KruskalMST tinyEWG.txt 
 *  0-7 0.16000
 *  2-3 0.17000
 *  1-7 0.19000
 *  0-2 0.26000
 *  5-7 0.28000
 *  4-5 0.35000
 *  6-2 0.40000
 *  1.81000
 *
 *  % java KruskalMST mediumEWG.txt
 *  168-231 0.00268
 *  151-208 0.00391
 *  7-157   0.00516
 *  122-205 0.00647
 *  8-152   0.00702
 *  156-219 0.00745
 *  28-198  0.00775
 *  38-126  0.00845
 *  10-123  0.00886
 *  ...
 *  10.46351
 *
 ******************************************************************************/

import com.notorious.visualization.graphing.algorithm.graph.Edge;
import com.notorious.visualization.graphing.algorithm.graph.WeightedEdgeGraph;
import com.notorious.visualization.graphing.algorithm.tree.spanning.prim.RenderablePrimMST;
import com.notorious.visualization.graphing.collection.queue.Queue;
import com.notorious.visualization.graphing.util.MinPQ;
import com.notorious.visualization.graphing.util.StdDraw;
import com.notorious.visualization.graphing.util.union.UF;

import java.awt.*;

/**
 * A modern adaptation of the KruskalMST.java written by Robert Sedgewick and Kevin Wayne.
 * Following a much stricter OOP structure, adding encapsulation, removing literal null
 * values, and introducing some of the features given by Java 8. This class renders the
 * given edge graph, and each step of the process in 250ms intervals; supported by
 * code written by Quinn Rohlf.
 *
 * <p>
 * ORIGINAL DOCUMENTATION:
 * -------------------------------------------------------------------------------
 *  The {@code KruskalMST} class represents a data type for computing a
 *  <em>minimum spanning tree</em> in an edge-weighted graph.
 *  The edge weights can be positive, zero, or negative and need not
 *  be distinct. If the graph is not connected, it computes a <em>minimum
 *  spanning forest</em>, which is the union of minimum spanning trees
 *  in each connected component. The {@code weight()} method returns the 
 *  weight of a minimum spanning tree and the {@code edges()} method
 *  returns its edges.
 *  <p>
 *  This implementation uses <em>Krusal's algorithm</em> and the
 *  union-find data type.
 *  The constructor takes time proportional to <em>E</em> log <em>E</em>
 *  and extra space (not including the graph) proportional to <em>V</em>,
 *  where <em>V</em> is the number of vertices and <em>E</em> is the number of edges.
 *  Afterwards, the {@code weight()} method takes constant time
 *  and the {@code edges()} method takes time proportional to <em>V</em>.
 *  <p>
 *  For additional documentation,
 *  see <a href="http://algs4.cs.princeton.edu/43mst">Section 4.3</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *  For alternate implementations, see {@link LazyPrimMST}, {@link RenderablePrimMST},
 *  and {@link BoruvkaMST}.
 * -------------------------------------------------------------------------------
 *
 * @author Notorious
 * @version 0.0.1
 * @since 3/13/2017
 */
public class RenderableKruskalMST {
    private static final double FLOATING_POINT_EPSILON = 1E-12;

    private final double[][] coordinates;
    private double weight;                        // weight of MST
    private Queue<Edge> mst;// edges in MST

    /**
     * Compute a minimum spanning tree (or forest) of an edge-weighted graph. All
     * while animating the process using the {@link StdDraw} class for rendering.
     *
     * @param edgeGraph the edge-weighted graph
     * @param coordinates the 2D coordinates of the given edge graph
     */
    public RenderableKruskalMST(WeightedEdgeGraph edgeGraph, double[][] coordinates) {
        this.coordinates = coordinates;
        mst = new Queue<>();
        // more efficient to build heap by passing array of edges
        MinPQ<Edge> pq = new MinPQ<>();
        for (Edge e : edgeGraph.getEdges()) {
            pq.insert(e);
        }

        // run greedy algorithm
        UF uf = new UF(edgeGraph.getVerticesCount());
        while (!pq.isEmpty() && mst.size() < edgeGraph.getVerticesCount() - 1) {
            Edge e = pq.delMin();
            int v = e.getEndpointA();
            int w = e.getOtherEndpoint(v);
            StdDraw.setPenColor();
            StdDraw.filledCircle(getXCoordinate(w), getYCoordinate(w), .01);
            StdDraw.filledCircle(getXCoordinate(v), getYCoordinate(v), .01);
            if (!uf.connected(v, w)) { // v-w does not create a cycle
                StdDraw.show(250);
                StdDraw.setPenColor(Color.MAGENTA);
                StdDraw.setPenRadius(.011);
                StdDraw.line(getXCoordinate(v), getYCoordinate(v), getXCoordinate(w), getYCoordinate(w));
                uf.union(v, w);  // merge v and w components
                mst.enqueue(e);  // add edge e to mst
                weight += e.getWeight();
            }
        }
        // check optimality conditions
        assert check(edgeGraph);
    }

    /**
     * Returns the edges in a minimum spanning tree (or forest).
     * @return the edges in a minimum spanning tree (or forest) as
     *    an iterable of edges
     */
    public Iterable<Edge> getEdges() {
        return mst;
    }

    /**
     * Returns the sum of the edge weights in a minimum spanning tree (or forest).
     * @return the sum of the edge weights in a minimum spanning tree (or forest)
     */
    public double getWeight() {
        return weight;
    }
    
    // check optimality conditions (takes time proportional to E V lg* V)
    private boolean check(WeightedEdgeGraph edgeGraph) {

        // check total weight
        double total = 0D;
        for (Edge e : getEdges()) {
            total += e.getWeight();
        }
        if (Math.abs(total - getWeight()) > FLOATING_POINT_EPSILON) {
            System.err.printf("Weight of edges does not equal weight(): %f vs. %f\n", total, getWeight());
            return false;
        }

        // check that it is acyclic
        UF uf = new UF(edgeGraph.getVerticesCount());
        for (Edge e : getEdges()) {
            int v = e.getEndpointA(), w = e.getOtherEndpoint(v);
            if (uf.connected(v, w)) {
                System.err.println("Not a forest!");
                return false;
            }
            uf.union(v, w);
        }

        // check that it is a spanning forest
        for (Edge e : edgeGraph.getEdges()) {
            int v = e.getEndpointA(), w = e.getOtherEndpoint(v);
            if (!uf.connected(v, w)) {
                System.err.println("Not a spanning forest!");
                return false;
            }
        }

        // check that it is a minimal spanning forest (cut optimality conditions)
        for (Edge e : getEdges()) {
            // all edges in MST except e
            uf = new UF(edgeGraph.getVerticesCount());
            for (Edge f : mst) {
                int x = f.getEndpointA(), y = f.getOtherEndpoint(x);
                if (f != e) uf.union(x, y);
            }
            
            // check that e is min weight edge in crossing cut
            for (Edge f : edgeGraph.getEdges()) {
                int x = f.getEndpointA(), y = f.getOtherEndpoint(x);
                if (!uf.connected(x, y)) {
                    if (f.getWeight() < e.getWeight()) {
                        System.err.println("Edge " + f + " violates cut optimality conditions!");
                        return false;
                    }
                }
            }

        }

        return true;
    }

    //Convenience methods for coordinate access
    private double getXCoordinate(int i) {
        return coordinates[i][0];
    }

    private double getYCoordinate(int i) {
        return coordinates[i][1];
    }

}
