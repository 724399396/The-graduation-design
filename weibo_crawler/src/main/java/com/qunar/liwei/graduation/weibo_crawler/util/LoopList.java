package com.qunar.liwei.graduation.weibo_crawler.util;


public class LoopList<E> {
	Node head;	// 对头
	Node near;  // 队尾
	Node current; // 当前节点
	private class Node {
		E element;
		Node next;
	}	
	public void add(E e) {
		Node ele = new Node();
		ele.element = e;
		if (head == null) {
			head = ele;
			current = ele;
		}
		if (near == null) {
			near = ele;
		}
		near.next = ele;
		ele.next = head;
		near = ele;
	}
	public E next() {
		E result = current.element;
		current = current.next;
		return result;
	}
	
	public static void main(String[] args) {
		LoopList<Integer> l = new LoopList<>();
		l.add(1);	l.add(2);	l.add(3);
		System.out.println(l.head.element);
		System.out.println(l.head.next.element);
		System.out.println(l.head.next.next.element);
		System.out.println(l.head.next.next.next.element);
	}
}
