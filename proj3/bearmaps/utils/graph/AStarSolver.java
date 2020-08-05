package bearmaps.utils.graph;

import bearmaps.utils.pq.MinHeapPQ;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {

    private SolverOutcome outcome;
    private List<Vertex> solution;
    private double totalWeight;
    private int numStates;
    private double exploreTime;
    private HashMap<Vertex, Double> mapDist;
    private HashMap<Vertex, Vertex> predecessors;

    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout) {
        numStates = 0;
        solution = new ArrayList<>();
        mapDist = new HashMap<>();
        predecessors = new HashMap<>();
        Stopwatch watch = new Stopwatch();
        MinHeapPQ<Vertex> heap = new MinHeapPQ<>();
        heap.insert(start, 0);
        mapDist.put(start, 0.0);
        while(heap.size() != 0 && !heap.peek().equals(end)) {
            if(watch.elapsedTime()>timeout*1000) {
                outcome = SolverOutcome.TIMEOUT;
                break;
            }
            Vertex curr = heap.poll();
            numStates+=1;
            for(WeightedEdge edge: input.neighbors(curr)) {
                Vertex p = (Vertex) edge.from();
                Vertex q = (Vertex) edge.to();
                double w = edge.weight();
                if(distTo(p) + w < distTo(q)) {
                    mapDist.put(q, distTo(p)+w);
                    predecessors.put(q, p);
                    if(heap.contains(q)) {
                        heap.changePriority(q, mapDist.get(q) + input.estimatedDistanceToGoal(q, end));
                    } else {
                        heap.insert(q, mapDist.get(q) + input.estimatedDistanceToGoal(q, end));
                    }
                }
            }
        }
        exploreTime = watch.elapsedTime();
        if(heap.size() == 0) {
            outcome = SolverOutcome.UNSOLVABLE;
        }
        if(outcome == SolverOutcome.TIMEOUT || outcome == SolverOutcome.UNSOLVABLE) {
            solution.clear();
            totalWeight = 0;
            return;
        }
        while(!end.equals(start)) {
            solution.add(0, end);
            end = predecessors.get(end);
        }
        outcome = SolverOutcome.SOLVED;
        solution.add(0, start);
        totalWeight = mapDist.get(end);
    }

    private double distTo(Vertex v) {
        if(mapDist.get(v) == null) {
            return Double.POSITIVE_INFINITY;
        } else {
            return mapDist.get(v);
        }
    }

    public SolverOutcome outcome() {
        return outcome;
    }

    public List<Vertex> solution() {
        return solution;
    }

    public double solutionWeight() {
        return totalWeight;
    }

    public int numStatesExplored() {
        return numStates;
    }

    public double explorationTime() {
        return exploreTime;
    }
}
