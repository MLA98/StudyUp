package edu.studyup.serviceImpl;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.studyup.entity.Event;
import edu.studyup.entity.Location;
import edu.studyup.entity.Student;
import edu.studyup.util.DataStorage;
import edu.studyup.util.StudyUpException;

class EventServiceImplTest {

	EventServiceImpl eventServiceImpl;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		eventServiceImpl = new EventServiceImpl();
		//Create Student
		Student student = new Student();
		student.setFirstName("John");
		student.setLastName("Doe");
		student.setEmail("JohnDoe@email.com");
		student.setId(1);
		
		//Create Event1
		Event event = new Event();
		event.setEventID(1);
		event.setDate(new Date());
		event.setName("Event 1");
		Location location = new Location(-122, 37);
		event.setLocation(location);
		List<Student> eventStudents = new ArrayList<>();
		eventStudents.add(student);
		event.setStudents(eventStudents);
		
		DataStorage.eventData.put(event.getEventID(), event);
	}
		

	@AfterEach
	void tearDown() throws Exception {
		DataStorage.eventData.clear();
	}

	@Test
	void testUpdateEventName_GoodCase() throws StudyUpException {
		int eventID = 1;
		eventServiceImpl.updateEventName(eventID, "Renamed Event 1");
		assertEquals("Renamed Event 1", DataStorage.eventData.get(eventID).getName());
	}
	
	@Test
	void testUpdateEvent_WrongEventID_badCase() {
		int eventID = 3;
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.updateEventName(eventID, "Renamed Event 3");
		  });
	}
	
	// expected a throw with students size larger than 2
	@Test
	void testAddstudent_size_BadCase() throws StudyUpException {
		int eventID = 1;
		Student han = new Student();
		han.setFirstName("Meimei");
		han.setLastName("Han");
		han.setEmail("hm@email.com");
		han.setId(2);
		Student li = new Student();
		han.setFirstName("Bai");
		han.setLastName("Li");
		han.setEmail("lb@email.com");
		han.setId(3);
		eventServiceImpl.addStudentToEvent(han, eventID);
		Assertions.assertThrows(StudyUpException.class, ()-> {
			eventServiceImpl.addStudentToEvent(li, eventID);
		});
	}

	
	@Test
	// don't expect a throw with students size equal to 2
	void testAddstudent_size_GoodCase() throws StudyUpException {
		int eventID = 1;
		Student han = new Student();
		han.setFirstName("Meimei");
		han.setLastName("Han");
		han.setEmail("hm@email.com");
		han.setId(2);
		//eventServiceImpl.addStudentToEvent(han,eventID);
		//eventServiceImpl.addStudentToEvent(han,eventID);
		System.out.println(DataStorage.eventData.get(eventID).getStudents().size());
		Assertions.assertDoesNotThrow(()-> {
			eventServiceImpl.addStudentToEvent(han, eventID);
		});
	}
	
		//Test if it throws exception if event == null
	@Test
	void testAddStudent_eventEqualnull() {
		int eventID = 1;
		Student han = new Student();
		han.setFirstName("Meimei");
		han.setLastName("Han");
		han.setEmail("hm@email.com");
		han.setId(2);
		eventServiceImpl.deleteEvent(1);
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.addStudentToEvent(han,eventID);
		  });
	}
	//Test if it still work if presentStudent == null
	@Test
	void testAddStudent_presentStudentEqualnull() {
		Student han = new Student();
		han.setFirstName("Meimei");
		han.setLastName("Han");
		han.setEmail("hm@email.com");
		han.setId(2);
		Event event = DataStorage.eventData.get(1);
		event.setStudents(null);
		Assertions.assertDoesNotThrow(() -> {
			eventServiceImpl.addStudentToEvent(han,1);
		  });
	}
	
	
	
	
	@Test
	// test if delete is successful
	void testDeleteEvents_GoodCase() {
		int eventID = 1;
		eventServiceImpl.deleteEvent(eventID);
		assertTrue("The event is not deleted", DataStorage.eventData.get(eventID) == null);
	}
	
	@Test
	// test if pastEvent is retrieved as expected
	void testPastEvents_GoodCase() {
		Event event = DataStorage.eventData.get(1);
		Date myDate = new Date(421412412341241234L);
		event.setDate(myDate);
		int size = eventServiceImpl.getPastEvents().size();
		assert(size == 0);
	}
	
	@Test
	void testUpdateEventName_NameLengthGreaterThanTwenty_Not_expect_exception() {
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.updateEventName(1, "This name is too long for the function");
		  });
	}
	/*@Test
	void testUpdateEventName_NameLengthGreaterThanTwenty_Not_expect_exception() {
		Assertions.assertDoesNotThrow(() -> {
			eventServiceImpl.updateEventName(1, "This name is too long for the function");
		  });
	}*/
	//Test if it throws exception when the length of the name is 20
	@Test
	void testUpdateEventName_NameLengthEqualsToTwenty_Not_Expect_Exception() {
		Assertions.assertDoesNotThrow(() -> eventServiceImpl.updateEventName(1, "This is twenty words"));		
	}
	
	/*@Test 
	void testUpdateEventName_NameLength_BadCase_NameLengthEqualsToTwenty() {
		Assertions.assertThrows(StudyUpException.class, () -> {eventServiceImpl.updateEventName(1, "This is twenty words");});
	}*/
	//Test if it throws exception when the length of the name is 20		
	@Test
	void testUpdateEventName_NameLengthLessThanTwenty_Not_Expect_Exception() {
		Assertions.assertDoesNotThrow(() -> eventServiceImpl.updateEventName(1, "This is ten words"));		
	}
		
	/*@Test
	void testUpdateEventName_NameLength_BadCase_NameLengthLessThanTwenty() {
		Assertions.assertThrows(StudyUpException.class, () -> {eventServiceImpl.updateEventName(1, "This is ten words");});
	}*/
		
	//test if future event is active.	
	@Test
	void testGetActiveEvent_future_Not_expect_exception() {
		Date myDate = new Date(421412412341241234L);
		Event event = DataStorage.eventData.get(1);
		event.setDate(myDate);	
		List<Event> activeEvents = eventServiceImpl.getActiveEvents();
		assert(activeEvents.size() == 1);
	}
	//test if past event is active
	@Test
	void testGetActiveEvent_past_Not_expect_exception() {
		Date myDate = new Date(2L);
		Event event = DataStorage.eventData.get(1);
		event.setDate(myDate);	
		List<Event> activeEvents = eventServiceImpl.getActiveEvents();
		assert(activeEvents.size() == 0);
	}
	
	
}
