/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.dolphinscheduler.common.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DAGTest {

    private DAG<Integer, String, String> graph;
    private static final Logger logger = LoggerFactory.getLogger(DAGTest.class);

    @BeforeEach
    public void setup() {
        graph = new DAG<>();
    }

    @AfterEach
    public void tearDown() {
        clear();
    }

    private void clear() {
        graph = null;
        graph = new DAG<>();

        Assertions.assertEquals(graph.getNodesCount(), 0);
    }

    private void makeGraph() {
        clear();

        // 1->2
        // 2->5
        // 3->5
        // 4->6
        // 5->6
        // 6->7

        for (int i = 1; i <= 7; ++i) {
            graph.addNode(i, "v(" + i + ")");
        }

        // construction side
        Assertions.assertTrue(graph.addEdge(1, 2));

        Assertions.assertTrue(graph.addEdge(2, 5));

        Assertions.assertTrue(graph.addEdge(3, 5));

        Assertions.assertTrue(graph.addEdge(4, 6));

        Assertions.assertTrue(graph.addEdge(5, 6));

        Assertions.assertTrue(graph.addEdge(6, 7));

        Assertions.assertEquals(graph.getNodesCount(), 7);
        Assertions.assertEquals(graph.getEdgesCount(), 6);

    }

    /**
     * add node
     */
    @Test
    public void testAddNode() {
        clear();

        graph.addNode(1, "v(1)");
        graph.addNode(2, null);
        graph.addNode(5, "v(5)");

        Assertions.assertEquals(graph.getNodesCount(), 3);

        Assertions.assertEquals(graph.getNode(1), "v(1)");
        Assertions.assertTrue(graph.containsNode(1));

        Assertions.assertFalse(graph.containsNode(10));
    }

    /**
     * add edge
     */
    @Test
    public void testAddEdge() {
        clear();

        Assertions.assertFalse(graph.addEdge(1, 2, "edge(1 -> 2)", false));

        graph.addNode(1, "v(1)");

        Assertions.assertTrue(graph.addEdge(1, 2, "edge(1 -> 2)", true));

        graph.addNode(2, "v(2)");

        Assertions.assertTrue(graph.addEdge(1, 2, "edge(1 -> 2)", true));

        Assertions.assertFalse(graph.containsEdge(1, 3));

        Assertions.assertTrue(graph.containsEdge(1, 2));
        Assertions.assertEquals(graph.getEdgesCount(), 1);

        int node = 3;
        graph.addNode(node, "v(3)");
        Assertions.assertFalse(graph.addEdge(node, node));

    }

    /**
     * add subsequent node
     */
    @Test
    public void testSubsequentNodes() {
        makeGraph();

        Assertions.assertEquals(graph.getSubsequentNodes(1).size(), 1);

    }

    /**
     * test indegree
     */
    @Test
    public void testIndegree() {
        makeGraph();

        Assertions.assertEquals(graph.getIndegree(1), 0);
        Assertions.assertEquals(graph.getIndegree(2), 1);
        Assertions.assertEquals(graph.getIndegree(3), 0);
        Assertions.assertEquals(graph.getIndegree(4), 0);
    }

    /**
     * test begin node
     */
    @Test
    public void testBeginNode() {
        makeGraph();

        Assertions.assertEquals(graph.getBeginNode().size(), 3);

        Assertions.assertTrue(graph.getBeginNode().contains(1));
        Assertions.assertTrue(graph.getBeginNode().contains(3));
        Assertions.assertTrue(graph.getBeginNode().contains(4));
    }

    /**
     * test end node
     */
    @Test
    public void testEndNode() {
        makeGraph();

        Assertions.assertEquals(graph.getEndNode().size(), 1);

        Assertions.assertTrue(graph.getEndNode().contains(7));
    }

    /**
     * test cycle
     */
    @Test
    public void testCycle() {
        clear();

        for (int i = 1; i <= 5; ++i) {
            graph.addNode(i, "v(" + i + ")");
        }

        // construction side
        try {
            graph.addEdge(1, 2);
            graph.addEdge(2, 3);
            graph.addEdge(3, 4);

            Assertions.assertFalse(graph.hasCycle());
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail();
        }

        try {
            boolean addResult = graph.addEdge(4, 1);

            if (!addResult) {
                Assertions.assertTrue(true);
            }

            graph.addEdge(5, 1);

            Assertions.assertFalse(graph.hasCycle());
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail();
        }

        clear();

        // construction node
        for (int i = 1; i <= 5; ++i) {
            graph.addNode(i, "v(" + i + ")");
        }

        // construction side, 1->2, 2->3, 3->4
        try {
            graph.addEdge(1, 2);
            graph.addEdge(2, 3);
            graph.addEdge(3, 4);
            graph.addEdge(4, 5);
            graph.addEdge(5, 2);// 会失败，添加不进去，所以下一步无环

            Assertions.assertFalse(graph.hasCycle());
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }

    @Test
    public void testTopologicalSort() {
        makeGraph();

        try {
            // topological result is : 1 3 4 2 5 6 7
            List<Integer> topoList = new ArrayList<>();
            topoList.add(1);
            topoList.add(3);
            topoList.add(4);
            topoList.add(2);
            topoList.add(5);
            topoList.add(6);
            topoList.add(7);

            Assertions.assertEquals(graph.topologicalSort(), topoList);
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }

    @Test
    public void testTopologicalSort2() {
        clear();

        graph.addEdge(1, 2, null, true);
        graph.addEdge(2, 3, null, true);
        graph.addEdge(3, 4, null, true);
        graph.addEdge(4, 5, null, true);
        graph.addEdge(5, 1, null, false); // The loop will fail to add

        try {
            List<Integer> topoList = new ArrayList<>();// topological result is : 1 2 3 4 5
            topoList.add(1);
            topoList.add(2);
            topoList.add(3);
            topoList.add(4);
            topoList.add(5);

            Assertions.assertEquals(graph.topologicalSort(), topoList);

        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail();
        }

    }

    @Test
    public void testTopologicalSort3() throws Exception {
        clear();

        // 1->2
        // 1->3
        // 2->5
        // 3->4
        // 4->6
        // 5->6
        // 6->7
        // 6->8

        for (int i = 1; i <= 8; ++i) {
            graph.addNode(i, "v(" + i + ")");
        }

        // construction node
        Assertions.assertTrue(graph.addEdge(1, 2));

        Assertions.assertTrue(graph.addEdge(1, 3));

        Assertions.assertTrue(graph.addEdge(2, 5));
        Assertions.assertTrue(graph.addEdge(3, 4));

        Assertions.assertTrue(graph.addEdge(4, 6));

        Assertions.assertTrue(graph.addEdge(5, 6));

        Assertions.assertTrue(graph.addEdge(6, 7));
        Assertions.assertTrue(graph.addEdge(6, 8));

        Assertions.assertEquals(graph.getNodesCount(), 8);

        logger.info(Arrays.toString(graph.topologicalSort().toArray()));

        List<Integer> expectedList = new ArrayList<>();

        for (int i = 1; i <= 8; ++i) {
            expectedList.add(i);

            logger.info(i + " subsequentNodes : " + graph.getSubsequentNodes(i));
        }
        logger.info(6 + "  previousNodesb: " + graph.getPreviousNodes(6));
        Assertions.assertEquals(5, graph.getSubsequentNodes(2).toArray()[0]);

    }

    @Test
    public void testTopologicalSort4() {
        clear();
        try {
            graph.topologicalSort();
        } catch (Exception e) {
            Assertions.assertTrue(e.getMessage().contains("serious error: graph has cycle"));
        }
    }

}
