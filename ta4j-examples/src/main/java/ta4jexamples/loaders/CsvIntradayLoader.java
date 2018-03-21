package ta4jexamples.loaders;

import ta4jexamples.scraperPaths.*;
import au.com.bytecode.opencsv.CSVReader;
import org.joda.time.DateTime;
import eu.verdelhan.ta4j.Tick;
import eu.verdelhan.ta4j.TimeSeries;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by yony on 3/19/17.
 */
public class CsvIntradayLoader extends AbstractCsvLoader{

    private TimeSeries series = null;
    private final int MINIMUM_TICKS = 5;

    /**
     * @param symbol        String                  - Stock Symbol that we want to load.
     * @param market        String                  - Exchange market stock belongs to.
     * @param periodDays    int                     - Amount of days in which data will be sampled (maximum 50)
     * @param interval      IntradayTimeInterval    - time interval that suits google intraday API
     * @return              TimeSeries - Stock parameters from start date until most recent trade date.
     */
    public CsvIntradayLoader(String market, String symbol , int periodDays, GoogleIntradayCsvPath.IntradayTimeInterval interval) {

        // Create a List of ticks
        List<Tick> ticks = new ArrayList<>();

        // load History and Current data on CSV
        InputStream googleIntradayStreamLoader  = loadGoogleIntraday(market, symbol, periodDays, interval);
        ticks.addAll(loadGoogleIntradayTicks(googleIntradayStreamLoader));
        if (ticks.size() < MINIMUM_TICKS-1) {
            return;
        }
        Collections.reverse(ticks);

        // When there are no ticks, return NULL
        if (ticks.size()!=0) {
            this.series = new TimeSeries(market + " " + symbol + " Stock Ticks", ticks);
        }
    }

    /**
     * @param symbol        String                  - Stock Symbol that we want to load.
     * @param periodDays    int                     - Amount of days in which data will be sampled (maximum 50)
     * @param interval      IntradayTimeInterval    - time interval that suits google intraday API
     * @return              TimeSeries - Stock parameters from start date until most recent trade date.
     */
    public CsvIntradayLoader(String symbol , int periodDays, GoogleIntradayCsvPath.IntradayTimeInterval interval) {
        this("", symbol, periodDays, interval);
    }

    public TimeSeries getSeries() {
        return series;
    }

    //================================================================================================


    public static void main(String args[]) throws IOException {
        TimeSeries series = new CsvIntradayLoader("NASDAQ", "MSFT", 3, GoogleIntradayCsvPath.IntradayTimeInterval.OneH).getSeries();

        System.out.println("Series: " + series.getName() + " (" + series.getSeriesPeriodDescription() + ")");
        System.out.println("Number of ticks: " + series.getTickCount());
        System.out.println("First tick: \n"
                + "\tTick: " + series.getTick(0) + "\n"
                + "\tVolume: " + series.getTick(0).getVolume() + "\n"
                + "\tOpen price: " + series.getTick(0).getOpenPrice()+ "\n"
                + "\tClose price: " + series.getTick(0).getClosePrice());
        System.out.println("Second tick: \n"
                + "\tTick: " + series.getTick(1) + "\n"
                + "\tVolume: " + series.getTick(1).getVolume() + "\n"
                + "\tOpen price: " + series.getTick(1).getOpenPrice()+ "\n"
                + "\tClose price: " + series.getTick(1).getClosePrice());
        System.out.println("Last tick - 1: \n"
                + "\tTick: " + series.getTick(series.getTickCount()-2) + "\n"
                + "\tVolume: " + series.getTick(series.getTickCount()-2).getVolume() + "\n"
                + "\tOpen price: " + series.getTick(series.getTickCount()-2).getOpenPrice()+ "\n"
                + "\tClose price: " + series.getTick(series.getTickCount()-2).getClosePrice());
        System.out.println("Last tick: \n"
                + "\tTick: " + series.getLastTick() + "\n"
                + "\tVolume: " + series.getLastTick().getVolume() + "\n"
                + "\tOpen price: " + series.getLastTick().getOpenPrice()+ "\n"
                + "\tClose price: " + series.getLastTick().getClosePrice());
    }

}


