package bgu.spl.a2.sim.privateStates;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;

import bgu.spl.a2.PrivateState;

/**
 * this class describe course's private state
 */
public class CoursePrivateState extends PrivateState{

	private Integer availableSpots;
	private Integer registered;
	private List<String> regStudents;
	private List<String> prequisites;

	/**
 	 * Implementors note: you may not add other constructors to this class nor
	 * you allowed to add any other parameter to this constructor - changing
	 * this may cause automatic tests to fail..
	 */
	public CoursePrivateState() {
		this.availableSpots = 0;
		this.registered = 0;
		this.regStudents = new LinkedList<>();
		this.prequisites = new LinkedList<>();
		//TODO: replace method body with real implementation
		//throw new UnsupportedOperationException("Not Implemented Yet.");
	}

	public Integer getAvailableSpots() {
		return availableSpots;
	}

	public Integer getRegistered() {
		return registered;
	}

	public List<String> getRegStudents() {
		return regStudents;
	}

	public List<String> getPrequisites() {
		return prequisites;
	}

	public void setPrequisites(List<String> prequisites){ this.prequisites = prequisites; }

	public void setAvailableSpots(int spaces){ this.availableSpots = spaces; }

	public void setRegistered(int registers){ this.registered = registers; }

	public void setRegStudents(String regStudents){ this.regStudents.add(regStudents); }

	public void updateParametrs(int Students,String StudentID){
		if(!regStudents.contains(StudentID)) {
			this.registered += Students;
			this.availableSpots -= Students;
		}
	}

	public void RemoveStudent(String studentID){
		if(regStudents.contains(studentID)){
			this.regStudents.remove(studentID);
			updateParametrs(-1,studentID);
		}
	}

	public boolean HasReqCourses(List<String> preCourses, HashMap<String,Integer> grades){//checks if has req courses inorder to register to a course

		AtomicBoolean HasCourse = new AtomicBoolean(true);
		if(preCourses.size()>0) {
			String X =  preCourses.get(0);
			for (int index = 0; index < preCourses.size(); index++) {
				if (!grades.containsKey(X)) {//dosen't contain
					HasCourse.set(false);
					break;
				}
				else if (preCourses.indexOf(X) < preCourses.size() - 1)
					X = preCourses.get(index + 1);
			}
		}
		if(HasCourse.toString() == "true"){
			return true;
		}
		else
			return false;

	}

	public void AddSpaces(int spaces){
		if (this.getAvailableSpots() != -1)
			this.setAvailableSpots((spaces + this.availableSpots));//current + added spaces

	}
}
