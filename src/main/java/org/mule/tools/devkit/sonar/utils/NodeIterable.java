package org.mule.tools.devkit.sonar.utils;

import com.google.common.base.Function;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import static org.w3c.dom.Node.ELEMENT_NODE;

public class NodeIterable implements Iterable<Node> {

    private NodeList nodeList;

    public NodeIterable(NodeList nodeList) {
        this.nodeList = nodeList;
    }

    @Override
    public Iterator iterator() {
        return new Iterator() {

            Integer cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor != nodeList.getLength() - 1;
            }

            @Override
            public Node next() {
                cursor = new Integer(cursor + 1);
                if (cursor >= nodeList.getLength()) {
                    throw new NoSuchElementException();
                } else
                    return nodeList.item(cursor);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();

            }
        };
    }

    public static Function<Node, String> getVersion = new Function<Node, String>() {

        @Override
        public String apply(@Nullable Node node) {
            if (node.getNodeType() == ELEMENT_NODE) {
                return node.getFirstChild()
                        .getTextContent();
            }
            return null;
        }
    };
}