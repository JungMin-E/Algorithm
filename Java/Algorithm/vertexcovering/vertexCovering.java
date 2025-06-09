package vertexcovering;

import java.util.*;

public class vertexCovering {
    
    static class Graph {
        private int vertices;           //정점의 개수
        private List<List<Integer>> adjacencyList;  //인접 리스트
        private List<Edge> edges;       //모든 간선들의 리스트
        
        public Graph(int vertices) {
            this.vertices = vertices;
            this.adjacencyList = new ArrayList<>();
            this.edges = new ArrayList<>();
            
            //인접 리스트 초기화
            for (int i = 0; i < vertices; i++) {
                adjacencyList.add(new ArrayList<>());
            }
        }
        
        public void addEdge(int u, int v) {
            adjacencyList.get(u).add(v);
            adjacencyList.get(v).add(u);
            edges.add(new Edge(u, v));
        }
        
        public int getDegree(int vertex) {
            return adjacencyList.get(vertex).size();
        }
        
        public List<Integer> getNeighbors(int vertex) {
            return adjacencyList.get(vertex);
        }
        
        public int getVertices() {
            return vertices;
        }
        
        public List<Edge> getEdges() {
            return edges;
        }
        
        public void printGraph() {
            System.out.println("그래프 정보");
            System.out.println("정점 수: " + vertices);
            System.out.println("간선 수: " + edges.size());
            
            System.out.println("\n인접 리스트:");
            for (int i = 0; i < vertices; i++) {
                System.out.print("정점 " + i + ": ");
                for (int neighbor : adjacencyList.get(i)) {
                    System.out.print(neighbor + " ");
                }
                System.out.println("(차수: " + getDegree(i) + ")");
            }
            
            System.out.println("\n간선 리스트:");
            for (Edge edge : edges) {
                System.out.println("(" + edge.u + ", " + edge.v + ")");
            }
        }
    }
    
    //간선 클래스
    static class Edge {
        int u, v;  //간선의 양 끝점
        
        public Edge(int u, int v) {
            this.u = u;
            this.v = v;
        }
        
        @Override
        public String toString() {
            return "(" + u + ", " + v + ")";
        }
    }
    
    //랜덤 그래프 생성
    public static Graph generateRandomGraph(int minVertices, int maxVertices, 
                                          int minEdges, int maxEdges) {
        Random random = new Random();
        
        //정점 수 랜덤 결정 (10~20개)
        int vertices = minVertices + random.nextInt(maxVertices - minVertices + 1);
        Graph graph = new Graph(vertices);
        
        //간선 수 랜덤 결정 (20~30개)
        int targetEdges = minEdges + random.nextInt(maxEdges - minEdges + 1);
        
        //최대 가능한 간선 수 계산 (완전 그래프)
        int maxPossibleEdges = vertices * (vertices - 1) / 2;
        targetEdges = Math.min(targetEdges, maxPossibleEdges);
        
        Set<String> addedEdges = new HashSet<>();  //중복 간선 방지
        
        //연결된 그래프를 만들기 위해 먼저 스패닝 트리 생성
        for (int i = 1; i < vertices; i++) {
            int parent = random.nextInt(i);
            graph.addEdge(parent, i);
            addedEdges.add(getEdgeKey(parent, i));
        }
        
        //나머지 간선들을 랜덤하게 추가
        int currentEdges = vertices - 1;  //스패닝 트리의 간선 수
        
        while (currentEdges < targetEdges) {
            int u = random.nextInt(vertices);
            int v = random.nextInt(vertices);
            
            if (u != v) {  //자기 루프 방지
                String edgeKey = getEdgeKey(u, v);
                if (!addedEdges.contains(edgeKey)) {
                    graph.addEdge(u, v);
                    addedEdges.add(edgeKey);
                    currentEdges++;
                }
            }
        }
        
        return graph;
    }
    
    private static String getEdgeKey(int u, int v) {
        return Math.min(u, v) + "-" + Math.max(u, v);
    }
    
    public static Set<Integer> vertexPrioritySelection(Graph graph) {
        Set<Integer> vertexCover = new HashSet<>();
        Set<Edge> uncoveredEdges = new HashSet<>(graph.getEdges());
        boolean[] covered = new boolean[graph.getVertices()];
        
        //모든 간선이 커버될 때까지 반복
        while (!uncoveredEdges.isEmpty()) {
            int bestVertex = -1;
            int maxUncoveredEdges = -1;
            
            //각 정점에 대해 커버되지 않은 간선의 수 계산
            for (int v = 0; v < graph.getVertices(); v++) {
                if (covered[v]) continue;  //이미 선택된 정점은 스킵
                
                int uncoveredCount = 0;
                for (Edge edge : uncoveredEdges) {
                    if (edge.u == v || edge.v == v) {
                        uncoveredCount++;
                    }
                }
                
                //가장 많은 커버되지 않은 간선을 가진 정점 선택
                if (uncoveredCount > maxUncoveredEdges) {
                    maxUncoveredEdges = uncoveredCount;
                    bestVertex = v;
                }
            }
            
            //선택된 정점을 vertex cover에 추가
            if (bestVertex != -1) {
                vertexCover.add(bestVertex);
                covered[bestVertex] = true;
                
                //해당 정점과 연결된 모든 간선을 커버됨으로 표시
                Iterator<Edge> iterator = uncoveredEdges.iterator();
                while (iterator.hasNext()) {
                    Edge edge = iterator.next();
                    if (edge.u == bestVertex || edge.v == bestVertex) {
                        iterator.remove();
                    }
                }
            }
        }
        
        return vertexCover;
    }
    
    //Maximal Matching 알고리즘
    public static Set<Integer> maximalMatching(Graph graph) {
        Set<Integer> vertexCover = new HashSet<>();
        Set<Edge> edges = new HashSet<>(graph.getEdges());
        Set<Integer> matchedVertices = new HashSet<>();
        
        //최대 매칭 찾기
        List<Edge> matching = new ArrayList<>();
        
        for (Edge edge : edges) {
            //두 정점 모두 아직 매칭되지 않았다면 매칭에 추가
            if (!matchedVertices.contains(edge.u) && !matchedVertices.contains(edge.v)) {
                matching.add(edge);
                matchedVertices.add(edge.u);
                matchedVertices.add(edge.v);
            }
        }
        
        //매칭된 모든 간선의 양 끝점을 vertex cover에 추가
        for (Edge edge : matching) {
            vertexCover.add(edge.u);
            vertexCover.add(edge.v);
        }
        
        //매칭되지 않은 간선들 처리
        Set<Edge> uncoveredEdges = new HashSet<>();
        for (Edge edge : edges) {
            boolean covered = false;
            for (Edge matchedEdge : matching) {
                if ((edge.u == matchedEdge.u || edge.u == matchedEdge.v) ||
                    (edge.v == matchedEdge.u || edge.v == matchedEdge.v)) {
                    covered = true;
                    break;
                }
            }
            if (!covered) {
                uncoveredEdges.add(edge);
            }
        }
        
        //커버되지 않은 간선들을 처리하기 위해 추가 정점 선택
        for (Edge edge : uncoveredEdges) {
            if (!vertexCover.contains(edge.u) && !vertexCover.contains(edge.v)) {
                //차수가 더 높은 정점 선택
                if (graph.getDegree(edge.u) >= graph.getDegree(edge.v)) {
                    vertexCover.add(edge.u);
                } else {
                    vertexCover.add(edge.v);
                }
            }
        }
        
        return vertexCover;
    }
    
    static class ExperimentResult {
        int vertexPrioritySize;
        int maximalMatchingSize;
        long vpExecutionTime;
        long mmExecutionTime;
        
        public ExperimentResult(int vps, int mms, long vpTime, long mmTime) {
            this.vertexPrioritySize = vps;
            this.maximalMatchingSize = mms;
            this.vpExecutionTime = vpTime;
            this.mmExecutionTime = mmTime;
        }
    }
    
    public static void runExperiments() {
        List<ExperimentResult> results = new ArrayList<>();
        Graph finalGraph = null;
        Set<Integer> finalVpResult = null;
        Set<Integer> finalMmResult = null;
        
        //10번 반복 실험 (과정은 출력하지 않음)
        for (int experiment = 1; experiment <= 10; experiment++) {
            Graph graph = generateRandomGraph(10, 20, 20, 30);
            
            long startTime = System.nanoTime();
            Set<Integer> vpResult = vertexPrioritySelection(graph);
            long vpTime = System.nanoTime() - startTime;
            
            startTime = System.nanoTime();
            Set<Integer> mmResult = maximalMatching(graph);
            long mmTime = System.nanoTime() - startTime;
            
            results.add(new ExperimentResult(vpResult.size(), mmResult.size(), vpTime, mmTime));
            
            //10번째 실험 결과 저장
            if (experiment == 10) {
                finalGraph = graph;
                finalVpResult = vpResult;
                finalMmResult = mmResult;
            }
        }
        
        //10번째 실험 결과만 출력
        System.out.println("10번 반복후 실험 결과");
        System.out.println("그래프: 정점 " + finalGraph.getVertices() + "개, 간선 " + finalGraph.getEdges().size() + "개");
        System.out.println();
        
        System.out.println("Vertex Priority Selection: " + finalVpResult.size() + "개 정점 선택");
        System.out.println("     선택된 정점: " + finalVpResult);
        System.out.println();
        
        System.out.println("Maximal Matching: " + finalMmResult.size() + "개 정점 선택");
        System.out.println("     선택된 정점: " + finalMmResult);
        System.out.println();
        
        //효율성 비교
        if (finalVpResult.size() < finalMmResult.size()) {
            int diff = finalMmResult.size() - finalVpResult.size();
            double improvement = ((double)diff / finalMmResult.size()) * 100;
            System.out.printf("결과: Vertex Priority Selection이 %d개 더 적게 선택 (%.1f%% 더 효율적)\n", 
                            diff, improvement);
        } else if (finalMmResult.size() < finalVpResult.size()) {
            int diff = finalVpResult.size() - finalMmResult.size();
            double improvement = ((double)diff / finalVpResult.size()) * 100;
            System.out.printf("결과: Maximal Matching이 %d개 더 적게 선택 (%.1f%% 더 효율적)\n", 
                            diff, improvement);
        } else {
            System.out.println("결과: 두 방식 모두 동일한 개수 선택");
        }
        
    }
    
    public static void main(String[] args) {
        runExperiments();
    }
}
