package algs.test;

import algs.collection.LinkList;

public class LinkListTest {

    public static void main(String[] args) {
        LinkList list = new LinkList();
        list.insert(22);
        list.insert(44);
        list.insert(66);
        list.insert(88);
        System.out.println(list);

        LinkList.Link f = list.find(44);
        if (null != f) {
            System.out.println("Found link with key " + f.getData());
        } else {
            System.out.println("Can't find link");
        }

        LinkList.Link d = list.delete(66);
        if (null != d) {
            System.out.println("Deleted link with key " + d.getData());
        } else {
            System.out.println("Can't delete link");
        }

        System.out.println(list);
    }
}