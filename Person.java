//Mackenzie Jorgensen
import java.util.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class Person {
	private ArrayList<Person> parents; 
	private ArrayList<Person> children;
	private ArrayList<Person> spouses;
	private String name;
	
	public Person(String a) { //default constructor
		name = a;
		parents = new ArrayList<Person>();
		children = new ArrayList<Person>();
		spouses = new ArrayList<Person>();
	}
	public String getName() {
		return this.name;
	}
	public boolean isParent(Person x) { 
		for(Person i : x.parents) {
			if(this.getName().equals(i.getName()))
				return true;
		}
		return false;
	}
	public boolean isSibling(Person x) {
		boolean p1 = false;
		boolean p2 = false;
		if(this.parents != null && x.parents != null && this.parents.size()> 0 && x.parents.size() > 0) {
			if((this.parents.get(0).getName()).equals(x.parents.get(0).getName()) || (this.parents.get(0).getName()).equals(x.parents.get(1).getName()))
				p1 = true;
			if((this.parents.get(1).getName()).equals(x.parents.get(0).getName()) || (this.parents.get(1).getName()).equals(x.parents.get(1).getName()))
				p2 = true;
		}
		return p1 && p2;
	}
	public boolean ishalfSibling(Person x) { 
		if(!this.isSibling(x) && this.parents != null && x.parents != null && this.parents.size()> 0 && x.parents.size() > 0) {
			if((this.parents.get(0).getName()).equals(x.parents.get(0).getName()) || this.parents.get(0).getName().equals(x.parents.get(1).getName()) || (this.parents.get(1).getName()).equals(x.parents.get(0)) || (this.parents.get(1).getName()).equals(x.parents.get(1).getName()))
				return true;
		}
		return false;
	}
	public boolean isSpouse(Person x) {
		for(int i = 0; i < spouses.size(); i++) {
			if(spouses.get(i).equals(x))
				return true;
		}
		return false;
	}
	public boolean isAncestor(Person y) {
		if(y == null)
			return false;
		HashMap<String, Integer> ancestors = y.getAncestors();
		for(String s : ancestors.keySet()) {
			if(s.equals(this.getName()))
				return true;
		}
		return false;
	}
	public boolean isCousin(Person x) {
		HashMap<String, Integer> ca = this.CommonAncestor(x);
		return ca != null && !ca.isEmpty() && !(this.isAncestor(x)) && !(x.isAncestor(this));
		//
	}
	public boolean isUnrelated(Person y) { 
		boolean result = true;
		if(this.isAncestor(y) || y.isAncestor(this))
			result = false;
		if(this.isCousin(y))
			result = false;
		return result;
	}
	public ArrayList<String> getParents() {  
		ArrayList<String> a = new ArrayList<String>();
		for(Person i : this.parents)
			a.add(i.getName());
		Collections.sort(a);
		return a;
	}
	public HashMap<String, Integer> getAncestors() { 
		HashMap<String, Integer> ancestors = new HashMap<String, Integer>();
		this.getAncestors(ancestors, 0);
		return ancestors;
	}
	public void getAncestors(HashMap<String, Integer> ancestors, int level) {
		ancestors.put(this.getName(), level);
		if(this.parents != null) {
			for (Person p : this.parents) {
				p.getAncestors(ancestors, level + 1);
			}
		}
	}
	public static void birth(Person p1, Person p2, Person p3) { //called in the tree class
		p3.parents.add(p1); //sets kids' parents
		p3.parents.add(p2);
		p1.children.add(p1); //sets parents
		p2.children.add(p3);
	}
	public static void matrimony(Person p1, Person p2) { //called in the tree
		p1.spouses.add(p2);
		p2.spouses.add(p1);
	}
	public HashMap<String, Integer> CommonAncestor(Person p) {
		HashMap<String, Integer> anc1 = this.getAncestors();
		HashMap<String, Integer> anc2 = p.getAncestors();
		HashMap<String, Integer> commonAncestors = new HashMap<String, Integer>();
		for(String name : anc2.keySet()) {
			if(anc1.containsKey(name)) {
				commonAncestors.put(name,  anc1.get(name));
			}
		}
		return commonAncestors;
	}
}
