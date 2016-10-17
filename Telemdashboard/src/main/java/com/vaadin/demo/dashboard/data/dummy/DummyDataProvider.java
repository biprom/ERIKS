package com.vaadin.demo.dashboard.data.dummy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bson.Document;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.vaadin.demo.dashboard.data.DataProvider;
import com.vaadin.demo.dashboard.domain.DashboardNotification;
import com.vaadin.demo.dashboard.domain.MeasuredValues;
import com.vaadin.demo.dashboard.domain.Movie;
import com.vaadin.demo.dashboard.domain.MovieRevenue;
import com.vaadin.demo.dashboard.domain.Parameters;
import com.vaadin.demo.dashboard.domain.Transaction;
import com.vaadin.demo.dashboard.domain.User;


/**
 * A dummy implementation for the backend API.
 */
public class DummyDataProvider implements DataProvider {

 

    /* List of countries and cities for them */
 
    private static Date lastDataUpdate;
    private static Collection<Parameters> paramCollection;
    private static Multimap<Long, Transaction> transactions;
    private static Multimap<Long, MeasuredValues> measuredValuesCollection;
    static List<Parameters> parameters = new ArrayList<Parameters>();
    static List<MeasuredValues> measuredValues = new ArrayList<MeasuredValues>();

  

    private final Collection<DashboardNotification> notifications = DummyDataGenerator
            .randomNotifications();

    /**
     * Initialize the data for this application.
     */
    public DummyDataProvider() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        if (lastDataUpdate == null || lastDataUpdate.before(cal.getTime())) {
            refreshStaticData();
            lastDataUpdate = new Date();
        }
    }

    private void refreshStaticData() {
      
        paramCollection = loadCollectionData();
        measuredValuesCollection = countMeasuredValues();
    }

    /**
     * Get a list of movies currently playing in theaters.
     *
     * @return a list of Movie objects
     */
    @Override
    public Collection<Parameters> getParameters() {
        return Collections.unmodifiableCollection(paramCollection);
    }

    /**
     * Initialize the list of movies playing in theaters currently. Uses the
     * Rotten Tomatoes API to get the list. The result is cached to a local file
     * for 24h (daily limit of API calls is 10,000).
     *
     * @return
     */
    private static Collection<Parameters> loadCollectionData() {

        
    	
    	
		MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://127.0.0.1:27017"));
    	MongoDatabase database = mongoClient.getDatabase("eriks");
    	MongoCollection<Document> collection = database.getCollection("parameters");
    	System.out.println("Aantal documents in parameters- collection: "+collection.count());
		MongoCursor<Document> cursor = collection.find().iterator();
    	
    	Parameters para = new Parameters();
    		
    		
   	try {
    	    while (cursor.hasNext()) {
    	    	
    	    	Document doc = cursor.next();
    	    	para.set_id(doc.getDouble("ID").longValue());
    	    	para.setTitle(doc.getString("param"));
    	    	parameters.add(para);
    	  
    	    	System.out.println("hoeveelste element? = "+parameters.size());
    	    	System.out.println("load parameters ID : "+ para.get_id());
    	    	System.out.println("load parameters title : "+ para.getTitle());
    	    	
    	    	
    	    }
    	} finally {
    	
    	    cursor.close();
    	}
    	mongoClient.close();
    	
    	
        
		return parameters ;
		
		
    }

 

    @Override
    public User authenticate(String userName, String password) {
        User user = new User();
        user.setFirstName(DummyDataGenerator.randomFirstName());
        user.setLastName(DummyDataGenerator.randomLastName());
        user.setRole("admin");
        String email = user.getFirstName().toLowerCase() + "."
                + user.getLastName().toLowerCase() + "@"
                + DummyDataGenerator.randomCompanyName().toLowerCase() + ".com";
        user.setEmail(email.replaceAll(" ", ""));
        user.setLocation(DummyDataGenerator.randomWord(5, true));
        user.setBio("Quis aute iure reprehenderit in voluptate velit esse."
                + "Cras mattis iudicium purus sit amet fermentum.");
        return user;
    }

    @Override
    public Collection<Transaction> getRecentTransactions(int count) {
        List<Transaction> orderedTransactions = Lists.newArrayList(transactions
                .values());
        Collections.sort(orderedTransactions, new Comparator<Transaction>() {
            @Override
            public int compare(Transaction o1, Transaction o2) {
                return o2.getTime().compareTo(o1.getTime());
            }
        });
        return orderedTransactions.subList(0,
                Math.min(count, transactions.values().size() - 1));
    }

    private Multimap<Long, MeasuredValues> countMeasuredValues() {
        Multimap<Long, MeasuredValues> result = MultimapBuilder.hashKeys()
                .arrayListValues().build();
        for (Parameters para : paramCollection) {
            result.putAll(para.get_id(), countParaMeasuredValues(para));
        }
        return result;
    }

    private Collection<MeasuredValues> countParaMeasuredValues(Parameters para) {
        Map<Date, Double> measuredValue = new HashMap<Date, Double>();
        
        Collection<MeasuredValues> result = new ArrayList<MeasuredValues>();


        return result;
    }

   
   

    @Override
    public int getUnreadNotificationsCount() {
        Predicate<DashboardNotification> unreadPredicate = new Predicate<DashboardNotification>() {
            @Override
            public boolean apply(DashboardNotification input) {
                return !input.isRead();
            }
        };
        return Collections2.filter(notifications, unreadPredicate).size();
    }

    @Override
    public Collection<DashboardNotification> getNotifications() {
        for (DashboardNotification notification : notifications) {
            notification.setRead(true);
        }
        return Collections.unmodifiableCollection(notifications);
    }

    @Override
    public double getTotalSum() {
        double result = 0;
        for (Transaction transaction : transactions.values()) {
            result += transaction.getPrice();
        }
        return result;
    }

   

    @Override
    public Collection<Transaction> getTransactionsBetween(final Date startDate,
            final Date endDate) {
        return Collections2.filter(transactions.values(),
                new Predicate<Transaction>() {
                    @Override
                    public boolean apply(Transaction input) {
                        return !input.getTime().before(startDate)
                                && !input.getTime().after(endDate);
                    }
                });
    }

	@Override
	public Collection<MeasuredValues> getDailyRevenuesByMovie(String par) {

		MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://127.0.0.1:27017"));
    	MongoDatabase database = mongoClient.getDatabase("eriks");
    	MongoCollection<Document> collection = database.getCollection("measuredValues");
    	System.out.println("Aantal documents in measuredValues- collection: "+collection.count());
    	MongoCursor<Document> cursor = collection.find().iterator();
    	
    	MeasuredValues mesval = new MeasuredValues();
    		
    		
   	try {
    	    while (cursor.hasNext()) {
    	    	
    	    	Document doc = cursor.next();
    	    	
    	    	mesval.setTimestamp(doc.getDate("timestamp"));
    	    	mesval.setTitle(doc.getString("title"));
    	    	mesval.setValue(doc.getDouble("value"));
    	    	
    	    	System.out.println("vergelijend parmaeter : "+ par);
    	    	
    	    	
    	    	if(doc.getString("title") == par){
    	    		
    	    		System.out.println("timestamp : "+mesval.getTimestamp());
        	    	System.out.println("title : "+mesval.getTitle());
        	    	System.out.println("value : "+mesval.getValue());
    	    		measuredValues.add(mesval);
    	    	}
    	    	
    	    	
    	  
    	    	measuredValues.add(mesval);
    	    }
    	} finally {
    	
    	    cursor.close();
    	}
    	mongoClient.close();
    	
        
		return measuredValues ;
	}

	@Override
	public Collection<MovieRevenue> getTotalMovieRevenues() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Movie getMovie(long movieId) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
