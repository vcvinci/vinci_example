package com.vcvinci.algorithm;

/**
 * @author vinci
 * @Description: single list revert
 * @date 2019-03-2510:07
 */
public class SingleListRevert {

    // 1 -> 2 -> 3 -> 4 -> 5   =>  54321
    public static void main(String[] args) {
        ListNode first = new ListNode(1);
        ListNode second = new ListNode(2);
        ListNode third = new ListNode(3);
        ListNode four = new ListNode(4);
        ListNode five = new ListNode(5);
        first.next = second;
        second.next = third;
        third.next = four;
        four.next = five;

        ListNode newHead = reverseList(first);
        while (newHead != null) {
            System.out.println(newHead.val);
            newHead = newHead.next;
        }
    }
    public static ListNode reverseList(ListNode head) {
        ListNode newHead = null;
        ListNode cur = head;
        ListNode pre = null;
        while (cur != null) {
            ListNode next = cur.next;
            if (next == null)  {
                newHead = cur;
            }
            cur.next = pre;
            pre = cur;
            cur = next;
        }
        return newHead;
    }


     public static class ListNode {
         int val;
         ListNode next;
         ListNode(int x) { val = x; }
     }
}
