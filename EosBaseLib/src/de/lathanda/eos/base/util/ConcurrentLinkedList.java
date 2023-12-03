package de.lathanda.eos.base.util;

import java.util.Iterator;

/**
 * Eine minimale Liste mit grundlegender Synchronization.
 * 
 * Lesende Operationen werden nie blockiert.
 * Schreibende werden synchroniziert.
 * 
 * Die Liste ist extrem einfach gehalten, sodass sicher gestellt ist, 
 * dass lesende Zugriffe auch während einer Änderung möglich sind.
 * Allerdings können lesende Zugriffe inkonsistent sein. 
 * Die Liste A,B,C,D,E
 * wird als Schleife ausgegeben.
 * Ein anderer Thread löscht B und D.
 * Es kann nun die Ausgabe 
 * A,B,C,E entstehen.
 * Obwohl B zuerst gelöscht wurde war die Ausgabe schon bei C.
 * Dann wird B und D gelöscht wodurch D nicht mehr ausgegeben wird.
 * 
 * Die ausgegebene Liste ist insofern problematisch, weil es diese Liste so nie gab.
 * 
 * @author Peter (Lathanda) Schneider
 */
public class ConcurrentLinkedList<T>  implements Iterable<T> {

	private Node<T> root;
	private Node<T> last;
	private int count;
	public ConcurrentLinkedList() {
		this.root = null;
		this.last = null;
		count = 0;
	}

	public synchronized void add(T t) {
		if (last == null) {
			root = new Node<T>(t);
			last = root;
		} else {
			last.next = new Node<T>(t);
			last = last.next;
		}
		count++;
	}
	public synchronized void addFront(T t) {
		if (root == null) {
			root = new Node<T>(t);
			last = root;
		} else {
			root = new Node<T>(t, root.next);
		}
	}
	public synchronized void remove(T t) {
		if (root == null) {
			return;
		} else if (root.t == t) {
			if (root == last) {
				last = null;
				root = null;
			} else {
				root = root.next;
			}
		} else  {
			Node<T> prev = root;
			Node<T> act = root.next;
			while (act != null) {
				if (act.t == t) {
					prev.next = act.next;
					if (act == last) {
						last = prev;
					}
					count--;
					return;
				}
				prev = act;
				act = act.next;
			}
		}
		count--;
	}
	public int getLength() {
		return count;
	}
	@Override
	public Iterator<T> iterator() {
		return new TIterator<T>(root);
	}

	public synchronized void clear() {
		root = null;
		last = null;
	}

	private static class Node<T> {
		private volatile Node<T> next;
		private T t;

		public Node(T t) {
			this.t = t;
		}
		public Node(T t, Node<T> next) {
			this.t= t;
			this.next = next;
		}
	}

	private static class TIterator<T> implements Iterator<T> {
		private Node<T> index;

		public TIterator(Node<T> index) {
			this.index = index;
		}

		@Override
		public boolean hasNext() {
			return index != null;
		}

		@Override
		public T next() {
			T next = index.t;
			index = index.next;
			return next;
		}
	}
}
