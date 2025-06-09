import java.util.*;

public class ShortestPath {
    static final int INF = Integer.MAX_VALUE;
    static final String[] cities = {"서울", "원주", "천안", "논산", "대전", "강릉", "광주", "대구", "포항", "부산"};
    
    //도시 그래프 초기화
    public static int[][] initializeCityGraph() {
        int[][] graph = {
            { 0,  15,  12, INF, INF, INF, INF, INF, INF, INF }, //서울
            { 15,  0, INF,  INF, INF, 21, INF, 7, INF, INF }, // 원주
            { 12,  INF, 0, 4, 10, INF, INF, INF, INF, INF }, //천안
            { INF, INF, 4,   0, 3, INF, 13, INF, INF, INF }, //논산
            { INF, INF, 10, 3, 0, INF, INF, 10, INF, INF }, //대전
            { INF, 21, INF, INF, INF, 0, INF, INF, 25, INF }, //강릉
            { INF, INF, INF, 13, INF, INF, 0, INF, INF, 15 }, //광주
            { INF, 7, INF, INF, 10, INF, INF, 0, 19, 9 }, //대구
            { INF, INF, INF, INF, INF, 25, INF, 19, 0, 5 }, //포항
            { INF, INF, INF, INF, INF, INF, 15, 9, 5, 0 }  //부산
        };
        return graph;
    }
    
    // 랜덤 그래프 생성 (연결된 방향성 가중치 그래프)
    public static int[][] generateConnectedGraph(int vertices) {
        int[][] graph = new int[vertices][vertices];
        Random random = new Random();
        
        // 초기화: 모든 간선을 INF로 설정
        for (int i = 0; i < vertices; i++) {
            Arrays.fill(graph[i], INF);
            graph[i][i] = 0;
        }
        
        // 최소 스패닝 트리를 생성하여 그래프가 연결되도록 보장
        for (int i = 1; i < vertices; i++) {
            int j = random.nextInt(i);
            int weight = random.nextInt(30) + 1;
            graph[j][i] = weight;
            graph[i][j] = weight;
        }
        
        // 추가 간선 생성 (연결 확률 30%)
        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                if (i != j && graph[i][j] == INF && random.nextDouble() < 0.3) {
                    graph[i][j] = random.nextInt(30) + 1;
                }
            }
        }
        
        return graph;
    }
    
    // Dijkstra 알고리즘
    public static int[] dijkstra(int[][] graph, int start) {
        int vertices = graph.length;
        int[] dist = new int[vertices];
        boolean[] visited = new boolean[vertices];
        int[] prev = new int[vertices];
        
        Arrays.fill(dist, INF);
        Arrays.fill(prev, -1);
        dist[start] = 0;
        
        for (int count = 0; count < vertices - 1; count++) {
            int u = minDistance(dist, visited);
            visited[u] = true;
            
            for (int v = 0; v < vertices; v++) {
                if (!visited[v] && graph[u][v] != INF && 
                    dist[u] != INF && dist[u] + graph[u][v] < dist[v]) {
                    dist[v] = dist[u] + graph[u][v];
                    prev[v] = u;
                }
            }
        }
        
        return prev;
    }
    
    // Floyd 알고리즘
    public static int[][] floyd(int[][] graph) {
        int vertices = graph.length;
        int[][] dist = new int[vertices][vertices];
        int[][] next = new int[vertices][vertices];
        
        // 초기화
        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                dist[i][j] = graph[i][j];
                if (graph[i][j] != INF) {
                    next[i][j] = j;
                } else {
                    next[i][j] = -1;
                }
            }
        }
        
        // Floyd 알고리즘 수행
        for (int k = 0; k < vertices; k++) {
            for (int i = 0; i < vertices; i++) {
                for (int j = 0; j < vertices; j++) {
                    if (dist[i][k] != INF && dist[k][j] != INF && 
                        dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                        next[i][j] = next[i][k];
                    }
                }
            }
        }
        
        return next;
    }
    
    // 최소 거리를 가진 정점 찾기
    private static int minDistance(int[] dist, boolean[] visited) {
        int min = INF;
        int minIndex = -1;
        
        for (int v = 0; v < dist.length; v++) {
            if (!visited[v] && dist[v] < min) {
                min = dist[v];
                minIndex = v;
            }
        }
        
        return minIndex;
    }
    
    // Dijkstra 경로 출력
    public static void printDijkstraPath(int[] prev, int start, int end) {
        if (prev[end] == -1) {
            System.out.println("경로가 존재하지 않습니다.");
            return;
        }
        
        List<Integer> path = new ArrayList<>();
        for (int v = end; v != -1; v = prev[v]) {
            path.add(v);
        }
        Collections.reverse(path);
        
        System.out.print("Dijkstra 경로: ");
        for (int i = 0; i < path.size(); i++) {
            System.out.print(path.get(i));
            if (i < path.size() - 1) {
                System.out.print(" -> ");
            }
        }
        System.out.println();
    }
    
    // Floyd 경로 출력
    public static void printFloydPath(int[][] next, int start, int end) {
        if (next[start][end] == -1) {
            System.out.println("경로가 존재하지 않습니다.");
            return;
        }
        
        System.out.print("Floyd 경로: ");
        System.out.print(start);
        while (start != end) {
            start = next[start][end];
            System.out.print(" -> " + start);
        }
        System.out.println();
    }
    
    // 거리 행렬 출력
    public static void printDistanceMatrix(int[][] graph) {
        System.out.println("\n거리 행렬:");
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph.length; j++) {
                if (graph[i][j] == INF) {
                    System.out.print("INF\t");
                } else {
                    System.out.print(graph[i][j] + "\t");
                }
            }
            System.out.println();
        }
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("\n1. Dijkstra 알고리즘 시간 측정");
            System.out.println("2. Floyd 알고리즘 시간 측정");
            System.out.println("3. 경로 추적 테스트");
            System.out.println("4. 종료");
            System.out.print("선택: ");
            
            int choice = scanner.nextInt();
            
            if (choice == 1) {
                // Dijkstra 알고리즘 시간 측정
                for (int vertices = 10; vertices <= 20; vertices += 10) {
                    System.out.println("\n=== " + vertices + "개 정점 그래프 ===");
                    
                    // 랜덤 그래프 생성
                    int[][] graph = generateConnectedGraph(vertices);
                    
                    // 모든 시작점에 대해 Dijkstra 알고리즘 실행
                    long startTime = System.nanoTime();
                    for (int start = 0; start < vertices; start++) {
                        int[] prev = dijkstra(graph, start);
                        // 각 시작점에서 다른 모든 정점까지의 경로 출력
                        for (int end = 0; end < vertices; end++) {
                            if (start != end) {
                                System.out.println("\n" + start + "에서 " + end + "까지의 최단 경로:");
                                printDijkstraPath(prev, start, end);
                            }
                        }
                    }
                    long endTime = System.nanoTime();
                    
                    System.out.println("\nDijkstra 알고리즘 실행 시간: " + 
                                     (endTime - startTime) / 1000000.0 + "ms");
                    
                    // 거리 행렬 출력 (10개 정점인 경우에만)
                    if (vertices == 10) {
                        printDistanceMatrix(graph);
                    }
                }
            } else if (choice == 2) {
                // Floyd 알고리즘 시간 측정
                for (int vertices = 10; vertices <= 20; vertices += 10) {
                    System.out.println("\n=== " + vertices + "개 정점 그래프 ===");
                    
                    // 랜덤 그래프 생성
                    int[][] graph = generateConnectedGraph(vertices);
                    
                    // Floyd 알고리즘 실행
                    long startTime = System.nanoTime();
                    int[][] next = floyd(graph);
                    long endTime = System.nanoTime();
                    
                    System.out.println("Floyd 알고리즘 실행 시간: " + 
                                     (endTime - startTime) / 1000000.0 + "ms");
                    
                    // 거리 행렬 출력 (10개 정점인 경우에만)
                    if (vertices == 10) {
                        printDistanceMatrix(graph);
                    }
                }
            } else if (choice == 3) {
                // 경로 추적 테스트
                System.out.print("정점 개수 입력: ");
                int vertices = scanner.nextInt();
                
                int[][] graph = generateConnectedGraph(vertices);
                System.out.println("\n생성된 그래프의 거리 행렬:");
                printDistanceMatrix(graph);
                
                while (true) {
                    System.out.print("\n경로를 입력하세요 (시작점,도착점) 또는 -1,-1로 종료: ");
                    String[] input = scanner.next().split(",");
                    int start = Integer.parseInt(input[0]);
                    int end = Integer.parseInt(input[1]);
                    
                    if (start == -1 && end == -1) break;
                    
                    if (start >= 0 && start < vertices && end >= 0 && end < vertices) {
                        System.out.println("\n" + start + "에서 " + end + "까지의 최단 경로:");
                        
                        // Dijkstra 알고리즘
                        int[] prev = dijkstra(graph, start);
                        printDijkstraPath(prev, start, end);
                        
                        // Floyd 알고리즘
                        int[][] next = floyd(graph);
                        printFloydPath(next, start, end);
                    } else {
                        System.out.println("잘못된 정점 번호입니다.");
                    }
                }
            } else if (choice == 4) {
                break;
            } else {
                System.out.println("잘못된 선택입니다.");
            }
        }
        
        scanner.close();
    }
} 