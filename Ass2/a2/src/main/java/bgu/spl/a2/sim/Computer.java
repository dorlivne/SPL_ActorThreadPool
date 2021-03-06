package bgu.spl.a2.sim;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


public class Computer {

	String computerType;
	long failSig;
	long successSig;
	SuspendingMutex Key;

	public Computer(String computerType,long failSig,long successSig) {
		this.computerType = computerType;
		this.failSig = failSig;
		this.successSig = successSig;
		this.Key = new SuspendingMutex(this);
	}

	/**
	 * this method checks if the courses' grades fulfill the conditions
	 * @param courses
	 * 							courses that should be pass
	 * @param coursesGrades
	 * 							courses' grade
	 * @return a signature if couersesGrades grades meet the conditions
	 */
	public long checkAndSign(List<String> courses, Map<String, Integer> coursesGrades){

		AtomicBoolean Passed = new AtomicBoolean(true);
		AtomicBoolean HasCourse = new AtomicBoolean(true);
		for(int index = 0; index < courses.size(); index++){
			String X = courses.get(index);
			if(!coursesGrades.containsKey(X)){//dosen't contain
				HasCourse.set(false);
				break;
			}
			else if(coursesGrades.get(X)<56){
				Passed.set(false);
				break;
			}
		}
		if(Passed.get()  && HasCourse.get()){
			return successSig;
		}
		else
			return failSig;

	}

	public void Release(){
		this.Key.up();
	}

	public long getSuccessSig(){
		return successSig;
	}
}
