package kruskal;

import java.util.ArrayList;
import java.util.Random;

//가중치가 있는 무방향 그래프를 구현하는 클래스 연결 그래프가 생성될 때까지 랜덤 간선을 추가
public class weightUndGraph {
    private int numVertices;  //정점의 개수
    private ArrayList<Edge> edges; 
    private int[] parent; //부모 배열
    
    //간선을 표현하는 내부 정적 클래스 
    //시작 정점 src,  도착 정점 dest, 가중치 weight
    public static class Edge {
        public int src, dest, weight; // public으로 변경
        
        public Edge(int src, int dest, int weight) {
            this.src = src;
            this.dest = dest;
            this.weight = weight;
        }
        
        @Override
        public String toString() {
            return "(" + src + "," + dest + "), weight: " + weight;
        }
    }
    
    //생성자
    public weightUndGraph(int numVertices) {
        this.numVertices = numVertices;
        this.edges = new ArrayList<>();
        this.parent = new int[numVertices + 1]; //1부터 numVertices까지
        
        //부모 배열 초기화
        for (int i = 0; i <= numVertices; i++) {
            parent[i] = i;
        }
    }
    
    // 간선 리스트를 반환하는 메소드 추가
    public ArrayList<Edge> getEdges() {
        return new ArrayList<>(edges);
    }
    
    //정점이 속한 집합의 대표 루트를 찾는 함수
    private int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }
    
    //두 정점이 속한 집합을 합침
    private void union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        //두 정점의 루트가 다르면 하나로 합침
        if (rootX != rootY) {
            parent[rootY] = rootX;
        }
    }
    
    //간선 추가 메소드 
    public void addEdge(int src, int dest, int weight) {
        Edge edge = new Edge(src, dest, weight);
        edges.add(edge);
        union(src, dest); //간선 추가 시 두 정점 연결
    }
    
    //그래프가 연결되어 있는지 확인
    public boolean isConnected() {
        int root = find(1);
        for (int i = 2; i <= numVertices; i++) {
            if (find(i) != root) {
                return false;
            }
        }
        return true;
    }
    
    //랜덤 간선 생성 & 그래프 추가 메소드 
    public void generateRandomEdge() {
        Random rand = new Random();
        int src = rand.nextInt(numVertices) + 1; //1부터 numVertices까지의 랜덤 정점 선택
        int dest = rand.nextInt(numVertices) + 1;
        
        //자기 자신으로의 간선은 제외
        while (src == dest) {
            dest = rand.nextInt(numVertices) + 1;
        }
        
        int weight = rand.nextInt(20) + 1; //1부터 20까지의 가중치
        addEdge(src, dest, weight);
    }
    
    //그래프 정보 출력 메소드 
    public void printGraph() {
        for (int i = 0; i < edges.size(); i++) {
            Edge edge = edges.get(i);
            //모든 간선 정보를 출력
            System.out.println((i + 1) + ". random edge : (" + edge.src + "," + edge.dest + "), (" 
                              + edge.dest + "," + edge.src + "), weight: " + edge.weight);
        }
        System.out.println("\nNum ber of Vertices : " + numVertices);
        System.out.println("Number of Edges : " + edges.size());
    }
    
    public static void main(String[] args) {
        int numVertices = 10; //정점 수
        weightUndGraph graph = new weightUndGraph(numVertices);
        
        //연결 그래프가 될 때까지 간선 추가
        while (!graph.isConnected()) {
            graph.generateRandomEdge();
        }
        
        //그래프 정보 출력
        graph.printGraph();
    }
}
