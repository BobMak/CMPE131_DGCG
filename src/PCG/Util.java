package PCG;

import com.sun.corba.se.impl.orbutil.graph.Graph;

import java.util.*;

public class Util {
  public static int randint(int min, int max) {
    return (int) ((Math.random() * (max - min)) + min);
  }

  // An Inplace function to rotate a N x N matrix
  // by 90 degrees in anti-clockwise direction
  // source: https://www.geeksforgeeks.org/inplace-rotate-square-matrix-by-90-degrees/
  static void rotateMatrix( int N, int mat[][] )
  {
    for (int x = 0; x < N / 2; x++) {
      for (int y = x; y < N - x - 1; y++) {
        int temp = mat[x][y];
        mat[x][y] = mat[y][N - 1 - x];
        mat[y][N - 1 - x] = mat[N - 1 - x][N - 1 - y];
        mat[N - 1 - x][N - 1 - y] = mat[N - 1 - y][x];
        mat[N - 1 - y][x] = temp;
      }
    }
  }

  static boolean randBool() {
    Random rn = new Random();
    return rn.nextBoolean();
  }

  public static void sop(Object msg){
    System.out.println(msg);
  }

  /*
  // source: https://www.baeldung.com/java-a-star-pathfinding
  public static class AStar() {

    public interface GraphNode {
      String getId();
    }

    public class Graph<TileNode extends GraphNode> {
      private final Set<TileNode> nodes;
      private final Map<String, Set<String>> connections;

      Graph() {
        nodes = new Set<TileNode>();
      }

      public TileNode getNode(String id) {
        return nodes.stream()
          .filter(node -> node.getId().equals(id))
          .findFirst()
          .orElseThrow(() -> new IllegalArgumentException("No node found with ID"));
      }
    }

    public class TileNode implements GraphNode {
      private final String id;
      private final int x;
      private final int y;

      TileNode(String id, String name, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
      }
      @Override
      public String getId() {
        return id;
      }

      public int getX() {
        return x;
      }

      public int getY() {
        return y;
      }
    }

    class RouteNode<TileNode extends GraphNode> implements Comparable<RouteNode> {
      private final TileNode current;
      private TileNode previous;
      private double routeScore;
      private double estimatedScore;

      RouteNode(TileNode current) {
        this(current, null, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
      }

      RouteNode(TileNode current, TileNode previous, double routeScore, double estimatedScore) {
        this.current = current;
        this.previous = previous;
        this.routeScore = routeScore;
        this.estimatedScore = estimatedScore;
      }

      public TileNode getCurrent() {
        return current;
      }

      public TileNode getPrevious() {
        return previous;
      }

      @Override
      public int compareTo(RouteNode other) {
        if (this.estimatedScore > other.estimatedScore) {
          return 1;
        } else if (this.estimatedScore < other.estimatedScore) {
          return -1;
        } else {
          return 0;
        }
      }
    }

    public class RouteFinder<TileNode extends GraphNode> {
      private final Graph<TileNode>  graph;
      private final Scorer<TileNode> nextNodeScorer;
      private final Scorer<TileNode> targetScorer;

      public RouteFinder(Graph<TileNode> graph, Scorer<TileNode> nextNodeScorer, Scorer<TileNode> targetScorer) {
        this.graph = graph;
        this.nextNodeScorer = nextNodeScorer;
        this.targetScorer = targetScorer;
      }

      public List<TileNode> findRoute(TileNode from, TileNode to) {
        Queue<RouteNode> openSet = new PriorityQueue<>();
        Map<TileNode, RouteNode<TileNode>> allNodes = new HashMap<>();

        RouteNode<TileNode> start = new RouteNode<TileNode>(from, null, 0d, targetScorer.computeCost(from, to));
        openSet.add(start);
        allNodes.put(from, start);
        while (!openSet.isEmpty()) {
          RouteNode<TileNode> next = openSet.poll();
          // found
          if (next.getCurrent().equals(to)) {
            List<TileNode> route = new ArrayList<>();
            RouteNode<TileNode> current = next;
            do {
              route.add(0, current.getCurrent());
              current = allNodes.get(current.getPrevious());
            } while (current != null);
            return route;
          }

          graph.getConnections(next.getCurrent()).forEach(connection -> {
            RouteNode<TileNode> nextNode = allNodes.getOrDefault(connection, new RouteNode<TileNode>(connection));
            allNodes.put(connection, nextNode);

            double newScore = next.getRouteScore() + nextNodeScorer.computeCost(next.getCurrent(), connection);
            if (newScore < nextNode.getRouteScore()) {
              nextNode.setPrevious(next.getCurrent());
              nextNode.setRouteScore(newScore);
              nextNode.setEstimatedScore(newScore + targetScorer.computeCost(connection, to));
              openSet.add(nextNode);
            }
          });
        }
        throw new IllegalStateException("No route found");
      }
    }
  }
  */
}
