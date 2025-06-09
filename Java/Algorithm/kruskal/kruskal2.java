package kruskal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class kruskal2 {
    private int numVertices;  //정점의 개수
    private ArrayList<Edge> edges; 
    private int[] parent; //부모 배열
    //사이클 발생으로 선택되지 않은 간선들을 저장할 리스트
    private static ArrayList<Edge> rejectedEdges = new ArrayList<>();
    
    //간선을 표현하는 내부 정적 클래스 
    //시작 정점 src, 도착 정점 dest, 가중치 weight
    public static class Edge {
        public int src, dest, weight;
        
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
    public kruskal2(int numVertices) {
        this.numVertices = numVertices;
        this.edges = new ArrayList<>();
        this.parent = new int[numVertices + 1]; //1부터 numVertices까지
        
        //부모 배열 초기화
        for (int i = 0; i <= numVertices; i++) {
            parent[i] = i;
        }
    }
    
    //간선 리스트를 반환하는 메소드
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
        System.out.println("\n그래프 정보");
        for (int i = 0; i < edges.size(); i++) {
            Edge edge = edges.get(i);
            //모든 간선 정보를 출력
            System.out.println((i + 1) + ". edge: (" + edge.src + "," + edge.dest + "), weight: " + edge.weight);
        }
        System.out.println("\n정점 수: " + numVertices);
        System.out.println("간선 수: " + edges.size());
    }
    
    //Kruskal 알고리즘으로 MST 찾기 (첫 번째 간선 포함 + 사이클 발생 간선 출력 기능)
    public static ArrayList<Edge> findMST(kruskal2 graph, int numVertices) {
        ArrayList<Edge> mst = new ArrayList<>();
        ArrayList<Edge> edges = graph.getEdges();
        
        //사이클 발생 간선 리스트 초기화
        rejectedEdges.clear();
        
        //첫 번째 생성된 간선 확보 (무조건 MST에 포함시키기 위해)
        Edge firstEdge = null;
        if (!edges.isEmpty()) {
            firstEdge = edges.get(0);
        }
        
        //첫 번째 간선을 제외한 나머지 간선을 가중치 기준으로 정렬
        ArrayList<Edge> remainingEdges = new ArrayList<>();
        for (int i = 1; i < edges.size(); i++) {
            remainingEdges.add(edges.get(i));
        }
        
        //간선을 가중치 기준으로 오름차순 정렬
        Collections.sort(remainingEdges, new Comparator<Edge>() {
            @Override
            public int compare(Edge e1, Edge e2) {
                return e1.weight - e2.weight;
            }
        });
        
        //부모 배열 초기화
        int[] parent = new int[numVertices + 1];
        for (int i = 0; i <= numVertices; i++) {
            parent[i] = i;
        }
        
        //첫 번째 간선을 MST에 추가 
        if (firstEdge != null) {
            mst.add(firstEdge);
            union(parent, firstEdge.src, firstEdge.dest);
        }
        
        //정렬된 간선을 하나씩 고려
        for (Edge edge : remainingEdges) {
            int rootSrc = find(parent, edge.src);
            int rootDest = find(parent, edge.dest);
            
            //사이클이 형성되지 않으면 MST에 추가
            if (rootSrc != rootDest) {
                mst.add(edge);
                union(parent, rootSrc, rootDest);
                
                //MST는 정점 개수 - 1개의 간선을 가짐
                if (mst.size() == numVertices - 1) {
                    //MST가 완성되면 남은 간선들도 모두 사이클을 형성하는 간선으로 간주
                    for (Edge remainingEdge : remainingEdges) {
                        if (!mst.contains(remainingEdge)) {
                            rejectedEdges.add(remainingEdge);
                        }
                    }
                    break;
                }
            } else {
                //사이클이 발생하는 간선 저장
                rejectedEdges.add(edge);
            }
        }
        
        return mst;
    }
    
    //Find 연산 - 경로 압축 사용 (MST 알고리즘용)
    private static int find(int[] parent, int x) {
        if (parent[x] != x) {
            parent[x] = find(parent, parent[x]);
        }
        return parent[x];
    }
    
    //Union 연산 (MST 알고리즘용)
    private static void union(int[] parent, int x, int y) {
        parent[y] = x;
    }
    
    //MST를 출력하는 메소드
    public static void printMST(ArrayList<Edge> mst) {
        System.out.println("\nMinimum Spanning Tree (MST)");
        int totalWeight = 0;
        
        for (int i = 0; i < mst.size(); i++) {
            Edge edge = mst.get(i);
            String marker = i == 0 ? " (첫 번째 간선)" : "";
            System.out.println((i + 1) + ". MST edge: (" + edge.src + "," + edge.dest + "), weight: " + edge.weight + marker);
            totalWeight += edge.weight;
        }
        
        System.out.println("\nMST의 총 간선 개수: " + mst.size());
        System.out.println("MST의 총 가중치: " + totalWeight);
    }
    
    //사이클을 형성하는 간선을 출력하는 메소드
    public static void printRejectedEdges() {
        System.out.println("\n사이클 발생으로 선택되지 않은 간선");
        
        if (rejectedEdges.isEmpty()) {
            System.out.println("사이클을 형성하는 간선이 없습니다.");
            return;
        }
        
        for (int i = 0; i < rejectedEdges.size(); i++) {
            Edge edge = rejectedEdges.get(i);
            System.out.println((i + 1) + ". rejected edge: (" + edge.src + "," + edge.dest + "), weight: " + edge.weight);
        }
        
        System.out.println("\n사이클 발생 간선 개수: " + rejectedEdges.size());
    }
    
    public static void main(String[] args) {
        int numVertices = 10; //정점 수
        kruskal2 graph = new kruskal2(numVertices);
        
        //연결 그래프가 될 때까지 간선 추가
        while (!graph.isConnected()) {
            graph.generateRandomEdge();
        }
        
        //추가 간선 생성하여 더 다양한 MST를 만들기
        for (int i = 0; i < 10; i++) {
            graph.generateRandomEdge();
        }
        
        //그래프 정보 출력
        System.out.println("Random Graph Generation");
        graph.printGraph();
        
        //Kruskal 알고리즘으로 MST 찾기
        ArrayList<Edge> mst = findMST(graph, numVertices);
        
        //MST 출력
        printMST(mst);
        
        //사이클 발생으로 선택되지 않은 간선 출력
        printRejectedEdges();
    }
}
