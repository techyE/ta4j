package ta4jexamples.loaders;

import java.util.ArrayList;
import java.util.List;

import static ta4jexamples.loaders.CsvFileLoader.loadFileCSV;

/**
 * <p>
 * Created by yony on 4/1/17.                                   <br>
 * Handles a CSV file contains a list of publicly traded companies generated using Nasdaq Website  <br>
 * Structure:                                                   <br>
 * Symbol | Company Name | Price | Cap Size | ?ADR/TSO? | IPO Year | Sector | Industry | Quote on Nasdaq Web    <br>
 * @see <a href=http://www.nasdaq.com/screening/company-list.aspx>Link</a>
 * </p>
 * @deprecated - Using a general method {@link CsvCompanyListLoader}
 */
public class CsvNasdaqListLoader{

    private static final int HEADER_SIZE_SKIP   = 1;
    private static final int SYMBOLS_COL        = 0;
    private static final int PRICE_COL          = 2;
    private static final int CAP_SIZE_COL       = 3;

    private List<String[]> csvList;

    /**
     * Constructor that loads CSV file and holds basic information about companies in a specific exchange.
     * @param nasdaqListCsvPath     String - path to CSV file that was received from Nasdaq website.
     */
    public CsvNasdaqListLoader(String nasdaqListCsvPath){
        csvList = loadFileCSV(nasdaqListCsvPath, HEADER_SIZE_SKIP);
        List<String[]>  deletedLines = new ArrayList<>();

        // Remove all the weird records / undefined
        for (String[] line : csvList) {
            if (Double.parseDouble(line[CAP_SIZE_COL]) == 0){
                deletedLines.add(line);
            }
        }
        for (String[] line : deletedLines) {
            csvList.remove(line);
        }
    }

    /**
     * Converts the List of companies info to a list that contains only the symbols of companies.
     * @return  List<String> - Stock Symbols list.
     */
    public List<String> loadSymbols(){
        List<String>    symbolsList = new ArrayList<>();

        for (String[] line : csvList) {
            symbolsList.add(line[SYMBOLS_COL].replaceAll("\\s",""));    // Also making sure there are no 'Spaces' in String
        }
        return symbolsList;
    }

    /**
     * Filter stocks with higher price than the one given.
     * @param price     int - Minimum price threshold.
     */
    public void filterHigherPrices(int price){
        List<String[]>  deletedLines = new ArrayList<>();

        // Find only Stocks with higher price.
        for (String[] line : csvList) {
            if (Double.parseDouble(line[PRICE_COL]) < price){
                deletedLines.add(line);
            }
        }
        for (String[] line : deletedLines) {
            csvList.remove(line);
        }
    }

    /**
     * Filter stocks with lower price than the one given.
     * @param price     int - Maximum price threshold.
     */
    public void filterLowerPrices(int price){
        List<String[]>  deletedLines = new ArrayList<>();

        // Find only Stocks with higher price.
        for (String[] line : csvList) {
            if (Double.parseDouble(line[PRICE_COL]) > price){
                deletedLines.add(line);
            }
        }
        for (String[] line : deletedLines) {
            csvList.remove(line);
        }
    }

    /**
     * Filter stocks with bigger market cap than the one given.
     * @param capSize     int - Minimum market cap threshold.
     */
    public void filterBiggerCap(int capSize){
        List<String[]>  deletedLines = new ArrayList<>();

        // Find only Stocks with higher price.
        for (String[] line : csvList) {
            if (Double.parseDouble(line[CAP_SIZE_COL]) < capSize){
                deletedLines.add(line);
            }
        }
        for (String[] line : deletedLines) {
            csvList.remove(line);
        }
    }

    /**
     * Filter stocks with smaller market cap than the one given.
     * @param capSize     int - Maximum market cap threshold.
     */
    public void filterSmallerCap(int capSize){
        List<String[]>  deletedLines = new ArrayList<>();

        // Find only Stocks with higher price.
        for (String[] line : csvList) {
            if (Double.parseDouble(line[CAP_SIZE_COL]) > capSize){
                deletedLines.add(line);
            }
        }
        for (String[] line : deletedLines) {
            csvList.remove(line);
        }
    }

    public void setCsvList(List<String[]> list) {
        csvList = list;
    }

    public List<String[]> getCsvList() {
        return csvList;
    }

    //==========================================================================

    public static void main(String args[]) {
//        List<String>    symbolsList = loadSymbols();
//        System.out.println(symbolsList.get(0));

        CsvNasdaqListLoader nyseList = new CsvNasdaqListLoader("nyseList2017.csv");
        nyseList.filterHigherPrices(700);

        // Print columns
        for (String[] line : nyseList.getCsvList()) {
            System.out.println(line[0]);
        }
    }
}