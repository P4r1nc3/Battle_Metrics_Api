package com.battlemetrics.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class GraphGenerator {
    private JSONObject graph;

    private Map<String, Integer> nodeIndexMap = new HashMap<>();
    private int counter = 0;

    public GraphGenerator() {
        this.graph = new JSONObject();
        this.graph.put("nodes", new JSONArray());
        this.graph.put("edges", new JSONArray());
    }

    public void addNode(String label) {
        JSONObject node = new JSONObject();
        node.put("id", counter);
        node.put("label", label);

        nodeIndexMap.put(label, counter);

        JSONArray nodesArray = graph.getJSONArray("nodes");
        nodesArray.put(node);

        counter++;
    }

    public void addEdge(int from, int to) {
        JSONObject edge = new JSONObject();
        edge.put("from", from);
        edge.put("to", to);

        JSONArray edgesArray = graph.getJSONArray("edges");
        edgesArray.put(edge);
    }

    public JSONObject getGraph() {
        return graph;
    }

    public int getNodeIndex(String label) {
        return nodeIndexMap.get(label);
    }
}
