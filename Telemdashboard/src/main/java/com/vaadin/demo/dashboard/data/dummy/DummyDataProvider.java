package com.vaadin.demo.dashboard.data.dummy;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.vaadin.demo.dashboard.data.DataProvider;
import com.vaadin.demo.dashboard.domain.*;
import org.bson.Document;

import java.util.*;


/**
 * A dummy implementation for the backend API.
 */
public class DummyDataProvider implements DataProvider {

 

    /* List of countries and cities for them */

	private static Date lastDataUpdate;

	private static Collection<String> paramCollection;

	private static Multimap<Long, Transaction> transactions;

	private static Multimap<String, MeasuredValues> measuredValuesCollection;

	static List<Parameters> parameters = new ArrayList<Parameters>();


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
		measuredValuesCollection = getMeasuredValues();
	}

	/**
	 * Get a list of movies currently playing in theaters.
	 *
	 * @return a list of Movie objects
	 */
	@Override
	public Collection<String> getParameters() {
		return Collections.unmodifiableCollection(paramCollection);
	}

	/**
	 * Initialize the list of movies playing in theaters currently. Uses the
	 * Rotten Tomatoes API to get the list. The result is cached to a local file
	 * for 24h (daily limit of API calls is 10,000).
	 *
	 * @return
	 */
	private static Collection<String> loadCollectionData() {

		MongoClient mongoClient = getMongoClient();
		MongoDatabase database = mongoClient.getDatabase("eriksdashboard");
		MongoCollection<Document> collection = database.getCollection("sensors");
//		System.out.println("Aantal documents in parameters- collection: " + collection.count());
//		MongoCursor<Document> cursor = collection.distinct("t", String.class);

		final DistinctIterable<String> iterable = collection.distinct("t", String.class);

		MongoCursor<String> cursor = iterable.iterator();
//		Parameters para = new Parameters();
		List<String> parameters = new ArrayList<String>();

		try {
			while (cursor.hasNext()) {

//				Document doc = cursor.next();
//				para.set_id(doc.getDouble("ID").longValue());
//				para.setTitle(doc.getString("param"));
				final String title = cursor.next();
				System.out.println(title);
				parameters.add(title);


//				System.out.println("hoeveelste element? = " + parameters.size());

//				System.out.println("load parameters ID : " + para.get_id());
//				System.out.println("load parameters title : " + para.getTitle());
			}
		} finally {

			cursor.close();
		}
		mongoClient.close();


		return parameters;


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

	private Multimap<String, MeasuredValues> getMeasuredValues() {


		Multimap<String, MeasuredValues> result = MultimapBuilder.hashKeys()
				.arrayListValues().build();
		for (String para : paramCollection) {
			result.putAll(para, fetchParaMeasuredValues(para));
		}
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
	public Collection<MeasuredValues> fetchParaMeasuredValues(String par) {
		final MongoClient mongoClient = getMongoClient();
		final MongoDatabase database = mongoClient.getDatabase("eriksdashboard");
		MongoCollection<Document> collection = database.getCollection("sensors");

//		System.out.println("Aantal documents in measuredValues- collection: " + collection.count());
		MongoCursor<Document> cursor = collection.find(new BasicDBObject("t", par)).iterator();

		List<MeasuredValues> measuredValues = new ArrayList<MeasuredValues>();


		try {
			while (cursor.hasNext()) {

				MeasuredValues mesval = new MeasuredValues();

				Document doc = cursor.next();

				mesval.setTimestamp(doc.getDate("d"));
				mesval.setTitle(doc.getString("t"));
				mesval.setValue(doc.getDouble("v"));


				System.out.println("timestamp : " + mesval.getTimestamp());
				System.out.println("title : " + mesval.getTitle());
				System.out.println("value : " + mesval.getValue());
				measuredValues.add(mesval);
			}
		} finally {

			cursor.close();
		}
		mongoClient.close();


		return measuredValues;
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

	// TODO FIX THIS, DON'T STORE PASSWORDS IN CODE!!!!!
	private static MongoClient getMongoClient() {
		List<MongoCredential> credentials = new ArrayList<MongoCredential>();
		credentials.add(
				MongoCredential.createMongoCRCredential(
						"eriks_user",
						"eriksdashboard",
						"xVP3VibxPWE".toCharArray()
				)
		);
		List<ServerAddress> seeds = new ArrayList<ServerAddress>();
		seeds.add(new ServerAddress("localhost"));
		return new MongoClient(seeds, credentials);
	}


}
