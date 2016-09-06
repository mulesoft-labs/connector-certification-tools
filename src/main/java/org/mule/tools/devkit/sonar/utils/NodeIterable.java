package org.mule.tools.devkit.sonar.utils;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class NodeIterable implements Iterable<Node> {

    private NodeList nodeList;

    public NodeIterable(NodeList nodeList) {
        this.nodeList = nodeList;
    }

    @Override
    public Iterator iterator() {
        return new Iterator() {

            private Integer cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor != nodeList.getLength() - 1;
            }

            @Override
            public Node next() {
                cursor++;
                if (cursor >= nodeList.getLength()) {
                    throw new NoSuchElementException();
                } else {
                    return nodeList.item(cursor);
                }
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}