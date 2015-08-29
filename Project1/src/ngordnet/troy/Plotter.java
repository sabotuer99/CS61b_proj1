package ngordnet.troy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.ChartBuilder;
import com.xeiam.xchart.SwingWrapper;

/** Utility class for generating plots. */
public class Plotter {
	/**
	 * Creates a plot of the TimeSeries TS. Labels the graph with the given
	 * TITLE, XLABEL, YLABEL, and LEGEND.
	 */
	public static void plotTS(TimeSeries<? extends Number> ts, String title,
			String xlabel, String ylabel, String legend) {
//		ArrayList<Number> xValues = new ArrayList<Number>(ts.years());
//		ArrayList<Number> yValues = new ArrayList<Number>(ts.data());
//
//		Chart chart = new ChartBuilder().width(800).height(600)
//				.xAxisTitle(ylabel).yAxisTitle(xlabel).build();
//		// chart.getStyleManager().setYAxisLogarithmic(true);
//		// chart.getStyleManager().setXAxisLogarithmic(true);
//		chart.addSeries(legend, xValues, yValues);
//
//		// Show it
//		new SwingWrapper(chart).displayChart();
		HashMap<String, TimeSeries<? extends Number>> tsa = new HashMap<String, TimeSeries<? extends Number>>();
		tsa.put(legend, ts);
		plotTS(tsa, title, xlabel, ylabel);
	}

	// Private plot method actually does all the work
	private static void plotTS(HashMap<String, TimeSeries<? extends Number>> ts, String title,
			String xlabel, String ylabel) {

		Chart chart = new ChartBuilder().width(800).height(600)
				.xAxisTitle(ylabel).yAxisTitle(xlabel).build();
		// chart.getStyleManager().setYAxisLogarithmic(true);
		// chart.getStyleManager().setXAxisLogarithmic(true);
//		for (int i = 0; i < ts.size(); i++) {
//			chart.addSeries(legend, ts.get(i).years(), ts.get(i).data());
//		}
		for(String legend : ts.keySet()){
			chart.addSeries(legend, ts.get(legend).years(), ts.get(legend).data());
		}

		// Show it
		new SwingWrapper(chart).displayChart();
	}

	/**
	 * Creates a plot of the absolute word counts for WORD from STARTYEAR to
	 * ENDYEAR, using NGM as a data source.
	 */
	public static void plotCountHistory(NGramMap ngm, String word,
			int startYear, int endYear) {
		plotTS(ngm.countHistory(word, startYear, endYear),
				"Plot Count History", "year", "count", "count");

	}

	/**
	 * Creates a plot of the normalized weight counts for WORD from STARTYEAR to
	 * ENDYEAR, using NGM as a data source.
	 */
	public static void plotWeightHistory(NGramMap ngm, String word,
			int startYear, int endYear) {
		plotTS(ngm.weightHistory(word, startYear, endYear),
				"Plot Weighted History", "year", "count", "count");

	}

	/**
	 * Creates a plot of the processed history from STARTYEAR to ENDYEAR, using
	 * NGM as a data source, and the YRP as a yearly record processor.
	 */
	public static void plotProcessedHistory(NGramMap ngm, int startYear,
			int endYear, YearlyRecordProcessor yrp) {
		plotTS(ngm.processedHistory(startYear, endYear, yrp),
				"Processed History", "year", "count", "count");
	}

	/**
	 * Creates a plot of the total normalized count of
	 * WN.hyponyms(CATEGORYLABEL) from STARTYEAR to ENDYEAR using NGM and WN as
	 * data sources.
	 */
	public static void plotCategoryWeights(NGramMap ngm, WordNet wn,
			String categoryLabel, int startYear, int endYear) {
		TimeSeries<Double> ts = ngm.summedWeightHistory(
				wn.hyponyms(categoryLabel), startYear, endYear);
		plotTS(ts, "Category Weights", "year", "count", "count");
	}

	/**
	 * Creates overlaid category weight plots for each category label in
	 * CATEGORYLABELS from STARTYEAR to ENDYEAR using NGM and WN as data
	 * sources.
	 */
	public static void plotCategoryWeights(NGramMap ngm, WordNet wn,
			String[] categoryLabels, int startYear, int endYear) {
		//List<TimeSeries<? extends Number>> tsa = new ArrayList<TimeSeries<? extends Number>>();
		HashMap<String, TimeSeries<? extends Number>> tsa = new HashMap<String, TimeSeries<? extends Number>>();
		
		for (String categoryLabel : categoryLabels) {
			tsa.put(categoryLabel, ngm.summedWeightHistory(wn.hyponyms(categoryLabel), startYear, endYear));
		}

		plotTS(tsa, "Multi Category Weights", "year", "counts");
	}

	/**
	 * Makes a plot showing overlaid individual normalized count for every word
	 * in WORDS from STARTYEAR to ENDYEAR using NGM as a data source.
	 */
	public static void plotAllWords(NGramMap ngm, String[] words,
			int startYear, int endYear) {
		HashMap<String, TimeSeries<? extends Number>> tsa = new HashMap<String, TimeSeries<? extends Number>>();
		
		for (String word : words) {
			tsa.put(word, ngm.countHistory(word, startYear, endYear));
		}

		plotTS(tsa, "Multi Word Counts", "year", "counts");
	}

	/**
	 * Returns the numbers from max to 1, inclusive in decreasing order.
	 * Private, so you don't have to implement if you don't want to.
	 */
	@SuppressWarnings("unused")
	private static Collection<Number> downRange(int max) {
		return null;

	}

	/**
	 * Plots the count (or weight) of every word against the rank of every word
	 * on a log-log plot. Uses data from YEAR, using NGM as a data source.
	 */
	public static void plotZipfsLaw(NGramMap ngm, int year) {
		Chart chart = new ChartBuilder().width(800).height(600)
				.xAxisTitle("rank").yAxisTitle("count").build();
		 chart.getStyleManager().setYAxisLogarithmic(true);
		 chart.getStyleManager().setXAxisLogarithmic(true);

		ArrayList<Integer> xData = new ArrayList<Integer>();
		ArrayList<Integer> yData = new ArrayList<Integer>();
		YearlyRecord yr = ngm.getRecord(year);
		
		for (String word : yr.words()) {
			xData.add(yr.rank(word));
			yData.add(yr.count(word));
		}
		
		chart.addSeries("count", xData, yData);

		// Show it
		new SwingWrapper(chart).displayChart();
	}
}
