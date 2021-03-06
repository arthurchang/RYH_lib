package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

public class Lecture implements Serializable{
	private static final long serialVersionUID = 2110167116581440442L;

	private final int courseNum;
	private String activeQuestion;
	private AttendanceState attendanceState;
	private QuizState quizState;
	private LinkedHashMap<String, String> quizAnswers;
	private LinkedHashMap<String, Integer> quizResults;
	private LinkedHashSet<String> presentStudents;
	private LinkedHashSet<String> attendance;
	private ArrayList<String> questions;
	private ArrayList<String> instructorShared;
	private ArrayList<String> studentShared;
	private ArrayList<String> pendingStudentShared;
	
	public Lecture(int courseNum) {
		this.courseNum = courseNum;
		this.presentStudents = new LinkedHashSet<String>();
		this.activeQuestion = null;
		this.attendanceState = AttendanceState.NOT_TAKEN;
		this.quizState = QuizState.NOT_TAKEN;
		this.questions = new ArrayList<String>();
		this.instructorShared = new ArrayList<String>();
		this.studentShared = new ArrayList<String>();
		this.pendingStudentShared = new ArrayList<String>();
	}
	
	public synchronized int getCourseNum() {
		return courseNum;
	}
	
	public synchronized boolean startAttendance() {
		if (attendanceState == AttendanceState.NOT_TAKEN) {
			attendanceState = AttendanceState.BEING_TAKEN;
			return true;
		}
		return false;
	}
	
	public synchronized boolean stopAttendance() {
		if (attendanceState == AttendanceState.BEING_TAKEN) {
			attendanceState = AttendanceState.TAKEN;
			return true;
		}
		return false;
	}
	
	public synchronized AttendanceState getAttendanceState() {
		return attendanceState;
	}
	
	public synchronized void joinLecture(String username) {
		if (attendanceState == AttendanceState.BEING_TAKEN) {
			attendance.add(username);
		}
		presentStudents.add(username);
	}
	
	public synchronized void leaveLecture(String username) {
		presentStudents.remove(username);
		questions.remove(username);
	}
	
	public synchronized boolean startQuiz() {
		if (quizState == QuizState.NOT_TAKEN) {
			quizState = QuizState.BEING_TAKEN;
			quizAnswers = new LinkedHashMap<String, String>();
			return true;
		}
		return false;
	}
	
	public synchronized boolean stopQuiz() {
		if (quizState == QuizState.BEING_TAKEN) {
			quizState = QuizState.TAKEN;
			quizResults = new LinkedHashMap<String, Integer>();
			quizResults.put("A", 0);
			quizResults.put("B", 0);
			quizResults.put("C", 0);
			quizResults.put("D", 0);
			quizResults.put("E", 0);
			for (Iterator<String> it = quizAnswers.values().iterator();it.hasNext();){
				String answer = it.next();
				quizResults.put(answer, quizResults.get(answer) + 1);
			}
			return true;
		}
		return false;
	}
	
	public synchronized boolean hideQuiz() {
		if (quizState == QuizState.TAKEN) {
			quizState = QuizState.HIDDEN;
			return true;
		}
		return false;
	}
	
	public synchronized LinkedHashMap<String, Integer> getQuizResults() {
		if (quizState == QuizState.TAKEN ||
			quizState == QuizState.HIDDEN) {
			return quizResults;
		}
		return null;
	}
	
	public synchronized LinkedHashMap<String, String> getQuizAnswers() {
		if (quizState == QuizState.TAKEN ||
			quizState == QuizState.HIDDEN) {
			return quizAnswers;
		}
		return null;
	}
	
	public synchronized QuizState getQuizState() {
		return quizState;
	}
	
	public synchronized void answerQuiz(String username, String answer) {
		if (quizState == QuizState.BEING_TAKEN) {
			quizAnswers.put(username, answer);
		}
	}
	
	public synchronized void requestQuestion(String username) {
		if (!questions.contains(username)) {
			questions.add(username);
		}
	}
	
	public synchronized void dismissQuestion(String username) {
		if (questions.contains(username)) {
			questions.remove(username);
		}
	}
	
	public synchronized void answerQuestion(String username) {
		if (questions.contains(username)) {
			activeQuestion = username;
			dismissQuestion(username);
		}
	}
	
	public synchronized void endQuestion() {
		activeQuestion = null;
	}
	
	public synchronized String getActiveQuestion() {
		return activeQuestion;
	}
	
	public synchronized LinkedHashSet<String> getAttendance() {
		if (attendanceState == AttendanceState.TAKEN) {
			return attendance;
		}
		return null;
	}
	
	public synchronized ArrayList<String> getInstructorShared() {
		return instructorShared;
	}
	
	public synchronized ArrayList<String> getStudentShared() {
		return studentShared;
	}
	
	public synchronized ArrayList<String> getPendingStudentShared() {
		return pendingStudentShared;
	}
	
	public synchronized void deleteStudentShared(String note) {
		if (studentShared.contains(note)) {
			studentShared.remove(note);
		}
	}
	
	public synchronized void denyStudentShared(String note) {
		if (pendingStudentShared.contains(note)) {
			pendingStudentShared.remove(note);
		}
	}}
