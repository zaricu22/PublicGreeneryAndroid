package com.example.myapplication.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.Date;
import java.util.List;
import androidx.room.Update;

// Interfejs za komunikaciju sa bazom
@Dao
public interface DatabaseDAO {
    @Query("SELECT * FROM user")
    List<User> getAllUsers();

    @Query("SELECT * FROM user WHERE username = :username AND password = :password")
    LiveData<User> login(String username, String password);

    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    LiveData<User> findByName(String first, String last);

    @Query("SELECT * FROM event")
    List<Event> getAllEvents();

    @Query("SELECT * FROM event WHERE id = :id")
    Event getEventById(Integer id);

    @Query("SELECT * FROM event WHERE date = :date")
    Event getEventByDate(Date date);

    @Query("SELECT * FROM job")
    List<Job> getAllJobs();

    @Query("SELECT * FROM job WHERE id = :id")
    Job getJobById(Integer id);

    @Query("SELECT * FROM job WHERE status = :status AND pocetak BETWEEN :date1 AND :date2")
    List<Job> getJobs(String status, Date date1, Date date2);

    @Query("SELECT * FROM job WHERE pocetak BETWEEN :date1 AND :date2")
    List<Job> getJobsByMesec(Date date1, Date date2);

    @Insert
    void insertUsers(User... users);

    @Delete
    void deleteUsers(User... users);

    @Update
    void updateUsers(User... users);

    @Insert
    void insertEvents(Event... events);

    @Delete
    void deleteEvents(Event... events);

    @Update
    void updateEvents(Event... events);

    @Insert
    void insertJobs(Job... jobs);

    @Delete
    void deleteJobs(Job... jobs);

    @Update
    void updateJobs(Job... jobs);
}

