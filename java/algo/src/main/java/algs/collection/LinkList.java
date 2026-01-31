package algs.collection;

public class LinkList {
    private Link first;

    public static class Link {
        private final int data;
        private Link next;

        public Link(int data) {
            this.data = data;
        }

        public int getData() {
            return data;
        }

        public Link getNext() {
            return next;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder("Link{");
            builder.append("data=").append(data);
            builder.append("}");
            return builder.toString();
        }
    }

    public LinkList() {
        first = null;
    }

    public void insert(int data) {
        Link link = new Link(data);
        link.next = first;
        first = link;
    }

    public Link find(int key) {
        Link rc = null;
        for (Link current = first; null != current; current = current.next) {
            if (key == current.data) {
                rc = current;
                break;
            }
        }

        return rc;
    }

    public Link delete(int key) {
        Link previous = first;
        Link current = first;

        while (true) {
            if (null == current) {
                break;
            }

            if (key == current.data) {
                break;
            }

            previous = current;
            current = current.next;
        }

        if (null == current) {
            return null;
        }

        if (current == first) {
            first = first.next;
        } else {
            previous.next = current.next;
        }

        return current;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("List(first-->last)\n");
        for (Link link = first; null != link; link = link.next) {
            builder.append(link).append("\n");
        }

        return builder.toString();
    }
}