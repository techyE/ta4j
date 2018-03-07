package ta4jexamples.loaders;

import ta4jexamples.scraperPaths.*;
import au.com.bytecode.opencsv.CSVReader;
import org.joda.time.DateTime;
import eu.verdelhan.ta4j.Tick;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractCsvLoader implements CsvLoader {

    /**
     * @param csvCell   String - The value shown in the cell being checked.
     * @return String   Real value when int, "0" when "-" or "N/A"
     */
    public String cellChecker(String csvCell) {
        if (csvCell.equals("N/A") || csvCell.equals("-"))
            return "0";
        else
            return csvCell;
    }

    //=========================

    /**
     * @param market            String - Exchange market on which stock is being traded
     * @param symbol            String - Stock Symbol that we want to load.
     * @param startDate        DateTime - Start date that we want to start load from.
     * @return                  InputStream - data of Historical stock data from google
     */
    public InputStream loadGoogleHist(String market, String symbol , DateTime startDate) {
        ScraperPath uniquePath  = new GoogleHistCsvPath(market, symbol , startDate , new DateTime());
        String path             = uniquePath.getScraperPath();
        return loadStream(path);
    }

    /**
     * @param market        String                   - Stock Exchange
     * @param symbol        String                  - Stock Symbol that we want to load
     * @param periodDays    int                 - Amount of days in which data will be sampled (maximum 50)
     * @param interval      IntradayTimeInterval    - time interval that suits google intraday API
     * @return              InputStream - data of intraday stock data from google
     */
    public InputStream loadGoogleIntraday(String market, String symbol , int periodDays, GoogleIntradayCsvPath.IntradayTimeInterval interval) {
        ScraperPath uniquePath  = new GoogleIntradayCsvPath(market , symbol , periodDays, interval);
        String path             = uniquePath.getScraperPath();
        return loadStream(path);
    }

    /**
     * @param market            String - Exchange market on which stock is being traded
     * @param symbol            String - Stock Symbol that we want to load.
     * @param startDate        DateTime - Start date that we want to start load from.
     * @return                  InputStream - data of Historical stock data from google
     */
    public InputStream loadYahooHist(String market, String symbol , DateTime startDate, DateTime endDate,YahooHistCsvPath.YahooHistInterval interval) {
        ScraperPath uniquePath  = new YahooHistCsvPath(market, symbol , startDate , endDate, interval);
        String path             = uniquePath.getScraperPath();
        return loadStream(path);
    }

    /**
     * @param market            String - Exchange market on which stock is being traded
     * @param symbol            String - Stock Symbol that we want to load.
     * @return                  InputStream - data of Most Recent stock data from Yahoo
     */
    public InputStream loadYahooCurr(String market, String symbol){
        ScraperPath uniquePath  = new YahooCurrCsvPath(market, symbol);
        String path             = uniquePath.getScraperPath();
        return loadStream(path);
    }

    //===============================

    /**
     * @param path String - Contains a URL or a path to a static CSV file.
     * @return InputStream of path or NULL when can't open path
     */
    public static InputStream loadStream(String path) {
        InputStream stream;

        // Try Open A connection using HTTP URL, Or CSV file.
        try{
            stream = new URL(path).openConnection().getInputStream();
            return stream;
        }
        catch (FileNotFoundException ex){
            Logger.getLogger(AbstractCsvLoader.class.getName()).log(Level.WARNING, "Code 404 - Website doesn't exist", ex);
            return null;
        }
        catch (MalformedURLException ex){
            stream = CsvTicksLoader.class.getClassLoader().getResourceAsStream(path);
            return stream;
        } catch (ConnectException e){
            Logger.getLogger(AbstractCsvLoader.class.getName()).log(Level.WARNING, "Bad Response, Please try another time.", e);
            return null;
        } catch (IOException ex){
            Logger.getLogger(AbstractCsvLoader.class.getName()).log(Level.WARNING, "Bad Response, Please try another time.", ex);
            return null;
        }
    }

    //=============================================

    public List<Tick> loadGoogleHistTicks(InputStream stream){
        return loadCsvTicks(stream, 1, "dd-MMM-yy", 0,1,4,2,3,5);
    }
    public List<Tick> loadGoogleIntradayTicks(InputStream stream){
        return loadCsvTicks(stream, 7, "yyyy", 0,4,1,2,3,5);
    }
    public List<Tick> loadYahooHistTicks(InputStream stream){
        return loadCsvTicks(stream, 1, "yy-MM-dd", 0,1,4,2,3,5);
    }

    public List<Tick> loadYahooCurrTick(InputStream stream){
        return loadCsvTicks(stream, 0, "MM/dd/yyyy", 0,1,4,2,3,5);
        // DateFormat can be change to: "MM/dd/yyyy hh:mma"
        // Then parser should parse: line[0] + " " + line[6])
    }

    public List<Tick> loadCsvTicks(InputStream stream, int headerSizeToSkip, String dateFormat, int dateCol, int openCol, int closeCol, int highCol, int lowCol, int volumeCol){
        final SimpleDateFormat DATE_FORMAT  = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
        final int HEADER_TO_SKIP            = headerSizeToSkip;

        // Create a List of ticks
        List<Tick> ticks = new ArrayList<Tick>();
        InputStreamReader streamReader;
        try {
            streamReader = new InputStreamReader(stream, Charset.forName("UTF-8"));
        }
        catch (NullPointerException ex){
            return ticks;
        }

        CSVReader streamCsv = new CSVReader(streamReader, ',', '"', HEADER_TO_SKIP);

        String[] line;
        DateTime date = new DateTime();
        double open;
        double high;
        double low;
        double close;
        double volume;

        // Read Historical Stock Value GoogleHist
        try {
            // Read historical data date by date.
            while ((line = streamCsv.readNext()) != null) {
                try {
                    try { // Try JoNeS.loaders.CsvHistoricalLoader Parsing
                        date = new DateTime(DATE_FORMAT.parse(line[dateCol]));
                    } catch (ParseException e) {    // Try JoNeS.loaders.CsvIntradayLoader Parsing
                        // Takes Unix timestamp converts it to 'DateTime' timestamp
                        date = new DateTime((Integer.parseInt(line[dateCol].substring(1)))*1000L);
                    }
                    open    = Double.parseDouble(cellChecker(line[openCol]));
                    high    = Double.parseDouble(cellChecker(line[highCol]));
                    low     = Double.parseDouble(cellChecker(line[lowCol]));
                    close   = Double.parseDouble(cellChecker(line[closeCol]));
                    volume  = Double.parseDouble(cellChecker(line[volumeCol]));
                    ticks.add(0, new Tick(date, open, high, low, close, volume));
                } catch (NumberFormatException ex) {
                    try {
                        Logger.getLogger(AbstractCsvLoader.class.getName()).log(Level.WARNING, "Error while parsing value: \n" +
                                line[dateCol] + "," + line[openCol] + "," + line[highCol] + "," + line[lowCol] + "," + line[closeCol] + "," + line[volumeCol] +
                                "\n If everything seems to be fine - might be DateTime Threading problem => SEVERE", ex);
                    }catch (IndexOutOfBoundsException e){/*Line Is Bad*/}
                        continue;
                } catch (ArrayIndexOutOfBoundsException ex) {
                    Logger.getLogger(AbstractCsvLoader.class.getName()).log(Level.WARNING, "Error while parsing value: \n" +
                           line[dateCol] + "," + line[openCol] + "," + line[highCol] + "," + line[lowCol] + "," + line[closeCol] + "," + line[volumeCol] +
                            "\n If everything seems to be fine - might be DateTime Threading problem => SEVERE", ex);
                    continue;
                }
            }
            streamCsv.close();
        } catch (IOException ex) {
            Logger.getLogger(AbstractCsvLoader.class.getName()).log(Level.SEVERE, "Unable to load ticks from CSV", ex);
        }

        return ticks;
    }

}
