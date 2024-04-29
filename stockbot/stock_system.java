import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import Project_part1.mainSystem;

public class stock_system<E> {

	// Starting variables and objects as needed

	// Date formats
	// Source: stackoverflow
	String format1 = "M/dd/yyyy", format2 = "MM/dd/yyyy", format3 = "M/d/yyyy", format4 = "M/dd/yyyy",
			format5 = "yyy-MM-dd";
	DateTimeFormatter formDate = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	String fileNameIn, fileNameOut, line;

	Scanner scan = new Scanner(System.in);
	private double balance;
	private int stock;
	private static final int N = 14;

	private StringBuilder str = new StringBuilder();
	private BufferedWriter log;

	private ArrayList<Double> rsiValues = new ArrayList<>();
	private ArrayList<Double> open = new ArrayList<>();
	private ArrayList<Double> high = new ArrayList<>();
	private ArrayList<Double> low = new ArrayList<>();
	private ArrayList<Double> close = new ArrayList<>();
	private ArrayList<Double> adj_close = new ArrayList<>();
	private ArrayList<Double> vol = new ArrayList<>();
	private ArrayList<LocalDate> date = new ArrayList<>();
	private ArrayList<Double> ma = new ArrayList<>();

	mainSystem use;

	// Constructor to get everything going
	public stock_system() {

//		try {
//			balance = 0;
//			stock = 0;
//			use = new mainSystem();
//			System.out.println("Enter file name:");
//			fileNameIn = use.fixName(use.askName());
//			System.out.println("Enter the file name to export the RSI values");
//			fileNameOut = use.fixName(use.askName());
//
//			BufferedReader br = use.readFile(fileNameIn);
//			BufferedWriter write = use.writeFile(fileNameOut);
//			boolean header = false;
//
//			// Store respective values
//			while ((line = br.readLine()) != null) {
//				String[] list = line.split(",");
//				if (header == false) {
//					header = true;
//					continue;
//				}
//
//				date.add(add_Date(list[0]));
//				open.add(Double.parseDouble(list[1].trim()));
//				high.add(Double.parseDouble(list[2].trim()));
//				low.add(Double.parseDouble(list[3].trim()));
//				close.add(Double.parseDouble(list[4].trim()));
//				adj_close.add(Double.parseDouble(list[5].trim()));
//				vol.add(Double.parseDouble(list[6].trim()));
//			}
//
//			// To add headers
//			StringBuilder head_line = new StringBuilder();
//
//			// Getting necessary stuffs
//			// Moving Average
//			// I'll add this along with the rsi values
//			ma = use.smoothy(open);
//
//			// RSI values
//			rsiValues = getRSI(open, N);
//
//			// Header added manually
//			head_line.append("Date").append(",").append("RSI values").append(",").append("Open price").append(",")
//					.append("Moving Average"); // Not yet next line
//
//			str.append(head_line).append("\n");
//			for (int i = 0; i < rsiValues.size(); i++) {
//				Double rsi_val = use.format(rsiValues.get(i));
//				Double ma_val = use.format(ma.get(i));
//				str.append(date.get(i)).append(",").append(rsi_val).append(",").append(open.get(i)).append(",")
//						.append(ma_val).append("\n");
//			}
//
//			write.write(str.toString());
//			write.close();
//			br.close();
//
//		} catch (IOException e) {
//			// Handle exception if necessary
//			e.printStackTrace();
//		}
	}

	/*
	 * The equivalent of run() Intentionally not rounding the money, I don't want to
	 * go to jail for tax fraud....At least not yet
	 */
	public void start() throws ParseException {
		getInfo();
		balance = start_Balance();
		algo1();
		algo2();
		algo3();
	}

	/*
	 * Algorithm 1, a stock broker bought the shares of a company as soon as the
	 * company goes public They believe in the company so much that tthey went all
	 * in on the first day they decided no matter what they would sell all the
	 * stocks after 2 years and a half
	 */

	public void algo1() {

		log = use.writeFile("Activity_log_Algorithm_1_TheLongGame.csv");
		str = new StringBuilder();
		double bal = balance, worth;
		LocalDate buy_date = date.get(0);
		LocalDate[] sell_date = new LocalDate[4];
		double[] price = new double[4];
		price[0] = open.get(0);

		// Header
		str.append("Date").append(",").append("Networth").append("\n");

		// To test for 1-4 years of patient
		for (int i = 0; i < 4; i++) {
			sell_date[i] = buy_date.plusYears(i + 1);
		}

		int buyShare = (int) (bal / price[0]);
		bal -= buyShare * open.get(0);

		System.out.println("\nalgorithm 1:");

		for (int i = 0; i < date.size(); i++) {
			if (isWithinAWeek(sell_date[0], date.get(i))) {
				price[0] = open.get(i);
			}
			if (isWithinAWeek(sell_date[1], date.get(i))) {
				price[1] = open.get(i);
			}
			if (isWithinAWeek(sell_date[2], date.get(i))) {
				price[2] = open.get(i);
			}
			if (isWithinAWeek(sell_date[3], date.get(i))) {
				price[3] = open.get(i);
				break; // Break because it's the last date im searching for, no need to continue
			}

			worth = bal + (buyShare * close.get(i));
			str.append(date.get(i).format(formDate)).append(",").append(use.format(worth)).append("\n");
		}

		try {
			log.write(str.toString());
			log.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Original balance: $" + getBal());
		for (int i = 1; i < 5; i++) {
			double total = 0;
			total = bal + (buyShare * price[i - 1]);
			System.out.println("After waiting for " + i + " year(s), your total balance is: $" + use.format(total));
		}
	}

	// Algorithm 2
	// Uses MA and RSI values to determine selling and buying
	public void algo2() {

		log = use.writeFile("Activity_log_Algorithm_2_RSI_MA_Method.csv");
		str = new StringBuilder();
		double bal = balance, worth = 0;
		int buyShare = 0;
		int val;
		boolean stop = false;

		// Header
		str.append("Date").append(",").append("Networth").append("\n");

		// Since you said the first 28 bars aren't really usable with the rsi
		// But I tested it and it yeild more money so I'll include it
		System.out.println("\nalgorithm 2:");
		System.out.println("Original balance: $" + balance);
		for (int i = 0; i < date.size(); i++) {

			// Buy if the rsi value is going above 30 and the MA is lower than the current
			// price
			if (!stop) {
				if (rsiValues.get(i) > 30 && ma.get(i) < open.get(i) && rsiValues.get(i) < 70) {
					if (bal > open.get(i)) {
						val = tradeEvaluator(1, i, bal);
						if (val > 0) {
							bal -= val * open.get(i);
							buyShare += val;
						}
					}
				}
				// Sell if ris is < 70 and ma > current price
				else if (rsiValues.get(i) < 70 && (i > 0 && rsiValues.get(i - 1) > 70) && ma.get(i) > open.get(i)) {
					if (buyShare > 0) {
						val = tradeEvaluator(2, i, (double) buyShare);
						if (val > 0) {
							buyShare -= val;
							bal += val * open.get(i);
						}
					}
				}
			}

			// Since we calculate the price after a day of trading thus closing price
			worth = bal + (buyShare * close.get(i));
			if (balance * 2 <= worth) {
				if ((rsiValues.get(i) > rsiValues.get(i - 1) && (rsiValues.get(i - 1) > rsiValues.get(i - 2)))) {
				} else {
					stop = true;
				}
			}

			str.append(date.get(i).format(formDate)).append(",").append(use.format(worth)).append("\n");
		}

		try {
			log.write(str.toString());
			log.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (buyShare > 0) {
			worth += buyShare * close.get(close.size() - 1);
		}
		worth = use.format(worth);

		System.out.println("Using RSI and MA method, your end balance is: $" + use.format(worth));
	}

	/*
	 * Algorithm 3 Let's go crazy No backing out Go all in at first and sell when
	 * the price is higher than the price when bought or the rsi condition is met
	 * and repeat
	 */
	public void algo3() {
		log = use.writeFile("Activity_log_Algorithm_3_UsingModule.csv");
		str = new StringBuilder();
		double bal = balance, worth = 0;
		double buyPrice = open.get(0);
		int bought, buyShare = 0;

		bought = (int) (bal / buyPrice);
		buyShare += bought;
		bal -= (bought * buyPrice);

		boolean stop = false;
		// Header
		str.append("Date").append(",").append("Networth").append("\n");


		System.out.println("\nalgorithm 3:");
		System.out.println("Original balance: $" + balance);
		for (int i = 1; i < date.size(); i++) {

			// Buy if the rsi value is going above 30 and the MA is lower than the current
			// price
			if (!stop) {
				if (open.get(i) < buyPrice || rsiValues.get(i) > 30 && rsiValues.get(i) < 70) {
					bought = (int) (bal / open.get(i));
					buyShare += bought;
					buyPrice = open.get(i);
					bal -= bought * buyPrice;

				}
				// Sell if ris is < 70 and ma > current price
				else if (open.get(i) > buyPrice || rsiValues.get(i) > 70) {
					bal += buyShare * open.get(i);
					buyShare = 0;

				}
			}
			worth = bal + (buyShare * close.get(i));
			if (balance * 2 <= worth) {
				if ((rsiValues.get(i) > rsiValues.get(i - 1) && (rsiValues.get(i - 1) > rsiValues.get(i - 2)))) {
				} else
					stop = true;
			}

			str.append(date.get(i).format(formDate)).append(",").append(use.format(worth)).append("\n");

			if (balance * 2 <= worth) {
				if ((rsiValues.get(i) > rsiValues.get(i - 1) && (rsiValues.get(i - 1) > rsiValues.get(i - 2)))) {
				} else {
					stop = true;
				}
			}
		}

		try {
			log.write(str.toString());
			log.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (buyShare > 0) {
			worth += buyShare * open.get(open.size() - 1);
		}
		worth = use.format(worth);
		System.out.println("Live and die with the stock," + " your end balance is: $" + use.format(worth));
	}

	public void start_trading() throws IOException {
		balance = start_Balance();

	}

	// Evaluate the user input
	// bal = balance or stock amount
	public int tradeEvaluator(int input, int i, double bal) {
		Double current_price = use.format(open.get(i));
		int shares = 0;

		// buy and sell 30% at a time
		switch (input) {
		case 1: // Buy stock
			shares = (int) Math.min((bal * 0.3) / current_price, bal / current_price);
			return shares;
		case 2:
			// Sell stock
			return (int) (bal * 0.5);
		default: // No change
			return 0;
		}
	}

	//This to get the informations form the csv stock file
	public void getInfo() throws ParseException {
		try {
			balance = 0;
			stock = 0;
			use = new mainSystem();
			System.out.println("Enter file name:");
			fileNameIn = use.fixName(use.askName());
			BufferedReader br = use.readFile(fileNameIn);

			System.out.println("Enter the file name to export the RSI values");
			fileNameOut = use.fixName(use.askName());
			BufferedWriter write = use.writeFile(fileNameOut);
			boolean header = false;

			// Store respective values
			while ((line = br.readLine()) != null) {
				String[] list = line.split(",");
				if (header == false) {
					header = true;
					continue;
				}

				date.add(add_Date(list[0]));
				open.add(Double.parseDouble(list[1].trim()));
				high.add(Double.parseDouble(list[2].trim()));
				low.add(Double.parseDouble(list[3].trim()));
				close.add(Double.parseDouble(list[4].trim()));
				adj_close.add(Double.parseDouble(list[5].trim()));
				vol.add(Double.parseDouble(list[6].trim()));
			}

			// To add headers
			StringBuilder head_line = new StringBuilder();

			// Getting necessary stuffs
			// Moving Average
			// I'll add this along with the rsi values
			ma = use.smoothy(open);

			// RSI values
			rsiValues = getRSI(open, N);

			// Header added manually
			head_line.append("Date").append(",").append("RSI values").append(",").append("Open price").append(",")
					.append("Moving Average"); // Not yet next line

			str.append(head_line).append("\n");
			for (int i = 0; i < rsiValues.size(); i++) {
				Double rsi_val = use.format(rsiValues.get(i));
				Double ma_val = use.format(ma.get(i));
				str.append(date.get(i)).append(",").append(rsi_val).append(",").append(open.get(i)).append(",")
						.append(ma_val).append("\n");
			}

			write.write(str.toString());
			write.close();
			br.close();

		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			e.printStackTrace();
		} catch (IOException e) {
			// Handle exception if necessary
			e.printStackTrace();
		}
	}

	public double start_Balance() {
		System.out.println("Deposit your gamble amount (in USD):");
		balance = scan.nextDouble();
		return balance;
	}

	public double getBal() {
		return balance;
	}

	public int getStock() {
		return stock;
	}

	// Get the current date with customed format
	public String getDate(int i) {
		String today = date.get(i).format(formDate);
		return today;
	}

	// This format the dates to then be use later
	// Ex: compare dates
	public LocalDate add_Date(String list) throws ParseException {
		LocalDate current_date = parseDateFlexible(list, format1, format2, format3, format4, format5);

		return current_date;
	}

	// Thank to stackoverflow for this method
	// Test for all formats of dates
	private static LocalDate parseDateFlexible(String dateString, String... formats) {
		for (String format : formats) {
			try {
				return LocalDate.parse(dateString, DateTimeFormatter.ofPattern(format));
			} catch (DateTimeParseException e) {
			}
		}
		throw new IllegalArgumentException("Could not parse date: " + dateString);
	}

	// Thank to Stackoverflow
	// This method is to check if a date is within a week of a date in the data set
	// Because 1 year later the dates changes
	// I think
	private static boolean isWithinAWeek(LocalDate date1, LocalDate date2) {
		// Calculate the difference in days between the dates
		long daysBetween = 10;
		if (date1.getYear() == date2.getYear()) {
			if (date1.getMonth() == date2.getMonth()) {
				daysBetween = Math.abs(date1.until(date2).getDays());
			}
		}

		// Check if the absolute difference is less than or equal to 6 days
		// 6 should be enough
		return daysBetween <= 3;
	}

	// Get the RSI values
	public ArrayList<Double> getRSI(ArrayList<Double> price, int n) {
		ArrayList<Double> U = new ArrayList<>();
		ArrayList<Double> D = new ArrayList<>();
		ArrayList<Double> rsiVal = new ArrayList<>();
		ArrayList<Double> rs = new ArrayList<>();

		double change;

		// for the first value
		// Because my code need it for formatting
		// I either do this or remove some early days
		U.add(0.0);
		D.add(0.0);
		U.add(0.0);
		D.add(0.0);

		for (int i = 1; i < price.size(); i++) {
			change = price.get(i) - price.get(i - 1);
			if (change >= 0) {
				U.add(change);
				D.add(0.0);
			} else {
				U.add(0.0);
				D.add(Math.abs(change));
			}
		}
		rs = getRS(U, D, n);

		for (double ele : rs) {
			double rsi = 0;
			if (ele == 0) {
				rsiVal.add(100.0);
			} else {
				rsi = (100 - (100 / (1 + ele)));
				rsiVal.add(rsi);
			}
		}
		return rsiVal;
	}

	// Get the rs values
	public ArrayList<Double> getRS(ArrayList<Double> up, ArrayList<Double> down, int n) {
		ArrayList<Double> rs = new ArrayList<>();
		for (int i = /* n + */ 1; i < up.size(); i++) {
			double avgU = 0, avgD = 0;
			for (int num = i; num > Math.max(0, i - n); num--) {
				avgU += up.get(num);
				avgD += down.get(num);
			}
			if (avgD == 0) {
				rs.add(0.0);
			} else
				rs.add(avgU / avgD);
		}
		return rs;
	}

	public boolean isPrime(int num) {
		if (num <= 1) {
			return false;
		}
		for (int i = 2; i <= Math.sqrt(num); i++) {
			if (num % i == 0) {
				return false;
			}
		}
		return true;
	}

}
