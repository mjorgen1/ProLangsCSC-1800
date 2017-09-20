//Mackenzie Jorgensen
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Jorgensen {
	private ArrayList<Person> people;
	//reads in text input
	public static void main(String[] args) {
		Tree tree = new Tree();
		tree.readInput();
	}
	public Tree() {
		this.people = new ArrayList<Person>();
	}
	public void readInput() {
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		String line;
		//remember to change program name to my last name
		try {
			while((line = input.readLine()) != null && line.length() > 0) { 
				if(line.charAt(0) == 'E')
					this.readEvent(line);
				else if(line.charAt(0) == 'X') {
					System.out.println(line);
					this.readRelated(line);
				}
				else if(line.charAt(0) == 'W') {
					System.out.println(line);
					this.readWho(line);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void readEvent(String input) {
		String[] parameter = input.split(" ");
		Person p1 = this.findPerson(parameter[1]);
		Person p2 = this.findPerson(parameter[2]);
		if(parameter.length > 3) {
			Person child = this.findPerson(parameter[3]);
			Person.birth(p1, p2, child);
		}
		else {
			Person.matrimony(p1, p2);
		}
	}
	public void readRelated(String input) {
		String[] parameter = input.split(" ");
		Person p1 = this.findPerson(parameter[1]);
		String relation = parameter[2];
		Person p2 = this.findPerson(parameter[3]);
		if(relation.equals("sibling")) {
			boolean sibling = p1.isSibling(p2);
			if(sibling == true)
				System.out.println("Yes" + "\n");
			else
				System.out.println("No" + "\n");
		}
		else if(relation.equals("cousin")) {
			boolean cousin = p1.isCousin(p2);
			if(cousin)
				System.out.println("Yes" + "\n");
			else
				System.out.println("No" + "\n");
		}
		else if(relation.equals("parent")) {
			boolean parent = p1.isParent(p2);
			if(parent)
				System.out.println("Yes" + "\n");
			else
				System.out.println("No" + "\n");
		}
		else if(relation.equals("half-sibling")) {
			boolean halfSib = p1.ishalfSibling(p2);
			if(halfSib)
				System.out.println("Yes" + "\n");
			else
				System.out.println("No" + "\n");
		}
		else if(relation.equals("ancestor")) {
			boolean ancestor = p1.isAncestor(p2);
			if(ancestor)
				System.out.println("Yes" + "\n");
			else
				System.out.println("No" + "\n");
		}
		else if(relation.equals("unrelated")) {
			boolean unrelated = p1.isUnrelated(p2);
			if(unrelated)
				System.out.println("Yes" + "\n");
			else
				System.out.println("No" + "\n");
		}
	}
	public void readWho(String input) {
		String[] parameter = input.split(" ");
		String relation = parameter[1];
		Person p1 = this.findPerson(parameter[2]);
		if(relation.equals("sibling")) {
			ArrayList<String> siblings = new ArrayList<String>();
			for(Person i : this.people) {
				if(i.isSibling(p1) && p1.getName() != i.getName())
					siblings.add(i.getName());
			}
			Collections.sort(siblings);
			for(int i = 0; i < siblings.size(); i++) {
				System.out.println(siblings.get(i));
			}
			System.out.println("");
		}
		else if(relation.equals("cousin")) {
			ArrayList<String> cousins = new ArrayList<String>();
			for(Person i : people) {
				if(i.isCousin(p1))
					cousins.add(i.getName());
			}
			Collections.sort(cousins);
			for(int i = 0; i < cousins.size(); i++) {
				System.out.println(cousins.get(i));
			}
			System.out.println("");
		}
		else if(relation.equals("parent")) {
			ArrayList<String> parents = p1.getParents();
			for(String i : parents) {
				System.out.println(i);
			}
			System.out.println("");
		}
		else if(relation.equals("half-sibling")) {
			ArrayList<String> halfSibs = new ArrayList<String>();
			for(Person i : people) {
				if(i.ishalfSibling(p1))
					halfSibs.add(i.getName());
			}
			Collections.sort(halfSibs);
			for(int i = 0; i < halfSibs.size(); i++) {
				System.out.println(halfSibs.get(i));
			}
			System.out.println("");
		}
		else if(relation.equals("ancestor")) {
			HashMap<String, Integer> ancestors = p1.getAncestors();
			ArrayList<String> ancArr = new ArrayList<String>(ancestors.keySet());
			Collections.sort(ancArr);
			for(String value : ancArr) {
				if(! value.equals(p1.getName()))
					System.out.println(value);
			}
			System.out.println("");
		}
		else if(relation.equals("unrelated")) {
			ArrayList<String> unrel = new ArrayList<String>();
			for(Person i : people) {
				if(i.isUnrelated(p1))
					unrel.add(i.getName());
			}
			Collections.sort(unrel);
			for(int i = 0; i < unrel.size(); i++) {
				System.out.println(unrel.get(i));
			}
			System.out.println("");
		}
	}
	public void addPerson(Person p) {
		this.people.add(p);
	}
	public Person findPerson(String name) {
		for (Person p : this.people) {
			if(p.getName().equals(name)) {
				return p;
			}
		}
		Person p = new Person(name);
		this.addPerson(p);
		return p;
	}
	
}
